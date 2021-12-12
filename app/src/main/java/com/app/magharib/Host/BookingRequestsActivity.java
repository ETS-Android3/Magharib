package com.app.magharib.Host;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.app.magharib.Adapter.BookingRequestAdapter;
import com.app.magharib.Adapter.HostRequestsAdapter;
import com.app.magharib.Admin.MainAdminActivity;
import com.app.magharib.Firebase.FirebaseViewModel;
import com.app.magharib.Model.HostTravelerRequests;
import com.app.magharib.Model.Order;
import com.app.magharib.R;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class BookingRequestsActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView mImageBack;
    private RecyclerView mRvBookingRequest;
    private ProgressBar mPbBookingRequest;
    private TextView mTvBookingEmpty;

    private List<Order> all_order;
    private LinearLayoutManager adsManager;

    private BookingRequestAdapter bookingRequestAdapter;
    private FirebaseFirestore firebaseFirestore;
    private SharedPreferences sharedPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_requests);

        intiViews();


    }

    // عشان اقدر اتحم بالعناصر الموجودة ف ال Activity و افعل الضغطة مثلا على الزر
    private void intiViews() {

        all_order = new ArrayList<>();
        sharedPreferences = getSharedPreferences(FirebaseViewModel.SHARED_PREFERENCE_NAME, MODE_PRIVATE);
        firebaseFirestore = FirebaseFirestore.getInstance();

        mImageBack = findViewById(R.id.image_back);
        mRvBookingRequest = findViewById(R.id.rv_booking_request);
        mPbBookingRequest = findViewById(R.id.pb_booking_request);
        mTvBookingEmpty = findViewById(R.id.tv_booking_empty);

        mImageBack.setOnClickListener(this);

        getAllBookingRequest();

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.image_back:
                onBackPressed();
                break;
        }
    }


    // عرض طلبات الحجز
    public void getAllBookingRequest() {
        mPbBookingRequest.setVisibility(View.VISIBLE);
        firebaseFirestore.collection("Order")
                .whereEqualTo("host_id", sharedPreferences.getString(FirebaseViewModel.Preference_ID_USER, ""))
                .whereEqualTo("is_enabled", "0")
                .addSnapshotListener((value, e) -> {

                    mPbBookingRequest.setVisibility(View.GONE);
                    mRvBookingRequest.setVisibility(View.VISIBLE);

                    // اضافة الطلبات الي ال arrayList و عرضها داخل ال RecyclerView
                    all_order.addAll(value.toObjects(Order.class));

                    adsManager = new LinearLayoutManager(BookingRequestsActivity.this);
                    mRvBookingRequest.setLayoutManager(adsManager);
                    bookingRequestAdapter = new BookingRequestAdapter(BookingRequestsActivity.this, all_order);
                    mRvBookingRequest.setAdapter(bookingRequestAdapter);

                    if (all_order.isEmpty()){
                        mTvBookingEmpty.setVisibility(View.VISIBLE);
                    }

                    // عند الضغط على ال item يتم ارسال الاوبجكت الخاص في الطلب الي تفاصيل الطلب للقبول او الرفض
                    bookingRequestAdapter.setOnItemClickListener((parent, view, position) -> {
                        Order data = (Order) parent;
                        Intent i = new Intent(BookingRequestsActivity.this, DetailsBookingRequestsActivity.class);
                        i.putExtra("data_order", data);
                        startActivity(i);
                    });


                });
    }

}