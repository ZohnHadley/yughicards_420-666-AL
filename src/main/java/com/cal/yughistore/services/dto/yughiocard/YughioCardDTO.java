package com.cal.yughistore.services.dto.yughiocard;

import com.cal.yughistore.model.yughiocard.YughioCard;
import com.cal.yughistore.model.yughiocard.enums.*;
import com.cal.yughistore.model.yughiocard.properties.PropertiesMonsterCard;
import com.cal.yughistore.model.yughiocard.properties.PropertiesSpellCard;
import com.cal.yughistore.model.yughiocard.properties.PropertiesTrapCard;
import com.cal.yughistore.model.yughiocard.properties.CardProperties;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

import static com.cal.yughistore.services.dto.yughiocard.util.CardDtoUtil.*;

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
    @Builder.Default
    private Long id = -1L;

    @Builder.Default
    private int api_id = -1;

    @Builder.Default
    private String name = "";

    @Builder.Default
    private EnumCardType type = EnumCardType.NULL;

    @Builder.Default
    private EnumFrameType frame_type = EnumFrameType.NULL;

    @Builder.Default
    private String description = "";

    @Builder.Default
    private String ygoprodeck_url = "";

    @Builder.Default
    private EnumCardStockStatus stock_status = EnumCardStockStatus.OUT_OF_STOCK;

    @Builder.Default
    private int quantity_in_stock = 0;

    /// Properties (depends on card type (trap, spell, monster, etc) ) ///
    @Builder.Default
    private EnumPropertiesConfigType card_config = EnumPropertiesConfigType.NULL;

    private CardProperties card_properties;

    /// ///
    @Builder.Default
    private List<CardSetsDTO> card_sets = new ArrayList<>();

    @Builder.Default
    private List<CardImagesDTO> card_images = new ArrayList<>();

    @Builder.Default
    private List<CardPricesDTO> card_prices = new ArrayList<>();




    /// static methode ///
    ///
    public static YughioCardDTO of(JsonNode node) {
        EnumCardType cardType = findPropertyEnumValue(EnumCardType.class, node.path("type").asText(""));
        EnumFrameType frameType = findPropertyEnumValue(EnumFrameType.class, node.path("frameType").asText(""));

        EnumCardStockStatus stockStatus = EnumCardStockStatus.OUT_OF_STOCK;
        String stockStatusText = node.path("stock_status").asText(null);
        if (stockStatusText != null && !stockStatusText.isBlank()) {
            stockStatus = findPropertyEnumValue(EnumCardStockStatus.class, stockStatusText);
        }

        CardProperties cardProperties = checkCardProperties(cardType);
        EnumPropertiesConfigType cardConfigType;

        switch (cardProperties) {
            case PropertiesMonsterCard monster -> {
                cardConfigType = EnumPropertiesConfigType.MONSTER;

                monster.setAtk(node.path("atk").asInt());
                monster.setDef(node.path("def").asInt());
                monster.setLevel(node.path("level").asInt());

                EnumMonsterCardRace race = findPropertyEnumValue(EnumMonsterCardRace.class, node.path("race").asText(""));
                EnumCardAttribute attribute = findPropertyEnumValue(EnumCardAttribute.class, node.path("attribute").asText(""));

                monster.setRace(race);
                monster.setAttribute(attribute);
            }
            case PropertiesSpellCard propertiesSpellCard -> {
                cardConfigType = EnumPropertiesConfigType.SPELL;

                EnumNonMonsterCardRace race = findPropertyEnumValue(EnumNonMonsterCardRace.class, node.path("race").asText(""));
                propertiesSpellCard.setRace(race);
            }
            case PropertiesTrapCard propertiesTrapCard -> {
                cardConfigType = EnumPropertiesConfigType.TRAP;

                EnumNonMonsterCardRace race = findPropertyEnumValue(EnumNonMonsterCardRace.class, node.path("race").asText(""));
                propertiesTrapCard.setRace(race);
            }
            default -> cardConfigType = EnumPropertiesConfigType.NULL;
        }

        return YughioCardDTO.builder()
                .api_id(node.path("id").asInt(-1))
                .name(node.path("name").asText("").replace("\"", ""))
                .type(cardType)
                .frame_type(frameType)
                .description(node.path("desc").asText(""))
                .ygoprodeck_url(node.path("ygoprodeck_url").asText(""))
                .stock_status(stockStatus)
                .card_config(cardConfigType)
                .card_properties(cardProperties)
                .card_sets(cardSetsDTOFromNode(node))
                .card_images(cardImageDTOFromNode(node))
                .card_prices(cardPricesDTOFromNode(node))
                .build();
    }


    public static YughioCardDTO of(YughioCard card) {
        return YughioCardDTO.builder()
                .id(card.getId())
                .api_id(card.getApiId())
                .name(card.getName())
                .type(card.getType())
                .frame_type(card.getFrameType())
                .description(card.getDescription())
                .ygoprodeck_url(card.getYgoprodeck_url())
                .stock_status(card.getStockStatus())
                .quantity_in_stock(card.getQuantity_in_stock())

                .card_config(card.getCardConfig())
                .card_properties(card.getCardProperties())

                .card_sets(cardSetsDTOFromList(card.getCardSets()))
                .card_images(cardImageDTOFromList(card.getCardImages()))
                .card_prices(cardPricesDTOFromList(card.getCardPrices()))

                .build();
    }

    /// Non-static methodes ///

    public YughioCard toYughioCard() {
        return YughioCard.builder()
                //                .id(this.id)
                .apiId(this.api_id)
                .name(this.name)
                .type(this.type)
                .frameType(this.frame_type)
                .description(this.description)
                .ygoprodeck_url(this.ygoprodeck_url)
                .stockStatus(this.stock_status)
                .quantity_in_stock(this.quantity_in_stock)
                .cardConfig(this.card_config)
                .cardProperties(this.card_properties)
                .cardSets(cardSetsFromList(this.card_sets))
                .cardImages(cardImagesFromList(this.card_images))
                .cardPrices(cardPricesFromList(this.card_prices))
                .build();
    }
}
