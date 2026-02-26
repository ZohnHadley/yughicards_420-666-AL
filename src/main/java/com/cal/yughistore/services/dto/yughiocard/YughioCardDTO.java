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
    private int apiId = -1;
    private String name = "";
    private EnumCardType type = EnumCardType.NULL;
    private EnumFrameType frameType = EnumFrameType.NULL;
    private String description = "";
    private String ygoprodeck_url = "";

    private EnumCardStockStatus stock_status = EnumCardStockStatus.OUT_OF_STOCK;
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
        if (node == null) {
            return cardSets;
        }

        JsonNode cardSetCollectionList = node.get("card_sets");
        if (cardSetCollectionList == null || cardSetCollectionList.isNull() || !cardSetCollectionList.isArray()) {
            return cardSets; // missing/nullable field => no sets
        }

        for (JsonNode cardSetCollection : cardSetCollectionList) {
            if (cardSetCollection == null || cardSetCollection.isNull() || !cardSetCollection.isObject()) {
                continue;
            }
            EnumCardSetRarity cardSetRarity = SimpleEnumUtils.findEnumValue(EnumCardSetRarity.class, cardSetCollection.path("set_rarity").asText().replaceAll("\\s", "_").replaceAll("-", "_"));

            cardSets.add(
                    CardSets.builder()
                            .set_name(cardSetCollection.path("set_name").asText(""))
                            .set_code(cardSetCollection.path("set_code").asText(""))
                            .set_rarity(cardSetRarity)
                            .set_rarity_code(cardSetCollection.path("set_rarity_code").asText(""))
                            .set_price(cardSetCollection.path("set_price").asDouble(-1))
                            .build()
            );
        }

        return cardSets;
    }

    private static List<CardImages> cardImageGroupsFromNode(JsonNode node) {
        List<CardImages> cardImages = new ArrayList<>();
        if (node == null) {
            return cardImages;
        }

        JsonNode imageCollectionList = node.get("card_images");
        if (imageCollectionList == null || imageCollectionList.isNull() || !imageCollectionList.isArray()) {
            return cardImages;
        }
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
        List<CardPrices> cardPrices = new ArrayList<>();
        if (node == null) {
            return cardPrices;
        }

        JsonNode priceCollectionList = node.get("card_prices");
        if (priceCollectionList == null || priceCollectionList.isNull() || !priceCollectionList.isArray()) {
            return cardPrices;
        }
        for (JsonNode imageCollection : priceCollectionList) {
            cardPrices.add(CardPrices.builder()
                    .cardmarket_price(imageCollection.get("cardmarket_price").asDouble(-1))
                    .tcgplayer_price(imageCollection.get("tcgplayer_price").asDouble(-1))
                    .ebay_price(imageCollection.get("ebay_price").asDouble(-1))
                    .amazon_price(imageCollection.get("amazon_price").asDouble(-1))
                    .coolstuffinc_price(imageCollection.get("coolstuffinc_price").asDouble(-1))
                    .build());
        }
        return cardPrices;
    }

    public static YughioCardDTO of(JsonNode node) {
        EnumCardType cardType = SimpleEnumUtils.findEnumValue(EnumCardType.class, node.get("type").asText().replaceAll("\\s", "_").replaceAll("-", "_"));
        EnumFrameType frameType = SimpleEnumUtils.findEnumValue(EnumFrameType.class, node.get("frameType").asText().replaceAll("\\s", "_").replaceAll("-", "_"));

        EnumCardStockStatus stockStatus = EnumCardStockStatus.OUT_OF_STOCK;
        if(node.get("stock_status") != null){
            stockStatus =  SimpleEnumUtils.findEnumValue(EnumCardStockStatus.class, node.get("stock_status").asText().replaceAll("\\s", "_").replaceAll("-", "_"));
        }

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
                .apiId(node.get("id").asInt())
                .name(node.get("name").asText().replaceAll("\"", ""))
                .type(cardType)
                .frameType(frameType)
                .description(node.get("desc").asText())
                .ygoprodeck_url(node.get("ygoprodeck_url").asText())

                .stock_status(stockStatus)

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
                .apiId(card.getApiId())
                .name(card.getName())
                .type(card.getType())
                .frameType(card.getFrameType())
                .description(card.getDescription())
                .ygoprodeck_url(card.getYgoprodeck_url())
                .stock_status(card.getStockStatus())
                .quantity_in_stock(card.getQuantity_in_stock())

                .cardConfig(card.getCardConfig())
                .cardProperties(card.getCardProperties())

                .card_sets(card.getCardSets())
                .card_images(card.getCardImages())
                .card_prices(card.getCardPrices())

                .build();
    }

    /// Non-static methodes ///

    public YughioCard toYughioCard() {
        YughioCard card = YughioCard.builder()
                .apiId(this.apiId)
                .name(this.name)
                .type(this.type)
                .frameType(this.frameType)
                .description(this.description)
                .ygoprodeck_url(this.ygoprodeck_url)
                .stockStatus(this.stock_status)
                .quantity_in_stock(this.quantity_in_stock)
                
                .cardConfig(this.cardConfig)
                .cardProperties(this.cardProperties)

                .cardSets(this.card_sets)
                .cardImages(this.card_images)
                .cardPrices(this.card_prices)
                .build();

        return card;
    }
}
