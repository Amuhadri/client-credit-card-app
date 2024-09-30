package hr.rba.creditcard.management.controller;

import hr.rba.creditcard.management.entity.Client;
import hr.rba.creditcard.management.service.ClientService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

        try {
            List<Client> clients = clientService.findAllClients();

            if (clients.isEmpty()) {
                logger.warn("No clients found");
                return new ResponseEntity<>(clients, HttpStatus.NO_CONTENT); // Returns 204 when no clients are found
            }

            logger.info("Found {} clients", clients.size());
            return new ResponseEntity<>(clients, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error occurred while retrieving clients: {}", e.getMessage(), e);
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{oib}")
    public ResponseEntity<Object> getClientByOib(@PathVariable String oib) {
        logger.info("Received request to get client by OIB: {}", oib);

        try {
            Optional<Client> client = clientService.findClientByOib(oib);

            if (client.isPresent()) {
                logger.info("Client found for OIB: {}", oib);
                return new ResponseEntity<>(client.get(), HttpStatus.OK);
            } else {
                logger.warn("Client not found for OIB: {}", oib);
                return new ResponseEntity<>("Client not found", HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            logger.error("Error occurred while retrieving client for OIB {}: {}", oib, e.getMessage(), e);
            return new ResponseEntity<>("An error occurred while processing your request", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping
    public ResponseEntity<Client> createClient(@RequestBody Client client) {
        logger.info("Received request to create new client: {}", client);

        try {
            Client savedClient = clientService.saveClient(client);
            logger.info("Client created successfully: {}", savedClient);
            return new ResponseEntity<>(savedClient, HttpStatus.CREATED);
        } catch (Exception e) {
            logger.error("Error occurred while creating client: {}", e.getMessage(), e);
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{oib}")
    public ResponseEntity<Client> updateClient(@PathVariable String oib, @RequestBody Client updatedClient) {
        logger.info("Received request to update client with OIB: {}", oib);

        try {
            Client client = clientService.updateClient(oib, updatedClient);
            logger.info("Client with OIB {} updated successfully: {}", oib, client);
            return new ResponseEntity<>(client, HttpStatus.OK);
        } catch (RuntimeException e) {
            logger.error("Client with OIB {} not found, update failed", oib);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{oib}")
    public ResponseEntity<String> deleteClientByOib(@PathVariable String oib) {
        logger.info("Received request to delete client with OIB: {}", oib);

        Optional<Client> client = clientService.findClientByOib(oib);
        if (client.isPresent()) {
            clientService.deleteClientByOib(oib);
            logger.info("Client with OIB {} deleted successfully", oib);
            return new ResponseEntity<>("Client deleted successfully", HttpStatus.OK);
        } else {
            logger.warn("Client with OIB {} not found, deletion failed", oib);
            return new ResponseEntity<>("Client not found", HttpStatus.NOT_FOUND);
        }
    }
}
