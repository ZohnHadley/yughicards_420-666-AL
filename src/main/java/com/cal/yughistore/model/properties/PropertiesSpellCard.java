package com.cal.yughistore.model.properties;

import com.cal.yughistore.model.enums.EnumNonMonsterCardRace;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.*;

@Entity
@NoArgsConstructor
@Getter
@Setter
@ToString
public class PropertiesSpellCard extends CardProperties {
    @Column(nullable = false)
    private EnumNonMonsterCardRace race;

    @Builder
    public PropertiesSpellCard(EnumNonMonsterCardRace race) {
        this.race = race;
    }

}
