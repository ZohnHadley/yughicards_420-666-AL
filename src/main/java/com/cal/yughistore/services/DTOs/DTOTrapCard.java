package com.cal.yughistore.services.DTOs;

import com.cal.yughistore.model.TrapCard;
import com.cal.yughistore.model.enums.EnumCardType;
import com.cal.yughistore.model.enums.EnumFrameType;
import com.cal.yughistore.model.enums.EnumTrapCardRace;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Data
public class DTOTrapCard extends DTOYughioCard{

    private EnumTrapCardRace race;

    public DTOTrapCard(Long id, Long api_id, String name, EnumFrameType frameType, String description, String ygoprodeck_url, EnumTrapCardRace race) {
        super(id, api_id, name, EnumCardType.TRAP_CARD, frameType, description, ygoprodeck_url);
        this.race = race;
    }

    public TrapCard toEntity(){
        return DTOTrapCard.toEntity(this);
    }

    public static TrapCard toEntity(DTOTrapCard dtoYughioCard){
        return  new TrapCard(
                dtoYughioCard.getId(),
                dtoYughioCard.getApi_id(),
                dtoYughioCard.getName(),
                dtoYughioCard.getFrameType(),
                dtoYughioCard.getDescription(),
                dtoYughioCard.getYgoprodeck_url(),
                dtoYughioCard.race
        );
    }

    public static DTOTrapCard toDTO(TrapCard yughioCard){
        return  new DTOTrapCard(
                yughioCard.getId(),
                yughioCard.getApi_id(),
                yughioCard.getName(),
                yughioCard.getFrameType(),
                yughioCard.getDescription(),
                yughioCard.getYgoprodeck_url(),
                yughioCard.getRace()
        );
    }

    public static DTOSpellCard empty() {return new DTOSpellCard();}
}
