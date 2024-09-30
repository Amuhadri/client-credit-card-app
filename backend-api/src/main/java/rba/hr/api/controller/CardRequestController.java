package rba.hr.api.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import rba.hr.api.entity.NewCardRequest;
import rba.hr.api.service.CardRequestService;

@RestController
@RequestMapping("/api/v1")
public class CardRequestController {

    private static final Logger logger = LoggerFactory.getLogger(CardRequestController.class);

    @Autowired
    private CardRequestService cardRequestService;

    @PostMapping("/card-request")
    public ResponseEntity<?>createCardRequest(@RequestBody NewCardRequest newCardRequest) {
        try {
            logger.info("Received new card request: {}", newCardRequest);

            cardRequestService.saveCardRequest(newCardRequest);
            return new ResponseEntity<>("New card request successfully created.", HttpStatus.CREATED);

        } catch (Exception e) {
            logger.error("Failed to create new card request: {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}

