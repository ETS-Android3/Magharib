package com.app.magharib.Host;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListPopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.app.magharib.Adapter.ListPopupAdapter;
import com.app.magharib.Firebase.FirebaseViewModel;
import com.app.magharib.Model.DataLocation;
import com.app.magharib.Model.Experience;
import com.app.magharib.R;
import com.app.magharib.Utils.Constants;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UpdateExperienecDetailsActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText mEdNameExperience, mEdPriceExperience, mEdDetailsExperience, mEdEmailExperience, mEdPhoneExperience;
    private TextView mTvUploadImage, mTvLocationExperience;
    private ImageView mImageBack, mImageExperience;
    private Button mBtnSubmit;

    private final int RC_READ_STORAGE_PERMISSION = 102;
    private final int RC_PICK_IMG = 200;

    private String first_name ,last_name ,type_experience, location_experience;
    private Uri downloadUrl;
    private Bitmap bitmap_image = null;


    private List<DataLocation> dataLocations;
    private ListPopupAdapter listPopupAdapter;
    private ListPopupWindow listPopupWindow;


    private SharedPreferences sharedPreferences;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseStorage storage;


    private Experience data_experience ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_experienec_details);

        intiViews();


    }

    // عشان اقدر اتحم بالعناصر الموجودة ف ال Activity و افعل الضغطة مثلا على الزر
    private void intiViews() {

        dataLocations = new ArrayList<>();
        storage = FirebaseStorage.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        sharedPreferences = getSharedPreferences(FirebaseViewModel.SHARED_PREFERENCE_NAME, MODE_PRIVATE);

        // استدعاء القيم المخزنة داخل ال sharedPreferences
        first_name = sharedPreferences.getString(FirebaseViewModel.Preference_First_NAME, "");
        last_name = sharedPreferences.getString(FirebaseViewModel.Preference_Last_NAME, "");

        // استقبال اوبجكت ال experience
        data_experience = (Experience) getIntent().getSerializableExtra("data_experience");

        mImageBack = findViewById(R.id.image_back);
        mBtnSubmit = findViewById(R.id.btn_submit);
        mImageExperience = findViewById(R.id.image_experience);
        mTvUploadImage = findViewById(R.id.tv_upload_image);
        mEdNameExperience = findViewById(R.id.ed_name_experience);
        mTvLocationExperience = findViewById(R.id.tv_location_experience);
        mEdPriceExperience = findViewById(R.id.ed_price_experience);
        mEdDetailsExperience = findViewById(R.id.ed_details_experience);
        mEdEmailExperience = findViewById(R.id.ed_email_experience);
        mEdPhoneExperience = findViewById(R.id.ed_phone_experience);


        mImageBack.setOnClickListener(this);
        mBtnSubmit.setOnClickListener(this);
        mTvUploadImage.setOnClickListener(this);


        // عرض البيانات الخاصة في ال Experience
        Glide.with(UpdateExperienecDetailsActivity.this).load(data_experience.getImage_uri_experience()).into(mImageExperience);
        mEdNameExperience.setText(data_experience.getName_experience());
        mTvLocationExperience.setText(data_experience.getLocation_experience());
        mEdPriceExperience.setText(data_experience.getPrice_experience());
        mEdDetailsExperience.setText(data_experience.getDetails_experience());
        mEdEmailExperience.setText(data_experience.getEmail_experience());
        mEdPhoneExperience.setText(data_experience.getPhone_experience());

        mBtnSubmit.setText("Update");

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.image_back:
                onBackPressed();
                break;
            case R.id.tv_upload_image:
                // اخذ برمشن للوصول الي الاستديو و الكتابة و القراءة منه
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                        ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, RC_READ_STORAGE_PERMISSION);
                    return;
                }

                // الانتقال الي الاستديو الخاص بالجهاز بعد اخذ البرمشن الخاص به
                Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryIntent, RC_PICK_IMG);
                break;
            case R.id.btn_submit:
                if (validation()) {
                    Constants.showProgress(UpdateExperienecDetailsActivity.this);
                    if (bitmap_image == null){
                        UpdateExperience(data_experience.getImage_uri_experience());
                    }else {
                        uploadImage(bitmap_image);
                    }

                }
                break;

        }
    }

    // هذه الدالة تقوم بقحص جميع المدخلات اذا موجودة او لا
    private boolean validation() {

    if (!Constants.ValidationEmptyInput(mEdNameExperience)) {
            Toast.makeText(this, "Please Enter The Name", Toast.LENGTH_SHORT).show();
            return false;

        } else if (mTvLocationExperience.getText().toString().equals("")) {
            Toast.makeText(this, "Please Choose The Location", Toast.LENGTH_SHORT).show();
            return false;

        } else if (!Constants.ValidationEmptyInput(mEdPriceExperience)) {
            Toast.makeText(this, "Please Enter The Price", Toast.LENGTH_SHORT).show();
            return false;

        } else if (!Constants.ValidationEmptyInput(mEdDetailsExperience)) {
            Toast.makeText(this, "Please Enter Details", Toast.LENGTH_SHORT).show();
            return false;

        } else if (!Constants.ValidationEmptyInput(mEdEmailExperience)) {
            Toast.makeText(this, "Please Enter Email", Toast.LENGTH_SHORT).show();
            return false;

        } else if (!Constants.isValidEmail(mEdEmailExperience.getText().toString())) {
            Toast.makeText(this, "Please Write a Valid Email Format", Toast.LENGTH_SHORT).show();
            return false;

        } else if (!Constants.ValidationEmptyInput(mEdPhoneExperience)) {
            Toast.makeText(this, "Please Enter The Phone", Toast.LENGTH_SHORT).show();
            return false;

        } else if (mEdPhoneExperience.getText().toString().length() < 9) {
            Toast.makeText(this, "Please Enter a Phone Number More Than 9 Digits", Toast.LENGTH_SHORT).show();
            return false;

        } else {
            return true;

        }
    }


    // عرض اللوكيشن او المواقع المتاحة
    public void showListPopup(View anchorView) {
        listPopupWindow = new ListPopupWindow(this);
        listPopupWindow.setAnchorView(anchorView);

        // اضافة المواقع المتاحة الي ال arrayList
        dataLocations.clear();
        dataLocations.add(new DataLocation("Riyadh"));
        dataLocations.add(new DataLocation("Jeddah"));
        dataLocations.add(new DataLocation("Dammam"));
        dataLocations.add(new DataLocation("King Abdullah Economic City"));
        dataLocations.add(new DataLocation("Abha"));
        dataLocations.add(new DataLocation("Yanbu"));
        dataLocations.add(new DataLocation("Taif"));
        dataLocations.add(new DataLocation("Al khobar"));
        dataLocations.add(new DataLocation("AlUla"));

        // عرض اللوكيشن او المواقع المتاحة
        listPopupAdapter = new ListPopupAdapter(UpdateExperienecDetailsActivity.this, dataLocations);
        listPopupWindow.setAdapter(listPopupAdapter);
        listPopupWindow.setWidth(ListPopupWindow.WRAP_CONTENT);
        listPopupWindow.setHeight(350);

        // الضغط على لوكيشن محدد و اخذ القيمة التي قام باختيارها
        listPopupAdapter.setOnItemClickListener((parent, view, position) -> {

            listPopupWindow.dismiss();

            DataLocation data = (DataLocation) parent;

            // اخذ اللوكيشن عند الاختيار و عرضه في ال textview
            location_experience = data.getCity_name();
            mTvLocationExperience.setText(location_experience);


        });

        listPopupWindow.show();


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_PICK_IMG && data != null) {
            try {

                // القيمة المرجعة عند اختيار الصورة من الاستديو الخاص بالجهاز
                bitmap_image = MediaStore.Images.Media.getBitmap(this.getContentResolver(), data.getData());
                // عرض الصورة المختارة من الاستديو
                mImageExperience.setImageBitmap(bitmap_image);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }


    // رفع الصورة على ال FirebaseStorage و من ثم اخذر رابط الصورة و تعديله داخل ال FirebaseFirestore
    private void uploadImage(Bitmap bitmap) {
        StorageReference storageRef = storage.getReferenceFromUrl("gs://magharib-9a689.appspot.com/");
        StorageReference mountainsRef = storageRef.child("images/" + System.currentTimeMillis() + ".jpg");

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data1 = baos.toByteArray();

        UploadTask uploadTask = mountainsRef.putBytes(data1);
        uploadTask.addOnFailureListener(exception -> {
            Log.e("taskSnapshot", exception.getMessage());

        }).addOnSuccessListener(taskSnapshot -> {
            // FirebaseStorage اذا نجحت العملية يتم اخذ رابط الصورة من الفايربيس و تخزينة داخل ال
            Task<Uri> urlTask = taskSnapshot.getStorage().getDownloadUrl();
            while (!urlTask.isSuccessful()) ;
            downloadUrl = urlTask.getResult();
            if (!downloadUrl.toString().equals("")) {
                // دالة تعديل ال Experience
                UpdateExperience(String.valueOf(downloadUrl));
            }
            Log.e("taskSnapshot", downloadUrl.toString());
        });
    }

    public void UpdateExperience(String image_uri) {
        CollectionReference complaintsRef = firebaseFirestore.collection("Experience");
        complaintsRef.whereEqualTo("id_experience", data_experience.getId_experience()).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Constants.hideProgress();
                for (QueryDocumentSnapshot document : task.getResult()) {
                    Map<Object, String> map = new HashMap<>();
                    map.put("user_id",sharedPreferences.getString(FirebaseViewModel.Preference_ID_USER, ""));
                    map.put("first_name",first_name);
                    map.put("last_name",last_name);
                    map.put("type_experience", data_experience.getType_experience());
                    map.put("image_uri_experience", image_uri);
                    map.put("name_experience", mEdNameExperience.getText().toString());
                    map.put("location_experience",location_experience);
                    map.put("price_experience",mEdPriceExperience.getText().toString());
                    map.put("details_experience",mEdDetailsExperience.getText().toString());
                    map.put("email_experience",mEdEmailExperience.getText().toString());
                    map.put("phone_experience",mEdPhoneExperience.getText().toString());
                    map.put("is_enabled","1");
                    complaintsRef.document(document.getId()).set(map, SetOptions.merge());
                }

                Toast.makeText(this, "Editing Completed Successfully", Toast.LENGTH_SHORT).show();

                Intent i = new Intent(getApplicationContext(), MainHostActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
                finish();
            }
        });


    }





}