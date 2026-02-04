package com.cal.yughistore.model;

import com.cal.yughistore.model.enums.EnumCardType;
import com.cal.yughistore.model.enums.EnumFrameType;
import com.cal.yughistore.model.enums.EnumSpellTrapCardRace;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class SpellCard  extends YughioCard {

    private EnumSpellTrapCardRace race;

    public SpellCard(Long id, Long api_id, EnumSpellTrapCardRace race, String name, EnumFrameType frameType, String description, String ygoprodeck_url) {
        super(id, api_id, name, EnumCardType.SPELL_CARD, frameType, description, ygoprodeck_url);
        this.race = race;
    }
}
