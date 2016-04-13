package com.jixstreet.rekatoursandtravel.hotel.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by fachrifebrian on 2/15/16.
 */
public class Room {
    @SerializedName("id")
    public String id;
    @SerializedName("room_available")
    public String roomAvailable;
    @SerializedName("room_id")
    public String roomId;
    @SerializedName("minimum_stays")
    public String minimum_stays;
    @SerializedName("with_breakfasts")
    public String with_breakfasts;
    @SerializedName("photo_url")
    public String photoUrl;
    @SerializedName("all_photo_room")
    public String[] all_photo_room;
    @SerializedName("room_name")
    public String roomName;
    @SerializedName("oldprice")
    public String oldprice;
    @SerializedName("price")
    public String price;
    @SerializedName("bookUri")
    public String bookUri;
    @SerializedName("room_facility")
    public String[] roomFacility;
    @SerializedName("room_description")
    public String roomDescription;

//    "id": "153020160218",
//            "room_available": "9",
//            "room_id": "1530",
//            "currency": "IDR",
//            "minimum_stays": "1",
//            "with_breakfasts": "0",
//            "all_photo_room": [
//            "http://api-sandbox.tiket.com/img/business/1/0/business-10_super_deluxe_room.room.jpg"
//            ],
//            "photo_url": "http://api-sandbox.tiket.com/img/business/1/0/business-10_super_deluxe_room.s.jpg",
//            "room_name": "Super Deluxe With Breakfast",
//            "oldprice": "2439855.00",
//            "price": "1325000.00",
//            "bookUri": "https://api-sandbox.tiket.com/order/add/hotel?startdate=2016-02-18&enddate=2016-02-19&night=1&room=1&adult=2&child=0&minstar=0&maxstar=0&minprice=0&maxprice=830000&hotelname=0&is_partner=0&room_id=1530&hasPromo=0",
//            "room_facility": [
//            "AC",
//            "Teras",
//            "Mesin pembuat kopi/teh",
//            "Air mineral botol gratis",
//            "Meja",
//            "Pengering rambut dengan permintaan",
//            "Pengering rambut berdasarkan permintaan",
//            "Brankas",
//            "Internet - Wifi (gratis)",
//            "Internet - Wifi",
//            "Televisi LCD/layar plasma",
//            "Bar kecil",
//            "Cermin",
//            "Kamar bebas rokok ada tergantung ketersediaan",
//            "Satelit/TV kabel",
//            "Shower",
//            "Sandal",
//            "Telepon"
//            ],
//            "room_description": false,
//            "additional_surcharge_currency": ""
}
