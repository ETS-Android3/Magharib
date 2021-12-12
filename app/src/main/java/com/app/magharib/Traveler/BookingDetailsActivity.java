package com.app.magharib.Traveler;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.app.magharib.R;

public class BookingDetailsActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView mTvPhoneExperience , mTvEmailExperience;
    private ImageView mImageBack;

    private String phone_experience , email_experience ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_details);

        intiViews();

    }

    // عشان اقدر اتحم بالعناصر الموجودة ف ال Activity و افعل الضغطة مثلا على الزر
    private void intiViews() {

        // استقبال الايميل و رقم الهاتف الخاص في ال experience
        phone_experience = getIntent().getExtras().getString("phone_experience");
        email_experience = getIntent().getExtras().getString("email_experience");

        mImageBack = findViewById(R.id.image_back);
        mTvPhoneExperience = findViewById(R.id.tv_phone_experience);
        mTvEmailExperience = findViewById(R.id.tv_email_experience);

        // عرض الايميل و رقم الهاتف الخاص في experience
        mTvPhoneExperience.setText(phone_experience);
        mTvEmailExperience.setText(email_experience);

        mImageBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.image_back:
                onBackPressed();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(getApplicationContext(), MainTravelerActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
        finish();
        super.onBackPressed();
    }
}