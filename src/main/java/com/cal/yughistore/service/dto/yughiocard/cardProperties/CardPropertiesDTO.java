package com.cal.yughistore.service.dto.yughiocard.cardProperties;


import com.cal.yughistore.model.yughiocard.enums.EnumCardType;
import com.cal.yughistore.model.yughiocard.enums.EnumPropertiesConfigType;
import com.cal.yughistore.model.yughiocard.properties.CardProperties;
import com.cal.yughistore.model.yughiocard.properties.PropertiesMonsterCard;
import com.cal.yughistore.model.yughiocard.properties.PropertiesSpellCard;
import com.cal.yughistore.model.yughiocard.properties.PropertiesTrapCard;
import com.cal.yughistore.utils.SimpleEnumUtils;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString(exclude = "yughioCard")
public class CardPropertiesDTO {


    //TODO : PROPERTIES DTOs ARN'T BEING USED ANYWHERE (BUT WHEN REQUIRES MORE DETAIL SEARCH ENDPOINT THEY WILL COME IN HANDY (I THINK?))
    private Long id;
    private static CardProperties getCardProperties(EnumCardType cardType) {
        if (cardType.name().toUpperCase().contains(EnumPropertiesConfigType.MONSTER.getName())) {
            return new PropertiesMonsterCard();
        } else if (cardType.name().toUpperCase().contains(EnumPropertiesConfigType.SPELL.getName())) {
            return new PropertiesSpellCard();
        } else if (cardType.name().toUpperCase().contains(EnumPropertiesConfigType.TRAP.getName())) {
            return new PropertiesTrapCard();
        }
        return new CardProperties();
    }

    public static CardPropertiesDTO toDto(JsonNode node) {
        EnumCardType type = SimpleEnumUtils.findEnumValue(EnumCardType.class, node.get("type").asText());
        CardProperties properties = getCardProperties(type);

        if (properties.getClass().equals(PropertiesMonsterCard.class)) {
            return PropertiesMonsterCardDTO.of(node);
        } else if (properties.getClass().equals(PropertiesSpellCard.class)) {
            return PropertiesSpellCardDTO.of(node);
        } else if (properties.getClass().equals(PropertiesTrapCard.class)) {
            return PropertiesTrapCardDTO.of(node);
        }
        return new CardPropertiesDTO();
    }

    public static CardPropertiesDTO of(CardProperties properties) {
        if (properties.getClass().equals(PropertiesMonsterCard.class)) {
            return PropertiesMonsterCardDTO.of((PropertiesMonsterCard) properties);
        } else if (properties.getClass().equals(PropertiesSpellCard.class)) {
            return PropertiesSpellCardDTO.of((PropertiesSpellCard) properties);
        } else if (properties.getClass().equals(PropertiesTrapCard.class)) {
            return PropertiesTrapCardDTO.of((PropertiesTrapCard) properties);
        }
        return new CardPropertiesDTO();
    }

    public CardProperties toCardProperties() {
        if (this.getClass().equals(PropertiesMonsterCardDTO.class)) {
            return ((PropertiesMonsterCardDTO) this).toPropertiesMonsterCard();
        } else if (this.getClass().equals(PropertiesSpellCardDTO.class)) {
            return ((PropertiesSpellCardDTO) this).toPropertiesSpellCard();
        } else if (this.getClass().equals(PropertiesTrapCardDTO.class)) {
            return ((PropertiesTrapCardDTO) this).toPropertiesTrapCard();
        }
        return new CardProperties();
    }
}
