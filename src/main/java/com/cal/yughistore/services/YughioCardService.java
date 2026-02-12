package com.cal.yughistore.services;

import com.cal.yughistore.model.YughioCard;
import com.cal.yughistore.model.enums.EnumCardType;
import com.cal.yughistore.model.enums.EnumFrameType;
import com.cal.yughistore.model.util.JsonUtil;
import com.cal.yughistore.model.util.SimpleEnumUtils;
import com.cal.yughistore.repository.YughioCardRepository;
import com.cal.yughistore.services.DTOs.DTOYughioCard;
import com.fasterxml.jackson.databind.JsonNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Service
public class YughioCardService {
    private static final Logger logger = LoggerFactory.getLogger(YughioCardService.class);
    private final YughioCardRepository repository;

    public YughioCardService(YughioCardRepository repository) {
        this.repository = repository;
    }

    public DTOYughioCard save(DTOYughioCard card) {
        if (card == null) {
            throw new RuntimeException("card can't be null");
        }

        DTOYughioCard cardDto = DTOYughioCard.toDTO(repository.save(card.toEntity()));
        logger.info("YughioCardService : saved monster card {}", cardDto.toString());
        return cardDto;
    }

    public List<DTOYughioCard> getAllPaged(int page, int num) {
        Pageable pageWithElementCount = PageRequest.of(page, num);
        List<DTOYughioCard> cardList = new ArrayList<>();

        Page<YughioCard> cards = repository.findAll(pageWithElementCount);

        for (YughioCard card : cards) {
            cardList.add(DTOYughioCard.toDTO(card));
        }

        logger.info("YughioCardService : getting all cards paged");
        return cardList;
    }

    public DTOYughioCard getById(Long id) {
        if (id == null || id == -1) {
            throw new RuntimeException("card id cannot be blank");
        }

        DTOYughioCard cardDto = DTOYughioCard.toDTO(repository.getById(id));
        logger.info("YughioCardService : getById {}", cardDto.toString());
        return cardDto;
    }

    public DTOYughioCard getByName(String name) {
        if (name.isBlank()) {
            throw new RuntimeException("card name cannot be blank");
        }

        DTOYughioCard cardDto = DTOYughioCard.toDTO(repository.getByName(name));
        logger.info("YughioCardService : getByName {}", cardDto.toString());
        return cardDto;
    }

    public List<DTOYughioCard> getByFrameTypePaged(String frameType, int page, int num) {
        Pageable pageWithElementCount = PageRequest.of(page, num);
        List<DTOYughioCard> cardList = new ArrayList<>();
        EnumFrameType requestedType = SimpleEnumUtils.findEnumValue(EnumFrameType.class, frameType);

        Page<YughioCard> cards = repository.getAllByFrameType(requestedType, pageWithElementCount);

        for (YughioCard card : cards) {
            cardList.add(DTOYughioCard.toDTO(card));
        }

        logger.info("YughioCardService : getByName {}", cardList.toString());
        return cardList;
    }

    public List<DTOYughioCard> getByTypePaged(String type, int page, int num) {
        if (type.isBlank()) {
            throw new RuntimeException("card type cannot be blank");
        }

        Pageable pageWithElementCount = PageRequest.of(page, num);
        List<DTOYughioCard> cardList = new ArrayList<>();
        EnumCardType requestedType = SimpleEnumUtils.findEnumValue(EnumCardType.class, type);
        Page<YughioCard> cards = repository.getAllByType(requestedType, pageWithElementCount);

        for (YughioCard card : cards) {
            cardList.add(DTOYughioCard.toDTO(card));
        }

        logger.info("YughioCardService : getByName {}", cardList.toString());
        return cardList;
    }


}
