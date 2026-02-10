package com.cal.yughistore.model.properties;

import com.cal.yughistore.model.enums.EnumNonMonsterCardRace;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
public class PropertiesTrapCard extends SpecificProperties{
    private EnumNonMonsterCardRace race;
}
