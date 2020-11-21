package com.example.mydiary.models;

public class App {
    private String name;
    private String category;
    private int icon;
    private int color;

    public App(String name, String category, int icon, int color) {
        this.name = name;
        this.category = category;
        this.icon = icon;
        this.color = color;
    }

    public App() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }
}
