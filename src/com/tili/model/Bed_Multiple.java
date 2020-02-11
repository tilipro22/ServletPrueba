package com.tili.model;

import java.util.Arrays;
import java.util.List;

public class Bed_Multiple extends Bed {

    private List<Boolean> listOccupied;

    public Bed_Multiple(Integer id, Integer capacity, String name, String status, List<Boolean> listOccupied) {
        this.id = id;
        this.capacity = capacity;
        this.name = name;
        this.status = status;
        this.listOccupied = listOccupied;
    }

    public Bed_Multiple(String name) {

        switch (BedEnum.valueOf(name.toUpperCase())) {
            case MATRIMONIAL:
                capacity = BedEnum.MATRIMONIAL.getNumber();
                break;

            case DOUBLE:
                capacity = BedEnum.DOUBLE.getNumber();
                break;

            case TRIPLE:
                capacity = BedEnum.TRIPLE.getNumber();
                break;

            case QUADRUPLE:
                capacity = BedEnum.QUADRUPLE.getNumber();

        }

        this.name = name;
        this.status = STATUS_FREE;
        initListOccupied(capacity);
    }



    private void initListOccupied(Integer total) {
        listOccupied = Arrays.asList(new Boolean[total]);

        for (int i = 0; i < total; i++) {
            listOccupied.set(i, false);
        }
    }

    public List<Boolean> getListOccupied() {
        return listOccupied;
    }

    public void setListOccupied(List<Boolean> listOccupied) {
        this.listOccupied = listOccupied;
    }
}
