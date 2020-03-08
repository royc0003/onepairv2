package com.iff.onepairv2;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Results {

    @SerializedName("uid1")
    @Expose
    private String uID1;
    @SerializedName("uid2")
    @Expose
    private String uID2;


    public String getUid1(){
        return uID1;
    }


    public String getUid2() {
        return uID2;
    }

    public void setuID1(String uID1){
        this.uID1 = uID1;
    }
    public void setuID2(String uID2){
        this.uID2 = uID2;
    }




}