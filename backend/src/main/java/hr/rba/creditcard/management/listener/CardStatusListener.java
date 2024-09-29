package hr.rba.creditcard.management.listener;

import hr.rba.creditcard.management.entity.Client;
import hr.rba.creditcard.management.service.ClientService;
import hr.rba.creditcard.management.websocket.CardStatusWebSocketHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class CardStatusListener {

    private static final Logger logger = LoggerFactory.getLogger(CardStatusListener.class);  // Definirajte logger

    @Autowired
    private ClientService clientService;

    @Autowired
    private CardStatusWebSocketHandler webSocketHandler;

    @KafkaListener(topics = "card-status-topic", groupId = "card-status-group")
    public void listenCardStatus(String message) {

        try {
            logger.info("Received Kafka message: {}", message);

            String[] parts = message.split(":");
            String oib = parts[0];
            String newStatus = parts[1];

            Optional<Client> clientOpt = clientService.findClientByOib(oib);
            if (clientOpt.isPresent()) {
                Client client = clientOpt.get();
                client.setCardStatus(newStatus);
                clientService.saveClient(client);

                String updateMessage = "{\"oib\":\"" + client.getOib() + "\", \"firstName\":\"" + client.getFirstName() + "\", \"lastName\":\"" + client.getLastName() + "\", \"cardStatus\":\"" + client.getCardStatus() + "\"}";
                webSocketHandler.broadcastMessage(updateMessage);

                logger.info("Updated client status: {}", client);
            } else {
                logger.warn("Client with OIB {} not found.", oib);
            }
        } catch (Exception e) {
            logger.error("Error processing Kafka message: {}. Error: {}", message, e.getMessage());
        }
    }
}