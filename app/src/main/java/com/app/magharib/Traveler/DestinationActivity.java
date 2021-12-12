package com.app.magharib.Traveler;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.magharib.Adapter.DestinationAdapter;
import com.app.magharib.Model.Data;
import com.app.magharib.Model.DataLocation;
import com.app.magharib.R;

import java.util.ArrayList;
import java.util.List;

public class DestinationActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView mImageBack;
    private RecyclerView mRvDestination;

    private List<DataLocation> dataLocations;
    private LinearLayoutManager adsManager;
    private DestinationAdapter destinationAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_destination);


        intiViews();

    }

    // عشان اقدر اتحم بالعناصر الموجودة ف ال Activity و افعل الضغطة مثلا على الزر
    private void intiViews() {
        dataLocations = new ArrayList<>();
        mImageBack = findViewById(R.id.image_back);
        mRvDestination = findViewById(R.id.rv_destination);

        mImageBack.setOnClickListener(this);


        // اضافة المواقع المتاحة الي ال arrayList
        dataLocations.add(new DataLocation("Riyadh"));
        dataLocations.add(new DataLocation("Jeddah"));
        dataLocations.add(new DataLocation("Dammam"));
        dataLocations.add(new DataLocation("King Abdullah Economic City"));
        dataLocations.add(new DataLocation("Abha"));
        dataLocations.add(new DataLocation("Yanbu"));
        dataLocations.add(new DataLocation("Taif"));
        dataLocations.add(new DataLocation("Al khobar"));
        dataLocations.add(new DataLocation("AlUla"));


        // عرض اللوكيشن او المواقع المتاحة
        adsManager = new LinearLayoutManager(DestinationActivity.this);
        mRvDestination.setLayoutManager(adsManager);
        destinationAdapter = new DestinationAdapter(DestinationActivity.this, dataLocations);
        mRvDestination.setAdapter(destinationAdapter);

        destinationAdapter.setOnItemClickListener((parent, view, position) -> {
            DataLocation data = (DataLocation) parent;
            // Experience اخذ قيمة الموقع الذي اختاره اليوزر و نقلها الي واجهة اختيار نوع ال
            Intent i = new Intent(DestinationActivity.this, ExperienceActivity.class) ;
            i.putExtra("city_name" , data.getCity_name());
            startActivity(i);
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.image_back:
                onBackPressed();
                break;
        }
    }
}