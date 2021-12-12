package com.app.magharib.Host;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.app.magharib.Admin.ExperienceRequestsActivity;
import com.app.magharib.Admin.StatusAdminActivity;
import com.app.magharib.Firebase.FirebaseViewModel;
import com.app.magharib.Model.Order;
import com.app.magharib.R;
import com.app.magharib.Utils.Constants;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class DetailsBookingRequestsActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView mTvUserName , mTvInformationUser , mTvNameExperience , mTvTypeExperience ,mTvDate  ;
    private Button mBtnApprove, mBtnDeny;
    private ImageView mImageBack, mImageReport;

    private Dialog dialog_report;
    private Order data_order;

    private SharedPreferences sharedPreferences;
    private FirebaseFirestore rootRef;
    private FirebaseViewModel firebaseViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_booking_requests);

        intiViews();


    }

    // عشان اقدر اتحم بالعناصر الموجودة ف ال Activity و افعل الضغطة مثلا على الزر
    private void intiViews() {

        sharedPreferences = getSharedPreferences(FirebaseViewModel.SHARED_PREFERENCE_NAME, MODE_PRIVATE);
        rootRef = FirebaseFirestore.getInstance();
        firebaseViewModel=new FirebaseViewModel(this);

        // استقبال الاوبكت الخاص بالاوردر
        data_order = (Order) getIntent().getSerializableExtra("data_order");

        mImageBack = findViewById(R.id.image_back);
        mImageReport = findViewById(R.id.image_report);
        mBtnApprove = findViewById(R.id.btn_approve);
        mBtnDeny = findViewById(R.id.btn_deny);
        mTvUserName = findViewById(R.id.tv_user_name);
        mTvInformationUser = findViewById(R.id.tv_information_user);
        mTvNameExperience = findViewById(R.id.tv_name_experience);
        mTvTypeExperience = findViewById(R.id.tv_type_experience);
        mTvDate = findViewById(R.id.tv_date);

        mImageBack.setOnClickListener(this);
        mImageReport.setOnClickListener(this);
        mBtnApprove.setOnClickListener(this);
        mBtnDeny.setOnClickListener(this);

        // عرض معلومات الطلب
        mTvUserName.setText(data_order.getFirst_name()+" "+data_order.getLast_name());
        mTvInformationUser.setText(data_order.getLocation_experience()+" , "+data_order.getAge_user()+" years old");
        mTvNameExperience.setText(data_order.getName_experience());
        mTvTypeExperience.setText(data_order.getType_experience());
        mTvDate.setText(data_order.getDate_order());


        MutableLiveData<Boolean> liveData= firebaseViewModel.isUserReport(sharedPreferences.getString(FirebaseViewModel.Preference_ID_USER, ""),data_order.getUser_id());
        // عمل فحص اذا كان اليوزر عامل ابلاغ ل يوزر اخر
        liveData.observe(DetailsBookingRequestsActivity.this, aBoolean -> {
            if (aBoolean){
                mImageReport.setClickable(false);
                mImageReport.setImageResource(R.drawable.flag_active);
            }else {
                mImageReport.setClickable(true);
                mImageReport.setImageResource(R.drawable.image_flag);
            }
        });

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.image_back:
                onBackPressed();
                break;

            case R.id.image_report:
                DialogReportUser();
                break;
            case R.id.btn_approve:
                ApproveOrder();
                break;
            case R.id.btn_deny:
                DenyOrder();
                break;
        }
    }


    public void ApproveOrder() {
        // جلب البيانات الخاصة ب جدول الطلبات على حسب id_order و تعديل قيمة is_enabled الي 1 بمعنى الهوست قبل الطلب
        Constants.showProgress(DetailsBookingRequestsActivity.this);
        CollectionReference complaintsRef = rootRef.collection("Order");
        complaintsRef.whereEqualTo("id_order", data_order.getId_order()).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Constants.hideProgress();
                for (QueryDocumentSnapshot document : task.getResult()) {
                    Map<Object, String> map = new HashMap<>();
                    map.put("is_enabled", "1");
                    complaintsRef.document(document.getId()).set(map, SetOptions.merge());
                }
                // الانتقال الي واجهة حالة الطلب و ارسال قيمة حالة الطلب
                Intent i = new Intent(DetailsBookingRequestsActivity.this, StatusBookingRequestsActivity.class);
                i.putExtra("status", "approve");
                startActivity(i);
            }
        });
    }

    public void DenyOrder() {
        // جلب البيانات الخاصة ب جدول الطلبات على حسب id_order و تعديل قيمة is_enabled الي 2 بمعنى الهوست رفض الطلب
        Constants.showProgress(DetailsBookingRequestsActivity.this);
        CollectionReference complaintsRef = rootRef.collection("Order");
        complaintsRef.whereEqualTo("id_order", data_order.getId_order()).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Constants.hideProgress();
                for (QueryDocumentSnapshot document : task.getResult()) {
                    Map<Object, String> map = new HashMap<>();
                    map.put("is_enabled", "2");
                    complaintsRef.document(document.getId()).set(map, SetOptions.merge());
                }
                // الانتقال الي واجهة حالة الطلب و ارسال قيمة حالة الطلب
                Intent i2 = new Intent(DetailsBookingRequestsActivity.this, StatusBookingRequestsActivity.class);
                i2.putExtra("status", "deny");
                startActivity(i2);
            }
        });
    }

    private void DialogReportUser() {
        dialog_report = new Dialog(DetailsBookingRequestsActivity.this, R.style.SheetDialog);
        dialog_report.setContentView(R.layout.dialog_report);
        dialog_report.setCancelable(false);

        TextView tv_no = dialog_report.findViewById(R.id.tv_no);
        TextView tv_yes = dialog_report.findViewById(R.id.tv_yes);

        dialog_report.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);


        tv_no.setOnClickListener(view -> {
            dialog_report.dismiss();
        });

        tv_yes.setOnClickListener(view -> {
            ReportUser();
        });


        dialog_report.show();

    }


    public void ReportUser() {

        Map<String, Object> data = new HashMap<>();

        // عمل id لل Report المضاف من خلال رقم عشوائي
        Random rand = new Random();
        int id = rand.nextInt(1000000);
        String id_report = String.valueOf(id);

        // ارسال القيم الي جدول ال Report داخل الفايربيس
        data.put("id_report",id_report);
        data.put("id_user",sharedPreferences.getString(FirebaseViewModel.Preference_IDUserOrder, ""));
        data.put("email_user",sharedPreferences.getString(FirebaseViewModel.Preference_Email, ""));
        data.put("id_user_report",data_order.getUser_id());
        data.put("email_user_report",data_order.getEmail_user());

        rootRef.collection("Report").add(data).addOnSuccessListener(documentReference -> {

            dialog_report.dismiss();

            mImageReport.setClickable(false);
            mImageReport.setImageResource(R.drawable.flag_active);

            Toast.makeText(this, "Reporting Completed Successfully", Toast.LENGTH_SHORT).show();

            Constants.hideProgress();

        }).addOnFailureListener(e -> {
            e.printStackTrace();
            Constants.hideProgress();
            // اذا فشلت العملية يتم طباعة الخطأ
            Toast.makeText(this, ""+ e, Toast.LENGTH_SHORT).show();
            Log.w("Error", "Error writing document", e);
        });

    }


}