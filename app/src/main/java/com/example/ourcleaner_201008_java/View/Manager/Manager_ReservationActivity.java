package com.example.ourcleaner_201008_java.View.Manager;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.ourcleaner_201008_java.Adapter.ManagerReservationAdapter;
import com.example.ourcleaner_201008_java.Adapter.MyPlaceAdapter;
import com.example.ourcleaner_201008_java.Adapter.RecyclerDecoration;
import com.example.ourcleaner_201008_java.DTO.ManagerWaitingDTO;
import com.example.ourcleaner_201008_java.GlobalApplication;
import com.example.ourcleaner_201008_java.R;
import com.example.ourcleaner_201008_java.View.LoginActivity;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.prolificinteractive.materialcalendarview.OnMonthChangedListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.threeten.bp.DayOfWeek;

import java.util.ArrayList;

import static android.content.Intent.FLAG_ACTIVITY_NO_HISTORY;

public class Manager_ReservationActivity extends AppCompatActivity
        implements ManagerReservationAdapter.OnListItemSelectedInterface, MyPlaceAdapter.OnListItemSelectedInterface
        ,OnDateSelectedListener, OnMonthChangedListener {

    private static final String TAG = "매니저용예정목록화면";

    /* 매니저용 전체 메뉴 변수 */
    TextView waitingTxt, myWorkListTxt, chatListTxt, moreTxt;


    /* 리사이클러뷰에 보여주기 위한 변수 */
    ManagerWaitingDTO managerWaitingDTO;
    ArrayList<ManagerWaitingDTO> managerWaitingDTOArrayList = new ArrayList<>();

    // PlaceInputAdapter 리사이클러뷰 작업 2 단계.
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    /* 서버에서 내 장소 정보 받아오기 위한 변수 */
    String jsonResponse;

    /* material 달력 */
    MaterialCalendarView materialCalendarView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager__reservation);
        Log.d(TAG, "=== onCreate ===" );

        makeStringRequestGet();

        /* 매니저용 전체 메뉴 코드 */
        //각버튼 아이디 매칭
        waitingTxt = findViewById(R.id.waitingTxt);
        myWorkListTxt = findViewById(R.id.myWorkListTxt);
        chatListTxt = findViewById(R.id.chatListTxt);
        moreTxt = findViewById(R.id.moreTxt);

        materialCalendarView = findViewById(R.id.calendarView);

        materialCalendarView.setOnDateChangedListener(this);


        materialCalendarView.setOnDateChangedListener(this);
        materialCalendarView.addDecorator(new TodayDecorator());

       // CalendarDay maxdate = CalendarDay.from(CalendarDay.today().getYear(), CalendarDay.today().getMonth(), CalendarDay.today().getDay()+14);

        materialCalendarView.setCurrentDate(CalendarDay.today());
        materialCalendarView.setSelectedDate(CalendarDay.today());
        materialCalendarView.state().edit()
                .setFirstDayOfWeek(DayOfWeek.SUNDAY)
                //.setMaximumDate(maxdate)
                .commit();


        Button.OnClickListener onClickListener = new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    //waitingTxt 버튼 행동
                    case R.id.waitingTxt:
                        Log.d(TAG, "=== waitingTxt ===" );
                        //있으면 넘어감
                        Intent intent = new Intent(getApplicationContext(), Manager_MainActivity.class);
                        startActivity(intent);
                        finish();

                        break;
                    //myWorkListTxt 버튼 행동
                    case R.id.myWorkListTxt:
                        Log.d(TAG, "=== myWorkListTxt ===" );
                        //있으면 넘어감
