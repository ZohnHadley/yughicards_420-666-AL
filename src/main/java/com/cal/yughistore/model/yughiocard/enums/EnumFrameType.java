package com.cal.yughistore.model.yughiocard.enums;

public enum EnumFrameType {
    NULL( "NULL"),
    NORMAL,
    EFFECT,
    RITUAL,
    FUSION,
    SYNCHRO,
    XYZ(),
    LINK,
    NORMAL_PENDULUM,
    EFFECT_PENDULUM,
    RITUAL_PENDULUM,
    FUSION_PENDULUM,
    SYNCHRO_PENDULUM,
    XYZ_PENDULUM,
    SPELL,
    TRAP,
    TOKEN,
    SKILL;

    private final String name;

    EnumFrameType(String name){
        this.name = name;
    }

    EnumFrameType(){
        this.name = this.name();
    }
}