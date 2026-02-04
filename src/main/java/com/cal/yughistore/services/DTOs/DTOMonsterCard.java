package com.cal.yughistore.services.DTOs;

import com.cal.yughistore.model.MonsterCard;
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

    public DTOMonsterCard(Long id, Long api_id, String name, EnumFrameType frameType, String description, String ygoprodeck_url, int atk, int def, int level, EnumMonsterCardRace race, EnumCardAttribute cardAttribute) {
        super(id, api_id, name, EnumCardType.NORMAL_MONSTER, frameType, description, ygoprodeck_url);
        this.atk = atk;
        this.def = def;
        this.level = level;
        this.race = race;
        this.cardAttribute = cardAttribute;
    }

    public MonsterCard toEntity(){
        return DTOMonsterCard.toEntity(this);
    }

    public static MonsterCard toEntity(DTOMonsterCard dtoYughioCard){
        return  new MonsterCard(
                dtoYughioCard.getId(),
                dtoYughioCard.getApi_id(),
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

    public static DTOMonsterCard toDTO(MonsterCard yughioCard){
        return  new DTOMonsterCard(
                yughioCard.getId(),
                yughioCard.getApi_id(),
                yughioCard.getName(),
                yughioCard.getFrameType(),
                yughioCard.getDescription(),
                yughioCard.getYgoprodeck_url(),
                yughioCard.getAtk(),
                yughioCard.getDef(),
                yughioCard.getLevel(),
                yughioCard.getRace(),
                yughioCard.getCardAttribute()
        );
    }

    public static DTOMonsterCard empty() {return new DTOMonsterCard();}

}
