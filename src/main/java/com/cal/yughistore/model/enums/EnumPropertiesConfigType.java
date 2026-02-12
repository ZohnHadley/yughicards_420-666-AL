package com.cal.yughistore.model.enums;

import lombok.Getter;

@Getter
public enum EnumPropertiesConfigType {
    NULL("NULL"),
    MONSTER("MONSTER"),
    SPELL("SPELL"),
    TRAP("TRAP");

    private final String name;

    EnumPropertiesConfigType(String name){
        this.name = name;
    }

}
