package com.project.paws;

public class Bird extends Pet{
    private String name;
    private String breed;
    private String imageUrl;

    public Bird(String name, String breed, String imageUrl) {
        this.name = name;
        this.breed = breed;
        this.imageUrl = imageUrl;
    }

    public String getName() {
        return name;
    }

    public String getBreed() {
        return breed;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}
