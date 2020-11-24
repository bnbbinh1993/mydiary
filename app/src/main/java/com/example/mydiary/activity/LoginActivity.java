package com.example.mydiary.activity;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.example.mydiary.R;
public class LoginActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.out_left, R.anim.in_left);
    }


}