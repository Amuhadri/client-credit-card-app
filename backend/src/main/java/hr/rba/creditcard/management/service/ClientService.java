package hr.rba.creditcard.management.service;

import hr.rba.creditcard.management.entity.Client;
import hr.rba.creditcard.management.repository.ClientRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClientService {

    private static final Logger logger = LoggerFactory.getLogger(ClientService.class);

    @Autowired
    private ClientRepository clientRepository;

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

}
