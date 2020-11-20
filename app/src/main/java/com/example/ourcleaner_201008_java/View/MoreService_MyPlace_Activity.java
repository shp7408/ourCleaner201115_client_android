package com.example.ourcleaner_201008_java.View;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.example.ourcleaner_201008_java.R;

public class MoreService_MyPlace_Activity extends AppCompatActivity {

    private static final String TAG = "집 부가정보";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_service__my_place_);
        Log.d(TAG, "=== onCreate ===" );
    }
}