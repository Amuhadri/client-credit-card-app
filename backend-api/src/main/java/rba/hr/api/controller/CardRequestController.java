package rba.hr.api.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rba.hr.api.entity.NewCardRequest;
import rba.hr.api.service.CardRequestService;

import java.util.List;

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

    @GetMapping("/card-requests")
    public ResponseEntity<List<NewCardRequest>> getAllCardRequests() {
        try {
            List<NewCardRequest> cardRequests = cardRequestService.getAllCardRequests();
            return new ResponseEntity<>(cardRequests, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Failed to fetch card requests: {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}

