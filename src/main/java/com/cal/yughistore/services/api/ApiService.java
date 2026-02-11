package com.cal.yughistore.services.api;

import com.cal.yughistore.model.enums.EnumCardAttribute;
import com.cal.yughistore.repository.YughioCardRepository;
import com.cal.yughistore.services.DTOs.DTOYughioCard;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.ArrayList;
import java.util.List;

@Service
public class ApiService {
    private static final Logger logger = LoggerFactory.getLogger(ApiService.class);
    private final YughioCardRepository repository;

    private final RestClient restClient;
    private final ObjectMapper objectMapper;
    private final String url = "https://db.ygoprodeck.com/api/v7";

    public ApiService(YughioCardRepository repository, RestClient.Builder builder) {
        this.repository = repository;
        this.restClient = builder.baseUrl(url).build();
        this.objectMapper = new ObjectMapper();
    }

    private JsonNode apiGet(String path) {
        try {
            String json = restClient.get()
                    .uri(url+path)
                    .retrieve()
                    .body(String.class);

            JsonNode root = objectMapper.readTree(json);
            return root;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<DTOYughioCard> getInformationForAllCards() {
        try {
            JsonNode result = apiGet("/cardinfo.php");
            if (result != null) {
                List<DTOYughioCard> dtoList = new ArrayList<>();
                for (JsonNode node : result.get("data")) {
                    dtoList.add(DTOYughioCard.toDTO(node));
                }
                logger.info("ApiService getAll results : {}", result.get("data"));
                return dtoList;
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return null;
    }

    public List<DTOYughioCard> getInformationForAllCards(int numberOfCards) {
        try {
            // Use the get() method for an HTTP GET request
            JsonNode result = apiGet("/cardinfo.php?num=" + numberOfCards + "/offset=0");

            if (result != null) {
                List<DTOYughioCard> dtoList = new ArrayList<>();
                for (JsonNode node : result.get("data")) {
                    dtoList.add(DTOYughioCard.toDTO(node));
                }
                logger.info("ApiService getAll results : {}", result.get("data"));
                return dtoList;
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
                logger.info("ApiService getAll results : {}", result.get("data"));
                return result.get("data");

            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return null;
    }

    public DTOYughioCard getInformationForNamedCard(String cardName) {
        try {
            JsonNode result = apiGet("/cardinfo.php?name="+cardName);
            if (result != null) {
                DTOYughioCard cardDto = DTOYughioCard.toDTO(result.get("data").get(0));
                logger.info("ApiService getAll results : {}", cardDto);
                return cardDto;
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return null;
    }

    //Get all Level 4/RANK 4 Water cards and order by atk
    public JsonNode getInformationForAllWithLevelAttribOrderedByProperty(int level, EnumCardAttribute attribute, String property) {
        try {
            //TODO check to see attribut keeps underscore from enum name
            JsonNode result = apiGet("/cardinfo.php?level=" + level + "&attribute=" + attribute.name() + "&sort=" + property);

            if (result != null) {
                logger.info("ApiService getAll results : {}", result.get("data"));
                return result.get("data");

            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return null;
    }
}
