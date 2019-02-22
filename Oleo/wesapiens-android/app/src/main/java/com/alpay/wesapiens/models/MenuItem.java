package com.alpay.wesapiens.models;

public class MenuItem {
    String title;
    int image;
    boolean isActive;

    public MenuItem(String title, int image, boolean isActive) {
        this.title = title;
        this.image = image;
        this.isActive = isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public boolean isActive() {
        return isActive;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }
}
