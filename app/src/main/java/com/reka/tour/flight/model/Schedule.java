package com.reka.tour.flight.model;

/**
 * Created by fachrifebrian on 9/2/15.
 */
public class Schedule {
    String day,date,price;

    public Schedule(String day, String date, String price) {
        this.day = day;
        this.date = date;
        this.price = price;
    }

    public String getDay() {
        return day;
    }

    public String getDate() {
        return date;
    }

    public String getPrice() {
        return price;
    }
}
