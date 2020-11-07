package com.example.mydiary.models;

public class Show {
    private int id;
    private String title;
    private String content;
    private String date;
    private String address;
    private byte [] image;
    private int vote;
    private int filter;


    public Show() {
    }

    public Show(int id,String title, String content, String date, String address, byte[] image, int vote, int filter) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.date = date;
        this.address = address;
        this.image = image;
        this.vote = vote;
        this.filter = filter;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public int getVote() {
        return vote;
    }

    public void setVote(int vote) {
        this.vote = vote;
    }

    public int getFilter() {
        return filter;
    }

    public void setFilter(int filter) {
        this.filter = filter;
    }
}
