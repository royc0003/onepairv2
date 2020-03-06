package com.iff.onepairv2;

public class Location {
    private int id;
    private String name;

    public Location(int id, String name){
        this.id = id;
        this.name = name;
    }

    public int getId(){
        return this.id;
    }

    public String getName(){
        return this.name;
    }
}
