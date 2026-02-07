package com.cal.yughistore.services.api;

import com.cal.yughistore.model.YughioCard;
import com.cal.yughistore.services.DTOs.DTOYughioCard;
import com.cal.yughistore.services.MonsterCardService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import tools.jackson.databind.JsonNode;

import java.util.List;

@Service
public class ApiService {
    private static final Logger logger = LoggerFactory.getLogger(ApiService.class);

    private final RestClient restClient;

    public ApiService(RestClient.Builder builder) {
        this.restClient = builder.baseUrl("https://db.ygoprodeck.com/api/v7/").build();
    }

    public JsonNode getInformationForAllCards() {
        try {
            // Use the get() method for an HTTP GET request
            JsonNode result = this.restClient.get()
                    .uri("/cardinfo.php")
                    .retrieve() // Start retrieving the body
                    .body(JsonNode.class); // Convert the body to the Quote class
            if (result != null) {
                logger.info("ApiService getAll results : {}", result);
                return result.get("data");

            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return null;
    }

    public JsonNode getInformationForNamedCard(String cardName) {
        try {
            // Use the get() method for an HTTP GET request
            JsonNode result = this.restClient.get()
                    .uri("/cardinfo.php?name=" + cardName)
                    .retrieve() // Start retrieving the body
                    .body(JsonNode.class); // Convert the body to the Quote class

            if (result != null) {
                logger.info("ApiService getAll results : {}", result);
                return result.get("data");

            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return null;
    }
}
