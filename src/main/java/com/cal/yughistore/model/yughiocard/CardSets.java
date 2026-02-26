package com.cal.yughistore.model.yughiocard;

import com.cal.yughistore.model.yughiocard.enums.EnumCardSetRarity;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@NoArgsConstructor
@Getter
@Setter
public class CardSets {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonBackReference
    private Long id;

    String setName;
    String setCode;
    EnumCardSetRarity setRarity;
    String setRarityCode;
    Double setPrice;

    Integer quantityInStock = 0;

    @ManyToOne
    @JsonBackReference
    private YughioCard yughioCard;

    @Builder
    public CardSets(
            Long id,
            String setName,
            String setCode,
            EnumCardSetRarity setRarity,
            String setRarityCode,
            Double setPrice,
            Integer quantityInStock
    ){
        this.id = id;
        this.setName = setName;
        this.setCode = setCode;
        this.setRarity = setRarity;
        this.setRarityCode = setRarityCode;
        this.setPrice = setPrice;
        this.quantityInStock = quantityInStock;
    }
}
