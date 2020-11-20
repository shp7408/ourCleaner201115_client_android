package com.example.ourcleaner_201008_java.View;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.ourcleaner_201008_java.R;

import org.w3c.dom.Text;

public class MoreService_Payment_Activity extends AppCompatActivity {

    private static final String TAG = "결제 수단 관리";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_service__payment_);
        Log.d(TAG, "=== onCreate ===" );


    }
}