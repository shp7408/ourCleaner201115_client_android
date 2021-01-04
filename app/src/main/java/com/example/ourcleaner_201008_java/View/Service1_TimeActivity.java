package com.example.ourcleaner_201008_java.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.speech.tts.TextToSpeech;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.bumptech.glide.Glide;
import com.example.ourcleaner_201008_java.Adapter.MyPlaceAdapter;
import com.example.ourcleaner_201008_java.Adapter.RecyclerDecoration;
import com.example.ourcleaner_201008_java.DTO.AddressDTO;
import com.example.ourcleaner_201008_java.DTO.BaddressDTO;
import com.example.ourcleaner_201008_java.DTO.MyplaceDTO;
import com.example.ourcleaner_201008_java.DTO.ServiceDTO;
import com.example.ourcleaner_201008_java.GlobalApplication;
import com.example.ourcleaner_201008_java.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import static java.lang.System.out;


public class Service1_TimeActivity extends AppCompatActivity implements MyPlaceAdapter.OnListItemSelectedInterface{

    private static final String TAG = "서비스 신청. 시간 정하기";

    private static final int MANAGER_REQUEST =1 ;

    /* 1회 예약인지, 정기 예약인지 확인하는 변수. intent로 받아옴. yes면 정기 no면 1회 예약*/
    String serviceRegularStr;

    /* 화면에서 쓰는 변수 선언 */

    TextView serTimePriTxt; //
    int startTime, needDefTime, needDefCost; //시작 시간, 필요한 기본 시간 분의 형태로, 평 수에 따라 책정된 시간(분), 가격

    //우리집 선택 관련 변수
    LinearLayout myplaceChoLayout1;
    LinearLayout ChoLayoutall1;
    TextView myplaceChoTxt;
    String myplaceChoStr;

    LinearLayout ChoLayoutall2; //리사이클러 뷰 있는 레이아웃 myplaceChoLayout1 클릭시, visible
    Button plusPlaceBtn;
    ImageButton close_btn2;

    //매니저 선택 관련 변수 -> 지정안해도 되므로 ㅇㅇㅇ 그냥 넘어감 불린 필요없음
    // TODO: 2020-10-23 매니저 부분 더 고민해야 함. 매니저 등록하면서
    Button managerChoBtn;
    String managerChoStr="매니저미지정"; //매니저 선택 후, 스트링 담는 변수
    String managerNameEmailStr="매니저미지정";

    //청소 주기 관련 변수
    LinearLayout regularTermLayout; //정기 예약시에만 보임
    TextView regularTerm1Txt, regularTerm2Txt; // 요일 클릭 시, 보임
    LinearLayout regulardaysBtnsLayout; //선택 후, 요일버튼레이아웃 닫음
    Button MonDBtn, TueDBtn, WendDBtn, ThursDBtn, FriDBtn, SatDBtn, SunDBtn; //요일들 버튼
    String regularTerm2Str; //클릭한 요일 담는 스트링 변수

    //방문 날짜 관련 변수
    LinearLayout dateLayout; //클릭하면 캘린더 뷰 나타남
    TextView date2Txt;
    String date2Str, date3Str; // 날짜, 요일
    CalendarView dateCalendar;

    //시간 관련 변수(얼마나 하고, 언제 시작하는지)
    LinearLayout timeLayout;
    TextView time2Txt; //선택 결과 나오는 텍스트 뷰
    String time2Str;

    //   시간 관련 변수 중, 정기 예약시 관련 변수
    TextView regulartimeTxt;
    LinearLayout regulartimeLayout;
    TextView regulartimeChanTxt;
    Button regulartimeChanBtn;
    TextView regulartimeChoTxt; //정기 예약시에만 보임

    ConstraintLayout timeDetailConst; //앞에 날짜 선택한 경우만 나타남
    Button time8Btn, time830Btn, time9Btn, time930Btn, time10Btn, time14Btn, time1430Btn, time15Btn, time1530Btn, time16Btn;
    String timeStartStr;

    //정기 예약 시, 시작 날짜 관련 변수
    LinearLayout regularstartdateLayout; //정기 예약시에만 나타남
    CalendarView regularstartdateCalendar;
    TextView regularstartdate2Txt;
    String regularstartdate2Str;

