package com.example.mydiary.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mydiary.R;
import com.example.mydiary.models.EventCalendar;
import com.example.mydiary.utils.ItemClick;
import com.example.mydiary.utils.OnClickItem;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class EventCalendarAdapter extends RecyclerView.Adapter<EventCalendarAdapter.ViewHolder> {
    private Context context;
    private List<EventCalendar> list = new ArrayList<>();
    private ItemClick itemClick;
    private int choice;
    private Calendar calendar = Calendar.getInstance();
    private long check = calendar.getTimeInMillis();

    public EventCalendarAdapter(Context context, List<EventCalendar> list) {
        this.context = context;
        this.list = list;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_event_calendar, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        EventCalendar model = list.get(choice);
        String s[] = model.getContent().split(":");

        calendar.setTimeInMillis(model.getLoc());
        calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(s[0].trim()));
        calendar.set(Calendar.MINUTE, Integer.parseInt(s[1].trim()));
        holder.timeText.setText(model.getContent());
        holder.contentText.setText(model.getTime());
        if (calendar.getTimeInMillis() < check) {
            holder.timeText.setTextColor(context.getResources().getColor(R.color.red_orange_fake));
            holder.contentText.setTextColor(context.getResources().getColor(R.color.black_fake_2));
        }


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public int getItemViewType(int position) {
        choice = position;
        return choice;
    }

    public void setOnClickItem(ItemClick itemClick) {
        this.itemClick = itemClick;
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView timeText;
        private TextView contentText;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            timeText = itemView.findViewById(R.id.timeText);
            contentText = itemView.findViewById(R.id.contentText);
            itemView.setOnClickListener(this::onClick);
        }

        @Override
        public void onClick(View v) {
            itemClick.Click(getAdapterPosition());
        }
    }
}
