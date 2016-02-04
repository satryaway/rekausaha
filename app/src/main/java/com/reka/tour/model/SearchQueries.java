package com.reka.tour.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by satryaway on 12/26/2015.
 * airport entity
 */
public class SearchQueries implements Serializable {
    @SerializedName("from")
    public String from;
    @SerializedName("to")
    public String to;
    @SerializedName("date")
    public String date;
    @SerializedName("ret_date")
    public String ret_date;
    @SerializedName("adult")
    public String adult;
    @SerializedName("child")
    public String child;
    @SerializedName("infant")
    public String infant;
    @SerializedName("sort")
    public String sort;

//    "from": "CGK",
//            "to": "DPS",
//            "date": "2016-02-05",
//            "ret_date": "2014-05-30",
//            "adult": "1",
//            "child": "0",
//            "infant": "0",
//            "sort": false

}
