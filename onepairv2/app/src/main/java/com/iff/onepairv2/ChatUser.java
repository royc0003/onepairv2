package com.iff.onepairv2;

public class ChatUser
{
    String title;
    String desc;
    String icon;

    //constructor
    public ChatUser(String title, String desc, String icon) {
        this.title = title;
        this.desc = desc;
        this.icon = icon;
    }

    //getters


    public String getDesc() {
        return this.desc;
    }

    public String getTitle() {
        return this.title;
    }

    public String getIcon() {
        return this.icon;
    }
}

