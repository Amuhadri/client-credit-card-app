package hr.rba.creditcard.management.listener;

import hr.rba.creditcard.management.entity.Client;
import hr.rba.creditcard.management.service.ClientService;
import hr.rba.creditcard.management.websocket.CardStatusWebSocketHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.util.Optional;

import static org.mockito.Mockito.*;

public class CardStatusListenerTest {

    @Mock
    private ClientService clientService;

    @Mock
    private CardStatusWebSocketHandler webSocketHandler;

    @InjectMocks
    private CardStatusListener cardStatusListener;

    private Client client;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        client = new Client();
        client.setId(1L);
        client.setOib("12345678901");
        client.setFirstName("John");
        client.setLastName("Doe");
        client.setCardStatus("INACTIVE");
    }

    @Test
    public void testListenCardStatusSuccess() throws IOException {
        String message = "12345678901:ACTIVE";

        when(clientService.findClientByOib("12345678901")).thenReturn(Optional.of(client));

        cardStatusListener.listenCardStatus(message);

        verify(clientService).saveClient(client);
        verify(webSocketHandler).broadcastMessage("{\"oib\":\"12345678901\", \"firstName\":\"John\", \"lastName\":\"Doe\", \"cardStatus\":\"ACTIVE\"}");
    }

    @Test
    public void testListenCardStatusClientNotFound() throws IOException {
        String message = "12345678901:ACTIVE";

        when(clientService.findClientByOib("12345678901")).thenReturn(Optional.empty());

        cardStatusListener.listenCardStatus(message);

        verify(clientService, never()).saveClient(any(Client.class));
        verify(webSocketHandler, never()).broadcastMessage(anyString());
    }

    @Test
    public void testListenCardStatusErrorHandling() throws IOException {
        String message = "12345678901:ACTIVE";

        when(clientService.findClientByOib("12345678901")).thenThrow(new RuntimeException("Database error"));

        cardStatusListener.listenCardStatus(message);

        verify(clientService).findClientByOib("12345678901");
        verify(clientService, never()).saveClient(any(Client.class));
        verify(webSocketHandler, never()).broadcastMessage(anyString());
    }

    @Test
    public void testListenCardStatusInvalidMessageFormat() throws IOException {
        String invalidMessage = "InvalidMessageFormat";

        cardStatusListener.listenCardStatus(invalidMessage);

        verify(clientService, never()).findClientByOib(anyString());
        verify(clientService, never()).saveClient(any(Client.class));
        verify(webSocketHandler, never()).broadcastMessage(anyString());
    }
}
