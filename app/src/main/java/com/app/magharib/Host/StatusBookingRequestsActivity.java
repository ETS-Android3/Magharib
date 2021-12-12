package com.app.magharib.Host;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.app.magharib.R;

public class StatusBookingRequestsActivity extends AppCompatActivity {

    private TextView mTvStatusBooking;

    private String status;
    private ImageView mImageBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status_booking_requests);

        // استقبال حالة الطلب سؤاء القبول او الرفض ل عرضها داخل ال TextView
        status = getIntent().getExtras().getString("status");

        mTvStatusBooking = findViewById(R.id.tv_status_booking);
        mImageBack = findViewById(R.id.image_back);

        mImageBack.setOnClickListener(view -> onBackPressed());

        // قحص حالة الطلب و عرض حالة النص المناسب لها
        if (status.equals("approve")) {
            mTvStatusBooking.setText(getResources().getString(R.string.txt_approve_booking));
        } else {
            mTvStatusBooking.setText(getResources().getString(R.string.txt_deny_booking));
        }

    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(getApplicationContext(), MainHostActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
        finish();
        super.onBackPressed();
    }
}