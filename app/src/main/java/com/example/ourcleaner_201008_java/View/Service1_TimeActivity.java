package com.example.ourcleaner_201008_java.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
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
import com.example.ourcleaner_201008_java.Adapter.MyPlaceAdapter;
import com.example.ourcleaner_201008_java.Adapter.RecyclerDecoration;
import com.example.ourcleaner_201008_java.DTO.MyplaceDTO;
import com.example.ourcleaner_201008_java.GlobalApplication;
import com.example.ourcleaner_201008_java.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static java.lang.System.out;


public class Service1_TimeActivity extends AppCompatActivity implements MyPlaceAdapter.OnListItemSelectedInterface{

    private static final String TAG = "서비스 신청. 시간 정하기";

    /* 1회 예약인지, 정기 예약인지 확인하는 변수. intent로 받아옴. yes면 정기 no면 1회 예약*/
    String serviceRegularStr;

    /* 화면에서 쓰는 변수 선언 */

    TextView serTimePriTxt; //
    String serTimeStr, serPriceStr; //서비스 제공 시간, 서비스 가격 <- 집의 평 수에 따라서 정해짐.

    //우리집 선택 관련 변수
    LinearLayout myplaceChoLayout1;
    LinearLayout ChoLayoutall1;
    TextView myplaceChoTxt;
    String myplaceChoStr;

    //리사이클러뷰에서 선택한 데이터를 담을 변수 -> 이걸로, myplaceChoTxt를 구성함
    String placeNameChoStr;
    String addressCho;
    String detailAddressCho;
    String sizeChoStr;
    int sizeIndexChoint;

    LinearLayout ChoLayoutall2; //리사이클러 뷰 있는 레이아웃 myplaceChoLayout1 클릭시, visible
    Button plusPlaceBtn;
    ImageButton close_btn2;

    //매니저 선택 관련 변수 -> 지정안해도 되므로 ㅇㅇㅇ 그냥 넘어감 불린 필요없음
    // TODO: 2020-10-23 매니저 부분 더 고민해야 함. 매니저 등록하면서
    Button managerChoBtn;
    String managerChoStr; //매니저 선택 후, 스트링 담는 변수

    //청소 주기 관련 변수
    LinearLayout regularTermLayout; //정기 예약시에만 보임
    TextView regularTerm1Txt, regularTerm2Txt; // 요일 클릭 시, 보임
    LinearLayout regulardaysBtnsLayout; //선택 후, 요일버튼레이아웃 닫음
    Button MonDBtn, TueDBtn, WendDBtn, ThursDBtn, FriDBtn, SatDBtn, SunDBtn; //요일들 버튼
    String regularTerm2Str; //클릭한 요일 담는 스트링 변수

    //방문 날짜 관련 변수
    LinearLayout dateLayout; //클릭하면 캘린더 뷰 나타남
    TextView date2Txt;
    String date2Str;
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

        managerChoBtn = findViewById(R.id.managerChoBtn);

        //청소 주기 관련 변수
        regularTermLayout = findViewById(R.id.regularTermLayout);

        regularTerm1Txt = findViewById(R.id.regularTerm1Txt);//정기 예약시에만 보임
        regularTerm2Txt = findViewById(R.id.regularTerm2Txt);// 요일 클릭 시, 보임

        regulardaysBtnsLayout = findViewById(R.id.regulardaysBtnsLayout);

        MonDBtn = findViewById(R.id.MonDBtn);
        TueDBtn = findViewById(R.id.TueDBtn);
        WendDBtn = findViewById(R.id.WendDBtn);
        ThursDBtn = findViewById(R.id.ThursDBtn);
        FriDBtn = findViewById(R.id.FriDBtn);
        SatDBtn = findViewById(R.id.SatDBtn);
        SunDBtn = findViewById(R.id.SunDBtn);

        //방문 날짜 관련 변수
        dateLayout = findViewById(R.id.dateLayout);
        date2Txt= findViewById(R.id.date2Txt);
        dateCalendar = findViewById(R.id.dateCalendar);

        //시간 관련 변수(얼마나 하고, 언제 시작하는지)
        timeLayout = findViewById(R.id.timeLayout);
        time2Txt= findViewById(R.id.time2Txt);

        //시간 관련변수 중 정기 결제 시 필요한 변수
        regulartimeChoTxt = findViewById(R.id.regulartimeChoTxt);
        regulartimeTxt = findViewById(R.id.regulartimeTxt);
        regulartimeLayout = findViewById(R.id.regulartimeLayout);
        regulartimeChanTxt = findViewById(R.id.regulartimeChanTxt);
        regulartimeChanBtn = findViewById(R.id.regulartimeChanBtn);

