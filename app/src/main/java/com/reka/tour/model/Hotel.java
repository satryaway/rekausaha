package com.reka.tour.model;

/**
 * Created by fachrifebrian on 2/15/16.
 */
public class Hotel {
    String image,rating,name,price;

    public Hotel(String image, String rating, String name, String price) {
        this.image = image;
        this.rating = rating;
        this.name = name;
        this.price = price;
    }

    public String getImage() {
        return image;
    }

    public String getRating() {
        return rating;
    }

    public String getName() {
        return name;
    }

    public String getPrice() {
        return price;
    }
}

