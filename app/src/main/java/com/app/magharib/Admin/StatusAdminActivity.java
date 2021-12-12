package com.app.magharib.Admin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.app.magharib.R;

public class StatusAdminActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView mImageCheck, mImageBack;
    private TextView mTvStatus;
    private String delete_comment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status_admin);


        intiViews();

        // استقبال القيمة الخاصة بالحالة التي قام بها الادمن
        delete_comment = getIntent().getExtras().getString("status_admin");

        if (delete_comment.equals("delete_comment")) {
            mTvStatus.setText(R.string.txt_delete_comment);
        } else if (delete_comment.equals("disable_user")) {
            mTvStatus.setText(R.string.txt_disable_user);
        } else if (delete_comment.equals("approve_user")) {
            mTvStatus.setText(R.string.txt_approve_user);
        } else if (delete_comment.equals("deny_user")) {
            mTvStatus.setText(R.string.txt_deny_user);
            mImageCheck.setImageResource(R.drawable.image_chack_dis);
        }

    }

    private void intiViews() {
        mTvStatus = findViewById(R.id.tv_status);
        mImageCheck = findViewById(R.id.image_check);
        mImageBack = findViewById(R.id.image_back);

        mImageBack.setOnClickListener(this);

    }


    @Override
    public void onBackPressed() {
        Intent i = new Intent(getApplicationContext(), MainAdminActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
        finish();
        super.onBackPressed();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.image_back:
                onBackPressed();
                break;
        }
    }
}