package com.example.mydiary.models;

import java.util.ArrayList;
import java.util.List;

public class ImageSub {
    private String time;
    private List<String> list;

    public ImageSub(String time, List<String> list) {
        this.time = time;
        this.list = list;
    }

    public ImageSub() {
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public List<String> getList() {
        return list;
    }

    public void setList(List<String> list) {
        this.list = list;
    }
}
