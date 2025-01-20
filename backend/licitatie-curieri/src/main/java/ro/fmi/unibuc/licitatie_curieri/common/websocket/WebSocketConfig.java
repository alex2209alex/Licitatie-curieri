package ro.fmi.unibuc.licitatie_curieri.common.websocket;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import ro.fmi.unibuc.licitatie_curieri.controller.WebSocketOrderHandler;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(webSocketHandler(), "/web-socket")
                .setAllowedOrigins("*");
    }

    @Bean
    WebSocketHandler webSocketHandler() {
        return new WebSocketOrderHandler();
    }
}
