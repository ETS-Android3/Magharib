package com.app.magharib.Traveler;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.app.magharib.R;

public class QuestionnaireActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView mImageBack, mImageQuestionnaire1, mImageQuestionnaire2, mImageQuestionnaire3, mImageQuestionnaire4;
    private FrameLayout mFrameQuestionnaire1, mFrameQuestionnaire2, mFrameQuestionnaire3, mFrameQuestionnaire4;
    private ImageButton mBtnNext;

    private String type  = "mountains";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questionnare);

        intiViews();


    }

    // عشان اقدر اتحم بالعناصر الموجودة ف ال Activity و افعل الضغطة مثلا على الصورة
    private void intiViews() {
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
                type  = "mountains";
                mFrameQuestionnaire1.setBackgroundResource(R.drawable.shape_image_questionnaire);
                mFrameQuestionnaire2.setBackgroundResource(R.color.white);
                mFrameQuestionnaire3.setBackgroundResource(R.color.white);
                mFrameQuestionnaire4.setBackgroundResource(R.color.white);
                break;
            case R.id.image_questionnaire2:
                type  = "rocks";
                mFrameQuestionnaire2.setBackgroundResource(R.drawable.shape_image_questionnaire);
                mFrameQuestionnaire1.setBackgroundResource(R.color.white);
                mFrameQuestionnaire3.setBackgroundResource(R.color.white);
                mFrameQuestionnaire4.setBackgroundResource(R.color.white);
                break;
            case R.id.image_questionnaire3:
                type  = "city";
                mFrameQuestionnaire3.setBackgroundResource(R.drawable.shape_image_questionnaire);
                mFrameQuestionnaire1.setBackgroundResource(R.color.white);
                mFrameQuestionnaire2.setBackgroundResource(R.color.white);
                mFrameQuestionnaire4.setBackgroundResource(R.color.white);
                break;
            case R.id.image_questionnaire4:
                type  = "beach";
                mFrameQuestionnaire4.setBackgroundResource(R.drawable.shape_image_questionnaire);
                mFrameQuestionnaire1.setBackgroundColor(getResources().getColor(R.color.transparent));
                mFrameQuestionnaire2.setBackgroundColor(getResources().getColor(R.color.transparent));
                mFrameQuestionnaire3.setBackgroundColor(getResources().getColor(R.color.transparent));
                break;

            case R.id.btn_next :
                // الانتقال الي واجهة الاختبار الثانية و اخد القيمة الي اختارها و ارسالها الي الواجهة المراد الانتقال لها لعرض الصور الخاصة بالقيمة او الصورة التي اختارها
                Intent intent = new Intent(QuestionnaireActivity.this,Questionnaire2Activity.class);
                if (type.equals("mountains")){
                    intent.putExtra("type","mountains");
                }else if (type.equals("rocks")){
                    intent.putExtra("type","rocks");
                }else if (type.equals("city")){
                    intent.putExtra("type","city");
                }else if (type.equals("beach")){
                    intent.putExtra("type","beach");
                }
                startActivity(intent);
                break;
        }
    }
}