package com.example.ourcleaner_201008_java.View.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.ourcleaner_201008_java.R;

public class Fragment_Third extends Fragment {

    public Fragment_Third(){

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_third,container,false);

        return view;
    }
}