package com.app.magharib;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.MutableLiveData;

import com.app.magharib.Firebase.FirebaseViewModel;
import com.app.magharib.Model.User;
import com.app.magharib.Traveler.QuestionnaireActivity;
import com.app.magharib.Traveler.WelcomeActivity;
import com.app.magharib.Utils.Constants;

import java.util.Random;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText mEdFirstName, mEdLastName, mEdAge, mEdEmail, mEdPassword, mEdConfirmPassword;
    private RadioButton mRbTraveler, mRbHost;
    private CheckBox mCbAgree;
    private ImageView mImageBack;
    private Button mBtnSignUp;

    private String account_type = "host";
    private FirebaseViewModel firebaseViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        intiViews();

    }

    // عشان اقدر اتحم بالعناصر الموجودة ف ال Activity و افعل الضغطة مثلا على الزر
    private void intiViews() {
        firebaseViewModel = new FirebaseViewModel(SignUpActivity.this);

        mImageBack = findViewById(R.id.image_back);
        mBtnSignUp = findViewById(R.id.btn_sign_up);
        mEdFirstName = findViewById(R.id.ed_first_name);
        mEdLastName = findViewById(R.id.ed_last_name);
        mEdAge = findViewById(R.id.ed_age);
        mEdEmail = findViewById(R.id.ed_email);
        mEdPassword = findViewById(R.id.ed_password);
        mEdConfirmPassword = findViewById(R.id.ed_confirm_password);
        mRbTraveler = findViewById(R.id.rb_traveler);
        mRbHost = findViewById(R.id.rb_host);
        mCbAgree = findViewById(R.id.cb_agree);

        mImageBack.setOnClickListener(this);
        mBtnSignUp.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.image_back:
                onBackPressed();
                break;
            case R.id.btn_sign_up:
                if (validation()) {

                    // يتم فحص نوع الحساب بناء على الريدو بوتن الفعال
                    if (mRbHost.isChecked()) {
                        account_type = "host";
                    } else if (mRbTraveler.isChecked()) {
                        account_type = "traveler";
                    }

                    // اخد القيم من ال EditText
                    String first_name = mEdFirstName.getText().toString();
                    String last_name = mEdLastName.getText().toString();
                    String age = mEdAge.getText().toString();
                    String email = mEdEmail.getText().toString();
                    String password = mEdPassword.getText().toString();

                    // عمل id
                    Random rand = new Random();
                    int id_user_order = rand.nextInt(1000000);

                    // ادخال الداتا داخل ال كونستركتور الخاص باليوزر
                    User user = new User(first_name, last_name, age, email, password, account_type, "0", String.valueOf(id_user_order));
                    Constants.showProgress(SignUpActivity.this);

                    //فحص اذا كان الايميل موجود او لا داخل الفايربيس
                    MutableLiveData<Boolean> userEmailValidationLiveData = firebaseViewModel.isUserEmailAlreadyExist(user.getEmail());
                    userEmailValidationLiveData.observe(this, aBoolean -> {
                        // اذا كان موجود في الفاييربيس بيطلع توست انو الايميل موجود
                        if (aBoolean) {
                            Toast.makeText(SignUpActivity.this, "Email Already Exists, Please Choose Another Email .", Toast.LENGTH_SHORT).show();
                            Constants.hideProgress();
                        } else {
                            // اذا غير موجود بفحص اذا العملية نجحت بيسجل البيانات في جدول يوزر داخل الفايربيس
                            MutableLiveData<String> liveData = firebaseViewModel.SignUp(user);
                            liveData.observe(SignUpActivity.this, s -> {
                                if (!s.equalsIgnoreCase(FirebaseViewModel.OPERATION_SUCCESS)) {

                                    Toast.makeText(SignUpActivity.this, s, Toast.LENGTH_SHORT).show();
                                    Constants.hideProgress();
                                } else {
                                    Constants.hideProgress();
                                    // بيتم الفحص اذا اختار هوست بيروح على تسجيل الدخول و اذا اختار ترفيلر بيروح على الواجهة الترحبية
                                    Toast.makeText(this, "You have been successfully registered, waiting for admin approval", Toast.LENGTH_SHORT).show();
                                    if (mRbHost.isChecked()) {
                                        finish();
                                    } else if (mRbTraveler.isChecked()) {
                                        startActivity(new Intent(SignUpActivity.this , WelcomeActivity.class));
                                    }


                                }
                            });
                        }
                    });
                }
                break;
        }
    }

    // هذه الدالة تقوم بقحص جميع المدخلات اذا موجودة او لا
    private boolean validation() {
        if (!Constants.ValidationEmptyInput(mEdFirstName)) {
            Toast.makeText(this, "Please Enter The First Name ..", Toast.LENGTH_SHORT).show();
            return false;

        } else if (!Constants.ValidationEmptyInput(mEdLastName)) {
            Toast.makeText(this, "Please Enter The Last Name ..", Toast.LENGTH_SHORT).show();
            return false;

        } else if (!Constants.ValidationEmptyInput(mEdAge)) {
            Toast.makeText(this, "Please Enter Age ..", Toast.LENGTH_SHORT).show();
            return false;

        } else if (!Constants.ValidationEmptyInput(mEdEmail)) {
            Toast.makeText(this, "Please Enter Your Email ..", Toast.LENGTH_SHORT).show();
            return false;

        } else if (!Constants.isValidEmail(mEdEmail.getText().toString())) {
            Toast.makeText(this, "Please Write a Valid Email Format ..", Toast.LENGTH_SHORT).show();
            return false;

        } else if (!Constants.ValidationEmptyInput(mEdPassword)) {
            Toast.makeText(this, "Please Enter The Password ..", Toast.LENGTH_SHORT).show();
            return false;

        } else if (mEdPassword.getText().toString().length() < 6) {
            Toast.makeText(this, "Please Enter a Password More Than 6 Digits ..", Toast.LENGTH_SHORT).show();
            return false;

        } else if (!Constants.isValidPassword(mEdPassword.getText().toString())) {
            Toast.makeText(this, "Please enter a strong password containing numbers, letters and symbols", Toast.LENGTH_SHORT).show();
            return false;

        }else if (!Constants.ValidationEmptyInput(mEdConfirmPassword)) {
            Toast.makeText(this, "Please Enter Confirm Password ..", Toast.LENGTH_SHORT).show();
            return false;

        } else if (!mEdPassword.getText().toString().equalsIgnoreCase(mEdConfirmPassword.getText().toString())) {
            Toast.makeText(this, "Password Does Not Match ..", Toast.LENGTH_SHORT).show();

            return false;
        } else if (!mCbAgree.isChecked()) {
            Toast.makeText(this, "Please Agree To The Terms And Conditions", Toast.LENGTH_SHORT).show();

            return false;
        } else {
            return true;

        }
    }

}