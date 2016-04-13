package com.jixstreet.rekatoursandtravel.flight.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by fachrifebrian on 2/10/16.
 */
public class Resource {
    @SerializedName("id")
    public String id;
    @SerializedName("name")
    public String name;

    public Resource(String id, String name) {
        this.id = id;
        this.name = name;
    }
}
