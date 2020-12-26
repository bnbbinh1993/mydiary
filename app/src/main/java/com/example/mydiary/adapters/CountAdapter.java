package com.example.mydiary.adapters;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mydiary.R;
import com.example.mydiary.activity.ShowFollowActivity;
import com.example.mydiary.database.DatabaseCount;
import com.example.mydiary.models.Count;
import com.example.mydiary.receiver.AlarmReceiver;
import com.example.mydiary.utils.OnClickItem;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class CountAdapter extends RecyclerView.Adapter<CountAdapter.ViewHolder> {

    private List<Count> list = new ArrayList<>();
    private static OnClickItem onClickItem;
    private int choice;
    private Activity activity;

    public CountAdapter(Activity activity, List<Count> list) {
        this.activity = activity;
        this.list = list;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(activity).inflate(R.layout.item_coutdown, parent, false));
    }

    @Override
    public int getItemViewType(int position) {
        choice = position;
        return choice;
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Count model = list.get(choice);
        holder.name.setText(model.getTitle());
        holder.time.setText(model.getDate());
        holder.place.setText(model.getPlace());
        if (model.getVote() == 0) {
            SimpleDateFormat format = new SimpleDateFormat("HH:mm - dd.MM.yyyy");
            try {
                long timeCount = format.parse(model.getDate()).getTime();
                long timeRes = timeCount - System.currentTimeMillis();
                if (timeRes > 0) {
                    long day = (timeRes / 1000) / 86400;
                    long hours = (timeRes / 1000) % 86400 / 60 / 60;
                    long minute = (timeRes / 1000) % 86400 / 60 % 60;
                    long seconds = (timeRes / 1000) % 86400 % 60;
                    holder.count.setText(day + " " + activity.getResources().getString(R.string._day_item)
                            + " " + hours + " " + activity.getResources().getString(R.string._hours_item)
                            + " " + minute + " " + activity.getResources().getString(R.string._minute_item)
                            + " " + seconds + " " + activity.getResources().getString(R.string._seconds_item));
                } else {
                    holder.count.setText(activity.getResources().getString(R.string._finished));
                    DatabaseCount count = new DatabaseCount(activity);
                    model.setVote(1);
                    model.setPrioritize(0);
                    count.update(model);

                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        } else {
            holder.count.setText(activity.getResources().getString(R.string._finished));
        }


        if (choice == 0) {
            holder.ovel.setBackgroundResource(R.drawable.bg_frame_1);
        } else if (choice == 1) {
            holder.ovel.setBackgroundResource(R.drawable.bg_frame_2);
        } else if (choice == 2) {
            holder.ovel.setBackgroundResource(R.drawable.bg_frame_3);
        }

        if (list.get(choice).getPrioritize() == 1) {
            holder.mVote.setVisibility(View.VISIBLE);
        } else {
            holder.mVote.setVisibility(View.GONE);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, ShowFollowActivity.class);
                intent.putExtra("POSITION", position);
                activity.startActivity(intent);
                activity.overridePendingTransition(R.anim.out_bottom, R.anim.in_bottom);
            }
        });
        holder.ovel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, ShowFollowActivity.class);
                intent.putExtra("POSITION", position);
                activity.startActivity(intent);
                activity.overridePendingTransition(R.anim.out_bottom, R.anim.in_bottom);
            }
        });
        holder.name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, ShowFollowActivity.class);
                intent.putExtra("POSITION", position);
                activity.startActivity(intent);
                activity.overridePendingTransition(R.anim.out_bottom, R.anim.in_bottom);
            }
        });
        holder.time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, ShowFollowActivity.class);
                intent.putExtra("POSITION", position);
                activity.startActivity(intent);
                activity.overridePendingTransition(R.anim.out_bottom, R.anim.in_bottom);
            }
        });
        holder.place.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, ShowFollowActivity.class);
                intent.putExtra("POSITION", position);
                activity.startActivity(intent);
                activity.overridePendingTransition(R.anim.out_bottom, R.anim.in_bottom);
            }
        });


    }

    private void stopService(Context c, int id) {
        AlarmManager manager = (AlarmManager) c.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(c, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(c, id, intent, 0);
        manager.cancel(pendingIntent);
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
        private ImageView mVote;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.mName);
            time = itemView.findViewById(R.id.mTime);
            count = itemView.findViewById(R.id.mCount);
            ovel = itemView.findViewById(R.id.mOvel);
            place = itemView.findViewById(R.id.mPlace);
            mVote = itemView.findViewById(R.id.mVote);


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
