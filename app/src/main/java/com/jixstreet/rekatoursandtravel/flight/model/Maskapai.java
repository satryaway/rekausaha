package com.jixstreet.rekatoursandtravel.flight.model;

/**
 * Created by fachrifebrian on 2/9/16.
 */
public class Maskapai {
    String airplane;
Boolean status;

    public Maskapai(String airplane, Boolean status) {
        this.airplane = airplane;
        this.status = status;
    }

    public String getAirplane() {
        return airplane;
    }

    public Boolean getStatus() {
        return status;
    }
}
