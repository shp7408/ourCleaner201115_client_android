package com.example.ourcleaner_201008_java.View.Manager;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.viewpager.widget.ViewPager;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.ourcleaner_201008_java.Adapter.Manager_Service_DetailAdapter;
import com.example.ourcleaner_201008_java.GlobalApplication;
import com.example.ourcleaner_201008_java.Interface.AlarmManagerSelectInterface;
import com.example.ourcleaner_201008_java.Interface.ServiceStatesChangeInterface;
import com.example.ourcleaner_201008_java.Interface.TokenSelectInterface;
import com.example.ourcleaner_201008_java.R;
import com.example.ourcleaner_201008_java.Server.ServiceSelectRequest;
import com.example.ourcleaner_201008_java.Service.FcmPushTest;
import com.example.ourcleaner_201008_java.View.Fragment.Fragment_Manager_Detail_Service1;
import com.example.ourcleaner_201008_java.View.Fragment.Fragment_Manager_Detail_Service2;
import com.example.ourcleaner_201008_java.View.Fragment.Fragment_Manager_Detail_Service3;
import com.google.android.material.tabs.TabLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;


public class Manager_Detail_MatchingPostActivity extends AppCompatActivity {

    /* 메인에서 받아온 uid로 세부 정보 불러오기 */
    private static final String TAG = "매니저용메인상세보기";

    int serviceWaitingUidInt, countVisitInt;

    //토큰값받아오는 변수. 받는 사람의 이메일 가져옴
    String clientToken;


    /* 서버에서 서비스 상세 정보 받아올 때, 필요한 변수임.*/
    String currentUser, serviceState, myplaceDTO_placeName, myplaceDTO_address, myplaceDTO_detailAddress,
            myplaceDTO_sizeStr, managerName,regularBool, visitDate, visitDay, startTime,
            needDefTime, needDefCost, servicefocusedhashMap, laundryBool, laundryCaution,
            garbagerecycleBool, garbagenormalBool, garbagefoodBool, garbagehowto,
            serviceplus, serviceCaution, cardBilling_key;

    String nickname, phoneNum;

    private ViewPager mViewPager;
    Manager_Service_DetailAdapter adapter = new Manager_Service_DetailAdapter(getSupportFragmentManager());


    TextView customerPlaceNameTxt, firstVisitTxt, secondVisitTxt, dateDayTimeTxt,
            serviceStateTxt, serviceState2Txt,serviceState3Txt, serviceState4Txt, serviceStateChangeTxt
            , addressTxt, addressDetailTxt, contactTxt;

    String dialogStr, fcmMessageStr; //다이얼로그에서 질문할 내용

    String diffDayDeleteGuideTxt;

//    int startTimeInt, needDefTimeInt, allTimeInt;

//    TextView datedayTxt, startAndAllTimeTxt, stateTxt, placeNameTxt,
//            addressSizeTxt, editPossTxt, focusTxt, freePlusTxt, garbageTxt,
//            plusTxt, cautionTxt;
//
//    LinearLayout receiptLayout, priceDefLayout, priceIronLayout, pricefridgeLayout, priceResultLayout;
//    TextView priceDefNumTxt, priceIronNumTxt, pricefridgeNumTxt, priceResutNumTxt;
//
//    int resultNeedTimeInt=0, needDefTimeInt; //필요한 전체 시간. 총 시간에서 setText하기 위해 필요한 변수
    int ironPlusTimeInt=0, refridgeTimeInt=0, startTimeInt=0, resultNeedTimeInt=0, defaultTimeInt=0, endTimeInt=0;
//    int ironCostInt, fridgeCostInt, resultCost;
//    String resultFocusedStr="", resultGarbageStr, resultPlusStr;
//
//    //숫자 천 자리에 콤마 찍기
//    DecimalFormat formatter = new DecimalFormat("###,###");
//
//    Button Btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager__detail_matching_post);
        Log.d(TAG, "=== onCreate ===" );

        //intent로 받을 때 사용하는 코드
        Intent intent = getIntent();
        Log.d(TAG, "매니저용 메인에서 인텐트 받음 intent :"+intent);

        serviceWaitingUidInt = intent.getIntExtra("uid", 0);
        Log.d(TAG, "매니저용 메인에서 인텐트 받음 intent : "+ serviceWaitingUidInt);

        /* 프래그먼트 관련 */

        mViewPager = findViewById(R.id.container);

