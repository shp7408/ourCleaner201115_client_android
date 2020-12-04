package com.example.ourcleaner_201008_java.View.Manager;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.ourcleaner_201008_java.R;

public class Manager_SigninGuide1Activity extends AppCompatActivity {

    private static final String TAG = "매니저회원가입가이드1";

    Button nextBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager__signin_guide1);
        Log.d(TAG, "=== onCreate ===" );

        nextBtn = (Button) findViewById(R.id.nextBtn);
        nextBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Log.d(TAG, "=== nextBtn 클릭 : === ");

                //있으면 넘어감
                Intent intent = new Intent(getApplicationContext(), Manager_SigninGuide2Activity.class);
                startActivity(intent);

                //finish();

            }

        });
    }
}