package com.reka.tour.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by fachrifebrian on 2/17/16.
 */
public class Steps {
    @SerializedName("name")
    public String name;
    @SerializedName("step")
    public String[] step;
}
