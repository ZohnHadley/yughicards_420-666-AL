package com.cal.yughistore.model.yughiocard;

import com.cal.yughistore.model.TextImbbededObject;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;


@Embeddable
@NoArgsConstructor
@Getter
@Builder
public class CardPrices implements TextImbbededObject {

    @Column(name = "cardmarket_price")
    private Double cardmarket_price = 0.0;
    @Column(name = "tcgplayer_price")
    private Double tcgplayer_price = 0.0;
    @Column(name = "ebay_price")
    private Double ebay_price = 0.0;
    @Column(name = "amazon_price")
    private Double amazon_price = 0.0;
    @Column(name = "coolstuffinc_price")
    private Double coolstuffinc_price = 0.0;

    @Builder
    public CardPrices(
            Double cardmarket_price,
            Double tcgplayer_price,
            Double ebay_price,
            Double amazon_price,
            Double coolstuffinc_price)
    {
        this.cardmarket_price = cardmarket_price;
        this.tcgplayer_price = tcgplayer_price;
        this.ebay_price = ebay_price;
        this.amazon_price = amazon_price;
        this.coolstuffinc_price = coolstuffinc_price;
    }

    @Override
    public String toEmbeddingText() {
        return String.format("cardmarket price %s, tcgplayer price %s, ebay price %s, amazon price %s, coolstuffinc price %s",
                cardmarket_price,
                tcgplayer_price,
                ebay_price,
                amazon_price,
                coolstuffinc_price
        );
    }

}
