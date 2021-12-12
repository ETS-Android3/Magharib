package com.app.magharib.Admin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.app.magharib.Adapter.HostRequestsAdapter;
import com.app.magharib.Firebase.FirebaseViewModel;
import com.app.magharib.Model.HostTravelerRequests;
import com.app.magharib.R;
import com.app.magharib.Utils.Constants;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;

public class HostTravelerRequestsActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView mTvUserName, mTvAccountType, mTvAge, mTvUserEmail;
    private Button mBtnApproveUser, mBtnDenyUser;
    private ImageView mImageBack;


    private String id_user, user_name, account_type, age, user_email;

    private FirebaseFirestore rootRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_host_traveler_requests);

        intiViews();

    }

    private void intiViews() {

        rootRef = FirebaseFirestore.getInstance();

        id_user = getIntent().getExtras().getString("id_user");
        user_name = getIntent().getExtras().getString("user_name");
        account_type = getIntent().getExtras().getString("account_type");
        age = getIntent().getExtras().getString("age");
        user_email = getIntent().getExtras().getString("user_email");


        mImageBack = findViewById(R.id.image_back);
        mTvUserName = findViewById(R.id.tv_user_name);
        mTvAccountType = findViewById(R.id.tv_account_type);
        mTvAge = findViewById(R.id.tv_age);
        mTvUserEmail = findViewById(R.id.tv_user_email);
        mBtnApproveUser = findViewById(R.id.btn_approve_user);
        mBtnDenyUser = findViewById(R.id.btn_deny_user);

        mImageBack.setOnClickListener(this);
        mBtnApproveUser.setOnClickListener(this);
        mBtnDenyUser.setOnClickListener(this);

        mTvUserName.setText(user_name);
        mTvAccountType.setText(account_type + " Request");
        mTvAge.setText(age + " years old");
        mTvUserEmail.setText(user_email);

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.image_back:
                onBackPressed();
                break;
            case R.id.btn_approve_user:
                ApproveUser();
                break;
            case R.id.btn_deny_user:
                DenyUser();
                break;
        }
    }


    public void ApproveUser() {
        Constants.showProgress(HostTravelerRequestsActivity.this);
        CollectionReference complaintsRef = rootRef.collection(FirebaseViewModel.USERS_COLLECTION);
        complaintsRef.whereEqualTo(FirebaseViewModel.IDUserOrder, id_user).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Constants.hideProgress();
                for (QueryDocumentSnapshot document : task.getResult()) {
                    Map<Object, String> map = new HashMap<>();
                    map.put(FirebaseViewModel.IsEnabled, "1");
                    complaintsRef.document(document.getId()).set(map, SetOptions.merge());
                }
                Intent i = new Intent(HostTravelerRequestsActivity.this, StatusAdminActivity.class);
                i.putExtra("status_admin", "approve_user");
                startActivity(i);
            }
        });
    }

    public void DenyUser() {
        Constants.showProgress(HostTravelerRequestsActivity.this);
        CollectionReference complaintsRef = rootRef.collection(FirebaseViewModel.USERS_COLLECTION);
        complaintsRef.whereEqualTo(FirebaseViewModel.IDUserOrder, id_user).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Constants.hideProgress();
                for (QueryDocumentSnapshot document : task.getResult()) {
                    Map<Object, String> map = new HashMap<>();
                    map.put(FirebaseViewModel.IsEnabled, "2");
                    complaintsRef.document(document.getId()).set(map, SetOptions.merge());
                }
                Intent i2 = new Intent(HostTravelerRequestsActivity.this, StatusAdminActivity.class);
                i2.putExtra("status_admin", "deny_user");
                startActivity(i2);
            }
        });
    }

}