package com.iff.onepairv2;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;

public interface BackEndController {
    @GET("getAllDeals")
    Call<ArrayList<Deal>> getAllDeals();

    @GET("getFoodDeals")
    Call<ArrayList<Deal>> getFoodDeals();

    @GET("getEntertainmentDeals")
    Call<ArrayList<Deal>> getEntertainmentDeals();

    @GET("getRetailDeals")
    Call<ArrayList<Deal>> getRetailDeals();

    @GET("getOthersDeals")
    Call<ArrayList<Deal>> getOthersDeals();
}
