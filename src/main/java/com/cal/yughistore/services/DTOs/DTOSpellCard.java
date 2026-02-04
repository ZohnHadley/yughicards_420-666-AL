package com.cal.yughistore.services.DTOs;

import com.cal.yughistore.model.SpellCard;
import com.cal.yughistore.model.enums.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Data
public class DTOSpellCard extends DTOYughioCard{

    private EnumSpellCardRace race;

    public DTOSpellCard(Long id, Long api_id, String name, EnumFrameType frameType, String description, String ygoprodeck_url, EnumSpellCardRace race) {
        super(id, api_id, name, EnumCardType.SPELL_CARD, frameType, description, ygoprodeck_url);
        this.race = race;
    }

    public SpellCard toEntity(){
        return DTOSpellCard.toEntity(this);
    }

    public static SpellCard toEntity(DTOSpellCard dtoYughioCard){
        return  new SpellCard(
                dtoYughioCard.getId(),
                dtoYughioCard.getApi_id(),
                dtoYughioCard.getName(),
                dtoYughioCard.getFrameType(),
                dtoYughioCard.getDescription(),
                dtoYughioCard.getYgoprodeck_url(),
                dtoYughioCard.race
        );
    }

    public static DTOSpellCard toDTO(SpellCard yughioCard){
        return  new DTOSpellCard(
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
