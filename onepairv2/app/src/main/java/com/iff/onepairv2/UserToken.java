package com.iff.onepairv2;

/**
 * Each UserToken object stores a user's token information which is used for sending push notifications
 */
public class UserToken {
    /**
     * User's email
     */
    public String email;
    /**
     * User's session token
     */
    public String token;

    /**
     * Constructor for new UserToken
     * @param email Email of user
     * @param token Token of user
     */
    public UserToken(String email, String token) {
        this.email = email;
        this.token = token;
    }
}