    //다음 버튼
    Button nextBtn;

    /* 선택 완료했는지 여부 저장하기 위한 변수 */
    // 1. 1회 선택 시,
    Boolean  dateBool, timeBool;

    // 2. 정기 선택 시, 더 필요한 변수
    Boolean regularTimeBool, regularTermIsBool,regularstartdateBool;


    /* 서버에서 내 장소 정보 받아오기 위한 변수 */
    String jsonResponse;

    /* 리사이클러뷰에 보여주기 위한 변수 */
    MyplaceDTO myplaceDTO, myplaceDTORecent, myplaceDTOChoose; //최근 데이터 받아서 보여주기 위함.
    ArrayList<MyplaceDTO> myplaceDTOArrayList = new ArrayList<>();

    // PlaceInputAdapter 리사이클러뷰 작업 2 단계.
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    /* 캘린더 관련 변수 */
    private long currentMinLong, currentMaxLong;

    /* 현재 엑티비티의 정보 담는 객체 */
    ServiceDTO serviceDTO;

    Boolean regularBool; //원래 String이었는데, Boolean이 더 직관적임. 변경할 것.



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service1__time);
        Log.d(TAG, "=== activity_service1__time 생명주기 onCreate ===" );

        //intent로 받을 때 사용하는 코드
        Intent intent = getIntent();
        Log.d(TAG, "홈프래그먼트에서 엑티비티에서 인텐트 받음 intent :"+intent);

        serviceRegularStr = intent.getStringExtra("serviceRegularStr");
        Log.d(TAG, "홈프래그먼트에서 인텐트 받음 intent.serviceRegularStr : "+ serviceRegularStr);

        if(serviceRegularStr.equals("no")){
            Log.d(TAG, "=== 정기 결제 no인 경우 === regularBool : " + regularBool );

            regularTerm2Str ="none";
            Log.d(TAG, "=== regularTerm2Str 없는 경우. 정기결제만 있음 ===" );
            regularBool = false;
        }else{
            Log.d(TAG, "=== 정기 결제 yes인 경우 === regularBool : " + regularBool );
            regularBool = true;
        }

        managerChoBtn= findViewById(R.id.managerChoBtn);

        managerChoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e(TAG, "=== managerChoBtn 클릭 ===" );

                if(myplaceDTOChoose==null){
                    Log.e(TAG, "=== myplaceDTOChoose 널임 ===" );
                    myplaceDTOChoose = myplaceDTORecent;
                }else{

                }

                serviceDTO = new ServiceDTO(GlobalApplication.currentUser, "신청중", myplaceDTOChoose, managerNameEmailStr,
                        regularBool, date2Str, date3Str, startTime, needDefTime, needDefCost );

                Log.d(TAG, "=== serviceDTO currentUser === "+ serviceDTO.getCurrentUser() );
                Log.d(TAG, "=== serviceDTO serviceState === "+ serviceDTO.getServiceState() );
                Log.d(TAG, "=== serviceDTO myplaceDTO === "+ serviceDTO.getMyplaceDTO() );
                Log.d(TAG, "=== serviceDTO managerName === "+ serviceDTO.getManagerName() );
                Log.d(TAG, "=== serviceDTO regularBool === "+ serviceDTO.getRegularBool() );
                Log.d(TAG, "=== serviceDTO visitDate === "+ serviceDTO.getVisitDate() );
                Log.d(TAG, "=== serviceDTO visitDay === "+ serviceDTO.getVisitDay() );
                Log.d(TAG, "=== serviceDTO startTime === "+ serviceDTO.getStartTime() );
                Log.d(TAG, "=== serviceDTO needDefTime === "+ serviceDTO.getNeedDefTime() );
                Log.d(TAG, "=== serviceDTO needDefCost === "+ serviceDTO.getNeedDefCost() );


                Log.d(TAG, "=== 객체 넣고 다음 화면으로 이동 ===" );
                Intent intent = new Intent(getApplicationContext(), Service_SelectManagersActivity.class);
                intent.putExtra("serviceDTO", serviceDTO);
                startActivity(intent);

            }
        });





        nextBtn = (Button) findViewById(R.id.nextBtn);
        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                    Log.d(TAG, "=== nextBtn 클릭 : === ");
