package com.app.magharib.Traveler;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.app.magharib.Admin.MainAdminActivity;
import com.app.magharib.Firebase.FirebaseViewModel;
import com.app.magharib.Host.MainHostActivity;
import com.app.magharib.Model.Experience;
import com.app.magharib.R;
import com.app.magharib.SignInActivity;
import com.app.magharib.SplashActivity;
import com.app.magharib.WayToSignActivity;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.wang.avi.AVLoadingIndicatorView;

public class DetailsPlacesActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView mTvNameExperience , mTvPriceExperience ,mTvDetailsExperience ,mTvPhoneExperience , mTvEmailExperience  ;
    private ImageView mImageBack , mImageExperience;
    private AVLoadingIndicatorView mAviLoading;
    private Button mBtnBookPlace;

    private Experience data_experience;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_places);

        intiViews();

    }

    // عشان اقدر اتحم بالعناصر الموجودة ف ال Activity و افعل الضغطة مثلا على الزر
    private void intiViews() {

        // experience استقبال الاوبجت الخاص في ال
        data_experience = (Experience) getIntent().getSerializableExtra("data_experience");

        mImageBack = findViewById(R.id.image_back);
        mImageExperience = findViewById(R.id.image_experience);
        mAviLoading = findViewById(R.id.avi_Loading);
        mTvNameExperience = findViewById(R.id.tv_name_experience);
        mTvPriceExperience = findViewById(R.id.tv_price_experience);
        mTvDetailsExperience = findViewById(R.id.tv_details_experience);
        mTvPhoneExperience = findViewById(R.id.tv_phone_experience);
        mTvEmailExperience = findViewById(R.id.tv_email_experience);
        mBtnBookPlace = findViewById(R.id.btn_book_place);

        mImageBack.setOnClickListener(this);
        mBtnBookPlace.setOnClickListener(this);

        // experience عرض الداتا الخاصة ب ال
        Glide.with(DetailsPlacesActivity.this)
                .load(data_experience.getImage_uri_experience())
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        mAviLoading.setVisibility(View.GONE);
                        return false;
                    }
                }).into(mImageExperience);

        mTvNameExperience.setText(data_experience.getName_experience());
        mTvPriceExperience.setText(data_experience.getPrice_experience()+" SAR");
        mTvDetailsExperience.setText(data_experience.getDetails_experience());
        mTvPhoneExperience.setText(data_experience.getPhone_experience());
        mTvEmailExperience.setText(data_experience.getEmail_experience());

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.image_back:
                onBackPressed();
                break;
            case R.id.btn_book_place:

                // يتم الفحص اذا اليوزر عامل لوجن او لا من خلال القيمة المخزنة داخل ال sharedPreferences اذا مسجل دخول يتم الانتقال الي واجهة اختيار تاريخ الحجز اذ العكس بيروح ل واجهة تسجيل الدخول
                sharedPreferences = getSharedPreferences(FirebaseViewModel.SHARED_PREFERENCE_NAME, MODE_PRIVATE);
                if (sharedPreferences.contains(FirebaseViewModel.IS_LOGIN)) {
                    if (sharedPreferences.getInt(FirebaseViewModel.IS_LOGIN, -1) == 0) {
                        Toast.makeText(this, "You Must Be Logged In To Book", Toast.LENGTH_SHORT).show();
                        startActivity( new Intent(DetailsPlacesActivity.this, SignInActivity.class));
                    } else {
                        // ارسال الاوجبت الى واجهة اختيار التاريخ الخاص بالحجز
                        Intent i = new Intent(DetailsPlacesActivity.this, BookPlaceActivity.class);
                        i.putExtra("data_experience",data_experience);
                        startActivity(i);
                    }
                } else {
                    Toast.makeText(this, "You Must Be Logged In To Book", Toast.LENGTH_SHORT).show();
                    startActivity( new Intent(DetailsPlacesActivity.this, SignInActivity.class));
                }

                break;
        }
    }
}