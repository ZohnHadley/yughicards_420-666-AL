package com.cal.yughistore.service.dto.yughiocard;

import com.cal.yughistore.model.yughiocard.YughioCard;
import com.cal.yughistore.model.yughiocard.enums.*;
import com.cal.yughistore.service.dto.yughiocard.cardProperties.CardPropertiesDTO;
import com.cal.yughistore.service.dto.yughiocard.cardProperties.PropertiesMonsterCardDTO;
import com.cal.yughistore.service.dto.yughiocard.cardProperties.PropertiesSpellCardDTO;
import com.cal.yughistore.service.dto.yughiocard.cardProperties.PropertiesTrapCardDTO;
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
@ToString(exclude = {"card_images", "card_prices", "cardProperties"})
public class YughioCardDTO {
    private static final Logger logger = LoggerFactory.getLogger(YughioCardDTO.class);

    private Long id = null;
    private int api_id;
    private String name = "null card";
    private EnumCardType type = EnumCardType.NULL;
    private EnumFrameType frameType = EnumFrameType.NULL;
    private String description = "";
    private String ygoprodeck_url = "";

    private EnumPropertiesConfigType cardConfig = EnumPropertiesConfigType.NULL;
    @NonNull
    @Builder.Default
    private CardPropertiesDTO cardProperties = new CardPropertiesDTO();
    @NonNull
    @Builder.Default
    private CardImagesDTO card_images = new CardImagesDTO();
    @NonNull
    @Builder.Default
    private CardPricesDTO card_prices = new CardPricesDTO();
    @NonNull
    @Builder.Default
    private List<CardSetDTO> card_sets = new ArrayList<>();

    private int quantity = 0;

//    private String rarity = "";
//    private String setName = "";
//    private String setCode = "";

    // ── Static helpers ──────────────────────────────────────────────────────

    public static CardPropertiesDTO getCardProperties(EnumCardType cardType) {
        if (cardType.name().toUpperCase().contains(EnumPropertiesConfigType.MONSTER.getName())) {
            return new PropertiesMonsterCardDTO();
        } else if (cardType.name().toUpperCase().contains(EnumPropertiesConfigType.SPELL.getName())) {
            return new PropertiesSpellCardDTO();
        } else if (cardType.name().toUpperCase().contains(EnumPropertiesConfigType.TRAP.getName())) {
            return new PropertiesTrapCardDTO();
        }
        return new CardPropertiesDTO();
    }

    private static CardImagesDTO cardImageGroupsFromNode(JsonNode node) {
        JsonNode nodeData = node.get("card_images").get(0);

        return CardImagesDTO.builder()
                .images_id(nodeData.get("id").asInt())
                .image_url(nodeData.get("image_url").asText())
                .image_url_small(nodeData.get("image_url_small").asText())
                .image_url_cropped(nodeData.get("image_url_cropped").asText())
                .build();
    }

    private static CardPricesDTO cardPricesFromNode(JsonNode node) {
        JsonNode nodeData = node.get("card_prices").get(0);

        return CardPricesDTO.builder()
                .cardmarket_price(nodeData.get("cardmarket_price").asDouble(0))
                .tcgplayer_price(nodeData.get("tcgplayer_price").asDouble(0))
                .ebay_price(nodeData.get("ebay_price").asDouble(0))
                .amazon_price(nodeData.get("amazon_price").asDouble(0))
                .coolstuffinc_price(nodeData.get("coolstuffinc_price").asDouble(0))
                .build();


    }

    private static List<CardSetDTO> cardSetsFromNode(JsonNode node) {
        List<CardSetDTO> sets = new ArrayList<>();
        JsonNode list = node.get("card_sets");
        if (list == null || !list.isArray()) return sets;
        for (JsonNode s : list) {
            sets.add(CardSetDTO.builder()
                    .set_name(s.get("set_name").asText(""))
                    .set_code(s.get("set_code").asText(""))
                    .set_rarity(s.get("set_rarity").asText(""))
                    .set_rarity_code(s.get("set_rarity_code").asText(""))
                    .set_price(s.get("set_price").asText("0.00"))
                    .build());
        }
        return sets;
    }

    // ── of(JsonNode) — utilisé lors de l'import depuis YGOPRODeck ──────────

