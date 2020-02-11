package com.tili.model;

public class Bed_Single extends Bed {

    private Boolean occupied;

    public Bed_Single(Integer id, Integer capacity, String name, String status, Boolean occupied) {
        this.id = id;
        this.capacity = capacity;
        this.name = name;
        this.status = status;
        this.occupied = occupied;
    }

    public Bed_Single(String name) {

        capacity = BedEnum.SINGLE.getNumber();
        this.name = name;
        status = STATUS_FREE;
        occupied = false;
    }

    public Boolean getOccupied() {
        return occupied;
    }

    public void setOccupied(Boolean occupied) {
        this.occupied = occupied;
    }
}

