package com.app.magharib.Traveler;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.app.magharib.Admin.ExperienceRequestsActivity;
import com.app.magharib.Admin.StatusAdminActivity;
import com.app.magharib.Firebase.FirebaseViewModel;
import com.app.magharib.Host.DetailsBookingRequestsActivity;
import com.app.magharib.Model.Order;
import com.app.magharib.R;
import com.app.magharib.Utils.Constants;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.SetOptions;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class HistoryDetailsActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView mTvNameExperience, mTvTypeExperience, mTvDate;
    private ImageView mImageBack;
    private Button mBtnRating;

    private Order order_details;
    private Dialog dialog_rating;

    private FirebaseFirestore firebaseFirestore;
    private SharedPreferences sharedPreferences;
    private int rat_number = 0 ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_details);

        intiViews();

    }

    // عشان اقدر اتحم بالعناصر الموجودة ف ال Activity و افعل الضغطة مثلا على الزر
    private void intiViews() {

        firebaseFirestore = FirebaseFirestore.getInstance();
        sharedPreferences = getSharedPreferences(FirebaseViewModel.SHARED_PREFERENCE_NAME, MODE_PRIVATE);


        // استقبال الاوبجكت الخاص ب تفاصيل الحجز
        order_details = (Order) getIntent().getSerializableExtra("order_details");

        mImageBack = findViewById(R.id.image_back);
        mTvNameExperience = findViewById(R.id.tv_name_experience);
        mTvTypeExperience = findViewById(R.id.tv_type_experience);
        mTvDate = findViewById(R.id.tv_date);
        mBtnRating = findViewById(R.id.btn_rating);

        mImageBack.setOnClickListener(this);
        mBtnRating.setOnClickListener(this);

        // عرض تفاصيل الحجز
        mTvNameExperience.setText(order_details.getName_experience());
        mTvTypeExperience.setText(order_details.getType_experience());
        mTvDate.setText(order_details.getDate_order());

        // عمل فحص اذا الترفيلر قيم او لا اذا قيم يتم اخفاء زر التقييم اذا لم يقيم يتم اظهار زر التقييم
        if (order_details.getIs_traveler_rating().equals("0")){
            mBtnRating.setVisibility(View.VISIBLE);
        }else {
            mBtnRating.setVisibility(View.GONE);
        }

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.image_back:
                onBackPressed();
                break;

            case R.id.btn_rating:
                DialogRating();
                break;

        }
    }


    // ديلوق التقييم
    private void DialogRating() {
        dialog_rating = new Dialog(HistoryDetailsActivity.this, R.style.SheetDialog);
        dialog_rating.setContentView(R.layout.dialog_rating);
        dialog_rating.setCancelable(false);

        RatingBar rb_rating = dialog_rating.findViewById(R.id.rb_rating);
        EditText ed_comment = dialog_rating.findViewById(R.id.ed_comment);
        Button btn_rating = dialog_rating.findViewById(R.id.btn_rating);

        rb_rating.setOnRatingBarChangeListener((ratingBar, rating, fromUser) -> {
            rat_number = (int) rating;
        });

        btn_rating.setOnClickListener(view -> {
            if (rat_number == 0){
                Toast.makeText(this, "Please Enter a Rating", Toast.LENGTH_SHORT).show();
            }else if (ed_comment.getText().toString().equals("")){
                Toast.makeText(this, "Please enter a comment", Toast.LENGTH_SHORT).show();
            }else {
                RatingOrder(ed_comment.getText().toString());
            }
        });

        dialog_rating.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);


        dialog_rating.show();

    }


    // اضافة تقييم على جدول ال rating
    public void RatingOrder(String comment) {

        Constants.showProgress(HistoryDetailsActivity.this);
        Map<String, Object> data = new HashMap<>();

        Date c = Calendar.getInstance().getTime();

        data.put("host_id", order_details.getHost_id());
        data.put("traveler_id", order_details.getUser_id());
        data.put("rating", rat_number);
        data.put("comment", comment);
        data.put("type", "host");
        data.put("date", Constants.FormatDate(c));


        firebaseFirestore.collection("Rating").add(data).addOnSuccessListener(documentReference -> {

            //  اذا نجحت العملية يتم التحديث على جدول الطلبات او الحجوزات على قيمة is_traveler_rating
            UpdateTravelerRating();

        }).addOnFailureListener(e -> {
            e.printStackTrace();
            Constants.hideProgress();
            // اذا فشلت العملية يتم طباعة الخطأ
            Toast.makeText(this, "" + e, Toast.LENGTH_SHORT).show();
            Log.w("Error", "Error writing document", e);
        });

    }

    // عمل تحديث على جدول الطلبات او الحجوزات على قيمة is_traveler_rating
    public void UpdateTravelerRating() {
        CollectionReference complaintsRef = firebaseFirestore.collection("Order");
        complaintsRef.whereEqualTo("id_order", order_details.getId_order()).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Constants.hideProgress();
                for (QueryDocumentSnapshot document : task.getResult()) {
                    Map<Object, String> map = new HashMap<>();
                    map.put("is_traveler_rating", "1");
                    complaintsRef.document(document.getId()).set(map, SetOptions.merge());
                }

                mBtnRating.setVisibility(View.GONE);
                Toast.makeText(this, "Your Review Has Been Successfully Added", Toast.LENGTH_SHORT).show();
                dialog_rating.dismiss();
            }
        });
    }



}