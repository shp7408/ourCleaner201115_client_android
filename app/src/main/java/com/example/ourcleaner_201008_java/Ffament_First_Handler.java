package com.example.ourcleaner_201008_java;

import android.os.Handler;
import android.os.Message;

import androidx.annotation.NonNull;

import com.example.ourcleaner_201008_java.View.Fragment.Fragment_First;

public class Ffament_First_Handler extends Handler {
    Fragment_First fragment_first;

    public Ffament_First_Handler(Fragment_First fragment_first){
        this.fragment_first = fragment_first;
    }

    @Override
    public void handleMessage(@NonNull Message msg) {
        super.handleMessage(msg);
        if(msg.what ==0){
            fragment_first.needDefTimeTxt.setText(msg.arg1+"시간");
        }
    }



}