        //시간 선택하는 컨스트레이아웃
        timeDetailConst = findViewById(R.id.timeDetailConst);

        time8Btn = findViewById(R.id.time8Btn);
        time830Btn = findViewById(R.id.time830Btn);
        time9Btn = findViewById(R.id.time9Btn);
        time930Btn = findViewById(R.id.time930Btn);
        time10Btn = findViewById(R.id.time10Btn);
        time14Btn = findViewById(R.id.time14Btn);
        time1430Btn = findViewById(R.id.time1430Btn);
        time15Btn = findViewById(R.id.time15Btn);
        time1530Btn = findViewById(R.id.time1530Btn);
        time16Btn = findViewById(R.id.time16Btn);

        //정기 예약 시, 시작 날짜 관련 변수
        regularstartdateLayout = findViewById(R.id.regularstartdateLayout);
        regularstartdateCalendar = findViewById(R.id.regularstartdateCalendar);
        regularstartdate2Txt = findViewById(R.id.regularstartdate2Txt);

        //다음 버튼
        nextBtn = findViewById(R.id.nextBtn);

        if(serviceRegularStr.equals("no")){
            Log.d(TAG, "=== 1회 예약 버튼 클릭 시, 청소주기 레이아웃 전체 안보임, " +
                    "방문 날짜에서 텍스트2 랑 캘린더 안 보임," +
                    "시간에서 서비스시간 레이아웃 전체, 방문시간컨스트레이아웃 전체, 결과값 안 보임" +
                    "시작일 전체 안 보임===" );

            regularTermLayout.setVisibility(View.GONE);

            date2Txt.setVisibility(View.GONE);
            //dateCalendar.setVisibility(View.GONE); //달력 먼저 보여야 함.

            time2Txt.setVisibility(View.GONE);
            regulartimeChoTxt.setVisibility(View.GONE);
            regulartimeTxt.setVisibility(View.GONE);
            regulartimeLayout.setVisibility(View.GONE);

            timeDetailConst.setVisibility(View.GONE);

            regularstartdateLayout.setVisibility(View.GONE);
            regularstartdateCalendar.setVisibility(View.GONE);
            regularstartdate2Txt.setVisibility(View.GONE);

        }else{
            Log.d(TAG, "=== 정기 예약 버튼 클릭 시, 청소주기에서 결과2텍스트뷰, 요일버튼 안보임" +
                    "방문날짜 레이아웃 전체 안보임. 필요 없음" +
                    "시간에서 결과 텍스트뷰2, 서비스시간, 변경하는텍스트랑 버튼 안보임, 방문시간 버튼 컨스트 안보임" +
                    "시작일에서 결과값과 캘린더 안보임===" );
            regularTerm2Txt.setVisibility(View.GONE);
            //regulardaysBtnsLayout.setVisibility(View.GONE); //청소 주기 먼저 보여야 함

            dateLayout.setVisibility(View.GONE);

            time2Txt.setVisibility(View.GONE);
            dateCalendar.setVisibility(View.GONE);
            regulartimeTxt.setVisibility(View.GONE);
            regulartimeLayout.setVisibility(View.GONE);
            regulartimeChoTxt.setVisibility(View.GONE);
            timeDetailConst.setVisibility(View.GONE);

            regularstartdate2Txt.setVisibility(View.GONE);
            regularstartdateCalendar.setVisibility(View.GONE);
        }

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

        myplaceChoTxt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Log.d(TAG, "=== myplaceChoTxt 텍스트 뷰 값 변경 ===" );


            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        settingdateCalendar();

        settingregularstartdateCalendar();


