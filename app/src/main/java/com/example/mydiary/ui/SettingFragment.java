package com.example.mydiary.ui;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;

import com.example.mydiary.R;
import com.example.mydiary.activity.ChangePasswordActivity;
import com.example.mydiary.activity.CreatePassWordActivity;
import com.example.mydiary.activity.LoginActivity;
import com.example.mydiary.utils.Pef;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;


public class SettingFragment extends Fragment {
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private TextView mFeedback, mLogin, mPollicy, mRate, mKeys, mText;
    private CircleImageView mAvt;
    private TextView mVersion;
    private SwitchCompat mKey;
    private static final int SIGN_IN = 1;
    private GoogleSignInClient mGoogleSignInClient;

    public static SettingFragment newInstance() {
        return new SettingFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_setting, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init(view);
        initSetUp();
        initClick();
    }

    private void initSetUp() {
        Pef.getReference(getActivity());
        mKey.setChecked(Pef.getBoolean("isPassWord"));
        checkVersion();
    }

    private void checkVersion() {
        try {
            PackageInfo pInfo = getActivity().getPackageManager().getPackageInfo(getActivity().getPackageName(), 0);
            String version = pInfo.versionName;
            mVersion.setText("Version: " + version);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void initClick() {
        mFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                feedback();
            }
        });
        mPollicy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pollicy();
            }
        });
        mLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });
        mKey.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                key(isChecked);
            }
        });
        mRate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.example.mydiary"));

                try {
                    startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        mKeys.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Pef.getString("PassWord", "isPassWord").equals("isPassWord")) {
                    Intent intent = new Intent(getContext(), CreatePassWordActivity.class);
                    startActivity(intent);
                    Objects.requireNonNull(getActivity()).overridePendingTransition(R.anim.out_left, R.anim.in_left);
                } else {
                    Intent intent = new Intent(getContext(), ChangePasswordActivity.class);
                    startActivity(intent);
                    Objects.requireNonNull(getActivity()).overridePendingTransition(R.anim.out_left, R.anim.in_left);
                }
            }
        });
    }

    private void key(boolean isChecked) {
        if (!Pef.getBoolean("isPassWord") && Pef.getString("PassWord", "isPassWord").equals("isPassWord")) {
            mKey.setChecked(isChecked);
            Intent intent = new Intent(getContext(), CreatePassWordActivity.class);
            startActivity(intent);
        } else if (!Pef.getBoolean("isPassWord") && !Pef.getString("PassWord", "isPassWord").equals("isPassWord")) {
            mKey.setChecked(isChecked);
            Pef.setBoolean("isPassWord", isChecked);
        } else if (Pef.getBoolean("isPassWord") && !Pef.getString("PassWord", "isPassWord").equals("isPassWord")) {
            mKey.setChecked(isChecked);
            Pef.setBoolean("isPassWord", isChecked);
        }
    }

    private void pollicy() {

    }

    private void login() {
        startActivity(new Intent(getContext(), LoginActivity.class));
        Objects.requireNonNull(getActivity()).overridePendingTransition(R.anim.out_left, R.anim.in_left);
    }

    private void init(View view) {
        mFeedback = view.findViewById(R.id.mFeedback);
        mLogin = view.findViewById(R.id.mLogin);
        mPollicy = view.findViewById(R.id.mPollicy);
        mKey = view.findViewById(R.id.mKey);
        mKeys = view.findViewById(R.id.mKeys);
        mRate = view.findViewById(R.id.mRate);
        mText = view.findViewById(R.id.mText);
        mAvt = view.findViewById(R.id.mAvt);
        mVersion = view.findViewById(R.id.version);
    }

    private void feedback() {
        Intent mailIntent = new Intent(Intent.ACTION_VIEW);
        Uri data = Uri.parse("mailto:?subject=" + "title: " + "&body=" + "content: " + "&to=" + "bnbbinh@mail.com");
        mailIntent.setData(data);
        startActivity(Intent.createChooser(mailIntent, "Send Gmail"));
    }

    private void siginGG() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(getContext(), gso);
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, SIGN_IN);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            Toast.makeText(getContext(), "oke", Toast.LENGTH_SHORT).show();
            // Signed in successfully, show authenticated UI.
            //updateUI(account);
        } catch (ApiException e) {
            Toast.makeText(getContext(), "Toang", Toast.LENGTH_SHORT).show();
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.

            // updateUI(null);
        }

    }

}