//
//                    if(myplaceDTOChoose==null){
//                        Log.d(TAG, "=== myplaceDTOChoose 널인 경우 예외처리 ===" );
//                        myplaceDTOChoose= myplaceDTORecent;
//                    }
//
//                    if(date2Str.equals(null) && regularTerm2Str.equals(null) &&  startTime==0){
//                       Log.d(TAG, "=== visitDate 널인경우 ===" );
//                    }
//
//                    serviceDTO = new ServiceDTO(GlobalApplication.currentUser, "신청중", myplaceDTOChoose, managerNameEmailStr,
//                            regularBool, date2Str, date3Str, startTime, needDefTime, needDefCost );
//
//                    Log.d(TAG, "=== serviceDTO currentUser === "+ serviceDTO.getCurrentUser() );
//                    Log.d(TAG, "=== serviceDTO serviceState === "+ serviceDTO.getServiceState() );
//                    Log.d(TAG, "=== serviceDTO myplaceDTO === "+ serviceDTO.getMyplaceDTO() );
//                    Log.d(TAG, "=== serviceDTO managerName === "+ serviceDTO.getManagerName() );
//                    Log.d(TAG, "=== serviceDTO regularBool === "+ serviceDTO.getRegularBool() );
//                    Log.d(TAG, "=== serviceDTO visitDate === "+ serviceDTO.getVisitDate() );
//                    Log.d(TAG, "=== serviceDTO visitDay === "+ serviceDTO.getVisitDay() );
//                    Log.d(TAG, "=== serviceDTO startTime === "+ serviceDTO.getStartTime() );
//                    Log.d(TAG, "=== serviceDTO needDefTime === "+ serviceDTO.getNeedDefTime() );
//                    Log.d(TAG, "=== serviceDTO needDefCost === "+ serviceDTO.getNeedDefCost() );
//
//
//                    Log.d(TAG, "=== 객체 넣고 다음 화면으로 이동 ===" );
//
//                    //있으면 넘어감
//                    Intent intent = new Intent(getApplicationContext(), Service2_InfoActivity.class);
//                    intent.putExtra("serviceDTO", serviceDTO);
//                    startActivity(intent);

                    //finish();

                    }

            });
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "=== 생명주기 onResume() ===" );

        makeStringRequestGet();
        Log.d(TAG, "=== makeJsonArrayRequest() 메서드 종료 ===" );

        /* 현재 내 장소 정보를 모두 가져온 상태. 추가한 후에도 가져온 상태임. 이제는 리사이클러 뷰에 내 장소 목록을 보여줄 것임 */
        //리사이클러 뷰가 있는 엑티비티의 리사이클러 뷰 id 연결
        recyclerView = findViewById(R.id.myplace_recycle);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);

        recyclerView.setLayoutManager(layoutManager);

        // 기본 구분선 추가
        DividerItemDecoration dividerItemDecoration =
                new DividerItemDecoration(recyclerView.getContext(),new LinearLayoutManager(this).getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);

        // 아이템 간격 처리
        RecyclerDecoration spaceDecoration = new RecyclerDecoration(5);
        recyclerView.addItemDecoration(spaceDecoration);

        recyclerView.setNestedScrollingEnabled(true);

        mAdapter = new MyPlaceAdapter(getApplicationContext(),myplaceDTOArrayList, this );//리스너 구현을 위해 context 와 리스너 인자 추가함
        recyclerView.setAdapter(mAdapter);

        Log.d(TAG, "=== 리사이클러 뷰 생성 ===" );

        serTimePriTxt = findViewById(R.id.serTimePriTxt);
        myplaceChoLayout1 = findViewById(R.id.myplaceChoLayout1);
        myplaceChoTxt = findViewById(R.id.myplaceChoTxt);

        ChoLayoutall1 = findViewById(R.id.ChoLayoutall1);
        ChoLayoutall2 = findViewById(R.id.ChoLayoutall2);
        plusPlaceBtn = findViewById(R.id.plusPlaceBtn);
        close_btn2 = findViewById(R.id.close_btn2);

        // TODO: 2020-11-18 매니저 어플만든 후, 매니저 목록 띄우는 다이얼로그 생성하기 
        managerChoBtn = findViewById(R.id.managerChoBtn);


        //다음 버튼
        nextBtn = findViewById(R.id.nextBtn);


        myplaceChoLayout1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                    Log.d(TAG, "=== myplaceChoLayout1 클릭하면, myplaceChoLayout1 안보이고, ChoLayoutall2 나타남 === ");
                    ChoLayoutall2.setVisibility(View.VISIBLE);
                    }
            });

        plusPlaceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "=== 장소 추가 버튼 클릭  ===" );

                //엑티비티 이동 후, 리사이클러뷰있는 레이아웃 사라짐
                ChoLayoutall2.setVisibility(View.GONE);
                Intent intent = new Intent(getApplicationContext(), PlaceinputActivity.class);
                startActivity(intent);
            }
        });

        close_btn2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                    Log.d(TAG, "=== close_btn2 클릭하면, 리사이클러뷰 사라짐 === ");
                    ChoLayoutall2.setVisibility(View.GONE);
            }
            });


    }

