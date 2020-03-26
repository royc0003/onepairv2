package com.iff.onepairv2;

/**
 * Each Location object contains information of the name of location and its assigned id
 * @author ifandonlyif
 */
public class Location {
    /**
     * The id assigned to the location
     */
    private int id;
    /**
     * The name of the location
     */
    private String name;

    /**
     * Constructor for the Location
     * @param id
     * @param name
     */
    public Location(int id, String name){
        this.id = id;
        this.name = name;
    }

    /**
     * Return ID of the location
     * @return ID of location
     */
    public int getId(){
        return this.id;
    }

    /**
     * Return name of the location
     * @return name of the location
     */
    public String getName(){
        return this.name;
    }
}
