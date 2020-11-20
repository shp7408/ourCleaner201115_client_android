package com.example.ourcleaner_201008_java.View;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;

import com.example.ourcleaner_201008_java.R;

public class MoreService_Area_Activity extends AppCompatActivity {

    private static final String TAG = "서비스 범위";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_service__area_);
        Log.d(TAG, "=== onCreate ===" );



    }
}