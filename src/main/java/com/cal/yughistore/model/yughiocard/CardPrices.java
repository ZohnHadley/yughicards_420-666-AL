package com.cal.yughistore.model.yughiocard;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@NoArgsConstructor
@Getter
@Setter
@ToString(exclude = "yughioCard")
public class CardPrices {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonBackReference
    private Long id;

    private Double cardmarketPrice;
    private Double tcgplayerPrice;
    private Double ebayPrice;
    private Double amazonPrice;
    private Double coolstuffincPrice;

    @ManyToOne
    @JsonBackReference
    private YughioCard yughioCard;

    @Builder
    public CardPrices(
            Long id,
            Double cardmarketPrice,
            Double tcgplayerPrice,
            Double ebayPrice,
            Double amazonPrice,
            Double coolstuffincPrice)
    {
        this.id = id;
        this.cardmarketPrice = cardmarketPrice;
        this.tcgplayerPrice = tcgplayerPrice;
        this.ebayPrice = ebayPrice;
        this.amazonPrice = amazonPrice;
        this.coolstuffincPrice = coolstuffincPrice;
    }

}