//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        Log.d(TAG, "=== requestCode ===" +requestCode);
//        Log.d(TAG, "=== resultCode ===" +resultCode);
//        Log.d(TAG, "=== data ===" +data);
//
//        /* 1. 매니저 선택에서 */
//        if (resultCode == MANAGER_REQUEST && data != null ) {
//            Log.d(TAG, "=== MANAGER_REQUEST 받앙옴!!! ===" +MANAGER_REQUEST);
//            managerNameEmailStr = data.getStringExtra("managerNameEmailStr");
//            Log.d(TAG, "=== managerNameEmailStr ===" +managerNameEmailStr);
//
//            // 먼저 , 의 인덱스를 찾는다 - 인덱스 값: idx
//            int idx = managerNameEmailStr.indexOf(",");
//            managerChoStr = managerNameEmailStr.substring(0, idx);
//
//            managerChoBtn.setText(managerChoStr + " 매니저님");
//            Log.d(TAG, "=== managerChoStr ===" +managerChoStr);
//
//        }
//
//    }



    // 현재 사용자를 url에 넣어서 보내면, 사용자가 등록한 장소 목록들을 받아오는 메서드
    private void makeStringRequestGet() {

        String url = "http://52.79.179.66/checkMyPlace.php?currentUser="+ GlobalApplication.currentUser;
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
                            myplaceDTOArrayList.clear();

                            for (int i = 0; i < response.length(); i++) {

                                    JSONObject myPlace = (JSONObject) response.get(i);

                                    String currentUser = myPlace.getString("currentUser");
                                    String placeNameStr = myPlace.getString("placeNameStr");
                                    String address = myPlace.getString("address");
                                    String detailAddress = myPlace.getString("detailAddress");
                                    String sizeStr = myPlace.getString("sizeStr");
                                    int sizeIndexint = myPlace.getInt("sizeIndexint");

                                    jsonResponse += "currentUser: " + currentUser + "\n\n";
                                    jsonResponse += "placeNameStr: " + placeNameStr + "\n\n";
                                    jsonResponse += "address: " + address + "\n\n";
                                    jsonResponse += "detailAddress: " + detailAddress + "\n\n";
                                    jsonResponse += "sizeStr: " + sizeStr + "\n\n";
                                    jsonResponse += "sizeIndexint: " + sizeIndexint + "\n\n";

                                    myplaceDTO = new MyplaceDTO(currentUser, placeNameStr, address, detailAddress, sizeStr, sizeIndexint);
                                    Log.d(TAG, "=== myplaceDTO 객체 생성 ===");

                                    myplaceDTOArrayList.add(myplaceDTO);
                                    Log.d(TAG, "=== myplaceDTOArrayList.add(myplaceDTO)에 더함 ===");

                                // TODO: 2020-11-19 가장 최근 추가한 데이터로 책정한 가격과 시간을 setText 할 것
                                    if(i==(response.length()-1)){
                                        Log.d(TAG, "=== 가장 최근 추가한 데이터 집의 이름 ===placeNameStr : " +placeNameStr);

                                        myplaceDTORecent = new MyplaceDTO(currentUser, placeNameStr, address, detailAddress, sizeStr, sizeIndexint);

                                        //가장 최근 장소 데이터 객체의 값을 넣기
                                        myplaceChoTxt.setText(myplaceDTORecent.getPlaceNameStr()+"("+myplaceDTORecent.getSizeStr()+")\n"+myplaceDTORecent.getAddress()+" "+myplaceDTORecent.getDetailAddress());

                                        if(myplaceDTORecent.getSizeIndexint()>=0 && myplaceDTORecent.getSizeIndexint()<=4){
                                            Log.d(TAG, "=== 리사이클러 뷰 클릭 -> 평 수가 8평이하 ~ 12평 까지. ==="+ myplaceDTORecent.getSizeStr());
                                            Log.d(TAG, "=== 리사이클러 뷰 클릭 -> 평 수 인덱스 0 ~ 인덱스 4 ==="+ myplaceDTORecent.getSizeIndexint());

                                            needDefTime = 180;
                                            needDefCost = 30000;

                                        }else if(myplaceDTORecent.getSizeIndexint()>=5 && myplaceDTORecent.getSizeIndexint()<=38){
                                            Log.d(TAG, "=== 리사이클러 뷰 클릭 -> 평 수가 13평 ~ 50평 까지. ==="+ myplaceDTORecent.getSizeStr());
                                            Log.d(TAG, "=== 리사이클러 뷰 클릭 -> 평 수 인덱스 5 ~ 인덱스 38 ==="+ myplaceDTORecent.getSizeIndexint());

                                            needDefTime = 240;
                                            needDefCost = 42000;

                                        }else{
                                            Log.d(TAG, "=== 리사이클러 뷰 클릭 -> 평 수가 51평 ~ 101평 이상 까지. ==="+ myplaceDTORecent.getSizeStr());
                                            Log.d(TAG, "=== 리사이클러 뷰 클릭 -> 평 수 인덱스 39 ~ 인덱스 93 ==="+ myplaceDTORecent.getSizeIndexint());

                                            needDefTime = 300;
                                            needDefCost = 54000;

                                        }

                                        //숫자 천 자리에 콤마 찍기
                                        DecimalFormat formatter = new DecimalFormat("###,###");

                                        serTimePriTxt.setText(timeIntToHourMin(needDefTime)+"에\n" + formatter.format(needDefCost)+"원 예상됩니다.");



                                    }
                            }

//                            if(jsonResponse.isEmpty()){
//                                Log.d(TAG, "=== 반복문 이후에도 jsonResponse 비어 있음 ===" );
//
//                                /* 내 장소가 없으므로, 장소를 등록하는 엑티비티로 이동해야 함. */
//                                // 현재 사용자의 장소 정보가 등록되어 있는지 검증한  후,
//                                // 장소 정보가 있으면, 현재 엑티비티 진행.
//                                // 없으면, 장소 입력하는 엑티비티로 이동.
//
//                                try{
//                                    Log.d(TAG, "=== 가져온 장소 정보에 장소가 없는 경우, 장소 입력 엑티비티로 이동 ===" );
//                                    Intent intent = new Intent(getApplicationContext(), PlaceinputActivity.class);
//                                    startActivity(intent);
//                                }catch (Exception e){
//                                    Log.e(TAG, "=== 에러코드 ===" +e);
//                                }
//                             }
//
//
//                            Log.d(TAG, "=== jsonResponse 반복문 이후 ===" +jsonResponse);

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






    /* 나의 장소 리사이클러뷰에서 장소 아이템 선택 */
    @Override
    public void onItemSelected(View v, int position) {
        Log.d(TAG, "=== onItemSelected 아이템 하나 클릭 ===" );
        Log.d(TAG, "=== position ===" + position);

        myplaceDTOArrayList.get(position).getCurrentUser();

        String currentUser = myplaceDTOArrayList.get(position).getCurrentUser();
        String placeNameStr = myplaceDTOArrayList.get(position).getPlaceNameStr();
        String address = myplaceDTOArrayList.get(position).getAddress();
        String detailAddress = myplaceDTOArrayList.get(position).getDetailAddress();
        String sizeStr = myplaceDTOArrayList.get(position).getSizeStr();
        int sizeIndexint = myplaceDTOArrayList.get(position).getSizeIndexint();

        myplaceDTOChoose = new MyplaceDTO(currentUser, placeNameStr, address, detailAddress, sizeStr, sizeIndexint);

        //가장 최근 장소 데이터 객체의 값을 넣기
        myplaceChoTxt.setText(myplaceDTOChoose.getPlaceNameStr()+"("+myplaceDTOChoose.getSizeStr()+")\n"+myplaceDTOChoose.getAddress()+" "+myplaceDTOChoose.getDetailAddress());
        Log.d(TAG, "=== 가장 최근 장소 데이터 객체의 값을 넣기 ===" );

        ChoLayoutall2.setVisibility(View.GONE);

        /* 집 평수에 따라서 기본 시간 / 비용 결정하는 코드 */
        if(myplaceDTOChoose.getSizeIndexint()>=0 && myplaceDTOChoose.getSizeIndexint()<=4){
            Log.d(TAG, "=== 리사이클러 뷰 클릭 -> 평 수가 8평이하 ~ 12평 까지. ==="+ myplaceDTOChoose.getSizeStr());
            Log.d(TAG, "=== 리사이클러 뷰 클릭 -> 평 수 인덱스 0 ~ 인덱스 4 ==="+ myplaceDTOChoose.getSizeIndexint());

            needDefTime = 180;
            needDefCost = 30000;
        }else if(myplaceDTOChoose.getSizeIndexint()>=5 && myplaceDTOChoose.getSizeIndexint()<=38){
            Log.d(TAG, "=== 리사이클러 뷰 클릭 -> 평 수가 13평 ~ 50평 까지. ==="+ myplaceDTOChoose.getSizeStr());
            Log.d(TAG, "=== 리사이클러 뷰 클릭 -> 평 수 인덱스 5 ~ 인덱스 38 ==="+ myplaceDTOChoose.getSizeIndexint());

            needDefTime = 240;
            needDefCost = 42000;
        }else{
            Log.d(TAG, "=== 리사이클러 뷰 클릭 -> 평 수가 51평 ~ 101평 이상 까지. ==="+ myplaceDTOChoose.getSizeStr());
            Log.d(TAG, "=== 리사이클러 뷰 클릭 -> 평 수 인덱스 39 ~ 인덱스 93 ==="+ myplaceDTOChoose.getSizeIndexint());

            needDefTime = 300;
            needDefCost = 54000;
        }

        //숫자 천 자리에 콤마 찍기
        DecimalFormat formatter = new DecimalFormat("###,###");
        serTimePriTxt.setText(timeIntToHourMin(needDefTime)+"에\n" + formatter.format(needDefCost)+"원 예상됩니다.");





    }



    //int 형태의 정수 -> "3시간 30분" String으로 나타내는 메서드
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
            plusTimeStr = hour + "시간";
            Log.d(TAG, "=== plusTimeStr ===" +plusTimeStr);
        }else{
            Log.d(TAG, "=== hour 랑 minutes 둘 다 0이 아닌, 경우 ===" );
            plusTimeStr = hour +"시간 "+ minutes + "분";
            Log.d(TAG, "=== plusTimeStr ===" +plusTimeStr);
        }

        return plusTimeStr;
    }

    /** * 날짜로 요일 구하기 * @param date - 요일 구할 날짜 yyyyMMdd */
    public String getDayOfweek(String date){

        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        String[] week = {"일","월","화","수","목","금","토"};
        Calendar cal = Calendar.getInstance(); Date getDate;

        String result = null;

        try { getDate = format.parse(date);
            cal.setTime(getDate);
            int w = cal.get(Calendar.DAY_OF_WEEK)-1;
            out.println(date + "는 " + week[w] +"요일 입니다");

            result= week[w];

            Log.d(TAG, "=== 요일 === : " + week[w]);

        }
        catch (ParseException e) {
            e.printStackTrace();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public void settingdateCalendar(){
        // 현재시간을 msec 으로 구한다.
        long now = System.currentTimeMillis();

        // 현재시간을 date 변수에 저장한다.
        Date date = new Date(now);

        // 시간을 나타냇 포맷을 정한다 ( yyyy/MM/dd 같은 형태로 변형 가능 )
        SimpleDateFormat sdfNow = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        // nowDate 변수에 값을 저장한다.
        String formatDate = sdfNow.format(date);

        Log.d(TAG, "=== 자바 날짜 long now ===" + now);
        Log.d(TAG, "=== 자바 날짜 date ===" + date);
        Log.d(TAG, "=== 자바 날짜 sdfNow ===" + sdfNow);
        Log.d(TAG, "=== 자바 날짜 formatDate ===" + formatDate);

        // Java 시간 더하기
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        String today = null;

        today = sdfNow.format(cal.getTime());
        Log.d(TAG, "=== 자바 날짜 지금 today ===" + today);

        // 이틀 후
        cal.add(Calendar.DATE, +2);
        today = sdfNow.format(cal.getTime());
        Log.d(TAG, "=== 자바 날짜 오늘 부터 이틀 후 today ===" + today);

        Date currentDay = sdfNow.parse(today, new ParsePosition(0));
        currentMinLong = currentDay.getTime();
        Log.d(TAG, "=== 자바 날짜 오늘 부터 이틀 후 today 밀리세컨드로 ==="+currentMinLong);

        // 오늘 부터 14일 후
        cal.add(Calendar.DATE, +12);
        today = sdfNow.format(cal.getTime());
        Log.d(TAG, "=== 자바 날짜 오늘 부터 14일 후 today ===" + today);

        currentDay = sdfNow.parse(today, new ParsePosition(0));
        currentMaxLong = currentDay.getTime();
        Log.d(TAG, "=== 자바 날짜 오늘 14일 후 today 밀리세컨드로 ==="+currentMaxLong);

        //dateCalendar.setMaxDate(1570668133353L);
        dateCalendar.setMinDate(currentMinLong);
        dateCalendar.setMaxDate(currentMaxLong);
        Log.d(TAG, "=== 자바 날짜 예약날짜 범위 지정 완료 ===");

    }


    public void settingregularstartdateCalendar(){
        // 현재시간을 msec 으로 구한다.
        long now = System.currentTimeMillis();

        // 현재시간을 date 변수에 저장한다.
        Date date = new Date(now);

        // 시간을 나타냇 포맷을 정한다 ( yyyy/MM/dd 같은 형태로 변형 가능 )
        SimpleDateFormat sdfNow = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        // nowDate 변수에 값을 저장한다.
        String formatDate = sdfNow.format(date);

        Log.d(TAG, "=== 자바 날짜 long now ===" + now);
        Log.d(TAG, "=== 자바 날짜 date ===" + date);
        Log.d(TAG, "=== 자바 날짜 sdfNow ===" + sdfNow);
        Log.d(TAG, "=== 자바 날짜 formatDate ===" + formatDate);

        // Java 시간 더하기
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        String today = null;

        today = sdfNow.format(cal.getTime());
        Log.d(TAG, "=== 자바 날짜 지금 today ===" + today);

        // 이틀 후
        cal.add(Calendar.DATE, +2);
        today = sdfNow.format(cal.getTime());
        Log.d(TAG, "=== 자바 날짜 오늘 부터 이틀 후 today ===" + today);

        Date currentDay = sdfNow.parse(today, new ParsePosition(0));
        currentMinLong = currentDay.getTime();
        Log.d(TAG, "=== 자바 날짜 오늘 부터 이틀 후 today 밀리세컨드로 ==="+currentMinLong);

        // 오늘 부터 14일 후
        cal.add(Calendar.DATE, +12);
        today = sdfNow.format(cal.getTime());
        Log.d(TAG, "=== 자바 날짜 오늘 부터 14일 후 today ===" + today);

        currentDay = sdfNow.parse(today, new ParsePosition(0));
        currentMaxLong = currentDay.getTime();
        Log.d(TAG, "=== 자바 날짜 오늘 14일 후 today 밀리세컨드로 ==="+currentMaxLong);

        //dateCalendar.setMaxDate(1570668133353L);
        regularstartdateCalendar.setMinDate(currentMinLong);
        regularstartdateCalendar.setMaxDate(currentMaxLong);
        Log.d(TAG, "=== 자바 날짜 예약날짜 범위 지정 완료 ===");

    }


}