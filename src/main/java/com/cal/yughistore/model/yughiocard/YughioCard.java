package com.cal.yughistore.model.yughiocard;


import com.cal.yughistore.model.yughiocard.enums.EnumCardStockStatus;
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
@ToString
public class YughioCard {

    /// base card properties (all cards have these) ///
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int apiId;
    private String name = "";
    private EnumCardType type = EnumCardType.NULL;
    private EnumFrameType frameType = EnumFrameType.NULL;
    @Lob
    @Column(columnDefinition = "TEXT")
    private String description;
    @Column(length = 1000)
    private String ygoprodeck_url;

    private EnumCardStockStatus stockStatus = EnumCardStockStatus.OUT_OF_STOCK;
    private int quantity_in_stock = 0;

    @OneToMany(
            mappedBy = "yughioCard",
            cascade = jakarta.persistence.CascadeType.ALL
    )
    private List<CardSets> cardSets;
    @OneToMany(
            mappedBy = "yughioCard",
            cascade = jakarta.persistence.CascadeType.ALL
    )
    private List<CardImages> cardImages;
    @OneToMany(
            mappedBy = "yughioCard",
            cascade = jakarta.persistence.CascadeType.ALL
    )
    private List<CardPrices> cardPrices;

    /// Properties (depends on card type (trap, spell, monster, etc) ) ///
    private EnumPropertiesConfigType cardConfig;

    @OneToOne(mappedBy = "yughioCard", cascade = CascadeType.ALL)
    @JoinColumn(insertable = false, updatable = false)
    @JsonManagedReference
    private CardProperties cardProperties;



    @Builder
    public YughioCard(
            Long id,
            int apiId,
            String name,
            EnumCardType type,
            EnumFrameType frameType,
            String description,
            String ygoprodeck_url,
            EnumCardStockStatus stockStatus,
            int quantity_in_stock,
            EnumPropertiesConfigType cardConfig,
            CardProperties cardProperties,
            List<CardSets> cardSets,
            List<CardImages> cardImages,
            List<CardPrices> cardPrices
    ) {

        this.id = id;
        this.apiId = apiId;
        this.name = name;
        this.type = type;
        this.frameType = frameType;
        this.description = description;
        this.ygoprodeck_url = ygoprodeck_url;
        this.stockStatus = stockStatus;
        this.quantity_in_stock = quantity_in_stock;
        this.cardConfig = cardConfig;
        this.cardProperties = cardProperties;
        this.cardSets = (cardSets != null) ? cardSets : new ArrayList<>();
        this.cardImages = (cardImages != null) ? cardImages : new ArrayList<>();
        this.cardPrices = (cardPrices != null) ? cardPrices : new ArrayList<>();
    }
}
