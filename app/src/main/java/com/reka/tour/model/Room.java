package com.reka.tour.model;

/**
 * Created by fachrifebrian on 2/15/16.
 */
public class Room {
    String name,price,count;

    public Room(String name, String price, String count) {
        this.name = name;
        this.price = price;
        this.count = count;
    }

    public String getName() {
        return name;
    }

    public String getPrice() {
        return price;
    }

    public String getCount() {
        return count;
    }
}
