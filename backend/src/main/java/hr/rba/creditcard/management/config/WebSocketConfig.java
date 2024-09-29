package hr.rba.creditcard.management.config;

import hr.rba.creditcard.management.websocket.CardStatusWebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    private final CardStatusWebSocketHandler cardStatusWebSocketHandler;

    public WebSocketConfig(CardStatusWebSocketHandler cardStatusWebSocketHandler) {
        this.cardStatusWebSocketHandler = cardStatusWebSocketHandler;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(cardStatusWebSocketHandler, "/card-status-updates")
                .setAllowedOrigins("*");
    }

}
