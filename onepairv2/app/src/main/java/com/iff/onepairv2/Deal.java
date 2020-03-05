package com.iff.onepairv2;

import java.io.Serializable;

public class Deal implements Serializable {
    private int id;
    private String name;
    private String start;
    private String end;
    private String image;
    private String vendors;
    private String terms;
    private String category;

    public Deal(int id, String name, String start, String end, String image, String vendors, String terms, String category){
        this.id = id;
        this.name = name;
        this.start = start;
        this.end = end;
        this.image = image;
        this.vendors = vendors;
        this.terms = terms;
        this.category = category;
    }

    public int getId(){
        return this.id;
    }

    public String getName(){
        return this.name;
    }

    public String getStart(){
        return this.start;
    }

    public String getEnd(){
        return this.end;
    }

    public String getImage(){
        return this.image;
    }

    public String getVendors(){
        return this.vendors;
    }

    public String getTerms(){
        return this.terms;
    }

    public String getCategory(){
        return this.category;
    }

    public void printDeal(){
        System.out.println("Deal name:" + getName());
    }
}
