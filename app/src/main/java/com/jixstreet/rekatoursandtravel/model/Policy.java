package com.jixstreet.rekatoursandtravel.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by satryaway on 4/14/2016.
 */
public class Policy implements Serializable {
    public String name;

    public ArrayList<String> before;

    public ArrayList<String> after;

    public String type;

}
