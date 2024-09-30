package hr.rba.creditcard.management.service;

import hr.rba.creditcard.management.entity.Client;
import hr.rba.creditcard.management.repository.ClientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ClientServiceTest {

    @Mock
    private ClientRepository clientRepository;

    @InjectMocks
    private ClientService clientService;

    private Client client;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        client = new Client();
        client.setId(1L);
        client.setOib("12345678901");
        client.setFirstName("John");
        client.setLastName("Doe");
        client.setCardStatus("ACTIVE");
    }

    @Test
    public void testFindAllClients() {
        when(clientRepository.findAll()).thenReturn(Arrays.asList(client));

        List<Client> clients = clientService.findAllClients();

        assertNotNull(clients);
        assertEquals(1, clients.size());
        assertEquals("12345678901", clients.get(0).getOib());
    }

    @Test
    public void testFindClientByOibSuccess() {
        when(clientRepository.findByOib("12345678901")).thenReturn(Optional.of(client));

        Optional<Client> foundClient = clientService.findClientByOib("12345678901");

        assertTrue(foundClient.isPresent());
        assertEquals("12345678901", foundClient.get().getOib());
    }

    @Test
    public void testFindClientByOibNotFound() {
        when(clientRepository.findByOib("12345678901")).thenReturn(Optional.empty());

        Optional<Client> foundClient = clientService.findClientByOib("12345678901");

        assertFalse(foundClient.isPresent());
    }

    @Test
    public void testSaveClient() {
        when(clientRepository.save(any(Client.class))).thenReturn(client);

        Client savedClient = clientService.saveClient(client);

        assertNotNull(savedClient);
        assertEquals("12345678901", savedClient.getOib());
        verify(clientRepository, times(1)).save(client);
    }

    @Test
    public void testUpdateClientSuccess() {
        Client updatedClient = new Client();
        updatedClient.setFirstName("Jane");
        updatedClient.setLastName("Smith");
        updatedClient.setCardStatus("INACTIVE");

        when(clientRepository.findByOib("12345678901")).thenReturn(Optional.of(client));
        when(clientRepository.save(any(Client.class))).thenReturn(client);

        Client result = clientService.updateClient("12345678901", updatedClient);

        assertNotNull(result);
        assertEquals("Jane", result.getFirstName());
        assertEquals("Smith", result.getLastName());
        assertEquals("INACTIVE", result.getCardStatus());
        verify(clientRepository, times(1)).save(client);
    }

    @Test
    public void testUpdateClientNotFound() {
        Client updatedClient = new Client();
        updatedClient.setFirstName("Jane");
        updatedClient.setLastName("Smith");
        updatedClient.setCardStatus("INACTIVE");

        when(clientRepository.findByOib("12345678901")).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> {
            clientService.updateClient("12345678901", updatedClient);
        });

        assertEquals("Client not found", exception.getMessage());
        verify(clientRepository, never()).save(any(Client.class));
    }

    @Test
    public void testDeleteClientByOibSuccess() {
        when(clientRepository.findByOib("12345678901")).thenReturn(Optional.of(client));

        clientService.deleteClientByOib("12345678901");

        verify(clientRepository, times(1)).delete(client);
    }

    @Test
    public void testDeleteClientByOibNotFound() {
        when(clientRepository.findByOib("12345678901")).thenReturn(Optional.empty());

        clientService.deleteClientByOib("12345678901");

        verify(clientRepository, never()).delete(any(Client.class));
    }
}
