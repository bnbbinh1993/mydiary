package com.example.mydiary.adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mydiary.R;
import com.example.mydiary.activity.ShowDiaryActivity;
import com.example.mydiary.models.Diary;
import com.example.mydiary.models.ItemSub;
import com.example.mydiary.callback.OnClickItem;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@SuppressLint("SimpleDateFormat")
public class AdapterSub extends RecyclerView.Adapter<AdapterSub.ViewHolder> {
    private RecyclerView.RecycledViewPool viewPool = new RecyclerView.RecycledViewPool();
    private List<ItemSub> list;
    protected int i = 0;
    private Activity activity;
    private SimpleDateFormat f = new SimpleDateFormat("hh:mm - dd.MM.yyyy");
    private SimpleDateFormat fmoth = new SimpleDateFormat("MMM");

    private SimpleDateFormat fday = new SimpleDateFormat("EEE");

    public AdapterSub(List<ItemSub> list, Activity activity) {
        this.list = list;
        this.activity = activity;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_sub, parent, false));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ItemSub model = list.get(i);

        Calendar calendarEvent = Calendar.getInstance();
        Calendar calendarToDay = Calendar.getInstance();

        calendarEvent.setTimeInMillis(Long.parseLong(model.getmDate()));

        if (calendarEvent.get(Calendar.DAY_OF_MONTH) == calendarToDay.get(Calendar.DAY_OF_MONTH)
                && calendarEvent.get(Calendar.MONTH) + 1 == calendarToDay.get(Calendar.MONTH) + 1
                && calendarEvent.get(Calendar.YEAR) == calendarToDay.get(Calendar.YEAR)) {
            holder.mDate.setText(activity.getResources().getString(R.string._today)+" - " + calendarEvent.get(Calendar.DAY_OF_MONTH) + " " + String.valueOf(fmoth.format(calendarToDay.getTimeInMillis())) + " " + calendarToDay.get(Calendar.YEAR));
        } else {
            holder.mDate.setText(calendarEvent.get(Calendar.DAY_OF_MONTH) + " " + String.valueOf(fmoth.format(calendarEvent.getTimeInMillis())) + " " + calendarEvent.get(Calendar.YEAR));
        }

        List<Diary> diaryList = model.getList();
        Collections.reverse(diaryList);
        ShowAdapter showAdapter = new ShowAdapter(diaryList, activity);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(
                activity, LinearLayoutManager.VERTICAL, false
        );
        linearLayoutManager.setInitialPrefetchItemCount(model.getList().size());

        holder.mRecyclerview.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false));
        holder.mRecyclerview.setHasFixedSize(true);
        holder.mRecyclerview.setAdapter(showAdapter);

        holder.mRecyclerview.setRecycledViewPool(viewPool);


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public int getItemViewType(int position) {
        i = position;
        return i;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView mDate;
        private RecyclerView mRecyclerview;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mRecyclerview = itemView.findViewById(R.id.mRecyclerview);
            mDate = itemView.findViewById(R.id.txtDate);

        }
    }
}
