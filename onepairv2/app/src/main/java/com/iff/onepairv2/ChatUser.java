package com.iff.onepairv2;

/**
 * Each ChatUser object stores the name and icon
 * of a matched user for displaying purposes
 */
public class ChatUser
{
    /**
     * Name of a matched user
     */
    String title;
    /**
     * Description of matched user (not implemented)
     */
    String desc;
    /**
     * The profile picture of a matched user
     */
    String icon;

    /**
     * Constructor for a new ChatUser object
     * @param title Name of a matched user
     * @param desc Description of a matched user
     * @param icon Profile picture of a matched user
     */
    public ChatUser(String title, String desc, String icon) {
        this.title = title;
        this.desc = desc;
        this.icon = icon;
    }

    /**
     * Accessor of ChatUser's description
     * @return ChatUser's description
     */
    public String getDesc() {
        return this.desc;
    }

    /**
     * Accessor of ChatUser's title
     * @return ChatUser's title
     */
    public String getTitle() {
        return this.title;
    }

    /**
     * Accessor of ChatUser's icon
     * @return ChatUser's icon
     */
    public String getIcon() {
        return this.icon;
    }
}

