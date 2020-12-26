package com.example.mydiary.adapters;

import android.content.Context;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.mydiary.R;
import com.example.mydiary.models.Diary;
import com.example.mydiary.utils.OnClickItem;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ShowAdapter extends RecyclerView.Adapter<ShowAdapter.ViewHolder> {
    private Context context;
    private List<Diary> list = new ArrayList<>();
    private static OnClickItem onClickItem;
    private int choice;
    private SimpleDateFormat f = new SimpleDateFormat("hh:mm - dd.MM.yyyy");
    private SimpleDateFormat fmoth = new SimpleDateFormat("MMM");
    private SimpleDateFormat fday = new SimpleDateFormat("EEE");
    private Calendar calendar = Calendar.getInstance();
    private Date date = new Date();

    public ShowAdapter(Context context, List<Diary> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getItemViewType(int position) {
        choice = position;
        return choice;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_show_2, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Diary model = list.get(choice);
        holder.name.setText(model.getTitle());
        holder.content.setText(model.getContent());
        String d[] = model.getDate().split(" - ");
        holder.date.setText(d[0]);

        try {
            calendar.setTimeInMillis(f.parse(model.getDate()).getTime());

            date.setTime(f.parse(model.getDate()).getTime());
            holder.txtDay.setText(String.valueOf(calendar.get(Calendar.DAY_OF_MONTH)));
            holder.txtThu.setText(fday.format(date));
            holder.txtMonth.setText(fmoth.format(date));
            holder.txtYear.setText(String.valueOf(calendar.get(Calendar.YEAR)));
        } catch (ParseException e) {
            e.printStackTrace();
        }


        if (model.getImage() != null) {
            if (!model.getImage().trim().isEmpty()) {
                String s[] = model.getImage().split("<->");
                File file = new File(s[0]);
                Glide.with(context)
                        .load(file)
                        .centerCrop()
                        .into(holder.image);
            } else {
                holder.image.setVisibility(View.GONE);
            }
        } else {
            holder.image.setVisibility(View.GONE);
        }

        switch (model.getVote()) {
            case 1: {
                holder.body.setBackgroundResource(R.drawable.bg_gradent_1);
                break;
            }
            case 2: {
                holder.body.setBackgroundResource(R.drawable.bg_gradent_2);

                break;
            }
            case 3: {
                holder.body.setBackgroundResource(R.drawable.bg_gradent_3);

                break;
            }
            case 4: {
                holder.body.setBackgroundResource(R.drawable.bg_gradent_4);

                break;
            }
            case 5: {
                holder.body.setBackgroundResource(R.drawable.bg_gradent_5);

                break;
            }
            case 6: {
                holder.body.setBackgroundResource(R.drawable.bg_gradent_6);

                break;
            }
            case 7: {
                holder.body.setBackgroundResource(R.drawable.bg_gradent_7);
                break;
            }
            case 8: {
                holder.body.setBackgroundResource(R.drawable.bg_gradent_8);

                break;
            }
            case 9: {
                holder.body.setBackgroundResource(R.drawable.bg_gradent_9);

                break;
            }
            case 10: {
                holder.body.setBackgroundResource(R.drawable.bg_gradent_10);

                break;
            }
            case 11: {
                holder.body.setBackgroundResource(R.drawable.bg_gradent_11);

                break;
            }
            case 12: {
                holder.body.setBackgroundResource(R.drawable.bg_gradent_12);

                break;
            }
            default: {
                holder.body.setBackgroundResource(R.drawable.bg_gradent_1);

                break;
            }
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
        private TextView content;
        private TextView date;
        private TextView txtDay;
        private TextView txtThu;
        private TextView txtMonth;
        private TextView txtYear;
        private ImageView image;
        private LinearLayout body;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            content = itemView.findViewById(R.id.content);
            date = itemView.findViewById(R.id.date);
            image = itemView.findViewById(R.id.image);
            body = itemView.findViewById(R.id.body);
            txtDay = itemView.findViewById(R.id.txtDay);
            txtThu = itemView.findViewById(R.id.txtThu);
            txtMonth = itemView.findViewById(R.id.txtMonth);
            txtYear = itemView.findViewById(R.id.txtYear);


            itemView.setOnClickListener(this::onClick);
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
