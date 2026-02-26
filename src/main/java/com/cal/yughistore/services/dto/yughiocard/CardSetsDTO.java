package com.cal.yughistore.services.dto.yughiocard;

import com.cal.yughistore.model.yughiocard.CardSets;
import com.cal.yughistore.model.yughiocard.enums.EnumCardSetRarity;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.*;

import static com.cal.yughistore.services.dto.yughiocard.util.CardDtoUtil.findPropertyEnumValue;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class CardSetsDTO {

    private Long id;
    String set_name;
    String set_code;
    EnumCardSetRarity set_rarity;
    String set_rarity_code;
    Double set_price;
    Integer quantity_in_stock = 0;

    public static CardSetsDTO of(JsonNode jsonNode) {
        EnumCardSetRarity setRarity = findPropertyEnumValue(EnumCardSetRarity.class, jsonNode.get("set_rarity").asText());

        return CardSetsDTO.builder()
                .set_name(jsonNode.get("set_name").asText())
                .set_code(jsonNode.get("set_code").asText())
                .set_rarity(setRarity)
                .set_rarity_code(jsonNode.get("set_rarity_code").asText())
                .set_price(jsonNode.get("set_price").asDouble())
                .quantity_in_stock(0)
                .build();
    }

    public static CardSetsDTO of(CardSets cardSets) {
        return CardSetsDTO.builder()
                .id(cardSets.getId())
                .set_name(cardSets.getSetName())
                .set_code(cardSets.getSetCode())
                .set_rarity(cardSets.getSetRarity())
                .set_rarity_code(cardSets.getSetRarityCode())
                .set_price(cardSets.getSetPrice())
                .quantity_in_stock(cardSets.getQuantityInStock())
                .build();
    }

    public CardSets toCardSets(){
        return CardSets.builder()
                .id(this.getId())
                .setName(this.getSet_name())
                .setCode(this.getSet_code())
                .setRarity(this.getSet_rarity())
                .setRarityCode(this.getSet_rarity_code())
                .setPrice(this.getSet_price())
                .quantityInStock(this.getQuantity_in_stock())
                .build();
    }

}
