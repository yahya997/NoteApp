package com.example.noteapp.Model;

public class CategoryModel {
    private String name,image,from,categoryId;

    public CategoryModel() {
    }

    public CategoryModel(String name, String image, String from) {
        this.name = name;
        this.image = image;
        this.from = from;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
