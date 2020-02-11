package com.tili.model;

import java.util.ArrayList;
import java.util.List;

public class Bed {

    final static String STATUS_FREE = "FREE";
    final static String STATUS_COMPLETE = "COMPLETE";

    protected Integer id;
    protected Integer capacity;
    protected String name;
    protected String status;

    public Integer getId() {
        return id;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public String getName() {
        return name;
    }

    public String getStatus() {
        return status;
    }

    public List<Boolean> listOccupied() {
        List<Boolean> list = new ArrayList<>();
        if (this instanceof Bed_Single) {
            list.add( ((Bed_Single) this).getOccupied());
        }
        else {
            list = ((Bed_Multiple) this).getListOccupied();
        }

        return list;
    }
}
