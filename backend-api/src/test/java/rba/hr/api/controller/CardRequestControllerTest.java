package rba.hr.api.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.beans.factory.annotation.Autowired;
import rba.hr.api.entity.NewCardRequest;
import rba.hr.api.service.CardRequestService;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CardRequestController.class)
public class CardRequestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CardRequestService cardRequestService;

    private NewCardRequest newCardRequest;

    @BeforeEach
    void setUp() {
        newCardRequest = new NewCardRequest();
        newCardRequest.setId(1L);
        newCardRequest.setFirstName("John");
        newCardRequest.setLastName("Doe");
        newCardRequest.setStatus("PENDING");
        newCardRequest.setOib("12345678901");
    }


    @Test
    void testCreateCardRequestSuccess() throws Exception {
        when(cardRequestService.saveCardRequest(any(NewCardRequest.class))).thenReturn(newCardRequest);

        String cardRequestJson = "{\"firstName\":\"John\", \"lastName\":\"Doe\", \"status\":\"PENDING\", \"oib\":\"12345678901\"}";

        mockMvc.perform(post("/api/v1/card-request")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(cardRequestJson))
                .andExpect(status().isCreated())
                .andExpect(content().string("New card request successfully created."));
    }

    @Test
    void testCreateCardRequestFailure() throws Exception {
        doThrow(new RuntimeException("Database error")).when(cardRequestService).saveCardRequest(any(NewCardRequest.class));

        String cardRequestJson = "{\"firstName\":\"John\", \"lastName\":\"Doe\", \"status\":\"PENDING\", \"oib\":\"12345678901\"}";

        mockMvc.perform(post("/api/v1/card-request")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(cardRequestJson))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void testGetAllCardRequestsSuccess() throws Exception {
        List<NewCardRequest> cardRequests = Arrays.asList(newCardRequest);

        when(cardRequestService.getAllCardRequests()).thenReturn(cardRequests);

        mockMvc.perform(get("/api/v1/card-requests"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].firstName").value("John"))
                .andExpect(jsonPath("$[0].lastName").value("Doe"))
                .andExpect(jsonPath("$[0].status").value("PENDING"))
                .andExpect(jsonPath("$[0].oib").value("12345678901"));
    }

    @Test
    void testGetAllCardRequestsFailure() throws Exception {
        when(cardRequestService.getAllCardRequests()).thenThrow(new RuntimeException("Database error"));

        mockMvc.perform(get("/api/v1/card-requests"))
                .andExpect(status().isInternalServerError());
    }
}
