package com.cal.yughistore.services.dto.yughiocard;

import com.cal.yughistore.model.yughiocard.CardImages;
import com.cal.yughistore.model.yughiocard.CardPrices;
import com.cal.yughistore.model.yughiocard.CardSets;
import com.cal.yughistore.model.yughiocard.YughioCard;
import com.cal.yughistore.model.yughiocard.enums.*;
import com.cal.yughistore.model.yughiocard.properties.PropertiesMonsterCard;
import com.cal.yughistore.model.yughiocard.properties.PropertiesSpellCard;
import com.cal.yughistore.model.yughiocard.properties.PropertiesTrapCard;
import com.cal.yughistore.model.yughiocard.properties.CardProperties;
import com.cal.yughistore.utils.SimpleEnumUtils;
import com.fasterxml.jackson.databind.JsonNode;
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
@ToString
public class YughioCardDTO {
    private static final Logger logger = LoggerFactory.getLogger(YughioCardDTO.class);

    /// base card properties (all cards have these) ///
    private Long id;
    private int api_id;
    private String name = "";
    private EnumCardType type = EnumCardType.NULL;
    private EnumFrameType frameType = EnumFrameType.NULL;
    private String description = "";
    private String ygoprodeck_url = "";

    private int quantity_in_stock = 0;

    /// Properties (depends on card type (trap, spell, monster, etc) ) ///
    private EnumPropertiesConfigType cardConfig;
    private CardProperties cardProperties;

    /// ///
    private List<CardImages>  card_images;
    private List<CardPrices> card_prices;
    private List<CardSets> card_sets;



    /// static methode ///
    public static CardProperties getCardProperties(EnumCardType cardType) {
        if (cardType.name().toUpperCase().contains(EnumPropertiesConfigType.MONSTER.getName())) {
            return new PropertiesMonsterCard();
        } else if (cardType.name().toUpperCase().contains(EnumPropertiesConfigType.SPELL.getName())) {
            return new PropertiesSpellCard();
        } else if (cardType.name().toUpperCase().contains(EnumPropertiesConfigType.TRAP.getName())) {
            return new PropertiesTrapCard();
        }
        return new CardProperties();
    }

    private static List<CardSets> cardSetsFromNode(JsonNode node) {
        List<CardSets> cardSets = new ArrayList<>();

        JsonNode cardSetCollectionList = node.get("card_sets");
        if (cardSetCollectionList == null) {
            return cardSets;
        }
        for (JsonNode cardSetCollection : cardSetCollectionList) {
            cardSets.add(
                    CardSets.builder()
                            .set_name(cardSetCollection.get("set_name").asText(""))
                            .set_code(cardSetCollection.get("set_code").asText(""))
                            .set_rarity(cardSetCollection.get("set_rarity").asText(""))
                            .set_rarity_code(cardSetCollection.get("set_rarity_code").asText(""))
                            .set_price(cardSetCollection.get("set_price").asDouble(-1))
                            .build()
            );
        }
        return cardSets;
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
                    .cardmarket_price(imageCollection.get("cardmarket_price").asDouble(-1))
                    .tcgplayer_price(imageCollection.get("tcgplayer_price").asDouble(-1))
                    .ebay_price(imageCollection.get("ebay_price").asDouble(-1))
                    .amazon_price(imageCollection.get("amazon_price").asDouble(-1))
                    .coolstuffinc_price(imageCollection.get("coolstuffinc_price").asDouble(-1))
                    .build());
        }
        return cardImages;
    }

    public static YughioCardDTO of(JsonNode node) {
        EnumCardType cardType = SimpleEnumUtils.findEnumValue(EnumCardType.class, node.get("type").asText().replaceAll("\\s", "_").replaceAll("-", "_"));
        EnumFrameType frameType = SimpleEnumUtils.findEnumValue(EnumFrameType.class, node.get("frameType").asText().replaceAll("\\s", "_").replaceAll("-", "_"));

        CardProperties cardProperties = getCardProperties(cardType);
        EnumPropertiesConfigType cardConfigType = EnumPropertiesConfigType.NULL;
        if (cardProperties != null) {
            if (cardProperties.getClass().equals(PropertiesMonsterCard.class)) {
                cardConfigType = EnumPropertiesConfigType.MONSTER;
                PropertiesMonsterCard monster = ((PropertiesMonsterCard) cardProperties);
                monster.setAtk(node.get("atk").asInt());
                monster.setDef(node.get("def").asInt());
                monster.setLevel(node.get("level").asInt());

                EnumMonsterCardRace race = SimpleEnumUtils.findEnumValue(EnumMonsterCardRace.class, node.get("race").asText().replaceAll("\\s", "_").replaceAll("-", "_"));
                EnumCardAttribute attribute = SimpleEnumUtils.findEnumValue(EnumCardAttribute.class, node.get("attribute").asText().replaceAll("\\s", "_").replaceAll("-", "_"));

                monster.setRace(race);
                monster.setAttribute(attribute);
            }else if (cardProperties.getClass().equals(PropertiesSpellCard.class)) {
                cardConfigType = EnumPropertiesConfigType.SPELL;
                PropertiesSpellCard propertiesSpellCard = ((PropertiesSpellCard) cardProperties);
                EnumNonMonsterCardRace race = SimpleEnumUtils.findEnumValue(EnumNonMonsterCardRace.class, node.get("race").asText());
                propertiesSpellCard.setRace(race);
            }
            else if (cardProperties.getClass().equals(PropertiesTrapCard.class)) {
                cardConfigType = EnumPropertiesConfigType.TRAP;
                PropertiesTrapCard propertiesTrapCard = ((PropertiesTrapCard) cardProperties);
                EnumNonMonsterCardRace race = SimpleEnumUtils.findEnumValue(EnumNonMonsterCardRace.class, node.get("race").asText());
                propertiesTrapCard.setRace(race);
            }
        }

        return YughioCardDTO.builder()
                .api_id(node.get("id").asInt())
                .name(node.get("name").asText().replaceAll("\"", ""))
                .type(cardType)
                .frameType(frameType)
                .description(node.get("desc").asText())
                .ygoprodeck_url(node.get("ygoprodeck_url").asText())

                .cardConfig(cardConfigType)
                .cardProperties(cardProperties)
                .card_sets(cardSetsFromNode(node))
                .card_images(cardImageGroupsFromNode(node))
                .card_prices(cardPricesFromNode(node))
                .build();
    }


    public static YughioCardDTO of(YughioCard card) {
        return YughioCardDTO.builder()
                .id(card.getId())
                .api_id(card.getApi_id())
                .name(card.getName())
                .type(card.getType())
                .frameType(card.getFrameType())
                .description(card.getDescription())
                .ygoprodeck_url(card.getYgoprodeck_url())
                .quantity_in_stock(card.getQuantity_in_stock())

                .cardConfig(card.getCardConfig())
                .cardProperties(card.getCardProperties())

                .card_sets(card.getCard_sets())
                .card_images(card.getCard_images())
                .card_prices(card.getCard_prices())

                .build();
    }

    /// Non-static methodes ///

    public YughioCard toYughioCard() {
        YughioCard card = YughioCard.builder()
                .api_id(this.api_id)
                .name(this.name)
                .type(this.type)
                .frameType(this.frameType)
                .description(this.description)
                .ygoprodeck_url(this.ygoprodeck_url)
                .quantity_in_stock(this.quantity_in_stock)
                
                .cardConfig(this.cardConfig)
                .cardProperties(this.cardProperties)

                .card_sets(this.card_sets)
                .card_images(this.card_images)
                .card_prices(this.card_prices)
                .build();

        return card;
    }
}
