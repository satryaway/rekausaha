package com.jixstreet.rekatoursandtravel.hotel.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Facilitiy implements Serializable {
    @SerializedName("facility_type")
    public String facilityType;
    @SerializedName("facility_name")
    public String facilityName;

//    "facility_type": "hotel",
//            "facility_name": "Layanan Kamar 24 Jam"
}
