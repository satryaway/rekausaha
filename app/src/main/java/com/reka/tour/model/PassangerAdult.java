package com.reka.tour.model;

/**
 * Created by fachrifebrian on 2/11/16.
 */
public class PassangerAdult {
    String titel, firstName, lastName;

    public PassangerAdult(String titel, String firstName, String lastName) {
        this.titel = titel;
        this.firstName = firstName;
        this.lastName = lastName;
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
}
