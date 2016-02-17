package com.reka.tour.hotel.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Foto implements Serializable {
    @SerializedName("file_name")
    public String fileName;
    @SerializedName("photo_type")
    public String photoType;
}