    public static YughioCardDTO of(JsonNode node) {
        EnumCardType cardType = SimpleEnumUtils.findEnumValue(
                EnumCardType.class,
                node.get("type").asText().replaceAll(" ", "_").replaceAll("-", "_"));
        EnumFrameType frameType = SimpleEnumUtils.findEnumValue(
                EnumFrameType.class,
                node.get("frameType").asText().replaceAll(" ", "_").replaceAll("-", "_"));

        CardPropertiesDTO cardProperties = getCardProperties(cardType);

        return YughioCardDTO.builder()
                .api_id(node.get("id").asInt())
                .name(node.get("name").asText().replaceAll("\"", ""))
                .type(cardType)
                .frameType(frameType)
                .description(node.get("desc").asText())
                .quantity(0)
                .ygoprodeck_url(node.get("ygoprodeck_url").asText())
                .cardProperties(cardProperties)
                .cardConfig(populateCardProperties(cardProperties, node))
                .card_images(cardImageGroupsFromNode(node))
                .card_prices(cardPricesFromNode(node))
                .card_sets(cardSetsFromNode(node))
                .build();
    }

    private static EnumPropertiesConfigType populateCardProperties(CardPropertiesDTO cardProperties, JsonNode node) {
        String normalizedRace = normalizeEnumName(node.get("race").asText());

        if (cardProperties instanceof PropertiesMonsterCardDTO monster) {
            String normalizedAttribute = normalizeEnumName(node.get("attribute").asText());

            monster.setAtk(node.get("atk").asInt());
            monster.setDef(node.get("def").asInt());
            monster.setLevel(node.get("level").asInt());
            monster.setRace(SimpleEnumUtils.findEnumValue(EnumMonsterCardRace.class, normalizedRace));
            monster.setAttribute(SimpleEnumUtils.findEnumValue(EnumCardAttribute.class, normalizedAttribute));

            return EnumPropertiesConfigType.MONSTER;
        }

        if (cardProperties instanceof PropertiesSpellCardDTO spellCard) {
            spellCard.setRace(SimpleEnumUtils.findEnumValue(EnumNonMonsterCardRace.class, normalizedRace));
            return EnumPropertiesConfigType.SPELL;
        }

        if (cardProperties instanceof PropertiesTrapCardDTO trapCard) {
            trapCard.setRace(SimpleEnumUtils.findEnumValue(EnumNonMonsterCardRace.class, normalizedRace));
            return EnumPropertiesConfigType.TRAP;
        }

        return EnumPropertiesConfigType.NULL;
    }

    private static String normalizeEnumName(String value) {
        return value.replaceAll(" ", "_").replaceAll("-", "_");
    }


    public static CardPropertiesDTO toCardPropertiesDto(YughioCard card) {
        if (card.getCardProperties() == null) {
            return new CardPropertiesDTO();
        }
        return CardPropertiesDTO.of(card.getCardProperties());
    }

    private static List<CardSetDTO> toCardSetDtos(YughioCard card) {
        if (card.getCard_sets() == null) {
            return new ArrayList<>();
        }
        return card.getCard_sets().stream()
                .map(CardSetDTO::of)
                .toList();
    }

    public static YughioCardDTO of(YughioCard card) {
        return YughioCardDTO.builder()
                .id(card.getId())
                .api_id(card.getApi_id())
                .name(card.getName())
                .quantity(card.getQuantity())
                .type(card.getType())
                .frameType(card.getFrameType())
                .description(card.getDescription())
                .ygoprodeck_url(card.getYgoprodeck_url())
                .cardConfig(card.getCardConfig())
                .cardProperties(toCardPropertiesDto(card))
                .card_images(CardImagesDTO.of(card.getCard_images()))
                .card_prices(CardPricesDTO.of(card.getCard_prices()))
                .card_sets(toCardSetDtos(card))
                .build();
    }

    // ── toYughioCard() — utilisé pour sauvegarder en DB ────────────────────

    public YughioCard toYughioCard() {
        return YughioCard.builder()
                .id(this.getId())
                .api_id(this.getApi_id())
                .name(this.getName())
                .quantity(this.getQuantity())
                .type(this.getType())
                .frameType(this.getFrameType())
                .description(this.getDescription())
                .ygoprodeck_url(this.getYgoprodeck_url())
                .cardConfig(this.getCardConfig())
                .card_prices(this.getCard_prices().toCardPrices())
                .card_images(this.getCard_images().toCardImages())
                .card_sets(this.getCard_sets().stream().map(CardSetDTO::toCardSet).toList())
                .build();
    }
}