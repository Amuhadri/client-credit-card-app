package hr.rba.creditcard.management.service;

import hr.rba.creditcard.management.entity.Client;
import hr.rba.creditcard.management.repository.ClientRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClientService {

    private static final Logger logger = LoggerFactory.getLogger(ClientService.class);

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private ExternalApiService externalApiService;

    public List<Client> findAllClients() {return clientRepository.findAll();}

    public Optional<Client> findClientByOib(String oib) {
        return clientRepository.findByOib(oib);
    }

    public Client saveClient(Client client) {
        return clientRepository.save(client);
    }

    public Client updateClient(String oib, Client updatedClient) {
        Optional<Client> existingClient = findClientByOib(oib);
        if (existingClient.isPresent()) {
            Client client = existingClient.get();
            client.setFirstName(updatedClient.getFirstName());
            client.setLastName(updatedClient.getLastName());
            client.setCardStatus(updatedClient.getCardStatus());
            return clientRepository.save(client);
        } else {
            throw new RuntimeException("Client not found");
        }
    }

    public void deleteClientByOib(String oib) {
        Optional<Client> client = clientRepository.findByOib(oib);
        client.ifPresent(clientRepository::delete);
    }

    public String sendClientData(String oib) {
        Optional<Client> client = findClientByOib(oib);
        if (client.isPresent()) {
            // Client found, try sending data to external API
            ResponseEntity<String> apiResponse = externalApiService.sendClientData(client.get());

            if (apiResponse.getStatusCode().is2xxSuccessful()) {
                return "Client found and successfully sent: " + client.get().toString() + "\nAPI Response: " + apiResponse.getBody();
            } else {
                return "Client found, but the API is unavailable or returned an error: " + apiResponse.getStatusCode() + " - " + apiResponse.getBody();
            }
        } else {
            throw new RuntimeException("Client not found");
        }
    }


}
