package com.app.magharib.Traveler;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.app.magharib.Host.AddExperienecDetailsActivity;
import com.app.magharib.Host.CreateExperienceActivity;
import com.app.magharib.R;

public class ExperienceActivity extends AppCompatActivity implements View.OnClickListener {

    private Button mBtnRestaurants, mBtnAccommodations, mBtnActivities;
    private ImageView mImageBack;

    private String city_name ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_experience);


        intiViews();


    }

    // عشان اقدر اتحم بالعناصر الموجودة ف ال Activity و افعل الضغطة مثلا على الزر
    private void intiViews() {

        // استقبال الموقع الذي اختاره الترفيلر
        city_name = getIntent().getExtras().getString("city_name");


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
                //الانتقال الي واجهة عرض ال experience و نقل النوع و الموقع الذي اختاره الترفيلر
                Intent i = new Intent(ExperienceActivity.this, PlacesActivity.class);
                i.putExtra("type_experience" ,"restaurants");
                i.putExtra("city_name" ,city_name);
                startActivity(i);
                break;
            case R.id.btn_accommodations:
                //الانتقال الي واجهة عرض ال experience و نقل النوع و الموقع الذي اختاره الترفيلر
                Intent i2 = new Intent(ExperienceActivity.this, PlacesActivity.class);
                i2.putExtra("type_experience" ,"accommodations");
                i2.putExtra("city_name" ,city_name);
                startActivity(i2);
                break;
            case R.id.btn_activities:
                //الانتقال الي واجهة عرض ال experience و نقل النوع و الموقع الذي اختاره الترفيلر
                Intent i3 = new Intent(ExperienceActivity.this, PlacesActivity.class);
                i3.putExtra("type_experience" ,"activities");
                i3.putExtra("city_name" ,city_name);
                startActivity(i3);
                break;
        }
    }
}