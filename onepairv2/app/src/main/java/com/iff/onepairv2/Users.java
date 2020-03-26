package com.iff.onepairv2;

/**
 * Each Users object models the information for a single user using the application
 * @author ifandonlyif
 */
public class Users {

    /** the name of the user */
    public String name;
    /** the uid of the user */
    public String uid;
    /** the image url of the user */
    public String image;

    /**
     * constructor creates a user
     */
    public Users(){}

    /**
     * constructor creates a user
     * @param name
     * @param uid
     * @param image
     */
    public Users(String name, String uid, String image) {
        this.name = name;
        this.uid = uid;
        this.image = image;
    }

    /**
     * Returns the name of the user
     * @return name of the user
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the uid of the user
     * @return uid of the user
     */
    public String getUid() {
        return uid;
    }

    /**
     * Sets the uid of the user
     * @param uid
     */
    public void setUid(String uid) {
        this.uid = uid;
    }

    /**
     * Sets the name of the user
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns the image of the user
     * @return image of the user
     */
    public String getImage() {
        return image;
    }

    /**
     * Sets the image of the user
     * @param image
     */
    public void setImage(String image) {
        this.image = image;
    }
}
