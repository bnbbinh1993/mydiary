package com.example.mydiary.models;

import java.util.List;

public class ItemSub {
    private String mDate;
    private List<Diary> list;

    public ItemSub(String mDate, List<Diary> list) {
        this.mDate = mDate;
        this.list = list;
    }

    public String getmDate() {
        return mDate;
    }

    public void setmDate(String mDate) {
        this.mDate = mDate;
    }

    public List<Diary> getList() {
        return list;
    }

    public void setList(List<Diary> list) {
        this.list = list;
    }
}
