package com.example.andriodlabs;

import android.content.Intent;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

public class ProfileActivity<ACTIVITY_NAME> extends AppCompatActivity {
    static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final String ACTIVITY_NAME = "PROFILE_ACTIVITY";
    private ImageButton mImageButton = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e(ACTIVITY_NAME, "In function: onCreate");
        setContentView(R.layout.activitylayout);

        final Intent intent = getIntent();
        if (intent != null) {
            String emailText = intent.getStringExtra("email");
            if (!TextUtils.isEmpty(emailText)) {
                EditText emailTextView = findViewById(R.id.editText9);
                emailTextView.setText(emailText);
            }
        }

        mImageButton = findViewById(R.id.imgbtn);
        if (mImageButton != null) {
            mImageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                        startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                    }

                }
            });
        }

        Button chatBt = findViewById(R.id.buttonGotoChat);
        chatBt.setOnClickListener((View) -> {
            Intent intentchat= new Intent(ProfileActivity.this, ChatWindow.class);
            startActivity(intentchat);
        });
        Log.e(ACTIVITY_NAME,"In onCreate()");


        Button weatherForecastBt = findViewById(R.id.buttonGotoWeather);
        weatherForecastBt.setOnClickListener((View) -> {
            Intent intentchat= new Intent(ProfileActivity.this, WeatherForecast.class);
            startActivity(intentchat);
        });
        Log.e(ACTIVITY_NAME,"In onCreate()");


    }
    @Override
    protected void onStart() {
        super.onStart();
        Log.e(ACTIVITY_NAME, "In function: onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e(ACTIVITY_NAME, "In function: onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.e(ACTIVITY_NAME, "In function: onPause");
    }


    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e(ACTIVITY_NAME, "In function: onDestroy");
    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e(ACTIVITY_NAME, "In function: onActivityResult");
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            mImageButton.setImageBitmap(imageBitmap);
        }


    }
}
