package com.cal.yughistore.model.yughiocard.properties;


import com.cal.yughistore.model.yughiocard.enums.EnumCardAttribute;
import com.cal.yughistore.model.yughiocard.enums.EnumMonsterCardRace;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.*;

@Entity
@NoArgsConstructor
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

    @Builder
    public PropertiesMonsterCard(
            int atk,
            int def,
            int level,
            EnumMonsterCardRace race,
            EnumCardAttribute attribute
    ) {
        this.atk = atk;
        this.def = def;
        this.level = level;
        this.race = race;
        this.attribute = attribute;
    }

    @Override
    public String toEmbeddingText() {
        return String.format("card atk %s, card def %s, card level %s, card race %s, card attribute %s",
                atk,
                def,
                level,
                race,
                attribute
        );
    }

}
