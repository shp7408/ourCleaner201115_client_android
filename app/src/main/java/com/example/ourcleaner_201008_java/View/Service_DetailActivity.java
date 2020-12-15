package com.example.ourcleaner_201008_java.View;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.ourcleaner_201008_java.GlobalApplication;
import com.example.ourcleaner_201008_java.R;
import com.example.ourcleaner_201008_java.Server.LoginRequest;
import com.example.ourcleaner_201008_java.Server.ServiceSelectRequest;
import com.example.ourcleaner_201008_java.SharedP.PreferenceManager_Manager;
import com.example.ourcleaner_201008_java.View.Manager.Manager_Acount_Activity;
import com.example.ourcleaner_201008_java.View.Manager.Manager_LoginActivity;
import com.example.ourcleaner_201008_java.View.Manager.Manager_MainActivity;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.LogoutResponseCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.concurrent.TimeUnit;

public class Service_DetailActivity extends AppCompatActivity {

    /* 매칭 대기 중, 매칭 완료, 청소 중, 청소 완료 */
    private static final String TAG = "고객용서비스상세보기화면";

    /* 메인에서 받아온 uid로 세부 정보 불러오기 */
    int serviceWaitingUidInt;

    /* 서버에서 서비스 상세 정보 받아올 때, 필요한 변수임.*/
    String currentUser, serviceState, myplaceDTO_placeName, myplaceDTO_address, myplaceDTO_detailAddress,
            myplaceDTO_sizeStr, managerName,regularBool, visitDate, visitDay, startTime,
            needDefTime, needDefCost, servicefocusedhashMap, laundryBool, laundryCaution,
            garbagerecycleBool, garbagenormalBool, garbagefoodBool, garbagehowto,
            serviceplus, serviceCaution;

//    int startTimeInt, needDefTimeInt, allTimeInt;

    TextView datedayTxt, startAndAllTimeTxt, stateTxt, placeNameTxt, addressSizeTxt, editPossTxt, focusTxt, freePlusTxt, garbageTxt,
            plusTxt, cautionTxt;

    LinearLayout receiptLayout, priceDefLayout, priceIronLayout, pricefridgeLayout, priceResultLayout;
    TextView priceDefNumTxt, priceIronNumTxt, pricefridgeNumTxt, priceResutNumTxt;

//    int resultNeedTimeInt=0, needDefTimeInt; //필요한 전체 시간. 총 시간에서 setText하기 위해 필요한 변수
    int ironPlusTimeInt=0, refridgeTimeInt=0, startTimeInt=0, resultNeedTimeInt=0, defaultTimeInt=0, endTimeInt=0;
    int ironCostInt, fridgeCostInt, resultCost;
    String resultFocusedStr="", resultGarbageStr, resultPlusStr="";

    //숫자 천 자리에 콤마 찍기
    DecimalFormat formatter = new DecimalFormat("###,###");