        //방문 날짜에서 날짜 클릭 시
        dateCalendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int year, int month, int dayOfMonth) {

                Log.d(TAG, "=== 방문 날짜에서 날짜 클릭 시 ==="+String.format("%d / %d / %d",year,month+1,dayOfMonth));

                dateCalendar.setVisibility(View.GONE);
                Log.d(TAG, "=== 방문날짜 캘린더 사라짐 ===" );

                //date2Str = String.format("%d / %d / %d",year,month+1,dayOfMonth);

                String dayofWeek = getDayOfweek(String.format("%d%d%d",year,month+1,dayOfMonth)); //요일 가져오기 위한 형식

                Log.d(TAG, "=== 선택한 날짜의 요일 가져오기 ===" + dayofWeek);

                date2Txt.setVisibility(View.VISIBLE);
                date2Txt.setText(String.format("%d.%d("+dayofWeek+")",month+1,dayOfMonth));

                Log.d(TAG, "=== 셋텍스트 ===" +String.format("%d.%d("+dayofWeek+")",month+1,dayOfMonth));

                // TODO: 2020-10-24 저장하기 위한 날짜 포맷 확인할 것. date2Str 비어있음

                dateBool = true;
                Log.d(TAG, "=== dateBool === "+ dateBool );

                timeDetailConst.setVisibility(View.VISIBLE);




            }
        });

        //정기 결제에서 방문 날짜에서 날짜 클릭 시
        regularstartdateCalendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int year, int month, int dayOfMonth) {

                Log.d(TAG, "=== 방문 날짜에서 날짜 클릭 시 ==="+String.format("%d / %d / %d",year,month+1,dayOfMonth));

                regularstartdateCalendar.setVisibility(View.GONE);
                Log.d(TAG, "=== 방문날짜 캘린더 사라짐 ===" );

                //date2Str = String.format("%d / %d / %d",year,month+1,dayOfMonth);

                String dayofWeek = getDayOfweek(String.format("%d%d%d",year,month+1,dayOfMonth)); //요일 가져오기 위한 형식

                Log.d(TAG, "=== 선택한 날짜의 요일 가져오기 ===" + dayofWeek);

                regularstartdate2Txt.setVisibility(View.VISIBLE);
                regularstartdate2Txt.setText(String.format("%d.%d("+dayofWeek+")",month+1,dayOfMonth));

                Log.d(TAG, "=== 셋텍스트 ===" +String.format("%d.%d("+dayofWeek+")",month+1,dayOfMonth));

                // TODO: 2020-10-24 저장하기 위한 날짜 포맷 확인할 것. date2Str 비어있음

                regularstartdateBool = true;
                Log.d(TAG, "=== regularstartdateBool === "+ regularstartdateBool );


                nextBtn.setEnabled(true);

            }
        });


        //서비스 시작 시간 선택하는 버튼
        dateLayout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                    Log.d(TAG, "=== dateLayout 클릭 : 데이트 캘린더 등장 === ");
                    dateCalendar.setVisibility(View.VISIBLE);
                    timeDetailConst.setVisibility(View.GONE);
                    time2Txt.setText("");
                    time2Str="";

                time8Btn.setSelected(false);
                time830Btn.setSelected(false);
                time9Btn.setSelected(false);
                time930Btn.setSelected(false);
                time10Btn.setSelected(false);
                time14Btn.setSelected(false);
                time1430Btn.setSelected(false);
                time15Btn.setSelected(false);
                time1530Btn.setSelected(false);
                time16Btn.setSelected(false);


                    }

            });


                Button.OnClickListener onClickListener = new Button.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        switch (v.getId()){
                            //time8Btn 버튼 행동
                            case R.id.time8Btn:
                                time8Btn.setSelected(true);
                                time830Btn.setSelected(false);
                                time9Btn.setSelected(false);
                                time930Btn.setSelected(false);
                                time10Btn.setSelected(false);
                                time14Btn.setSelected(false);
                                time1430Btn.setSelected(false);
                                time15Btn.setSelected(false);
                                time1530Btn.setSelected(false);
                                time16Btn.setSelected(false);

                                time2Str = time8Btn.getText().toString();

                                time2Txt.setVisibility(View.VISIBLE);
                                time2Txt.setText(time2Str);
                                timeDetailConst.setVisibility(View.GONE);

                                nextBtn.setEnabled(true);

                                if(serviceRegularStr.equals("yes")){
                                    Log.d(TAG, "=== 8시 버튼 클릭 정기결제 텍스트뷰, 레이아웃, 텍스트뷰 사라짐===" );
                                    regulartimeTxt.setVisibility(View.GONE);
                                    regulartimeLayout.setVisibility(View.GONE);
                                    regulartimeChoTxt.setVisibility(View.GONE);
                                    nextBtn.setEnabled(false);
                                    regularstartdateCalendar.setVisibility(View.VISIBLE);

                                }

                                break;
                            //time830Btn 버튼 행동
                            case R.id.time830Btn:
                                time8Btn.setSelected(false);
                                time830Btn.setSelected(true);
                                time9Btn.setSelected(false);
                                time930Btn.setSelected(false);
                                time10Btn.setSelected(false);
                                time14Btn.setSelected(false);
                                time1430Btn.setSelected(false);
                                time15Btn.setSelected(false);
                                time1530Btn.setSelected(false);
                                time16Btn.setSelected(false);

                                time2Str = time830Btn.getText().toString();

                                time2Txt.setVisibility(View.VISIBLE);
                                time2Txt.setText(time2Str);
                                timeDetailConst.setVisibility(View.GONE);

                                nextBtn.setEnabled(true);

                                if(serviceRegularStr.equals("yes")){
                                    Log.d(TAG, "=== 8시 버튼 클릭 정기결제 텍스트뷰, 레이아웃, 텍스트뷰 사라짐===" );
                                    regulartimeTxt.setVisibility(View.GONE);
                                    regulartimeLayout.setVisibility(View.GONE);
                                    regulartimeChoTxt.setVisibility(View.GONE);
                                    nextBtn.setEnabled(false);
                                    regularstartdateCalendar.setVisibility(View.VISIBLE);

                                }
                                break;

                            //time9Btn 버튼 행동
                            case R.id.time9Btn:
                                time8Btn.setSelected(false);
                                time830Btn.setSelected(false);
                                time9Btn.setSelected(true);
                                time930Btn.setSelected(false);
                                time10Btn.setSelected(false);
                                time14Btn.setSelected(false);
                                time1430Btn.setSelected(false);
                                time15Btn.setSelected(false);
                                time1530Btn.setSelected(false);
                                time16Btn.setSelected(false);

                                time2Str = time9Btn.getText().toString();

                                time2Txt.setVisibility(View.VISIBLE);
                                time2Txt.setText(time2Str);
                                timeDetailConst.setVisibility(View.GONE);

                                nextBtn.setEnabled(true);

                                if(serviceRegularStr.equals("yes")){
                                    Log.d(TAG, "=== 8시 버튼 클릭 정기결제 텍스트뷰, 레이아웃, 텍스트뷰 사라짐===" );
                                    regulartimeTxt.setVisibility(View.GONE);
                                    regulartimeLayout.setVisibility(View.GONE);
                                    regulartimeChoTxt.setVisibility(View.GONE);
                                    nextBtn.setEnabled(false);
                                    regularstartdateCalendar.setVisibility(View.VISIBLE);

                                }
                                break;

                            //time930Btn 버튼 행동
                            case R.id.time930Btn:
                                time8Btn.setSelected(false);
                                time830Btn.setSelected(false);
                                time9Btn.setSelected(false);
                                time930Btn.setSelected(true);
                                time10Btn.setSelected(false);
                                time14Btn.setSelected(false);
                                time1430Btn.setSelected(false);
                                time15Btn.setSelected(false);
                                time1530Btn.setSelected(false);
                                time16Btn.setSelected(false);

                                time2Str = time930Btn.getText().toString();

                                time2Txt.setVisibility(View.VISIBLE);
                                time2Txt.setText(time2Str);
                                timeDetailConst.setVisibility(View.GONE);

                                nextBtn.setEnabled(true);

                                if(serviceRegularStr.equals("yes")){
                                    Log.d(TAG, "=== 8시 버튼 클릭 정기결제 텍스트뷰, 레이아웃, 텍스트뷰 사라짐===" );
                                    regulartimeTxt.setVisibility(View.GONE);
                                    regulartimeLayout.setVisibility(View.GONE);
                                    regulartimeChoTxt.setVisibility(View.GONE);
                                    nextBtn.setEnabled(false);
                                    regularstartdateCalendar.setVisibility(View.VISIBLE);

                                }
                                break;
                            //time10Btn 버튼 행동
                            case R.id.time10Btn:
                                time8Btn.setSelected(false);
                                time830Btn.setSelected(false);
                                time9Btn.setSelected(false);
                                time930Btn.setSelected(false);
                                time10Btn.setSelected(true);
                                time14Btn.setSelected(false);
                                time1430Btn.setSelected(false);
                                time15Btn.setSelected(false);
                                time1530Btn.setSelected(false);
                                time16Btn.setSelected(false);

                                time2Str = time10Btn.getText().toString();

                                time2Txt.setVisibility(View.VISIBLE);
                                time2Txt.setText(time2Str);
                                timeDetailConst.setVisibility(View.GONE);

                                nextBtn.setEnabled(true);

                                if(serviceRegularStr.equals("yes")){
                                    Log.d(TAG, "=== 8시 버튼 클릭 정기결제 텍스트뷰, 레이아웃, 텍스트뷰 사라짐===" );
                                    regulartimeTxt.setVisibility(View.GONE);
                                    regulartimeLayout.setVisibility(View.GONE);
                                    regulartimeChoTxt.setVisibility(View.GONE);
                                    nextBtn.setEnabled(false);
                                    regularstartdateCalendar.setVisibility(View.VISIBLE);

                                }
                                break;

                            //time14Btn 버튼 행동
                            case R.id.time14Btn:
                                time8Btn.setSelected(false);
                                time830Btn.setSelected(false);
                                time9Btn.setSelected(false);
                                time930Btn.setSelected(false);
                                time10Btn.setSelected(false);
                                time14Btn.setSelected(true);
                                time1430Btn.setSelected(false);
                                time15Btn.setSelected(false);
                                time1530Btn.setSelected(false);
                                time16Btn.setSelected(false);

                                time2Str = time14Btn.getText().toString();

                                time2Txt.setVisibility(View.VISIBLE);
                                time2Txt.setText(time2Str);
                                timeDetailConst.setVisibility(View.GONE);

                                if(serviceRegularStr.equals("yes")){
                                    Log.d(TAG, "=== 8시 버튼 클릭 정기결제 텍스트뷰, 레이아웃, 텍스트뷰 사라짐===" );
                                    regulartimeTxt.setVisibility(View.GONE);
                                    regulartimeLayout.setVisibility(View.GONE);
                                    regulartimeChoTxt.setVisibility(View.GONE);
                                    nextBtn.setEnabled(false);
                                    regularstartdateCalendar.setVisibility(View.VISIBLE);

                                }

                                nextBtn.setEnabled(true);
                                break;
                            //time1430Btn 버튼 행동
                            case R.id.time1430Btn:
                                time8Btn.setSelected(false);
                                time830Btn.setSelected(false);
                                time9Btn.setSelected(false);
                                time930Btn.setSelected(false);
                                time10Btn.setSelected(false);
                                time14Btn.setSelected(false);
                                time1430Btn.setSelected(true);
                                time15Btn.setSelected(false);
                                time1530Btn.setSelected(false);
                                time16Btn.setSelected(false);

                                time2Str = time1430Btn.getText().toString();

                                time2Txt.setVisibility(View.VISIBLE);
                                time2Txt.setText(time2Str);
                                timeDetailConst.setVisibility(View.GONE);

                                nextBtn.setEnabled(true);

                                if(serviceRegularStr.equals("yes")){
                                    Log.d(TAG, "=== 요일 버튼 클릭 정기결제 텍스트뷰, 레이아웃, 텍스트뷰 사라짐===" );
                                    regulartimeTxt.setVisibility(View.GONE);
                                    regulartimeLayout.setVisibility(View.GONE);
                                    regulartimeChoTxt.setVisibility(View.GONE);
                                    nextBtn.setEnabled(false);
                                    regularstartdateCalendar.setVisibility(View.VISIBLE);

                                }
                                break;

                            //time15Btn 버튼 행동
                            case R.id.time15Btn:
                                time8Btn.setSelected(false);
                                time830Btn.setSelected(false);
                                time9Btn.setSelected(false);
                                time930Btn.setSelected(false);
                                time10Btn.setSelected(false);
                                time14Btn.setSelected(false);
                                time1430Btn.setSelected(false);
                                time15Btn.setSelected(true);
                                time1530Btn.setSelected(false);
                                time16Btn.setSelected(false);

                                time2Str = time15Btn.getText().toString();

                                time2Txt.setVisibility(View.VISIBLE);
                                time2Txt.setText(time2Str);
                                timeDetailConst.setVisibility(View.GONE);

                                nextBtn.setEnabled(true);

                                if(serviceRegularStr.equals("yes")){
                                    Log.d(TAG, "=== 요일 버튼 클릭 정기결제 텍스트뷰, 레이아웃, 텍스트뷰 사라짐===" );
                                    regulartimeTxt.setVisibility(View.GONE);
                                    regulartimeLayout.setVisibility(View.GONE);
                                    regulartimeChoTxt.setVisibility(View.GONE);
                                    nextBtn.setEnabled(false);
                                    regularstartdateCalendar.setVisibility(View.VISIBLE);

                                }
                                break;

                            //time1530Btn 버튼 행동
                            case R.id.time1530Btn:
                                time8Btn.setSelected(false);
                                time830Btn.setSelected(false);
                                time9Btn.setSelected(false);
                                time930Btn.setSelected(false);
                                time10Btn.setSelected(false);
                                time14Btn.setSelected(false);
                                time1430Btn.setSelected(false);
                                time15Btn.setSelected(false);
                                time1530Btn.setSelected(true);
                                time16Btn.setSelected(false);

                                time2Str = time1530Btn.getText().toString();

                                time2Txt.setVisibility(View.VISIBLE);
                                time2Txt.setText(time2Str);
                                timeDetailConst.setVisibility(View.GONE);

                                nextBtn.setEnabled(true);

                                if(serviceRegularStr.equals("yes")){
                                    Log.d(TAG, "=== 8시 버튼 클릭 정기결제 텍스트뷰, 레이아웃, 텍스트뷰 사라짐===" );
                                    regulartimeTxt.setVisibility(View.GONE);
                                    regulartimeLayout.setVisibility(View.GONE);
                                    regulartimeChoTxt.setVisibility(View.GONE);
                                    nextBtn.setEnabled(false);
                                    regularstartdateCalendar.setVisibility(View.VISIBLE);

                                }
                                break;

                            //time16Btn 버튼 행동
                            case R.id.time16Btn:
                                time8Btn.setSelected(false);
                                time830Btn.setSelected(false);
                                time9Btn.setSelected(false);
                                time930Btn.setSelected(false);
                                time10Btn.setSelected(false);
                                time14Btn.setSelected(false);
                                time1430Btn.setSelected(false);
                                time15Btn.setSelected(false);
                                time1530Btn.setSelected(false);
                                time16Btn.setSelected(true);

                                time2Str = time16Btn.getText().toString();

                                time2Txt.setVisibility(View.VISIBLE);
                                time2Txt.setText(time2Str);
                                timeDetailConst.setVisibility(View.GONE);

                                nextBtn.setEnabled(true);

                                if(serviceRegularStr.equals("yes")){
                                    Log.d(TAG, "=== 요일 버튼 클릭 정기결제 텍스트뷰, 레이아웃, 텍스트뷰 사라짐===" );
                                    regulartimeTxt.setVisibility(View.GONE);
                                    regulartimeLayout.setVisibility(View.GONE);
                                    regulartimeChoTxt.setVisibility(View.GONE);

                                    nextBtn.setEnabled(false);
                                    regularstartdateCalendar.setVisibility(View.VISIBLE);


                                }
                                break;
                        }
                    }
                };

        time8Btn.setOnClickListener(onClickListener);
        time830Btn.setOnClickListener(onClickListener);
        time9Btn.setOnClickListener(onClickListener);
        time930Btn.setOnClickListener(onClickListener);
        time10Btn.setOnClickListener(onClickListener);
        time14Btn.setOnClickListener(onClickListener);
        time1430Btn.setOnClickListener(onClickListener);
        time15Btn.setOnClickListener(onClickListener);
        time1530Btn.setOnClickListener(onClickListener);
        time16Btn.setOnClickListener(onClickListener);



                Button.OnClickListener onClickListener2 = new Button.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        switch (v.getId()){
                            //MonDBtn 버튼 행동
                            case R.id.MonDBtn:
                                MonDBtn.setSelected(true);
                                TueDBtn.setSelected(false);
                                WendDBtn.setSelected(false);
                                ThursDBtn.setSelected(false);
                                FriDBtn.setSelected(false);
                                SatDBtn.setSelected(false);
                                SunDBtn.setSelected(false);


                                regularTerm2Str=MonDBtn.getText().toString();
                                Log.d(TAG, "=== regularTerm2Str ===" +regularTerm2Str);


                                regularTerm2Txt.setText("매주 "+regularTerm2Str+" 요일");
                                regularTerm2Txt.setVisibility(View.VISIBLE);

                                regulardaysBtnsLayout.setVisibility(View.GONE);

                                regulartimeTxt.setVisibility(View.VISIBLE);
                                regulartimeLayout.setVisibility(View.VISIBLE);
                                regulartimeChoTxt.setVisibility(View.VISIBLE);
                                timeDetailConst.setVisibility(View.VISIBLE);

                                break;
                            //TueDBtn 버튼 행동
                            case R.id.TueDBtn:
                                MonDBtn.setSelected(false);
                                TueDBtn.setSelected(true);
                                WendDBtn.setSelected(false);
                                ThursDBtn.setSelected(false);
                                FriDBtn.setSelected(false);
                                SatDBtn.setSelected(false);
                                SunDBtn.setSelected(false);

                                regularTerm2Str=TueDBtn.getText().toString();
                                Log.d(TAG, "=== regularTerm2Str ===" +regularTerm2Str);

                                regularTerm2Txt.setText("매주 "+regularTerm2Str+" 요일");
                                regularTerm2Txt.setVisibility(View.VISIBLE);

                                regulardaysBtnsLayout.setVisibility(View.GONE);

                                regulartimeTxt.setVisibility(View.VISIBLE);
                                regulartimeLayout.setVisibility(View.VISIBLE);
                                regulartimeChoTxt.setVisibility(View.VISIBLE);
                                timeDetailConst.setVisibility(View.VISIBLE);
                                break;

                            //WendDBtn 버튼 행동
                            case R.id.WendDBtn:
                                MonDBtn.setSelected(true);
                                TueDBtn.setSelected(false);
                                WendDBtn.setSelected(true);
                                ThursDBtn.setSelected(false);
                                FriDBtn.setSelected(false);
                                SatDBtn.setSelected(false);
                                SunDBtn.setSelected(false);

                                regularTerm2Str=WendDBtn.getText().toString();
                                Log.d(TAG, "=== regularTerm2Str ===" +regularTerm2Str);

                                regularTerm2Txt.setText("매주 "+regularTerm2Str+" 요일");
                                regularTerm2Txt.setVisibility(View.VISIBLE);

                                regulardaysBtnsLayout.setVisibility(View.GONE);

                                regulartimeTxt.setVisibility(View.VISIBLE);
                                regulartimeLayout.setVisibility(View.VISIBLE);
                                regulartimeChoTxt.setVisibility(View.VISIBLE);
                                timeDetailConst.setVisibility(View.VISIBLE);
                                break;

                            //ThursDBtn 버튼 행동
                            case R.id.ThursDBtn:
                                MonDBtn.setSelected(false);
                                TueDBtn.setSelected(false);
                                WendDBtn.setSelected(false);
                                ThursDBtn.setSelected(true);
                                FriDBtn.setSelected(false);
                                SatDBtn.setSelected(false);
                                SunDBtn.setSelected(false);

                                regularTerm2Str=ThursDBtn.getText().toString();
                                Log.d(TAG, "=== regularTerm2Str ===" +regularTerm2Str);

                                regularTerm2Txt.setText("매주 "+regularTerm2Str+" 요일");
                                regularTerm2Txt.setVisibility(View.VISIBLE);

                                regulardaysBtnsLayout.setVisibility(View.GONE);

                                regulartimeTxt.setVisibility(View.VISIBLE);
                                regulartimeLayout.setVisibility(View.VISIBLE);
                                regulartimeChoTxt.setVisibility(View.VISIBLE);
                                timeDetailConst.setVisibility(View.VISIBLE);
                                break;

                            //FriDBtn 버튼 행동
                            case R.id.FriDBtn:

                                MonDBtn.setSelected(false);
                                TueDBtn.setSelected(false);
                                WendDBtn.setSelected(false);
                                ThursDBtn.setSelected(false);
                                FriDBtn.setSelected(true);
                                SatDBtn.setSelected(false);
                                SunDBtn.setSelected(false);

                                regularTerm2Str=FriDBtn.getText().toString();
                                Log.d(TAG, "=== regularTerm2Str ===" +regularTerm2Str);

                                regularTerm2Txt.setText("매주 "+regularTerm2Str+" 요일");
                                regularTerm2Txt.setVisibility(View.VISIBLE);

                                regulardaysBtnsLayout.setVisibility(View.GONE);

                                regulartimeTxt.setVisibility(View.VISIBLE);
                                regulartimeLayout.setVisibility(View.VISIBLE);
                                regulartimeChoTxt.setVisibility(View.VISIBLE);
                                timeDetailConst.setVisibility(View.VISIBLE);
                                break;

                            //SatDBtn 버튼 행동
                            case R.id.SatDBtn:
                                MonDBtn.setSelected(false);
                                TueDBtn.setSelected(false);
                                WendDBtn.setSelected(false);
                                ThursDBtn.setSelected(false);
                                FriDBtn.setSelected(false);
                                SatDBtn.setSelected(true);
                                SunDBtn.setSelected(false);

                                regularTerm2Str=SatDBtn.getText().toString();
                                Log.d(TAG, "=== regularTerm2Str ===" +regularTerm2Str);

                                regularTerm2Txt.setText("매주 "+regularTerm2Str+" 요일");
                                regularTerm2Txt.setVisibility(View.VISIBLE);

                                regulardaysBtnsLayout.setVisibility(View.GONE);

                                regulartimeTxt.setVisibility(View.VISIBLE);
                                regulartimeLayout.setVisibility(View.VISIBLE);
                                regulartimeChoTxt.setVisibility(View.VISIBLE);
                                timeDetailConst.setVisibility(View.VISIBLE);
                                break;

                            //SunDBtn 버튼 행동
                            case R.id.SunDBtn:
                                MonDBtn.setSelected(false);
                                TueDBtn.setSelected(false);
                                WendDBtn.setSelected(false);
                                ThursDBtn.setSelected(false);
                                FriDBtn.setSelected(false);
                                SatDBtn.setSelected(false);
                                SunDBtn.setSelected(true);

                                regularTerm2Str=SunDBtn.getText().toString();
                                Log.d(TAG, "=== regularTerm2Str ===" +regularTerm2Str);

                                regularTerm2Txt.setText("매주 "+regularTerm2Str+" 요일");
                                regularTerm2Txt.setVisibility(View.VISIBLE);

                                regulardaysBtnsLayout.setVisibility(View.GONE);

                                regulartimeTxt.setVisibility(View.VISIBLE);
                                regulartimeLayout.setVisibility(View.VISIBLE);
                                regulartimeChoTxt.setVisibility(View.VISIBLE);
                                timeDetailConst.setVisibility(View.VISIBLE);
                                break;
                        }
                    }
                };


                MonDBtn.setOnClickListener(onClickListener2);
        TueDBtn.setOnClickListener(onClickListener2);
        WendDBtn.setOnClickListener(onClickListener2);
        ThursDBtn.setOnClickListener(onClickListener2);
        FriDBtn.setOnClickListener(onClickListener2);
        SatDBtn.setOnClickListener(onClickListener2);
        SunDBtn.setOnClickListener(onClickListener2);


        regularTerm2Txt.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                    Log.d(TAG, "=== regularTerm2Txt 클릭 : === ");

                    regulardaysBtnsLayout.setVisibility(View.VISIBLE);

                // TODO: 2020-10-24 장소별 기본 시간 세팅해야 함.
                MonDBtn.setSelected(false);
                TueDBtn.setSelected(false);
                WendDBtn.setSelected(false);
                ThursDBtn.setSelected(false);
                FriDBtn.setSelected(false);
                SatDBtn.setSelected(false);
                SunDBtn.setSelected(false);

                regularTerm2Txt.setText("");
                regularTerm2Str="";




                    }

            });


    }

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

                                    Log.d(TAG, "===  === i :" + i);

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

                                    if(i==(response.length()-1)){
                                        Log.d(TAG, "=== 가장 최근 추가한 데이터 집의 이름 ===placeNameStr : " +placeNameStr);

                                        myplaceDTORecent = new MyplaceDTO(currentUser, placeNameStr, address, detailAddress, sizeStr, sizeIndexint);

                                        //가장 최근 장소 데이터 객체의 값을 넣기
                                        myplaceChoTxt.setText(myplaceDTORecent.getPlaceNameStr()+"("+myplaceDTORecent.getSizeStr()+")\n"+myplaceDTORecent.getAddress()+" "+myplaceDTORecent.getDetailAddress());


                                    }


                            }

                            if(jsonResponse.isEmpty()){
                                Log.d(TAG, "=== 반복문 이후에도 jsonResponse 비어 있음 ===" );

                                /* 내 장소가 없으므로, 장소를 등록하는 엑티비티로 이동해야 함. */
                                // 현재 사용자의 장소 정보가 등록되어 있는지 검증한  후,
                                // 장소 정보가 있으면, 현재 엑티비티 진행.
                                // 없으면, 장소 입력하는 엑티비티로 이동.

                                Log.d(TAG, "=== 가져온 장소 정보에 장소가 없는 경우, 장소 입력 엑티비티로 이동 ===" );
                                Intent intent = new Intent(getApplicationContext(), PlaceinputActivity.class);
                                startActivity(intent);
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

        ChoLayoutall2.setVisibility(View.GONE);


    }

    /** * 날짜로 요일 구하기 * @param date - 요일 구할 날짜 yyyyMMdd*/
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