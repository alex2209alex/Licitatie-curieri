package ro.fmi.unibuc.licitatie_curieri.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;
import ro.fmi.unibuc.licitatie_curieri.common.exception.InternalServerErrorException;
import ro.fmi.unibuc.licitatie_curieri.common.security.JwtFilter;
import ro.fmi.unibuc.licitatie_curieri.common.utils.ErrorMessageUtils;
import ro.fmi.unibuc.licitatie_curieri.controller.realtime.models.OfferDto;
import ro.fmi.unibuc.licitatie_curieri.controller.realtime.models.OfferStatusDto;
import ro.fmi.unibuc.licitatie_curieri.service.OrderService;

import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.security.Key;
import java.util.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class WebSocketOrderHandler implements WebSocketHandler {
    private static final String SECRET_KEY = "MOPS";
    private final OrderService orderService;
    private final ObjectMapper objectMapper;

    private final Set<WebSocketSession> sessions = new HashSet<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        log.info("Connection established, with session: {}", session.getId());
        sessions.add(session);
    }

    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) {
        val payload = (String) message.getPayload();

        try {
            if (payload == null) {
                sendErrorMessage(session);
            }

            OfferDto offerDto = objectMapper.readValue(payload, new TypeReference<>() {});
            setAuthFromToken(offerDto.getToken());

            log.info("Offer received for order with Id: " + offerDto.getOrderId() + " and delivery price: " + offerDto.getDeliveryPrice() + " and token: " + offerDto.getToken() + ", in session: " + session.getId());
            if (offerDto.getDeliveryPrice() < 0) {
                sendErrorMessage(session);
            }
            val offerResponseDto = orderService.makeOffer(offerDto);
            if (OfferStatusDto.ACCEPTED == offerResponseDto.getOfferStatus()) {
                sendOrderUpdateToAll(objectMapper.writeValueAsString(offerResponseDto));
            } else {
                sendErrorMessage(session);
            }
        } catch (Exception exception) {
            sendErrorMessage(session);
        }
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) {
        log.info("Transport error: " + exception.getMessage() + ", in session: " + session.getId());
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) {
        log.info("Connection closed with session: " + session.getId() + ", with status: " + closeStatus.getCode());
        sessions.remove(session);
    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }

    private void sendErrorMessage(WebSocketSession session) {
        try {
            session.sendMessage(new TextMessage(ErrorMessageUtils.INVALID_OFFER_MESSAGE));
        } catch (IOException exception) {
            throw new InternalServerErrorException(ErrorMessageUtils.ERROR_MESSAGE_FAILED_TO_BE_SENT_VIA_WEB_SOCKET);
        }
    }

    private void sendOrderUpdateToAll(String orderUpdateMessage) {
        for (val session : sessions) {
            try {
                session.sendMessage(new TextMessage(orderUpdateMessage));
            } catch (IOException exception) {
                log.info("Order update failed for session with id: " + session.getId());
            }
        }
    }

    private void setAuthFromToken(String token)
    {
        byte[] apiKeySecretBytes = Base64.getEncoder().encode(SECRET_KEY.getBytes());
        Key signingKey = new SecretKeySpec(apiKeySecretBytes, SignatureAlgorithm.HS512.getJcaName());

        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(signingKey)
                    .parseClaimsJws(token)
                    .getBody();

            String userId = claims.getId();
            String userType = claims.getSubject();
            if (userId != null) {
                List<GrantedAuthority> authorities = Collections.emptyList();
                UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                        userId, userType, authorities);
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        } catch (Exception e) {
            SecurityContextHolder.clearContext();
        }
    }

}
