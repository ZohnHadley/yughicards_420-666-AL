package com.cal.yughistore.services.dto.yughiocard.cardProperties;

import com.cal.yughistore.model.yughiocard.enums.EnumCardAttribute;
import com.cal.yughistore.model.yughiocard.enums.EnumMonsterCardRace;
import com.cal.yughistore.model.yughiocard.properties.PropertiesMonsterCard;
import com.cal.yughistore.utils.SimpleEnumUtils;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class PropertiesMonsterCardDTO extends CardPropertiesDTO {
    private int atk;
    private int def;
    private int level;
    private EnumMonsterCardRace race;
    private EnumCardAttribute attribute;


    public static PropertiesMonsterCardDTO of(JsonNode node){
        EnumMonsterCardRace race = SimpleEnumUtils.findEnumValue(EnumMonsterCardRace.class, node.get("race").asText());
        EnumCardAttribute attribute = SimpleEnumUtils.findEnumValue(EnumCardAttribute.class, node.get("attribute").asText());

        return PropertiesMonsterCardDTO.builder()
                .atk(node.get("atk").asInt())
                .def(node.get("def").asInt())
                .level(node.get("level").asInt())
                .race(race)
                .attribute(attribute)
                .build();
    }

    public static PropertiesMonsterCardDTO of(PropertiesMonsterCard properties){
        return PropertiesMonsterCardDTO.builder()
                .atk(properties.getAtk())
                .def(properties.getDef())
                .level(properties.getLevel())
                .race(properties.getRace())
                .attribute(properties.getAttribute())
                .build();
    }

    public PropertiesMonsterCard toPropertiesMonsterCard(){
        return PropertiesMonsterCard.builder()
                .atk(this.atk)
                .def(this.def)
                .level(this.level)
                .race(this.race)
                .attribute(this.attribute)
                .build();
    }
}
