package com.reka.tour.flight.model;

import android.view.ViewGroup;

import java.io.Serializable;

/**
 * Created by satryaway on 4/10/2016.
 */
public class RequestedField implements Serializable {
    public RequiredField requiredField;
    public ViewGroup fieldWrapper;
    public String key;

    public RequestedField(RequiredField requiredField, ViewGroup fieldWrapper, String key) {
        this.requiredField = requiredField;
        this.fieldWrapper = fieldWrapper;
        this.key = key;
    }

}
