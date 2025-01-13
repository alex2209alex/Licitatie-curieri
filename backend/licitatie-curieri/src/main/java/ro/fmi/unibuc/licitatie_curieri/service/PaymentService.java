package ro.fmi.unibuc.licitatie_curieri.service;

import com.stripe.Stripe;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ro.fmi.unibuc.licitatie_curieri.common.exception.InternalServerErrorException;
import ro.fmi.unibuc.licitatie_curieri.common.utils.ErrorMessageUtils;
import ro.fmi.unibuc.licitatie_curieri.controller.stripe_payment.models.CreateIntentDto;
import ro.fmi.unibuc.licitatie_curieri.controller.stripe_payment.models.CreateIntentResponseDto;
import ro.fmi.unibuc.licitatie_curieri.domain.payment.mapper.PaymentMapper;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentService {
    private final PaymentMapper paymentMapper;
    private final String STRIPE_API_KEY = "";

    public CreateIntentResponseDto createPaymentIntent(CreateIntentDto createIntentDto){
        Stripe.apiKey = STRIPE_API_KEY;

        PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                .setAmount(createIntentDto.getAmount())
                .setCurrency(createIntentDto.getCurrency())
                .build();

        try {
            PaymentIntent paymentIntent = PaymentIntent.create(params);
            return paymentMapper.toCreateIntentResponseDto(paymentIntent.getClientSecret(), paymentIntent.getId());
        } catch (Exception e) {
            throw new InternalServerErrorException(String.format(ErrorMessageUtils.PAYMENT_ERROR) + e.getMessage());
        }
    }
}
