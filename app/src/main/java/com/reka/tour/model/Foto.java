package com.reka.tour.model;

public class Foto {
    String imageId,imageName;

    public Foto(String imageId, String imageName) {
        this.imageId = imageId;
        this.imageName = imageName;
    }

    public String getImageId() {
        return imageId;
    }

    public String getImageName() {
        return imageName;
    }
}
