package com.reka.tour.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by satryaway on 12/26/2015.
 * airport entity
 */
public class Airport implements Serializable {
    @SerializedName("airport_name")
    public String name;

    @SerializedName("airport_code")
    public String code;

    @SerializedName("location_name")
    public String locationName;

    @SerializedName("country_id")
    public String countryId;

    @SerializedName("country_name")
    public String countryName;
}
