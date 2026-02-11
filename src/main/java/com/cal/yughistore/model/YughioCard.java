package com.cal.yughistore.model;


import com.cal.yughistore.model.enums.EnumPropertiesConfigType;
import com.cal.yughistore.model.enums.EnumCardType;
import com.cal.yughistore.model.enums.EnumFrameType;
import com.cal.yughistore.model.properties.PropertiesMonsterCard;
import com.cal.yughistore.model.properties.PropertiesSpellCard;
import com.cal.yughistore.model.properties.PropertiesTrapCard;
import com.cal.yughistore.model.properties.CardProperties;
import jakarta.persistence.*;
import lombok.*;

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
    private String name = "no_name";
    private EnumCardType type = EnumCardType.NULL;
    private EnumFrameType frameType = EnumFrameType.NULL;
    @Lob
    private String description;
    @Column(length = 500)
    private String ygoprodeck_url;

    /// Properties (depends on card type (trap, spell, monster, etc) ) ///
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "cardProperties_id")
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
            EnumPropertiesConfigType propertiesConfigType
    ) {

        this.id = id;
        this.api_id = api_id;
        this.name = name;
        this.type = type;
        this.frameType = frameType;
        this.description = description;
        this.ygoprodeck_url = ygoprodeck_url;

        if (this.type.name().toUpperCase().contains(EnumPropertiesConfigType.TYPE_MONSTER.getName())) {
            this.cardProperties = new PropertiesMonsterCard();
        }
        else if (this.type.name().toUpperCase().contains(EnumPropertiesConfigType.TYPE_SPELL.getName())) {
            this.cardProperties = new PropertiesSpellCard();
        }
        else if (this.type.name().toUpperCase().contains(EnumPropertiesConfigType.TYPE_TRAP.getName())) {
            this.cardProperties = new PropertiesTrapCard();
        }
    }

    private void typaSet() {

    }
}