        customerPlaceNameTxt = findViewById(R.id.customerPlaceNameTxt);

        firstVisitTxt = findViewById(R.id.firstVisitTxt);
        secondVisitTxt = findViewById(R.id.secondVisitTxt);
        firstVisitTxt.setVisibility(View.GONE);
        secondVisitTxt.setVisibility(View.GONE);

        dateDayTimeTxt = findViewById(R.id.dateDayTimeTxt);

        serviceStateTxt = findViewById(R.id.serviceStateTxt);
        serviceState3Txt = findViewById(R.id.serviceState3Txt);
        serviceState4Txt = findViewById(R.id.serviceState4Txt);

        serviceStateChangeTxt = findViewById(R.id.serviceStateChangeTxt);

        addressTxt = findViewById(R.id.addressTxt);
        addressDetailTxt = findViewById(R.id.addressDetailTxt);

        contactTxt = findViewById(R.id.contactTxt);

        /* 전화 걸기 권한 문제 */
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.M) { // 안드로이드 M 이상일 경우
            if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                //전화 권한 있을 경우 getContext().startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(strArg1)));
            } else { //전화 권한 없을 경우
            }
        }else{
            // 안드로이드 M 이하일 경우
        }



        /* 서버에서 서비스 상세 정보 받아오는 코드임 필요한 변수는 전역으로 선언 함.*/
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e(TAG, "=== response ===" +response);
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    if(jsonObject.getString("status").contains("true")){
                        Log.d(TAG, "=== 서버로부터 데이터 잘 받아옴 count ===" +jsonObject.getString("count"));
                        //jsonObject.getString("count") -> 매니저 / 고객 동일한거 + serviceState = "청소 완료" 즉, 방문한 적이 있음
                        // 0 이면, 첫 방문
                        // 0 아니면, 재 방문

                        JSONArray jsonArray = jsonObject.getJSONArray("data");

                        /* 어차피 게시물 하나만 받아올거니까 index 0 */
                        JSONObject dataObject = (JSONObject) jsonArray.get(0);

                        countVisitInt = jsonObject.getInt("count");

                        Log.e(TAG, "=== countVisitInt ===" +countVisitInt);

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


                        /* 해당 일반고객의 토큰 값 받아오기  managerToken 미리 받아옴.*/
                        postSelectToken(currentUser, 1);



                        /* 서비스 예약자 추가 정보 */
                        nickname = dataObject.getString( "nickname" );
                        phoneNum = dataObject.getString( "phoneNum" );

                        /* 서비스 장소 추가 정보  => 필요하면. 하자.. 일단 다른거 부터.. ㅎㅎ */
                        Log.d(TAG, "=== nickname ===" +nickname);
                        Log.d(TAG, "=== phoneNum ===" +phoneNum);


                        /* 0보다 크면, 재방문
                        * 그렇지 않으면, 첫방문 */
                        if(countVisitInt>0){
                            secondVisitTxt.setVisibility(View.VISIBLE);
                        }else{
                            firstVisitTxt.setVisibility(View.VISIBLE);
                        }



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

                            //startAndAllTimeTxt.setText();

                        }catch (Exception e){
                            Log.e(TAG+123, "=== startAndAllTimeTxt ===" +e);
                        }
