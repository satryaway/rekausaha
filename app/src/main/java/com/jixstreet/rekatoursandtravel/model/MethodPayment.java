package com.jixstreet.rekatoursandtravel.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by fachrifebrian on 2/13/16.
 */
public class MethodPayment implements Serializable {
    @SerializedName("link")
    public String link;
    @SerializedName("text")
    public String text;
    @SerializedName("message")
    public String message;
    @SerializedName("type")
    public String type;

//    "link": "https://api-sandbox.tiket.com/checkout/checkout_payment/35",
//            "text": "ATM Transfer",
//            "message": "",
//            "type": "jatis"
}
