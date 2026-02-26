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

    String set_name;
    String set_code;
    EnumCardSetRarity set_rarity;
    String set_rarity_code;
    Double set_price;

    @ManyToOne
    @JsonBackReference
    private YughioCard yughioCard;

    @Builder
    public CardSets(
            String set_name,
            String set_code,
            EnumCardSetRarity set_rarity,
            String set_rarity_code,
            Double set_price
    ){
        this.set_name = set_name;
        this.set_code = set_code;
        this.set_rarity = set_rarity;
        this.set_rarity_code = set_rarity_code;
        this.set_price = set_price;
    }
}
