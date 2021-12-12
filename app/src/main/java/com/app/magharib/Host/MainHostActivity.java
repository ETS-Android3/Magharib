package com.app.magharib.Host;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.magharib.Adapter.BookingRequestAdapter;
import com.app.magharib.Adapter.ExperiencesAdapter;
import com.app.magharib.Adapter.RatingAdapter;
import com.app.magharib.Firebase.FirebaseViewModel;
import com.app.magharib.Model.Experience;
import com.app.magharib.Model.Order;
import com.app.magharib.Model.Rating;
import com.app.magharib.R;
import com.app.magharib.WayToSignActivity;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class MainHostActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView mTvUserName, mTvUserAge, mTvLogOut, mTvExperienceEmpty, mTvRatingHost, mTvHistoryEmpty;
    private RecyclerView mRvExperiences, mRvRatingHost, mRvHistory;
    private ProgressBar mPbExperience, mPbRatingHost, mPbHistory;
    private Button mBtnBookingRequests, mBtnCreateExperience;

    private LinearLayoutManager adsManager;

    private ExperiencesAdapter experiencesAdapter;
    private BookingRequestAdapter bookingRequestAdapter;
    private RatingAdapter ratingAdapter;

    private List<Experience> data_experiences;
    private List<Order> all_order;
    private List<Rating> data_rating;

    private SharedPreferences sharedPreferences;
    private FirebaseViewModel firebaseViewModel;
    private FirebaseFirestore firebaseFirestore;

    private String first_name, last_name, age;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_host);

        intiViews();


    }

    // عشان اقدر اتحم بالعناصر الموجودة ف ال Activity و افعل الضغطة مثلا على الزر
    private void intiViews() {

        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseViewModel = new FirebaseViewModel(MainHostActivity.this);
        sharedPreferences = getSharedPreferences(FirebaseViewModel.SHARED_PREFERENCE_NAME, MODE_PRIVATE);

        data_experiences = new ArrayList<>();
        data_rating = new ArrayList<>();
        all_order = new ArrayList<>();

        mBtnBookingRequests = findViewById(R.id.btn_booking_requests);
        mBtnCreateExperience = findViewById(R.id.btn_create_experience);
        mRvExperiences = findViewById(R.id.rv_experiences);
        mTvUserName = findViewById(R.id.tv_user_name);
        mTvUserAge = findViewById(R.id.tv_user_age);
        mTvLogOut = findViewById(R.id.tv_log_out);
        mPbExperience = findViewById(R.id.pb_experience);
        mTvExperienceEmpty = findViewById(R.id.tv_experience_empty);
        mRvRatingHost = findViewById(R.id.rv_rating_host);
        mPbRatingHost = findViewById(R.id.pb_rating_host);
        mTvRatingHost = findViewById(R.id.tv_rating_host);
        mPbHistory = findViewById(R.id.pb_history);
        mTvHistoryEmpty = findViewById(R.id.tv_history_empty);
        mRvHistory = findViewById(R.id.rv_history);

        mBtnBookingRequests.setOnClickListener(this);
        mBtnCreateExperience.setOnClickListener(this);
        mTvLogOut.setOnClickListener(this);

        // استدعاء القيم المخزنة داخل ال sharedPreferences
        first_name = sharedPreferences.getString(FirebaseViewModel.Preference_First_NAME, "");
        last_name = sharedPreferences.getString(FirebaseViewModel.Preference_Last_NAME, "");
        age = sharedPreferences.getString(FirebaseViewModel.Preference_Age, "");

        // عرض القيم المخزنة داخل ال sharedPreferences
        mTvUserName.setText(first_name + " " + last_name);
        mTvUserAge.setText("Age : " + age);

        getMyExperience();
        getMyRating();
        getAllBookingRequest();

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_booking_requests:
                startActivity(new Intent(MainHostActivity.this, BookingRequestsActivity.class));
                break;
            case R.id.btn_create_experience:
                startActivity(new Intent(MainHostActivity.this, CreateExperienceActivity.class));
                break;

            case R.id.tv_log_out:
                // sharedPreferences حذف القيم المخزنة في ال
                Toast.makeText(this, "You have been successfully logged out", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(MainHostActivity.this, WayToSignActivity.class));
                finish();
                firebaseViewModel.resetPreference();
                break;
        }
    }


    public void getMyExperience() {
        mPbExperience.setVisibility(View.VISIBLE);
        //عرض ال Experience الخاصة باليوزر المسجل دخول به و التي قبلها الادمن
        firebaseFirestore.collection("Experience")
                .whereEqualTo("is_enabled", "1")
                .whereEqualTo("user_id", sharedPreferences.getString(FirebaseViewModel.Preference_ID_USER, ""))
                .addSnapshotListener((value, e) -> {

                    mPbExperience.setVisibility(View.GONE);
                    mRvExperiences.setVisibility(View.VISIBLE);

                    data_experiences.addAll(value.toObjects(Experience.class));

                    if (data_experiences.isEmpty()) {
                        mTvExperienceEmpty.setVisibility(View.VISIBLE);
                    }

                    adsManager = new LinearLayoutManager(MainHostActivity.this);
                    mRvExperiences.setLayoutManager(adsManager);
                    experiencesAdapter = new ExperiencesAdapter(MainHostActivity.this, data_experiences);
                    mRvExperiences.setAdapter(experiencesAdapter);

                    experiencesAdapter.setOnItemClickListener((parent, view, position) -> {

                        Experience data = (Experience) parent;
                        // الانتقال الي واجهة التفاصيل و نقل الاوبجت الخاص ب ال Experience
                        Intent i = new Intent(MainHostActivity.this, ExperienceDetailsActivity.class);
                        i.putExtra("data_experience", data);
                        startActivity(i);
                    });


                });
    }

    // عرض التقييمات التي قام بتقيمها الترفيلر لل الهوست على حسب ال id و نوع الحساب
    public void getMyRating() {
        mPbRatingHost.setVisibility(View.VISIBLE);
        firebaseFirestore.collection("Rating")
                .whereEqualTo("host_id", sharedPreferences.getString(FirebaseViewModel.Preference_ID_USER, ""))
                .whereEqualTo("type", "host")
                .addSnapshotListener((value, e) -> {

                    mPbRatingHost.setVisibility(View.GONE);
                    mRvRatingHost.setVisibility(View.VISIBLE);

                    data_rating.addAll(value.toObjects(Rating.class));

                    if (data_rating.isEmpty()) {
                        mTvRatingHost.setVisibility(View.VISIBLE);
                    }

                    adsManager = new LinearLayoutManager(MainHostActivity.this);
                    mRvRatingHost.setLayoutManager(adsManager);
                    ratingAdapter = new RatingAdapter(MainHostActivity.this, data_rating);
                    mRvRatingHost.setAdapter(ratingAdapter);


                });
    }


    // عرض طلبات الحجز التي قبلت من ضمن الهوست للترفيلر
    public void getAllBookingRequest() {
        mPbHistory.setVisibility(View.VISIBLE);
        firebaseFirestore.collection("Order")
                .whereEqualTo("host_id", sharedPreferences.getString(FirebaseViewModel.Preference_ID_USER, ""))
                .whereEqualTo("is_enabled", "1")
                .addSnapshotListener((value, e) -> {

                    mPbHistory.setVisibility(View.GONE);
                    mRvHistory.setVisibility(View.VISIBLE);

                    // اضافة الطلبات الي ال arrayList و عرضها داخل ال RecyclerView
                    all_order.clear();
                    all_order.addAll(value.toObjects(Order.class));

                    adsManager = new LinearLayoutManager(MainHostActivity.this);
                    mRvHistory.setLayoutManager(adsManager);
                    bookingRequestAdapter = new BookingRequestAdapter(MainHostActivity.this, all_order);
                    mRvHistory.setAdapter(bookingRequestAdapter);
                    bookingRequestAdapter.notifyDataSetChanged();

                    if (all_order.isEmpty()) {
                        mTvHistoryEmpty.setVisibility(View.VISIBLE);
                    }

                    // عند الضغط على ال item يتم ارسال الاوبجكت الخاص في الطلب الي تفاصيل الطلب للقبول او الرفض
                    bookingRequestAdapter.setOnItemClickListener((parent, view, position) -> {
                        Order data = (Order) parent;
//                   // الانتقال الى واجهة تفاصيل الحجز و نقل اوبجت الحجز او الطلب
                        Intent intent = new Intent(MainHostActivity.this, HistoryDetails2Activity.class);
                        intent.putExtra("order_details", data);
                        startActivity(intent);
                    });


                });
    }

}