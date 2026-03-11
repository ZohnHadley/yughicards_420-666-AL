package com.cal.yughistore.model.yughiocard.enums;

public enum EnumMonsterCardRace {
    NULL( "NULL"),

    AQUA,
    BEAST,
    BEAST_WARRIOR,
    CREATOR_GOD,
    CYBERSE,
    DINOSAUR,
    DIVINE_BEAST,
    DRAGON,
    FAIRY,
    FIEND,
    FISH,
    INSECT,
    ILLUSION,
    MACHINE,
    PLANT,
    PSYCHIC,
    PYRO,
    REPTILE,
    ROCK,
    SEA_SERPENT,
    SPELLCASTER,
    THUNDER,
    WARRIOR,
    WINGED_BEAST,
    WYRM,
    ZOMBIE;

    private final String name;

    EnumMonsterCardRace(String name){
        this.name = name;
    }

    EnumMonsterCardRace(){
        this.name = this.name();
    }
}
