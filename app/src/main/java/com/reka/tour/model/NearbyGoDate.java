package com.reka.tour.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by satryaway on 12/26/2015.
 * airport entity
 */
public class NearbyGoDate implements Serializable {
    @SerializedName("date")
    public String date;

    @SerializedName("price")
    public String price;

}
