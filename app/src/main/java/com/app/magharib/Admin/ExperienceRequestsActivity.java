package com.app.magharib.Admin;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.app.magharib.Firebase.FirebaseViewModel;
import com.app.magharib.Model.Experience;
import com.app.magharib.R;
import com.app.magharib.Utils.Constants;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.SetOptions;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.HashMap;
import java.util.Map;

public class ExperienceRequestsActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView mTvNameUser, mTvNameExperience, mTvTypeExperience, mTvLocationExperience, mTvPriceExperience, mTvDetailsExperience, mTvPhoneExperience, mTvEmailExperience;
    private Button mBtnApproveExperience, mBtnDenyExperience;
    private ImageView mImageBack, mImageExperience;
    private AVLoadingIndicatorView mAviLoading;

    private Experience experience_data;
    private FirebaseFirestore rootRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_experience_requests);

        intiViews();

    }

    private void intiViews() {

        rootRef = FirebaseFirestore.getInstance();
        experience_data = (Experience) getIntent().getSerializableExtra("experience_data");

        mImageBack = findViewById(R.id.image_back);
        mTvNameUser = findViewById(R.id.tv_name_user);
        mImageExperience = findViewById(R.id.image_experience);
        mAviLoading = findViewById(R.id.avi_Loading);
        mTvNameExperience = findViewById(R.id.tv_name_experience);
        mTvTypeExperience = findViewById(R.id.tv_type_experience);
        mTvLocationExperience = findViewById(R.id.tv_location_experience);
        mTvPriceExperience = findViewById(R.id.tv_price_experience);
        mTvDetailsExperience = findViewById(R.id.tv_details_experience);
        mTvPhoneExperience = findViewById(R.id.tv_phone_experience);
        mTvEmailExperience = findViewById(R.id.tv_email_experience);
        mBtnApproveExperience = findViewById(R.id.btn_approve_experience);
        mBtnDenyExperience = findViewById(R.id.btn_deny_experience);

        mImageBack.setOnClickListener(this);
        mBtnApproveExperience.setOnClickListener(this);
        mBtnDenyExperience.setOnClickListener(this);

        Glide.with(ExperienceRequestsActivity.this)
                .load(experience_data.getImage_uri_experience())
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

        mTvNameUser.setText("From : " + experience_data.getFirst_name() + " " + experience_data.getLast_name());
        mTvNameExperience.setText(experience_data.getName_experience());
        mTvTypeExperience.setText(experience_data.getType_experience());
        mTvLocationExperience.setText(experience_data.getLocation_experience());
        mTvPriceExperience.setText(experience_data.getPrice_experience()+" SAR");
        mTvDetailsExperience.setText(experience_data.getDetails_experience());
        mTvPhoneExperience.setText(experience_data.getPhone_experience());
        mTvEmailExperience.setText(experience_data.getEmail_experience());

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.image_back:
                onBackPressed();
                break;

            case R.id.btn_approve_experience:
                ApproveExperience();
                break;

            case R.id.btn_deny_experience:
                DenyExperience();
                break;
        }
    }


    public void ApproveExperience() {
        Constants.showProgress(ExperienceRequestsActivity.this);
        CollectionReference complaintsRef = rootRef.collection("Experience");
        complaintsRef.whereEqualTo("id_experience", experience_data.getId_experience()).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Constants.hideProgress();
                for (QueryDocumentSnapshot document : task.getResult()) {
                    Map<Object, String> map = new HashMap<>();
                    map.put("is_enabled", "1");
                    complaintsRef.document(document.getId()).set(map, SetOptions.merge());
                }
                Intent i = new Intent(ExperienceRequestsActivity.this, StatusAdminActivity.class);
                i.putExtra("status_admin", "approve_user");
                startActivity(i);
            }
        });
    }

    public void DenyExperience() {
        Constants.showProgress(ExperienceRequestsActivity.this);
        CollectionReference complaintsRef = rootRef.collection("Experience");
        complaintsRef.whereEqualTo("id_experience", experience_data.getId_experience()).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Constants.hideProgress();
                for (QueryDocumentSnapshot document : task.getResult()) {
                    Map<Object, String> map = new HashMap<>();
                    map.put("is_enabled", "2");
                    complaintsRef.document(document.getId()).set(map, SetOptions.merge());
                }
                Intent i2 = new Intent(ExperienceRequestsActivity.this, StatusAdminActivity.class);
                i2.putExtra("status_admin", "deny_user");
                startActivity(i2);
            }
        });
    }
}