//                        Intent intent2 = new Intent(getApplicationContext(), Manager_ReservationActivity.class);
//                        startActivity(intent2);
//                        finish();
                        break;

                    //chatListTxt 버튼 행동
                    case R.id.chatListTxt:
                        Log.d(TAG, "=== chatListTxt ===" );
                        //있으면 넘어감
                        Intent intent3 = new Intent(getApplicationContext(), Manager_ChatActivity.class);
                        startActivity(intent3);
                        finish();
                        break;

                    //moreTxt 버튼 행동
                    case R.id.moreTxt:
                        Log.d(TAG, "=== moreTxt ===" );
                        //있으면 넘어감
                        Intent intent4 = new Intent(getApplicationContext(), Manager_MoreActivity.class);
                        startActivity(intent4);
                        finish();
                        break;
                }
            }
        };

        waitingTxt.setOnClickListener(onClickListener);
        myWorkListTxt.setOnClickListener(onClickListener);
        chatListTxt.setOnClickListener(onClickListener);
        moreTxt.setOnClickListener(onClickListener);

        /* 현재 내 장소 정보를 모두 가져온 상태. 추가한 후에도 가져온 상태임. 이제는 리사이클러 뷰에 내 장소 목록을 보여줄 것임 */
        //리사이클러 뷰가 있는 엑티비티의 리사이클러 뷰 id 연결
        recyclerView = findViewById(R.id.myWorkList_recycle);
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

        mAdapter = new ManagerReservationAdapter(getApplicationContext(),managerWaitingDTOArrayList, this);//리스너 구현을 위해 context 와 리스너 인자 추가함
        recyclerView.setAdapter(mAdapter);


    }



    /* 오늘 날짜로 업무 목록 서버에서 받아오는 코드 */
    private void makeStringRequestGet() {

        /* 오늘 날짜 만드는 코드 */
        CalendarDay date = CalendarDay.today();
        String dateStr = date.toString();
        Log.d(TAG, "=== prepareData dateStr ===" +dateStr);

        String year = dateStr.substring(12,16);
        Log.d(TAG, "=== prepareData year ===" +year);

        //월 일 붙어있는거
        String monthDayAll = dateStr.substring(17);
        Log.d(TAG, "=== prepareData monthDayAll ===" +monthDayAll);

        //월
        String month = monthDayAll.substring(0, monthDayAll.indexOf("-"));
        Log.d(TAG, "=== prepareData month ===" +month);

        //일
        String dayOfMonth = monthDayAll.substring(monthDayAll.lastIndexOf("-")+1, monthDayAll.length()-1);
        Log.d(TAG, "=== prepareData dayOfMonth ===" +dayOfMonth);

        int yearInt = Integer.parseInt(year); //2021
        int monthInt = Integer.parseInt(month); //1
        int dayOfMonthInt = Integer.parseInt(dayOfMonth); //6

        String resultDate = monthInt+"."+dayOfMonthInt;
        Log.d(TAG, "=== prepareData resultDate ==="+resultDate );




        managerWaitingDTOArrayList = new ArrayList<>();

        String url = "http://52.79.179.66/managerWaitingMatching.php?managerNameId="
                +GlobalApplication.currentManagerName+","+GlobalApplication.currentManager
                +"&date="+resultDate;

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
                            managerWaitingDTOArrayList.clear();
                            final int numberOfItemsInResp = response.length();

                            for (int i = 0; i < numberOfItemsInResp; i++) {

                                Log.d(TAG, "===  === i :" + i);

                                JSONObject managerWaiting = (JSONObject) response.get(i);

                                String uid = managerWaiting.getString("uid");
                                String currentUser = managerWaiting.getString("currentUser");
                                String serviceState = managerWaiting.getString("serviceState");
                                String myplaceDTO_address = managerWaiting.getString("myplaceDTO_address");
                                String myplaceDTO_sizeStr = managerWaiting.getString("myplaceDTO_sizeStr");
                                String visitDate = managerWaiting.getString("visitDate");
                                String visitDay = managerWaiting.getString("visitDay");
                                String startTime = managerWaiting.getString("startTime");
                                String needDefTime = managerWaiting.getString("needDefTime");
                                String needDefCost = managerWaiting.getString("needDefCost");


                                jsonResponse += "uid: " + uid + "\n\n";
                                jsonResponse += "currentUser: " + currentUser + "\n\n";
                                jsonResponse += "serviceState: " + serviceState + "\n\n";
                                jsonResponse += "myplaceDTO_address: " + myplaceDTO_address + "\n\n";
                                jsonResponse += "myplaceDTO_sizeStr: " + myplaceDTO_sizeStr + "\n\n";
                                jsonResponse += "visitDate: " + visitDate + "\n\n";
                                jsonResponse += "visitDay: " + visitDay + "\n\n";
                                jsonResponse += "startTime: " + startTime + "\n\n";
                                jsonResponse += "needDefTime: " + needDefTime + "\n\n";
                                jsonResponse += "needDefCost: " + needDefCost + "\n\n";

                                String dateDayStr= (visitDate+"("+visitDay+")"); //11.26(목) 형태

                                managerWaitingDTO = new ManagerWaitingDTO(Integer.parseInt(uid), currentUser,
                                        dateDayStr, Integer.parseInt(startTime), Integer.parseInt(needDefTime),
                                        Integer.parseInt(needDefCost), myplaceDTO_address.substring(8,14),
                                        myplaceDTO_sizeStr,serviceState);
                                Log.d(TAG, "=== myplaceDTO 객체 생성 ===");

                                managerWaitingDTOArrayList.add(managerWaitingDTO);
                            }

                            mAdapter.notifyDataSetChanged();
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
                Log.d(TAG, "===  === Error " + String.valueOf(error));
            }
        });

        // Adding request to request queue
        GlobalApplication.getInstance().addToRequestQueue(req);
    }







    /* 캘린더에서 클릭한 날짜로 업무 목록 서버에서 받아오는 코드 */
    private void makeStringRequestGet2(CalendarDay date) {

        /* 오늘 날짜 만드는 코드 */
//        CalendarDay date = CalendarDay.today();
        String dateStr = date.toString();
        Log.d(TAG, "=== prepareData dateStr ===" +dateStr);

        String year = dateStr.substring(12,16);
        Log.d(TAG, "=== prepareData year ===" +year);

        //월 일 붙어있는거
        String monthDayAll = dateStr.substring(17);
        Log.d(TAG, "=== prepareData monthDayAll ===" +monthDayAll);

        //월
        String month = monthDayAll.substring(0, monthDayAll.indexOf("-"));
        Log.d(TAG, "=== prepareData month ===" +month);

        //일
        String dayOfMonth = monthDayAll.substring(monthDayAll.lastIndexOf("-")+1, monthDayAll.length()-1);
        Log.d(TAG, "=== prepareData dayOfMonth ===" +dayOfMonth);

        int yearInt = Integer.parseInt(year); //2021
        int monthInt = Integer.parseInt(month); //1
        int dayOfMonthInt = Integer.parseInt(dayOfMonth); //6

        String resultDate = monthInt+"."+dayOfMonthInt;
        Log.d(TAG, "=== prepareData resultDate ==="+resultDate );




        managerWaitingDTOArrayList = new ArrayList<>();

        String url = "http://52.79.179.66/managerWaitingMatching.php?managerNameId="
                +GlobalApplication.currentManagerName+","+GlobalApplication.currentManager
                +"&date="+resultDate;

        JsonArrayRequest req = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, "makeStringRequestGet2() onResponse"+response.toString());

                        try {
                            // Parsing json array response
                            // loop through each json object
                            jsonResponse = "";
                            Log.d(TAG, "=== makeStringRequestGet2() jsonResponse ===" +jsonResponse);

                            //리사이클러뷰에 여러개 추가되는 것 막음
                            managerWaitingDTOArrayList.clear();
                            final int numberOfItemsInResp = response.length();

                            for (int i = 0; i < numberOfItemsInResp; i++) {

                                Log.d(TAG, "=== makeStringRequestGet2() === i :" + i);

                                JSONObject managerWaiting = (JSONObject) response.get(i);

                                String uid = managerWaiting.getString("uid");
                                String currentUser = managerWaiting.getString("currentUser");
                                String serviceState = managerWaiting.getString("serviceState");
                                String myplaceDTO_address = managerWaiting.getString("myplaceDTO_address");
                                String myplaceDTO_sizeStr = managerWaiting.getString("myplaceDTO_sizeStr");
                                String visitDate = managerWaiting.getString("visitDate");
                                String visitDay = managerWaiting.getString("visitDay");
                                String startTime = managerWaiting.getString("startTime");
                                String needDefTime = managerWaiting.getString("needDefTime");
                                String needDefCost = managerWaiting.getString("needDefCost");


                                jsonResponse += "uid: " + uid + "\n\n";
                                jsonResponse += "currentUser: " + currentUser + "\n\n";
                                jsonResponse += "serviceState: " + serviceState + "\n\n";
                                jsonResponse += "myplaceDTO_address: " + myplaceDTO_address + "\n\n";
                                jsonResponse += "myplaceDTO_sizeStr: " + myplaceDTO_sizeStr + "\n\n";
                                jsonResponse += "visitDate: " + visitDate + "\n\n";
                                jsonResponse += "visitDay: " + visitDay + "\n\n";
                                jsonResponse += "startTime: " + startTime + "\n\n";
                                jsonResponse += "needDefTime: " + needDefTime + "\n\n";
                                jsonResponse += "needDefCost: " + needDefCost + "\n\n";

                                String dateDayStr= (visitDate+"("+visitDay+")"); //11.26(목) 형태

                                managerWaitingDTO = new ManagerWaitingDTO(Integer.parseInt(uid), currentUser,
                                        dateDayStr, Integer.parseInt(startTime), Integer.parseInt(needDefTime),
                                        Integer.parseInt(needDefCost), myplaceDTO_address.substring(8,14),
                                        myplaceDTO_sizeStr,serviceState);
                                Log.d(TAG, "=== makeStringRequestGet2() myplaceDTO 객체 생성 ===");

                                managerWaitingDTOArrayList.add(managerWaitingDTO);
                            }


                            mAdapter = new ManagerReservationAdapter(getApplicationContext()
                            , managerWaitingDTOArrayList, Manager_ReservationActivity.this::onItemSelected);


                            mAdapter.notifyDataSetChanged();
                            Log.d(TAG, "=== jsonResponse 반복문 이후 ===" +jsonResponse);

                            recyclerView.setAdapter(mAdapter);



                        } catch (JSONException e) {
                            e.printStackTrace();

                            Log.d(TAG, "=== Error ===  " + e);
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                Log.d(TAG, "===  === Error " + String.valueOf(error));
            }
        });

        // Adding request to request queue
        GlobalApplication.getInstance().addToRequestQueue(req);
    }









    @Override
    public void onItemSelected(View v, int position) {
        Log.e(TAG, "=== position ===" +position);

        //있으면 넘어감
        Intent intent = new Intent(getApplicationContext(), Manager_Detail_MatchingPostActivity.class);

        intent.putExtra("uid", managerWaitingDTOArrayList.get(position).getUidInt());

        startActivity(intent);


        //finish();


    }



    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Log.e(TAG, "=== onBackPressed ===" );

        //메인 엑티비티가 왜 2번 호출 되는 것인가...
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);

        /* 엑티비티 모두 정리 위함 */
        intent.addFlags(FLAG_ACTIVITY_NO_HISTORY);
        startActivity(intent);

        //finish();
    }

    @Override
    public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
        Log.d(TAG, "=== onDateSelected date ===" +date);
        Log.d(TAG, "=== onDateSelected selected ===" +selected);

        makeStringRequestGet2(date);

    }

    @Override
    public void onMonthChanged(MaterialCalendarView widget, CalendarDay date) {
        Log.d(TAG, "=== onMonthChanged date ===" );
    }

    private class TodayDecorator implements DayViewDecorator {

        private final CalendarDay today;
        private final Drawable backgroundDrawable;

        public TodayDecorator() {
            today = CalendarDay.today();
            backgroundDrawable = getResources().getDrawable(R.drawable.today_circle_background);
        }

        @Override
        public boolean shouldDecorate(CalendarDay day) {
            return today.equals(day);
        }

        @Override
        public void decorate(DayViewFacade view) {
            view.setBackgroundDrawable(backgroundDrawable);
        }
    }
}