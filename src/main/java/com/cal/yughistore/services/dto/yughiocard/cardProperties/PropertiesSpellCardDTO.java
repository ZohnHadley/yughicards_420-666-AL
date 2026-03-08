package com.cal.yughistore.services.dto.yughiocard.cardProperties;

import com.cal.yughistore.model.yughiocard.enums.EnumNonMonsterCardRace;
import com.cal.yughistore.model.yughiocard.properties.PropertiesSpellCard;
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
public class PropertiesSpellCardDTO extends CardPropertiesDTO {
    private EnumNonMonsterCardRace race;

    public static PropertiesSpellCardDTO of(JsonNode node) {
        EnumNonMonsterCardRace race = SimpleEnumUtils.findEnumValue(EnumNonMonsterCardRace.class, node.get("race").asText());
        return PropertiesSpellCardDTO.builder().race(race).build();
    }

    public static PropertiesSpellCardDTO of(PropertiesSpellCard spellCard) {
        return PropertiesSpellCardDTO.builder().race(spellCard.getRace()).build();
    }

    public PropertiesSpellCard toPropertiesSpellCard(){
        return PropertiesSpellCard.builder().race(this.getRace()).build();
    }
}
