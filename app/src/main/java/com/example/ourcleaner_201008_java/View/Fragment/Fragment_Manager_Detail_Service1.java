package com.example.ourcleaner_201008_java.View.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.ourcleaner_201008_java.R;


public class Fragment_Manager_Detail_Service1 extends Fragment {

/* 매니저가 매칭 완료된 서비스 상세보기 화면
* 서비상태 열람 및 변경하고
* =========> 고객 요구 업무 목록 열람
* 기본 업무 목록 열람
* 정산 확인할 수 있음 */

    private static final String TAG = "매니저 서비스 상세보기1";

    String servicefocusedhashMap, laundryBool, laundryCaution,
            garbagerecycleBool, garbagenormalBool, garbagefoodBool,
            resultGarbageStr, garbagehowto, serviceplus, serviceCaution;

    CheckBox laundryCheckbox, ironCheckbox, fridgeCheckbox;
    TextView focusTxt, cautionTxt;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        Log.e(TAG+" 생명 주기", "=== onCreateView ===" );

        /* fragment 관련 코드. 레이아웃 연결 */
        ViewGroup rootView = (ViewGroup)inflater.inflate(R.layout.fragment_manager_service_detail1, container, false);
        laundryCheckbox = rootView.findViewById(R.id.laundryCheckbox);
        ironCheckbox = rootView.findViewById(R.id.ironCheckbox);
        fridgeCheckbox = rootView.findViewById(R.id.fridgeCheckbox);

        focusTxt = rootView.findViewById(R.id.focusTxt);
        cautionTxt = rootView.findViewById(R.id.cautionTxt);

        if(laundryCheckbox.equals("세탁 및 건조된 빨래 개개")){
            laundryCheckbox.setVisibility(View.VISIBLE);
        }else{
            laundryCheckbox.setVisibility(View.GONE);
        }

        if(serviceplus.contains("다림질=true") && serviceplus.contains("냉장고=true")){
            ironCheckbox.setVisibility(View.VISIBLE);
            fridgeCheckbox.setVisibility(View.VISIBLE);

        }else if(serviceplus.contains("다림질=true")){
            ironCheckbox.setVisibility(View.VISIBLE);
            fridgeCheckbox.setVisibility(View.GONE);

        }else if(serviceplus.contains("냉장고=true")){
            ironCheckbox.setVisibility(View.GONE);
            fridgeCheckbox.setVisibility(View.VISIBLE);

        }

        if(servicefocusedhashMap.contains("roomBtn=true")){
            focusTxt.setText("방 집중적으로 청소해주세요.");
        }else if(servicefocusedhashMap.contains("livingRoomBtn=true")){
            focusTxt.setText("거실 집중적으로 청소해주세요.");
        }else if(servicefocusedhashMap.contains("kitchenBtn=true")){
            focusTxt.setText("주방 집중적으로 청소해주세요.");
        }else if(servicefocusedhashMap.contains("bathRoomBtn=true")){
            focusTxt.setText("화장실 집중적으로 청소해주세요.");
        }

        cautionTxt.setText(serviceCaution);


        return rootView;
    }


    //onCreate에서 데이터 넣기
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG+" 생명 주기", "onCreate()");
        Bundle extra = this.getArguments();
        if(extra != null) {

            extra = getArguments();
            servicefocusedhashMap = extra.getString("servicefocusedhashMap");

            laundryBool = extra.getString("laundryBool");
            laundryCaution = extra.getString("laundryCaution");

            garbagerecycleBool = extra.getString("garbagerecycleBool");
            garbagenormalBool = extra.getString("garbagenormalBool");
            garbagefoodBool = extra.getString("garbagefoodBool");

            resultGarbageStr = extra.getString("resultGarbageStr");
            garbagehowto = extra.getString("garbagehowto");
            serviceplus = extra.getString("serviceplus");
            serviceCaution = extra.getString("serviceCaution");

            Log.d(TAG, "=== servicefocusedhashMap ===" + servicefocusedhashMap);

            Log.d(TAG, "=== laundryBool ===" + laundryBool);
            Log.d(TAG, "=== laundryCaution ===" + laundryCaution);

            Log.d(TAG, "=== garbagerecycleBool ===" + garbagerecycleBool);
            Log.d(TAG, "=== garbagenormalBool ===" + garbagenormalBool);
            Log.d(TAG, "=== garbagefoodBool ===" + garbagefoodBool);

            Log.d(TAG, "=== resultGarbageStr ===" + resultGarbageStr);
            Log.d(TAG, "=== garbagehowto ===" + garbagehowto);
            Log.d(TAG, "=== serviceplus ===" + serviceplus);
            Log.d(TAG, "=== serviceCaution ===" + serviceCaution);

        }else{
            Log.d(TAG, "=== getNeedDefCost 비어있음 ===");
        }

    }




    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);


    }







}

