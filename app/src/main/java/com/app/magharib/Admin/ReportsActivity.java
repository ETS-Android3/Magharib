package com.app.magharib.Admin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.app.magharib.Firebase.FirebaseViewModel;
import com.app.magharib.Model.Report;
import com.app.magharib.R;
import com.app.magharib.Utils.Constants;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;

public class ReportsActivity extends AppCompatActivity implements View.OnClickListener {

    private Button mBtnDeleteComment, mBtnDisableUser;
    private TextView mTvUserEmail, mTvUserEmailFrom;
    private ImageView mImageBack;

    private Report data_reports;
    private FirebaseFirestore rootRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reports);

        intiViews();


    }

    private void intiViews() {

        rootRef = FirebaseFirestore.getInstance();
        // استقبال الاوبجكت الخاص ب ال reports
        data_reports = (Report) getIntent().getSerializableExtra("data_reports");

        mBtnDeleteComment = findViewById(R.id.btn_delete_comment);
        mBtnDisableUser = findViewById(R.id.btn_disable_user);
        mTvUserEmail = findViewById(R.id.tv_user_email);
        mTvUserEmailFrom = findViewById(R.id.tv_user_email_from);
        mImageBack = findViewById(R.id.image_back);

        mBtnDeleteComment.setOnClickListener(this);
        mBtnDisableUser.setOnClickListener(this);
        mImageBack.setOnClickListener(this);

        // عرض الايميلات الخاصة بالابلاغ عن اليوزر
        mTvUserEmail.setText(data_reports.getEmail_user_report());
        mTvUserEmailFrom.setText("From : " + data_reports.getEmail_user());


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.image_back:
               onBackPressed();
                break;
                case R.id.btn_delete_comment:
                Intent i = new Intent(ReportsActivity.this, StatusAdminActivity.class);
                i.putExtra("status_admin", "delete_comment");
                startActivity(i);
                break;
            case R.id.btn_disable_user:
                DisableUser();
                break;
        }
    }

    public void DisableUser() {
        // تعديل قيمة is_enabled داخل جدول اليوزر الى 2 لعمل deny او disable لليوزر الذي قام بالابلاغ عنه
        Constants.showProgress(ReportsActivity.this);
        CollectionReference complaintsRef = rootRef.collection(FirebaseViewModel.USERS_COLLECTION);
        complaintsRef.whereEqualTo(FirebaseViewModel.IDUserOrder, data_reports.getId_user_report()).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Constants.hideProgress();
                for (QueryDocumentSnapshot document : task.getResult()) {
                    Map<Object, String> map = new HashMap<>();
                    map.put(FirebaseViewModel.IsEnabled, "2");
                    complaintsRef.document(document.getId()).set(map, SetOptions.merge());
                }
                DeleteReports("disable_user");
            }
        });
    }


    public void DeleteReports(String status_admin) {
        CollectionReference itemsRef = rootRef.collection("Report");
        Query query = itemsRef.whereEqualTo("id_report", data_reports.getId_report());
        query.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (DocumentSnapshot document : task.getResult()) {

                    itemsRef.document(document.getId()).delete();

                    Intent i2 = new Intent(ReportsActivity.this, StatusAdminActivity.class);
                    i2.putExtra("status_admin", status_admin);
                    startActivity(i2);
                }
            } else {
                Constants.hideProgress();
                Toast.makeText(this, "Error : " + task.getException(), Toast.LENGTH_SHORT).show();
            }
        });
    }


}