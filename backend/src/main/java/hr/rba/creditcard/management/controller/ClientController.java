package hr.rba.creditcard.management.controller;

import hr.rba.creditcard.management.entity.Client;
import hr.rba.creditcard.management.service.ClientService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/clients")
public class ClientController {

    private static final Logger logger = LoggerFactory.getLogger(ClientController.class);

    @Autowired
    private ClientService clientService;

    @GetMapping
    public ResponseEntity<List<Client>> getAllClients() {
        logger.info("Received request to get all clients");
        List<Client> clients = clientService.findAllClients();
        logger.info("Found {} clients", clients.size());
        return new ResponseEntity<>(clients, HttpStatus.OK);
    }

    @GetMapping("/{oib}")
    public ResponseEntity<Object> getClientByOib(@PathVariable String oib) {
        logger.info("Received request to get client by OIB: {}", oib);

        Optional<Client> client = clientService.findClientByOib(oib);

        if (client.isPresent()) {
            return new ResponseEntity<>(client.get(), HttpStatus.OK);
        } else {
            logger.warn("Client not found for OIB: {}", oib);
            return new ResponseEntity<>("Client not found", HttpStatus.NOT_FOUND);
        }
    }
}
