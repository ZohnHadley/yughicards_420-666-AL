package com.cal.yughistore.model;

import com.cal.yughistore.model.enums.EnumCardType;
import com.cal.yughistore.model.enums.EnumFrameType;
import com.cal.yughistore.model.enums.EnumNonMonsterCardRace;
import jakarta.persistence.Entity;
import lombok.*;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class SpellCard extends YughioCard {

    private EnumNonMonsterCardRace race;

    public SpellCard(Long id,
                     int api_id,
                     String name,
                     EnumFrameType frameType,
                     String description,
                     String ygoprodeck_url,
                     EnumNonMonsterCardRace race
    ) {
        super(id, api_id, name, EnumCardType.SPELL_CARD, frameType, description, ygoprodeck_url);
        this.race = race;
    }
}
