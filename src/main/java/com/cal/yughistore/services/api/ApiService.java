package com.cal.yughistore.services.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import tools.jackson.databind.JsonNode;

@Service
public class ApiService {
    private static final Logger logger = LoggerFactory.getLogger(ApiService.class);

    private final RestClient restClient;

    public ApiService(RestClient.Builder builder) {
        this.restClient = builder.baseUrl("https://db.ygoprodeck.com/api/v7/").build();
    }

    private JsonNode apiGet(String path) {
        return this.restClient.get()
                .uri(path)
                .retrieve() // Start retrieving the body
                .body(JsonNode.class);
    }

    public JsonNode getInformationForAllCards() {
        try {
            // Use the get() method for an HTTP GET request
            JsonNode result = apiGet("/cardinfo.php");

            if (result != null) {
                logger.info("ApiService getAll results : {}", result);
                return result.get("data");
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return null;
    }

    public JsonNode getInformationForAllCards(int numberOfCards) {
        try {
            // Use the get() method for an HTTP GET request
            JsonNode result = apiGet("/cardinfo.php?num=" + numberOfCards + "/offset=0");

            if (result != null) {
                logger.info("ApiService getAll results : {}", result);
                return result.get("data");

            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return null;
    }

    public JsonNode getInformationForAllCards(int numberOfCards, int offset) {
        try {
            // Use the get() method for an HTTP GET request
            JsonNode result = apiGet("/cardinfo.php?num=" + numberOfCards + "/offset=" + offset);

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
            JsonNode result = apiGet("/cardinfo.php?name=" + cardName);

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
