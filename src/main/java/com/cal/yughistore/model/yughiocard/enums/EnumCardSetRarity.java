package com.cal.yughistore.model.yughiocard.enums;

public enum EnumCardSetRarity {
    NULL("NULL"),
    COMMON("COMMON"),
    RARE("RARE"),
    SUPER_RARE("SUPER_RARE"),
    ULTRA_RARE("ULTRA_RARE"),
    SECRET_RARE("SECRET_RARE"),
    QUATER_CENTURY_SECRET_RARE("QUATER_CENTRY_SECRET_RARE"),


    STARLIGHT_RARE("STARLIGHT_RARE"),
    COLLECTOR_RARE("COLLECTOR'S_RARE"),
    ULTIMATE_RARE("ULTIMATE_RARE"),
    GHOST_RARE("GHOST_RARE"),
    PLATINUM_SECRET_RARE("PLATINUM_SECRET_RARE");


    private final String name;

    EnumCardSetRarity(String name){
        this.name = name;
    }
}
