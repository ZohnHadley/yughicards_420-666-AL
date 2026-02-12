package com.cal.yughistore.services.DTOs.Properties;

import com.cal.yughistore.model.enums.EnumNonMonsterCardRace;
import com.cal.yughistore.model.properties.PropertiesTrapCard;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.*;
 

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class DTOPropertiesTrapCard extends DTOCardProperties {
    private EnumNonMonsterCardRace race;

    public static DTOPropertiesTrapCard of(JsonNode node) {
        return DTOPropertiesTrapCard.builder().race(EnumNonMonsterCardRace.valueOf(node.get("race").asText())).build();
    }

    public static DTOPropertiesTrapCard of(PropertiesTrapCard trapCard) {
        return DTOPropertiesTrapCard.builder().race(trapCard.getRace()).build();
    }

    public PropertiesTrapCard toPropertiesTrapCard(){
        return PropertiesTrapCard.builder().race(this.race).build();
    }
}
