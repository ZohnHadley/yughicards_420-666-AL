package com.cal.yughistore.service.dto.yughiocard;

import com.cal.yughistore.model.yughiocard.CardPrices;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class CardPricesDTO {
    private Long id;
    private String cardmarket_price;
    private String tcgplayer_price;
    private String ebay_price;
    private String amazon_price;
    private String coolstuffinc_price;


    public static CardPricesDTO fromCardPrices(CardPrices cardPrices) {
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
                this.id,
                this.cardmarket_price,
                this.tcgplayer_price,
                this.ebay_price,
                this.amazon_price,
                this.coolstuffinc_price
        );
    }

}
