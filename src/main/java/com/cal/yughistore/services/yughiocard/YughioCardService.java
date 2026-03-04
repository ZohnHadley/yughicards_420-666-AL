package com.cal.yughistore.services.yughiocard;

import com.cal.yughistore.model.yughiocard.CardImages;
import com.cal.yughistore.model.yughiocard.CardPrices;
import com.cal.yughistore.model.yughiocard.CardSet;
import com.cal.yughistore.model.yughiocard.YughioCard;
import com.cal.yughistore.model.yughiocard.enums.EnumCardType;
import com.cal.yughistore.model.yughiocard.enums.EnumFrameType;
import com.cal.yughistore.model.yughiocard.properties.CardProperties;
import com.cal.yughistore.repository.CardImagesRepository;
import com.cal.yughistore.repository.CardPricesRepository;
import com.cal.yughistore.repository.CardSetRepository;
import com.cal.yughistore.repository.propertie.CardPropertiesRepository;
import com.cal.yughistore.repository.YughioCardRepository;
import com.cal.yughistore.services.dto.yughiocard.CardSetDTO;
import com.cal.yughistore.services.dto.yughiocard.YughioCardDTO;
import com.cal.yughistore.utils.SimpleEnumUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class YughioCardService {
    private static final Logger logger = LoggerFactory.getLogger(YughioCardService.class);

    private final YughioCardRepository cardRepository;
    private final CardPropertiesRepository cardPropertiesRepository;
    private final CardImagesRepository cardImagesRepository;
    private final CardPricesRepository cardPriceRepository;
    private final CardSetRepository cardSetRepository;

    private final int pagination_default_number_of_elements_per_page = 10;

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
        if (dtoCard == null) throw new IllegalArgumentException("card can't be null");

        YughioCard savedCard = cardRepository.save(dtoCard.toYughioCard());

        saveCardProperties(savedCard);

        if (dtoCard.getCard_images() != null) {
            for (CardImages ci : dtoCard.getCard_images()) {
                ci.setYughioCard(savedCard);
                cardImagesRepository.save(ci);
            }
        }

        if (dtoCard.getCard_prices() != null) {
            for (CardPrices cp : dtoCard.getCard_prices()) {
                cp.setYughioCard(savedCard);
                cardPriceRepository.save(cp);
            }
        }

        saveCardSets(savedCard, dtoCard.getCard_sets());

        YughioCardDTO response = YughioCardDTO.of(savedCard);
        logger.info("Saved card: {}", response);
        return response;
    }

    @Transactional
    public List<YughioCardDTO> saveAll(List<YughioCardDTO> dtoCards) {
        if (dtoCards == null || dtoCards.isEmpty())
            throw new IllegalArgumentException("cards list can't be empty");

        List<YughioCardDTO> response = new ArrayList<>();
        for (YughioCardDTO dto : dtoCards) {
            response.add(save(dto));
        }
        logger.info("Saved {} cards", response.size());
        return response;
    }

    private void saveCardProperties(YughioCard card) {
        CardProperties properties = card.getCardProperties();
        if (properties != null) {
            properties.setYughioCard(card);
            cardPropertiesRepository.save(properties);
        }
    }

    // ── Sauvegarde les sets d'une carte ─────────────────────────────────────

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

    // ── Get ─────────────────────────────────────────────────────────────────

    @Transactional(readOnly = true)
    public YughioCardDTO getById(Long id) {
        if (id == null) throw new IllegalArgumentException("card id can't be null");
        YughioCard card = cardRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Card not found with id: " + id));
        return YughioCardDTO.of(card);

    }

    @Transactional(readOnly = true)
    public YughioCardDTO getByName(String name) {
        if (name.isBlank()) throw new RuntimeException("card name cannot be blank");
        YughioCard card = cardRepository.findByNameIgnoreCase(name)
                .orElseThrow(() -> new RuntimeException("Card not found with name: " + name));
        return YughioCardDTO.of(card);
    }

    @Transactional(readOnly = true)
    public List<YughioCardDTO> getAllVersionsByName(String name, int page, int num) {
        if (name.isBlank()) throw new RuntimeException("card name cannot be blank");
        Pageable pageable = PageRequest.of(page, num);
        Page<YughioCard> cards = cardRepository.findAllByNameIgnoreCaseOrderBySetNameAsc(name, pageable);
        return cards.stream().map(YughioCardDTO::of).toList();
    }

    @Transactional(readOnly = true)
    public List<YughioCardDTO> getBySearchName(String name, int page, int num) {
        if (name.isBlank()) throw new RuntimeException("card name cannot be blank");
        Pageable pageable = PageRequest.of(page, num);
        Page<YughioCard> cards = cardRepository.findByNameContainingIgnoreCase(name, pageable);
        return cards.stream().map(YughioCardDTO::of).toList();
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
}