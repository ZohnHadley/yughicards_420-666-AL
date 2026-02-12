package com.cal.yughistore.services;

import com.cal.yughistore.model.CardImages;
import com.cal.yughistore.model.CardPrices;
import com.cal.yughistore.model.YughioCard;
import com.cal.yughistore.model.enums.EnumCardType;
import com.cal.yughistore.model.enums.EnumFrameType;
import com.cal.yughistore.model.properties.CardProperties;
import com.cal.yughistore.model.util.SimpleEnumUtils;
import com.cal.yughistore.repository.CardImagesRepository;
import com.cal.yughistore.repository.CardPricesRepository;
import com.cal.yughistore.repository.CardPropertiesRepository;
import com.cal.yughistore.repository.YughioCardRepository;
import com.cal.yughistore.services.DTOs.DTOYughioCard;
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
    private final CardImagesRepository cardImagesRepository;
    private final CardPricesRepository cardPriceRepository;

    public YughioCardService(YughioCardRepository cardRepository, CardPropertiesRepository cardPropertiesRepository, CardImagesRepository cardImagesRepository, CardPricesRepository cardPriceRepository) {
        this.cardRepository = cardRepository;
        this.cardPropertiesRepository = cardPropertiesRepository;
        this.cardImagesRepository = cardImagesRepository;
        this.cardPriceRepository = cardPriceRepository;
    }

    @Transactional
    public DTOYughioCard save(DTOYughioCard dtoCard) {
        if (dtoCard == null) {
            throw new IllegalArgumentException("card can't be null");
        }

        YughioCard cardToSave = dtoCard.toYughioCard();

        YughioCard savedCard = cardRepository.save(cardToSave);


        DTOYughioCard response = DTOYughioCard.of(savedCard);
        saveCardProperties(savedCard);
        //card images
        for (CardImages cardImages : dtoCard.getCard_images()) {
            cardImages.setYughioCard(savedCard);
            cardImagesRepository.save(cardImages);
        }

        for (CardPrices cardPrices : dtoCard.getCard_prices()) {
            cardPrices.setYughioCard(savedCard);
            cardPriceRepository.save(cardPrices);
        }


        logger.info("YughioCardService : saved card {}", response);
        return response;
    }

    private CardProperties saveCardProperties(YughioCard savedCard) {
        CardProperties properties = new CardProperties();
        if (savedCard != null) {
            properties.setYughioCard(savedCard); // ensure owning side is set
            properties = cardPropertiesRepository.save(savedCard.getCardProperties());
        }
        return properties;
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

        saveAllCardsProperties(savedCards);
        saveAllCardsCardImages(savedCards);
        saveAllCardsCardPrices(savedCards);

        List<DTOYughioCard> response = new ArrayList<>(savedCards.size());
        for (YughioCard savedCard : savedCards) {
            response.add(DTOYughioCard.of(savedCard));
        }

        logger.info("YughioCardService : saved cards {}", response.isEmpty() ? "none" : "success");
        return response;
    }

    private void saveAllCardsProperties(List<YughioCard> savedCards) {
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
    }

    private void saveAllCardsCardImages(List<YughioCard> savedCards) {
        List<CardImages> cardsCardImagesToSave = new ArrayList<>();
        for (YughioCard savedCard : savedCards) {
            for (CardImages cardImages : savedCard.getCard_images()) {
                cardImages.setYughioCard(savedCard);
                cardsCardImagesToSave.add(cardImages);
            }
        }
        if(!cardsCardImagesToSave.isEmpty()) {
            cardImagesRepository.saveAll(cardsCardImagesToSave);
        }
    }

    private void saveAllCardsCardPrices(List<YughioCard> savedCards) {
        List<CardPrices> cardsCardPricesToSave = new ArrayList<>();
        for (YughioCard savedCard : savedCards) {
            for (CardPrices cardPrices : savedCard.getCard_prices()) {
                cardPrices.setYughioCard(savedCard);
                cardsCardPricesToSave.add(cardPrices);
            }
        }
        if(!cardsCardPricesToSave.isEmpty()) {
            cardPriceRepository.saveAll(cardsCardPricesToSave);
        }
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

        List<DTOYughioCard> response = new ArrayList<>(cards.size());
        for (YughioCard card : cards) {
            DTOYughioCard dto = DTOYughioCard.of(card);
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
