package com.app.magharib.Firebase;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.app.magharib.Model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class FirebaseViewModel {

    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore db;
    private FirebaseUser currentUser;


    // اسماء الحقول جدول اليوزر و اسم الجدول
    public static final String USERS_COLLECTION = "User";
    public static final String USER_FirstName = "first_name";
    public static final String USER_LastName = "last_name";
    public static final String USER_AGE = "age";
    public static final String USER_PASSWORD = "password";
    public static final String USER_EMAIL = "email";
    public static final String AccountType = "account_type";
    public static final String IsEnabled = "is_enabled";
    public static final String IDUserOrder = "id_user_order";


    public static final String OPERATION_SUCCESS = "The operation succeeded .";
    public static final String Not_Enabled = "The account is inactive, waiting for admin approval";
    public static final String DenyAccount = "This account is rejected by the admin";
    public static final String INVALID_PASSWORD = "The password is wrong ..";
    public static final String NOT_FOUND_ACCOUNT = "Account not found ..";
    public static final String AccountTypeHost = "Successfully logged in as HOST";
    public static final String AccountTypeTraveler = "Successfully logged in as Traveler";
    public static final String AccountTypeAdmin = "Successfully logged in as Admin";
    public static final String EMAIL_SENT = "Email sent, check email inbox";


    // sharedPreference اسماء ال key الخاص بحفظ البيانات داخل ال
    public static final String SHARED_PREFERENCE_NAME = "com.app.magharib.sharedPreference";
    public static final String IS_LOGIN = "sharedPreference.isLogin";
    public static final String Preference_ID_USER = "sharedPreference.id_user";
    public static final String Preference_First_NAME = "sharedPreference.first_name";
    public static final String Preference_Last_NAME = "sharedPreference.last_name";
    public static final String Preference_Age = "sharedPreference.age";
    public static final String Preference_Email = "sharedPreference.email";
    public static final String Preference_AccountType = "sharedPreference.account_type";
    public static final String Preference_IDUserOrder = "sharedPreference.id_userOrder";

    private SharedPreferences sharedPreferences;

    public FirebaseViewModel(Context context) {
        firebaseAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE);
    }

    // دالة بتتأكد اذا الايميل موجود داخل الفايربيس أو لا
    public MutableLiveData<Boolean> isUserEmailAlreadyExist(String userEmail) {

        MutableLiveData<Boolean> liveData = new MutableLiveData<>();
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        db.collection(USERS_COLLECTION)
                .whereEqualTo(USER_EMAIL, userEmail)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        if (!task.getResult().isEmpty()) {
                            liveData.setValue(true);
                        } else {
                            liveData.setValue(false);

                        }

                    }
                });
        return liveData;
    }

    // دالة تسجيل حساب جديد
    public MutableLiveData<String> SignUp(User user) {
        final MutableLiveData<String> liveData = new MutableLiveData<>();
        firebaseAuth.createUserWithEmailAndPassword(user.getEmail(), user.getPassword()).addOnCompleteListener(task -> {
            currentUser = FirebaseAuth.getInstance().getCurrentUser();
            if (currentUser != null) {
                db.collection(USERS_COLLECTION).document(currentUser.getUid()).set(user).addOnCompleteListener(task1 -> {
                    liveData.setValue(OPERATION_SUCCESS);
                }).addOnFailureListener(e -> {
                    liveData.setValue(e.getMessage());

                    Log.e("error ", e.getMessage() + "===============1");
                    e.printStackTrace();

                });
            }

        }).addOnFailureListener(e -> {
            liveData.setValue(e.getMessage());
            e.printStackTrace();

        });
        return liveData;

    }

    // دالة تسجيل الدخول
    public MutableLiveData<String> Login(String email, final String password) {

        final MutableLiveData<String> liveData = new MutableLiveData<>();
        db.collection(USERS_COLLECTION)
                .whereEqualTo(USER_EMAIL, email)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        if (!task.getResult().isEmpty()) {
                            for (QueryDocumentSnapshot doc : task.getResult()) {
                                if (doc.get(IsEnabled).equals("0")) {
                                    liveData.setValue(Not_Enabled);
                                } else if (doc.get(IsEnabled).equals("1")) {
                                    firebaseAuth.signInWithEmailAndPassword((String) (doc.get(USER_EMAIL)), password).addOnCompleteListener(task1 -> {
                                        currentUser = FirebaseAuth.getInstance().getCurrentUser();
                                        if (task1.isSuccessful()) {
                                            liveData.setValue(OPERATION_SUCCESS);

                                            Log.e("AccountType", String.valueOf(doc.get(IDUserOrder)));


                                            if (currentUser != null) {
                                                db.collection(USERS_COLLECTION).document(currentUser.getUid()).update(USER_PASSWORD, password);
                                            }
                                            // تخزين بيانات اليوزر داخل ال sharedPreferences
                                            writeOnPreference(currentUser.getUid(), 1, (String) (doc.get(USER_FirstName)), (String) (doc.get(USER_LastName)), (String) (doc.get(USER_AGE)), (String) (doc.get(USER_EMAIL)), (String) (doc.get(AccountType)), (String) (doc.get(IDUserOrder)));


                                        } else {
                                            liveData.setValue(INVALID_PASSWORD);
                                        }
                                    });

                                } else if (doc.get(IsEnabled).equals("2")) {
                                    liveData.setValue(DenyAccount);
                                }
                            }
                        } else {

                            liveData.setValue(NOT_FOUND_ACCOUNT);

                        }
                    }
                }).addOnFailureListener(e -> {
            liveData.setValue(e.getMessage());
        });
        return liveData;

    }

    // دالة جلب نوع الحساب
    public MutableLiveData<String> AccountType(String email) {

        final MutableLiveData<String> liveData = new MutableLiveData<>();
        db.collection(USERS_COLLECTION)
                .whereEqualTo(USER_EMAIL, email)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        if (!task.getResult().isEmpty()) {
                            for (QueryDocumentSnapshot doc : task.getResult()) {
                                if (String.valueOf(doc.get(AccountType)).equals("host")) {
                                    liveData.setValue(AccountTypeHost);
                                } else if (String.valueOf(doc.get(AccountType)).equals("traveler")) {
                                    liveData.setValue(AccountTypeTraveler);
                                } else if (String.valueOf(doc.get(AccountType)).equals("admin")) {
                                    liveData.setValue(AccountTypeAdmin);
                                }
                                Log.e("AccountType", String.valueOf(doc.get(AccountType)));


                            }
                        }
                    }
                }).addOnFailureListener(e -> {
            liveData.setValue(e.getMessage());
        });
        return liveData;

    }

    public MutableLiveData<String> forgetPassword(String email) {
        final MutableLiveData<String> liveData = new MutableLiveData<>();
        firebaseAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        liveData.setValue(EMAIL_SENT);
                    }
                }).addOnFailureListener(e -> {
            liveData.setValue(e.getMessage());
        });
        return liveData;
    }

    // دالة فحص اذا كان اليوزر عامل ابلاغ ل يوزر اخر على حسب ال id المرسل له
    public MutableLiveData<Boolean> isUserReport(String id_user,String id_user_report ) {
        final MutableLiveData<Boolean> liveDataa = new MutableLiveData<>();
        db.collection("Report")
                .whereEqualTo("id_user", id_user)
                .whereEqualTo("id_user_report", id_user_report)
                .get().addOnCompleteListener(task -> {
                    if (task.isSuccessful()){
                        if (!task.getResult().isEmpty()) {
                            liveDataa.setValue(true);
                        } else {
                            liveDataa.setValue(false);
                        }
                    }
                });



        return liveDataa;
    }


    // دالة حفظ البيانات الخاصة باليوزر داخل ال sharedPreference
    public void writeOnPreference(String id_user, int isLogin, String first_name, String last_name, String age, String email, String account_type, String id_user_order) {
        sharedPreferences.edit().putInt(IS_LOGIN, isLogin)
                .putString(Preference_First_NAME, first_name)
                .putString(Preference_Last_NAME, last_name)
                .putString(Preference_Age, age)
                .putString(Preference_Email, email)
                .putString(Preference_ID_USER, id_user)
                .putString(Preference_AccountType, account_type)
                .putString(Preference_IDUserOrder, id_user_order)
                .apply();

    }

    // دالة حذف البيانات من داخل ال sharedPreference
    public void resetPreference() {
        sharedPreferences.edit().clear().apply();
    }

}
