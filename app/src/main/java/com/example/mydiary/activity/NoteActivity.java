package com.example.mydiary.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import android.app.Activity;
import android.content.ClipData;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.mydiary.R;
import com.example.mydiary.database.MyDatabaseHelper;
import com.example.mydiary.models.Show;
import com.example.mydiary.utils.ImageFilePath;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class NoteActivity extends AppCompatActivity {
    private static final int RESULT_LOAD_IMAGE = 1;
    private int vote;
    private ImageButton mBack;
    private ImageButton mSave;
    private ImageButton mTrue;
    private ImageButton mCancel;
    private ImageButton btnColor;
    private ImageButton btnImage;
    private TextView mDate;
    private EditText mTitle;
    private EditText mContent;
    private RadioGroup group;
    private RadioButton rd1;
    private RadioButton rd2;
    private RadioButton rd3;
    private RadioButton rd4;
    private RadioButton rd5;
    private RadioButton rd6;
    private MyDatabaseHelper helper;
    private BottomSheetBehavior behavior;
    private CoordinatorLayout background;
    private Spinner spinner;
    private String employees[] = {"Vui", "Buồn", "Hạnh phúc"};
    private String title;
    private String path;
    private static final int SELECT_PICTURES = 1;
    private String imageEncoded;
    private List<String> resPath;
    private GridView gridView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);
        if (Build.VERSION.SDK_INT >= 19 && Build.VERSION.SDK_INT < 21) {
            setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, true);
        }
        if (Build.VERSION.SDK_INT >= 19) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
        if (Build.VERSION.SDK_INT >= 21) {
            setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }


        init();
        initSetUp();
        setOnclick();
        RDGroup();
        setSpinner();
        setGridView();

    }

    private void setGridView() {
        CustomAdapter adapter = new CustomAdapter();
        gridView.setAdapter(adapter);
    }

    private class CustomAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return resPath.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = getLayoutInflater().inflate(R.layout.item_image,null);
            ImageView image = view.findViewById(R.id.image);
            File file = new File(resPath.get(position));
            Glide.with(NoteActivity.this)
                    .load(file)
                    .centerCrop()
                    .error(R.drawable.ic_launcher_background)
                    .into(image);
            return view;
        }
    }

    private void setSpinner() {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item,
                employees);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                title = employees[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void initSetUp() {
        helper = new MyDatabaseHelper(this);
        View bottomSheet = findViewById(R.id.viewColor);
        behavior = BottomSheetBehavior.from(bottomSheet);
        behavior.setPeekHeight(0);
        behavior.setHideable(true);
        resPath = new ArrayList<>();

    }

    private void init() {
        mBack = findViewById(R.id.mBack);
        mSave = findViewById(R.id.save);
        mDate = findViewById(R.id.date);
        mTitle = findViewById(R.id.title);
        mContent = findViewById(R.id.body);
        btnColor = findViewById(R.id.btnColor);
        mTrue = findViewById(R.id.mTrue);
        mCancel = findViewById(R.id.mCancel);
        background = findViewById(R.id.background);
        group = findViewById(R.id.group);
        rd1 = findViewById(R.id.rd1);
        rd2 = findViewById(R.id.rd2);
        rd3 = findViewById(R.id.rd3);
        rd4 = findViewById(R.id.rd4);
        rd5 = findViewById(R.id.rd5);
        rd6 = findViewById(R.id.rd6);
        spinner = findViewById(R.id.spinner);
        btnImage = findViewById(R.id.btnImage);
        gridView = findViewById(R.id.gridview);
    }

    private void setOnclick() {
        btnImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getMultipleImage();
            }
        });
        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save();
                finish();
            }
        });
        btnColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            }
        });
        mCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            }
        });
        mTrue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            }
        });

    }

    private void RDGroup() {
        group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rd1: {
                        background.setBackgroundColor(getResources().getColor(R.color.note));
                        vote = 1;
                        break;
                    }
                    case R.id.rd2: {
                        background.setBackgroundColor(getResources().getColor(R.color.event));
                        vote = 2;
                        break;
                    }
                    case R.id.rd3: {
                        background.setBackgroundColor(getResources().getColor(R.color.mood));
                        vote = 3;
                        break;
                    }
                    case R.id.rd4: {
                        background.setBackgroundColor(getResources().getColor(R.color.work));
                        vote = 4;
                        break;
                    }
                    case R.id.rd5: {
                        background.setBackgroundColor(getResources().getColor(R.color.travel));
                        vote = 5;
                        break;
                    }
                    case R.id.rd6: {
                        background.setBackgroundColor(getResources().getColor(R.color.shopping));
                        vote = 6;
                        break;
                    }
                }
            }
        });
    }

    private void save() {
        String title = mTitle.getText().toString();
        String content = mContent.getText().toString();
        String date = mDate.getText().toString();
        int filter = 1;
        String image = "";
        for (String s : resPath){
            image  += " "+s;
        }
        Show show = new Show();
        show.setTitle(title);
        show.setContent(content);
        show.setDate(date);
        show.setFilter(filter);
        show.setImage(image.trim());
        show.setVote(vote);
        helper.adđ(show);
    }

    public static void setWindowFlag(Activity activity, final int bits, boolean on) {
        Window win = activity.getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }

    private void getImageFromAlbum() {
        try {
            startActivityForResult(new Intent().setAction(Intent.ACTION_GET_CONTENT).setType("image/*"), RESULT_LOAD_IMAGE);
        } catch (Exception exp) {
            Log.i("Error", exp.toString());
        }
    }

    private void getMultipleImage() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_PICTURES);
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
                        path = ImageFilePath.getPath(NoteActivity.this, imageUri);
                        resPath.add(path);
                    }
                } else if (data.getData() != null) {
                    String imagePath = data.getData().getPath();
                    Uri imageUri = data.getData();
                    path = ImageFilePath.getPath(NoteActivity.this, imageUri);
                    resPath.add(path);
                }
                setGridView();
            }
        }
        Log.d("SIZE", "onActivityResult: " + resPath.size());
    }

    @Override
    protected void onResume() {
        super.onResume();

    }
}

