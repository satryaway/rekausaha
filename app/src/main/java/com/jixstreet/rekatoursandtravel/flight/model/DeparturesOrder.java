package com.jixstreet.rekatoursandtravel.flight.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by satryaway on 12/26/2015.
 * airport entity
 */
public class DeparturesOrder implements Serializable {
    @SerializedName("flight_id")
    public String flightId;
    @SerializedName("airlines_name")
    public String airlinesName;
    @SerializedName("flight_number")
    public String flightNumber;
    @SerializedName("flight_date")
    public String flightDate;
    @SerializedName("stop")
    public String stop;
    @SerializedName("price_value")
    public String priceValue;
    @SerializedName("price_adult")
    public String priceAdult;
    @SerializedName("price_child")
    public String priceChild;
    @SerializedName("price_infant")
    public String priceInfant;
    @SerializedName("count_adult")
    public String countAdult;
    @SerializedName("count_child")
    public String countChild;
    @SerializedName("count_infant")
    public String countInfant;
    @SerializedName("timestamp")
    public String timestamp;
    @SerializedName("simple_departure_time")
    public String simpleDepartureTime;
    @SerializedName("simple_arrival_time")
    public String simpleArrivalTime;
    @SerializedName("long_via")
    public String longVia;
    @SerializedName("full_via")
    public String fullVia;
    @SerializedName("markup_price_string")
    public String markupPriceString;
    @SerializedName("need_baggage")
    public String needBaggage;
    @SerializedName("duration")
    public String duration;
    @SerializedName("image")
    public String image;
    public String status;

//    "flight_id": "2037033",
//            "airlines_name": "GARUDA",
//            "flight_number": "GA-349/GA-331",
//            "flight_date": "2016-03-30",
//            "stop": "1 Transit",
//            "price_value": "2502000.00",
//            "price_adult": "1251000.00",
//            "price_child": "0.00",
//            "price_infant": "0.00",
//            "count_adult": "2",
//            "count_child": "0",
//            "count_infant": "0",
//            "timestamp": "2016-02-10 21:05:58",
//            "simple_departure_time": "19:30",
//            "simple_arrival_time": "23:25",
//            "long_via": "Surabaya (SUB)",
//            "full_via": "DPS - SUB (19:30 - 19:40), SUB - CGK (21:50 - 23:25)",
//            "markup_price_string": "",
//            "need_baggage": 0,
//            "duration": "4 j 55 m",
//            "image": "http://api-sandbox.tiket.com/images/tiket2/icon_garuda_2.jpg",

}
