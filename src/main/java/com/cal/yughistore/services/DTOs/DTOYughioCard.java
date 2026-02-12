package com.cal.yughistore.services.DTOs;

import com.cal.yughistore.model.CardImages;
import com.cal.yughistore.model.CardPrices;
import com.cal.yughistore.model.YughioCard;
import com.cal.yughistore.model.enums.*;
import com.cal.yughistore.model.properties.PropertiesMonsterCard;
import com.cal.yughistore.model.properties.PropertiesSpellCard;
import com.cal.yughistore.model.properties.PropertiesTrapCard;
import com.cal.yughistore.model.properties.CardProperties;
import com.cal.yughistore.model.util.SimpleEnumUtils;
import com.cal.yughistore.services.api.ApiService;
import com.fasterxml.jackson.databind.JsonNode;
import jakarta.persistence.*;
import lombok.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table
@ToString
public class DTOYughioCard {
    private static final Logger logger = LoggerFactory.getLogger(DTOYughioCard.class);

    /// base card properties (all cards have these) ///
    private Long id;
    private int api_id;
    private String name = "no_name";
    private EnumCardType type = EnumCardType.NULL;
    private EnumFrameType frameType = EnumFrameType.NULL;
    private String description = "";
    private String ygoprodeck_url = "";

    private List<CardImages>  card_images;
    private List<CardPrices> card_prices = new ArrayList<>();

    /// Properties (depends on card type (trap, spell, monster, etc) ) ///
    private EnumPropertiesConfigType cardConfig;
    private CardProperties cardProperties;


    /// static methode ///
    private static CardProperties getCardProperties(EnumCardType cardType) {
        if (cardType.name().toUpperCase().contains(EnumPropertiesConfigType.TYPE_MONSTER.getName())) {
            return new PropertiesMonsterCard();
        } else if (cardType.name().toUpperCase().contains(EnumPropertiesConfigType.TYPE_SPELL.getName())) {
            return new PropertiesSpellCard();
        } else if (cardType.name().toUpperCase().contains(EnumPropertiesConfigType.TYPE_TRAP.getName())) {
            return new PropertiesTrapCard();
        }
        System.out.println("AAAAAAAAAAAAAAAAAAAAAAA");
        return null;
    }

    private static List<CardImages> cardImageGroupsFromNode(JsonNode node) {
        List<CardImages> cardImages = new ArrayList<>();

        JsonNode imageCollectionList = node.get("card_images");
        for (JsonNode imageCollection : imageCollectionList) {
            cardImages.add(CardImages.builder()
                    .image_group_api_id(imageCollection.get("id").asInt())
                    .image_url(imageCollection.get("image_url").asText(""))
                    .image_url_small(imageCollection.get("image_url_small").asText(""))
                    .image_url_cropped(imageCollection.get("image_url_cropped").asText(""))
                    .build());
        }
        return cardImages;
    }

    private static List<CardPrices> cardPricesFromNode(JsonNode node) {
        List<CardPrices> cardImages = new ArrayList<>();
        JsonNode imageCollectionList = node.get("card_prices");
        for (JsonNode imageCollection : imageCollectionList) {
            cardImages.add(CardPrices.builder()
                    .cardmarket_price(imageCollection.get("cardmarket_price").asText(""))
                    .tcgplayer_price(imageCollection.get("tcgplayer_price").asText(""))
                    .ebay_price(imageCollection.get("ebay_price").asText(""))
                    .amazon_price(imageCollection.get("amazon_price").asText(""))
                    .coolstuffinc_price(imageCollection.get("coolstuffinc_price").asText(""))
                    .build());
        }
        return cardImages;
    }

    public static DTOYughioCard toDTO(JsonNode node) {
        EnumCardType cardType = SimpleEnumUtils.findEnumValue(EnumCardType.class, node.get("type").asText().replaceAll("\\s", "_").replaceAll("-", "_"));
        EnumFrameType frameType = SimpleEnumUtils.findEnumValue(EnumFrameType.class, node.get("frameType").asText().replaceAll("\\s", "_").replaceAll("-", "_"));

        CardProperties cardProperties = getCardProperties(cardType);
        EnumPropertiesConfigType cardConfigType = EnumPropertiesConfigType.NULL;
        if (cardProperties != null) {
            if (cardProperties.getClass().equals(PropertiesMonsterCard.class)) {
                cardConfigType = EnumPropertiesConfigType.TYPE_MONSTER;
                PropertiesMonsterCard monster = ((PropertiesMonsterCard) cardProperties);

                monster.setAtk(node.get("atk").asInt());
                monster.setDef(node.get("def").asInt());
                monster.setLevel(node.get("level").asInt());

                EnumMonsterCardRace race = SimpleEnumUtils.findEnumValue(EnumMonsterCardRace.class, node.get("race").asText().replaceAll("\\s", "_").replaceAll("-", "_"));
                EnumCardAttribute attribute = SimpleEnumUtils.findEnumValue(EnumCardAttribute.class, node.get("attribute").asText().replaceAll("\\s", "_").replaceAll("-", "_"));

                monster.setRace(race);
                monster.setAttribute(attribute);
            } else if (cardProperties.getClass().equals(PropertiesSpellCard.class)) {
                cardConfigType = EnumPropertiesConfigType.TYPE_SPELL;
                PropertiesSpellCard spell = ((PropertiesSpellCard) cardProperties);

                EnumNonMonsterCardRace race = SimpleEnumUtils.findEnumValue(EnumNonMonsterCardRace.class, node.get("race").asText().replaceAll("\\s", "_").replaceAll("-", "_"));

                spell.setRace(race);
            } else if (cardProperties.getClass().equals(PropertiesTrapCard.class)) {
                cardConfigType = EnumPropertiesConfigType.TYPE_TRAP;
                PropertiesTrapCard trap = ((PropertiesTrapCard) cardProperties);

                EnumNonMonsterCardRace race = SimpleEnumUtils.findEnumValue(EnumNonMonsterCardRace.class, node.get("race").asText().replaceAll("\\s", "_").replaceAll("-", "_"));

                trap.setRace(race);
            }

        }

        return DTOYughioCard.builder()
                .api_id(node.get("id").asInt())
                .name(node.get("name").asText().replaceAll("\"", ""))
                .type(cardType)
                .description(node.get("desc").asText())
                .ygoprodeck_url(node.get("ygoprodeck_url").asText())
                .frameType(frameType)

                .cardConfig(cardConfigType)
//                .cardProperties(cardProperties)
                .card_images(cardImageGroupsFromNode(node))
                .card_prices(cardPricesFromNode(node))
                .build();
    }


    public static DTOYughioCard toDTO(YughioCard card) {
        return DTOYughioCard.builder()
                .id(card.getId())
                .api_id(card.getApi_id())
                .name(card.getName())
                .type(card.getType())
                .description(card.getDescription())
                .ygoprodeck_url(card.getYgoprodeck_url())
                .frameType(card.getFrameType())
                .cardConfig(card.getCardConfig())
                .cardProperties(card.getCardProperties())
                .build();
    }

    /// ///

    public YughioCard toEntity() {
        YughioCard card = YughioCard.builder()
                .api_id(this.api_id)
                .name(this.name)
                .type(this.type)
                .frameType(this.frameType)
                .description(this.description)
                .ygoprodeck_url(this.ygoprodeck_url)
//                .cardConfig(this.cardConfig)
                .card_images(this.card_images)
                .card_prices(this.card_prices)
                .build();

        CardProperties cardProperties = getCardProperties(card.getType());
        card.setCardProperties(cardProperties);

        return card;
    }
}
