package com.iff.onepairv2;

public class Users {

    public String name;
    public String uid;
    public String image;

    public Users(){}

    public Users(String name, String uid, String image) {
        this.name = name;
        this.uid = uid;
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
