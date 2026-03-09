package com.cal.yughistore.model.yughiocard.enums;

public enum EnumCardAttribute {
    NULL("NULL"),
    DARK( "DARK"),
    DIVINE( "DIVINE"),
    EARTH( "EARTH"),
    FIRE( "FIRE"),
    LIGHT( "LIGHT"),
    WATER( "WATER"),
    WIND( "WIND"),
    LAUGH( "LAUGH"),;

    private final String name;

    EnumCardAttribute(String name){
        this.name = name;
    }

    EnumCardAttribute(){
        this.name = this.name();
    }
}
