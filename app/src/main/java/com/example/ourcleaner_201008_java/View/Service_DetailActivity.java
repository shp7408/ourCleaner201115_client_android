package com.example.ourcleaner_201008_java.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.ourcleaner_201008_java.GlobalApplication;
import com.example.ourcleaner_201008_java.Interface.AlarmManagerSelectInterface;
import com.example.ourcleaner_201008_java.Interface.ServiceStatesChangeInterface;
import com.example.ourcleaner_201008_java.Interface.TokenSelectInterface;
import com.example.ourcleaner_201008_java.R;
import com.example.ourcleaner_201008_java.Server.ServiceSelectRequest;
import com.example.ourcleaner_201008_java.Service.FcmPushTest;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import kr.co.bootpay.javaApache.BootpayApi;
import kr.co.bootpay.javaApache.model.request.Cancel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/* 서비스 상세정보 보는 엑티비티
* 1. 매칭 대기중 - 결제 완료 - 취소 가능
* 2. 매칭 완료 - 결제 완료 - 취소 가능
* 3. 청소를 위해 매니저 이동중 - 결제 취소 가능 (청소 시작 전 까지)
* 4. 청소 시작 결제 취소 불가
* 5. 청소 완료 - 결제 취소 불가 - 서비스에 대한 후기 작성 가능함. */
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
            serviceplus, serviceCaution, receipt_id;

//    int startTimeInt, needDefTimeInt, allTimeInt;

    TextView datedayTxt, startAndAllTimeTxt, stateTxt, placeNameTxt, addressSizeTxt, editPossTxt, focusTxt, freePlusTxt, garbageTxt,
            plusTxt, cautionTxt, editServiceTxt, receiptTxt, priceDefTxt,priceResultTxt ;

    LinearLayout receiptLayout, priceDefLayout, priceIronLayout, pricefridgeLayout, priceResultLayout;
    TextView priceDefNumTxt, priceIronNumTxt, pricefridgeNumTxt, priceResutNumTxt, serviceDeleteGuideTxt;

