package com.cal.yughistore.services.DTOs;

import com.cal.yughistore.model.YughioCard;
import com.cal.yughistore.model.enums.*;
import com.cal.yughistore.model.properties.PropertiesMonsterCard;
import com.cal.yughistore.model.properties.PropertiesSpellCard;
import com.cal.yughistore.model.properties.PropertiesTrapCard;
import com.cal.yughistore.model.properties.SpecificProperties;
import com.cal.yughistore.model.util.SimpleEnumUtils;
import jakarta.persistence.*;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table
@ToString
public class DTOYughioCard {
    private EnumConfigType enumConfigType = EnumConfigType.TYPE_MONSTER;
    private SpecificProperties properties;

    private Long id;
    private int api_id;
    private String name = "no_name";
    private EnumCardType type = EnumCardType.NULL;
    private EnumFrameType frameType = EnumFrameType.NULL;
    private String description = "null";
    private String ygoprodeck_url = "null";


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

//   private static DTOYughioCard toMonsterDTO(JsonNode node) {
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

    public static DTOYughioCard toDTO(YughioCard card){
        return DTOYughioCard.builder()
                .id(card.getId())
                .api_id(card.getApi_id())
                .name(card.getName())
                .description(card.getDescription())
                .ygoprodeck_url(card.getYgoprodeck_url())
                .frameType(card.getFrameType())
                .enumConfigType(card.getEnumConfigType())
                .properties(card.getProperties())
                .build();
    }

    public YughioCard toEntity() {
        EnumConfigType cardConfig = SimpleEnumUtils.findEnumValue(EnumConfigType.class, this.enumConfigType.name());
        YughioCard card = YughioCard.builder()
                .api_id(this.api_id)
                .name(this.name)
                .frameType(this.frameType)
                .description(this.description)
                .ygoprodeck_url(this.ygoprodeck_url)
                .build();

        switch (cardConfig) {
            case EnumConfigType.TYPE_MONSTER:
                card.setProperties(new PropertiesMonsterCard());
                break;
            case EnumConfigType.TYPE_SPELL:
                card.setProperties(new PropertiesSpellCard());
                break;
            case EnumConfigType.TYPE_TRAP:
                card.setProperties(new PropertiesTrapCard());
                break;
            default:
                throw new IllegalArgumentException("Unsupported card type: " + this.type);
        }

        return card;
    }
}
