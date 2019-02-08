package com.example.andriodlabs;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {
private static final String USER_PREFS_NAME="filename";
private static final String USER_EMAIL="email";
    private EditText emailTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_linear);

        //setContentView(R.layout.activity_main);
        //setContentView(R.layout.activity_main_grid);
      //setContentView(R.layout.activity_main_linear);
     // setContentView(R.layout.activity_main_relative);
        SharedPreferences prefs = getSharedPreferences( "student", MODE_PRIVATE);
        String restoredText = prefs.getString(USER_EMAIL, "");

        emailTextView=findViewById(R.id.editText2);
        if (restoredText != null) {
            if (emailTextView != null) {
                emailTextView.setText(restoredText);
            }
        }

        Button button=findViewById(R.id.LoginButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, ProfileActivity.class);
                if (emailTextView != null) {
                    String emailText = emailTextView.getText().toString();
                    intent.putExtra("email", emailText);
                }
                startActivity(intent);
            }
        });
    }


    @Override
    protected void onPause() {
        super.onPause();

        emailTextView = findViewById(R.id.editText2);
        if (emailTextView != null) {
            String emailText = emailTextView.getText().toString();
            SharedPreferences.Editor editor = getSharedPreferences(USER_PREFS_NAME, MODE_PRIVATE).edit();
            editor.putString(USER_EMAIL, emailText);
            editor.commit();
        }

    }
}



