package com.cal.yughistore.services.DTOs;

import com.cal.yughistore.model.YughioCard;
import com.cal.yughistore.model.enums.*;
import com.cal.yughistore.model.util.SimpleEnumUtils;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DTOYughioCard {
    private int id;
    private Integer api_id = -1;
    private String name = "";
    private EnumCardType type = EnumCardType.NULL;
    private EnumFrameType frameType = EnumFrameType.NULL;
    private String description = "";
    private String ygoprodeck_url = "";

    //for monstercards
    private Integer atk = -1;
    private Integer def = -1;
    private Integer level = -1;
    private String cardAttribute = "";
    //
    private String race = ""; //string then convert to card race depending on if its trap, spell or monster card


//    public static DTOYughioCard fromJsonNode(JsonNode node) {
//        EnumCardType cardType = SimpleEnumUtils.findEnumValue(EnumCardType.class, node.get("type").toString().replaceAll("\\s", "_").replaceAll("\"", ""));
//
//        switch (cardType) {
//            case NORMAL_MONSTER:
//                return toMonsterDTO(node);
//            case SPELL_CARD:
//            case TRAP_CARD:
//                return toSpellDTO(node);
//            default:
//                throw new IllegalArgumentException("Unsupported card type: " + node.get("type").toString());
//        }
//    }

    //    private static DTOYughioCard toMonsterDTO(JsonNode node) {
//        return DTOYughioCard.builder()
//                        .api_id(node.get("id").asInt())
//                        .name(node.get("name").asText())
//                        .type(node.get("type").asText())
//                        .description(node.get("desc").asText(""))
//                        .ygoprodeck_url(node.get("ygoprodeck_url").asText(""))
//                        .frameType(node.get("frameType").asText(""))
//                        .race(node.get("race").asText())
//                        .atk(node.get("atk").asInt())
//                        .def(node.get("def").asInt())
//                        .level(node.get("level").asInt())
//                        .cardAttribute(node.get("cardAttribute").asText())
//                        .build();
//    }
//
//    private static DTOYughioCard toSpellDTO(JsonNode node) {
//        return DTOYughioCard.builder()
//                .api_id(node.get("id").asInt())
//                .name(node.get("name").asText())
//                .type(node.get("type").asText())
//                .description(node.get("desc").asText(""))
//                .ygoprodeck_url(node.get("ygoprodeck_url").asText(""))
//                .frameType(node.get("frameType").asText(""))
//                .race(node.get("race").asText())
//                .build();
//
//    }
//
//
    public YughioCard toEntity() {
        EnumCardType cardType = SimpleEnumUtils.findEnumValue(EnumCardType.class, this.type);
        EnumFrameType frameType = SimpleEnumUtils.findEnumValue(EnumFrameType.class, this.frameType);

        switch (cardType) {
            case NORMAL_MONSTER:
                return toMonsterCard(cardType, frameType);
            case SPELL_CARD:
                return toSpellCard(cardType, frameType);
            case TRAP_CARD:
                return toTrapCard(cardType, frameType);
            default:
                throw new IllegalArgumentException("Unsupported card type: " + this.type);
        }
    }

    private MonsterCard toMonsterCard(EnumCardType type, EnumFrameType frameType) {
        return MonsterCard.builder()
                .api_id(this.api_id)
                .name(this.name)
                .frameType(frameType)
                .description(this.description)
                .ygoprodeck_url(this.ygoprodeck_url)
                .atk(this.atk)
                .def(this.def)
                .level(this.level)
                .race(SimpleEnumUtils.findEnumValue(EnumMonsterCardRace.class, this.race))
                .cardAttribute(SimpleEnumUtils.findEnumValue(EnumCardAttribute.class, this.cardAttribute))
                .build();
    }

    private SpellCard toSpellCard(EnumCardType type, EnumFrameType frameType) {
        return SpellCard.builder()
                .api_id(this.api_id)
                .name(this.name)
                .frameType(frameType)
                .description(this.description)
                .ygoprodeck_url(this.ygoprodeck_url)
                .race(SimpleEnumUtils.findEnumValue(EnumNonMonsterCardRace.class, this.race)).build();
    }

    private TrapCard toTrapCard(EnumCardType type, EnumFrameType frameType) {
        return TrapCard.builder()
                .api_id(this.api_id)
                .name(this.name)
                .frameType(frameType)
                .description(this.description)
                .ygoprodeck_url(this.ygoprodeck_url)
                .race(SimpleEnumUtils.findEnumValue(EnumNonMonsterCardRace.class, this.race)).build();
    }
}
