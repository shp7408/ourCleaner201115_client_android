package com.example.ourcleaner_201008_java.View.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.ourcleaner_201008_java.R;


public class Fragment_Manager_Detail_Service2 extends Fragment {

    /* 매니저가 매칭 완료된 서비스 상세보기 화면
     * 서비상태 열람 및 변경하고
     * 고객 요구 업무 목록 열람
     * =========> 기본 업무 목록 열람
     * 정산 확인할 수 있음 */


    private static final String TAG = "매니저 서비스 상세보기2";




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        Log.e(TAG+" 생명 주기", "=== onCreateView ===" );

        /* fragment 관련 코드. 레이아웃 연결 */
        ViewGroup rootView = (ViewGroup)inflater.inflate(R.layout.fragment_manager_service_detail2, container, false);


        return rootView;
    }


    //onCreate에서 데이터 넣기
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG+" 생명 주기", "onCreate()");


    }




    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);


    }







}

