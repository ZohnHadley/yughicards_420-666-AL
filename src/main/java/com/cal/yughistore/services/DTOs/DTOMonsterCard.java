package com.cal.yughistore.services.DTOs;

import com.cal.yughistore.model.MonsterCard;
import com.cal.yughistore.model.YughioCard;
import com.cal.yughistore.model.enums.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Data
public class DTOMonsterCard extends DTOYughioCard{
    private int atk;
    private int def;
    private int level;
    private EnumMonsterCardRace race;
    private EnumCardAttribute cardAttribute;

    public MonsterCard toEntity(){
        return DTOMonsterCard.toEntity(this);
    }

    public static MonsterCard toEntity(DTOMonsterCard dtoYughioCard){
        return  new MonsterCard(
                dtoYughioCard.getId(),
                dtoYughioCard.getApiID(),
                dtoYughioCard.getName(),
                dtoYughioCard.getFrameType(),
                dtoYughioCard.getDescription(),
                dtoYughioCard.getYgoprodeck_url(),
                dtoYughioCard.atk,
                dtoYughioCard.def,
                dtoYughioCard.level,
                dtoYughioCard.race,
                dtoYughioCard.cardAttribute
        );
    }

    public static DTOMonsterCard empty() {return new DTOMonsterCard();}

}
