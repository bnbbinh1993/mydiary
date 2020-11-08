package com.example.mydiary.adapters;

import android.content.Context;
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
import com.example.mydiary.models.Diary;
import com.example.mydiary.R;
import com.example.mydiary.utils.OnClickItem;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ShowAdapter extends RecyclerView.Adapter<ShowAdapter.ViewHolder> {
    private Context context;
    private List<Diary> list = new ArrayList<>();
    private static OnClickItem onClickItem;

    public ShowAdapter(Context context, List<Diary> list) {
        this.context = context;
        this.list = list;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_show_2, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.setIsRecyclable(false);
        Diary model = list.get(position);
        holder.name.setText(model.getTitle());
        holder.content.setText(model.getContent());
        holder.date.setText(model.getDate());
        if (model.getImage() != null) {
            if (!model.getImage().trim().isEmpty()) {
                String s[] = model.getImage().split("\\s+");
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
                holder.body.setBackgroundColor(context.getResources().getColor(R.color.note));
                holder.top.setBackgroundColor(context.getResources().getColor(R.color.top_note));
                break;
            }
            case 2: {
                holder.body.setBackgroundColor(context.getResources().getColor(R.color.mood));
                holder.top.setBackgroundColor(context.getResources().getColor(R.color.top_mood));
                break;
            }
            case 3: {
                holder.body.setBackgroundColor(context.getResources().getColor(R.color.work));
                holder.top.setBackgroundColor(context.getResources().getColor(R.color.top_work));
                break;
            }
            case 4: {
                holder.body.setBackgroundColor(context.getResources().getColor(R.color.event));
                holder.top.setBackgroundColor(context.getResources().getColor(R.color.top_event));
                break;
            }
            case 5: {
                holder.body.setBackgroundColor(context.getResources().getColor(R.color.shopping));
                holder.top.setBackgroundColor(context.getResources().getColor(R.color.top_shopping));
                break;
            }
            case 6: {
                holder.body.setBackgroundColor(context.getResources().getColor(R.color.travel));
                holder.top.setBackgroundColor(context.getResources().getColor(R.color.top_travel));
                break;
            }
            case 7: {
                holder.body.setBackgroundColor(context.getResources().getColor(R.color.cele));
                holder.top.setBackgroundColor(context.getResources().getColor(R.color.top_cele));
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
        private ImageView image;
        private RelativeLayout body;
        private FrameLayout top;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            content = itemView.findViewById(R.id.content);
            date = itemView.findViewById(R.id.date);
            image = itemView.findViewById(R.id.image);
            top = itemView.findViewById(R.id.top);
            body = itemView.findViewById(R.id.body);
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
