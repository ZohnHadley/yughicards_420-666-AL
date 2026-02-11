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
//            JsonNode dataList = (JsonUtil.getInstance().fromJson(content)).get("data");
//            for(JsonNode node : dataList){
//                repository.save(DTOYughioCard.toDTO(node).toEntity());
//            }

        } catch (IOException e) {
            // Handle potential I/O errors (e.g., file not found, permission issues)
            e.printStackTrace();
        }
    }

    public DTOYughioCard save(DTOYughioCard card){
        if (card == null){
            throw new RuntimeException("card can't be null");
        }

        DTOYughioCard cardDto = DTOYughioCard.toDTO(repository.save(card.toEntity()));
        logger.info("YughioCardRepository : saved monster card {}", cardDto.toString());
        return cardDto;
    }

    public DTOYughioCard getById(Long id){
        if(id == null || id == -1){
            throw new RuntimeException("card id cannot be blank");
        }

        DTOYughioCard cardDto = DTOYughioCard.toDTO(repository.getTrapCardsById(id));
        logger.info("YughioCardRepository : getById {}", cardDto.toString());
        return cardDto;
    }

    public DTOYughioCard getByName(String name){
        if(name.isBlank()){
            throw new RuntimeException("card name cannot be blank");
        }

        DTOYughioCard cardDto = DTOYughioCard.toDTO(repository.getTrapCardsByName(name));
        logger.info("YughioCardRepository : getByName {}", cardDto.toString());
        return cardDto;
    }
}
