package com.jixstreet.rekatoursandtravel.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by fachrifebrian on 2/17/16.
 */
public class Country {
    @SerializedName("country_id")
    public String countryId;

    @SerializedName("country_name")
    public String countryName;

    @SerializedName("country_areacode")
    public String countryAreacode;

}
