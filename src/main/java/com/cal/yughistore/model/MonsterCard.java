package com.cal.yughistore.model;

import com.cal.yughistore.model.enums.EnumCardAttribute;
import com.cal.yughistore.model.enums.EnumCardType;
import com.cal.yughistore.model.enums.EnumFrameType;
import com.cal.yughistore.model.enums.EnumMonsterCardRace;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class MonsterCard extends YughioCard {

    private int atk;
    private int def;
    private int level;
    private EnumMonsterCardRace race;
    private EnumCardAttribute cardAttribute;

    public MonsterCard(Long id, Long api_id, String name, EnumFrameType frameType, String description, String ygoprodeck_url, int atk, int def, int level, EnumMonsterCardRace race, EnumCardAttribute cardAttribute) {
        super(id, api_id, name, EnumCardType.NORMAL_MONSTER, frameType, description, ygoprodeck_url);
        this.atk = atk;
        this.def = def;
        this.level = level;
        this.race = race;
        this.cardAttribute = cardAttribute;
    }
}
