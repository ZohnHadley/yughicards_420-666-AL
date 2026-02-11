package com.cal.yughistore.model.enums;

import lombok.Getter;

@Getter
public enum EnumPropertiesConfigType {
    TYPE_MONSTER("MONSTER"),
    TYPE_SPELL("SPELL"),
    TYPE_TRAP("TRAP");

    private final String name;

    EnumPropertiesConfigType(String name){
        this.name = name;
    }

}
