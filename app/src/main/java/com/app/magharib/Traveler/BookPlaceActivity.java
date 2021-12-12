package com.app.magharib.Traveler;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.app.magharib.Firebase.FirebaseViewModel;
import com.app.magharib.Model.Experience;
import com.app.magharib.R;
import com.app.magharib.Utils.Constants;
import com.applandeo.materialcalendarview.CalendarView;
import com.applandeo.materialcalendarview.DatePicker;
import com.applandeo.materialcalendarview.builders.DatePickerBuilder;
import com.applandeo.materialcalendarview.listeners.OnSelectDateListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class BookPlaceActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView mTvChooseDate, mTvCancel, mTvConfirm;
    private ImageView mImageBack;

    private Experience data_experience;
    private SharedPreferences sharedPreferences;
    private FirebaseFirestore firebaseFirestore;

    private String user_id, first_name, last_name, Age, email_user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_place);

        intiViews();

    }

    // عشان اقدر اتحم بالعناصر الموجودة ف ال Activity و افعل الضغطة مثلا على الزر
    private void intiViews() {

        firebaseFirestore = FirebaseFirestore.getInstance();
        sharedPreferences = getSharedPreferences(FirebaseViewModel.SHARED_PREFERENCE_NAME, MODE_PRIVATE);

        // experience استقبال الاويجكت الخاص في ال
        data_experience = (Experience) getIntent().getSerializableExtra("data_experience");

        // استدعاء القيم المخزنة داخل ال sharedPreferences
        user_id = sharedPreferences.getString(FirebaseViewModel.Preference_IDUserOrder, "");
        first_name = sharedPreferences.getString(FirebaseViewModel.Preference_First_NAME, "");
        last_name = sharedPreferences.getString(FirebaseViewModel.Preference_Last_NAME, "");
        Age = sharedPreferences.getString(FirebaseViewModel.Preference_Age, "");
        email_user = sharedPreferences.getString(FirebaseViewModel.Preference_Email, "");

        mImageBack = findViewById(R.id.image_back);
        mTvCancel = findViewById(R.id.tv_cancel);
        mTvConfirm = findViewById(R.id.tv_confirm);
        mTvChooseDate = findViewById(R.id.tv_choose_date);

        mImageBack.setOnClickListener(this);
        mTvConfirm.setOnClickListener(this);
        mTvCancel.setOnClickListener(this);
        mTvChooseDate.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_choose_date:
                // عرض ديلوق الخاص بالتاريخ ل اختيار تاريخ الحجز
                DatePickerBuilder builder = new DatePickerBuilder(this, listener).pickerType(CalendarView.RANGE_PICKER);
                DatePicker datePicker = builder.build();
                datePicker.show();
                break;
            case R.id.image_back:
                onBackPressed();
                break;
            case R.id.tv_cancel:
                finish();
                break;
            case R.id.tv_confirm:
                if (mTvChooseDate.getText().toString().equals("Choose The Date")) {
                    Toast.makeText(this, "Please Choose The Date", Toast.LENGTH_SHORT).show();
                } else {
                    AddOrder();
                }
                break;
        }
    }


    // عند الاختيار يتم عرض تاريخ الحجز المختار من الى
    public OnSelectDateListener listener = calendars -> {

        for (int i = 0; i < calendars.size(); i++) {

            Date date1 = calendars.get(0).getTime();
            Date date2 = calendars.get(i).getTime();

            mTvChooseDate.setText(Constants.FormatDate(date1) + " - " + Constants.FormatDate(date2));

        }
    };


    public void AddOrder() {

        Constants.showProgress(BookPlaceActivity.this);
        Map<String, Object> data = new HashMap<>();

        // عمل id للطلب المضاف من خلال رقم عشوائي
        Random rand = new Random();
        int id = rand.nextInt(1000000);
        String id_order = String.valueOf(id);

        // ارسال القيم الي جدول ال الطلبات داخل الفايربيس
        data.put("id_order", id_order);
        data.put("user_id", user_id);
        data.put("host_id", data_experience.getUser_id());
        data.put("first_name", first_name);
        data.put("last_name", last_name);
        data.put("age_user", Age);
        data.put("email_user", email_user);
        data.put("date_order", mTvChooseDate.getText().toString());
        data.put("id_experience", data_experience.getId_experience());
        data.put("name_experience", data_experience.getName_experience());
        data.put("location_experience", data_experience.getLocation_experience());
        data.put("type_experience", data_experience.getType_experience());
        data.put("is_enabled", "0");
        data.put("is_traveler_rating", "0");
        data.put("is_host_rating", "0");

        firebaseFirestore.collection("Order").add(data).addOnSuccessListener(documentReference -> {

            // اذا نجحت العملية يتم الانتقال الي تفاصيل الحجز و يتم انتظار الموافقة من جهة الهوست
            Intent i = new Intent(getApplicationContext(), BookingDetailsActivity.class);
            // نقل الايميل و رقم الهاتف الخاص ب ال experience الى واجهة تفاصيل الحجز
            i.putExtra("phone_experience", data_experience.getPhone_experience());
            i.putExtra("email_experience", data_experience.getEmail_experience());
            i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);
            finish();

            Constants.hideProgress();

        }).addOnFailureListener(e -> {
            e.printStackTrace();
            Constants.hideProgress();
            // اذا فشلت العملية يتم طباعة الخطأ
            Toast.makeText(this, "" + e, Toast.LENGTH_SHORT).show();
            Log.w("Error", "Error writing document", e);
        });

    }


}