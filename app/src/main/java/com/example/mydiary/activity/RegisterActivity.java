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
import com.google.firebase.auth.FirebaseUser;

public class RegisterActivity extends AppCompatActivity {
    private TextView mLogin;
    private AppCompatButton mRegister;
    private EditText mEmail;
    private EditText mPassword;
    private EditText mPassword2;
    private EditText mYourName;
    private FirebaseAuth mAuth;
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Pef.getReference(this);
        Pef.setFullScreen(RegisterActivity.this);
        mAuth = FirebaseAuth.getInstance();
        dialog = new ProgressDialog(this);
        dialog.setTitle("Đăng ký");
        dialog.setMessage("Vui lòng chờ trong giây lát");
        initUI();
        initClick();
    }

    private void initClick() {
        mLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.out_left, R.anim.in_left);
            }
        });
        mRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
            }
        });
    }

    private boolean isEmailValid(CharSequence email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private void register() {
        if (mYourName.getText().toString().isEmpty()) {
            Toast.makeText(this, "Please enter your name", Toast.LENGTH_SHORT).show();
            mYourName.requestFocus();
        } else if (!isEmailValid(mEmail.getText().toString())) {
            Toast.makeText(this, "Please enter correct gmail format", Toast.LENGTH_SHORT).show();
            mEmail.requestFocus();
        } else if (!(mPassword.getText().length() >= 6)) {
            Toast.makeText(this, "Password must be at least 6 characters", Toast.LENGTH_SHORT).show();
            mPassword.requestFocus();
        } else if (!mPassword.getText().toString().equals(mPassword2.getText().toString())) {
            Toast.makeText(this, "Password incorrect", Toast.LENGTH_SHORT).show();
            mPassword2.requestFocus();
        } else {
            dialog.show();
            mAuth.createUserWithEmailAndPassword(mEmail.getText().toString(), mPassword.getText().toString())
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                FirebaseUser user = mAuth.getCurrentUser();
                                login(mEmail.getText().toString(), mPassword.getText().toString());
                            } else {
                                dialog.dismiss();
                                Toast.makeText(RegisterActivity.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }

    }

    private void login(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                            finish();
                            overridePendingTransition(R.anim.out_left, R.anim.in_left);
                            dialog.dismiss();
                        } else {
                            dialog.dismiss();
                            Toast.makeText(RegisterActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void initUI() {
        mRegister = findViewById(R.id.mRegister);
        mPassword2 = findViewById(R.id.mPassword2);
        mLogin = findViewById(R.id.mLogin);
        mEmail = findViewById(R.id.mEmail);
        mPassword = findViewById(R.id.mPassword);
        mYourName = findViewById(R.id.mYourName);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.out_left, R.anim.in_left);
    }
}