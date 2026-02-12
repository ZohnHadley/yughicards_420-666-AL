package com.cal.yughistore.model.properties;


import com.cal.yughistore.model.enums.EnumCardAttribute;
import com.cal.yughistore.model.enums.EnumMonsterCardRace;
import jakarta.persistence.Column;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
public class PropertiesMonsterCard extends CardProperties {

    @Column(nullable = false)
    private int atk;
    @Column(nullable = false)
    private int def;
    @Column(nullable = false)
    private int level;
    @Column(nullable = false)
    private EnumMonsterCardRace race;
    @Column(nullable = false)
    private EnumCardAttribute attribute;

}
