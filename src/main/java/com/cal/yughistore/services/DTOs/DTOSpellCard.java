package com.cal.yughistore.services.DTOs;

import com.cal.yughistore.model.MonsterCard;
import com.cal.yughistore.model.SpellCard;
import com.cal.yughistore.model.TrapCard;
import com.cal.yughistore.model.enums.EnumCardAttribute;
import com.cal.yughistore.model.enums.EnumMonsterCardRace;
import com.cal.yughistore.model.enums.EnumSpellCardRace;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Data
public class DTOSpellCard extends DTOYughioCard{

    private EnumSpellCardRace race;

    public SpellCard toEntity(){
        return DTOSpellCard.toEntity(this);
    }

    public static SpellCard toEntity(DTOSpellCard dtoYughioCard){
        return  new SpellCard(
                dtoYughioCard.getId(),
                dtoYughioCard.getApiID(),
                dtoYughioCard.getName(),
                dtoYughioCard.getFrameType(),
                dtoYughioCard.getDescription(),
                dtoYughioCard.getYgoprodeck_url(),
                dtoYughioCard.race
        );
    }

    public static DTOSpellCard empty() {return new DTOSpellCard();}
}
