package com.example.ourcleaner_201008_java.View;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.ourcleaner_201008_java.R;

public class TermsMoreActivity extends AppCompatActivity {

    private static final String TAG = "TermsMoreActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms_more);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Log.d(TAG, "===  ===" );

        //있으면 넘어감
        Intent intent = new Intent(getApplicationContext(), TermsActivity.class);

        startActivity(intent);

        finish();

    }
}