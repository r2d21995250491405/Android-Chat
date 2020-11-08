package com.example.firebasechat;

public class User {
    private String name, email, id;
    private int imageResource;

    public User(String name, String email, String id, int imageResource) {
        this.name = name;
        this.email = email;
        this.id = id;
        this.imageResource = imageResource;
    }

    public User() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getImageResource() {
        return imageResource;
    }

    public void setImageResource(int imageResource) {
        this.imageResource = imageResource;
    }
}
