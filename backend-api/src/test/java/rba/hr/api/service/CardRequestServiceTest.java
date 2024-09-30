package rba.hr.api.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import rba.hr.api.entity.NewCardRequest;
import rba.hr.api.repository.CardRequestRepository;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CardRequestServiceTest {

    @Mock
    private CardRequestRepository cardRequestRepository;

    @InjectMocks
    private CardRequestService cardRequestService;

    private NewCardRequest newCardRequest;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        newCardRequest = new NewCardRequest();
        newCardRequest.setId(1L);
        newCardRequest.setFirstName("John");
        newCardRequest.setLastName("Doe");
        newCardRequest.setStatus("PENDING");
        newCardRequest.setOib("12345678901");
    }

    @Test
    public void testSaveCardRequestSuccess() {
        when(cardRequestRepository.save(any(NewCardRequest.class))).thenReturn(newCardRequest);

        NewCardRequest savedRequest = cardRequestService.saveCardRequest(newCardRequest);

        assertNotNull(savedRequest);
        assertEquals("12345678901", savedRequest.getOib());
        assertEquals("PENDING", savedRequest.getStatus());
        verify(cardRequestRepository, times(1)).save(newCardRequest);
    }

    @Test
    public void testSaveCardRequestFailure() {
        when(cardRequestRepository.save(any(NewCardRequest.class))).thenThrow(new RuntimeException("Database error"));

        Exception exception = assertThrows(RuntimeException.class, () -> {
            cardRequestService.saveCardRequest(newCardRequest);
        });

        assertEquals("Failed to save card request", exception.getMessage());
        verify(cardRequestRepository, times(1)).save(newCardRequest);
    }

    @Test
    public void testGetAllCardRequestsSuccess() {
        List<NewCardRequest> cardRequests = Arrays.asList(newCardRequest);

        when(cardRequestRepository.findAll()).thenReturn(cardRequests);

        List<NewCardRequest> result = cardRequestService.getAllCardRequests();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("12345678901", result.get(0).getOib());
        verify(cardRequestRepository, times(1)).findAll();
    }

    @Test
    public void testGetAllCardRequestsFailure() {
        when(cardRequestRepository.findAll()).thenThrow(new RuntimeException("Database error"));

        Exception exception = assertThrows(RuntimeException.class, () -> {
            cardRequestService.getAllCardRequests();
        });

        assertEquals("Failed to retrieve card requests", exception.getMessage());
        verify(cardRequestRepository, times(1)).findAll();
    }
}
