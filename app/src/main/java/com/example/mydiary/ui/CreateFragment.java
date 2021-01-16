package com.example.mydiary.ui;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.mydiary.R;
import com.example.mydiary.activity.CreateCountDownActivity;
import com.example.mydiary.activity.CreateNoteActivity;
import com.example.mydiary.models.App;
import com.example.mydiary.models.Create;
import com.example.mydiary.utils.ImageFilePath;
import com.example.mydiary.utils.Pef;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;

import java.io.File;
import java.util.ArrayList;
import java.util.Objects;

public class CreateFragment extends Fragment {
    private static final int RESULT_LOAD_IMAGE = 1;
    private static final int SELECT_PICTURES = 1;
    private ArrayList<Create> list;
    private ArrayList<Create> list2;
    private ArrayList<App> list3;
    private String path;
    private ImageView mAvtar;
    private AdView mAdView;
    private FrameLayout layout;


    public static CreateFragment newInstance() {
        return new CreateFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create, container, false);
        init(view);
        return view;
    }

    private void init(View v) {
        GridView mGridView = v.findViewById(R.id.mGridView);
        GridView mGridView3 = v.findViewById(R.id.mGridView3);
        mAvtar = v.findViewById(R.id.mAvtar);
        layout = v.findViewById(R.id.adView);



        Pef.getReference(getActivity());
        String res = Pef.getString("AVATAR", "ERROR");
        if (!res.equals("ERROR")) {
            File file = new File(res);
            Glide.with(Objects.requireNonNull(getContext()))
                    .load(file)
                    .centerCrop()
                    .error(R.drawable.test5)
                    .into(mAvtar);
        }
        mAvtar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getImageFromAlbum();
            }
        });

        list = new ArrayList<>();
        list2 = new ArrayList<>();
        list3 = new ArrayList<>();
        addList();
        CustomAdapter customAdapter = new CustomAdapter();
        CustomAdapter2 customAdapter2 = new CustomAdapter2();
        CustomAdapter3 customAdapter3 = new CustomAdapter3();
        mGridView.setAdapter(customAdapter);
        mGridView3.setAdapter(customAdapter3);
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    startActivity(new Intent(getContext(), CreateNoteActivity.class));
                    Objects.requireNonNull(getActivity()).overridePendingTransition(R.anim.out_bottom, R.anim.in_bottom);
                } else if (position == 1) {
                    startActivity(new Intent(getContext(), CreateCountDownActivity.class));
                    Objects.requireNonNull(getActivity()).overridePendingTransition(R.anim.out_bottom, R.anim.in_bottom);
                } else if (position == 2) {
                    // startActivity(new Intent(getContext(), MoodActivity.class));
                } else if (position == 3) {
                    //startActivity(new Intent(getContext(), EventActivity.class));
                }

            }
        });
        mGridView3.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=vn.bnb.binh.foreveralone"));

                    try {
                        startActivity(intent);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (position == 1) {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=vn.truatvl.fancytext"));

                    try {
                        startActivity(intent);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
       loadAd();
    }

    private void addList() {
        list.add(new Create(getResources().getString(R.string._diary), R.drawable.ic_notes, R.color.note));
//        list.add(new Create(getResources().getString(R.string._mood),R.drawable.ic_angry,R.color.mood));
//        list.add(new Create(getResources().getString(R.string._work),R.drawable.ic_work,R.color.work));
//        list.add(new Create(getResources().getString(R.string._event),R.drawable.ic_confetti,R.color.event));
//        list.add(new Create(getResources().getString(R.string._shopping),R.drawable.ic_shopping_bag,R.color.shopping));
//        list.add(new Create(getResources().getString(R.string._travel),R.drawable.ic_travel,R.color.travel));
//        list.add(new Create(getResources().getString(R.string._celebration),R.drawable.ic_fireworks,R.color.cele));
        list.add(new Create(getResources().getString(R.string._follow), R.drawable.ic_flow_red, R.color.countdown));
        //list2.add(new Create(getResources().getString(R.string._emotion),R.drawable.ic_emoticons,R.color.event));
        list3.add(new App(getResources().getString(R.string._alone), getResources().getString(R.string._entertaiment), R.drawable.alone, R.color.note));
        list3.add(new App(getResources().getString(R.string._fancy_text), getResources().getString(R.string._tools), R.drawable.fancytext, R.color.work));


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
            @SuppressLint({"ViewHolder", "InflateParams"}) View view1 = getLayoutInflater().inflate(R.layout.item_create, null);

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
            @SuppressLint({"ViewHolder", "InflateParams"}) View view2 = getLayoutInflater().inflate(R.layout.item_create, null);

            ImageView image = view2.findViewById(R.id.mIcon);
            TextView name = view2.findViewById(R.id.mName);

            image.setImageResource(list2.get(i).getIcon());
            image.setBackgroundResource(list2.get(i).getColor());
            name.setText(list2.get(i).getName());
            return view2;

        }
    }

    private class CustomAdapter3 extends BaseAdapter {
        @Override
        public int getCount() {
            return list3.size();
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
            @SuppressLint({"ViewHolder", "InflateParams"}) View view2 = getLayoutInflater().inflate(R.layout.item_app, null);

            ImageView image = view2.findViewById(R.id.mIcon);
            TextView name = view2.findViewById(R.id.mName);
            TextView category = view2.findViewById(R.id.mCategory);

            image.setImageResource(list3.get(i).getIcon());
            image.setBackgroundResource(list3.get(i).getColor());
            name.setText(list3.get(i).getName());
            category.setText(list3.get(i).getCategory());
            return view2;

        }
    }

    private void getImageFromAlbum() {
        try {
            Intent i = new Intent(Intent.ACTION_PICK,
                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(i, RESULT_LOAD_IMAGE);
        } catch (Exception exp) {
            Log.i("Error", exp.toString());
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SELECT_PICTURES) {
            if (resultCode == Activity.RESULT_OK) {
                if (data.getClipData() != null) {
                    int count = data.getClipData().getItemCount();
                    for (int i = 0; i < count; i++) {
                        Uri imageUri = data.getClipData().getItemAt(i).getUri();
                        path = ImageFilePath.getPath(getContext(), imageUri);
                    }
                } else if (data.getData() != null) {
                    Uri imageUri = data.getData();
                    path = ImageFilePath.getPath(getContext(), imageUri);
                }

                try {
                    Pef.setString("AVATAR", path);
                    File file = new File(path);
                    Glide.with(Objects.requireNonNull(getContext()))
                            .load(file)
                            .centerCrop()
                            .error(R.drawable.test5)
                            .into(mAvtar);
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }

            }
        }


    }

    private void loadAd(){
//        mAdView = new AdView(getContext());
//        mAdView.setAdUnitId(getString(R.string.id_test));
//        layout.addView(mAdView);
//        MobileAds.initialize(getContext(), new OnInitializationCompleteListener() {
//            @Override
//            public void onInitializationComplete(InitializationStatus initializationStatus) {
//
//            }
//        });
//        AdRequest adRequest = new AdRequest.Builder()
//                .build();
//        AdSize adSize = getAdSize();
//        mAdView.setAdSize(adSize);
//        mAdView.loadAd(adRequest);
    }
    private AdSize getAdSize() {
        Display display = Objects.requireNonNull(getActivity()).getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);
        float widthPixels = outMetrics.widthPixels;
        float density = outMetrics.density;
        int adWidth = (int) (widthPixels / density);
        return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(getContext(), adWidth);
    }

    @Override
    public void onPause() {
        if (mAdView != null) {
            mAdView.pause();
        }
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mAdView != null) {
            mAdView.resume();
        }
    }

    @Override
    public void onDestroy() {
        if (mAdView != null) {
            mAdView.destroy();
        }
        super.onDestroy();
    }
}