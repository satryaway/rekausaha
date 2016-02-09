package com.reka.tour.model;

/**
 * Created by fachrifebrian on 2/9/16.
 */
public class Passanger {
    String type, titel, name;

    public Passanger(String type, String titel, String name) {
        this.type = type;
        this.titel = titel;
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public String getTitel() {
        return titel;
    }

    public String getName() {
        return name;
    }

}
