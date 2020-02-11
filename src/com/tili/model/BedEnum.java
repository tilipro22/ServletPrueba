package com.tili.model;

public enum BedEnum {
    SINGLE(1),
    MATRIMONIAL(2),
    DOUBLE(2),
    TRIPLE(3),
    QUADRUPLE(4);


    private Integer number;

    BedEnum (Integer number) {
        this.number = number;
    }

    public Integer getNumber() {
        return number;
    }
}
