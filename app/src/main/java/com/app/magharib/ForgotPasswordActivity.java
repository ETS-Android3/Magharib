package com.app.magharib;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.MutableLiveData;

import com.app.magharib.Firebase.FirebaseViewModel;
import com.app.magharib.Utils.Constants;

public class ForgotPasswordActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView mImageBack;
    private EditText mEbEmail;
    private Button mBtnSend;

    private FirebaseViewModel firebaseViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        intiViews();

    }

    // عشان اقدر اتحم بالعناصر الموجودة ف ال Activity و افعل الضغطة مثلا على الزر
    private void intiViews() {

        firebaseViewModel=new FirebaseViewModel(this);

        mImageBack = findViewById(R.id.image_back);
        mEbEmail = findViewById(R.id.eb_email);
        mBtnSend = findViewById(R.id.btn_send);

        mImageBack.setOnClickListener(this);
        mBtnSend.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.image_back:
                onBackPressed();
                break;
            case R.id.btn_send:
                if (validation()){
                    sendPasswordEmail();
                }
                break;
        }
    }

    // ارسال ايميل ل اعادة كتابة كلمة مرور جديدة
    public void sendPasswordEmail() {
        Constants.showProgress(ForgotPasswordActivity.this);
            MutableLiveData<String> liveData=firebaseViewModel.forgetPassword(mEbEmail.getText().toString());
            liveData.observe(this, s -> {
                if (s.equals(FirebaseViewModel.EMAIL_SENT)){
                    Constants.hideProgress();
                    Toast.makeText(ForgotPasswordActivity.this, s, Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(ForgotPasswordActivity.this,SignInActivity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(i);
                    finish();
                }else {
                    Constants.hideProgress();
                    Toast.makeText(ForgotPasswordActivity.this, s, Toast.LENGTH_SHORT).show();
                }
            });
        }

    // هذه الدالة تقوم بقحص جميع المدخلات اذا موجودة او لا
    private boolean validation() {
        if (!Constants.ValidationEmptyInput(mEbEmail)) {
            Toast.makeText(this, "Please enter your email", Toast.LENGTH_SHORT).show();
            return false;

        } else if (!Constants.isValidEmail(mEbEmail.getText().toString())) {
            Toast.makeText(this, "Please enter a valid email format", Toast.LENGTH_SHORT).show();
            return false;

        } else {
            return true;

        }
    }
}