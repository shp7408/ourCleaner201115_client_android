package com.example.ourcleaner_201008_java.View;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.example.ourcleaner_201008_java.R;

public class Service_completeActivity extends AppCompatActivity {

    private static final String TAG = "서비스신청 완료 엑티비티";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_complete);
        Log.d(TAG, "=== onCreate ===" );



    }
}