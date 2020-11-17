package com.example.mydiary.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mydiary.R;
import com.example.mydiary.activity.ShowFollowActivity;
import com.example.mydiary.adapters.CountAdapter;
import com.example.mydiary.database.DatabaseCount;
import com.example.mydiary.models.Count;
import com.example.mydiary.utils.OnClickItem;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.Collections;

public class FollowFragment extends Fragment {
    private DatabaseCount helper;
    private ArrayList<Count> list;
    private ArrayList<Count> listRes;
    private RecyclerView mRecyclerview;
    private CountAdapter adapter;
    private TabLayout tablayout;
    private TabItem tab1;
    private TabItem tab2;
    private TabItem tab3;
    private int filter = 0;
    private LinearLayout no_item;
    private FloatingActionButton fab;
    private CountDownTimer count;

    public static FollowFragment newInstance() {
        FollowFragment fragment = new FollowFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_follow, container, false);
        init(view);
        setUp();
        Filter();
        fabRecyclerview();
        initClick();
        countDown();
        return view;
    }

    private void initClick() {
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delete();
            }
        });
    }

    private void delete() {

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(getResources().getString(R.string._delete));
        builder.setMessage(getResources().getString(R.string._messenger_delete_all));
        builder.setPositiveButton(getResources().getString(R.string._yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                helper.deleteAll(list);
                listRes.clear();
                listRes = helper.getData();
                updateFilter();
                dialog.dismiss();
            }
        });
        builder.setNegativeButton(getResources().getString(R.string._no), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();

    }

    private void init(View v) {
        mRecyclerview = v.findViewById(R.id.mRecyclerview);
        tablayout = v.findViewById(R.id.tablayout);
        no_item = v.findViewById(R.id.no_item);
        tab1 = v.findViewById(R.id.tab1);
        tab2 = v.findViewById(R.id.tab2);
        tab3 = v.findViewById(R.id.tab3);
        fab = v.findViewById(R.id.fab);
    }

    private void setUp() {
        helper = new DatabaseCount(getContext());
        list = new ArrayList<>();
        listRes = new ArrayList<>();
        list.clear();
        listRes.clear();
        list = helper.getData();
        listRes = helper.getData();
        Collections.sort(list);
        Collections.sort(listRes);
        adapter = new CountAdapter(getActivity(),list);
        mRecyclerview.setHasFixedSize(true);
        mRecyclerview.setLayoutManager(new GridLayoutManager(getContext(), 1));
        mRecyclerview.setAdapter(adapter);
    }

    private void fabRecyclerview() {
        mRecyclerview.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0 && fab.getVisibility() == View.VISIBLE) {
                    fab.hide();
                } else if (dy < 0 && fab.getVisibility() != View.VISIBLE) {
                    fab.show();
                }
            }
        });
    }

    private void Filter() {
        tablayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == 0) {
                    filter = 0;
                } else if (tab.getPosition() == 1) {
                    filter = 1;
                } else if (tab.getPosition() == 2) {
                    filter = 2;
                } else if (tab.getPosition() == 3) {
                    filter = 3;
                } else if (tab.getPosition() == 4) {
                    filter = 4;
                }
                updateFilter();

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void updateFilter() {
        list.clear();
        ArrayList<Count> test = new ArrayList<>();
        if (filter == 0) {
            test = listRes;
        } else {
            for (Count count : listRes) {
                if (count.getVote() == (filter - 1)) {
                    test.add(count);
                }
            }
        }

        updateData(test);
    }


    public void updateData(ArrayList<Count> viewModels) {
        list.clear();
        list.addAll(viewModels);
        Collections.sort(list);
        Log.d("TEST", "updateFilter: " + list.size());
        updateUI();
    }

    private void updateUI() {
        if (list.size() <= 0) {
            no_item.setVisibility(View.VISIBLE);
            mRecyclerview.setVisibility(View.GONE);
        } else {
            no_item.setVisibility(View.GONE);
            mRecyclerview.setVisibility(View.VISIBLE);
        }
        adapter.notifyDataSetChanged();
    }

    private void countDown() {
        count = new CountDownTimer(1800000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFinish() {
                countDown();
            }
        };
    }

    @Override
    public void onPause() {
        super.onPause();
        if (count != null) {
            count.cancel();
        }
    }

    @Override
    public void onResume() {
        setUp();
        updateUI();
        super.onResume();
        if (count != null) {
            count.start();
        }

    }

    @Override
    public void onStart() {
        super.onStart();
        if (count != null) {
            count.start();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (count != null) {
            count.cancel();
        }
    }
}