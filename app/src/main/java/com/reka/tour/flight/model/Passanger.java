package com.reka.tour.flight.model;

/**
 * Created by fachrifebrian on 2/9/16.
 */
public class Passanger {
    String type, titel, firstName,lastName;
    int id;

    public Passanger(String type, String titel, String firstName, String lastName, int id) {
        this.type = type;
        this.titel = titel;
        this.firstName = firstName;
        this.lastName = lastName;
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public String getTitel() {
        return titel;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public int getId() {
        return id;
    }
}
