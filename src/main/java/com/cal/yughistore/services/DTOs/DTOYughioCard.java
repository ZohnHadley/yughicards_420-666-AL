package com.cal.yughistore.services.DTOs;

import com.cal.yughistore.model.MonsterCard;
import com.cal.yughistore.model.SpellCard;
import com.cal.yughistore.model.YughioCard;
import com.cal.yughistore.model.enums.*;
import com.cal.yughistore.model.util.SimpleEnumUtils;
import lombok.*;

import javax.validation.constraints.Null;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Data
@ToString
public class DTOYughioCard {
    private Long id;
    private Integer api_id = -1;
    private String name = "null";
    private String type = "null";
    private String frameType = "null";
    private String description = "null";
    private String ygoprodeck_url = "null";

    //for monstercards
    private Integer atk = -1;
    private Integer def = -1;
    private Integer level = -1;
    private String cardAttribute;
    //
    private String race; //string then convert to card race depending on if its trap, spell or monster card



    public YughioCard toEntity() {
        EnumCardType cardType = SimpleEnumUtils.findEnumValue(EnumCardType.class, this.type);
        EnumFrameType frameType = SimpleEnumUtils.findEnumValue(EnumFrameType.class, this.frameType);

        switch (cardType) {
            case NORMAL_MONSTER:
                return toMonster(cardType, frameType);
//            case SPELL_CARD:
//                return toSpell(cardType, frameType);
//            case TRAP_CARD:
//                return toTrap(cardType, frameType);
            default:
                throw new IllegalArgumentException("Unsupported card type: " + this.type);
        }
    }

    private MonsterCard toMonster(EnumCardType type, EnumFrameType frameType) {
        return new MonsterCard(
                this.id,
                this.api_id,
                this.name,
                frameType,
                this.ygoprodeck_url,
                this.description,
                this.atk,
                this.def,
                this.level,
                SimpleEnumUtils.findEnumValue(EnumMonsterCardRace.class, this.race),
                SimpleEnumUtils.findEnumValue(EnumCardAttribute.class, this.cardAttribute)
                );
    }



}
