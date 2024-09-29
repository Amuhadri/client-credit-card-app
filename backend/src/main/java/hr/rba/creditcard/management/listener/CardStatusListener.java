package hr.rba.creditcard.management.listener;

import hr.rba.creditcard.management.entity.Client;
import hr.rba.creditcard.management.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class CardStatusListener {
    @Autowired
    private ClientService clientService;

    @KafkaListener(topics = "card-status-topic", groupId = "card-status-group")
    public void listenCardStatus(String message) {

        try {
            String[] parts = message.split(":");
            String oib = parts[0];
            String newStatus = parts[1];

            Optional<Client> clientOpt = clientService.findClientByOib(oib);
            if (clientOpt.isPresent()) {
                Client client = clientOpt.get();
                client.setCardStatus(newStatus);
                clientService.saveClient(client);

                System.out.println("Updated client status: " + client);
            } else {
                System.err.println("Client with OIB " + oib + " not found.");
            }
        } catch (Exception e) {
            System.err.println("Error processing Kafka message: " + message + ". Error: " + e.getMessage());
        }
    }
}
