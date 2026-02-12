package com.cal.yughistore.model;


import com.cal.yughistore.model.enums.EnumCardType;
import com.cal.yughistore.model.enums.EnumFrameType;
import com.cal.yughistore.model.enums.EnumPropertiesConfigType;
import com.cal.yughistore.model.properties.CardProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table
@ToString
public class YughioCard {

    /// base card properties (all cards have these) ///
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int api_id;
    private String name = "";
    private EnumCardType type = EnumCardType.NULL;
    private EnumFrameType frameType = EnumFrameType.NULL;
    @Lob
    private String description;
    @Column(length = 1000)
    private String ygoprodeck_url;

    @OneToMany(
            mappedBy = "yughioCard",
            cascade = jakarta.persistence.CascadeType.ALL
    )
    private List<CardImages> card_images;
//    @OneToMany(
//            mappedBy = "yughioCard",
//            cascade = jakarta.persistence.CascadeType.ALL
//    )
//    private List<CardPrices> card_prices;

    /// Properties (depends on card type (trap, spell, monster, etc) ) ///
    private EnumPropertiesConfigType cardConfig;

    @OneToOne(mappedBy = "yughioCard", cascade = CascadeType.ALL)
    @JoinColumn(insertable = false, updatable = false)
    @JsonManagedReference
    private CardProperties cardProperties;



    @Builder
    public YughioCard(
            Long id,
            int api_id,
            String name,
            EnumCardType type,
            EnumFrameType frameType,
            String description,
            String ygoprodeck_url,
            EnumPropertiesConfigType cardConfig,
            CardProperties cardProperties,
            List<CardImages> card_images,
            List<CardPrices> card_prices
    ) {

        this.id = id;
        this.api_id = api_id;
        this.name = name;
        this.type = type;
        this.frameType = frameType;
        this.description = description;
        this.ygoprodeck_url = ygoprodeck_url;
        this.cardConfig = cardConfig;
        this.cardProperties = cardProperties;
//        this.card_images = (card_images != null) ? card_images : new ArrayList<>();
//        this.card_prices = (card_prices != null) ? card_prices : new ArrayList<>();
    }
}
