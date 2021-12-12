package com.app.magharib.Admin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.magharib.Adapter.ExperienceRequestsAdapter;
import com.app.magharib.Adapter.HostRequestsAdapter;
import com.app.magharib.Adapter.ReportsAdapter;
import com.app.magharib.Firebase.FirebaseViewModel;
import com.app.magharib.Model.Experience;
import com.app.magharib.Model.HostTravelerRequests;
import com.app.magharib.Model.Report;
import com.app.magharib.R;
import com.app.magharib.WayToSignActivity;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class MainAdminActivity extends AppCompatActivity implements View.OnClickListener {

    private RecyclerView mRvReports, mRvExperienceRequests, mRvHost;
    private ProgressBar mPbHostTravelerRequests, mPbExperienceRequests, mPbReports;
    private TextView mTvLogOut;

    private List<HostTravelerRequests> host_traveler_requests;
    private List<Experience> experience_requests;

    private List<Report> data_reports;


    private LinearLayoutManager adsManager;
    private ReportsAdapter reportsAdapter;

    private ExperienceRequestsAdapter experienceRequestsAdapter;
    private HostRequestsAdapter hostRequestsAdapter;

    private FirebaseFirestore firebaseFirestore;
    private FirebaseViewModel firebaseViewModel;

    private TextView mTvReportsEmpty;
    private TextView mTvExperienceRequestsEmpty;
    private TextView mTvHostTravelerEmpty;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_admin);

        intiViews();


    }

    // عشان اقدر اتحم بالعناصر الموجودة ف ال Activity و افعل الضغطة مثلا على الزر
    private void intiViews() {

        firebaseViewModel = new FirebaseViewModel(MainAdminActivity.this);
        firebaseFirestore = FirebaseFirestore.getInstance();

        host_traveler_requests = new ArrayList<>();
        data_reports = new ArrayList<>();
        experience_requests = new ArrayList<>();

        mRvReports = findViewById(R.id.rv_reports);
        mRvExperienceRequests = findViewById(R.id.rv_experience_requests);
        mRvHost = findViewById(R.id.rv_host);
        mPbHostTravelerRequests = findViewById(R.id.pb_host_traveler_requests);
        mPbExperienceRequests = findViewById(R.id.pb_experience_requests);
        mRvHost = findViewById(R.id.rv_host);
        mTvLogOut = findViewById(R.id.tv_log_out);
        mPbReports = findViewById(R.id.pb_reports);
        mTvReportsEmpty = findViewById(R.id.tv_reports_empty);
        mTvExperienceRequestsEmpty = findViewById(R.id.tv_experience_requests_empty);
        mTvHostTravelerEmpty = findViewById(R.id.tv_host_traveler_empty);

        mTvLogOut.setOnClickListener(this);


        getAllHostTravelerRequests();
        getAllExperienceRequests();
        getAllReports();


    }


    // عرض طلبات تسجيل جديد سؤاء كان ترفيلر او هوست
    public void getAllHostTravelerRequests() {
        mPbHostTravelerRequests.setVisibility(View.VISIBLE);
        firebaseFirestore.collection(FirebaseViewModel.USERS_COLLECTION)
                .whereEqualTo(FirebaseViewModel.IsEnabled, "0")
                .addSnapshotListener((value, e) -> {

                    mPbHostTravelerRequests.setVisibility(View.GONE);
                    mRvHost.setVisibility(View.VISIBLE);

                    host_traveler_requests.addAll(value.toObjects(HostTravelerRequests.class));


                    if (host_traveler_requests.isEmpty()){
                        mTvHostTravelerEmpty.setVisibility(View.VISIBLE);
                    }

                    adsManager = new LinearLayoutManager(MainAdminActivity.this);
                    mRvHost.setLayoutManager(adsManager);
                    hostRequestsAdapter = new HostRequestsAdapter(MainAdminActivity.this, host_traveler_requests);
                    mRvHost.setAdapter(hostRequestsAdapter);


                });
    }

    // عرض طلبات اضافة ال Experience للهوست
    public void getAllExperienceRequests() {
        mPbExperienceRequests.setVisibility(View.VISIBLE);
        firebaseFirestore.collection("Experience")
                .whereEqualTo("is_enabled", "0")
                .addSnapshotListener((value, e) -> {

                    mPbExperienceRequests.setVisibility(View.GONE);
                    mRvExperienceRequests.setVisibility(View.VISIBLE);

                    experience_requests.addAll(value.toObjects(Experience.class));


                    if (experience_requests.isEmpty()){
                        mTvExperienceRequestsEmpty.setVisibility(View.VISIBLE);
                    }

                    adsManager = new LinearLayoutManager(MainAdminActivity.this);
                    mRvExperienceRequests.setLayoutManager(adsManager);
                    experienceRequestsAdapter = new ExperienceRequestsAdapter(MainAdminActivity.this, experience_requests);
                    mRvExperienceRequests.setAdapter(experienceRequestsAdapter);


                });
    }

    // عرض طلبات الابلاغات للهوست
    public void getAllReports() {
        mPbReports.setVisibility(View.VISIBLE);
        firebaseFirestore.collection("Report")
                .addSnapshotListener((value, e) -> {

                    mPbReports.setVisibility(View.GONE);
                    mRvReports.setVisibility(View.VISIBLE);

                    data_reports.addAll(value.toObjects(Report.class));

                    if (data_reports.isEmpty()){
                        mTvReportsEmpty.setVisibility(View.VISIBLE);
                    }

                    adsManager = new LinearLayoutManager(MainAdminActivity.this);
                    mRvReports.setLayoutManager(adsManager);
                    reportsAdapter = new ReportsAdapter(MainAdminActivity.this, data_reports);
                    mRvReports.setAdapter(reportsAdapter);


                });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_log_out:
                // sharedPreferences حذف القيم المخزنة في ال
                Toast.makeText(this, "You have been successfully logged out", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(MainAdminActivity.this, WayToSignActivity.class));
                finish();
                firebaseViewModel.resetPreference();
                break;
        }
    }
}