package com.cal.yughistore.model.yughiocard;


import com.cal.yughistore.model.yughiocard.enums.EnumCardType;
import com.cal.yughistore.model.yughiocard.enums.EnumFrameType;
import com.cal.yughistore.model.yughiocard.enums.EnumPropertiesConfigType;
import com.cal.yughistore.model.yughiocard.properties.CardProperties;
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
@ToString(exclude = {"card_images", "card_prices", "cardProperties"})
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
    @Column(columnDefinition = "TEXT")
    private String description;
    @Column(length = 1000)
    private String ygoprodeck_url;

    @Column(nullable = false)
    private Integer quantity = 0;

    @Embedded
    private CardImages card_images = new CardImages();

    @Embedded
    private CardPrices card_prices = new CardPrices();

    /// Properties (depends on card type (trap, spell, monster, etc) ) ///
    private EnumPropertiesConfigType cardConfig;

    @OneToOne(mappedBy = "yughioCard", cascade = CascadeType.ALL)
    @JoinColumn(insertable = false, updatable = false)
    @JsonManagedReference
    private CardProperties cardProperties;


    @OneToMany(mappedBy = "yughioCard", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<CardSet> card_sets = new ArrayList<>();


    @Builder
    public YughioCard(
            Long id,
            int api_id,
            String name,
            EnumCardType type,
            Integer quantity,
            EnumFrameType frameType,
            String description,
            String ygoprodeck_url,
            EnumPropertiesConfigType cardConfig,
            CardProperties cardProperties,
            CardPrices card_prices,
            CardImages card_images,
            List<CardSet> card_sets
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
        this.card_images = card_images;
        this.card_prices = card_prices;
        this.card_sets = (card_sets != null) ? card_sets : new ArrayList<>();
        this.quantity = quantity;
    }

}
