package com.app.magharib.Utils;

import android.content.Context;
import android.text.TextUtils;
import android.util.Patterns;
import android.widget.EditText;

import com.app.magharib.Views.AVLoadingIndicatorDialog;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Pattern;

public class Constants {

    // ValidationEmptyInput
    public static boolean ValidationEmptyInput(EditText text) {
        if (TextUtils.isEmpty(text.getText().toString())) {
            return false;
        }
        return true;

    }

    // ValidationEmail true or false
    public static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }

    // strong password
    public static boolean isValidPassword(String s) {
        Pattern PASSWORD_PATTERN
                = Pattern.compile(
                "[a-zA-Z0-9\\!\\@\\#\\$]{8,24}");

        return !TextUtils.isEmpty(s) && PASSWORD_PATTERN.matcher(s).matches();
    }


    private static AVLoadingIndicatorDialog indicatorView;

    // اخفاء الديلوق
    public static void hideProgress() {
        if (indicatorView != null && indicatorView.isShowing()) {
            indicatorView.dismiss();
        }
    }

    // عرض الديلوق
    public static void showProgress(Context context) {
        indicatorView = new AVLoadingIndicatorDialog(context);
        indicatorView.setCancelable(false);
        indicatorView.show();

    }

    // فورمات خاص بالتاريخ
    public static String FormatDate(Date date) {
        if (date == null) {
            return null;
        }
        String dateFormat = "dd/MM/yyyy";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat);
        return simpleDateFormat.format(date);
    }




}
