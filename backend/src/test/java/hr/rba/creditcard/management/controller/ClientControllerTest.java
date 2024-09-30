package hr.rba.creditcard.management.controller;

import hr.rba.creditcard.management.entity.Client;
import hr.rba.creditcard.management.service.ClientService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ClientController.class)
public class ClientControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ClientService clientService;

    private Client client;

    @BeforeEach
    void setUp() {
        client = new Client();
        client.setId(1L);
        client.setOib("12345678901");
        client.setFirstName("John");
        client.setLastName("Doe");
        client.setCardStatus("ACTIVE");
    }

    @Test
    void testGetAllClientsSuccess() throws Exception {
        when(clientService.findAllClients()).thenReturn(Arrays.asList(client));

        mockMvc.perform(get("/api/clients"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].oib").value("12345678901"))
                .andExpect(jsonPath("$[0].firstName").value("John"))
                .andExpect(jsonPath("$[0].lastName").value("Doe"))
                .andExpect(jsonPath("$[0].cardStatus").value("ACTIVE"));
    }

    @Test
    void testGetAllClientsNoContent() throws Exception {
        when(clientService.findAllClients()).thenReturn(Arrays.asList());

        mockMvc.perform(get("/api/clients"))
                .andExpect(status().isNoContent());
    }

    @Test
    void testGetClientByOibSuccess() throws Exception {
        when(clientService.findClientByOib("12345678901")).thenReturn(Optional.of(client));

        mockMvc.perform(get("/api/clients/12345678901"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.oib").value("12345678901"))
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"))
                .andExpect(jsonPath("$.cardStatus").value("ACTIVE"));
    }

    @Test
    void testGetClientByOibNotFound() throws Exception {
        when(clientService.findClientByOib("12345678901")).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/clients/12345678901"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Client not found"));
    }

    @Test
    void testCreateClientSuccess() throws Exception {
        when(clientService.saveClient(any(Client.class))).thenReturn(client);

        String clientJson = "{\"oib\":\"12345678901\", \"firstName\":\"John\", \"lastName\":\"Doe\", \"cardStatus\":\"ACTIVE\"}";

        mockMvc.perform(post("/api/clients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(clientJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.oib").value("12345678901"))
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"))
                .andExpect(jsonPath("$.cardStatus").value("ACTIVE"));
    }

    @Test
    void testUpdateClientSuccess() throws Exception {
        when(clientService.updateClient(eq("12345678901"), any(Client.class))).thenReturn(client);

        String updatedClientJson = "{\"oib\":\"12345678901\", \"firstName\":\"John\", \"lastName\":\"Doe\", \"cardStatus\":\"INACTIVE\"}";

        mockMvc.perform(put("/api/clients/12345678901")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedClientJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.oib").value("12345678901"))
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"))
                .andExpect(jsonPath("$.cardStatus").value("ACTIVE"));  // ili INACTIVE, u zavisnosti šta vraća update
    }

    @Test
    void testUpdateClientNotFound() throws Exception {
        when(clientService.updateClient(eq("12345678901"), any(Client.class)))
                .thenThrow(new RuntimeException("Client not found"));

        String updatedClientJson = "{\"oib\":\"12345678901\", \"firstName\":\"John\", \"lastName\":\"Doe\", \"cardStatus\":\"INACTIVE\"}";

        mockMvc.perform(put("/api/clients/12345678901")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedClientJson))
                .andExpect(status().isNotFound());
    }

    @Test
    void testDeleteClientByOibSuccess() throws Exception {
        when(clientService.findClientByOib("12345678901")).thenReturn(Optional.of(client));
        doNothing().when(clientService).deleteClientByOib("12345678901");

        mockMvc.perform(delete("/api/clients/12345678901"))
                .andExpect(status().isOk())
                .andExpect(content().string("Client deleted successfully"));
    }

    @Test
    void testDeleteClientByOibNotFound() throws Exception {
        when(clientService.findClientByOib("12345678901")).thenReturn(Optional.empty());

        mockMvc.perform(delete("/api/clients/12345678901"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Client not found"));
    }
}
