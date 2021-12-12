package com.app.magharib.Traveler;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.app.magharib.Host.MainHostActivity;
import com.app.magharib.R;
import com.app.magharib.SignInActivity;

public class TakeQuestionnaireActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView mTvSkip;
    private Button mBtnTakeQuestionnare;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_questionnare);

        intiViews();


    }

    private void intiViews() {
        mTvSkip = findViewById(R.id.tvSkip);
        mBtnTakeQuestionnare = findViewById(R.id.btn_take_questionnare);

        mBtnTakeQuestionnare.setOnClickListener(this);
        mTvSkip.setOnClickListener(this);


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_take_questionnare:
                startActivity(new Intent(TakeQuestionnaireActivity.this, QuestionnaireActivity.class));
                break;

            case R.id.tvSkip:
                Intent i = new Intent(getApplicationContext(), SignInActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
                finish();
                break;
        }
    }
}