package com.tili.model;

import java.util.List;

public class Room {
    private Integer id;
    private String name;
    private Integer capacity;
    private String type; //public o private room
    private List<Bed> beds;

    private Integer occupied;

    public Room() {
    }

    public Room(String name, String type, List<Bed> beds) {
        this.name = name;
        this.type = type;
        this.beds = beds;
        calculateCapacity();
    }

    public Room(Integer id, String name, Integer capacity, String type, List<Bed> beds) {
        this.id = id;
        this.name = name;
        this.capacity = capacity;
        this.type = type;
        this.beds = beds;
    }

    private void calculateCapacity() {
        Integer total = 0;
        for (Bed bed:
             beds) {
            total += bed.capacity;
        }

        capacity = total;
    }

    public Integer getOccupied() {
        return occupied;
    }


    public void calculateOccupied() {
        occupied = 0;
        for (Bed bed: beds) {

            if (bed instanceof Bed_Single) {
                if ( ((Bed_Single) bed).getOccupied() )
                    occupied++;
            } else {
                List<Boolean> occupiedList = ((Bed_Multiple) bed).getListOccupied();

                for (Boolean isOccupied: occupiedList) {
                    if (isOccupied)
                        occupied++;
                }
            }
        }
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<Bed> getBeds() {
        return beds;
    }

    public void setBeds(List<Bed> beds) {
        this.beds = beds;
    }
}
