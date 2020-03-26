package com.iff.onepairv2;

import java.io.Serializable;

/**
 * Each deal object contains relevant information of the deal such as its id, name , start date, end date
 * image url, venfors. terms and category
 * @author ifandonlyif
 */

public class Deal implements Serializable {
    /** id of the deal */
    private int id;
    /** name of the deal */
    private String name;
    /** start date of the deal */
    private String start;
    /** end date of the deal */
    private String end;
    /** image url of the deal */
    private String image;
    /** vendors of the deal */
    private String vendors;
    /** terms of the deal */
    private String terms;
    /** category of the deal */
    private String category;

    /**
     * Constructor for a Deal object
     * @param id
     * @param name
     * @param start
     * @param end
     * @param image
     * @param vendors
     * @param terms
     * @param category
     */
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

    /**
     * Returns the ID of the deal
     * @return ID of the deal
     */
    public int getId(){
        return this.id;
    }

    /**
     * Returns the name of the deal
     * @return name of the deal
     */
    public String getName(){
        return this.name;
    }

    /**
     * Returns start date of the deal
     * @return start date
     */
    public String getStart(){
        return this.start;
    }

    /**
     * Returns end date of the deal
     * @return end date
     */
    public String getEnd(){
        return this.end;
    }

    /**
     * Return image url of the deal
     * @return image url of the deal
     */
    public String getImage(){
        return this.image;
    }

    /**
     * Return vendors of the deal
     * @return vendors of the deal
     */
    public String getVendors(){
        return this.vendors;
    }

    /**
     * Return terms of the deal
     * @return terms of the deal
     */
    public String getTerms(){
        return this.terms;
    }

    /**
     * Return category of the deal
     * @return category of the deal
     */
    public String getCategory(){
        return this.category;
    }

    /**
     * Prints deal name
     */
    public void printDeal(){
        System.out.println("Deal name:" + getName());
    }
}
