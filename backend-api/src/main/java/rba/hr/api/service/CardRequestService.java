package rba.hr.api.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rba.hr.api.entity.NewCardRequest;
import rba.hr.api.repository.CardRequestRepository;

import java.util.List;

@Service
public class CardRequestService {

    private static final Logger logger = LoggerFactory.getLogger(CardRequestService.class);
    @Autowired
    private CardRequestRepository cardRequestRepository;

    public NewCardRequest saveCardRequest(NewCardRequest newCardRequest) {
        try {
            return cardRequestRepository.save(newCardRequest);
        } catch (Exception e) {
            throw new RuntimeException("Failed to save card request", e);
        }
    }

    public List<NewCardRequest> getAllCardRequests() {
        try {
            return cardRequestRepository.findAll();
        } catch (Exception e) {
            throw new RuntimeException("Failed to retrieve card requests", e);
        }
    }

}
