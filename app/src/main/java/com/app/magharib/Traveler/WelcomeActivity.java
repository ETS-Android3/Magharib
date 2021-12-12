package com.app.magharib.Traveler;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.app.magharib.Adapter.WelcomeAdapter;
import com.app.magharib.Model.Welcome;
import com.app.magharib.R;

import java.util.ArrayList;
import java.util.List;

import me.relex.circleindicator.CircleIndicator;

public class WelcomeActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView mTvSkip;
    private ViewPager mPager;
    private CircleIndicator mIndicator;

    private WelcomeAdapter welcomeAdapter;
    private List<Welcome> welcomeList ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        intiViews();


    }

    // عشان اقدر اتحم بالعناصر الموجودة ف ال Activity و افعل الضغطة مثلا على الزر
    private void intiViews() {

        welcomeList = new ArrayList<>();

        mTvSkip = findViewById(R.id.tvSkip);
        mPager = findViewById(R.id.pager);
        mIndicator = findViewById(R.id.indicator);

        mTvSkip.setOnClickListener(this);

        welcomeList.add(new Welcome(R.drawable.image_welcome1, "PLAN", "Get more clarity on your visit with an easy to use layout"));
        welcomeList.add(new Welcome(R.drawable.image_welcome2, "Explore", "The choices in the application help you exploring different parts of Saudi !"));
        welcomeList.add(new Welcome(R.drawable.image_welcome3, "Connect", "Experience new things and get connected with new people !"));

        // عرض واجهة الترجبية الخاصة بالترفيلر عن طريق ال adapter
        welcomeAdapter = new WelcomeAdapter(welcomeList, WelcomeActivity.this);
        mPager.setAdapter(welcomeAdapter);
        mIndicator.setViewPager(mPager);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tvSkip :
                startActivity(new Intent(WelcomeActivity.this , TakeQuestionnaireActivity.class));
                break;
        }
    }
}