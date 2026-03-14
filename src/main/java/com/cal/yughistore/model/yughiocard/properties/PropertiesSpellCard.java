package com.cal.yughistore.model.yughiocard.properties;

import com.cal.yughistore.model.yughiocard.enums.EnumNonMonsterCardRace;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.*;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class PropertiesSpellCard extends CardProperties {
    @Column(nullable = false)
    private EnumNonMonsterCardRace race;

    @Builder
    public PropertiesSpellCard(EnumNonMonsterCardRace race) {
        this.race = race;
    }

    @Override
    public String toEmbeddingText() {
        return String.format("card race %s", race.name().toLowerCase().replaceAll("_", " "));
    }
}
