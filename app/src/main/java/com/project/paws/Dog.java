package com.project.paws;

public class Dog extends Pet{
    private String name;
    private String breed;
    private String imageUrl;

    public Dog(String name, String breed, String imageUrl) {
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
