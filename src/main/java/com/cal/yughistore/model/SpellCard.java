package com.cal.yughistore.model;

import com.cal.yughistore.model.enums.EnumCardType;
import com.cal.yughistore.model.enums.EnumFrameType;
import com.cal.yughistore.model.enums.EnumSpellCardRace;
import jakarta.persistence.Entity;
import lombok.*;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class SpellCard  extends YughioCard {

    private EnumSpellCardRace race;

    public SpellCard(Long id, Long api_id, String name, EnumFrameType frameType, String description, String ygoprodeck_url, EnumSpellCardRace race) {
        super(id, api_id, name, EnumCardType.SPELL_CARD, frameType, description, ygoprodeck_url);
        this.race = race;
    }
}
