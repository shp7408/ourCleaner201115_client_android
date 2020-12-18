package com.example.ourcleaner_201008_java.View.Manager;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ourcleaner_201008_java.Interface.AlarmManagerSelectInterface;
import com.example.ourcleaner_201008_java.Interface.ServiceStatesChangeInterface;
import com.example.ourcleaner_201008_java.Interface.TokenSelectInterface;
import com.example.ourcleaner_201008_java.R;
import com.example.ourcleaner_201008_java.Service.FcmPushTest;
import com.example.ourcleaner_201008_java.SharedP.PreferenceManager_Manager;
import com.example.ourcleaner_201008_java.View.MainActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;



public class Manager_AlarmActivity extends AppCompatActivity {

    private static final String TAG = "매니저알람엑티비티";

    int serviceIdInt;

    String currentUser, serviceState, myplaceDTO_placeName, myplaceDTO_address, myplaceDTO_detailAddress,
            myplaceDTO_sizeStr, managerName,regularBool, visitDate, visitDay, startTime,
            needDefTime, needDefCost, servicefocusedhashMap, laundryBool, laundryCaution,
            garbagerecycleBool, garbagenormalBool, garbagefoodBool, garbagehowto,
            serviceplus, serviceCaution, cardBilling_key;

    TextView datedayTxt, startAndAllTimeTxt, stateTxt, placeNameTxt, addressSizeTxt, editPossTxt, focusTxt, freePlusTxt, garbageTxt,
            plusTxt, cautionTxt;

    LinearLayout receiptLayout, priceDefLayout, priceIronLayout, pricefridgeLayout, priceResultLayout;
    TextView priceDefNumTxt, priceIronNumTxt, pricefridgeNumTxt, priceResutNumTxt, priceResutNum2Txt;

    //    int resultNeedTimeInt=0, needDefTimeInt; //필요한 전체 시간. 총 시간에서 setText하기 위해 필요한 변수
    int ironPlusTimeInt=0, refridgeTimeInt=0, startTimeInt=0, resultNeedTimeInt=0, defaultTimeInt=0, endTimeInt=0;
    int ironCostInt, fridgeCostInt, resultCost;
    String resultFocusedStr="", resultGarbageStr, resultPlusStr;

    //숫자 천 자리에 콤마 찍기
    DecimalFormat formatter = new DecimalFormat("###,###");

    Button noBtn, okBtn;

    /*서버에서 받아온 매니저의 이름과 이메일 - 나누어서 작업하는게 편함..*/
    String managerNameStr, managerEmailStr, cardCard_name;

    String clientToken;

    Integer resultCostIntegerForBootpay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager__alarm);
        Log.e(TAG, "===onCreate  ===" );

