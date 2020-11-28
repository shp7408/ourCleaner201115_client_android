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
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import androidx.annotation.Nullable;
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

import com.example.ourcleaner_201008_java.DTO.MyCardDTO;
import com.example.ourcleaner_201008_java.DTO.MyReservationDTO;
import com.example.ourcleaner_201008_java.DTO.ServiceDTO;
import com.example.ourcleaner_201008_java.GlobalApplication;
import com.example.ourcleaner_201008_java.R;
import com.example.ourcleaner_201008_java.SharedP.PreferenceManager_Manager;
import com.example.ourcleaner_201008_java.View.Manager.Manager_LoginActivity;
import com.example.ourcleaner_201008_java.View.Manager.Manager_MainActivity;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.LogoutResponseCallback;

import java.text.DecimalFormat;
import java.util.concurrent.TimeUnit;

import kr.co.bootpay.Bootpay;
import kr.co.bootpay.BootpayAnalytics;
import kr.co.bootpay.enums.Method;
import kr.co.bootpay.enums.PG;
import kr.co.bootpay.enums.UX;
import kr.co.bootpay.listener.CancelListener;
import kr.co.bootpay.listener.CloseListener;
import kr.co.bootpay.listener.ConfirmListener;
import kr.co.bootpay.listener.DoneListener;
import kr.co.bootpay.listener.ErrorListener;
import kr.co.bootpay.listener.ReadyListener;
import kr.co.bootpay.model.BootExtra;
import kr.co.bootpay.model.BootUser;

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

    TextView serviceDetailTxt, serviceGarbageTxt, servicePlusTxt;
    String focusRoomStr="", focusBathRoomStr="", focusLivingRoomStr="", focusKitchenStr=""; // 집중청소할구간 String. 서비스 내용에서 기본 부분에 넣을 String
    String laundryBoolStr=""; //세탁여부 보여주는 Str
    String garbagerecycleStr="", garbagenormalStr="", garbagefoodStr=""; //쓰레기선택여부 보여주는 Str
    String serviceIronStr="", serviceFridgeStr=""; //유료서비스 선택여부 보여주는 Str

    /* 카드 신규 등록 시 사용하는 변수 */
    String billing_key, pg_name,pg, method_name, method, card_code, card_name, e_at, c_at, receipt_id, action;

    Boolean isMyCardBool=true;

    /* 서버에서 내 장소 정보 받아오기 위한 변수 */
    String jsonResponse;

    MyCardDTO myCardDTO;
    ArrayList<MyCardDTO> myCardDTOArrayList = new ArrayList<>();

    //최근 등록한 카드 정보
    int recentUid;
    String recentBilling_key, recentCard_name;

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

        Log.d(TAG, "=== 불린확인 ===" +serviceDTO3.getServicefocusedhashMap());
        /* 서비스 기본 내용 보여주는 내용 setText하는 코드 */
        try{
            if((Boolean) serviceDTO3.getServicefocusedhashMap().get("roomBtn")){
                Log.d(TAG, "=== roomBtn ===" );
                focusRoomStr="방";
                Log.d(TAG, "=== focusRoomStr ===" +focusRoomStr);
            }

        }catch (Exception e){
            //홈페이지에서 더 많은 컨텐츠를 확인하세요,  https://stickode.com/
            Log.e(TAG, "=== 불린확인 roomBtn 널인 경우임. ==="+e );
        }

        try{
            if((Boolean) serviceDTO3.getServicefocusedhashMap().get("bathRoomBtn")){
                Log.d(TAG, "=== bathRoomBtn ===" );
                focusBathRoomStr="화장실";
                Log.d(TAG, "=== focusBathRoomStr ===" +focusBathRoomStr);
            }

        }catch (Exception e){
            //홈페이지에서 더 많은 컨텐츠를 확인하세요,  https://stickode.com/
            Log.e(TAG, "=== 불린확인 bathRoomBtn 널인 경우임. ==="+e );
        }

        try{
            if((Boolean) serviceDTO3.getServicefocusedhashMap().get("livingRoomBtn")){
                Log.d(TAG, "=== livingRoomBtn ===" );
                focusLivingRoomStr="거실";
                Log.d(TAG, "=== focusLivingRoomStr ===" +focusLivingRoomStr);
            }
        }catch (Exception e){
            //홈페이지에서 더 많은 컨텐츠를 확인하세요,  https://stickode.com/
            Log.e(TAG, "=== 불린확인 livingRoomBtn 널인 경우임. ==="+e );
        }

        try{
            if((Boolean) serviceDTO3.getServicefocusedhashMap().get("kitchenBtn")){
                Log.d(TAG, "=== kitchenBtn ===" );
                focusKitchenStr="주방";
                Log.d(TAG, "=== focusKitchenStr ===" +focusKitchenStr);

            }

        }catch (Exception e){
            //홈페이지에서 더 많은 컨텐츠를 확인하세요,  https://stickode.com/
            Log.e(TAG, "=== 불린확인 kitchenBtn 널인 경우임. ==="+e );
        }

        try{
            if((Boolean) serviceDTO3.getLaundryBool()){
                Log.d(TAG, "=== getLaundryBool ===" );
                laundryBoolStr="/ 세탁 추가";
            }
        }catch (Exception e){
            Log.e(TAG, "=== 불린확인 getLaundryBool 널인 경우임. ==="+e );
        }

        String allStr = focusRoomStr +""+ focusBathRoomStr+""+focusLivingRoomStr+""+focusKitchenStr+" 꼼꼼히 "+laundryBoolStr;

        Log.d(TAG, "=== allStr ===" +allStr );

        serviceDetailTxt.setText(allStr);

        /* 쓰레기 배출 방법 보여주는 텍스트뷰에 setText하는 코드 */
        try{
            if(serviceDTO3.getGarbagerecycleBool()){
                garbagerecycleStr = "재활용";
            }
        }catch (Exception e){
          Log.d(TAG, "=== 에러코드 널인 경우임 getGarbagerecycleBool ===" +e);
        }

        try{
            if(serviceDTO3.getGarbagenormalBool()){
                garbagenormalStr = "일반";
            }
        }catch (Exception e){
            Log.d(TAG, "=== 에러코드 널인 경우임 getGarbagenormalBool ===" +e);
        }

        try{
            if(serviceDTO3.getGarbagefoodBool()){
                garbagefoodStr = "음식물";
            }
        }catch (Exception e){
            Log.d(TAG, "=== 에러코드 널인 경우임 getGarbagefoodBool ===" +e);
        }

        allStr = garbagerecycleStr+" "+garbagenormalStr+" "+garbagefoodStr+" 쓰레기 배출";

        serviceGarbageTxt.setText(allStr);


        /* 유료 선택 추가 보여주는 텍스트뷰에 setText하는 코드 */
        try{
            if((Boolean)serviceDTO3.getServiceplus().get("다림질")){
                serviceIronStr = "다림질";
            }
        }catch (Exception e){
            Log.d(TAG, "=== 에러코드 널인 경우임 getServiceplus 다림질 ===" +e);
        }

        try{
            if(serviceDTO3.getGarbagefoodBool()){
                serviceFridgeStr = "냉장고 정리";
            }
        }catch (Exception e){
            Log.d(TAG, "=== 에러코드 널인 경우임 getServiceplus 냉장고 정리 ===" +e);

        }

        allStr = serviceIronStr+" "+serviceFridgeStr+" "+" 추가";

        servicePlusTxt.setText(allStr);



        /*
        * 결제 수단 보여주는 코드
        * */
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



        /* 신규 등록할 수도 있고, 다른 카드로 등록할 수 도 있음 */
        paymentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "=== paymentBtn 클릭 ===");

                if(paymentBtn.getText().toString().equals("변경")|| paymentBtn.getText().toString().equals("등록")){
                    Log.e(TAG, "=== 버튼 텍스트가 변경인 경우, 즉 나의 등록 카드가 없는 경우, ===" );
                    BootUser bootUser = new BootUser().setPhone("010-1234-5678");
                    BootExtra bootExtra = new BootExtra().setQuotas(new int[] {0,2,3});

                    BootpayAnalytics.init(getApplicationContext(), "5fba1e488f075100207de71f");
                    Bootpay.init(getFragmentManager())
                            .setContext(getApplicationContext())
                            .setApplicationId("5fba1e488f075100207de71f") // 해당 프로젝트(안드로이드)의 application id 값
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
                                        receipt_id = (String) jsonObj.get("receipt_id"); //카드 등록 후,
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
                                        Log.d(TAG, "=== receipt_id ===" +receipt_id);
//                            Log.d(TAG, "=== status ===" +status);
                                        Log.d(TAG, "=== action ===" +action);



                                        postData2();
                                        Log.e(TAG, "=== postData2() db에 저장 완료 ===" );



                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                        Log.e(TAG, "=== parse 과정에서 에러코드드 ===" +e);

                                    }
                                    JSONObject jsonObj = (JSONObject) obj;

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

                    postData3();

                }


            }
        });



        // 예약하기 버튼 클릭
        reserveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "=== reserveBtn 클릭 ===" );

                postData();

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
                    builder.setMessage("예약이 완료되었습니다. \n매칭 완료 시, 등록하신 카드로 자동 결제됩니다.");
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
                                                    Log.d(TAG, "=== onCompleteLogout : 예약이 완료되었습니다. ===");

                                                    //있으면 넘어감
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


                    Log.d(TAG, "=== params === : "+ params );
                    Log.d(TAG, "=== recentUid === : "+ String.valueOf(recentUid) );
                    Log.d(TAG, "=== recentBilling_key === : "+ recentBilling_key );
                    Log.d(TAG, "=== recentCard_name === : "+ recentCard_name );

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

    /* 최근 등록한 카드 정보 가져오는 코드 */
    public void postData3(){
        Log.e(TAG +"postData3", "=== postData2() ===" );
        // Request를 보낼 queue를 생성한다. 필요시엔 전역으로 생성해 사용하면 된다.
        RequestQueue queue = Volley.newRequestQueue(this);
        // 대표적인 예로 androidhive의 테스트 url을 삽입했다. 이부분을 자신이 원하는 부분으로 바꾸면 될 터
        String url = "http://52.79.179.66/selectMyCard.php";

        final StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e(TAG+"postData3", "=== response ===" +response);
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    if(jsonArray.length()>0){
                        Log.e(TAG+"postData3", "=== 최근 등록 카드 jsonArray 값이 있음 ===" );

                        /* 가장 최근 등록한 카드 보여줌 */

//                        recentUid = uid;
//                        recentBilling_key = billing_key;
//                        recentCard_name = card_name;
                            org.json.JSONObject jsonObject = jsonArray.getJSONObject(0);
                            recentUid = jsonObject.getInt( "uid" );
                            recentBilling_key = jsonObject.getString( "billing_key" );
                            recentCard_name = jsonObject.getString( "card_name" );


                            Log.e(TAG+"postData3", "=== recentUid ===" +recentUid);
                            Log.e(TAG+"postData3", "=== billing_key ===" +billing_key);
                            Log.e(TAG+"postData3", "=== card_name ===" +card_name);

                            paymentTxt.setText(card_name);
                            Log.d(TAG+"postData3", "=== paymentTxt 변경 ===" );




                    }else{
                        Log.e(TAG+"postData3", "=== 아이디 비밀번호 틀린 경우 jsonArray 값이 없음===" );
                        Toast.makeText( getApplicationContext(), "아이디 비밀번호를 확인해주세요.", Toast.LENGTH_SHORT ).show();

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e(TAG+"postData3", "=== response === 에러코드 : " +e);
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG+"postData3", "=== stringRequest === error :" +error);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<String, String>();
                params.put("currentUser", GlobalApplication.currentUser);
                Log.d(TAG+"postData3", "=== currentUser ===" +GlobalApplication.currentUser);
                Log.d(TAG+"postData3", "=== params === : "+ params );

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

    public String timeIntToHourMin2(int plusTimeInt){

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
            plusTimeStr = hour + "시간";
            Log.d(TAG, "=== plusTimeStr ===" +plusTimeStr);
        }else{
            Log.d(TAG, "=== hour 랑 minutes 둘 다 0이 아닌, 경우 ===" );
            plusTimeStr = hour +"시간 "+ minutes + "분";
            Log.d(TAG, "=== plusTimeStr ===" +plusTimeStr);
        }

        return plusTimeStr;
    }

    // 현재 사용자를 url에 넣어서 보내면, 사용자가 등록한 장소 목록들을 받아오는 메서드
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


}