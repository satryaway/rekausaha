package com.reka.tour.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by fachrifebrian on 2/13/16.
 */
public class MyOrder implements Serializable {
    @SerializedName("expire")
    public String expire;
    @SerializedName("uri")
    public String uri;
    @SerializedName("order_detail_id")
    public String orderDetailId;
    @SerializedName("order_expire_datetime")
    public String orderExpireDatetime;
    @SerializedName("order_type")
    public String orderType;
    @SerializedName("customer_price")
    public String customerPrice;
    @SerializedName("order_name")
    public String orderName;
    @SerializedName("order_name_detail")
    public String orderNameDetail;
    @SerializedName("order_detail_status")
    public String orderDetailStatus;
    @SerializedName("order_photo")
    public String orderPhoto;
    @SerializedName("order_icon")
    public String orderIcon;
    @SerializedName("subtotal_and_charge")
    public String subtotalAndCharge;
    @SerializedName("delete_uri")
    public String deleteUri;
    @SerializedName("business_id")
    public String businessId;
//    "expire": 0,
//            "uri": "citilink",
//            "order_detail_id": "12685547",
//            "order_expire_datetime": "2016-02-11 01:52:50",
//            "order_type": "flight",
//            "customer_price": "2178829.00",
//            "order_name": "CGK (Jakarta - Cengkareng) - DPS (Denpasar, Bali)",
//            "order_name_detail": "Citilink (QG-901/QG-650 - Depart)",
//            "order_detail_status": "recheck",
//            "order_photo": "https://api-sandbox.tiket.com/images/icon_citilink.jpg",
//            "order_icon": "h3b",
//            "tax_and_charge": "44500.97",
//            "subtotal_and_charge": "2223329.97",
//            "delete_uri": "https://api-sandbox.tiket.com/order/delete_order?order_detail_id=12685547",
//            "business_id": "20865"

}
