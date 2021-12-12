package com.app.magharib.Views;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;
import com.app.magharib.R;


public class AVLoadingIndicatorDialog extends AlertDialog {

    // كلاس خاص ب ديلوق التحميل
    private TextView mMessageView;
    public AVLoadingIndicatorDialog(Context context) {
        super(context);
        View view = LayoutInflater.from(getContext()).inflate(R.layout.progress_avld, null);
        mMessageView =  view.findViewById(R.id.message);
        setView(view);
    }


    @Override
    public void setMessage(CharSequence message) {
        mMessageView.setText(message);
    }
}