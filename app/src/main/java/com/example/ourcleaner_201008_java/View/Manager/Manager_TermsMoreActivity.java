package com.example.ourcleaner_201008_java.View.Manager;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.example.ourcleaner_201008_java.R;

public class Manager_TermsMoreActivity extends AppCompatActivity {

    private static final String TAG = "매니저용이용약관화면";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager__terms_more);
        Log.d(TAG, "=== onCreate ===" );


    }
}