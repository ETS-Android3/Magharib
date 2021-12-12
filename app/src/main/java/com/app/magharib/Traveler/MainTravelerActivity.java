package com.app.magharib.Traveler;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.magharib.Adapter.HistoryAdapter;
import com.app.magharib.Adapter.RatingAdapter;
import com.app.magharib.Firebase.FirebaseViewModel;
import com.app.magharib.Host.MainHostActivity;
import com.app.magharib.Model.Order;
import com.app.magharib.Model.Rating;
import com.app.magharib.R;
import com.app.magharib.WayToSignActivity;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class MainTravelerActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView mTvUserName, mTvUserAge, mTvLogOut, mTvHistoryEmpty , mTvRatingTraveler;
    private RecyclerView rv_history , mRvRatingTraveler;
    private ProgressBar mPbHistory , mPbRatingTraveler;
    private Button btn_reservation;

    private List<Order> data_history;
    private List<Rating> data_rating;

    private LinearLayoutManager adsManager;
    private HistoryAdapter historyAdapter;
    private RatingAdapter ratingAdapter;


    private SharedPreferences sharedPreferences;
    private FirebaseViewModel firebaseViewModel;


    private String first_name, last_name, age;

    private FirebaseFirestore firebaseFirestore;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_traveler);

        intiViews();



    }

    // عشان اقدر اتحم بالعناصر الموجودة ف ال Activity و افعل الضغطة مثلا على الزر
    private void intiViews() {

        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseViewModel = new FirebaseViewModel(MainTravelerActivity.this);
        sharedPreferences = getSharedPreferences(FirebaseViewModel.SHARED_PREFERENCE_NAME, MODE_PRIVATE);

        data_history = new ArrayList<>();
        data_rating = new ArrayList<>();

        rv_history = findViewById(R.id.rv_history);
        btn_reservation = findViewById(R.id.btn_reservation);
        mTvUserName = findViewById(R.id.tv_user_name);
        mTvUserAge = findViewById(R.id.tv_user_age);
        mTvLogOut = findViewById(R.id.tv_log_out);
        mPbHistory = findViewById(R.id.pb_history);
        mTvHistoryEmpty = findViewById(R.id.tv_history_empty);
        mPbRatingTraveler = findViewById(R.id.pb_rating_traveler);
        mTvRatingTraveler = findViewById(R.id.tv_rating_traveler);
        mRvRatingTraveler = findViewById(R.id.rv_rating_traveler);


        // استدعاء القيم المخزنة داخل ال sharedPreferences
        first_name = sharedPreferences.getString(FirebaseViewModel.Preference_First_NAME, "");
        last_name = sharedPreferences.getString(FirebaseViewModel.Preference_Last_NAME, "");
        age = sharedPreferences.getString(FirebaseViewModel.Preference_Age, "");

        // عرض القيم المخزنة داخل ال sharedPreferences
        mTvUserName.setText(first_name + " " + last_name);
        mTvUserAge.setText("Age : " + age);

        btn_reservation.setOnClickListener(this);
        mTvLogOut.setOnClickListener(this);


        getMyHistory();
        getMyRating();

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_reservation:
                startActivity(new Intent(MainTravelerActivity.this, DestinationActivity.class));
                break;


            case R.id.tv_log_out:
                // sharedPreferences حذف القيم المخزنة في ال
                Toast.makeText(this, "You have been successfully logged out", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(MainTravelerActivity.this, WayToSignActivity.class));
                finish();
                firebaseViewModel.resetPreference();
                break;
        }
    }


    public void getMyHistory() {
        mPbHistory.setVisibility(View.VISIBLE);
        //عرض ال Order الخاصة باليوزر المسجل دخول به و التي قبلها الهوست
        firebaseFirestore.collection("Order")
                .whereEqualTo("is_enabled", "1")
                .whereEqualTo("user_id", sharedPreferences.getString(FirebaseViewModel.Preference_IDUserOrder, ""))
                .addSnapshotListener((value, e) -> {

                    mPbHistory.setVisibility(View.GONE);
                    rv_history.setVisibility(View.VISIBLE);

                    data_history.clear();
                    data_history.addAll(value.toObjects(Order.class));

                    if (data_history.isEmpty()) {
                        mTvHistoryEmpty.setVisibility(View.VISIBLE);
                    }

                    Log.e("data_history", data_history.size() + "");


                    adsManager = new LinearLayoutManager(MainTravelerActivity.this);
                    rv_history.setLayoutManager(adsManager);
                    historyAdapter = new HistoryAdapter(MainTravelerActivity.this, data_history);
                    rv_history.setAdapter(historyAdapter);
                    historyAdapter.notifyDataSetChanged();


                });
    }

    // عرض التقييمات التي قام بتقيمها الهوست لل ترفيلر على حسب ال id و نوع الحساب
    public void getMyRating() {
        mPbRatingTraveler.setVisibility(View.VISIBLE);
        firebaseFirestore.collection("Rating")
                .whereEqualTo("traveler_id", sharedPreferences.getString(FirebaseViewModel.Preference_IDUserOrder, ""))
                .whereEqualTo("type", "traveler")
                .addSnapshotListener((value, e) -> {

                    mPbRatingTraveler.setVisibility(View.GONE);
                    mRvRatingTraveler.setVisibility(View.VISIBLE);

                    data_rating.addAll(value.toObjects(Rating.class));

                    if (data_rating.isEmpty()) {
                        mTvRatingTraveler.setVisibility(View.VISIBLE);
                    }

                    adsManager = new LinearLayoutManager(MainTravelerActivity.this);
                    mRvRatingTraveler.setLayoutManager(adsManager);
                    ratingAdapter = new RatingAdapter(MainTravelerActivity.this, data_rating);
                    mRvRatingTraveler.setAdapter(ratingAdapter);


                });
    }


}