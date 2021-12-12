package com.app.magharib;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.app.magharib.Traveler.DestinationActivity;

public class WayToSignActivity extends AppCompatActivity implements View.OnClickListener {

    private Button mBtnLogin, mBtnSignUp, mBtnGuest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_way_to_sign);

        intiViews();

    }

    private void intiViews() {
        mBtnLogin = findViewById(R.id.btn_login);
        mBtnSignUp = findViewById(R.id.btn_sign_up);
        mBtnGuest = findViewById(R.id.btn_guest);

        mBtnLogin.setOnClickListener(this);
        mBtnSignUp.setOnClickListener(this);
        mBtnGuest.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_login:
                startActivity(new Intent(WayToSignActivity.this, SignInActivity.class));
                break;
            case R.id.btn_sign_up:
                startActivity(new Intent(WayToSignActivity.this, SignUpActivity.class));
                break;
            case R.id.btn_guest:
                startActivity(new Intent(WayToSignActivity.this, DestinationActivity.class));
                break;
        }
    }
}