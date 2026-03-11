package com.cal.yughistore.service;

import com.cal.yughistore.service.exception.EntityDTONullException;
import com.cal.yughistore.service.exception.EntityIdentifierNullException;
import com.cal.yughistore.utils.ConsoleLoadingBar;
import com.cal.yughistore.model.yughiocard.CardImages;
import com.cal.yughistore.model.yughiocard.CardPrices;
import com.cal.yughistore.model.yughiocard.CardSet;
import com.cal.yughistore.model.yughiocard.YughioCard;
import com.cal.yughistore.model.yughiocard.enums.EnumCardType;
import com.cal.yughistore.model.yughiocard.enums.EnumFrameType;
import com.cal.yughistore.model.yughiocard.properties.CardProperties;
import com.cal.yughistore.repository.card.CardImagesRepository;
import com.cal.yughistore.repository.card.CardPricesRepository;
import com.cal.yughistore.repository.card.CardSetRepository;
import com.cal.yughistore.repository.card.CardPropertiesRepository;
import com.cal.yughistore.repository.card.YughioCardRepository;
import com.cal.yughistore.service.dto.yughiocard.CardImagesDTO;
import com.cal.yughistore.service.dto.yughiocard.CardPricesDTO;
import com.cal.yughistore.service.dto.yughiocard.CardSetDTO;
import com.cal.yughistore.service.dto.yughiocard.YughioCardDTO;
import com.cal.yughistore.utils.SimpleEnumUtils;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class YughioCardService {
    @PersistenceContext
    private EntityManager entityManager;

    private final ConsoleLoadingBar consoleLoadingBar = new ConsoleLoadingBar();
    private static final Logger logger = LoggerFactory.getLogger(YughioCardService.class);

    private final YughioCardRepository cardRepository;
    private final CardPropertiesRepository cardPropertiesRepository;
    private final CardImagesRepository cardImagesRepository;
    private final CardPricesRepository cardPriceRepository;
    private final CardSetRepository cardSetRepository;


    public YughioCardService(YughioCardRepository cardRepository,
                             CardPropertiesRepository cardPropertiesRepository,
                             CardImagesRepository cardImagesRepository,
                             CardPricesRepository cardPriceRepository,
                             CardSetRepository cardSetRepository) {
        this.cardRepository = cardRepository;
        this.cardPropertiesRepository = cardPropertiesRepository;
        this.cardImagesRepository = cardImagesRepository;
        this.cardPriceRepository = cardPriceRepository;
        this.cardSetRepository = cardSetRepository;
    }

    // ── Save ────────────────────────────────────────────────────────────────

    @Transactional
    public YughioCardDTO save(YughioCardDTO dtoCard) {
        if (dtoCard == null) {
            throw new EntityDTONullException(YughioCardDTO.class, "can't be null");
        }
        YughioCard card = dtoCard.toYughioCard();
        YughioCard savedCard = cardRepository.save(card);

        saveCardImages(dtoCard, savedCard);

        saveCardPrices(dtoCard, savedCard);

        saveCardSets(savedCard, dtoCard.getCard_sets());

        saveCardProperties(dtoCard, savedCard);

        YughioCardDTO response = YughioCardDTO.of(savedCard);
        logger.debug("Saved card: {}", response);
        return response;
    }

    ///
    ///
    ///
    private void saveCardProperties(YughioCardDTO cardDTO, YughioCard card) {
        if (cardDTO.getCardProperties() != null) {
            CardProperties properties = cardDTO.getCardProperties().toCardProperties();
            if (properties == null) return;
            properties.setYughioCard(card);
            cardPropertiesRepository.save(properties);
        }
    }

    private void saveCardImages(YughioCardDTO dtoCard, YughioCard savedCard) {
        if (dtoCard.getCard_images() != null) {
            for (CardImagesDTO ci : dtoCard.getCard_images()) {
                CardImages images = ci.toCardImages();
                images.setYughioCard(savedCard);
                cardImagesRepository.save(images);
            }
        }
    }

    private void saveCardPrices(YughioCardDTO dtoCard, YughioCard savedCard) {
        if (dtoCard.getCard_prices() != null) {
            for (CardPricesDTO cp : dtoCard.getCard_prices()) {
                CardPrices prices = cp.toCardPrices();
                prices.setYughioCard(savedCard);
                cardPriceRepository.save(prices);
            }
        }
    }

    private void saveCardSets(YughioCard card, List<CardSetDTO> sets) {
        if (sets == null || sets.isEmpty()) return;
        for (CardSetDTO s : sets) {
            CardSet entity = CardSet.builder()
                    .set_name(s.set_name())
                    .set_code(s.set_code())
                    .set_rarity(s.set_rarity())
                    .set_rarity_code(s.set_rarity_code())
                    .set_price(s.set_price())
                    .yughioCard(card)
                    .build();
            cardSetRepository.save(entity);
        }
    }

    ///
    ///
    ///

    @Transactional
    public List<YughioCardDTO> saveAll(List<YughioCardDTO> dtoCards) {
        if (dtoCards == null) {
            throw new EntityDTONullException("cards list can't be empty");
        }

        logger.info("saving {} cards to database", dtoCards.size());

        int batchSize = 500;

        List<YughioCardDTO> response = new ArrayList<>(dtoCards.size());

        for (int index = 0; index < dtoCards.size(); index++) {
            YughioCardDTO dto = dtoCards.get(index);

            if (dto == null) {
                throw new EntityDTONullException("dto can't be null");
            }

            response.add(save(dto));

            consoleLoadingBar.printProgress(index + 1, dtoCards.size());

            if ((index + 1) % batchSize == 0) {
                entityManager.flush();
                entityManager.clear();
            }
        }

        consoleLoadingBar.finish();

        logger.info("Saved {} cards", response.size());

        return response;
    }

    // ── Get ─────────────────────────────────────────────────────────────────

    @Transactional(readOnly = true)
    public YughioCardDTO getById(Long cardId) {
        if (cardId == null) {
            throw new EntityIdentifierNullException("card id cannot be null");
        }
        Optional<YughioCard> card = cardRepository.findById(cardId);
        if (card.isEmpty()) {
            throw new EntityIdentifierNullException("Card not found with id: " + cardId);
        }
        return YughioCardDTO.of(card.get());
    }

//    @Transactional(readOnly = true)
//    public YughioCardDTO getByName(String name) {
//        if (name.isBlank()) {
//            throw new RuntimeException("card name cannot be blank");
//        }
//        YughioCard card = cardRepository.findByNameIgnoreCase(name)
//                .orElseThrow(() -> new RuntimeException("Card not found with name: " + name));
//        return YughioCardDTO.of(card);
//    }

//    @Transactional(readOnly = true)
//    public List<YughioCardDTO> getAllVersionsByName(String name, int page, int num) {
//        if (name.isBlank()) throw new RuntimeException("card name cannot be blank");
//        Pageable pageable = PageRequest.of(page, num);
//        Page<YughioCard> cards = cardRepository.findAllByNameIgnoreCaseOrderBySetNameAsc(name, pageable);
//        return cards.stream().map(YughioCardDTO::of).toList();
//    }

    @Transactional(readOnly = true)
    public List<YughioCardDTO> getSearchByName(String name, int page, int num) {
        if (name.isBlank()) throw new RuntimeException("card name cannot be blank");

        Pageable pageable = PageRequest.of(page, num);
        Page<YughioCard> cards = cardRepository.findByNameContainingIgnoreCase(name, pageable);

        List<YughioCardDTO> cardDTOs = new ArrayList<>();

        for (YughioCard card : cards) {
            if (card == null) {
                continue;
            }
            cardDTOs.add(YughioCardDTO.of(card));
        }

        return cardDTOs;
    }

    @Transactional(readOnly = true)
    public List<YughioCardDTO> getByFrameTypePaged(String frameType, int page, int num) {
        Pageable pageable = PageRequest.of(page, num);
        EnumFrameType type = SimpleEnumUtils.findEnumValue(EnumFrameType.class, frameType);
        Page<YughioCard> cards = cardRepository.getAllByFrameType(type, pageable);
        return cards.stream().map(YughioCardDTO::of).toList();
    }

    @Transactional(readOnly = true)
    public List<YughioCardDTO> getByTypePaged(String type, int page, int num) {
        if (type.isBlank()) throw new RuntimeException("card type cannot be blank");
        Pageable pageable = PageRequest.of(page, num);
        EnumCardType enumType = SimpleEnumUtils.findEnumValue(EnumCardType.class, type);
        Page<YughioCard> cards = cardRepository.getAllByType(enumType, pageable);
        return cards.stream().map(YughioCardDTO::of).toList();
    }

    @Transactional(readOnly = true)
    public List<YughioCardDTO> getAllPaged(int page, int num) {
        Pageable pageable = PageRequest.of(page, num, Sort.by(Sort.Direction.ASC, "id"));
        Page<YughioCard> cardsPage = cardRepository.findAll(pageable);
        return cardsPage.stream().map(YughioCardDTO::of).toList();
    }

    @Transactional(readOnly = true)
    public List<YughioCardDTO> getAllVersionsOfCard(String cardName) {
        List<YughioCard> cards = cardRepository.findAllByNameIgnoreCaseOrderByRarityAsc(cardName);
        return cards.stream().map(YughioCardDTO::of).collect(Collectors.toList());
    }

    @Transactional
    public Boolean deleteById(Long id) {
        try {
            cardRepository.deleteById(id);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}