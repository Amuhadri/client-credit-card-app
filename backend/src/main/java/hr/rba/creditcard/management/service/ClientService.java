package hr.rba.creditcard.management.service;

import hr.rba.creditcard.management.entity.Client;
import hr.rba.creditcard.management.repository.ClientRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClientService {

    private static final Logger logger = LoggerFactory.getLogger(ClientService.class);

    @Autowired
    private ClientRepository clientRepository;

    public List<Client> findAllClients() {
        logger.info("Fetching all clients");
        List<Client> clients = clientRepository.findAll();
        logger.info("Found {} clients", clients.size());
        return clients;
    }

}
