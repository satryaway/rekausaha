package com.reka.tour.flight.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by satryaway on 12/26/2015.
 * airport entity
 */
public class Departures implements Serializable {
    @SerializedName("flight_id")
    public String flightId;
    @SerializedName("airlines_name")
    public String airlinesName;
    @SerializedName("flight_number")
    public String flightNumber;
    @SerializedName("departure_city")
    public String departureCity;
    @SerializedName("arrival_city")
    public String arrivalCity;
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
    @SerializedName("timestamp")
    public String timestamp;
    @SerializedName("has_food")
    public String hasFood;
    @SerializedName("check_in_baggage")
    public String checkInBaggage;
    @SerializedName("is_promo")
    public String isPromo;
    @SerializedName("airport_tax")
    public String airportTax;
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
    @SerializedName("best_deal")
    public String bestDeal;
    @SerializedName("duration")
    public String duration;
    @SerializedName("image")
    public String image;
    @SerializedName("departure_city_name")
    public String departureCityName;
    @SerializedName("arrival_city_name")
    public String arrivalCityName;

//    "flight_id": "1908449",
//            "airlines_name": "SRIWIJAYA",
//            "flight_number": "SJ-260",
//            "departure_city": "CGK",
//            "arrival_city": "DPS",
//            "stop": "Langsung",
//            "price_value": "472900.00",
//            "price_adult": "472900.00",
//            "price_child": "0.00",
//            "price_infant": "0.00",
//            "timestamp": "2016-02-03 09:16:52",
//            "has_food": "1",
//            "check_in_baggage": "20",
//            "is_promo": 0,
//            "airport_tax": true,
//            "simple_departure_time": "15:10",
//            "simple_arrival_time": "18:00",
//            "long_via": "",
//            "full_via": "CGK - DPS (15:10 - 18:00)",
//            "markup_price_string": "",
//            "need_baggage": 0,
//            "best_deal": false,
//            "duration": "1 j 50 m",
//            "image": "http://api-sandbox.tiket.com/images/tiket2/icon_sriwijaya_2.jpg",
//            "flight_infos": {
//        "flight_info": [
//        {
//            "flight_number": "SJ-260",
//                "departure_city": "CGK",
//                "arrival_city": "DPS",
//                "simple_departure_time": "15:10",
//                "simple_arrival_time": "18:00"
//        }
//        ]
//    },
//            "departure_city_name": "Jakarta",
//            "arrival_city_name": "Denpasar",

}
