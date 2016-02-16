package com.reka.tour.hotel.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by fachrifebrian on 2/16/16.
 */
public class HotelArea implements Serializable {
    @SerializedName("id")
    public String id;
    @SerializedName("weight")
    public String weight;
    @SerializedName("distance")
    public String distance;
    @SerializedName("skey")
    public String skey;
    @SerializedName("country_id")
    public String countryId;
    @SerializedName("label")
    public String label;
    @SerializedName("value")
    public String value;
    @SerializedName("category")
    public String category;
    @SerializedName("tipe")
    public String tipe;
    @SerializedName("business_uri")
    public String businessUri;


}
