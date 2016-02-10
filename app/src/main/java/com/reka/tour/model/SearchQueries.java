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

//    "separator": {
//        "mandatory": 1,
//                "type": "text",
//                "example": "",
//                "FieldText": "Informasi Kontak yang Dapat Dihubungi",
//                "category": "separator",
//                "disabled": "false"
//    },
//            "conSalutation": {
//        "mandatory": 1,
//                "type": "combobox",
//                "example": "Mr",
//                "FieldText": "Titel",
//                "category": "contact",
//                "resource": [
//        {
//            "id": "Mr",
//                "name": "Tuan"
//        },
//        {
//            "id": "Mrs",
//                "name": "Nyonya"
//        },
//        {
//            "id": "Ms",
//                "name": "Nona"
//        }
//        ],
//        "disabled": "false"
//    },
//            "conFirstName": {
//        "mandatory": 1,
//                "type": "textbox",
//                "example": "Jane",
//                "FieldText": "Nama Depan",
//                "category": "contact",
//                "disabled": "false"
//    },
//            "conLastName": {
//        "mandatory": 0,
//                "type": "textbox",
//                "example": "wacob",
//                "FieldText": "Nama Belakang",
//                "category": "contact",
//                "disabled": "false"
//    },
//            "conPhone": {
//        "mandatory": 1,
//                "type": "textbox",
//                "example": "+6285212345678",
//                "FieldText": "No. Telepon",
//                "category": "contact",
//                "disabled": "false"
//    },
//            "conEmailAddress": {
//        "mandatory": 1,
//                "type": "textbox",
//                "example": "example@email.com",
//                "FieldText": "Kontak Email",
//                "category": "contact",
//                "disabled": "false"
//    },
//            "separator_adult1": {
//        "mandatory": 1,
//                "type": "text",
//                "example": "",
//                "FieldText": "Penumpang Dewasa 1",
//                "category": "separator",
//                "disabled": "false"
//    },
//            "titlea1": {
//        "mandatory": 1,
//                "type": "combobox",
//                "example": "Mr",
//                "FieldText": "Titel",
//                "category": "adult1",
//                "resource": [
//        {
//            "id": "Mr",
//                "name": "Tuan"
//        },
//        {
//            "id": "Mrs",
//                "name": "Nyonya"
//        },
//        {
//            "id": "Ms",
//                "name": "Nona"
//        }
//        ],
//        "disabled": "false"
//    },
//            "firstnamea1": {
//        "mandatory": 1,
//                "type": "textbox",
//                "example": "Jane",
//                "FieldText": "Nama Depan",
//                "category": "adult1",
//                "disabled": "false"
//    },
//            "lastnamea1": {
//        "mandatory": 0,
//                "type": "textbox",
//                "example": "wacob",
//                "FieldText": "Nama Belakang",
//                "category": "adult1",
//                "disabled": "false"
//    }

}
