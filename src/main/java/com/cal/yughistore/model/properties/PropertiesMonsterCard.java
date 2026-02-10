package com.cal.yughistore.model.properties;


import com.practice.springaiollama.model.enums.EnumCardAttribute;
import com.practice.springaiollama.model.enums.EnumMonsterCardRace;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
public class PropertiesMonsterCard extends SpecificProperties {

    private int atk;
    private int def;
    private int level;
    private EnumMonsterCardRace race;
    private EnumCardAttribute cardAttribute;

}
