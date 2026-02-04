package com.cal.yughistore.services.DTOs;

import com.cal.yughistore.model.MonsterCard;
import com.cal.yughistore.model.SpellCard;
import com.cal.yughistore.model.TrapCard;
import com.cal.yughistore.model.enums.EnumSpellCardRace;
import com.cal.yughistore.model.enums.EnumTrapCardRace;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Data
public class DTOTrapCard extends DTOYughioCard{

    private EnumTrapCardRace race;

    public TrapCard toEntity(){
        return DTOTrapCard.toEntity(this);
    }

    public static TrapCard toEntity(DTOTrapCard dtoYughioCard){
        return  new TrapCard(
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
