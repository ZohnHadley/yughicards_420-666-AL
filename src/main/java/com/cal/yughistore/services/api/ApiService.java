package com.cal.yughistore.services.api;

import com.cal.yughistore.model.YughioCard;
import com.cal.yughistore.model.util.JsonUtil;
import com.cal.yughistore.repository.YughioCardRepository;
import com.cal.yughistore.services.DTOs.DTOYughioCard;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;

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
            logger.error("ApiService : failed to fetch from api {}", e.getMessage());
        }
        return null;
    }

    @PostConstruct
    public void init() {
        loadApiCardData();
    }

    public List<DTOYughioCard> loadApiCardData() {
        List<DTOYughioCard> dtoList = new ArrayList<>();
        List<YughioCard> cards = new ArrayList<>();
        try {
            logger.info("ApiService : trying to load all cards data from api");

            JsonNode result = apiGet("/cardinfo.php");
            if (result != null && !result.get("data").isEmpty()) {
                for (JsonNode node : result.get("data")) {
                    DTOYughioCard dto = DTOYughioCard.toDTO(node);
                    dtoList.add(dto);
                }
                repository.saveAll(cards);

                logger.info("ApiService : getAll responded? : {}", (!result.isEmpty()));
                return dtoList;
            }
        }
        catch (Exception e) {
            logger.error("ApiService : failed to load all cards data from api {}", e.getMessage());

        }
        return loadApiCardDataFromStaticFile();
    }

    private List<DTOYughioCard> loadApiCardDataFromStaticFile() {

        List<DTOYughioCard> dtoList = new ArrayList<>();
        List<YughioCard> cards = new ArrayList<>();

        try {
            logger.info("ApiService : loading from static file");
            Path filePath = Paths.get("src/main/resources/static/cardinfo.php.json");
            // Read the entire file content into a string
            String content = Files.readString(filePath);
            JsonNode dataList = (JsonUtil.getInstance().fromJson(content)).get("data");
            for(JsonNode node : dataList){
                DTOYughioCard dto = DTOYughioCard.toDTO(node);
                dtoList.add(dto);
            }
            repository.saveAll(cards);

            return dtoList;
        } catch (IOException e) {
            logger.error("ApiService : failed to load all cards info from static file {}", e.getMessage());
        }
        return  null;
    }
}
