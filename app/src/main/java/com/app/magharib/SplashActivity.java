package com.app.magharib;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import com.app.magharib.Admin.MainAdminActivity;
import com.app.magharib.Firebase.FirebaseViewModel;
import com.app.magharib.Host.MainHostActivity;
import com.app.magharib.Traveler.MainTravelerActivity;

public class SplashActivity extends AppCompatActivity {

    private static int SPLASH_TIME_OUT = 2000;

    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);


        // عداد ثانيتين
        new Handler().postDelayed(() -> {

            CheckLogin();

        }, SPLASH_TIME_OUT);

    }

    // sharedPreferences جلب القيم من ال
    //و يتم فحص اذا كان في قيمة موجودة يتم الفحص نوع الحساب اذا كان ادمن او هوست او ترفيلر و يتم التوجيه الي الوجهات الخاصة بهم و اذا لم يكن في قيمة يتم التوجيه الي واجهة الاختيار اما تسجيل دخول او زائر او تسجيل جديد
    private void CheckLogin() {

        sharedPreferences = getSharedPreferences(FirebaseViewModel.SHARED_PREFERENCE_NAME, MODE_PRIVATE);
        if (sharedPreferences.contains(FirebaseViewModel.IS_LOGIN)) {
            if (sharedPreferences.getInt(FirebaseViewModel.IS_LOGIN, -1) == 0) {
                startActivity(new Intent(SplashActivity.this, WayToSignActivity.class));
                finish();
            } else if (sharedPreferences.getString(FirebaseViewModel.Preference_AccountType, "").equals("host")) {
                startActivity(new Intent(SplashActivity.this, MainHostActivity.class));
                finish();
            } else if (sharedPreferences.getString(FirebaseViewModel.Preference_AccountType, "").equals("traveler")) {
                startActivity(new Intent(SplashActivity.this, MainTravelerActivity.class));
                finish();
            } else {
                startActivity(new Intent(SplashActivity.this, MainAdminActivity.class));
                finish();
            }
        } else {
            startActivity(new Intent(SplashActivity.this, WayToSignActivity.class));
            finish();
        }


    }


}