//
                        dateDayTimeTxt.setText(visitDate+"("+visitDay+")  "+timeIntToHourMin(startTimeInt)+"~"+timeIntToHourMin(endTimeInt)+" ("+timeIntToHourMin2(endTimeInt-startTimeInt)+")");



                        /* 서비스 요청한 사람의 집 + 이름  */
                        customerPlaceNameTxt.setText(myplaceDTO_address.substring(11,15)+" "+nickname);

                        // 3. 서비스 상태 보여주기
                        try{
                            if(serviceState.contains("매칭 완료")){

                                serviceStateTxt.setText("청소 전");
                                serviceStateChangeTxt.setText("이동 하기");
                                addressDetailTxt.setVisibility(View.GONE);

                            }else if(serviceState.contains("이동 중")){
                                serviceStateTxt.setText("이동 중");
                                serviceStateChangeTxt.setText("청소 시작");
                                addressDetailTxt.setVisibility(View.VISIBLE);

                                //serviceState2Txt.setText("* 이동 중입니다.");
                                serviceState3Txt.setText("도착 시, 아래의 '청소 시작' 버튼을 눌러,");
                                serviceState4Txt.setText("고객에게 청소 시작을 알려주세요.");

                            }else if(serviceState.contains("청소 중")){
                                serviceStateTxt.setText("청소 중");
                                serviceStateChangeTxt.setText("청소 완료");
                                addressDetailTxt.setVisibility(View.VISIBLE);

                                //serviceState2Txt.setText("* 청소 중입니다.");
                                serviceState3Txt.setText("청소 완료 시, 아래의 '청소 완료' 버튼을 눌러,");
                                serviceState4Txt.setText("고객에게 청소 완료를 알려주세요.");


                            }else if(serviceState.contains("청소 완료")){
                                serviceStateTxt.setText("청소 완료");
                                //serviceStateChangeTxt.setText("리뷰 확인");
                                serviceStateChangeTxt.setVisibility(View.GONE);
                                addressDetailTxt.setVisibility(View.GONE);

                                //serviceState2Txt.setText("* 청소가 완료되었습니다.");
                                serviceState3Txt.setVisibility(View.GONE);
                                serviceState4Txt.setVisibility(View.GONE);
                            }
                        }//스틱코드에 접속하여 생산성을 향상시켜 보세요, https://stickode.com/
                        catch (Exception e){
                            Log.e(TAG, "=== serviceState 널인 경우, 에러코드 ===" + e);
                        }

                        // 4. 집 상세정보 보여주기
                        // 내가 보는 경우에는, 장소 이름, 자세히 보기 가능. 그리고 변경하기 텍스트뷰도 보임. 물론, 서비스 이후에는 텍스트 뷰 보이면 안 됨.


                        try{
                            Log.d(TAG, "===  myplaceDTO_address myplaceDTO_sizeStr ===" +myplaceDTO_address+"("+myplaceDTO_sizeStr+")");

                            addressTxt.setText(myplaceDTO_address.substring(8));
                            addressDetailTxt.setText(myplaceDTO_detailAddress);

                        }catch (Exception e){
                            Log.e(TAG, "=== myplaceDTO_address myplaceDTO_sizeStr 널인 경우 에러코드 ===" +e);
                        }




//
//                        // 5. 집중 청소 구역 보여주기

