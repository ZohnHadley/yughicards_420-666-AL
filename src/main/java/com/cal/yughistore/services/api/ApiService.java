package com.cal.yughistore.services.api;

import com.cal.yughistore.ConsoleLoadingBar;
import com.cal.yughistore.repository.YughioCardRepository;
import com.cal.yughistore.services.dto.yughiocard.YughioCardDTO;
import com.cal.yughistore.services.yughiocard.YughioCardService;
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
    private final ConsoleLoadingBar consoleLoadingBar = new ConsoleLoadingBar();
    private static final Logger logger = LoggerFactory.getLogger(ApiService.class);
    private final YughioCardRepository cardRepository;
    private final YughioCardService yughioCardService;
    private final RestClient restClient;
    private final ObjectMapper objectMapper;
    private final String url = "https://db.ygoprodeck.com/api/v7";

    public ApiService(YughioCardRepository cardRepository, YughioCardService yughioCardService,
                      RestClient.Builder builder,
                      ObjectMapper objectMapper) {
        this.cardRepository = cardRepository;
        this.yughioCardService = yughioCardService;
        this.restClient = builder.baseUrl(url).build();
        this.objectMapper = objectMapper;
    }


    private String apiGet(String path) {
        try {
            return restClient.get()
                    .uri(path)
                    .retrieve()
                    .body(String.class);
        } catch (Exception e) {
            logger.error("Failed to fetch from api", e);
            return null;
        }
    }

    @PostConstruct
    public void init() {
        if (cardRepository.count() == 0) {
            loadApiCardData();
//            loadApiCardDataFromStaticFile();
        } else {
            logger.info("Cards already exist. Skipping API load.");
        }
    }

    private void loadApiCardData() {
        List<YughioCardDTO> dtoList = new ArrayList<>();
        try {
            logger.info("ApiService : Downloading all card data data from api");

            String result = apiGet("/cardinfo.php");
            JsonNode dataList = objectMapper.readTree(result).get("data");
            if (dataList != null && dataList.isArray() && !dataList.isEmpty()) {

                for (int index =0; index < dataList.size(); index++) {
                    JsonNode node = dataList.get(index);
                    YughioCardDTO cardDto = YughioCardDTO.of(node);
                    dtoList.add(cardDto);
                    consoleLoadingBar.printProgress(index + 1, dataList.size());
                }
                consoleLoadingBar.finish();

                yughioCardService.saveAll(dtoList);
            }
            return;
        } catch (Exception e) {
            logger.error("ApiService : failed to load all cards data from api {}", e.getMessage());

        }
        loadApiCardDataFromStaticFile();
    }

    private void loadApiCardDataFromStaticFile() {

        List<YughioCardDTO> dtoList = new ArrayList<>();

        try {
            logger.info("ApiService : loading card data from json file");

            InputStream is = getClass()
                    .getClassLoader()
                    .getResourceAsStream("static/cardinfo_eng.json");

            if (is == null) {
                throw new IllegalStateException("Static JSON file not found in resources");
            }

            JsonNode root = objectMapper.readTree(is);
            JsonNode dataList = root.get("data");

            for (int index =0; index < dataList.size(); index++) {
                JsonNode node = dataList.get(index);
                YughioCardDTO cardDto = YughioCardDTO.of(node);
                dtoList.add(cardDto);
                consoleLoadingBar.printProgress(index + 1, dataList.size());
            }
            consoleLoadingBar.finish();

            yughioCardService.saveAll(dtoList);

        } catch (Exception e) {
            logger.error("ApiService : failed to load all cards info from static file", e);
        }

    }

}