    Button Btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service__detail);
        Log.d(TAG, "=== onCreate ===" );

        //intent로 받을 때 사용하는 코드
        Intent intent = getIntent();
        Log.d(TAG, "고객용 내 예약에서 인텐트 받음 intent :"+intent);

        serviceWaitingUidInt = intent.getIntExtra("uid",0);
        Log.d(TAG, "고객용 내 예약에서 인텐트 받음 intent : "+ serviceWaitingUidInt);

        /* 서버에서 서비스 상세 정보 받아오는 코드임 필요한 변수는 전역으로 선언 함.*/
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e(TAG, "=== response ===" +response);
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    Log.e(TAG, "=== 유효한 uid인 경우, jsonArray 값이 있음 ===" );
                    if(jsonArray.length()>0){

                        for(int i = 0 ; i<jsonArray.length(); i++){

                            JSONObject jsonObject = jsonArray.getJSONObject(i);

                            currentUser = jsonObject.getString( "currentUser" );
                            serviceState = jsonObject.getString( "serviceState" );

                            myplaceDTO_placeName = jsonObject.getString( "myplaceDTO_placeName" );
                            myplaceDTO_address = jsonObject.getString( "myplaceDTO_address" );
                            myplaceDTO_detailAddress = jsonObject.getString( "myplaceDTO_detailAddress" );
                            myplaceDTO_sizeStr = jsonObject.getString( "myplaceDTO_sizeStr" );

                            managerName = jsonObject.getString( "managerName" );
                            regularBool = jsonObject.getString( "regularBool" );
                            visitDate = jsonObject.getString( "visitDate" );
                            visitDay = jsonObject.getString( "visitDay" );

                            startTime = jsonObject.getString( "startTime" );
                            needDefTime = jsonObject.getString( "needDefTime" );
                            needDefCost = jsonObject.getString( "needDefCost" );

                            servicefocusedhashMap = jsonObject.getString( "servicefocusedhashMap" );
                            laundryBool = jsonObject.getString( "laundryBool" );
                            laundryCaution = jsonObject.getString( "laundryCaution" );

                            garbagerecycleBool = jsonObject.getString( "garbagerecycleBool" );
                            garbagenormalBool = jsonObject.getString( "garbagenormalBool" );
                            garbagefoodBool = jsonObject.getString( "garbagefoodBool" );

                            garbagehowto = jsonObject.getString( "garbagehowto" );
                            serviceplus = jsonObject.getString( "serviceplus" );
                            serviceCaution = jsonObject.getString( "serviceCaution" );

                            Log.e(TAG, "=== currentUser ===" +currentUser);
                            Log.e(TAG, "=== serviceState ===" +serviceState);

                            Log.e(TAG, "=== myplaceDTO_address ===" +myplaceDTO_address);
                            Log.e(TAG, "=== myplaceDTO_detailAddress ===" +myplaceDTO_detailAddress);
                            Log.e(TAG, "=== myplaceDTO_sizeStr ===" +myplaceDTO_sizeStr);

                            Log.e(TAG, "=== managerName ===" +managerName);
                            Log.e(TAG, "=== regularBool ===" +regularBool);
                            Log.e(TAG, "=== visitDate ===" +visitDate);
                            Log.e(TAG, "=== visitDay ===" +visitDay);

                            Log.e(TAG, "=== startTime ===" +startTime);
                            Log.e(TAG, "=== needDefTime ===" +needDefTime);
                            Log.e(TAG, "=== needDefCost ===" +needDefCost);

                            Log.e(TAG, "=== servicefocusedhashMap ===" +servicefocusedhashMap);
                            Log.e(TAG, "=== laundryBool ===" +laundryBool);
                            Log.e(TAG, "=== laundryCaution ===" +laundryCaution);

                            Log.e(TAG, "=== garbagerecycleBool ===" +garbagerecycleBool);
                            Log.e(TAG, "=== garbagenormalBool ===" +garbagenormalBool);
                            Log.e(TAG, "=== garbagefoodBool ===" +garbagefoodBool);

                            Log.e(TAG, "=== garbagehowto ===" +garbagehowto);
                            Log.e(TAG, "=== serviceplus ===" +serviceplus);
                            Log.e(TAG, "=== serviceCaution ===" +serviceCaution);

                            datedayTxt = findViewById(R.id.datedayTxt);
                            startAndAllTimeTxt = findViewById(R.id.startAndAllTimeTxt);
                            stateTxt = findViewById(R.id.stateTxt);

                            placeNameTxt = findViewById(R.id.placeNameTxt);
                            addressSizeTxt = findViewById(R.id.addressSizeTxt);

                            editPossTxt = findViewById(R.id.editPossTxt);
                            focusTxt = findViewById(R.id.focusTxt);
                            freePlusTxt = findViewById(R.id.freePlusTxt);
                            garbageTxt = findViewById(R.id.garbageTxt);
                            plusTxt = findViewById(R.id.plusTxt);

                            cautionTxt = findViewById(R.id.cautionTxt);

                            // 1. 날짜 요일 보여주기
                            datedayTxt.setText(visitDate+"("+visitDay+") 예약");

                            // 2. 시작 시간, 마치는 시간, 전체 소요 시간 보여주기
                            //마치는 시간, 전체 시간 가져오기 오기위해, 해쉬맵 string 체크
                            try{
                                Log.e(TAG+123, "=== serviceplus ===" +serviceplus);
                                if(serviceplus.contains("냉장고=true")){
//                                    resultNeedTimeInt = Integer.parseInt(needDefTime) + 120;
//                                    resultNeedTimeInt = needDefTimeInt;
                                    refridgeTimeInt=120;
                                    Log.d(TAG, "=== refridgeTimeInt ===" +refridgeTimeInt);
                                }
                                if(serviceplus.contains("다림질=true")){
//                                    resultNeedTimeInt = Integer.parseInt(needDefTime) + 30;
                                    ironPlusTimeInt=30;
                                    Log.d(TAG+123, "=== ironPlusTimeInt ===" +ironPlusTimeInt);
                                }

                                defaultTimeInt= Integer.parseInt(needDefTime);
                                Log.d(TAG+123, "=== defaultTimeInt ===" +defaultTimeInt);

                                startTimeInt= Integer.parseInt(startTime);
                                Log.d(TAG+123, "=== startTimeInt ===" +startTimeInt);

                                resultNeedTimeInt =Integer.parseInt(needDefTime);
                                Log.d(TAG+123, "=== resultNeedTimeInt ===" +resultNeedTimeInt);

                                endTimeInt=startTimeInt+defaultTimeInt+refridgeTimeInt+ironPlusTimeInt;
                                Log.d(TAG+123, "=== endTimeInt ===" +endTimeInt);

                                startAndAllTimeTxt.setText(timeIntToHourMin(startTimeInt)+"~"+timeIntToHourMin(endTimeInt)+"("+timeIntToHourMin2(endTimeInt-startTimeInt)+")");

                            }catch (Exception e){
                                Log.e(TAG+123, "=== startAndAllTimeTxt ===" +e);
                            }

                            // 3. 서비스 상태 보여주기
                            Btn = findViewById(R.id.Btn);
                            try{
                                if(serviceState.contains("매칭 대기 중")){
                                    stateTxt.setText("매칭 대기 중입니다.");
                                    Btn.setText("예약 취소하기");
                                }else if(serviceState.contains("예약 취소1")){
                                    stateTxt.setText("취소된 예약입니다.");
                                    Btn.setVisibility(View.GONE);
                                }else if(serviceState.contains("매칭 완료")){
                                    stateTxt.setText("취소된 예약입니다.");
                                    Btn.setText("예약 취소하기");
                                }else if(serviceState.contains("예약 취소2")){
                                    stateTxt.setText("취소된 예약입니다.");
                                    Btn.setVisibility(View.GONE);
                                }else if(serviceState.contains("청소 중")){
                                    stateTxt.setText("청소 중입니다.");
                                    Btn.setVisibility(View.GONE);
                                }else if(serviceState.contains("청소 완료")){
                                    stateTxt.setText("완료된 청소입니다.");
                                    Btn.setVisibility(View.GONE);
                                }
                            }catch (Exception e){
                                Log.e(TAG, "=== serviceState 널인 경우, 에러코드 ===" +e);
                            }

                            // 4. 집 상세정보 보여주기
                            // 내가 보는 경우에는, 장소 이름, 자세히 보기 가능. 그리고 변경하기 텍스트뷰도 보임. 물론, 서비스 이후에는 텍스트 뷰 보이면 안 됨.
                            placeNameTxt.setText(myplaceDTO_placeName);

                            try{
                                addressSizeTxt.setText(myplaceDTO_address.substring(8,14)+"("+myplaceDTO_sizeStr+")");
                            }catch (Exception e){
                                Log.e(TAG, "=== myplaceDTO_address myplaceDTO_sizeStr 널인 경우 에러코드 ===" +e);
                            }

                            editPossTxt.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Log.d(TAG, "=== editPossTxt 클릭 : === ");

                                    //있으면 넘어감
                                    Intent intent = new Intent(getApplicationContext(), MyPlace_EditActivity.class);

                                    intent.putExtra("myplaceDTO_address", myplaceDTO_address);
                                    intent.putExtra("myplaceDTO_sizeStr", myplaceDTO_sizeStr);
                                    intent.putExtra("currentUser", GlobalApplication.currentUser);

                                    startActivity(intent);

                                }

                            });

                            // 5. 집중 청소 구역 보여주기

                            if(servicefocusedhashMap!= null){
                                if(servicefocusedhashMap.contains("livingRoomBtn=true")){
                                    resultFocusedStr = "거실";
                                }

                                if(servicefocusedhashMap.contains("roomBtn=true")){
                                    if(resultFocusedStr.isEmpty()){
                                        resultFocusedStr="방";
                                    }else{
                                        resultFocusedStr= resultFocusedStr+" 방";
                                    }

                                }

                                if(servicefocusedhashMap.contains("bathRoomBtn=true")){
                                    if(resultFocusedStr.isEmpty()){
                                        resultFocusedStr="화장실";
                                    }else{
                                        resultFocusedStr= resultFocusedStr+" 화장실";
                                    }

                                }

                                if(servicefocusedhashMap.contains("kitchenBtn=true")){
                                    if(resultFocusedStr.isEmpty()){
                                        resultFocusedStr="부엌";
                                    }else{
                                        resultFocusedStr= resultFocusedStr+" 부엌";
                                    }
                                }
                                resultFocusedStr = resultFocusedStr+" 집중적으로 청소해주세요.";
                            }

                            focusTxt.setText(resultFocusedStr);

                            // 6. 무료 추가 선택 보여주기

                            if(laundryBool!= null){
                                if(laundryBool.contains("true")){
                                    laundryBool="세탁 추가";
                                    freePlusTxt.setText(laundryBool);
                                }else{
                                    freePlusTxt.setText("무료 추가 없음");
                                }
                            }else{
                                freePlusTxt.setText("무료 추가 없음");
                            }


                            // 7. 쓰레기 배출 선택 보여주기
                            if(garbagerecycleBool.contains("true")){
                                resultGarbageStr = "재활용";
                            }

                            if(garbagenormalBool.contains("true")){
                                if(resultFocusedStr.isEmpty()){
                                    resultGarbageStr="일반 쓰레기";
                                }
                                resultGarbageStr= resultGarbageStr+" 일반 쓰레기";
                            }

                            if(garbagefoodBool.contains("true")){
                                if(resultFocusedStr.isEmpty()){
                                    resultGarbageStr="음식물 쓰레기";
                                }
                                resultGarbageStr= resultGarbageStr+" 음식물 쓰레기";
                            }

                            if(resultGarbageStr!= null){
                                if(resultGarbageStr.isEmpty()){

                                }else{
                                    garbageTxt.setText(resultGarbageStr+" 버려주세요.");
                                }
                            }else{
                                garbageTxt.setText("선택 안 함");
                            }

                            // 8. 유료 선택 보여주기
                            if(serviceplus.contains("다림질=true")){
                                resultPlusStr = "다림질";
                                ironCostInt = 6600;
                            }
                            if(serviceplus.contains("냉장고=true")){
                                resultPlusStr = "냉장고";
                                fridgeCostInt = 26400;
                            }
                            if(serviceplus.contains("다림질=true")&&serviceplus.contains("냉장고=true")){
                                resultPlusStr="다림질 / 냉장고 청소 추가";
                            }

                            if(resultPlusStr.isEmpty()){
                                plusTxt.setText("유료 추가 없음");
                            }else{
                                plusTxt.setText(resultPlusStr+"( +"+formatter.format(ironCostInt+fridgeCostInt)+" 원)");
                            }

                            // 9. 입실 후, 주의사항 보여주기
                            if(serviceCaution.isEmpty()){
                                cautionTxt.setText("작성하신 주의사항이 없습니다.");
                            }else{
                                cautionTxt.setText(serviceCaution);
                            }

                            // 10. 결제 예정 내역 보여주기
                            // 다른 경우, 이름 바꿔야 함.
                            priceDefLayout = findViewById(R.id.priceDefLayout);
                            priceIronLayout = findViewById(R.id.priceIronLayout);
                            pricefridgeLayout = findViewById(R.id.pricefridgeLayout);
                            priceResultLayout = findViewById(R.id.priceResultLayout);

                            // 선택한 장소의 평 수에 따라서 책정한 기본 가격 세팅
                            priceDefNumTxt = findViewById(R.id.priceDefNumTxt);
                            priceDefNumTxt.setText(formatter.format(Integer.parseInt(needDefCost))+" 원");

                            // 선택한 장소의 유료 선택지에 따라서 레이아웃 보이게 함.
                            // 기본 가격
                            priceIronNumTxt = findViewById(R.id.priceIronNumTxt);
                            pricefridgeNumTxt = findViewById(R.id.pricefridgeNumTxt);

                            if(ironCostInt==6600){
                                priceIronNumTxt.setText(formatter.format(ironCostInt)+" 원");
                                priceIronLayout.setVisibility(View.VISIBLE);
                            }
                            if(fridgeCostInt==26400){
                                pricefridgeNumTxt.setText(formatter.format(fridgeCostInt)+" 원");
                                pricefridgeLayout.setVisibility(View.VISIBLE);
                            }
                            priceResutNumTxt = findViewById(R.id.priceResutNumTxt);
                            priceResutNumTxt.setText(formatter.format(Integer.parseInt(needDefCost)+ironCostInt+fridgeCostInt)+" 원");



                        }

                    }else{
                        Log.e(TAG, "=== 유효한 uid인 경우, jsonArray 값이 없음===" );
                        Toast.makeText( getApplicationContext(), "예약 내용을 다시 확인해주세요.", Toast.LENGTH_SHORT ).show();
                        finish();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e(TAG, "=== response === 에러코드 : " +e);
                }
            }
        };
        ServiceSelectRequest serviceSelectRequest = new ServiceSelectRequest(String.valueOf(serviceWaitingUidInt), "매칭 대기 중" ,"고객",responseListener);
        RequestQueue queue = Volley.newRequestQueue( Service_DetailActivity.this );
        queue.add( serviceSelectRequest );