//    int resultNeedTimeInt=0, needDefTimeInt; //필요한 전체 시간. 총 시간에서 setText하기 위해 필요한 변수
    int ironPlusTimeInt=0, refridgeTimeInt=0, startTimeInt=0, resultNeedTimeInt=0, defaultTimeInt=0, endTimeInt=0;
    int ironCostInt, fridgeCostInt, resultCost;
    String resultFocusedStr="", resultGarbageStr, resultPlusStr="";

    //숫자 천 자리에 콤마 찍기
    DecimalFormat formatter = new DecimalFormat("###,###");

    Button serviceDeleteBtn;

    RelativeLayout relativeLayout1;
    LinearLayout serviceDeleteLayout;

    String application_id = "5fba1e488f075100207de721"; //rest api applicationid로 변경 -> 결제할때는 안드 어플리케이션 아이디 써야 함
    String private_key = "GjaGT62Fxto9XyMBL835hRqDwz02QxdSmPo7GeAtfek=";

    // TODO: 2020-12-18 매니저 정보를 어떻게 보여줄 지 고민해야 할듯. 프로필 정보와 후기 까지... 
    ImageView profileImage; //수락 이후, 매니저 정보 보여주기
    String  profileImagePathStr="";

    int diffDayInt;
    int deleteCostInt;
    String diffDayDeleteGuideTxt;

    String managerToken;

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
        profileImage = findViewById(R.id.profileImage);

        relativeLayout1 = findViewById(R.id.relativeLayout1);
        serviceDeleteGuideTxt= findViewById(R.id.serviceDeleteGuideTxt);

        editServiceTxt = findViewById(R.id.editServiceTxt);
        receiptTxt = findViewById(R.id.receiptTxt);

        priceDefTxt = findViewById(R.id.priceDefTxt);
        priceResultTxt = findViewById(R.id.priceResultTxt);

        priceResutNumTxt = findViewById(R.id.priceResutNumTxt);

        serviceDeleteLayout = findViewById(R.id.serviceDeleteLayout);

        Log.e(TAG, "=== 기본 이미지로 변경 === ");
        relativeLayout1.bringChildToFront(profileImage);

        Glide.with(Service_DetailActivity.this)
                .load(getDrawable(R.drawable.ic_baseline_person_24))
                .circleCrop()
                .into(profileImage);

        // 10. 결제 예정 내역 보여주기
        // 다른 경우, 이름 바꿔야 함.
        priceDefLayout = findViewById(R.id.priceDefLayout);
        priceIronLayout = findViewById(R.id.priceIronLayout);
        pricefridgeLayout = findViewById(R.id.pricefridgeLayout);
        priceResultLayout = findViewById(R.id.priceResultLayout);

        // 선택한 장소의 평 수에 따라서 책정한 기본 가격 세팅
        priceDefNumTxt = findViewById(R.id.priceDefNumTxt);


        // 선택한 장소의 유료 선택지에 따라서 레이아웃 보이게 함.
        // 기본 가격
        priceIronNumTxt = findViewById(R.id.priceIronNumTxt);
        pricefridgeNumTxt = findViewById(R.id.pricefridgeNumTxt);

        serviceDeleteBtn = findViewById(R.id.serviceDeleteBtn);



        /* 서버에서 서비스 상세 정보 받아오는 코드임 필요한 변수는 전역으로 선언 함.*/
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e(TAG, "=== response ===" +response);
                try {
                        JSONObject jsonObject = new JSONObject(response);

                        if(jsonObject.getString("status").contains("true")){
                            Log.d(TAG, "=== 서버로부터 데이터 잘 받아옴 ===" );

                            JSONArray jsonArray = jsonObject.getJSONArray("data");

                            /* 어차피 게시물 하나만 받아올거니까 index 0 */
                            JSONObject dataObject = (JSONObject) jsonArray.get(0);

                            currentUser = dataObject.getString( "currentUser" );
                            serviceState = dataObject.getString( "serviceState" );

                            myplaceDTO_placeName = dataObject.getString( "myplaceDTO_placeName" );
                            myplaceDTO_address = dataObject.getString( "myplaceDTO_address" );
                            myplaceDTO_detailAddress = dataObject.getString( "myplaceDTO_detailAddress" );
                            myplaceDTO_sizeStr = dataObject.getString( "myplaceDTO_sizeStr" );

                            managerName = dataObject.getString( "managerName" );
                            regularBool = dataObject.getString( "regularBool" );
                            visitDate = dataObject.getString( "visitDate" );
                            visitDay = dataObject.getString( "visitDay" );

                            startTime = dataObject.getString( "startTime" );
                            needDefTime = dataObject.getString( "needDefTime" );
                            needDefCost = dataObject.getString( "needDefCost" );

                            servicefocusedhashMap = dataObject.getString( "servicefocusedhashMap" );
                            laundryBool = dataObject.getString( "laundryBool" );
                            laundryCaution = dataObject.getString( "laundryCaution" );

                            garbagerecycleBool = dataObject.getString( "garbagerecycleBool" );
                            garbagenormalBool = dataObject.getString( "garbagenormalBool" );
                            garbagefoodBool = dataObject.getString( "garbagefoodBool" );

                            garbagehowto = dataObject.getString( "garbagehowto" );
                            serviceplus = dataObject.getString( "serviceplus" );
                            serviceCaution = dataObject.getString( "serviceCaution" );

                            receipt_id = dataObject.getString( "receipt_id" );
                            Log.e(TAG, "=== receipt_id ===" +receipt_id);




                            try{


                                if(managerName.contains(",")){
                                    // 먼저 , 의 인덱스를 찾는다 - 인덱스 값: idx
                                    int idx = managerName.indexOf(",");
                                    String managerName22 = managerName.substring(0, idx);

                                    String string = managerName22.substring(idx+1);
                                    Log.d(TAG, "=== ddddd ==="+string );

                                    string = string.trim();

                                    Log.d(TAG, "=== ddddd ==="+string );

                                    /* 해당 매니저의 토큰 값 받아오기  managerToken 미리 받아옴.*/
                                    postSelectToken(string, 2);
                                }



                            }catch (Exception e){
                                Log.e(TAG, "=== 매니저 미지정 경우, 에러코드임 ===" +e );
                            }









                            if(jsonObject.getString("message").contains("매니저 미지정")){
                                Log.d(TAG, "=== 매니저 미지정 인 경우, ===" );

                                profileImage.setVisibility(View.GONE);

                                Glide.with(Service_DetailActivity.this)
                                        .load(getDrawable(R.drawable.ic_baseline_person_24))
                                        .circleCrop()
                                        .into(profileImage);
                                
                            }else if(jsonObject.getString("message").contains("매니저 지정") && serviceState.equals("매칭 완료")){
                                Log.d(TAG, "=== 매니저 지정 함 ===" );
                                profileImagePathStr = dataObject.getString( "profileImagePathStr" );
                                Log.d(TAG, "=== profileImagePathStr ===" +profileImagePathStr);

                                profileImage.setVisibility(View.VISIBLE);

                                Glide.with(Service_DetailActivity.this)
                                        .load(profileImagePathStr)
                                        .circleCrop()
                                        .into(profileImage);

                            }
                            // TODO: 2020-12-19 서비스 상태 변경 시, 작업하는 부분 

                        }else{

                        }






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

                            startAndAllTimeTxt.setText(timeIntToHourMin(startTimeInt)+"~"+
                                    timeIntToHourMin(endTimeInt)+"("+timeIntToHourMin2(endTimeInt-startTimeInt)+")");

                        }catch (Exception e){
                            Log.e(TAG+123, "=== startAndAllTimeTxt ===" +e);
                        }

                        // 3. 서비스 상태 보여주기
                        try{
                            if(serviceState.contains("매칭 대기 중")){

                                stateTxt.setText("매칭 대기 중입니다.");
                                serviceDeleteBtn.setText("예약 취소하기");
                                Log.d(TAG, "=== 결제 후, 영수증 보여주기 ===" );




                            }else if(serviceState.contains("예약 취소")){



                                stateTxt.setText("취소된 예약입니다.");
                                serviceDeleteBtn.setVisibility(View.GONE);
                                Log.d(TAG, "=== 취소 후, 취소 영수증 보여주기 ===" );

                                int greyColcor = ContextCompat.getColor(getApplicationContext(), R.color.grey);
                                stateTxt.setTextColor(greyColcor);

                                profileImage.setVisibility(View.GONE);

                                editPossTxt.setVisibility(View.GONE);
                                editServiceTxt.setVisibility(View.GONE);

                                receiptTxt.setText("결제 취소");

                                priceIronLayout.setVisibility(View.GONE);
                                pricefridgeLayout.setVisibility(View.GONE);
//                                serviceDeleteLayout.setVisibility(View.GONE);

                                // TODO: 2020-12-19 결제 취소한 영수증 저장한거 보여주기



                            }else if(serviceState.contains("매칭 완료")){

                                int whiteColcor = ContextCompat.getColor(getApplicationContext(), R.color.white);
                                stateTxt.setTextColor(whiteColcor);

                                stateTxt.setText(managerName.substring(0, 3) + " 매니저 님");
                                stateTxt.setBackgroundResource(R.drawable.bordercircle602);

                                stateTxt.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Log.d(TAG, "=== stateTxt 클릭 ===" );
                                    }
                                });

                                serviceDeleteBtn.setText("예약 취소하기");
                                Log.d(TAG, "=== 결제 후, 영수증 보여주기 + 매니저에 대한 정보 보여줘야 함. ===" );



                                // TODO: 2020-12-18 청소 중, 청소 후 상태 변경 해야 함.. 
                            }else if(serviceState.contains("청소 중")){

                                stateTxt.setText("청소 중입니다.");
                                serviceDeleteBtn.setVisibility(View.GONE);


                                
                            }else if(serviceState.contains("청소 완료")){

                                stateTxt.setText("완료된 청소입니다.");
                                serviceDeleteBtn.setVisibility(View.GONE);

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

                        priceDefNumTxt.setText(formatter.format(Integer.parseInt(needDefCost))+" 원");

                        if(ironCostInt==6600){
                            priceIronNumTxt.setText(formatter.format(ironCostInt)+" 원");
                            priceIronLayout.setVisibility(View.VISIBLE);
                        }
                        if(fridgeCostInt==26400){
                            pricefridgeNumTxt.setText(formatter.format(fridgeCostInt)+" 원");
                            pricefridgeLayout.setVisibility(View.VISIBLE);
                        }

                        priceResutNumTxt.setText(formatter.format(Integer.parseInt(needDefCost)+ironCostInt+fridgeCostInt)+" 원");

                    /* 결제 취소 시, 필요한 코드임. 오늘부터 예약 당일까지 얼마 남았는지 구하는 코드 */
                    Log.e(TAG, "=== visitDate ==="+visitDate );

                    diffDayInt= (int) calDateBetweenNowandB(visitDate);
                    if(diffDayInt == 0){

                        //어차피, 청소 시작 시, 예약 취소 버튼 없앨 것 임...
                        Log.e(TAG, "=== 결제 취소를 위함 visitDate ==0 ===" +visitDate );
                        diffDayDeleteGuideTxt="당일 예약 취소 시, 30%의 수수료가 있습니다.\n그래도 취소하시겠습니까?";
                        deleteCostInt= (int) ((Integer.parseInt(needDefCost)+ironCostInt+fridgeCostInt) * 0.7);

                    }else if(diffDayInt == 1){
                        Log.e(TAG, "=== 결제 취소를 위함 visitDate ==1 ===" +visitDate );
                        diffDayDeleteGuideTxt="예약 취소 하시겠습니까?";

                        deleteCostInt= Integer.parseInt(needDefCost)+ironCostInt+fridgeCostInt;

                        if(nowTimeToInt() > 18){
                            Log.d(TAG, "=== 예약 전날 6시 넘은 경우 ===" );
                            diffDayDeleteGuideTxt="전날 예약 취소 시, 30%의 수수료가 있습니다.\n그래도 취소하시겠습니까?";

                            deleteCostInt= (int) ((Integer.parseInt(needDefCost)+ironCostInt+fridgeCostInt) * 0.7);                        }

                    }else if(diffDayInt > 1){
                        Log.e(TAG, "=== 결제 취소를 위함 visitDate >1 ===" +visitDate );
                        diffDayDeleteGuideTxt="예약 취소 하시겠습니까?";

                        deleteCostInt= (int) (Integer.parseInt(needDefCost)+ironCostInt+fridgeCostInt);
                    }

                    Log.e(TAG, "=== 결제 취소를 위함 deleteCostInt ===" +deleteCostInt );
                    Log.e(TAG, "=== 결제 취소를 위함 diffDayDeleteGuideTxt ===" +diffDayDeleteGuideTxt );

                    if(serviceState.contains("예약 취소")){

                        priceIronLayout.setVisibility(View.GONE);
                        pricefridgeLayout.setVisibility(View.GONE);
                        serviceDeleteLayout.setVisibility(View.GONE);

                        priceDefTxt.setText("결제 금액");
                        priceDefNumTxt.setText(formatter.format(Integer.parseInt(needDefCost)
                                +ironCostInt+fridgeCostInt)+" 원");

                        priceResultTxt.setText("환불 금액"); //deleteCostInt
                        priceResutNumTxt.setText(formatter.format(deleteCostInt)+" 원");

                        // TODO: 2020-12-19 결제 취소한 영수증 저장한거 보여주기



                    }







                    /* 서비스 예약 취소하는 코드임 */
                    serviceDeleteBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Log.e(TAG, "=== serviceDeleteBtn 클릭. 예약 취소 버튼 누름 ===" );

                            AlertDialog.Builder builder = new AlertDialog.Builder(Service_DetailActivity.this);
                            builder.setMessage(diffDayDeleteGuideTxt);
                            builder.setPositiveButton("확인",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            Log.d(TAG, "=== 확인 클릭  ===" );

                                            postUpdateServiceState(serviceWaitingUidInt, "예약 취소");




                                        }
                                    });
                            builder.setNegativeButton("취소",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            Log.d(TAG, "=== 취소 클릭 ===" );
                                        }
                                    });
                            builder.show();


                        }
                    });

                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e(TAG, "=== response 받아올 때, === 에러코드 : " +e);
                }
            }
        };
        ServiceSelectRequest serviceSelectRequest = new ServiceSelectRequest(String.valueOf(serviceWaitingUidInt), "서비스본인열람" ,"고객",responseListener);
        RequestQueue queue = Volley.newRequestQueue( Service_DetailActivity.this );
        queue.add( serviceSelectRequest );


        /* 서비스 예약 취소 가이드 안내 다이얼로그 생성 */
        serviceDeleteGuideTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "=== serviceDeleteGuideTxt 결제 취소 가이드 클릭 시, 생성하는 다이얼로그 ===" );

                AlertDialog.Builder builder = new AlertDialog.Builder(Service_DetailActivity.this);
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
    }


    /* 레트로핏으로 서비스 번호, 서비스 상태 보내면 예약 취소 하는 코드 */
    private void postUpdateServiceState(int serviceId, String serviceState22){
        Log.e(TAG, "=== postUpdateServiceState 시작 ===" );

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(AlarmManagerSelectInterface.JSONURL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();

        ServiceStatesChangeInterface api =retrofit.create(ServiceStatesChangeInterface.class);
        /* 인터페이스에서 정의한 메서드 / 인자로 보낼 값 넣는 곳 */
        Call<String> call = api.updateServiceState(serviceId, serviceState22);

        Log.e(TAG, "=== 서비스 id serviceId ===" + serviceId);
        Log.e(TAG, "=== 서비스 id serviceState ===" + serviceState22);

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                Log.e("Responsestring", response.body());
                //Toast.makeText()
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        Log.e(TAG, "onSuccess" + response.body());
                        String jsonresponse = response.body();
                        //홈페이지에서 더 많은 컨텐츠를 확인하세요,  https://stickode.com/
                        Log.e(TAG, "=== 예약 취소 db 변경 완료 ===" +jsonresponse);
                        /*
                        *  1. 화면에서 서비스 상태 변경됨 보여주기
                        *  2. 오늘 날짜 확인해서 1일 이상 차이 나는지, 1일 차이 나는지, 0일 차이 나는지 확인해서,
                        * 그에 따른 가격으로 환불하기  */

                        int whiteColcor = ContextCompat.getColor(getApplicationContext(), R.color.white);
                        stateTxt.setText("취소된 예약입니다.");
                        stateTxt.setBackgroundColor(whiteColcor);

                        serviceDeleteBtn.setVisibility(View.GONE);
                        Log.d(TAG, "=== 취소 후, 취소 영수증 보여주기 ===" );

                        int greyColcor = ContextCompat.getColor(getApplicationContext(), R.color.grey);
                        stateTxt.setTextColor(greyColcor);

                        profileImage.setVisibility(View.GONE);

                        editPossTxt.setVisibility(View.GONE);
                        editServiceTxt.setVisibility(View.GONE);

                        receiptTxt.setText("결제 취소");

                        priceIronLayout.setVisibility(View.GONE);
                        pricefridgeLayout.setVisibility(View.GONE);
//                        serviceDeleteLayout.setVisibility(View.GONE);

                        priceDefTxt.setText("결제 금액");
                        priceDefNumTxt.setText(formatter.format(Integer.parseInt(needDefCost)
                                +ironCostInt+fridgeCostInt)+" 원");

                        priceResultTxt.setText("환불 금액"); //deleteCostInt
                        priceResutNumTxt.setText(formatter.format(deleteCostInt)+" 원");




                        Thread thread = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try  {


                                    /* 2. 결제 취소 부분 */
                                    BootpayApi api = new BootpayApi(application_id, private_key);

                                    try {
                                        api.getAccessToken();
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                        Log.d(TAG, "=== cancelTxt 취소 취소  ==="+e );
                                    }

                                    Cancel cancel = new Cancel();
                                    cancel.receipt_id = receipt_id; //영수증 번호 입력하는 곳!!!
                                    cancel.name = "본인";
                                    cancel.reason = "단순 변심으로 인한 결제 취소";
                                    cancel.price= Double.valueOf(deleteCostInt);

                                    Log.e(TAG, "=== cancel.price ===" +cancel.price);

                                    try {
                                        HttpResponse res = api.cancel(cancel);
                                        String str = IOUtils.toString(res.getEntity().getContent(), "UTF-8");

                                        /* 취소 완료 후 부분 영수증 */
                                        System.out.println(str);
                                        Log.d(TAG, "=== cancelTxt 취소 취소 str === " +str );



                                    } catch (Exception e) {
                                        e.printStackTrace();

                                        Log.e(TAG, "BootpayApi cancel 에러 코드 : "+ e);
                                    }

                                } catch (Exception e) {
                                    e.printStackTrace();
                                    Log.e(TAG, "BootpayApi 에러 코드 : "+ e);
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


//                        /* 메인에서 사용하는 서비스 상태임 매니저 지정된 경우에 fcm 보내기 위한 코드 */
//                        if(serviceState.contains("매칭 완료")){
//
//
//
//
//
//
//
//
//
//                            Thread thread2 = new Thread(new Runnable() {
//                                @Override
//                                public void run() {
//
//                                    try{
//                                        FcmPushTest fcmPushTest = new FcmPushTest();
//                                        try {
//                                            fcmPushTest.pushFCMNotification(datedayTxt.getText().toString()+"의 예약이 고객에 의해 취소됐습니다. 확인해주세요.",
//                                                    2,
//                                                    "c2eb041dc6852ec1af64e429d1133763c868478ff5547a9fb2d3b1b0026e29bc");
//                                        } catch (Exception e) {
//                                            e.printStackTrace();
//                                            Log.d(TAG, "=== pushFCMNotification 에러코드 매니저 미지저 경우. 그냥 넘어가도 딤 ==="+e );
//                                        }
//                                    }catch (Exception e){
//                                        Log.d(TAG, "=== pushFCMNotification 에러코드 매니저 미지저 경우. 그냥 넘어가도 딤 ==="+e );
//
//                                    }
//
//
//
//                                }
//                            });
//
//                            thread2.start();
//                            Handler mHandler2 = new Handler(Looper.getMainLooper());
//                            mHandler2.postDelayed(new Runnable() {
//                                @Override
//                                public void run() {
//                                    Log.e(TAG, "pushFCMNotification : run");
//
//                                }
//
//                            }, 1000);
//
//
//
//
//
//
//
//
//
//
//
//
//                        }





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
                                Toast.makeText(Service_DetailActivity.this, obj.optString("message")+"", Toast.LENGTH_SHORT).show();
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


    /* 현재 시간 int로 구하는 메서드 */
    public int nowTimeToInt(){
        Date date_now = new Date(System.currentTimeMillis()); // 현재시간을 가져와 Date형으로 저장한다

        int nowTimeInt;

        // 년월일시분초 14자리 포멧
        SimpleDateFormat fourteen_format = new SimpleDateFormat("HH");
        System.out.println(fourteen_format.format(date_now));

        nowTimeInt = Integer.parseInt(fourteen_format.format(date_now));

        return nowTimeInt;
    }

    /* 오늘부터 12.30일 형태의 날짜 사이 차이를 구하는 메서드. // 에러 시, 0을 return */
    public long calDateBetweenNowandB(String b)
    { //.substring(0,5) 사용하면 됨!!
        // TODO: 2020-12-18 시연할 때, 날짜 2021년 넘어가면 바꿔야 함...ㅎㅎ
        //입력할 때, 12.15 이런 식임

        String nowYear2 = "2020";
        String month2 = b.substring(0,2); //12월
        String date2 = b.substring(3); // 20일
        Log.e(TAG, "=== month2 ===" +month2 );
        Log.e(TAG, "=== date2 ===" +date2 );

        try{

            String dateAll2 = nowYear2+"-"+month2+"-"+date2;
            Log.e(TAG, "=== calDateBetweenAandB === dateAll2 " +dateAll2);

            String format = "yyyy-MM-dd";
            SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.ENGLISH);

            Date nowDate = new Date();
            Date secondDate = sdf.parse(dateAll2);

            long diffInMillies = Math.abs(secondDate.getTime() - nowDate.getTime());
            long diff = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
//            System.out.println(String.format("A %s , B %s Diff %s Days", a, b, diff));
            Log.e(TAG, "=== calDateBetweenAandB ===" +String.format("A %s , B %s Diff %s Days", nowDate.getTime(), b, diff));
            Log.e(TAG, "=== calDateBetweenAandB diff ===" + "D - "+diff);

            if(diff>0){
                diff= diff+1;
            }

            /* 1일 추가함 */
            return diff;

        }catch (Exception e){
            Log.d(TAG, "=== calDateBetweenAandB 에러코드 ===" +e  );
        }
        return 0;
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


    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Log.d(TAG, "=== onBackPressed ===" );

        //있으면 넘어감
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
        finish();

    }
}