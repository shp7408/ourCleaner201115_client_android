package com.example.ourcleaner_201008_java.View;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.example.ourcleaner_201008_java.R;

public class MyPlace_EditActivity extends AppCompatActivity {

    private static final String TAG = "나의장소변경화면";
/*
        intent.putExtra("myplaceDTO_address", myplaceDTO_address);
        intent.putExtra("myplaceDTO_sizeStr", myplaceDTO_sizeStr);
        intent.putExtra("currentUser", GlobalApplication.currentUser);
*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_place__edit);
        Log.d(TAG, "=== onCreate ===" );
    }
}