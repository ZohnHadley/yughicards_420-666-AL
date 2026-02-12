package com.cal.yughistore.services;

import com.cal.yughistore.model.YughioCard;
import com.cal.yughistore.model.enums.EnumCardType;
import com.cal.yughistore.model.enums.EnumFrameType;
import com.cal.yughistore.model.properties.CardProperties;
import com.cal.yughistore.model.util.SimpleEnumUtils;
import com.cal.yughistore.repository.CardPropertiesRepository;
import com.cal.yughistore.repository.YughioCardRepository;
import com.cal.yughistore.services.DTOs.DTOYughioCard;
import com.fasterxml.jackson.databind.JsonNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class YughioCardService {
    private static final Logger logger = LoggerFactory.getLogger(YughioCardService.class);
    private final YughioCardRepository cardRepository;
    private final CardPropertiesRepository cardPropertiesRepository;

    public YughioCardService(YughioCardRepository cardRepository, CardPropertiesRepository cardPropertiesRepository) {
        this.cardRepository = cardRepository;
        this.cardPropertiesRepository = cardPropertiesRepository;
    }

    @Transactional
    public DTOYughioCard save(DTOYughioCard dtoCard) {
        if (dtoCard == null) {
            throw new IllegalArgumentException("card can't be null");
        }

        YughioCard cardToSave = dtoCard.toYughioCard();
        CardProperties properties = cardToSave.getCardProperties();

        YughioCard savedCard = cardRepository.save(cardToSave);

        CardProperties savedProperties = null;
        if (properties != null) {
            properties.setYughioCard(savedCard); // ensure owning side is set
            savedProperties = cardPropertiesRepository.save(properties);
        }

        DTOYughioCard response = DTOYughioCard.of(savedCard);
        response.setCardProperties(savedProperties);

        logger.info("YughioCardService : saved card {}", response);
        return response;
    }

    @Transactional
    public List<DTOYughioCard> saveAll(List<DTOYughioCard> dtoCards) {
        if (dtoCards == null || dtoCards.isEmpty()) {
            throw new IllegalArgumentException("cards list can't be empty");
        }

        List<YughioCard> cardsToSave = dtoCards.stream()
                .map(DTOYughioCard::toYughioCard)
                .toList();

        List<YughioCard> savedCards = cardRepository.saveAll(cardsToSave);

        List<CardProperties> propertiesToSave = new ArrayList<>(savedCards.size());
        for (YughioCard savedCard : savedCards) {
            CardProperties properties = savedCard.getCardProperties();
            if (properties != null) {
                properties.setYughioCard(savedCard); // ensure owning side is set
                propertiesToSave.add(properties);
            }
        }
        if (!propertiesToSave.isEmpty()) {
            cardPropertiesRepository.saveAll(propertiesToSave);
        }

        List<DTOYughioCard> response = new ArrayList<>(savedCards.size());
        for (YughioCard savedCard : savedCards) {
            response.add(DTOYughioCard.of(savedCard));
        }

        logger.info("YughioCardService : saved cards {}", response.isEmpty() ? "none" : "success");
        return response;
    }

    @Transactional(readOnly = true)
    public List<DTOYughioCard> getAllPaged(int page, int num) {
        if (page < 0) {
            throw new IllegalArgumentException("page must be >= 0");
        }
        if (num <= 0) {
            throw new IllegalArgumentException("num must be > 0");
        }

        Pageable pageable = PageRequest.of(page, num, Sort.by(Sort.Direction.ASC, "id"));
        Page<YughioCard> cardsPage = cardRepository.findAll(pageable);

        List<YughioCard> cards = cardsPage.getContent();
        if (cards.isEmpty()) {
            logger.info("YughioCardService : getting all cards paged (page={}, size={}) -> 0 results", page, num);
            return List.of();
        }

        List<Long> cardIds = cards.stream()
                .map(YughioCard::getId)
                .toList();

        List<CardProperties> propertiesList = cardPropertiesRepository.findAllByYughioCard_IdIn(cardIds);

        Map<Long, CardProperties> propertiesByCardId = new HashMap<>(propertiesList.size() * 2);
        for (CardProperties props : propertiesList) {
            if (props != null && props.getYughioCard() != null && props.getYughioCard().getId() != null) {
                propertiesByCardId.put(props.getYughioCard().getId(), props);
            }
        }

        List<DTOYughioCard> response = new ArrayList<>(cards.size());
        for (YughioCard card : cards) {
            DTOYughioCard dto = DTOYughioCard.of(card);
            dto.setCardProperties(propertiesByCardId.get(card.getId())); // may be null -> OK if allowed
            response.add(dto);
        }

        logger.info("YughioCardService : getting all cards paged (page={}, size={}) -> {} results",
                page, num, response.size());
        return response;
    }

    @Transactional(readOnly = true)
    public DTOYughioCard getById(Long id) {
        if (id == null || id == -1) {
            throw new RuntimeException("card id cannot be blank");
        }

        DTOYughioCard cardDto = DTOYughioCard.of(cardRepository.getById(id));
        logger.info("YughioCardService : getById {}", cardDto.toString());
        return cardDto;
    }

    @Transactional(readOnly = true)
    public DTOYughioCard getByName(String name) {
        if (name.isBlank()) {
            throw new RuntimeException("card name cannot be blank");
        }

        DTOYughioCard cardDto = DTOYughioCard.of(cardRepository.getByName(name));
        logger.info("YughioCardService : getByName {}", cardDto.toString());
        return cardDto;
    }

    @Transactional(readOnly = true)
    public List<DTOYughioCard> getByFrameTypePaged(String frameType, int page, int num) {
        Pageable pageWithElementCount = PageRequest.of(page, num);
        List<DTOYughioCard> cardList = new ArrayList<>();
        EnumFrameType requestedType = SimpleEnumUtils.findEnumValue(EnumFrameType.class, frameType);

        Page<YughioCard> cards = cardRepository.getAllByFrameType(requestedType, pageWithElementCount);

        for (YughioCard card : cards) {
            cardList.add(DTOYughioCard.of(card));
        }

        logger.info("YughioCardService : getByName {}", cardList.toString());
        return cardList;
    }

    @Transactional(readOnly = true)
    public List<DTOYughioCard> getByTypePaged(String type, int page, int num) {
        if (type.isBlank()) {
            throw new RuntimeException("card type cannot be blank");
        }

        Pageable pageWithElementCount = PageRequest.of(page, num);
        List<DTOYughioCard> cardList = new ArrayList<>();
        EnumCardType requestedType = SimpleEnumUtils.findEnumValue(EnumCardType.class, type);
        Page<YughioCard> cards = cardRepository.getAllByType(requestedType, pageWithElementCount);

        for (YughioCard card : cards) {
            cardList.add(DTOYughioCard.of(card));
        }

        logger.info("YughioCardService : getByName {}", cardList.toString());
        return cardList;
    }

}
