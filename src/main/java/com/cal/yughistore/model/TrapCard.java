package com.cal.yughistore.model;

import com.cal.yughistore.model.abstractClasses.YughioCard;
import com.cal.yughistore.model.enums.EnumCardType;
import com.cal.yughistore.model.enums.EnumFrameType;
import com.cal.yughistore.model.enums.EnumSpellTrapCardRace;
import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TrapCard extends YughioCard {
    private EnumSpellTrapCardRace race;

    public TrapCard(Long id, Long api_id, EnumSpellTrapCardRace race, String name, EnumFrameType frameType, String description, String ygoprodeck_url) {
        super(id, api_id, name, EnumCardType.SPELL_CARD, frameType, description, ygoprodeck_url);
        this.race = race;
    }
}
