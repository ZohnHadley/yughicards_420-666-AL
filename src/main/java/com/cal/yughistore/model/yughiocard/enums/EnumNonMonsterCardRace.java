package com.cal.yughistore.model.yughiocard.enums;

public enum EnumNonMonsterCardRace {
    NULL( "NULL"),

    //shared (both spell and trap card)
    NORMAL,
    CONTINUOUS,
    COUNTER,
    SPELL_CARD,
    TRAP_CARD,
    //

    //spell card
    FIELD,
    EQUIP,
    QUICK_PLAY,
    QUICK_PLAY_SPELL,
    QUICK_PLAY_TRAP,
    RITUAL;

    private final String name;

    EnumNonMonsterCardRace(String name){
        this.name = name;
    }

    EnumNonMonsterCardRace(){
        this.name = this.name();
    }
}
