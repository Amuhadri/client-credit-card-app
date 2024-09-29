package hr.rba.creditcard.management.service;

import hr.rba.creditcard.management.entity.Client;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class ExternalApiService {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${external.api.url}")
    private String externalApiUrl;

    // Method to send client data to external API
    public ResponseEntity<String> sendClientData(Client client) {
        String url = externalApiUrl + "/api/v1/card-request";
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");

        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("firstName", client.getFirstName());
        requestBody.put("lastName", client.getLastName());
        requestBody.put("status", client.getCardStatus());
        requestBody.put("oib", client.getOib());

        HttpEntity<Map<String, String>> request = new HttpEntity<>(requestBody, headers);
        return restTemplate.exchange(url, HttpMethod.POST, request, String.class);
    }
}
