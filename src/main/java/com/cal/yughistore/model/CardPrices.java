package com.cal.yughistore.model;

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

    private String cardmarket_price;
    private String tcgplayer_price;
    private String ebay_price;
    private String amazon_price;
    private String coolstuffinc_price;

    @ManyToOne
    @JsonBackReference
    private YughioCard yughioCard;

    @Builder
    public CardPrices(
            Long id,
            String cardmarket_price,
            String tcgplayer_price,
            String ebay_price,
            String amazon_price,
            String coolstuffinc_price)
    {
        this.id = id;
        this.cardmarket_price = cardmarket_price;
        this.tcgplayer_price = tcgplayer_price;
        this.ebay_price = ebay_price;
        this.amazon_price = amazon_price;
        this.coolstuffinc_price = coolstuffinc_price;
    }

}
