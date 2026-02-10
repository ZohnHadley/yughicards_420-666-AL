package com.cal.yughistore.model;


import com.cal.yughistore.model.enums.EnumConfigType;
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
@NoArgsConstructor
@Getter
@Setter
@Table
@ToString
public class YughioCard {
    @Enumerated(EnumType.STRING)
    private EnumConfigType enumConfigType;

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
    public YughioCard(Long id, int api_id, String name, EnumConfigType type, EnumFrameType frameType, String description, String ygoprodeck_url){
        this.id = id;
        this.api_id = api_id;
        this.name = name;
        this.frameType = frameType;
        this.description = description;
        this.ygoprodeck_url = ygoprodeck_url;


        this.enumConfigType = type;
        if (type == EnumConfigType.TYPE_MONSTER) {
            this.properties = new PropertiesMonsterCard();
        } else if (type == EnumConfigType.TYPE_SPELL) {
            this.properties = new PropertiesSpellCard();
        } else if(type == EnumConfigType.TYPE_TRAP){
            this.properties = new PropertiesTrapCard();
        }
    }
}
