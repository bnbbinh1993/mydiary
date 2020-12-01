package com.example.mydiary.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.example.mydiary.MainActivity;
import com.example.mydiary.R;
import com.example.mydiary.utils.Pef;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {
    private TextView mRegister;
    private TextView mResetPassword;
    private AppCompatButton mLogin;
    private EditText mEmail;
    private EditText mPassword;
    private FirebaseAuth mAuth;
    private ProgressDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Pef.getReference(this);
        Pef.setFullScreen(LoginActivity.this);
        mAuth = FirebaseAuth.getInstance();
        dialog = new ProgressDialog(this);
        dialog.setTitle("Đăng nhập");
        dialog.setMessage("Vui lòng chờ trong giây lát");
        initUI();
        initClick();
    }

    private void initClick() {
        mRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),RegisterActivity.class));
                overridePendingTransition(R.anim.out_left,R.anim.in_left);
            }
        });
        mLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!isEmailValid(mEmail.getText().toString())) {
                    Toast.makeText(getApplicationContext(), "Please enter correct gmail format", Toast.LENGTH_SHORT).show();
                    mEmail.requestFocus();
                } else if (!(mPassword.getText().length() >= 6)) {
                    Toast.makeText(getApplicationContext(), "Password must be at least 6 characters", Toast.LENGTH_SHORT).show();
                    mPassword.requestFocus();
                }else {
                    login(mEmail.getText().toString(), mPassword.getText().toString());
                }
            }
        });

    }

    private void initUI() {
        mRegister = findViewById(R.id.mRegister);
        mResetPassword = findViewById(R.id.mResetPassword);
        mLogin = findViewById(R.id.mLogin);
        mEmail = findViewById(R.id.mEmail);
        mPassword = findViewById(R.id.mPassword);
    }
    private boolean isEmailValid(CharSequence email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private void login(String email, String password) {
        dialog.show();
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                            finish();
                            overridePendingTransition(R.anim.out_left, R.anim.in_left);
                            dialog.dismiss();
                        } else {
                            dialog.dismiss();
                            Toast.makeText(LoginActivity.this, "Login failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.out_left, R.anim.in_left);
    }


}