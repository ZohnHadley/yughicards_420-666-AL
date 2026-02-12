package com.cal.yughistore.services.DTOs.Properties;


import com.cal.yughistore.model.enums.EnumCardType;
import com.cal.yughistore.model.enums.EnumPropertiesConfigType;
import com.cal.yughistore.model.properties.CardProperties;
import com.cal.yughistore.model.properties.PropertiesMonsterCard;
import com.cal.yughistore.model.properties.PropertiesSpellCard;
import com.cal.yughistore.model.properties.PropertiesTrapCard;
import com.cal.yughistore.model.util.SimpleEnumUtils;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class DTOCardProperties {
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

    public static DTOCardProperties toDto(JsonNode node) {
        EnumCardType type = SimpleEnumUtils.findEnumValue(EnumCardType.class, node.get("type").asText());
        CardProperties properties = getCardProperties(type);

        if (properties.getClass().equals(PropertiesMonsterCard.class)) {
            return DTOPropertiesMonsterCard.of(node);
        } else if (properties.getClass().equals(PropertiesSpellCard.class)) {
            return DTOPropertiesSpellCard.of(node);
        } else if (properties.getClass().equals(PropertiesTrapCard.class)) {
            return DTOPropertiesTrapCard.of(node);
        }
        return new DTOCardProperties();
    }

    public static DTOCardProperties toDto(CardProperties properties) {
        if (properties.getClass().equals(PropertiesMonsterCard.class)) {
            return DTOPropertiesMonsterCard.of((PropertiesMonsterCard) properties);
        } else if (properties.getClass().equals(PropertiesSpellCard.class)) {
            return DTOPropertiesSpellCard.of((PropertiesSpellCard) properties);
        } else if (properties.getClass().equals(PropertiesTrapCard.class)) {
            return DTOPropertiesTrapCard.of((PropertiesTrapCard) properties);
        }
        return new DTOCardProperties();
    }

    public CardProperties toCardProperties() {
        if (this.getClass().equals(DTOPropertiesMonsterCard.class)) {
            return ((DTOPropertiesMonsterCard) this).toPropertiesMonsterCard();
        } else if (this.getClass().equals(DTOPropertiesSpellCard.class)) {
            return ((DTOPropertiesSpellCard) this).toPropertiesSpellCard();
        } else if (this.getClass().equals(DTOPropertiesTrapCard.class)) {
            return ((DTOPropertiesTrapCard) this).toPropertiesTrapCard();
        }
        return new CardProperties();
    }
}
