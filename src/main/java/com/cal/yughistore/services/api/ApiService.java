package com.cal.yughistore.services.api;

import com.cal.yughistore.model.YughioCard;
import com.cal.yughistore.model.properties.CardProperties;
import com.cal.yughistore.repository.CardPropertiesRepository;
import com.cal.yughistore.repository.YughioCardRepository;
import com.cal.yughistore.services.DTOs.DTOYughioCard;
import com.cal.yughistore.services.DTOs.Properties.DTOCardProperties;
import com.cal.yughistore.services.YughioCardService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Service
public class ApiService {
    private static final Logger logger = LoggerFactory.getLogger(ApiService.class);
    private final YughioCardRepository cardRepository;
    private final CardPropertiesRepository cardPropertiesRepository;
    private final YughioCardService yughioCardService;
    private final RestClient restClient;
    private final ObjectMapper objectMapper;
    private final String url = "https://db.ygoprodeck.com/api/v7";

    public ApiService(YughioCardRepository cardRepository, CardPropertiesRepository cardPropertiesRepository, YughioCardService yughioCardService,
                      RestClient.Builder builder,
                      ObjectMapper objectMapper) {
        this.cardRepository = cardRepository;
        this.cardPropertiesRepository = cardPropertiesRepository;
        this.yughioCardService = yughioCardService;
        this.restClient = builder.baseUrl(url).build();
        this.objectMapper = objectMapper;
    }


    private JsonNode apiGet(String path) {
        try {
            return restClient.get()
                    .uri(path)
                    .retrieve()
                    .body(JsonNode.class);
        } catch (Exception e) {
            logger.error("Failed to fetch from api", e);
            return null;
        }
    }

    @PostConstruct
    public void init() {
        if (cardRepository.count() == 0) {
//            loadApiCardData();
            loadApiCardDataFromStaticFile();
        } else {
            logger.info("Cards already exist. Skipping API load.");
        }
    }

    public void loadApiCardData() {
        List<DTOYughioCard> dtoList = new ArrayList<>();
        List<YughioCard> cards = new ArrayList<>();
        List<CardProperties> cardPropertiesList = new ArrayList<>();
        try {
            logger.info("ApiService : trying to load all cards data from api");

            JsonNode result = apiGet("/cardinfo.php");
            JsonNode data = result != null ? result.get("data") : null;
            if (data != null && data.isArray() && !data.isEmpty()) {
                for (JsonNode node : result.get("data")) {
                    DTOYughioCard dto = DTOYughioCard.of(node);
//                    cards.add(dto.toEntity());
                    dtoList.add(dto);
                }
                cardRepository.saveAll(cards);

                logger.info("ApiService : getAll responded? : {}", (!result.isEmpty()));
                return;
            }
        } catch (Exception e) {
            logger.error("ApiService : failed to load all cards data from api {}", e.getMessage());

        }
        loadApiCardDataFromStaticFile();
    }

    private void loadApiCardDataFromStaticFile() {

        List<DTOYughioCard> dtoList = new ArrayList<>();


        try {
            logger.info("ApiService : loading from static file");

            InputStream is = getClass()
                    .getClassLoader()
                    .getResourceAsStream("static/cardinfo.php.json");

            if (is == null) {
                throw new IllegalStateException("Static JSON file not found in resources");
            }

            JsonNode root = objectMapper.readTree(is);
            JsonNode dataList = root.get("data");

            for (JsonNode node : dataList) {
                DTOYughioCard cardDto = DTOYughioCard.of(node);
                dtoList.add(cardDto);
            }

            yughioCardService.saveAll(dtoList);

        } catch (Exception e) {
            logger.error("ApiService : failed to load all cards info from static file", e);
        }

    }

}
