package com.app.magharib;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.MutableLiveData;
import com.app.magharib.Admin.MainAdminActivity;
import com.app.magharib.Firebase.FirebaseViewModel;
import com.app.magharib.Host.MainHostActivity;
import com.app.magharib.Traveler.MainTravelerActivity;
import com.app.magharib.Utils.Constants;

public class SignInActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText mEbEmail, mEbPassword;
    private TextView mTvForgotPassword ;
    private ImageView mImageBack;
    private Button mBtnSignUp;

    private FirebaseViewModel firebaseViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        intiViews();

    }

    // عشان اقدر اتحم بالعناصر الموجودة ف ال Activity و افعل الضغطة مثلا على الزر
    private void intiViews() {

        firebaseViewModel = new FirebaseViewModel(this);

        mImageBack = findViewById(R.id.image_back);
        mBtnSignUp = findViewById(R.id.btn_sign_up);
        mEbEmail = findViewById(R.id.eb_email);
        mEbPassword = findViewById(R.id.eb_password);
        mTvForgotPassword = findViewById(R.id.tv_forgot_password);

        mImageBack.setOnClickListener(this);
        mBtnSignUp.setOnClickListener(this);
        mTvForgotPassword.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.image_back:
                onBackPressed();
                break;
            case R.id.btn_sign_up:
                if (validation()) {
                    Constants.showProgress(SignInActivity.this);
                    // فحص من خلال دالة ال لوجن اذا كان موجود اليوزر في جدول اليوزر في الفايربيس بيتطلع العملية نجحت
                    MutableLiveData<String> loginLiveData = firebaseViewModel.Login(mEbEmail.getText().toString(), mEbPassword.getText().toString());
                    loginLiveData.observe(this, s -> {
                        if (s.equals(FirebaseViewModel.OPERATION_SUCCESS)) {
                            Constants.hideProgress();
                            // بعد العملية ما نجحت بستدعي دالة نوع الحساب من خلالها بقدر اجيب نوع الحساب سواء كان هوست او ادمن او ترفيلر و عمل توجيه على الواجهة الرئيسية الخاصة فيه
                            MutableLiveData<String> liveData = firebaseViewModel.AccountType(mEbEmail.getText().toString());
                            liveData.observe(SignInActivity.this, s1 -> {

                                Toast.makeText(this, "You Are Logged In Successfully ..", Toast.LENGTH_SHORT).show();
                                // اذا كان هوست بيروح على الواجهة الرئيسية الخاصة بالهوست
                                if (s1.equals(FirebaseViewModel.AccountTypeHost)) {
                                    Intent i = new Intent(getApplicationContext(), MainHostActivity.class);
                                    i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(i);
                                    finish();
                                    // اذا كان ترفيلر بيروح على الواجهة الرئيسية الخاصة بالترفيلر
                                } else if (s1.equals(FirebaseViewModel.AccountTypeTraveler)) {
                                    Intent i = new Intent(getApplicationContext(), MainTravelerActivity.class);
                                    i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(i);
                                    finish();
                                } else {
                                    // غير كده بيروح على الواجهة الرئيسية الخاصة بالادمن
                                    Intent i = new Intent(getApplicationContext(), MainAdminActivity.class);
                                    i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(i);
                                    finish();
                                }
                            });

                        } else {
                            Constants.hideProgress();
                            Toast.makeText(SignInActivity.this, s, Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                break;

            case R.id.tv_forgot_password:
                startActivity(new Intent(SignInActivity.this , ForgotPasswordActivity.class));
                break;
        }
    }


    // هذه الدالة تقوم بقحص جميع المدخلات اذا موجودة او لا
    private boolean validation() {
        if (!Constants.ValidationEmptyInput(mEbEmail)) {
            Toast.makeText(this, "Please Enter Your Email ..", Toast.LENGTH_SHORT).show();
            return false;

        } else if (!Constants.isValidEmail(mEbEmail.getText().toString())) {
            Toast.makeText(this, "Please Write a Valid Email Format ..", Toast.LENGTH_SHORT).show();
            return false;

        } else if (!Constants.ValidationEmptyInput(mEbPassword)) {
            Toast.makeText(this, "Please Enter The Password ..", Toast.LENGTH_SHORT).show();
            return false;

        } else if (mEbPassword.getText().toString().length() < 6) {
            Toast.makeText(this, "Please Enter a Password More Than 6 Digits ..", Toast.LENGTH_SHORT).show();
            return false;

        } else {
            return true;

        }
    }

}