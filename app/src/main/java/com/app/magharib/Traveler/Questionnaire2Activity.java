package com.app.magharib.Traveler;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.app.magharib.R;

public class Questionnaire2Activity extends AppCompatActivity implements View.OnClickListener {

    private ImageView mImageBack, mImageQuestionnaire1, mImageQuestionnaire2, mImageQuestionnaire3, mImageQuestionnaire4;
    private FrameLayout mFrameQuestionnaire1, mFrameQuestionnaire2, mFrameQuestionnaire3, mFrameQuestionnaire4;
    private ImageButton mBtnNext;

    private String type ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questionnaire2);

        intiViews();

    }

    // عشان اقدر اتحم بالعناصر الموجودة ف ال Activity و افعل الضغطة مثلا على الزر
    private void intiViews() {

        // استقبال القيمة التي تم ارسالها من واجهة الاختبار الاولى
        type = getIntent().getExtras().getString("type");

        mImageBack = findViewById(R.id.image_back);
        mImageQuestionnaire1 = findViewById(R.id.image_questionnaire1);
        mFrameQuestionnaire1 = findViewById(R.id.frame_questionnaire1);
        mImageQuestionnaire2 = findViewById(R.id.image_questionnaire2);
        mFrameQuestionnaire2 = findViewById(R.id.frame_questionnaire2);
        mImageQuestionnaire3 = findViewById(R.id.image_questionnaire3);
        mFrameQuestionnaire3 = findViewById(R.id.frame_questionnaire3);
        mImageQuestionnaire4 = findViewById(R.id.image_questionnaire4);
        mFrameQuestionnaire4 = findViewById(R.id.frame_questionnaire4);
        mBtnNext = findViewById(R.id.btn_next);


        // فحص القيمة التي تم استقبالها و بناء عليها بعطيه الصور الخاصة بالقيمة
        if (type.equals("mountains")){
            mImageQuestionnaire1.setImageResource(R.drawable.a1);
            mImageQuestionnaire2.setImageResource(R.drawable.a2);
            mImageQuestionnaire3.setImageResource(R.drawable.a3);
            mImageQuestionnaire4.setImageResource(R.drawable.a4);
        }else if (type.equals("rocks")){
            mImageQuestionnaire1.setImageResource(R.drawable.b1);
            mImageQuestionnaire2.setImageResource(R.drawable.b2);
            mImageQuestionnaire3.setImageResource(R.drawable.b3);
            mImageQuestionnaire4.setImageResource(R.drawable.b4);
        }else if (type.equals("city")){
            mImageQuestionnaire1.setImageResource(R.drawable.c1);
            mImageQuestionnaire2.setImageResource(R.drawable.c2);
            mImageQuestionnaire3.setImageResource(R.drawable.c3);
            mImageQuestionnaire4.setImageResource(R.drawable.c4);
        }else if (type.equals("beach")){
            mImageQuestionnaire1.setImageResource(R.drawable.image_beach1);
            mImageQuestionnaire2.setImageResource(R.drawable.image_beach2);
            mImageQuestionnaire3.setImageResource(R.drawable.image_beach3);
            mImageQuestionnaire4.setImageResource(R.drawable.image_beach4);
        }

        mImageBack.setOnClickListener(this);
        mImageQuestionnaire1.setOnClickListener(this);
        mImageQuestionnaire2.setOnClickListener(this);
        mImageQuestionnaire3.setOnClickListener(this);
        mImageQuestionnaire4.setOnClickListener(this);
        mBtnNext.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.image_back:
                onBackPressed();
                break;
            case R.id.image_questionnaire1:
                mFrameQuestionnaire1.setBackgroundResource(R.drawable.shape_image_questionnaire);
                mFrameQuestionnaire2.setBackgroundResource(R.color.white);
                mFrameQuestionnaire3.setBackgroundResource(R.color.white);
                mFrameQuestionnaire4.setBackgroundResource(R.color.white);
                break;
            case R.id.image_questionnaire2:
                mFrameQuestionnaire2.setBackgroundResource(R.drawable.shape_image_questionnaire);
                mFrameQuestionnaire1.setBackgroundResource(R.color.white);
                mFrameQuestionnaire3.setBackgroundResource(R.color.white);
                mFrameQuestionnaire4.setBackgroundResource(R.color.white);
                break;
            case R.id.image_questionnaire3:
                mFrameQuestionnaire3.setBackgroundResource(R.drawable.shape_image_questionnaire);
                mFrameQuestionnaire1.setBackgroundResource(R.color.white);
                mFrameQuestionnaire2.setBackgroundResource(R.color.white);
                mFrameQuestionnaire4.setBackgroundResource(R.color.white);
                break;
            case R.id.image_questionnaire4:
                mFrameQuestionnaire4.setBackgroundResource(R.drawable.shape_image_questionnaire);
                mFrameQuestionnaire1.setBackgroundColor(getResources().getColor(R.color.transparent));
                mFrameQuestionnaire2.setBackgroundColor(getResources().getColor(R.color.transparent));
                mFrameQuestionnaire3.setBackgroundColor(getResources().getColor(R.color.transparent));
                break;

            case R.id.btn_next :
                startActivity(new Intent(Questionnaire2Activity.this,StartJourneyActivity.class));
                break;
        }
    }

}