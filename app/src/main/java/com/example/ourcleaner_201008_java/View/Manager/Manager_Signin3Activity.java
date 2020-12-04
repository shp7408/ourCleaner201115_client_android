package com.example.ourcleaner_201008_java.View.Manager;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.example.ourcleaner_201008_java.R;

public class Manager_Signin3Activity extends AppCompatActivity {

    private static final String TAG = "매니저회원가입3정보입력완료";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager__signin3);
        Log.d(TAG, "=== onCreate ===" );
    }
}