package com.example.mydiary.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.mydiary.R;
import com.example.mydiary.database.DatabaseCount;
import com.example.mydiary.models.Count;
import com.example.mydiary.models.Diary;
import com.example.mydiary.utils.OnClickItem;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class CountAdapter extends RecyclerView.Adapter<CountAdapter.ViewHolder> {
    private Context context;
    private List<Count> list = new ArrayList<>();
    private static OnClickItem onClickItem;

    public CountAdapter(Context context, List<Count> list) {
        this.context = context;
        this.list = list;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_coutdown, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.setIsRecyclable(false);
        Count model = list.get(position);
        holder.name.setText(model.getTitle());
        holder.time.setText(model.getDate());
        holder.place.setText(model.getPlace());
        if (model.getVote() == 0) {
            SimpleDateFormat format = new SimpleDateFormat("hh:mm - dd.MM.yyyy");
            try {
                long timeCount = format.parse(model.getDate()).getTime();
                long timeRes = timeCount - System.currentTimeMillis();
                if (timeRes > 0) {
                    long day = (timeRes / 1000) / 86400;
                    long hours = (timeRes / 1000) % 86400 / 60 / 60;
                    long minute = (timeRes / 1000) % 86400 / 60 % 60;
                    long seconds = (timeRes / 1000) % 86400 % 60;
                    holder.count.setText(day + " " + context.getResources().getString(R.string._day_item)
                            + " " + hours + " " + context.getResources().getString(R.string._hours_item)
                            + " " + minute + " " + context.getResources().getString(R.string._minute_item)
                            + " " + seconds + " " + context.getResources().getString(R.string._seconds_item));
                } else {
                    holder.count.setText("Đã hoàn thành");
                    DatabaseCount count = new DatabaseCount(context);
                    model.setVote(1);
                    count.update(model);
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        } else {
            holder.count.setText("Đã hoàn thành");
        }


        if (position == 0) {
            holder.ovel.setBackgroundResource(R.drawable.bg_frame_1);
        } else if (position == 1) {
            holder.ovel.setBackgroundResource(R.drawable.bg_frame_2);
        } else if (position == 2) {
            holder.ovel.setBackgroundResource(R.drawable.bg_frame_3);
        }

    }

    public void setOnClickItem(OnClickItem onClickItem1) {
        this.onClickItem = onClickItem1;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        private TextView name;
        private TextView time;
        private TextView count;
        private TextView place;
        private FrameLayout ovel;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.mName);
            time = itemView.findViewById(R.id.mTime);
            count = itemView.findViewById(R.id.mCount);
            ovel = itemView.findViewById(R.id.mOvel);
            place = itemView.findViewById(R.id.mPlace);


        }

        @Override
        public void onClick(View v) {
            onClickItem.click(getAdapterPosition());
        }

        @Override
        public boolean onLongClick(View v) {
            onClickItem.longClick(getAdapterPosition());
            return false;
        }
    }
}
