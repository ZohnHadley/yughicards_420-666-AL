package com.cal.yughistore.service.dto.yughiocard;

import com.cal.yughistore.model.yughiocard.CardPrices;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CardPricesDTO {
    private Double cardmarket_price = 0.0;
    private Double tcgplayer_price = 0.0;
    private Double ebay_price = 0.0;
    private Double amazon_price = 0.0;
    private Double coolstuffinc_price = 0.0;


    public static CardPricesDTO of(CardPrices cardPrices) {
        if (cardPrices == null){
            return new CardPricesDTO();
        }

        return CardPricesDTO.builder()
                .cardmarket_price(cardPrices.getCardmarket_price())
                .tcgplayer_price(cardPrices.getTcgplayer_price())
                .ebay_price(cardPrices.getEbay_price())
                .amazon_price(cardPrices.getAmazon_price())
                .coolstuffinc_price(cardPrices.getCoolstuffinc_price())
                .build();
    }

    public CardPrices toCardPrices() {
        return new CardPrices(
                this.cardmarket_price,
                this.tcgplayer_price,
                this.ebay_price,
                this.amazon_price,
                this.coolstuffinc_price
        );
    }

}
