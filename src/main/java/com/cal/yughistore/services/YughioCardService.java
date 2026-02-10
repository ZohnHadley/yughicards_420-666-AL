package com.cal.yughistore.services;
import com.cal.yughistore.model.YughioCard;
import com.cal.yughistore.model.util.JsonUtil;
import com.cal.yughistore.repository.YughioCardRepository;
import com.cal.yughistore.services.DTOs.DTOYughioCard;
import com.fasterxml.jackson.databind.JsonNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class YughioCardService {
    private static final Logger logger = LoggerFactory.getLogger(YughioCardService.class);
    private final YughioCardRepository repository;

    public YughioCardService(YughioCardRepository repository) {
        this.repository = repository;
    }

    public void loadFromStaticFile() {

        Path filePath = Paths.get("src/main/resources/static/cardinfo.php.json");
        try {

            // Read the entire file content into a string
            String content = Files.readString(filePath);
            JsonNode dataList = (JsonUtil.getInstance().fromJson(content)).get("data");


        } catch (IOException e) {
            // Handle potential I/O errors (e.g., file not found, permission issues)
            e.printStackTrace();
        }
    }

    public void save(DTOYughioCard card){
        if (card == null){
            throw new RuntimeException("card can't be null");
        }

        repository.save(card.toEntity());
        logger.info("YughioCardRepository : saved monster card {}", card.getName());
    }

    public YughioCard getById(Long id){
        if(id == null || id == -1){
            throw new RuntimeException("card id cannot be blank");
        }
        return repository.getTrapCardsById(id);
    }

    public YughioCard getByName(String name){
        if(name.isBlank()){
            throw new RuntimeException("card name cannot be blank");
        }
        return repository.getTrapCardsByName(name);
    }
}
