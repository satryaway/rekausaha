package com.reka.tour.model;

/**
 * Created by fachrifebrian on 9/2/15.
 */
public class Flight {
    String image,name,time,duration,price;

    public Flight(String image, String name, String time, String duration, String price) {
        this.image = image;
        this.name = name;
        this.time = time;
        this.duration = duration;
        this.price = price;
    }

    public String getImage() {
        return image;
    }

    public String getName() {
        return name;
    }

    public String getTime() {
        return time;
    }

    public String getDuration() {
        return duration;
    }

    public String getPrice() {
        return price;
    }
}