//        datedayTxt = findViewById(R.id.datedayTxt);
//        startAndAllTimeTxt = findViewById(R.id.startAndAllTimeTxt);
//        stateTxt = findViewById(R.id.stateTxt);
//
//        placeNameTxt = findViewById(R.id.placeNameTxt);
//        addressSizeTxt = findViewById(R.id.addressSizeTxt);
//
//        editPossTxt = findViewById(R.id.editPossTxt);
//        focusTxt = findViewById(R.id.focusTxt);
//        freePlusTxt = findViewById(R.id.freePlusTxt);
//        garbageTxt = findViewById(R.id.garbageTxt);
//        plusTxt = findViewById(R.id.plusTxt);
//
//        cautionTxt = findViewById(R.id.cautionTxt);


//        // 1. 날짜 요일 보여주기
//        datedayTxt.setText(visitDate="("+visitDay+") 예약");
//
//        // 2. 시작 시간, 마치는 시간, 전체 소요 시간 보여주기
//        //마치는 시간, 전체 시간 가져오기 오기위해, 해쉬맵 string 체크
//        try{
//            Log.e(TAG, "=== serviceplus ===" +serviceplus);
//            if(serviceplus.contains("냉장고=true")){
//                resultNeedTimeInt = Integer.parseInt(needDefTime) + 120;
//                resultNeedTimeInt = needDefTimeInt;
//            }
//            if(serviceplus.contains("다림질=true")){
//                resultNeedTimeInt = Integer.parseInt(needDefTime) + 30;
//            }
//        }catch (Exception e){
//            Log.e(TAG, "=== serviceplus널인 경우 ===" +e);
//
//        }
//
//        try{
//            startAndAllTimeTxt.setText(timeIntToHourMin(Integer.parseInt(startTime))+"~"+
//                    timeIntToHourMin(Integer.parseInt(startTime)+resultNeedTimeInt)+"("+timeIntToHourMin2(resultNeedTimeInt)+")");
//
//        }catch (Exception e){
//
//        }
//
//        // 3. 서비스 상태 보여주기
//        try{
//            if(serviceState.contains("매칭 대기 중")){
//                stateTxt.setText("매칭 대기 중입니다.");
//            }
//        }catch (Exception e){
//            Log.e(TAG, "=== serviceState 널인 경우, 에러코드 ===" +e);
//        }
//
//        // 4. 집 상세정보 보여주기
//        // 내가 보는 경우에는, 장소 이름, 자세히 보기 가능. 그리고 변경하기 텍스트뷰도 보임. 물론, 서비스 이후에는 텍스트 뷰 보이면 안 됨.
//        placeNameTxt.setText(myplaceDTO_placeName);
//
//        try{
//            addressSizeTxt.setText(myplaceDTO_address.substring(8,14)+"("+myplaceDTO_sizeStr+")");
//        }catch (Exception e){
//            Log.e(TAG, "=== myplaceDTO_address myplaceDTO_sizeStr 널인 경우 에러코드 ===" +e);
//        }
//
//        editPossTxt.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Log.d(TAG, "=== editPossTxt 클릭 : === ");
//
//                //있으면 넘어감
//                Intent intent = new Intent(getApplicationContext(), MyPlace_EditActivity.class);
//
//                intent.putExtra("myplaceDTO_address", myplaceDTO_address);
//                intent.putExtra("myplaceDTO_sizeStr", myplaceDTO_sizeStr);
//                intent.putExtra("currentUser", GlobalApplication.currentUser);
//
//                startActivity(intent);
//
//            }
//
//        });
//
//        // 5. 집중 청소 구역 보여주기
//
//        if(servicefocusedhashMap!= null){
//            if(servicefocusedhashMap.contains("livingRoomBtn=true")){
//                resultFocusedStr = "거실";
//            }
//
//            if(servicefocusedhashMap.contains("roomBtn=true")){
//                if(resultFocusedStr.isEmpty()){
//                    resultFocusedStr="방";
//                }
//                resultFocusedStr= resultFocusedStr+" 방";
//            }
//
//            if(servicefocusedhashMap.contains("bathRoomBtn=true")){
//                if(resultFocusedStr.isEmpty()){
//                    resultFocusedStr="화장실";
//                }
//                resultFocusedStr= resultFocusedStr+" 화장실";
//            }
//
//            if(servicefocusedhashMap.contains("kitchenBtn=true")){
//                if(resultFocusedStr.isEmpty()){
//                    resultFocusedStr="부엌";
//                }
//                resultFocusedStr= resultFocusedStr+" 부엌";
//            }
//            resultFocusedStr = resultFocusedStr+" 집중적으로 청소해주세요.";
//        }
//
//        focusTxt.setText(resultFocusedStr);
//
//        // 6. 무료 추가 선택 보여주기
//
//        if(laundryBool!= null){
//            if(laundryBool.contains("true")){
//                laundryBool="세탁 추가";
//                freePlusTxt.setText(laundryBool);
//            }else{
//                freePlusTxt.setText("무료 추가 없음");
//            }
//        }else{
//            freePlusTxt.setText("무료 추가 없음");
//        }
//
//
//        // 7. 쓰레기 배출 선택 보여주기
//        if(garbagerecycleBool.contains("true")){
//            resultGarbageStr = "재활용";
//        }
//
//        if(garbagenormalBool.contains("true")){
//            if(resultFocusedStr.isEmpty()){
//                resultGarbageStr="일반 쓰레기";
//            }
//            resultGarbageStr= resultGarbageStr+" 일반 쓰레기";
//        }
//
//        if(garbagefoodBool.contains("true")){
//            if(resultFocusedStr.isEmpty()){
//                resultGarbageStr="음식물 쓰레기";
//            }
//            resultGarbageStr= resultGarbageStr+" 음식물 쓰레기";
//        }
//
//        if(resultGarbageStr!= null){
//            if(resultGarbageStr.isEmpty()){
//
//            }else{
//                garbageTxt.setText(resultGarbageStr+" 버려주세요.");
//            }
//        }else{
//            garbageTxt.setText("선택 안 함");
//        }
//
//        // 8. 유료 선택 보여주기
//        if(serviceplus.contains("다림질=true")){
//            resultPlusStr = "다림질";
//            ironCostInt = 6600;
//        }
//        if(serviceplus.contains("냉장고=true")){
//            fridgeCostInt = 26400;
//            if(resultPlusStr.isEmpty()){
//                resultPlusStr= resultPlusStr+" 냉장고 청소";
//            }
//        }
//        if(resultPlusStr.isEmpty()){
//            plusTxt.setText("유료 추가 없음");
//        }else{
//            plusTxt.setText(resultPlusStr+"( +"+formatter.format(ironCostInt+fridgeCostInt)+" 원)");
//        }
//
//        // 9. 입실 후, 주의사항 보여주기
//        if(serviceCaution.isEmpty()){
//            cautionTxt.setText("작성하신 주의사항이 없습니다.");
//        }else{
//            cautionTxt.setText(serviceCaution);
//        }
//
//        // 10. 결제 예정 내역 보여주기
//        // 다른 경우, 이름 바꿔야 함.
//        priceDefLayout = findViewById(R.id.priceDefLayout);
//        priceIronLayout = findViewById(R.id.priceIronLayout);
//        pricefridgeLayout = findViewById(R.id.pricefridgeLayout);
//        priceResultLayout = findViewById(R.id.priceResultLayout);
//
//        // 선택한 장소의 평 수에 따라서 책정한 기본 가격 세팅
//        priceDefNumTxt = findViewById(R.id.priceDefNumTxt);
//        priceDefNumTxt.setText(formatter.format(Integer.parseInt(needDefCost))+" 원");
//
//        // 선택한 장소의 유료 선택지에 따라서 레이아웃 보이게 함.
//        // 기본 가격
//        priceIronNumTxt = findViewById(R.id.priceIronNumTxt);
//        pricefridgeNumTxt = findViewById(R.id.pricefridgeNumTxt);
//
//        if(ironCostInt==6600){
//            priceIronNumTxt.setText(formatter.format(ironCostInt)+" 원");
//            priceIronLayout.setVisibility(View.VISIBLE);
//        }
//        if(fridgeCostInt==26400){
//            pricefridgeNumTxt.setText(formatter.format(fridgeCostInt)+" 원");
//            pricefridgeLayout.setVisibility(View.VISIBLE);
//        }
//        priceResutNumTxt = findViewById(R.id.priceResutNumTxt);
//        priceResutNumTxt.setText(formatter.format(Integer.parseInt(needDefCost)+ironCostInt+fridgeCostInt)+" 원");
//



        Btn = (Button) findViewById(R.id.Btn);
        Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    Log.d(TAG, "=== Btn 클릭 : === ");

                    if(Btn.getText().toString().equals("예약 취소하기")&&serviceState.contains("매칭 대기 중")){
                        Log.d(TAG, "=== 매칭 대기 중 상태에서 예약 취소하기 ===" );


                        AlertDialog.Builder builder = new AlertDialog.Builder(Service_DetailActivity.this);
                        builder.setMessage("예약 취소 하시겠습니까?");
                        builder.setPositiveButton("확인",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        Log.d(TAG, "=== 확인 클릭  ===" );

                                        //UserManagement API 요청을 담당
                                        UserManagement.getInstance()
                                                //requestLogout : 로그아웃 요청
                                                //파라미터 : logout 요청 결과에 대한 callback
                                                .requestLogout(new LogoutResponseCallback() {
                                                    @Override
                                                    public void onCompleteLogout() {
                                                        Log.d(TAG, "=== onCompleteLogout : 예약 취소 되었습니다. ===");

                                                        /* 서버에서 서비스 상세 정보 받아오는 코드임 필요한 변수는 전역으로 선언 함.*/
                                                        Response.Listener<String> responseListener = new Response.Listener<String>() {
                                                            @Override
                                                            public void onResponse(String response) {
                                                                Log.e(TAG, "=== response ===" +response);
                                                                try {
                                                                    JSONArray jsonArray = new JSONArray(response);
                                                                    Log.e(TAG, "=== 유효한 uid인 경우, jsonArray 값이 있음 ===" );
                                                                    if(jsonArray.length()>0){

                                                                        for(int i = 0 ; i<jsonArray.length(); i++){

                                                                            JSONObject jsonObject = jsonArray.getJSONObject(i);

                                                                            currentUser = jsonObject.getString( "currentUser" );
                                                                            serviceState = jsonObject.getString( "serviceState" );

                                                                            myplaceDTO_placeName = jsonObject.getString( "myplaceDTO_placeName" );
                                                                            myplaceDTO_address = jsonObject.getString( "myplaceDTO_address" );
                                                                            myplaceDTO_detailAddress = jsonObject.getString( "myplaceDTO_detailAddress" );
                                                                            myplaceDTO_sizeStr = jsonObject.getString( "myplaceDTO_sizeStr" );

                                                                            managerName = jsonObject.getString( "managerName" );
                                                                            regularBool = jsonObject.getString( "regularBool" );
                                                                            visitDate = jsonObject.getString( "visitDate" );
                                                                            visitDay = jsonObject.getString( "visitDay" );

                                                                            startTime = jsonObject.getString( "startTime" );
                                                                            needDefTime = jsonObject.getString( "needDefTime" );
                                                                            needDefCost = jsonObject.getString( "needDefCost" );

                                                                            servicefocusedhashMap = jsonObject.getString( "servicefocusedhashMap" );
                                                                            laundryBool = jsonObject.getString( "laundryBool" );
                                                                            laundryCaution = jsonObject.getString( "laundryCaution" );

                                                                            garbagerecycleBool = jsonObject.getString( "garbagerecycleBool" );
                                                                            garbagenormalBool = jsonObject.getString( "garbagenormalBool" );
                                                                            garbagefoodBool = jsonObject.getString( "garbagefoodBool" );

                                                                            garbagehowto = jsonObject.getString( "garbagehowto" );
                                                                            serviceplus = jsonObject.getString( "serviceplus" );
                                                                            serviceCaution = jsonObject.getString( "serviceCaution" );

                                                                            Log.e(TAG, "=== currentUser ===" +currentUser);
                                                                            Log.e(TAG, "=== serviceState ===" +serviceState);

                                                                            Log.e(TAG, "=== myplaceDTO_address ===" +myplaceDTO_address);
                                                                            Log.e(TAG, "=== myplaceDTO_detailAddress ===" +myplaceDTO_detailAddress);
                                                                            Log.e(TAG, "=== myplaceDTO_sizeStr ===" +myplaceDTO_sizeStr);

                                                                            Log.e(TAG, "=== managerName ===" +managerName);
                                                                            Log.e(TAG, "=== regularBool ===" +regularBool);
                                                                            Log.e(TAG, "=== visitDate ===" +visitDate);
                                                                            Log.e(TAG, "=== visitDay ===" +visitDay);

                                                                            Log.e(TAG, "=== startTime ===" +startTime);
                                                                            Log.e(TAG, "=== needDefTime ===" +needDefTime);
                                                                            Log.e(TAG, "=== needDefCost ===" +needDefCost);

                                                                            Log.e(TAG, "=== servicefocusedhashMap ===" +servicefocusedhashMap);
                                                                            Log.e(TAG, "=== laundryBool ===" +laundryBool);
                                                                            Log.e(TAG, "=== laundryCaution ===" +laundryCaution);

                                                                            Log.e(TAG, "=== garbagerecycleBool ===" +garbagerecycleBool);
                                                                            Log.e(TAG, "=== garbagenormalBool ===" +garbagenormalBool);
                                                                            Log.e(TAG, "=== garbagefoodBool ===" +garbagefoodBool);

                                                                            Log.e(TAG, "=== garbagehowto ===" +garbagehowto);
                                                                            Log.e(TAG, "=== serviceplus ===" +serviceplus);
                                                                            Log.e(TAG, "=== serviceCaution ===" +serviceCaution);

                                                                            datedayTxt = findViewById(R.id.datedayTxt);
                                                                            startAndAllTimeTxt = findViewById(R.id.startAndAllTimeTxt);
                                                                            stateTxt = findViewById(R.id.stateTxt);

                                                                            placeNameTxt = findViewById(R.id.placeNameTxt);
                                                                            addressSizeTxt = findViewById(R.id.addressSizeTxt);

                                                                            editPossTxt = findViewById(R.id.editPossTxt);
                                                                            focusTxt = findViewById(R.id.focusTxt);
                                                                            freePlusTxt = findViewById(R.id.freePlusTxt);
                                                                            garbageTxt = findViewById(R.id.garbageTxt);
                                                                            plusTxt = findViewById(R.id.plusTxt);

                                                                            cautionTxt = findViewById(R.id.cautionTxt);

                                                                            // 1. 날짜 요일 보여주기
                                                                            datedayTxt.setText(visitDate+"("+visitDay+") 예약");

                                                                            // 2. 시작 시간, 마치는 시간, 전체 소요 시간 보여주기
                                                                            //마치는 시간, 전체 시간 가져오기 오기위해, 해쉬맵 string 체크
                                                                            try{
                                                                                Log.e(TAG+123, "=== serviceplus ===" +serviceplus);
                                                                                if(serviceplus.contains("냉장고=true")){
                                                                                    refridgeTimeInt=120;
                                                                                    Log.d(TAG, "=== refridgeTimeInt ===" +refridgeTimeInt);
                                                                                }
                                                                                if(serviceplus.contains("다림질=true")){
                                                                                    ironPlusTimeInt=30;
                                                                                    Log.d(TAG+123, "=== ironPlusTimeInt ===" +ironPlusTimeInt);
                                                                                }

                                                                                defaultTimeInt= Integer.parseInt(needDefTime);
                                                                                Log.d(TAG+123, "=== defaultTimeInt ===" +defaultTimeInt);

                                                                                startTimeInt= Integer.parseInt(startTime);
                                                                                Log.d(TAG+123, "=== startTimeInt ===" +startTimeInt);

                                                                                resultNeedTimeInt =Integer.parseInt(needDefTime);
                                                                                Log.d(TAG+123, "=== resultNeedTimeInt ===" +resultNeedTimeInt);

                                                                                endTimeInt=startTimeInt+defaultTimeInt+refridgeTimeInt+ironPlusTimeInt;
                                                                                Log.d(TAG+123, "=== endTimeInt ===" +endTimeInt);

                                                                                startAndAllTimeTxt.setText(timeIntToHourMin(startTimeInt)+"~"+timeIntToHourMin(endTimeInt)+"("+timeIntToHourMin2(endTimeInt-startTimeInt)+")");

                                                                            }catch (Exception e){
                                                                                Log.e(TAG+123, "=== startAndAllTimeTxt ===" +e);
                                                                            }

                                                                            // 3. 서비스 상태 보여주기
                                                                            Btn = findViewById(R.id.Btn);
                                                                            try{
                                                                                if(serviceState.contains("매칭 대기 중")){
                                                                                    stateTxt.setText("매칭 대기 중입니다.");
                                                                                    Btn.setText("예약 취소하기");
                                                                                }else if(serviceState.contains("예약 취소1")){
                                                                                    stateTxt.setText("취소된 예약입니다.");
                                                                                    Btn.setVisibility(View.GONE);
                                                                                }else if(serviceState.contains("매칭 완료")){
                                                                                    stateTxt.setText("취소된 예약입니다.");
                                                                                    Btn.setText("예약 취소하기");
                                                                                }else if(serviceState.contains("예약 취소2")){
                                                                                    stateTxt.setText("취소된 예약입니다.");
                                                                                    Btn.setVisibility(View.GONE);
                                                                                }else if(serviceState.contains("청소 중")){
                                                                                    stateTxt.setText("청소 중입니다.");
                                                                                    Btn.setVisibility(View.GONE);
                                                                                }else if(serviceState.contains("청소 완료")){
                                                                                    stateTxt.setText("완료된 청소입니다.");
                                                                                    Btn.setVisibility(View.GONE);
                                                                                }
                                                                            }catch (Exception e){
                                                                                Log.e(TAG, "=== serviceState 널인 경우, 에러코드 ===" +e);
                                                                            }

                                                                            // 4. 집 상세정보 보여주기
                                                                            // 내가 보는 경우에는, 장소 이름, 자세히 보기 가능. 그리고 변경하기 텍스트뷰도 보임. 물론, 서비스 이후에는 텍스트 뷰 보이면 안 됨.
                                                                            placeNameTxt.setText(myplaceDTO_placeName);

                                                                            try{
                                                                                addressSizeTxt.setText(myplaceDTO_address.substring(8,14)+"("+myplaceDTO_sizeStr+")");
                                                                            }catch (Exception e){
                                                                                Log.e(TAG, "=== myplaceDTO_address myplaceDTO_sizeStr 널인 경우 에러코드 ===" +e);
                                                                            }

                                                                            // TODO: 2020-11-28 장소 정보변경 불가능함
                                                                            editPossTxt.setVisibility(View.GONE);

                                                                            // 5. 집중 청소 구역 보여주기

                                                                            if(servicefocusedhashMap!= null){
                                                                                if(servicefocusedhashMap.contains("livingRoomBtn=true")){
                                                                                    resultFocusedStr = "거실";
                                                                                }

                                                                                if(servicefocusedhashMap.contains("roomBtn=true")){
                                                                                    if(resultFocusedStr.isEmpty()){
                                                                                        resultFocusedStr="방";
                                                                                    }else{
                                                                                        resultFocusedStr= resultFocusedStr+" 방";
                                                                                    }

                                                                                }

                                                                                if(servicefocusedhashMap.contains("bathRoomBtn=true")){
                                                                                    if(resultFocusedStr.isEmpty()){
                                                                                        resultFocusedStr="화장실";
                                                                                    }else{
                                                                                        resultFocusedStr= resultFocusedStr+" 화장실";
                                                                                    }

                                                                                }

                                                                                if(servicefocusedhashMap.contains("kitchenBtn=true")){
                                                                                    if(resultFocusedStr.isEmpty()){
                                                                                        resultFocusedStr="부엌";
                                                                                    }else{
                                                                                        resultFocusedStr= resultFocusedStr+" 부엌";
                                                                                    }
                                                                                }
                                                                                resultFocusedStr = resultFocusedStr+" 집중적으로 청소해주세요.";
                                                                            }

                                                                            focusTxt.setText(resultFocusedStr);

                                                                            // 6. 무료 추가 선택 보여주기

                                                                            if(laundryBool!= null){
                                                                                if(laundryBool.contains("true")){
                                                                                    laundryBool="세탁 추가";
                                                                                    freePlusTxt.setText(laundryBool);
                                                                                }else{
                                                                                    freePlusTxt.setText("무료 추가 없음");
                                                                                }
                                                                            }else{
                                                                                freePlusTxt.setText("무료 추가 없음");
                                                                            }


                                                                            // 7. 쓰레기 배출 선택 보여주기
                                                                            if(garbagerecycleBool.contains("true")){
                                                                                resultGarbageStr = "재활용";
                                                                            }

                                                                            if(garbagenormalBool.contains("true")){
                                                                                if(resultFocusedStr.isEmpty()){
                                                                                    resultGarbageStr="일반 쓰레기";
                                                                                }
                                                                                resultGarbageStr= resultGarbageStr+" 일반 쓰레기";
                                                                            }

                                                                            if(garbagefoodBool.contains("true")){
                                                                                if(resultFocusedStr.isEmpty()){
                                                                                    resultGarbageStr="음식물 쓰레기";
                                                                                }
                                                                                resultGarbageStr= resultGarbageStr+" 음식물 쓰레기";
                                                                            }

                                                                            if(resultGarbageStr!= null){
                                                                                if(resultGarbageStr.isEmpty()){

                                                                                }else{
                                                                                    garbageTxt.setText(resultGarbageStr+" 버려주세요.");
                                                                                }
                                                                            }else{
                                                                                garbageTxt.setText("선택 안 함");
                                                                            }

                                                                            // 8. 유료 선택 보여주기
                                                                            if(serviceplus.contains("다림질=true")){
                                                                                resultPlusStr = "다림질";
                                                                                ironCostInt = 6600;
                                                                            }
                                                                            if(serviceplus.contains("냉장고=true")){
                                                                                resultPlusStr = "냉장고";
                                                                                fridgeCostInt = 26400;
                                                                            }
                                                                            if(serviceplus.contains("다림질=true")&&serviceplus.contains("냉장고=true")){
                                                                                resultPlusStr="다림질 / 냉장고 청소 추가";
                                                                            }

                                                                            if(resultPlusStr.isEmpty()){
                                                                                plusTxt.setText("유료 추가 없음");
                                                                            }else{
                                                                                plusTxt.setText(resultPlusStr+"( +"+formatter.format(ironCostInt+fridgeCostInt)+" 원)");
                                                                            }

                                                                            // 9. 입실 후, 주의사항 보여주기
                                                                            if(serviceCaution.isEmpty()){
                                                                                cautionTxt.setText("작성하신 주의사항이 없습니다.");
                                                                            }else{
                                                                                cautionTxt.setText(serviceCaution);
                                                                            }

                                                                            // 10. 결제 예정 내역 보여주기
                                                                            // 다른 경우, 이름 바꿔야 함.
                                                                            priceDefLayout = findViewById(R.id.priceDefLayout);
                                                                            priceIronLayout = findViewById(R.id.priceIronLayout);
                                                                            pricefridgeLayout = findViewById(R.id.pricefridgeLayout);
                                                                            priceResultLayout = findViewById(R.id.priceResultLayout);

                                                                            // 선택한 장소의 평 수에 따라서 책정한 기본 가격 세팅
                                                                            priceDefNumTxt = findViewById(R.id.priceDefNumTxt);
                                                                            priceDefNumTxt.setText(formatter.format(Integer.parseInt(needDefCost))+" 원");

                                                                            // 선택한 장소의 유료 선택지에 따라서 레이아웃 보이게 함.
                                                                            // 기본 가격
                                                                            priceIronNumTxt = findViewById(R.id.priceIronNumTxt);
                                                                            pricefridgeNumTxt = findViewById(R.id.pricefridgeNumTxt);

                                                                            if(ironCostInt==6600){
                                                                                priceIronNumTxt.setText(formatter.format(ironCostInt)+" 원");
                                                                                priceIronLayout.setVisibility(View.VISIBLE);
                                                                            }
                                                                            if(fridgeCostInt==26400){
                                                                                pricefridgeNumTxt.setText(formatter.format(fridgeCostInt)+" 원");
                                                                                pricefridgeLayout.setVisibility(View.VISIBLE);
                                                                            }
                                                                            priceResutNumTxt = findViewById(R.id.priceResutNumTxt);
                                                                            priceResutNumTxt.setText(formatter.format(Integer.parseInt(needDefCost)+ironCostInt+fridgeCostInt)+" 원");



                                                                        }

                                                                    }else{
                                                                        Log.e(TAG, "=== 유효한 uid인 경우, jsonArray 값이 없음===" );
                                                                        Toast.makeText( getApplicationContext(), "예약 내용을 다시 확인해주세요.", Toast.LENGTH_SHORT ).show();
                                                                        finish();
                                                                    }

                                                                } catch (JSONException e) {
                                                                    e.printStackTrace();
                                                                    Log.e(TAG, "=== response === 에러코드 : " +e);
                                                                }
                                                            }
                                                        };
                                                        ServiceSelectRequest serviceSelectRequest = new ServiceSelectRequest(String.valueOf(serviceWaitingUidInt), "예약 취소1" ,"고객",responseListener);
                                                        RequestQueue queue = Volley.newRequestQueue( Service_DetailActivity.this );
                                                        queue.add( serviceSelectRequest );


                                                    }
                                                });
                                    }
                                });
                        builder.setNegativeButton("취소",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        Log.d(TAG, "=== 취소 클릭 ===" );
                                    }
                                });
                        builder.show();


                    }else if(Btn.getText().toString().equals("예약 취소하기")&&serviceState.contains("매칭 완료")){
                        Log.d(TAG, "=== 매칭 완료 상태에서 예약 취소하기 ===" );


                    }

                    }
            });



    }

    //int 형태의 정수를 "3시 30분" String으로 나타내는 메서드
    public String timeIntToHourMin(int plusTimeInt){

        long hour = TimeUnit.MINUTES.toHours(plusTimeInt); // 분을 시간으로 변경
        Log.d(TAG, "=== hour ===" +hour);

        long minutes = TimeUnit.MINUTES.toMinutes(plusTimeInt) - TimeUnit.HOURS.toMinutes(hour); // 시간으로 변경하고, 나머지 분
        Log.d(TAG, "=== minutes ==="+minutes );

        //이거 추가 해야 함.
        String plusTimeStr;

        if(hour==0){
            Log.d(TAG, "=== hour==0  ===" );
            plusTimeStr = minutes + "분";
            Log.d(TAG, "=== plusTimeStr ===" +plusTimeStr);
        }else if(minutes==0){
            Log.d(TAG, "=== minutes==0 ===" );
            plusTimeStr = hour + "시";
            Log.d(TAG, "=== plusTimeStr ===" +plusTimeStr);
        }else{
            Log.d(TAG, "=== hour 랑 minutes 둘 다 0이 아닌, 경우 ===" );
            plusTimeStr = hour +"시 "+ minutes + "분";
            Log.d(TAG, "=== plusTimeStr ===" +plusTimeStr);
        }

        return plusTimeStr;
    }

    //int 형태의 정수를 "3시간 30분" String으로 나타내는 메서드
    public String timeIntToHourMin2(int plusTimeInt){

        long hour = TimeUnit.MINUTES.toHours(plusTimeInt); // 분을 시간으로 변경
        Log.d(TAG, "=== hour ===" +hour);

        long minutes = TimeUnit.MINUTES.toMinutes(plusTimeInt) - TimeUnit.HOURS.toMinutes(hour); // 시간으로 변경하고, 나머지 분
        Log.d(TAG, "=== minutes ==="+minutes );

        //이거 추가 해야 함.
        String plusTimeStr;

        if(hour==0){
            Log.d(TAG, "=== hour==0  ===" );
            plusTimeStr = minutes + "분";
            Log.d(TAG, "=== plusTimeStr ===" +plusTimeStr);
        }else if(minutes==0){
            Log.d(TAG, "=== minutes==0 ===" );
            plusTimeStr =hour + "시간";
            Log.d(TAG, "=== plusTimeStr ===" +plusTimeStr);
        }else{
            Log.d(TAG, "=== hour 랑 minutes 둘 다 0이 아닌, 경우 ===" );
            plusTimeStr = hour +"시간 "+ minutes + "분";
            Log.d(TAG, "=== plusTimeStr ===" +plusTimeStr);
        }

        return plusTimeStr;
    }
}