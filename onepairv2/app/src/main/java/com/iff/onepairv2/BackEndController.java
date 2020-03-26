package com.iff.onepairv2;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Interface for BackEndController that links
 * the system to the backend server
 */
public interface BackEndController {
    /**
     * Constant created for the server's URL
     */
    public static final String URL = "http://128.199.167.80:8080/";

    /**
     * HTTP request for retrieving of all the deals in the server
     * @return ArrayList of all deals
     */
    @GET("getAllDeals")
    Call<ArrayList<Deal>> getAllDeals();

    /**
     * HTTP request for retrieving of all food deals in the server
     * @return ArrayList of all food deals
     */
    @GET("getFoodDeals")
    Call<ArrayList<Deal>> getFoodDeals();

    /**
     * HTTP request for retrieving of all entertainment deals in the server
     * @return ArrayList of all entertainment deals
     */
    @GET("getEntertainmentDeals")
    Call<ArrayList<Deal>> getEntertainmentDeals();

    /**
     * HTTP request for retrieving of all retail deals in the server
     * @return ArrayList of all retail deals
     */
    @GET("getRetailDeals")
    Call<ArrayList<Deal>> getRetailDeals();

    /**
     * HTTP request for retrieving of all other deals in the server
     * @return ArrayList of all other deals
     */
    @GET("getOthersDeals")
    Call<ArrayList<Deal>> getOthersDeals();

    /**
     * HTTP request for storing a new user in the server
     * upon registration
     * @param uid User id of newly created user
     * @return void
     */
    @GET("addUser/{uid}")
    Call<Void> addUser(@Path("uid") String uid);

    /**
     * HTTP request for storing a user in a blacklist
     * in the server upon blocking a user
     * @param uid1 User id of user blocking the other user
     * @param uid2 User id of user getting blocked
     * @return void
     */
    @GET("addBlacklist/{uid1}/{uid2}")
    Call<Void> addBlacklist(@Path("uid1") String uid1, @Path("uid2") String uid2);

    /**
     * HTTP request for user to find a match for a particular deal
     * @param uid User id of current user
     * @param dealid Deal id of selected deal
     * @param c Category of deal
     * @return void
     */
    @GET("addRequest/{uid}/{dealid}/{c}")
    Call<Void> addRequest(@Path("uid") String uid, @Path("dealid") int dealid, @Path("c") String c);

    /**
     * HTTP request for user to cancel matching process for a particular deal
     * @param uid User id of current user
     * @param dealid Deal id of selected deal
     * @return void
     */
    @GET("deleteRequest/{uid}/{dealid}")
    Call<Void> deleteRequest(@Path("uid") String uid, @Path("dealid") int dealid);

    /**
     * HTTP request for retrieving of all locations in the server
     * @return ArrayList of all locations
     */
    @GET("getAllLocation")
    Call<ArrayList<Location>> getAllLocation();

    /**
     * HTTP request for updating of a user's token in the server
     * @param uid User id of current user
     * @param token Token of current user
     * @return void
     */
    @GET("updateToken/{uid}/{token}")
    Call<Void> updateToken(@Path("uid") String uid, @Path("token") String token);
}
