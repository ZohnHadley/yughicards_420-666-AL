package com.cal.yughistore.model.yughiocard.enums;

public enum EnumCardStockStatus {
    IN_STOCK("IN_STOCK"), /// (when the quantity in stock goes from 0 to n)
    OUT_OF_STOCK("OUT_OF_STOCK"), /// none was required or not yet in store (by default when a new card gets added to store but nothing in stock)
    SOLD_OUT("SOLD_OUT"); ///  was sold out (when the quantity in stock goes from n to 0)

    private final String name;

    EnumCardStockStatus(String name){
        this.name = name;
    }
}
