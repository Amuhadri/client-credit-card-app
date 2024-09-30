package rba.hr.api.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rba.hr.api.entity.NewCardRequest;
import rba.hr.api.repository.CardRequestRepository;

@Service
public class CardRequestService {

    private static final Logger logger = LoggerFactory.getLogger(CardRequestService.class);
    @Autowired
    private CardRequestRepository cardRequestRepository;

    public NewCardRequest saveCardRequest(NewCardRequest newCardRequest) {
        try {
            NewCardRequest savedRequest = cardRequestRepository.save(newCardRequest);
            logger.info("New card request saved successfully: {}", newCardRequest);
            return savedRequest;
        } catch (Exception e) {
            logger.error("Error saving new card request: {}", e.getMessage());
            throw new RuntimeException("Failed to save card request", e);
        }
    }



}
