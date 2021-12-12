package com.app.magharib.Host;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.app.magharib.R;

public class CreateExperienceActivity extends AppCompatActivity implements View.OnClickListener {

    private Button mBtnRestaurants, mBtnAccommodations, mBtnActivities;
    private ImageView mImageBack;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_experience);

        intiViews();

    }

    // عشان اقدر اتحم بالعناصر الموجودة ف ال Activity و افعل الضغطة مثلا على الزر
    private void intiViews() {
        mImageBack = findViewById(R.id.image_back);
        mBtnRestaurants = findViewById(R.id.btn_restaurants);
        mBtnAccommodations = findViewById(R.id.btn_accommodations);
        mBtnActivities = findViewById(R.id.btn_activities);

        mImageBack.setOnClickListener(this);
        mBtnRestaurants.setOnClickListener(this);
        mBtnAccommodations.setOnClickListener(this);
        mBtnActivities.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.image_back:
                onBackPressed();
                break;

            case R.id.btn_restaurants:
                //الانتقال الي واجهة اضافة ال experience و نقل النوع الذي اختاره اليوزر
                Intent i = new Intent(CreateExperienceActivity.this, AddExperienecDetailsActivity.class);
                i.putExtra("type_experience" ,"restaurants");
                startActivity(i);
                break;
            case R.id.btn_accommodations:
                //الانتقال الي واجهة اضافة ال experience و نقل النوع الذي اختاره اليوزر
                Intent i2 = new Intent(CreateExperienceActivity.this, AddExperienecDetailsActivity.class);
                i2.putExtra("type_experience" ,"accommodations");
                startActivity(i2);
                break;
            case R.id.btn_activities:
                //الانتقال الي واجهة اضافة ال experience و نقل النوع الذي اختاره اليوزر
                Intent i3 = new Intent(CreateExperienceActivity.this, AddExperienecDetailsActivity.class);
                i3.putExtra("type_experience" ,"activities");
                startActivity(i3);
                break;
        }
    }
}