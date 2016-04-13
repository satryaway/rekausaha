package com.jixstreet.rekatoursandtravel.hotel.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Breadcrumb implements Serializable {
    @SerializedName("business_id")
    public String business_id;
    @SerializedName("business_uri")
    public String business_uri;
    @SerializedName("business_name")
    public String businessName;
    @SerializedName("area_id")
    public String area_id;
    @SerializedName("area_name")
    public String areaName;
    @SerializedName("kelurahan_id")
    public String kelurahan_id;
    @SerializedName("kelurahan_name")
    public String kelurahan_name;
    @SerializedName("kecamatan_id")
    public String kecamatan_id;
    @SerializedName("kecamatan_name")
    public String kecamatan_name;
    @SerializedName("city_id")
    public String city_id;
    @SerializedName("city_name")
    public String city_name;
    @SerializedName("province_id")
    public String province_id;
    @SerializedName("province_name")
    public String province_name;
    @SerializedName("country_id")
    public String country_id;
    @SerializedName("country_name")
    public String country_name;
    @SerializedName("continent_id")
    public String continent_id;
    @SerializedName("continent_name")
    public String continentName;
    @SerializedName("star_rating")
    public String starRating;


//    "business_id": "3623",
//            "business_uri": "https://api-sandbox.tiket.com/the-sunset-hotel-restaurant",
//            "business_name": "The Sunset Hotel & Restaurant",
//            "area_id": "87227",
//            "area_name": "Legian",
//            "kelurahan_id": "46216",
//            "kelurahan_name": "Legian",
//            "kecamatan_id": "4172",
//            "kecamatan_name": "Kuta",
//            "city_id": "21720",
//            "city_name": "Bali",
//            "province_id": "17",
//            "province_name": "Bali",
//            "country_id": "id",
//            "country_name": "Indonesia",
//            "continent_id": "1",
//            "continent_name": "Asia",
//            "star_rating": "3"
}
