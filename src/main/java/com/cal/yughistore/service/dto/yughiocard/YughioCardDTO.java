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

    private Long id;
    private int api_id;
    private String name = "";
    private EnumCardType type = EnumCardType.NULL;
    private EnumFrameType frameType = EnumFrameType.NULL;
    private String description = "";
    private String ygoprodeck_url = "";

    private EnumPropertiesConfigType cardConfig;
    @NonNull
    @Builder.Default
    private CardPropertiesDTO cardProperties = new CardPropertiesDTO();
    @NonNull
    @Builder.Default
    private List<CardImagesDTO> card_images = new ArrayList<>();
    @NonNull
    @Builder.Default
    private List<CardPricesDTO> card_prices = new ArrayList<>();
    @NonNull
    @Builder.Default
    private List<CardSetDTO> card_sets = new ArrayList<>();

    private int quantity = 0;

    private String rarity = "";
    private String setName = "";
    private String setCode = "";

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

    private static List<CardImagesDTO> cardImageGroupsFromNode(JsonNode node) {
        List<CardImagesDTO> cardImages = new ArrayList<>();
        JsonNode list = node.get("card_images");
        if (list == null || !list.isArray()) return cardImages;
        for (JsonNode img : list) {
            cardImages.add(CardImagesDTO.builder()
                    .image_group_api_id(img.get("id").asInt())
                    .image_url(img.get("image_url").asText(""))
                    .image_url_small(img.get("image_url_small").asText(""))
                    .image_url_cropped(img.get("image_url_cropped").asText(""))
                    .build());
        }
        return cardImages;
    }

    private static List<CardPricesDTO> cardPricesFromNode(JsonNode node) {
        List<CardPricesDTO> prices = new ArrayList<>();
        JsonNode list = node.get("card_prices");
        if (list == null || !list.isArray()) return prices;
        for (JsonNode p : list) {
            prices.add(
                    CardPricesDTO.builder()
                            .cardmarket_price(p.get("cardmarket_price").asText(""))
                            .tcgplayer_price(p.get("tcgplayer_price").asText(""))
                            .ebay_price(p.get("ebay_price").asText(""))
                            .amazon_price(p.get("amazon_price").asText(""))
                            .coolstuffinc_price(p.get("coolstuffinc_price").asText(""))
                            .build()
            );
        }
        return prices;
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

    // ── of(YughioCard) — utilisé pour toutes les réponses API ──────────────
    // ⚠ C'était ici le bug : card_sets n'était jamais retourné au frontend


    public static CardPropertiesDTO toCardPropertiesDto(YughioCard card) {
        if (card.getCardProperties() == null) {
            return new CardPropertiesDTO();
        }
        return CardPropertiesDTO.of(card.getCardProperties());
    }

    private static List<CardImagesDTO> toCardImageDtos(YughioCard card) {
        if (card.getCard_images() == null) {
            return List.of();
        }
        return card.getCard_images().stream()
                .map(img -> CardImagesDTO.builder()
                        .id(img.getId())
                        .image_group_api_id(img.getImage_group_api_id())
                        .image_url(img.getImage_url())
                        .image_url_small(img.getImage_url_small())
                        .image_url_cropped(img.getImage_url_cropped())
                        .build())
                .toList();
    }

    private static List<CardPricesDTO> toCardPriceDtos(YughioCard card) {
        if (card.getCard_prices() == null) {
            return new ArrayList<>();
        }
        return card.getCard_prices().stream()
                .map(price -> CardPricesDTO.builder()
                        .id(price.getId())
                        .cardmarket_price(price.getCardmarket_price())
                        .tcgplayer_price(price.getTcgplayer_price())
                        .ebay_price(price.getEbay_price())
                        .amazon_price(price.getAmazon_price())
                        .coolstuffinc_price(price.getCoolstuffinc_price())
                        .build())
                .toList();
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
                .rarity(card.getRarity())
                .setName(card.getSetName())
                .setCode(card.getSetCode())
                .type(card.getType())
                .frameType(card.getFrameType())
                .description(card.getDescription())
                .ygoprodeck_url(card.getYgoprodeck_url())
                .cardConfig(card.getCardConfig())
                .cardProperties(toCardPropertiesDto(card))
                .card_images(toCardImageDtos(card))
                .card_prices(toCardPriceDtos(card))
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
                .rarity(this.getRarity())
                .setName(this.getSetName())
                .setCode(this.getSetCode() == null ? "" : this.getSetCode())
                .type(this.getType())
                .frameType(this.getFrameType())
                .description(this.getDescription())
                .ygoprodeck_url(this.getYgoprodeck_url())
                .cardConfig(this.getCardConfig())
//                .cardProperties(this.getCardProperties().toCardProperties())
//                .card_images(this.getCard_images().stream().map(CardImagesDTO::toCardImages).toList())
//                .card_prices(this.getCard_prices().stream().map(CardPricesDTO::toCardPrices).toList())
                // card_sets est sauvegardé séparément dans YughioCardService.saveCardSets()
                .build();
    }
}