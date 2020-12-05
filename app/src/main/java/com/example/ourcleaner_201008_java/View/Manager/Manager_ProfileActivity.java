package com.example.ourcleaner_201008_java.View.Manager;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.example.ourcleaner_201008_java.R;

public class Manager_ProfileActivity extends AppCompatActivity {

    private static final String TAG = "매니저용프로필화면";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager__profile);
        Log.d(TAG, "=== onCreate ===" );



    }
}