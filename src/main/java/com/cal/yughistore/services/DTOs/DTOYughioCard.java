package com.cal.yughistore.services.DTOs;

import com.cal.yughistore.model.YughioCard;
import com.cal.yughistore.model.enums.*;
import com.cal.yughistore.model.properties.PropertiesMonsterCard;
import com.cal.yughistore.model.properties.PropertiesSpellCard;
import com.cal.yughistore.model.properties.PropertiesTrapCard;
import com.cal.yughistore.model.properties.CardProperties;
import com.cal.yughistore.model.util.SimpleEnumUtils;
import com.fasterxml.jackson.databind.JsonNode;
import jakarta.persistence.*;
import lombok.*;
import org.jspecify.annotations.Nullable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table
@ToString
public class DTOYughioCard {

    /// base card properties (all cards have these) ///
    private Long id;
    private int api_id;
    private String name = "no_name";
    private EnumCardType type = EnumCardType.NULL;
    private EnumFrameType frameType = EnumFrameType.NULL;
    private String description = "";
    private String ygoprodeck_url = "";

    /// Properties (depends on card type (trap, spell, monster, etc) ) ///
    private CardProperties cardProperties;


    /// static methode ///
    private static @Nullable CardProperties getCardProperties(EnumCardType cardType) {
        if (cardType.name().toUpperCase().contains(EnumPropertiesConfigType.TYPE_MONSTER.getName())) {
            return new PropertiesMonsterCard();
        } else if (cardType.name().toUpperCase().contains(EnumPropertiesConfigType.TYPE_SPELL.getName())) {
            return new PropertiesSpellCard();
        } else if (cardType.name().toUpperCase().contains(EnumPropertiesConfigType.TYPE_TRAP.getName())) {
            return new PropertiesTrapCard();
        }
        return null;
    }

    public static DTOYughioCard toDTO(JsonNode node) {
        EnumCardType cardType = SimpleEnumUtils.findEnumValue(EnumCardType.class, node.get("type").asText().replaceAll("\\s", "_").replaceAll("-","_"));
        EnumFrameType frameType = SimpleEnumUtils.findEnumValue(EnumFrameType.class, node.get("frameType").asText().replaceAll("\\s", "_").replaceAll("-","_"));

        CardProperties cardProperties = getCardProperties(cardType);
        if (cardProperties != null) {
            if (cardProperties.getClass().equals(PropertiesMonsterCard.class)) {
                PropertiesMonsterCard monster = ((PropertiesMonsterCard) cardProperties);

                monster.setAtk(node.get("atk").asInt());
                monster.setDef(node.get("def").asInt());
                monster.setLevel(node.get("level").asInt());

                EnumMonsterCardRace race = SimpleEnumUtils.findEnumValue(EnumMonsterCardRace.class, node.get("race").asText().replaceAll("\\s", "_").replaceAll("-","_"));
                EnumCardAttribute attribute = SimpleEnumUtils.findEnumValue(EnumCardAttribute.class, node.get("attribute").asText().replaceAll("\\s", "_").replaceAll("-","_"));

                monster.setRace(race);
                monster.setAttribute(attribute);
            } else if (cardProperties.getClass().equals(PropertiesSpellCard.class)) {
                PropertiesSpellCard spell = ((PropertiesSpellCard) cardProperties);

                EnumNonMonsterCardRace race = SimpleEnumUtils.findEnumValue(EnumNonMonsterCardRace.class, node.get("race").asText().replaceAll("\\s", "_").replaceAll("-","_"));

                spell.setRace(race);
            } else if (cardProperties.getClass().equals(PropertiesTrapCard.class)) {
                PropertiesTrapCard trap = ((PropertiesTrapCard) cardProperties);

                EnumNonMonsterCardRace race = SimpleEnumUtils.findEnumValue(EnumNonMonsterCardRace.class, node.get("race").asText().replaceAll("\\s", "_").replaceAll("-","_"));

                trap.setRace(race);
            }

        }

        DTOYughioCard card = DTOYughioCard.builder()
                .api_id(node.get("id").asInt())
                .name(node.get("name").asText())
                .type(cardType)
                .description(node.get("desc").asText())
                .ygoprodeck_url(node.get("ygoprodeck_url").asText())
                .frameType(frameType)
                .cardProperties(cardProperties)
                .build();

        return card;
    }


    public static DTOYughioCard toDTO(YughioCard card) {
        return DTOYughioCard.builder()
                .id(card.getId())
                .api_id(card.getApi_id())
                .name(card.getName())
                .type(card.getType())
                .description(card.getDescription())
                .ygoprodeck_url(card.getYgoprodeck_url())
                .frameType(card.getFrameType())
                .cardProperties(card.getCardProperties())
                .build();
    }

    /// ///

    public YughioCard toEntity() {
        YughioCard card = YughioCard.builder()
                .api_id(this.api_id)
                .name(this.name)
                .type(this.type)
                .frameType(this.frameType)
                .description(this.description)
                .ygoprodeck_url(this.ygoprodeck_url)
                .build();

        CardProperties cardProperties = getCardProperties(card.getType());
        card.setCardProperties(cardProperties);

        return card;
    }
}
