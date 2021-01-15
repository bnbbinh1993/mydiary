package com.example.mydiary.adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mydiary.R;
import com.example.mydiary.models.ImageSub;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

@SuppressLint("SimpleDateFormat")
public class AdapterImageSub extends RecyclerView.Adapter<AdapterImageSub.ViewHolder> {
    private RecyclerView.RecycledViewPool viewPool = new RecyclerView.RecycledViewPool();
    private List<ImageSub> list;
    protected int i = 0;
    private Activity activity;

    private SimpleDateFormat f = new SimpleDateFormat("hh:mm - dd.MM.yyyy");
    private SimpleDateFormat fmoth = new SimpleDateFormat("MMM");
    private SimpleDateFormat fday = new SimpleDateFormat("EEE");

    public AdapterImageSub(List<ImageSub> list, Activity activity) {
        this.list = list;
        this.activity = activity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(activity).inflate(R.layout.item_image_sub, parent, false));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ImageSub model = list.get(i);
        Calendar calendarEvent = Calendar.getInstance();
        Calendar calendarToDay = Calendar.getInstance();

        calendarEvent.setTimeInMillis(Long.parseLong(model.getTime()));

        if (calendarEvent.get(Calendar.DAY_OF_MONTH) == calendarToDay.get(Calendar.DAY_OF_MONTH)
                && calendarEvent.get(Calendar.MONTH) + 1 == calendarToDay.get(Calendar.MONTH) + 1
                && calendarEvent.get(Calendar.YEAR) == calendarToDay.get(Calendar.YEAR)) {
            holder.mDate.setText(activity.getResources().getString(R.string._today)+" - " + calendarEvent.get(Calendar.DAY_OF_MONTH) + " " + String.valueOf(fmoth.format(calendarToDay.getTimeInMillis())) + " " + calendarToDay.get(Calendar.YEAR));
        } else {
            holder.mDate.setText(calendarEvent.get(Calendar.DAY_OF_MONTH) + " " + String.valueOf(fmoth.format(calendarEvent.getTimeInMillis()))+ " " + calendarEvent.get(Calendar.YEAR));
        }


        ImageSubAdapter adapter = new ImageSubAdapter(model.getList(), activity);
        holder.mRecyclerview.setHasFixedSize(true);
        holder.mRecyclerview.setLayoutManager(new GridLayoutManager(activity, 4));
        holder.mRecyclerview.setAdapter(adapter);

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
        private RecyclerView mRecyclerview;
        private TextView mDate;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mRecyclerview = itemView.findViewById(R.id.mRecyclerview);
            mDate = itemView.findViewById(R.id.txtDate);

        }
    }

}
