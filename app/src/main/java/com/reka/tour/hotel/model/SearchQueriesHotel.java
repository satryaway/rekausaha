package com.reka.tour.hotel.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by satryaway on 12/26/2015.
 * airport entity
 */
public class SearchQueriesHotel implements Serializable {
    @SerializedName("night")
    public String night;
    @SerializedName("room")
    public String room;
    @SerializedName("adult")
    public String adult;

//    "search_queries": {
//        "q": "Bandung",
//                "uid": "",
//                "startdate": "2016-02-20",
//                "enddate": "2016-02-21",
//                "night": "1",
//                "room": 1,
//                "adult": "2",
//                "child": 0,
//                "sort": false,
//                "minstar": 0,
//                "maxstar": 5,
//                "minprice": "158000.00",
//                "maxprice": "8586000.00",
//                "distance": 100000
//    },

}
