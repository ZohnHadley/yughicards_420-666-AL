package com.cal.yughistore.model;

import com.cal.yughistore.model.enums.EnumCardType;
import com.cal.yughistore.model.enums.EnumFrameType;
import com.cal.yughistore.model.enums.EnumTrapCardRace;
import jakarta.persistence.Entity;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class TrapCard extends YughioCard {
    private EnumTrapCardRace race;

    public TrapCard(Long id, Long api_id, String name, EnumFrameType frameType, String description, String ygoprodeck_url, EnumTrapCardRace race) {
        super(id, api_id, name, EnumCardType.TRAP_CARD, frameType, description, ygoprodeck_url);
        this.race = race;
    }
}
