package com.example.mydiary.ui;

import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.mydiary.R;
import com.example.mydiary.activity.CountDownActivity;
import com.example.mydiary.activity.EventActivity;
import com.example.mydiary.activity.MoodActivity;
import com.example.mydiary.activity.NoteActivity;
import com.example.mydiary.models.Create;
import java.util.ArrayList;


public class CreateFragment extends Fragment {

    private ArrayList<Create> list;
    private ArrayList<Create> list2;
    private GridView mGridView;
    private GridView mGridView2;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_create, container, false);
        init(view);
        return view;
    }

    private void init(View v) {
        mGridView = v.findViewById(R.id.mGridView);
        mGridView2 = v.findViewById(R.id.mGridView2);
        list = new ArrayList<>();
        list2 = new ArrayList<>();
        addList();
        CustomAdapter customAdapter = new CustomAdapter();
        CustomAdapter2 customAdapter2 = new CustomAdapter2();
        mGridView.setAdapter(customAdapter);
        mGridView2.setAdapter(customAdapter2);
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    startActivity(new Intent(getContext(), NoteActivity.class));
                } else if (position==1){
                    startActivity(new Intent(getContext(), MoodActivity.class));
                }else if (position==2){
                   // startActivity(new Intent(getContext(), MoodActivity.class));
                }else if (position==3){
                    startActivity(new Intent(getContext(), EventActivity.class));
                }

            }
        });
        mGridView2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    startActivity(new Intent(getContext(), CountDownActivity.class));
                }
            }
        });
    }

    private void addList() {
        list.add(new Create(getResources().getString(R.string._note),R.drawable.ic_notes,R.color.note));
        list.add(new Create(getResources().getString(R.string._mood),R.drawable.ic_angry,R.color.mood));
        list.add(new Create(getResources().getString(R.string._work),R.drawable.ic_work,R.color.work));
        list.add(new Create(getResources().getString(R.string._event),R.drawable.ic_confetti,R.color.event));
        list.add(new Create(getResources().getString(R.string._shopping),R.drawable.ic_shopping_bag,R.color.shopping));
        list.add(new Create(getResources().getString(R.string._travel),R.drawable.ic_travel,R.color.travel));
        list.add(new Create(getResources().getString(R.string._celebration),R.drawable.ic_fireworks,R.color.cele));
        list2.add(new Create(getResources().getString(R.string._count_time),R.drawable.ic_countdown,R.color.countdown));

    }
    private class CustomAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            View view1 = getLayoutInflater().inflate(R.layout.item_create, null);

            ImageView image = view1.findViewById(R.id.mIcon);
            TextView name = view1.findViewById(R.id.mName);

            image.setImageResource(list.get(i).getIcon());
            image.setBackgroundResource(list.get(i).getColor());
            name.setText(list.get(i).getName());
            return view1;

        }
    }
    private class CustomAdapter2 extends BaseAdapter {
        @Override
        public int getCount() {
            return list2.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            View view2 = getLayoutInflater().inflate(R.layout.item_create, null);

            ImageView image = view2.findViewById(R.id.mIcon);
            TextView name = view2.findViewById(R.id.mName);

            image.setImageResource(list2.get(i).getIcon());
            image.setBackgroundResource(list2.get(i).getColor());
            name.setText(list2.get(i).getName());
            return view2;

        }
    }

}