package com.cal.yughistore.services.dto.yughiocard.util;

import com.cal.yughistore.model.yughiocard.CardImages;
import com.cal.yughistore.model.yughiocard.CardPrices;
import com.cal.yughistore.model.yughiocard.CardSets;
import com.cal.yughistore.model.yughiocard.enums.EnumCardType;
import com.cal.yughistore.model.yughiocard.enums.EnumPropertiesConfigType;
import com.cal.yughistore.model.yughiocard.properties.CardProperties;
import com.cal.yughistore.model.yughiocard.properties.PropertiesMonsterCard;
import com.cal.yughistore.model.yughiocard.properties.PropertiesSpellCard;
import com.cal.yughistore.model.yughiocard.properties.PropertiesTrapCard;
import com.cal.yughistore.services.dto.yughiocard.CardImagesDTO;
import com.cal.yughistore.services.dto.yughiocard.CardPricesDTO;
import com.cal.yughistore.services.dto.yughiocard.CardSetsDTO;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;

import java.util.ArrayList;
import java.util.List;

import static com.cal.yughistore.utils.SimpleEnumUtils.findEnumValue;

@Slf4j
public class CardDtoUtil {
    @NonNull
    public static CardProperties checkCardProperties(EnumCardType cardType) {
        if (cardType.name().toUpperCase().contains(EnumPropertiesConfigType.MONSTER.getName())) {
            return new PropertiesMonsterCard();
        } else if (cardType.name().toUpperCase().contains(EnumPropertiesConfigType.SPELL.getName())) {
            return new PropertiesSpellCard();
        } else if (cardType.name().toUpperCase().contains(EnumPropertiesConfigType.TRAP.getName())) {
            return new PropertiesTrapCard();
        }
        return new CardProperties();
    }

    public static <E extends Enum<E>> E findPropertyEnumValue(Class<E> enumType, String valueName) {
        return findEnumValue(enumType, valueName.replaceAll("\\s", "_").replaceAll("-", "_"));
    }

    public static List<CardSetsDTO> cardSetsDTOFromNode(JsonNode node) {
        List<CardSetsDTO> cardSetsDTOS = new ArrayList<>();

        JsonNode cardSetCollectionList = node.get("card_sets");
        if (cardSetCollectionList == null) {
            return cardSetsDTOS;
        }
        for (JsonNode cardSetCollection : cardSetCollectionList) {
            cardSetsDTOS.add(
                    CardSetsDTO.of(cardSetCollection)
            );
        }

        return cardSetsDTOS;
    }

    public static List<CardSetsDTO> cardSetsDTOFromList(List<CardSets> list) {
        List<CardSetsDTO> cardSetsDTOS = new ArrayList<>();
        for (CardSets cardSet : list) {
            cardSetsDTOS.add(
                    CardSetsDTO.of(cardSet)
            );
        }

        return cardSetsDTOS;
    }

    public static List<CardSets> cardSetsFromList(List<CardSetsDTO> list) {
        List<CardSets> cardSets = new ArrayList<>();
        for (CardSetsDTO cardSetDTO : list) {
            cardSets.add(
                    cardSetDTO.toCardSets()
            );
        }
        return cardSets;
    }

    public static List<CardImagesDTO> cardImageDTOFromNode(JsonNode node) {
        List<CardImagesDTO> cardImages = new ArrayList<>();
        if (node == null) {
            log.info("cardImageDTOFromNode : node is null ***");
            return cardImages;
        }

        JsonNode imageCollectionList = node.get("card_images");
        if (imageCollectionList == null) {
            log.info("cardImageDTOFromNode : imageCollectionList is null ***");
            return cardImages;
        }
        for (JsonNode imageCollection : imageCollectionList) {
            cardImages.add(
                    CardImagesDTO.of(imageCollection)
            );
        }
        return cardImages;
    }

    public static List<CardImagesDTO> cardImageDTOFromList(List<CardImages> list) {
        List<CardImagesDTO> cardImages = new ArrayList<>();
        for (CardImages cardImage : list) {
            cardImages.add(
                    CardImagesDTO.of(cardImage)
            );
        }
        return cardImages;
    }

    public static List<CardImages> cardImagesFromList(List<CardImagesDTO> list) {
        List<CardImages> cardImages = new ArrayList<>();
        for (CardImagesDTO cardImageDTO : list) {
            cardImages.add(
                    cardImageDTO.toCardImages()
            );
        }
        return cardImages;
    }

    public static List<CardPricesDTO> cardPricesDTOFromNode(JsonNode node) {
        List<CardPricesDTO> cardPricesDTOS = new ArrayList<>();
        if (node == null) {
            log.info("cardPricesDTOFromNode : node is null ***");
            return cardPricesDTOS;
        }

        JsonNode priceCollectionList = node.get("card_prices");
        if (priceCollectionList == null) {
            log.info("cardPricesDTOFromNode : priceCollectionList is null ***");
            return cardPricesDTOS;
        }

        for (JsonNode imageCollection : priceCollectionList) {
            cardPricesDTOS.add(
                    CardPricesDTO.of(imageCollection)
            );
        }
        return cardPricesDTOS;
    }

    public static List<CardPricesDTO> cardPricesDTOFromList(List<CardPrices> list) {
        List<CardPricesDTO> cardPricesDTOS = new ArrayList<>();
        for (CardPrices cardPrices : list) {
            cardPricesDTOS.add(
                    CardPricesDTO.of(cardPrices)
            );
        }
        return cardPricesDTOS;
    }

    public static List<CardPrices> cardPricesFromList(List<CardPricesDTO> list) {
        List<CardPrices> cardPrices = new ArrayList<>();
        for (CardPricesDTO cardPricesDTO : list) {
            cardPrices.add(
                    cardPricesDTO.toCardPrices()
            );
        }
        return cardPrices;
    }
}
