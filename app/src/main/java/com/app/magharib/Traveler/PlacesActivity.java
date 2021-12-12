package com.app.magharib.Traveler;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.app.magharib.Adapter.PlacesAdapter;
import com.app.magharib.Model.Experience;
import com.app.magharib.R;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.ArrayList;
import java.util.List;

public class PlacesActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView mTvHistoryEmpty;
    private ImageView mImageBack;
    private RecyclerView mRvPlaces;
    private ProgressBar mPbPlaces;

    private List<Experience> list_places;

    private LinearLayoutManager adsManager;
    private PlacesAdapter placesAdapter;

    private FirebaseFirestore firebaseFirestore;


    private String type_experience, city_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_places);

        intiViews();

    }

    // عشان اقدر اتحم بالعناصر الموجودة ف ال Activity و افعل الضغطة مثلا على الزر
    private void intiViews() {

        // استقبال قيمة النوع و الموقع الذي تم اختياره من الترفيلر
        type_experience = getIntent().getExtras().getString("type_experience");
        city_name = getIntent().getExtras().getString("city_name");

        firebaseFirestore = FirebaseFirestore.getInstance();
        list_places = new ArrayList<>();

        mImageBack = findViewById(R.id.image_back);
        mRvPlaces = findViewById(R.id.rv_places);
        mPbPlaces = findViewById(R.id.pb_places);
        mTvHistoryEmpty = findViewById(R.id.tv_history_empty);


        mImageBack.setOnClickListener(this);


        getAllExperience();

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.image_back:
                onBackPressed();
                break;
        }
    }


    // عرض كل Experience الموجودة في الفايربيس على حسب النوع و الموقع الذي تم اختياره من الترفيلر و فعال من طرف الادمن
    public void getAllExperience() {
        mPbPlaces.setVisibility(View.VISIBLE);
        firebaseFirestore.collection("Experience")
                .whereEqualTo("is_enabled", "1")
                .whereEqualTo("type_experience", type_experience)
                .whereEqualTo("location_experience", city_name)
                .addSnapshotListener((value, e) -> {

                    mPbPlaces.setVisibility(View.GONE);


                    list_places.addAll(value.toObjects(Experience.class));

                    if (list_places.isEmpty()){
                        mTvHistoryEmpty.setVisibility(View.VISIBLE);
                        mRvPlaces.setVisibility(View.GONE);
                    }else {
                        mRvPlaces.setVisibility(View.VISIBLE);
                        mTvHistoryEmpty.setVisibility(View.GONE);
                    }

                    // عرض ال كل Experience
                    adsManager = new LinearLayoutManager(PlacesActivity.this);
                    mRvPlaces.setLayoutManager(adsManager);
                    placesAdapter = new PlacesAdapter(PlacesActivity.this, list_places);
                    mRvPlaces.setAdapter(placesAdapter);
                    placesAdapter.setOnItemClickListener((parent, view, position) -> {

                        Experience data = (Experience) parent;
                        // الانتقال الي واجهة التفاصيل و نقل الاوبجت الخاص ب ال Experience
                        Intent i = new Intent(PlacesActivity.this, DetailsPlacesActivity.class);
                        i.putExtra("data_experience", data);
                        startActivity(i);
                    });

                });
    }


}