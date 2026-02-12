package com.cal.yughistore.services.DTOs.Properties;

import com.cal.yughistore.model.enums.EnumNonMonsterCardRace;
import com.cal.yughistore.model.properties.PropertiesSpellCard;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class DTOPropertiesSpellCard extends DTOCardProperties {
    private EnumNonMonsterCardRace race;

    public static DTOPropertiesSpellCard of(JsonNode node) {
        return DTOPropertiesSpellCard.builder().race(EnumNonMonsterCardRace.valueOf(node.get("race").asText())).build();
    }

    public static DTOPropertiesSpellCard of(PropertiesSpellCard spellCard) {
        return DTOPropertiesSpellCard.builder().race(spellCard.getRace()).build();
    }

    public PropertiesSpellCard toPropertiesSpellCard(){
        return PropertiesSpellCard.builder().race(this.race).build();
    }
}
