package com.cal.yughistore.services.dto.yughiocard.cardProperties;

import com.cal.yughistore.model.yughiocard.enums.EnumNonMonsterCardRace;
import com.cal.yughistore.model.yughiocard.properties.PropertiesTrapCard;
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
        return PropertiesTrapCardDTO.builder().race(EnumNonMonsterCardRace.valueOf(node.get("race").asText())).build();
    }

    public static PropertiesTrapCardDTO of(PropertiesTrapCard trapCard) {
        return PropertiesTrapCardDTO.builder().race(trapCard.getRace()).build();
    }

    public PropertiesTrapCard toPropertiesTrapCard(){
        return PropertiesTrapCard.builder().race(this.race).build();
    }
}
