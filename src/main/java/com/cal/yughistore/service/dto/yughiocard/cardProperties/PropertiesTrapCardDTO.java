package com.cal.yughistore.service.dto.yughiocard.cardProperties;

import com.cal.yughistore.model.yughiocard.enums.EnumNonMonsterCardRace;
import com.cal.yughistore.model.yughiocard.properties.PropertiesTrapCard;
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
public class PropertiesTrapCardDTO extends CardPropertiesDTO {
    private EnumNonMonsterCardRace race;

    public static PropertiesTrapCardDTO of(JsonNode node) {
        EnumNonMonsterCardRace race = SimpleEnumUtils.findEnumValue(EnumNonMonsterCardRace.class, node.get("race").asText().toUpperCase().replaceAll(" ","_").replaceAll("-","_"));
        return PropertiesTrapCardDTO.builder().race(race).build();
    }

    public static PropertiesTrapCardDTO of(PropertiesTrapCard trapCard) {
        return PropertiesTrapCardDTO.builder().race(trapCard.getRace()).build();
    }

    public PropertiesTrapCard toPropertiesTrapCard(){
        return PropertiesTrapCard.builder().race(this.getRace()).build();
    }
}
