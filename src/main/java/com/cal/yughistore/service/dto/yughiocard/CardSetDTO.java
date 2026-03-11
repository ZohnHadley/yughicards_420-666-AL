package com.cal.yughistore.service.dto.yughiocard;

import com.cal.yughistore.model.yughiocard.CardSet;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class CardSetDTO {
    private String set_name;
    private String set_code;
    private String set_rarity;
    private String set_rarity_code;
    private String set_price;

    public static CardSetDTO of(CardSet cardSet) {
        return CardSetDTO.builder()
                .set_name(cardSet.getSet_name())
                .set_code(cardSet.getSet_code())
                .set_rarity(cardSet.getSet_rarity())
                .set_rarity_code(cardSet.getSet_rarity_code())
                .set_price(cardSet.getSet_price())
                .build();
    }

    public CardSet toCardSet() {
        return CardSet.builder()
                .set_name(this.set_name)
                .set_code(this.set_code)
                .set_rarity(this.set_rarity)
                .set_rarity_code(this.set_rarity_code)
                .set_price(this.set_price)
                .build();
    }
}