package com.reka.tour.flight.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by satryaway on 4/10/2016.
 */
public class RequiredField implements Serializable {
    @SerializedName("mandatory")
    public int mandatory;

    @SerializedName("type")
    public String type;

    @SerializedName("example")
    public String example;

    @SerializedName("FieldText")
    public String fieldText;

    @SerializedName("category")
    public String category;

    @SerializedName("disabled")
    public String disabled;

    public String value;
}
