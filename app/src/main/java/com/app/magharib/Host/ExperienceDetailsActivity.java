package com.app.magharib.Host;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.wang.avi.AVLoadingIndicatorView;

public class ExperienceDetailsActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView mTvNameExperience, mTvLocationExperience, mTvPriceExperience, mTvDetailsExperience, mTvPhoneExperience, mTvEmailExperience;
    private ImageView mImageBack, mImageExperience;
    private Button mBtnDelete, mBtnUpdate;
    private AVLoadingIndicatorView mAviLoading;

    private Dialog dialog_delete;
    private Experience data_experience;

    private SharedPreferences sharedPreferences;
    private FirebaseFirestore firebaseFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_experience_details);

        intiViews();

    }

    // عشان اقدر اتحم بالعناصر الموجودة ف ال Activity و افعل الضغطة مثلا على الزر
    private void intiViews() {

        firebaseFirestore = FirebaseFirestore.getInstance();
        sharedPreferences = getSharedPreferences(FirebaseViewModel.SHARED_PREFERENCE_NAME, MODE_PRIVATE);

        // استقبال الاوبجت الخاص في ال Experience
        data_experience = (Experience) getIntent().getSerializableExtra("data_experience");

        mImageBack = findViewById(R.id.image_back);
        mImageExperience = findViewById(R.id.image_experience);
        mAviLoading = findViewById(R.id.avi_Loading);
        mTvNameExperience = findViewById(R.id.tv_name_experience);
        mTvLocationExperience = findViewById(R.id.tv_location_experience);
        mTvPriceExperience = findViewById(R.id.tv_price_experience);
        mTvDetailsExperience = findViewById(R.id.tv_details_experience);
        mTvPhoneExperience = findViewById(R.id.tv_phone_experience);
        mTvEmailExperience = findViewById(R.id.tv_email_experience);
        mBtnUpdate = findViewById(R.id.btn_update);
        mBtnDelete = findViewById(R.id.btn_delete);


        mImageBack.setOnClickListener(this);
        mBtnUpdate.setOnClickListener(this);
        mBtnDelete.setOnClickListener(this);


        // عرض تفاصيل ال Experience من خلال الاوبجكت المستقبل
        Glide.with(ExperienceDetailsActivity.this)
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
        mTvLocationExperience.setText(data_experience.getLocation_experience());
        mTvPriceExperience.setText(data_experience.getPrice_experience() + " SAR");
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
            case R.id.btn_update:
                // الانتقال الي واجهة تعديل ال Experience و نقل الاوبجت الخاص ب ال Experience
                Intent i =new Intent(ExperienceDetailsActivity.this, UpdateExperienecDetailsActivity.class);
                i.putExtra("data_experience", data_experience);
                startActivity(i);
                break;
            case R.id.btn_delete:
                DialogDelete();
                break;
        }
    }


    // ديلوق الحذف
    private void DialogDelete() {
        dialog_delete = new Dialog(ExperienceDetailsActivity.this, R.style.SheetDialog);
        dialog_delete.setContentView(R.layout.dialog_delete);
        dialog_delete.setCancelable(false);

        Button btn_cancel = dialog_delete.findViewById(R.id.btn_cancel);
        Button btn_delete = dialog_delete.findViewById(R.id.btn_delete);

        dialog_delete.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);


        btn_cancel.setOnClickListener(view -> {
            dialog_delete.dismiss();
        });

        btn_delete.setOnClickListener(view -> {
            DeleteExperience();
        });


        dialog_delete.show();

    }

    // حذف ال Experience الخاص في الهوست و حذف الطلبات الخاصة في ال Experience
    public void DeleteExperience() {
        Constants.showProgress(ExperienceDetailsActivity.this);
        CollectionReference itemsRef = firebaseFirestore.collection("Experience");
        Query query = itemsRef.whereEqualTo("user_id", sharedPreferences.getString(FirebaseViewModel.Preference_ID_USER, "")).whereEqualTo("id_experience", data_experience.getId_experience());
        query.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (DocumentSnapshot document : task.getResult()) {
                    itemsRef.document(document.getId()).delete();

                    DeleteOrder();
                }
            } else {
                Constants.hideProgress();
                Toast.makeText(this, "Error : " + task.getException(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    //حذف الطلبات الخاصة في ال experience المحذوف على حسب ال id
    public void DeleteOrder() {
        CollectionReference itemsRef = firebaseFirestore.collection("Order");
        Query query = itemsRef.whereEqualTo("id_experience", data_experience.getId_experience());
        query.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (DocumentSnapshot document : task.getResult()) {

                    itemsRef.document(document.getId()).delete();

                    dialog_delete.dismiss();
                    Intent i = new Intent(getApplicationContext(), MainHostActivity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(i);
                    finish();

                    Toast.makeText(this, "Experience Has Been Deleted Successfully", Toast.LENGTH_SHORT).show();

                }
            } else {
                Toast.makeText(this, "Error : " + task.getException(), Toast.LENGTH_SHORT).show();
            }
        });
    }


}