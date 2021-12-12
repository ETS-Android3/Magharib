package com.app.magharib.Traveler;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

import com.app.magharib.R;
import com.app.magharib.SignInActivity;

public class StartJourneyActivity extends AppCompatActivity implements View.OnClickListener {

    private Button mBtnStartJourney;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_journey);

        intiViews();


    }

    private void intiViews() {
        mBtnStartJourney = findViewById(R.id.btn_start_journey);

        mBtnStartJourney.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_start_journey :
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