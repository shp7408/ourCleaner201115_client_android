package com.example.ourcleaner_201008_java.View;


import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ourcleaner_201008_java.CircleTransform;
import com.example.ourcleaner_201008_java.DTO.MyCardDTO;
import com.example.ourcleaner_201008_java.DTO.ServiceDTO;
import com.example.ourcleaner_201008_java.GlobalApplication;
import com.example.ourcleaner_201008_java.Interface.TokenSelectInterface;
import com.example.ourcleaner_201008_java.R;
import com.example.ourcleaner_201008_java.Service.FcmPushTest;
import com.google.firebase.iid.FirebaseInstanceId;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.LogoutResponseCallback;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.concurrent.TimeUnit;

import kr.co.bootpay.Bootpay;
import kr.co.bootpay.BootpayAnalytics;
import kr.co.bootpay.enums.Method;
import kr.co.bootpay.enums.PG;
import kr.co.bootpay.enums.UX;
import kr.co.bootpay.javaApache.BootpayApi;
import kr.co.bootpay.javaApache.model.request.Cancel;
import kr.co.bootpay.javaApache.model.request.SubscribeBilling;
import kr.co.bootpay.javaApache.model.request.User;
import kr.co.bootpay.listener.CancelListener;
import kr.co.bootpay.listener.CloseListener;
import kr.co.bootpay.listener.ConfirmListener;
import kr.co.bootpay.listener.DoneListener;
import kr.co.bootpay.listener.ErrorListener;
import kr.co.bootpay.listener.ReadyListener;
import kr.co.bootpay.model.BootExtra;
import kr.co.bootpay.model.BootUser;
import kr.co.bootpay.model.bio.BioPayload;
import kr.co.bootpay.model.bio.BioPrice;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class Service_completeActivity extends AppCompatActivity {

    private static final String TAG = "서비스신청 완료 엑티비티";

    /* 현재 엑티비티의 정보 담는 객체 */
    ServiceDTO serviceDTO3, serviceDTO2server; //이전 엑티비티에서 객체 받아오기 위한 객체, 다음 엑티비티로 객체 보내기 위한 객체

    //숫자 천 자리에 콤마 찍기
    DecimalFormat formatter = new DecimalFormat("###,###");

    int needDefTime, needDefCost; //평 수에 따라 책정된 시간(분), 가격

    int ironCostInt, fridgeCostInt;

    String status, code, message, data, token, server_time, expired_at;

    TextView serReqTxt, serTimeTxt, serPlaceTxt, serManagerTxt, paymentTxt;
    String serReqStr;
    Button paymentBtn, reserveBtn;
    LinearLayout priceDefLayout, priceIronLayout, pricefridgeLayout, priceResultLayout;
    TextView priceDefTxt, priceDefNumTxt, priceIronTxt, priceIronNumTxt, pricefridgeTxt, pricefridgeNumTxt, priceResultTxt, priceResutNumTxt;

    TextView serviceDetailTxt, serviceGarbageTxt, servicePlusTxt,cancelTxt;
    String focusRoomStr="", focusBathRoomStr="", focusLivingRoomStr="", focusKitchenStr=""; // 집중청소할구간 String. 서비스 내용에서 기본 부분에 넣을 String
    String laundryBoolStr=""; //세탁여부 보여주는 Str
    String garbagerecycleStr="", garbagenormalStr="", garbagefoodStr=""; //쓰레기선택여부 보여주는 Str
    String serviceIronStr="", serviceFridgeStr=""; //유료서비스 선택여부 보여주는 Str

    /* 카드 신규 등록 시 사용하는 변수 */
    String billing_key, pg_name,pg, method_name, method, card_code, card_name, e_at, c_at, receipt_id, action;

    Boolean isMyCardBool=true;

    String serManagerNameStr="";

    /* 서버에서 내 장소 정보 받아오기 위한 변수 */
    String jsonResponse;

    MyCardDTO myCardDTO;
    ArrayList<MyCardDTO> myCardDTOArrayList = new ArrayList<>();

    //최근 등록한 카드 정보
    int recentUid;
    String recentBilling_key, recentCard_name;

    String managerToken;

    String easyUserToken2;

    String application_id = "5fba1e488f075100207de71f";
    String private_key = "GjaGT62Fxto9XyMBL835hRqDwz02QxdSmPo7GeAtfek=";

    TextView serviceDeleteGuideTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_complete);
        Log.d(TAG, "=== onCreate ===" );

        //intent로 받을 때 사용하는 코드
        Intent intent = getIntent();
        Log.d(TAG, "Service1_TimeActivity에서 인텐트 받음 intent :"+intent);

        serviceDTO3 = (ServiceDTO) intent.getSerializableExtra("serviceDTO3");
        Log.d(TAG, "=== serviceDTO3 ===" + serviceDTO3.getCurrentUser() );


        serviceDeleteGuideTxt = findViewById(R.id.serviceDeleteGuideTxt);


        /* 이 부분 때문에 결제가 안되는 거였음..ㅠㅠㅠ */
        int SDK_INT = android.os.Build.VERSION.SDK_INT;
        if (SDK_INT > 8)
        {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
            //your codes here

        }

        /* 토큰 발행 시간 때문에 oncreate할 때 토큰 발행 미리 함 */
        /* 예약하기 버틑 클릭 -> 결제화면 이동 -> 결제 완료 -> 매니저 에게 푸쉬 알람 -> 서비스 내용 저장   */
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    BootpayApi bootpayApi = new BootpayApi("5fba1e488f075100207de721",
                            private_key);
                    bootpayApi.getAccessToken();

                    User user = new User();
                    user.user_id = "shp7408@naver.com";
                    user.email = "shp7408@naver.com";
                    user.name = "박서현";
                    user.gender = 0; //0:여자, 1:남자
                    user.birth = "941220";
                    user.phone = "01030517408";

                    try {
                        HttpResponse res = bootpayApi.getUserToken(user);

                        /* 아래 파싱 코드에서 토큰만 남길 것 임임 */
                        easyUserToken2 = IOUtils.toString(res.getEntity().getContent(), "UTF-8");

                        Log.e(TAG, "=== easyUserToken2 ===" +easyUserToken2);
//{"status":200,"code":0,"message":"","data":{"user_token":"5fdbc3818f07510034d3dec8","expired_unixtime":1608241553,"expired_localtime":"2020-12-18 06:45:53 +0900"}}
                        //부분을 파싱하는 코드

                        try{
                            JSONParser jsonParser = new JSONParser();

                            //JSON데이터를 넣어 JSON Object 로 만들어 준다.
                            JSONObject jsonObject = (JSONObject) jsonParser.parse(easyUserToken2);

                            //data 도 JSON Object JSON형식 이기 때문에 JSON Object 로 추출
                            JSONObject dataObject = (JSONObject) jsonObject.get("data");

                            easyUserToken2 = (String) dataObject.get("user_token");
                            Log.e(TAG, "=== easyUserToken2 파싱 완료 ===" +easyUserToken2);

                        }catch (Exception e){
                            Log.d(TAG, "=== 파싱 에러 코드 확인인 ===" + e);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.e(TAG, "=== HttpResponse res 에러코드 ===" +e);
                    }
                }catch (Exception e) {
                    e.printStackTrace();
                    Log.e(TAG, "postBillingKey 에러 코드 : "+ e);
                }
            }
        });
        thread.start();
        Handler mHandler = new Handler(Looper.getMainLooper());
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.e(TAG, "BootpayApi : run");
            }

        }, 0);

        serReqTxt = findViewById(R.id.serReqTxt);
        serReqTxt.setText(serviceDTO3.getVisitDate()+"("+serviceDTO3.getVisitDay()+")"+"\n예약을 완료해주세요.");
        Log.d(TAG, "=== serReqTxt ===" + serReqTxt.getText());

        serTimeTxt = findViewById(R.id.serTimeTxt);
        serTimeTxt.setText(serviceDTO3.getVisitDate()+"("+serviceDTO3.getVisitDay()+") / "+timeIntToHourMin1(serviceDTO3.getStartTime()) +"시작");

        serPlaceTxt = findViewById(R.id.serPlaceTxt);
        serPlaceTxt.setText(serviceDTO3.getMyplaceDTO().getPlaceNameStr()+" : "+serviceDTO3.getMyplaceDTO().getAddress().substring(8,14)+" ("+serviceDTO3.getMyplaceDTO().getSizeStr()+")");

        serManagerTxt = findViewById(R.id.serManagerTxt);

        serviceDetailTxt = findViewById(R.id.serviceDetailTxt);
        serviceGarbageTxt = findViewById(R.id.serviceGarbageTxt);
        servicePlusTxt = findViewById(R.id.servicePlusTxt);





        serManagerNameStr = serviceDTO3.getManagerName();

        try{


            if(serManagerNameStr.contains(",")){
                // 먼저 , 의 인덱스를 찾는다 - 인덱스 값: idx
                int idx = serManagerNameStr.indexOf(",");
                serManagerNameStr = serManagerNameStr.substring(0, idx);

                serManagerTxt.setText(serManagerNameStr+" 매니저님");
                Log.d(TAG, "=== getManagerName ==="+serviceDTO3.getManagerName() );

                String string = serviceDTO3.getManagerName().substring(idx+1);
                Log.d(TAG, "=== ddddd ==="+string );

                string = string.trim();

                Log.d(TAG, "=== ddddd ==="+string );

                /* 해당 매니저의 토큰 값 받아오기  managerToken 미리 받아옴.*/
                postSelectToken(string, 2);
            }



        }catch (Exception e){
            Log.e(TAG, "=== 매니저 미지정 경우, 에러코드임 ===" +e );
            Log.e(TAG, "=== 매니저 미지정 경우, serManagerNameStr ===" +serManagerNameStr );
        }




        Log.d(TAG, "=== 불린확인 ===" +serviceDTO3.getServicefocusedhashMap());

        /* 서비스 기본 내용 보여주는 내용 setText하는 코드 */
        if((Boolean) serviceDTO3.getServicefocusedhashMap().get("roomBtn")){
            Log.d(TAG, "=== roomBtn ===" );
            focusRoomStr="방";
            Log.d(TAG, "=== focusRoomStr ===" +focusRoomStr);
        }

        if((Boolean) serviceDTO3.getServicefocusedhashMap().get("bathRoomBtn")){
            Log.d(TAG, "=== bathRoomBtn ===" );
            focusBathRoomStr="화장실";
            Log.d(TAG, "=== focusBathRoomStr ===" +focusBathRoomStr);
        }

        if((Boolean) serviceDTO3.getServicefocusedhashMap().get("livingRoomBtn")){
            Log.d(TAG, "=== livingRoomBtn ===" );
            focusLivingRoomStr="거실";
            Log.d(TAG, "=== focusLivingRoomStr ===" +focusLivingRoomStr);
        }

        if((Boolean) serviceDTO3.getServicefocusedhashMap().get("kitchenBtn")){
            Log.d(TAG, "=== kitchenBtn ===" );
            focusKitchenStr="주방";
            Log.d(TAG, "=== focusKitchenStr ===" +focusKitchenStr);

        }
        if(serviceDTO3.getLaundryBool()){
            Log.d(TAG, "=== getLaundryBool ===" );
            laundryBoolStr="/ 세탁 추가";
        }






        String allStr = focusRoomStr +""+ focusBathRoomStr+""+focusLivingRoomStr+""+focusKitchenStr+" 꼼꼼히 "+laundryBoolStr;

        Log.d(TAG, "=== allStr ===" +allStr );

        serviceDetailTxt.setText(allStr);

        /* 쓰레기 배출 방법 보여주는 텍스트뷰에 setText하는 코드 */
        if(serviceDTO3.getGarbagerecycleBool()){
            garbagerecycleStr = "재활용";
        }

        if(serviceDTO3.getGarbagenormalBool()){
            garbagenormalStr = "일반";
        }

        if(serviceDTO3.getGarbagefoodBool()){
            garbagefoodStr = "음식물";
        }

        allStr = garbagerecycleStr+" "+garbagenormalStr+" "+garbagefoodStr+" 쓰레기 배출";
        serviceGarbageTxt.setText(allStr);

        /* 20201218 추가함.. ㅎㅎ */
        if(serviceDTO3.getGarbagerecycleBool() && !serviceDTO3.getGarbagenormalBool() && !serviceDTO3.getGarbagefoodBool()){
            serviceGarbageTxt.setText("재활용 분리수거 선택");
        }else if(!serviceDTO3.getGarbagerecycleBool() && serviceDTO3.getGarbagenormalBool() && !serviceDTO3.getGarbagefoodBool()){
            serviceGarbageTxt.setText("일반 쓰레기 배출 선택");
        }else if(!serviceDTO3.getGarbagerecycleBool() && !serviceDTO3.getGarbagenormalBool() && serviceDTO3.getGarbagefoodBool()){
            serviceGarbageTxt.setText("음식물 쓰레기 배출 선택");
        }else if(serviceDTO3.getGarbagerecycleBool() && serviceDTO3.getGarbagenormalBool() && !serviceDTO3.getGarbagefoodBool()){
            serviceGarbageTxt.setText("분리 수거 / 일반 쓰레기 배출 선택");
        }else if(serviceDTO3.getGarbagerecycleBool() && !serviceDTO3.getGarbagenormalBool() && serviceDTO3.getGarbagefoodBool()){
            serviceGarbageTxt.setText("분리 수거 / 음식물 쓰레기 배출 선택");
        }else if(!serviceDTO3.getGarbagerecycleBool() && serviceDTO3.getGarbagenormalBool() && serviceDTO3.getGarbagefoodBool()){
            serviceGarbageTxt.setText("일반 / 음식물 쓰레기 배출 선택");
        }else if(!serviceDTO3.getGarbagerecycleBool() && !serviceDTO3.getGarbagenormalBool() && !serviceDTO3.getGarbagefoodBool()){
            serviceGarbageTxt.setText("쓰레기 배출 미선택");
        }








        /* 유료 선택 추가 보여주는 텍스트뷰에 setText하는 코드 */
        if((Boolean)serviceDTO3.getServiceplus().get("다림질")){
            serviceIronStr = "다림질";
        }
        if((Boolean)serviceDTO3.getServiceplus().get("냉장고")){
            serviceFridgeStr = "냉장고 정리";
        }

        allStr = serviceIronStr+" "+serviceFridgeStr+" "+" 추가";
        Log.d(TAG, "=== 유료 선택 allStr ===" + allStr);
        servicePlusTxt.setText(allStr);

        /* 20201218 추가함. 이게 더 간단한데..ㅎㅎ 앞에서 삽질함. */
        if((Boolean)serviceDTO3.getServiceplus().get("다림질") && !(Boolean)serviceDTO3.getServiceplus().get("냉장고")){
            servicePlusTxt.setText("다림질 추가");
        }else if(! (Boolean)serviceDTO3.getServiceplus().get("다림질") && (Boolean)serviceDTO3.getServiceplus().get("냉장고")){
            servicePlusTxt.setText("냉장고 정리 추가");
        }else if((Boolean)serviceDTO3.getServiceplus().get("다림질") && (Boolean)serviceDTO3.getServiceplus().get("냉장고")){
            servicePlusTxt.setText("다림질  / 냉장고 정리 추가");
        }




        /* 결제 수단 보여주는 코드 */
        paymentTxt = findViewById(R.id.paymentTxt);
        paymentBtn = findViewById(R.id.paymentBtn);
        reserveBtn = findViewById(R.id.reserveBtn);

        Log.d(TAG, "=== makeStringRequestGet 여기 메서드에서 현재 사용자의 카드가 있는지 없는지 까지 확인하자.. ===" );
        makeStringRequestGet(); // 여기 메서드에서 현재 사용자의 카드가 있는지 없는지 까지 확인하자..
        Log.d(TAG, "=== 가장 최근 추가한 데이터 카드의 이름 dddd===recentCard_name : " +recentCard_name);


        priceDefLayout = findViewById(R.id.priceDefLayout);
        priceIronLayout = findViewById(R.id.priceIronLayout);
        pricefridgeLayout = findViewById(R.id.pricefridgeLayout);
        priceResultLayout = findViewById(R.id.priceResultLayout);

        // 선택한 장소의 평 수에 따라서 책정한 기본 가격 세팅
        priceDefNumTxt = findViewById(R.id.priceDefNumTxt);
        if(serviceDTO3.getMyplaceDTO().getSizeIndexint()>=0 && serviceDTO3.getMyplaceDTO().getSizeIndexint()<=4){
            Log.d(TAG, "=== 리사이클러 뷰 클릭 -> 평 수가 8평이하 ~ 12평 까지. ==="+ serviceDTO3.getMyplaceDTO().getSizeStr());
            Log.d(TAG, "=== 리사이클러 뷰 클릭 -> 평 수 인덱스 0 ~ 인덱스 4 ==="+ serviceDTO3.getMyplaceDTO().getSizeIndexint());

            needDefTime = 210;
            needDefCost = 45000;

        }else if(serviceDTO3.getMyplaceDTO().getSizeIndexint()>=5 && serviceDTO3.getMyplaceDTO().getSizeIndexint()<=38){
            Log.d(TAG, "=== 리사이클러 뷰 클릭 -> 평 수가 13평 ~ 50평 까지. ==="+ serviceDTO3.getMyplaceDTO().getSizeStr());
            Log.d(TAG, "=== 리사이클러 뷰 클릭 -> 평 수 인덱스 5 ~ 인덱스 38 ==="+ serviceDTO3.getMyplaceDTO().getSizeIndexint());

            needDefTime = 270;
            needDefCost = 55000;

        }else{
            Log.d(TAG, "=== 리사이클러 뷰 클릭 -> 평 수가 51평 ~ 101평 이상 까지. ==="+ serviceDTO3.getMyplaceDTO().getSizeStr());
            Log.d(TAG, "=== 리사이클러 뷰 클릭 -> 평 수 인덱스 39 ~ 인덱스 93 ==="+ serviceDTO3.getMyplaceDTO().getSizeIndexint());

            needDefTime = 330;
            needDefCost = 65000;
        }

        priceDefNumTxt.setText(formatter.format(needDefCost)+" 원");

        // 선택한 장소의 유료 선택지에 따라서 레이아웃 보이게 함.
        // 기본 가격
        priceIronNumTxt = findViewById(R.id.priceIronNumTxt);
        pricefridgeNumTxt = findViewById(R.id.pricefridgeNumTxt);

        if((Boolean) serviceDTO3.getServiceplus().get("다림질")){
            ironCostInt = 6600;

            priceIronNumTxt.setText(formatter.format(ironCostInt)+" 원");
            priceIronLayout.setVisibility(View.VISIBLE);
        }
        if((Boolean) serviceDTO3.getServiceplus().get("냉장고")){
            fridgeCostInt = 26400;

            pricefridgeNumTxt.setText(formatter.format(fridgeCostInt)+" 원");
            pricefridgeLayout.setVisibility(View.VISIBLE);
        }

        priceResutNumTxt = findViewById(R.id.priceResutNumTxt);

        priceResutNumTxt.setText(formatter.format(needDefCost+ironCostInt+fridgeCostInt)+" 원");


        Log.d(TAG, "=== 최근 등록한 카드 보여주는 메서드 ===" );

        /* 서비스 예약 취소 가이드 안내 다이얼로그 생성 */
        serviceDeleteGuideTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "=== serviceDeleteGuideTxt 결제 취소 가이드 클릭 시, 생성하는 다이얼로그 ===" );

                AlertDialog.Builder builder = new AlertDialog.Builder(Service_completeActivity.this);
                builder.setMessage("[취소 수수료 정책]" +
                        "\n\n- 청소 1일 전 오후 18시 이후 : 이용 금액의 30%" +
                        "\n\n- 청소 당일 시작 전 : 이용금액의 30%" +
                        "\n\n- 업무가 시작된 이후 : 이용금액의 100%");
                builder.setPositiveButton("확인했습니다.",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Log.d(TAG, "=== 확인 클릭  ===" );
                            }
                        });

                builder.show();

            }
        });



        /* 신규 등록할 수도 있고, 다른 카드로 등록할 수 도 있음 */
        paymentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "=== paymentBtn 클릭 ===");

                if(paymentBtn.getText().toString().equals("변경")|| paymentBtn.getText().toString().equals("등록")){
                    Log.e(TAG, "=== 버튼 텍스트가 변경인 경우, 즉 나의 등록 카드가 없는 경우, ===" );
                    BootUser bootUser = new BootUser().setPhone("010-3051-7408");
                    BootExtra bootExtra = new BootExtra().setQuotas(new int[] {0,2,3});

                    BootpayAnalytics.init(getApplicationContext(), application_id);
                    Bootpay.init(getFragmentManager())
                            .setContext(getApplicationContext())
                            .setApplicationId(application_id) // 해당 프로젝트(안드로이드)의 application id 값
                            .setBootUser(bootUser)
                            .setBootExtra(bootExtra)
                            .setPG(PG.DANAL) // 결제할 PG 사
                            .setMethod(Method.SUBSCRIPT_CARD) // 결제수단 지정
                            .setUX(UX.PG_SUBSCRIPT) // UX 지정
                            .setPrice(0) // 정기결제는 0원으로 지정
                            .setName("우리집 청소 카드 등록") // 결제할 상품명
                            .setOrderId("1234") // 결제 고유번호expire_month
                            .addItem("마우's 스", 1, "ITEM_CODE_MOUSE", 100) // 주문정보에 담길 상품정보, 통계를 위해 사용
                            .addItem("키보드", 1, "ITEM_CODE_KEYBOARD", 200, "패션", "여성상의", "블라우스") // 주문정보에 담길 상품정보, 통계를 위해 사용
                            .onConfirm(new ConfirmListener() { // 결제가 진행되기 바로 직전 호출되는 함수로, 주로 재고처리 등의 로직이 수행
                                @Override
                                public void onConfirm(@Nullable String message) {
                                    if (true) Bootpay.confirm(message); // 재고가 있을 경우.
                                    else Bootpay.removePaymentWindow(); // 재고가 없어 중간에 결제창을 닫고 싶을 경우
                                    Log.d("confirm", message);

                                }
                            })
                            .onDone(new DoneListener() { // 결제완료시 호출, 아이템 지급 등 데이터 동기화 로직을 수행합니다
                                @Override
                                public void onDone(@Nullable String message) {
                                    Log.d(TAG+"done", message);
                                    //jsonsimple 라이브러리 직접 추가 해야 함... 다욵받고 뭐 일 많음
                                    //JSONParser 을 쓰기 위함.

                                    JSONParser parser = new JSONParser();
                                    Object obj = null;
                                    try {
                                        obj = parser.parse( message );
                                        JSONObject jsonObj = (JSONObject) obj;

                                        billing_key = (String) jsonObj.get("billing_key"); //부트페이 subscriptionAPI로 요청 시, 필요한 빌링 키,
                                        //db에 저장 후, 부트페이 서버로 요청할 때, 보내야 함.
                                        pg_name = (String) jsonObj.get("pg_name"); //결제한 pg사 명
                                        pg = (String) jsonObj.get("pg"); // 결제한 PG사의 Alias
                                        method_name = (String) jsonObj.get("method_name"); //결제수단명
                                        method = (String) jsonObj.get("method"); //결제수단 Alias

//                            String data = (String) jsonObj.get("data"); //결제수단 Alias
//                            objdata = parser.parse( data );
                                        JSONObject jsonObjdata = (JSONObject) jsonObj.get("data");

                                        card_code = (String) jsonObjdata.get("card_code");
                                        card_name = (String) jsonObjdata.get("card_name"); //하나카드
                                        e_at = (String) jsonObj.get("e_at"); //정기 결제 가능한 마지막 유효날짜
                                        c_at = (String) jsonObj.get("c_at"); //정기 결제 요청 시간
//                                        receipt_id = (String) jsonObj.get("receipt_id"); //카드 등록 후,
//                            String status = (String) jsonObj.get("status"); // 에러 떠서 그냥 없앰.
                                        action = (String) jsonObj.get("action"); //정기 결제 요청 시간

                                        Log.d(TAG, "=== billing_key ===" +billing_key);
                                        Log.d(TAG, "=== pg_name ===" +pg_name);
                                        Log.d(TAG, "=== pg ===" +pg);
                                        Log.d(TAG, "=== method_name ===" +method_name);
                                        Log.d(TAG, "=== method ===" +method);

                                        Log.d(TAG, "=== data ===" +data);

                                        Log.d(TAG, "=== card_code ===" +card_code);
                                        Log.d(TAG, "=== card_name ===" +card_name);

                                        Log.d(TAG, "=== e_at ===" +e_at);
                                        Log.d(TAG, "=== c_at ===" +c_at);
//                                        Log.d(TAG, "=== receipt_id ===" +receipt_id);
//                            Log.d(TAG, "=== status ===" +status);
                                        Log.d(TAG, "=== action ===" +action);



                                        postData2();
                                        Log.e(TAG, "=== postData2() db에 저장 완료 ===" );



                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                        Log.e(TAG, "=== parse 과정에서 에러코드드 ===" +e);

                                    }


                                }
                            })
                            .onReady(new ReadyListener() { // 가상계좌 입금 계좌번호가 발급되면 호출되는 함수입니다.
                                @Override
                                public void onReady(@Nullable String message) {
                                    Log.d(TAG+"ready", message);
                                }
                            })
                            .onCancel(new CancelListener() { // 결제 취소시 호출
                                @Override
                                public void onCancel(@Nullable String message) {
                                    Log.d(TAG+"cancel", message);
                                }
                            })
                            .onError(new ErrorListener() { // 에러가 났을때 호출되는 부분
                                @Override
                                public void onError(@Nullable String message) {
                                    Log.d(TAG+"error", message);
                                }
                            })
                            .onClose(new CloseListener() { //결제창이 닫힐때 실행되는 부분
                                @Override
                                public void onClose(String message) {
                                    Log.d(TAG+"close", "close");


                                }
                            })
                            .request();
                }
                else{
                    Log.e(TAG, "=== 버튼 텍스트가 그 외, 등록인 경우, 즉 내가 등록한 카드가 있는 경우, ===" );
                    // TODO: 2020-11-27 커스텀 다이얼로그로 리사이클러 뷰 넣어서 등록한 카드 보여주기 해야 함.



                }


            }
        });

        // 예약하기 버튼 클릭
        reserveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "=== reserveBtn 클릭 ===" );






                BootUser bootUser = new BootUser().setPhone("01030517408");
                BootExtra bootExtra = new BootExtra().setQuotas(new int[] {0,2,3});

                Log.e(TAG, "=== bootUser ==="+bootUser.toString());
                Log.e(TAG, "=== bootExtra ==="+bootExtra.toString());
                BioPayload bioPayload = new BioPayload();
                bioPayload.setPg(PG.PAYAPP)
                    .setName("우리집 청소")
                    .setPrice((double) (needDefCost+ironCostInt+fridgeCostInt)) //최종 결제 금액
                    .setOrder_id(String.valueOf(System.currentTimeMillis()))
                    .setName("우리집 청소")

                    .setNames(Arrays.asList("우리집 청소",
                            serviceDTO3.getMyplaceDTO().getAddress().substring(8,14),
                            " ("+serviceDTO3.getMyplaceDTO().getSizeStr()+")"))

                    .setPrices(Arrays.asList(new BioPrice("기본 가격", (double) needDefCost),
                            new BioPrice("유료 추가 1 다림질", (double) ironCostInt),
                            new BioPrice("유료 추가 2 냉장고 정리", (double) fridgeCostInt)));


                int stuck = 1;
                Bootpay.init(getSupportFragmentManager())
                    .setContext(getApplicationContext())
                    .setApplicationId(application_id)
                    .setOrderId(String.valueOf(System.currentTimeMillis()))
                    .setBootExtra(bootExtra)
                    .setBootUser(bootUser)
                    .setBioPayload(bioPayload)
                    .setEasyPayUserToken(easyUserToken2)
                    .setAccountExpireAt("2021-07-16")
                    .addItem("유료 추가 1 다림질", 1, "ITEM_CODE_MOUSE", 500)
                    .addItem("유료 추가 2 냉장고 정리", 1, "ITEM_CODE_KEYBOARD", 500, "패션", "여성상의", "블라우스")
                    .onConfirm(new ConfirmListener() {
                        @Override
                        public void onConfirm(@Nullable String data) {
                            if (0 < stuck) Bootpay.confirm(data);
                            else Bootpay.removePaymentWindow();

                            Log.d("bootpay confirm", data);
                        }
                    })
                    .onDone(new DoneListener() {
                        @Override
                        public void onDone(@Nullable String data) {
                            Log.e(TAG, "bootpay done !!!!!!!!!!!!!!!!  "+ data);
                            //{"receipt_id":"5fdbc6a98f07510034d3dee4",
                            // "price":61600,"card_no":"40285712****7317",
                            // "card_code":"07","card_name":"현대",
                            // "card_quota":"00",
                            // "receipt_url":"https://app.bootpay.co.kr/bill/clFxVmNCWFRBUEQ2V081dEZrZG9ldFJpVWNrYUljUVRCZ0xrc0tOd1dsNHYv%0AUT09LS1sbkUxRHcyWWtlWVM1TVh4LS1LalVZeFFSYnhDRC8vcExOTndBYUpR%0APT0%3D%0A",
                            // "item_name":"우리집 청소","order_id":"1608238756576","url":"https://app.bootpay.co.kr",
                            // "tax_free":0,"payment_name":"카드정기결제(REST)","pg_name":"페이앱",
                            // "pg":"payapp","method":"card_rebill_rest","method_name":"카드정기결제(REST)",
                            // "payment_group":"card","payment_group_name":"신용카드","requested_at":"2020-12-18 05:59:21",
                            // "purchased_at":"2020-12-18 05:59:31","status":1,
                            // "payment_data":
                            // {"card_name":"현대","card_no":"40285712****7317","card_quota":"00",
                            // "card_code":"07","card_auth_no":"00436501","receipt_id":"5fdbc6a98f07510034d3dee4",
                            // "n":"우리집 청소","p":61600,"tid":"59463435","pg":"페이앱","pm":"카드정기결제(REST)",
                            // "pg_a":"payapp","pm_a":"card_rebill_rest","o_id":"1608238756576",
                            // "p_at":"2020-12-18 05:59:31","s":1,"g":2},"action":"BootpayDone"
                            // }

                            try{
                                JSONParser jsonParser = new JSONParser();
                                //JSON데이터를 넣어 JSON Object 로 만들어 준다.
                                JSONObject jsonObject = (JSONObject) jsonParser.parse(data);

                                receipt_id= (String) jsonObject.get("receipt_id");
                                Log.e(TAG, "=== receipt_id ===" +receipt_id);


                            }catch (Exception e){

                            }

                            Bootpay.dismiss();

                            // TODO: 2020-12-18  결제 완료 하고, 결제한 서비스 내용 저장하는 부분
                            postData();

                        }
                    })
                    .onReady(new ReadyListener() {
                        @Override
                        public void onReady(@Nullable String data) {
                            Log.d("bootpay ready", data);
                        }
                    })
                    .onCancel(new CancelListener() {
                        @Override
                        public void onCancel(@Nullable String data) {

                            Log.d("bootpay cancel", data);
                        }
                    })
                    .onError(new ErrorListener() {
                        @Override
                        public void onError(@Nullable String data) {
                            Log.d("bootpay error", data);
                            Bootpay.dismiss();
                        }
                    })
                    .onClose(new CloseListener() {
                        @Override
                        public void onClose(String data) {
                            Log.d("bootpay close", "close");
                        }
                    })
                    .requestBio();






            }
        });

    }

    /* 서비스 내용 서버 db에 저장하는 코드 */
    public void postData(){
            Log.d(TAG, "=== postData() ===" );
            // Request를 보낼 queue를 생성한다. 필요시엔 전역으로 생성해 사용하면 된다.
            RequestQueue queue = Volley.newRequestQueue(this);
            // 대표적인 예로 androidhive의 테스트 url을 삽입했다. 이부분을 자신이 원하는 부분으로 바꾸면 될 터
            String url = "http://52.79.179.66/insertService2.php";

            final StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.d(TAG, "=== stringRequest === onResponse" );

                    // TODO: 2020-11-24 다이얼로그로 "예약이 완료되었습니다. 매칭 완료 시, "

                    AlertDialog.Builder builder = new AlertDialog.Builder(Service_completeActivity.this);
                    builder.setMessage("예약이 완료되었습니다.");
                    builder.setPositiveButton("확인",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    Log.d(TAG, "=== 확인 클릭  ===" );

                                    //UserManagement API 요청을 담당
                                    UserManagement.getInstance()
                                            .requestLogout(new LogoutResponseCallback() {
                                                @Override
                                                public void onCompleteLogout() {
                                                    Log.e(TAG, "=== onCompleteLogout : 예약이 완료되었습니다. ===");

                                                    // TODO: 2020-12-15 fcm 보내는 부분
                                                    FcmPushTest fcmPushTest = new FcmPushTest();
                                                    try {
                                                        fcmPushTest.pushFCMNotification(serManagerTxt.getText().toString()+" 에게 1건의 청소 요청이 들어왔습니다. 확인해주세요.",
                                                                2,
                                                                managerToken);
                                                    } catch (Exception e) {
                                                        e.printStackTrace();
                                                        Log.d(TAG, "=== pushFCMNotification 에러코드 매니저 미지저 경우. 그냥 넘어가도 딤 ==="+e );
                                                    }


                                                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);

                                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                                    Log.d(TAG, "=== 엑티비티 정리 ===" );

                                                    startActivity(intent);
                                                }
                                            });
                                }
                            });

                    builder.show();



                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d(TAG, "=== stringRequest === error :" +error);
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {

                    Map<String, String> params = new HashMap<String, String>();
                    params.put("currentUser", serviceDTO3.getCurrentUser());
                    params.put("serviceState", "매칭 대기 중");

                    params.put("myplaceDTO_placeName", serviceDTO3.getMyplaceDTO().getPlaceNameStr());
                    params.put("myplaceDTO_address", serviceDTO3.getMyplaceDTO().getAddress());
                    params.put("myplaceDTO_detailAddress", serviceDTO3.getMyplaceDTO().getDetailAddress());
                    params.put("myplaceDTO_sizeStr", serviceDTO3.getMyplaceDTO().getSizeStr());

                    params.put("managerName", serviceDTO3.getManagerName());
                    params.put("regularBool", String.valueOf(serviceDTO3.getRegularBool()));
                    params.put("visitDate", serviceDTO3.getVisitDate());
                    params.put("visitDay", serviceDTO3.getVisitDay());
                    params.put("startTime", String.valueOf(serviceDTO3.getStartTime()));
                    params.put("needDefTime", String.valueOf(serviceDTO3.getNeedDefTime()));
                    params.put("needDefCost", String.valueOf(serviceDTO3.getNeedDefCost()));

                    params.put("servicefocusedhashMap", String.valueOf(serviceDTO3.getServicefocusedhashMap()));
                    params.put("laundryBool", String.valueOf(serviceDTO3.getLaundryBool()));
                    params.put("laundryCaution", serviceDTO3.getLaundryCaution());

                    params.put("garbagerecycleBool", String.valueOf(serviceDTO3.getGarbagefoodBool()));
                    params.put("garbagenormalBool", String.valueOf(serviceDTO3.getGarbagenormalBool()));
                    params.put("garbagefoodBool", String.valueOf(serviceDTO3.getGarbagefoodBool()));
                    params.put("garbagehowto", serviceDTO3.getGarbagehowto());

                    params.put("serviceplus", String.valueOf(serviceDTO3.getServiceplus()));
                    params.put("serviceCaution", serviceDTO3.getServiceCaution());

                    params.put("recentUid", String.valueOf(recentUid));
                    params.put("recentBilling_key", recentBilling_key);
                    params.put("recentCard_name", recentCard_name);
                    params.put("receipt_id", receipt_id);

                    Log.d(TAG, "=== params === : "+ params );

                    Log.e(TAG, "=== receipt_id === : "+ receipt_id );

                    return params;
                }
            };

            stringRequest.setTag(TAG);

            queue.add(stringRequest);
        }

        /* 신규 등록한 카드 정보를 db에 저장하는 코드 */
    public void postData2(){
        Log.e(TAG +"123", "=== postData2() ===" );
        // Request를 보낼 queue를 생성한다. 필요시엔 전역으로 생성해 사용하면 된다.
        RequestQueue queue = Volley.newRequestQueue(this);
        // 대표적인 예로 androidhive의 테스트 url을 삽입했다. 이부분을 자신이 원하는 부분으로 바꾸면 될 터
        String url = "http://52.79.179.66/insertMyCard.php";

        final StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e(TAG+"123", "=== stringRequest ===onResponse " +response);

                try {
                    JSONArray jsonArray = new JSONArray(response);
                    if(jsonArray.length()>0){
                        Log.e(TAG, "=== 아이디 비밀번호 맞는 경우 jsonArray 값이 있음 ===" );

                        for(int i = 0 ; i<jsonArray.length(); i++){

                            org.json.JSONObject jsonObject = jsonArray.getJSONObject(i);
                            int uid = jsonObject.getInt( "uid" );
                            String currentUser = jsonObject.getString( "currentUser" );
                            String billing_key = jsonObject.getString( "billing_key" );
                            String pg_name = jsonObject.getString( "pg_name" );
                            String pg = jsonObject.getString( "pg" );
                            String card_name = jsonObject.getString( "card_name" );
                            String c_at = jsonObject.getString( "c_at" );
                            String receipt_id = jsonObject.getString( "receipt_id" );

                            Log.e(TAG+"123", "=== uid ===" +uid);
                            Log.e(TAG+"123", "=== currentUser ===" +currentUser);
                            Log.e(TAG+"123", "=== billing_key ===" +billing_key);
                            Log.e(TAG+"123", "=== pg_name ===" +pg_name);
                            Log.e(TAG+"123", "=== pg ===" +pg);
                            Log.e(TAG+"123", "=== card_name ===" +card_name);
                            Log.e(TAG+"123", "=== c_at ===" +c_at);
                            Log.e(TAG+"123", "=== receipt_id ===" +receipt_id);

                            /* 등록한 카드 정보 가져오는 코드 */
                            recentUid=uid;
                            recentBilling_key= billing_key;
                            recentCard_name=card_name;

                            Log.e(TAG+"123", "=== recentUid ===" +recentUid);
                            Log.e(TAG+"123", "=== recentBilling_key ===" +recentBilling_key);
                            Log.e(TAG+"123", "=== recentCard_name ===" +recentCard_name);

                            /* 받아온 데이터 setText 하는 코드임 */
                            paymentTxt.setText(card_name);
                            Log.e(TAG+"123", "=== paymentTxt 변경 ===" +recentUid);

                        }

                    }else{
                        Log.e(TAG, "=== 아이디 비밀번호 틀린 경우 jsonArray 값이 없음===" );
                        Toast.makeText( getApplicationContext(), "아이디 비밀번호를 확인해주세요.", Toast.LENGTH_SHORT ).show();

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e(TAG, "=== response === 에러코드 : " +e);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG+"123", "=== stringRequest === error :" +error);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<String, String>();
                params.put("currentUser", GlobalApplication.currentUser);
                params.put("billing_key", billing_key);

                params.put("pg_name", pg_name);
                params.put("pg", pg);
                params.put("method_name", method_name);

                params.put("method", method);
                params.put("card_code", card_code);
                params.put("card_name", card_name);
                params.put("e_at", e_at);
                params.put("c_at", c_at);
                params.put("receipt_id", receipt_id);
                params.put("action", action);



                Log.d(TAG, "=== params === : "+ params );

                return params;
            }
        };

        stringRequest.setTag(TAG);

        queue.add(stringRequest);
    }



    //int 형태의 정수를 "3시간 30분" String으로 나타내는 메서드
    public String timeIntToHourMin1(int plusTimeInt){

        long hour = TimeUnit.MINUTES.toHours(plusTimeInt); // 분을 시간으로 변경
        Log.d(TAG, "=== hour ===" +hour);

        long minutes = TimeUnit.MINUTES.toMinutes(plusTimeInt) - TimeUnit.HOURS.toMinutes(hour); // 시간으로 변경하고, 나머지 분
        Log.d(TAG, "=== minutes ==="+minutes );

        //이거 추가 해야 함.
        String plusTimeStr;

        if(hour==0){
            Log.d(TAG, "=== hour==0  ===" );
            plusTimeStr =  minutes + "분";
            Log.d(TAG, "=== plusTimeStr ===" +plusTimeStr);
        }else if(minutes==0){
            Log.d(TAG, "=== minutes==0 ===" );
            plusTimeStr = hour + "시 ";
            Log.d(TAG, "=== plusTimeStr ===" +plusTimeStr);
        }else{
            Log.d(TAG, "=== hour 랑 minutes 둘 다 0이 아닌, 경우 ===" );
            plusTimeStr = hour +"시  "+ minutes + "분";
            Log.d(TAG, "=== plusTimeStr ===" +plusTimeStr);
        }

        return plusTimeStr;
    }


    /* 내 계정으로 등록된 카드 가져오는 메서드 */
    private void makeStringRequestGet() {

        String url = "http://52.79.179.66/myCardCheck.php?currentUser="+ GlobalApplication.currentUser;
        JsonArrayRequest req = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, "makeStringRequestGet onResponse 성공!!!!!!!!!!!!!!!!!!"+response.toString());

                        try {
                            // Parsing json array response
                            // loop through each json object
                            jsonResponse = "";
                            Log.d(TAG, "=== jsonResponse 반복문 이전 ===" +jsonResponse);

                            //리사이클러뷰에 여러개 추가되는 것 막음
                            myCardDTOArrayList.clear();

                            for (int i = 0; i < response.length(); i++) {

                                Log.d(TAG, "===  === i :" + i);
                                org.json.JSONObject myCard = (org.json.JSONObject) response.get(i);

                                int uid = myCard.getInt("uid");
                                String currentUser = myCard.getString("currentUser");

                                String billing_key = myCard.getString("billing_key");
                                String pg_name = myCard.getString("pg_name");
                                String pg = myCard.getString("pg");

                                String method_name = myCard.getString("method_name");
                                String method = myCard.getString("method");

                                String card_code = myCard.getString("card_code");
                                String card_name = myCard.getString("card_name");
                                String e_at = myCard.getString("e_at");
                                String c_at = myCard.getString("c_at");
                                String receipt_id = myCard.getString("receipt_id");
                                String action = myCard.getString("action");

                                jsonResponse += "uid: " + uid + "\n\n";
                                jsonResponse += "currentUser: " + currentUser + "\n\n";

                                jsonResponse += "billing_key: " + billing_key + "\n\n";
                                jsonResponse += "pg_name: " + pg_name + "\n\n";
                                jsonResponse += "pg: " + pg + "\n\n";

                                jsonResponse += "method_name: " + method_name + "\n\n";
                                jsonResponse += "method: " + method + "\n\n";

                                jsonResponse += "card_code: " + card_code + "\n\n";
                                jsonResponse += "card_name: " + card_name + "\n\n";
                                jsonResponse += "e_at: " + e_at + "\n\n";
                                jsonResponse += "c_at: " + c_at + "\n\n";
                                jsonResponse += "receipt_id: " + receipt_id + "\n\n";
                                jsonResponse += "action: " + action + "\n\n";


                                myCardDTO = new MyCardDTO(uid, currentUser, billing_key, pg_name, pg, method_name, method, card_code, card_name, e_at, c_at, receipt_id, action);
                                Log.d(TAG, "=== myCardDTO 객체 생성 ===");

                                myCardDTOArrayList.add(myCardDTO);
                                Log.d(TAG, "=== myCardDTOArrayList.add(myCardDTO)에 더함 ===");

                                // TODO: 2020-11-27 가장 최근 추가한 카드 정보를 setText 할 것 
                                if(i==(response.length()-1)){

                                    recentUid = uid;
                                    recentBilling_key = billing_key;
                                    recentCard_name = card_name;

                                    Log.d(TAG, "=== 가장 최근 추가한 데이터 카드의 이름 ===recentUid : " +recentUid);
                                    Log.d(TAG, "=== 가장 최근 추가한 데이터 카드의 이름 ===recentBilling_key : " +recentBilling_key);
                                    Log.d(TAG, "=== 가장 최근 추가한 데이터 카드의 이름 ===recentCard_name : " +recentCard_name);

                                    paymentTxt = findViewById(R.id.paymentTxt);
                                    paymentBtn = findViewById(R.id.paymentBtn);

                                    paymentTxt.setText(recentCard_name);
                                    paymentBtn.setText("변경");
                                }
                            }

                            if(jsonResponse.isEmpty()){
                                Log.d(TAG, "=== 반복문 이후에도 jsonResponse 비어 있음 현재 사용자가 등록한 카드가 없음 ===" );

                                paymentTxt.setText("등록된 카드가 없습니다.");
                                paymentBtn.setText("등록");
                            }
                            Log.d(TAG, "=== jsonResponse 반복문 이후 ===" +jsonResponse);

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.d(TAG, "===  === Error " + e);
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                Log.d(TAG, "===  === Error " + error);
            }
        });

        // Adding request to request queue
        GlobalApplication.getInstance().addToRequestQueue(req);
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

                                    managerToken = dataobj.getString("Token");

                                    Log.e(TAG, "=== ddddddd ===managerToken" +managerToken);



                                }
                            }else {
                                Toast.makeText(Service_completeActivity.this, obj.optString("message")+"", Toast.LENGTH_SHORT).show();
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