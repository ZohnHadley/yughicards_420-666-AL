package com.cal.yughistore.model.yughiocard;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@NoArgsConstructor
@Getter
@Setter
public class CardPrices {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonBackReference
    private Long id;

    private Double cardmarket_price;
    private Double tcgplayer_price;
    private Double ebay_price;
    private Double amazon_price;
    private Double coolstuffinc_price;

    @ManyToOne
    @JsonBackReference
    private YughioCard yughioCard;

    @Builder
    public CardPrices(
            Long id,
            Double cardmarket_price,
            Double tcgplayer_price,
            Double ebay_price,
            Double amazon_price,
            Double coolstuffinc_price)
    {
        this.id = id;
        this.cardmarket_price = cardmarket_price;
        this.tcgplayer_price = tcgplayer_price;
        this.ebay_price = ebay_price;
        this.amazon_price = amazon_price;
        this.coolstuffinc_price = coolstuffinc_price;
    }

}