/* 이미 매니저 자동로그인 쉐어드에 idStr이 있으므로, 매니저 로그인 화면으로 이동하면, 매니저 메인 화면으로 이동할 것 임 */
        /* 여기 화면에서 서비스의 상세 정보가 보여지고,
        * 그 아래에 수락, 거절 버튼 있어야 함. */

        datedayTxt = findViewById(R.id.datedayTxt);
        startAndAllTimeTxt = findViewById(R.id.startAndAllTimeTxt);
        stateTxt = findViewById(R.id.stateTxt);



        receiptLayout = findViewById(R.id.receiptLayout);
        priceDefLayout = findViewById(R.id.priceDefLayout);
        priceDefNumTxt = findViewById(R.id.priceDefNumTxt);
        priceIronLayout = findViewById(R.id.priceIronLayout);
        priceIronNumTxt = findViewById(R.id.priceIronNumTxt);
        pricefridgeLayout = findViewById(R.id.pricefridgeLayout);
        pricefridgeNumTxt = findViewById(R.id.pricefridgeNumTxt);
        priceResutNumTxt = findViewById(R.id.priceResutNumTxt);
        priceResutNum2Txt = findViewById(R.id.priceResutNum2Txt);

        placeNameTxt = findViewById(R.id.placeNameTxt);
        addressSizeTxt = findViewById(R.id.addressSizeTxt);


        noBtn = findViewById(R.id.noBtn);
        okBtn = findViewById(R.id.okBtn);


        postEmailAlarm(PreferenceManager_Manager.getString(getApplicationContext(), "idStr"));



        noBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "=== noBtn 거절하기 클릭 : === ");

                /* 1. 일반 고객에게 푸쉬 알람 갈 것
                *  2. 서비스의 매니저 이름에 매니저 미지정으로 변경 -> 매칭 대기중인 목록에 뜨게 됨
                *  3. 매니저 메인 화면으로 이동하기
                *  4. 해당 알람 activate 상태를 0 으로 변경하기*/

                /* fcmpush 보내는 코드
                 * 1. 클래스 만들어야 함
                 * 2. fcmPushTest.pushFCMNotification 의 첫 번째 인자 : 메세지 내용 / 두 번째 인자 : 매니저인지, 고객인지 / 받는 사람 이메일 - 이걸로 찾음 */
                FcmPushTest fcmPushTest = new FcmPushTest();

                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try
                        {
                            Log.e(TAG, "=== fcmPushTest ==="+fcmPushTest.toString() );

                            fcmPushTest.pushFCMNotification(managerNameStr+"매니저 님 에게 요청하신 "+datedayTxt.getText().toString()+"이 거절되었습니다." +
                                            "\n다른 매니저님과의 매칭을 기다리세요.",
                                    1, clientToken);

                        } catch (Exception e) {
                            e.printStackTrace();
                            Log.e(TAG, "fcmPushTest 에러 코드 : "+ e);
                        }
                    }
                });

                thread.start();
                Handler mHandler = new Handler(Looper.getMainLooper());
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Log.e(TAG, "fcmPushTest : run");

                    }
                }, 0);

                /* 2. 서비스의 매니저 이름을 매니저미지정 으로 변경하는 코드 + 해당 메서드 호출하면, 알람도 0으로 변경 */
                postUpdateServiceState(serviceIdInt, "매니저미지정"); //포스트로 받을 때, 매니저미지정이면 다른 sql문으로





            }
        });

        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "=== okBtn 수락하기 클릭 : === ");
                /* 1. 일반 고객에게 푸쉬 알람 갈 것(매칭 완료)
                *  2. 서비스의 상태를 매칭 완료 로 변경
                *  3. 매니저 내 서비스 목록 화면으로 이동하기
                *  4. 해당 알람 activate 상태를 0 으로 변경하기
                 */
                FcmPushTest fcmPushTest = new FcmPushTest();

                /* 1. 푸쉬 알람 스레드 */
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try  {
                            Log.e(TAG, "=== fcmPushTest ==="+fcmPushTest.toString() );

                            fcmPushTest.pushFCMNotification(datedayTxt.getText().toString()+"을 " +managerNameStr+" 매니저님이 수락하였습니다."
                                            , 1, clientToken);

                        } catch (Exception e) {
                            e.printStackTrace();
                            Log.e(TAG, "fcmPushTest 에러 코드 : "+ e);
                        }
                    }
                });

                thread.start();
                Handler mHandler = new Handler(Looper.getMainLooper());
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Log.e(TAG, "fcmPushTest : run");

                    }

                }, 0);

                /* 2. 서비스의 상태를 매칭 완료 로 변경하는 코드 + 해당 메서드 호출하면, 알람도 0으로 변경 */
                postUpdateServiceState(serviceIdInt, "매칭 완료");








            }
        });
    }



    /* 레트로핏으로 서비스 번호, 서비스 상태 보내면 상태 변경하는 코드 */
    private void postUpdateServiceState(int serviceId, String serviceState){

        Log.e(TAG, "=== postEmailAlarm 시작 ===" );

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(AlarmManagerSelectInterface.JSONURL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();

        ServiceStatesChangeInterface api =retrofit.create(ServiceStatesChangeInterface.class);
        /* 인터페이스에서 정의한 메서드 / 인자로 보낼 값 넣는 곳 */
        Call<String> call = api.updateServiceState(serviceId, serviceState);

        Log.e(TAG, "=== 서비스 id serviceId ===" + serviceId);
        Log.e(TAG, "=== 서비스 id serviceState ===" + serviceState);

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                Log.e("Responsestring", response.body());
                //Toast.makeText()
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        Log.e(TAG, "onSuccess" + response.body());
                        String jsonresponse = response.body();

                        // TODO: 2020-12-18 이 부분 이동하는거 확인해야 함.
                        if(jsonresponse.contains("매니저")){
                            /* 메세지에 매니저 문자열 포함된 경우임.
                            * 즉, 매니저가 거절한 경우 */
                            Log.d(TAG, "=== 거절 메세지에 매니저 문자열 포함된 경우임. ===" );

                            Intent intent = new Intent(getApplicationContext(), Manager_LoginActivity.class);

                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            Log.d(TAG, "=== 엑티비티 정리 ===" );

                            startActivity(intent);

                        }else{
                            /* 메세지에 매니저 문자열 포함 X
                            * 매니저가 수락한 경우
                            * 매니저의 내 서비스 목록록*/
                            Log.d(TAG, "=== 수락 메세지에 매니저 문자열 포함 X ===" );

                            Intent intent = new Intent(getApplicationContext(), Manager_LoginActivity.class);

                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            Log.d(TAG, "=== 엑티비티 정리 ===" );

                            startActivity(intent);

                        }


                    } else {
                        Log.e(TAG, "onEmptyResponse Returned empty response");
                    }
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e(TAG, "=== onFailure call ===" +call+" t"+t);
            }
        });
    }

    /* 레트로핏으로 이메일 보내서 알람 내용 확인하는 코드  */
    private void postEmailAlarm(String email){

        Log.e(TAG, "=== postEmailAlarm 시작 ===" );

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(AlarmManagerSelectInterface.JSONURL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();

        AlarmManagerSelectInterface api =retrofit.create(AlarmManagerSelectInterface.class);
        /* 인터페이스에서 정의한 메서드 / 인자로 보낼 값 넣는 곳 */
        Call<String> call = api.selectAlarmManager(email);

        Log.e(TAG, "=== 매니저 email ===" + email);

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                Log.e("Responsestring", response.body());
                //Toast.makeText()
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        Log.e("onSuccess", response.body());

                        String jsonresponse = response.body();
                        writeAlarmInfo(jsonresponse);


                    } else {
                        Log.e("onEmptyResponse", "Returned empty response");
                    }
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

                Log.e(TAG, "=== onFailure call ===" +call+" t"+t);

            }
        });
    }

    private void writeAlarmInfo(String response){

        try {
            //getting the whole json object from the response
            JSONObject obj = new JSONObject(response);
            Log.e(TAG, "=== response ===" +response);

            if(obj.optString("status").equals("true")){

                JSONArray dataArray  = obj.getJSONArray("data");


                for (int i = 0; i < dataArray.length(); i++) {

                    JSONObject dataobj = dataArray.getJSONObject(i);
                    serviceIdInt = dataobj.getInt("uid");
                    currentUser = dataobj.getString("currentUser");

                    /* 해당 일반고객의 토큰 값 받아오기  managerToken 미리 받아옴.*/
                    postSelectToken(currentUser, 1);


                    serviceState = dataobj.getString("serviceState");
                    myplaceDTO_placeName = dataobj.getString("myplaceDTO_placeName");
                    myplaceDTO_address = dataobj.getString("myplaceDTO_address");
                    myplaceDTO_sizeStr = dataobj.getString("myplaceDTO_sizeStr");

                    managerName = dataobj.getString("managerName");
                    Log.d(TAG, "=== ddddd managerName ===" +managerName);

                    serviceplus= dataobj.getString("serviceplus");

                    visitDate = dataobj.getString("visitDate");
                    visitDay = dataobj.getString("visitDay");
                    startTime = dataobj.getString("startTime");
                    needDefTime = dataobj.getString("needDefTime");
                    needDefCost = dataobj.getString("needDefCost");

                    cardCard_name= dataobj.getString("cardCard_name");
                    cardBilling_key = dataobj.getString("cardBilling_key");



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
                        Log.e(TAG, "=== startAndAllTimeTxt ===" + e);
                    }

                    // 3. 서비스 상태 보여주기
                    if(serviceState.contains("매칭 대기 중")){
                        Log.e(TAG, "=== serviceState.contains(매칭 대기 중) ===");

                        /* 매니저 이름 나누기 먼저 하고 작업할 것 이름, 이메일 -> "배수지,shp7401@naver.com"*/
                        managerNameStr = managerName.substring(0, 3);
                        managerEmailStr = managerName.substring(4);
                        Log.d(TAG, "=== ddddd managerNameStr ===" + managerNameStr);
                        Log.d(TAG, "=== ddddd managerEmailStr ===" + managerEmailStr);

                        stateTxt.setText("1건의 서비스 요청이 도착했습니다.\n수락하시겠습니까?");

                    }else{
                        Log.e(TAG, "=== serviceState.contains(매칭 대기 중) 포함 안 함. 확인!!!!!!! serviceState ==="+serviceState);
                    }

                    // 4. 집 상세정보 보여주기
                    // 내가 보는 경우에는, 장소 이름, 자세히 보기 가능. 그리고 변경하기 텍스트뷰도 보임. 물론, 서비스 이후에는 텍스트 뷰 보이면 안 됨.
                    placeNameTxt.setText(myplaceDTO_placeName);

                    try{
                        addressSizeTxt.setText(myplaceDTO_address.substring(8,14)+"("+myplaceDTO_sizeStr+")");
                    }catch (Exception e){
                        Log.e(TAG, "=== myplaceDTO_address myplaceDTO_sizeStr 널인 경우 에러코드 ===" +e);
                    }

                    // 선택한 장소의 평 수에 따라서 책정한 기본 가격 세팅
                    priceDefNumTxt.setText(formatter.format(Integer.parseInt(needDefCost))+" 원");

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

                    resultCostIntegerForBootpay = Integer.parseInt(needDefCost)+ironCostInt+fridgeCostInt;
                    Log.e(TAG, "=== resultCostIntegerForBootpay ===" +resultCostIntegerForBootpay);


                    int resultPrice = Integer.parseInt(needDefCost)+ironCostInt+fridgeCostInt;
                    resultPrice = (int) (resultPrice*0.9);

                    priceResutNum2Txt.setText(formatter.format(resultPrice)+" 원");
                }

            }else {
                Toast.makeText(Manager_AlarmActivity.this, obj.optString("message")+"", Toast.LENGTH_SHORT).show();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

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

    /* 레트로핏으로 서버에 이메일 보내서 토큰 받아오는 코드 - 서버에서 mysql에 저장 함*/
    private void postSelectToken(String email, int whichClientManager){

        Log.e(TAG, "=== postManagerProfile 시작 ===" );

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(TokenSelectInterface.JSONURL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();

        TokenSelectInterface api =retrofit.create(TokenSelectInterface.class);
        /* 인터페이스에서 정의한 메서드 / 인자로 보낼 값 넣는 곳 */
        Call<String> call = api.selectToken(email, whichClientManager);

        Log.e(TAG, "=== email ===" +email);
        Log.e(TAG, "=== whichClientManager 1이면 클라, 2면 매니저===" +whichClientManager);

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                Log.e("Responsestring", response.body());
                //Toast.makeText()
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        Log.e(TAG, "onSuccess" + response.body());

                        String jsonresponse = response.body();
                        Log.e(TAG, "=== jsonresponse ===" +jsonresponse );

                        try {
                            org.json.JSONObject obj = new org.json.JSONObject(jsonresponse);
                            Log.e(TAG, "=== response ===" +response);

                            if(obj.optString("status").equals("true")){

                                JSONArray dataArray  = obj.getJSONArray("data");

                                // TODO: 2020-12-15 여기서 해당 텍스트 뷰에 데이터 넣음

                                for (int i = 0; i < dataArray.length(); i++) {

                                    org.json.JSONObject dataobj = dataArray.getJSONObject(i);

                                    clientToken = dataobj.getString("Token");

                                    Log.e(TAG, "=== ddddddd === clientToken" +clientToken);



                                }
                            }else {
                                Toast.makeText(Manager_AlarmActivity.this, obj.optString("message")+"", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    } else {
                        Log.e(TAG, "onEmptyResponse"+"Returned empty response");//Toast.makeText(getContext(),"Nothing returned",Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

                Log.e(TAG, "=== onFailure call ===" +call+" t"+t);

            }
        });


    }


}