//                        String resultFocusedStr=null;
//
//                        if(servicefocusedhashMap!= null){
//                            if(servicefocusedhashMap.contains("livingRoomBtn=true")){
//                                resultFocusedStr = "거실";
//                            }
//
//                            if(servicefocusedhashMap.contains("roomBtn=true")){
//                                if(resultFocusedStr.isEmpty()){
//                                    resultFocusedStr="방";
//                                }else{
//                                    resultFocusedStr= resultFocusedStr+" 방";
//                                }
//
//                            }
//
//                            if(servicefocusedhashMap.contains("bathRoomBtn=true")){
//                                if(resultFocusedStr.isEmpty()){
//                                    resultFocusedStr="화장실";
//                                }else{
//                                    resultFocusedStr= resultFocusedStr+" 화장실";
//                                }
//
//                            }
//
//                            if(servicefocusedhashMap.contains("kitchenBtn=true")){
//                                if(resultFocusedStr.isEmpty()){
//                                    resultFocusedStr="부엌";
//                                }else{
//                                    resultFocusedStr= resultFocusedStr+" 부엌";
//                                }
//                            }
//                            resultFocusedStr = resultFocusedStr+" 집중적으로 청소해주세요.";
//                        }



                        // 6. 무료 추가 선택 보여주기
                        if(laundryBool!= null){
                            if(laundryBool.contains("true")){
                                laundryBool="세탁 및 건조된 빨래 개기";

                            }else{
                                laundryBool="무료 추가 없음";
                            }
                        }else{
                            laundryBool="무료 추가 없음";
                        }

                        String resultGarbageStr = null;



                        /* 탭1 : 고객 요구 */
                        Fragment_Manager_Detail_Service1 fragment_manager_detail_service1 = new Fragment_Manager_Detail_Service1();

                        Bundle bundle = new Bundle();



                        bundle.putString("servicefocusedhashMap", servicefocusedhashMap);

                        bundle.putString("laundryBool", laundryBool);
                        bundle.putString("laundryCaution", laundryCaution);

                        bundle.putString("garbagerecycleBool", garbagerecycleBool);
                        bundle.putString("garbagenormalBool", garbagenormalBool);
                        bundle.putString("garbagefoodBool", garbagefoodBool);

                        bundle.putString("resultGarbageStr", resultGarbageStr);
                        bundle.putString("garbagehowto", garbagehowto);
                        bundle.putString("serviceplus", serviceplus);
                        bundle.putString("serviceCaution", serviceCaution);

                        fragment_manager_detail_service1.setArguments(bundle);

                        adapter.addFragment(fragment_manager_detail_service1, "고객 요구 업무");







                        /* 탭2 : 기본 업무 */
                        Fragment_Manager_Detail_Service2 fragment_manager_detail_service2 = new Fragment_Manager_Detail_Service2();

                        Bundle bundle2 = new Bundle();
                        bundle2.putInt("getNeedDefCost", 1);

                        fragment_manager_detail_service2.setArguments(bundle2);

                        adapter.addFragment(fragment_manager_detail_service2, "기본 업무");






                        /* 탭3 : 정산 */
                        Fragment_Manager_Detail_Service3 fragment_manager_detail_service3 = new Fragment_Manager_Detail_Service3();

                        Bundle bundle3 = new Bundle();
                        bundle3.putInt("getNeedDefCost", 1);

                        fragment_manager_detail_service3.setArguments(bundle3);

                        adapter.addFragment(fragment_manager_detail_service3, "정산");
                        mViewPager.setAdapter(adapter);


                        TabLayout tabLayout = findViewById(R.id.tabs);
                        tabLayout.setupWithViewPager(mViewPager);





                    }else{

                    }






                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e(TAG, "=== response === 에러코드 : " +e);
                }
            }
        };
        ServiceSelectRequest serviceSelectRequest = new ServiceSelectRequest(String.valueOf(serviceWaitingUidInt), "매칭 대기 중" ,"매니저",responseListener);
        RequestQueue queue = Volley.newRequestQueue( Manager_Detail_MatchingPostActivity.this );
        queue.add( serviceSelectRequest );


        serviceStateChangeTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "=== serviceStateChangeTxt ===" );
                
                
                if(serviceStateTxt.getText().toString().equals("청소 완료")){
                    Log.d(TAG, "=== 다이얼로그 생성하지 않고, " +
                            "1. 현재 서비스 앞으로 작성한 리뷰 있는지 서버에 확인 " +
                            "2. 리뷰 상세보기 화면으로 이동.===" );

                    // TODO: 2021-01-22 리뷰 기능 구현해야함..
                    
                    
                }else{


                    
                    if(serviceStateTxt.getText().toString().equals("청소 전")){

                        Log.d(TAG, "=== 청소 전 상태에서 이동 시작 클릭 ===" );
                        dialogStr = "이동하시겠습니까?";
                        fcmMessageStr= "매니저님이 이동을 시작합니다.";

                    }else if(serviceStateTxt.getText().toString().equals("이동 중")){

                        Log.d(TAG, "=== 이동 중 상태에서 청소 시작 클릭 ===" );
                        dialogStr = "청소를 시작하시겠습니까?";
                        fcmMessageStr= "매니저님이 청소를 시작합니다.";

                    }else if(serviceStateTxt.getText().toString().equals("청소 중")){

                        Log.d(TAG, "=== 청소 중 상태에서 청소 완료 클릭 ===" );
                        dialogStr = "청소를 완료하셨습니까?";
                        fcmMessageStr= "매니저님이 청소를 완료했습니다. 리뷰를 작성해주세요.";

                    }


                    AlertDialog.Builder builder = new AlertDialog.Builder(Manager_Detail_MatchingPostActivity.this);
                    builder.setMessage(dialogStr);
                    builder.setPositiveButton("확인",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    Log.d(TAG, "=== 확인 클릭  ===" );

                                    if(serviceStateTxt.getText().toString().equals("청소 전")){
                                        Log.d(TAG, "=== 청소 전 상태에서 이동 시작 클릭 ===" );
                                        postUpdateServiceState(serviceWaitingUidInt, "이동 중");

                                    }else if(serviceStateTxt.getText().toString().equals("이동 중")){

                                        Log.d(TAG, "=== 이동 중 상태에서 청소 시작 클릭 ===" );
                                        postUpdateServiceState(serviceWaitingUidInt, "청소 중");

                                    }else if(serviceStateTxt.getText().toString().equals("청소 중")){

                                        Log.d(TAG, "=== 청소 중 상태에서 청소 완료 클릭 ===" );
                                        postUpdateServiceState(serviceWaitingUidInt, "청소 완료");

                                    }else if(serviceStateTxt.getText().toString().equals("청소 완료")){
                                        Log.d(TAG, "=== 청소 완료 상태 ===" );
                                    }



                                    FcmPushTest fcmPushTest = new FcmPushTest();

                                    /* 1. 푸쉬 알람 스레드 */
                                    Thread thread = new Thread(new Runnable() {
                                        @Override
                                        public void run() {
                                            try  {
                                                Log.e(TAG, "=== fcmPushTest ==="+fcmPushTest.toString() );

                                                fcmPushTest.pushFCMNotification(fcmMessageStr
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
                
                

                
                












            }
        });
















//        Btn = (Button) findViewById(R.id.Btn);
//        Btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Log.d(TAG+"123", "=== Btn 클릭 : === ");
//
//                if(Btn.getText().toString().equals("서비스 수락하기")&&serviceState.contains("매칭 대기 중")){
//                    Log.d(TAG+"123", "=== 매칭 대기 중 상태에서 서비스 수락하기 ===" );
//
//
//                    AlertDialog.Builder builder = new AlertDialog.Builder(Manager_Detail_MatchingPostActivity.this);
//                    builder.setMessage("해당 서비스를 수락하시겠습니까?");
//                    builder.setPositiveButton("확인",
//                            new DialogInterface.OnClickListener() {
//                                public void onClick(DialogInterface dialog, int which) {
//                                    Log.d(TAG+"123", "=== 확인 클릭  ===" );
//
//// TODO: 2020-11-28 서비스 수락 시, 자동으로 결제되어야 함.
//                                    //UserManagement API 요청을 담당
//                                    UserManagement.getInstance()
//                                            //requestLogout : 로그아웃 요청
//                                            //파라미터 : logout 요청 결과에 대한 callback
//                                            .requestLogout(new LogoutResponseCallback() {
//                                                @Override
//                                                public void onCompleteLogout() {
//                                                    Log.e(TAG+"ddd", "=== onCompleteLogout : 해당 서비스를 수락 버튼 클릭함. 그다음은 요청내용 받아와야 함. ===");
//
//                                                    // TODO: 2020-12-17 자동 결제 구현해야 함...
//                                                    /* 해당 서버 요청-응답에서 이루어져야 하는 일
//                                                    1. rest api로 엑세스 토큰 받아오기
//                                                    2. 해당 서비스 db의 빌링키로 자동 결제되어야 함.
//                                                    3. 해당 서비스 db에서 서비스 상태 변경
//                                                    4.  */
//
////                                                    Log.e(TAG+"ddd", "=== BootpayApi api ===" );
////                                                    BootpayApi api = new BootpayApi(
////                                                            "5fba1e488f075100207de721",
////                                                            "6hZhD2SuxKpuNWoIfYg3uCk+jyNQ4avv1GAspvVTK2I="
////                                                    );
////                                                    try {
////                                                        api.getAccessToken();
////                                                        Log.e(TAG+"ddd", "=== api.getAccessToken(); ===");
////                                                    } catch (Exception e) {
////                                                        e.printStackTrace();
////                                                        Log.e(TAG+"ddd", "=== 에러코드 ===" + e );
////                                                    }
//
//                                                }
//                                            });
//                                }
//                            });
//                    builder.setNegativeButton("취소",
//                            new DialogInterface.OnClickListener() {
//                                public void onClick(DialogInterface dialog, int which) {
//                                    Log.d(TAG+"123", "=== 취소 클릭 ===" );
//                                }
//                            });
//                    builder.show();
//
//
//                }else if(Btn.getText().toString().equals("예약 취소하기")&&serviceState.contains("매칭 완료")){
//                    Log.d(TAG+"123", "=== 매칭 완료 상태에서 예약 취소하기 ===" );
//
//
//                }
//
//            }
//        });


        contactTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "=== contactTxt 클릭 === 1. 다이얼로그 생성하기" );

                CharSequence info[] = new CharSequence[] {"전화 하기", "채팅 하기" };
                AlertDialog.Builder builder = new AlertDialog.Builder(Manager_Detail_MatchingPostActivity.this);

                builder.setItems(info, new DialogInterface.OnClickListener() {

                    @Override

                    public void onClick(DialogInterface dialog, int which) {
                        switch(which)
                        {
                            case 0:

//                                //전화 어플에 있는 Activity 정보를 넣어 Intent 정의
//                                Intent callIntent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:01030517408"));
//                                startActivity(callIntent);

                                break;

                            case 1:
                                // 채팅 하기
                                //Toast.makeText(Manager_Detail_MatchingPostActivity.this, "채팅 하기", Toast.LENGTH_SHORT).show();
                                Log.d(TAG, "=== 채팅 하기 클릭 ===" );

                                //있으면 넘어감
                                Intent intent = new Intent(Manager_Detail_MatchingPostActivity.this, Manager_Chat_Room_Activity.class);

                                intent.putExtra("whomSent1", currentUser); //고객의 이메일
                                intent.putExtra("whomSent2", nickname); //고객의 지역/ 이름 화성시 박용현
                                intent.putExtra("whoSend", GlobalApplication.currentManager); //나. 매니저의 이메일

                                startActivity(intent);

                                //finish();

                                break;
                        }
                        dialog.dismiss();
                    }

                });

                builder.show();

            }
        });


    }


    /* 레트로핏으로 서버에 이메일 보내서 토큰 받아오는 코드 - 서버에서 이메일에 해당하는 토큰값 가져오는 코드*/
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

                                    Log.e(TAG, "=== postSelectToken === clientToken" +clientToken);



                                }
                            }else {
                                Toast.makeText(Manager_Detail_MatchingPostActivity.this, obj.optString("message")+"", Toast.LENGTH_SHORT).show();
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


    /* 레트로핏으로 서비스 번호, 서비스 상태 변경하는 코드 */
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

                        String stateStr = serviceStateTxt.getText().toString();

                        if(stateStr.equals("청소 전")){
                            /* 청소 전 상태에서, 이동 시작 버튼을 누른 경우, */
                            Log.d(TAG, "=== 청소 전 상태에서, 이동 하기 버튼을 누른 경우, ===" );
                            serviceStateTxt.setText("이동 중");
                            serviceStateChangeTxt.setText("청소 시작");

                            addressDetailTxt.setVisibility(View.VISIBLE);

                            //serviceState2Txt.setText("* 이동 중입니다.");
                            serviceState3Txt.setText("도착 시, 아래의 '청소 시작' 버튼을 눌러,");
                            serviceState4Txt.setText("고객에게 청소 시작을 알려주세요.");
                            serviceState4Txt.setVisibility(View.VISIBLE);

                        }else if(stateStr.equals("이동 중")){
                            /* 이동 중 상태에서, 청소 시작 버튼을 누른 경우, */
                            Log.d(TAG, "=== 이동 중 상태에서, 청소 시작 버튼을 누른 경우, ===" );
                            serviceStateTxt.setText("청소 중");
                            serviceStateChangeTxt.setText("청소 완료");

                            addressDetailTxt.setVisibility(View.VISIBLE);

                            //serviceState2Txt.setText("* 청소 중입니다.");
                            serviceState3Txt.setText("청소 완료 시, 아래의 '청소 완료' 버튼을 눌러,");
                            serviceState4Txt.setText("고객에게 청소 완료를 알려주세요.");
                            serviceState4Txt.setVisibility(View.VISIBLE);

                        }else if(stateStr.equals("청소 중")){
                            /* 청소 시작 상태에서, 청소 완료 버튼을 누른 경우, */
                            Log.d(TAG, "=== 청소 시작 상태에서, 청소 완료 버튼을 누른 경우, ===" );
                            serviceStateTxt.setText("청소 완료");
                            //serviceStateChangeTxt.setText("리뷰 보기");
                            serviceStateChangeTxt.setText("청소 완료");

                            serviceStateTxt.setText("청소 완료");
                            //serviceStateChangeTxt.setText("리뷰 확인");
                            serviceStateChangeTxt.setVisibility(View.GONE);
                            addressDetailTxt.setVisibility(View.GONE);

                            //serviceState2Txt.setText("* 청소가 완료되었습니다.");
                            serviceState3Txt.setVisibility(View.GONE);
                            serviceState4Txt.setVisibility(View.GONE);

                        }else if(stateStr.equals("청소 완료")){
                            /* 청소 중 상테에서 청소 완료 버튼을 누른 경우,*/
                            Log.d(TAG, "=== 청소 중 상테에서 청소 완료 버튼을 누른 경우, ===" );



                        }

                        Log.d(TAG, "=== serviceStateChangeTxt 클릭 막음 ===" );
//                        serviceStateChangeTxt.setEnabled(false);


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