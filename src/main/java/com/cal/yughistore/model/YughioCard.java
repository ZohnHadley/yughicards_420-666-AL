package com.cal.yughistore.model;


import com.cal.yughistore.model.enums.EnumCardType;
import com.cal.yughistore.model.enums.EnumFrameType;
import com.cal.yughistore.model.properties.PropertiesMonsterCard;
import com.cal.yughistore.model.properties.PropertiesSpellCard;
import com.cal.yughistore.model.properties.PropertiesTrapCard;
import com.cal.yughistore.model.properties.SpecificProperties;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@AllArgsConstructor

@Getter
@Setter
@Table
@ToString
public class YughioCard {
    public enum ConfigType { TYPE_MONSTER, TYPE_SPELL, TYPE_TRAP }
    @Enumerated(EnumType.STRING)
    private ConfigType configType;
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "properties_id")
    private SpecificProperties properties;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int api_id;
    private String name = "no_name";
    private EnumCardType type = EnumCardType.NULL;
    private EnumFrameType frameType = EnumFrameType.NULL;
    private String description = "null";
    private String ygoprodeck_url = "null";

    @Builder
    public YughioCard(){}

    @Builder
    public YughioCard(ConfigType type){
        this.configType = type;
        if (type == ConfigType.TYPE_MONSTER) {
            this.properties = new PropertiesMonsterCard();
        } else if (type == ConfigType.TYPE_SPELL) {
            this.properties = new PropertiesSpellCard();
        } else if(type == ConfigType.TYPE_TRAP){
            this.properties = new PropertiesTrapCard();
        }
    }
}
