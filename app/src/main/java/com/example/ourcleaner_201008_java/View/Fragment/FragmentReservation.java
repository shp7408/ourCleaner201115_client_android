package com.example.ourcleaner_201008_java.View.Fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.ourcleaner_201008_java.Adapter.MyReservationAdapter;
import com.example.ourcleaner_201008_java.DTO.MyReservationDTO;
import com.example.ourcleaner_201008_java.GlobalApplication;
import com.example.ourcleaner_201008_java.R;
import com.example.ourcleaner_201008_java.View.MainActivity;
import com.example.ourcleaner_201008_java.View.Service_DetailActivity;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.threeten.bp.DayOfWeek;

import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.TimeUnit;


public class FragmentReservation extends Fragment
        implements MyReservationAdapter.OnListItemSelectedInterface,
        OnDateSelectedListener {

    private static final String TAG = "나의예약";

    /*프래그먼트에서 메인엑티비티 컨텍스트 필요한 경우*/
    MainActivity mainActivity;

    private RecyclerView mRecyclerView;
//    private RecyclerView.Adapter adapter;
//    private RecyclerView.LayoutManager layoutManager;
    private  MyReservationAdapter adapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private ArrayList<MyReservationDTO> myReservationDTOArrayList = new ArrayList<>();

    /* 서버에서 내 장소 정보 받아오기 위한 변수 */
    String jsonResponse;
    // 서버에서 받아온 데이터를 담는 객체
    MyReservationDTO myReservationDTO;

    int resultNeedTime=0;


    /* material 달력 */
    MaterialCalendarView materialCalendarView;



    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        return inflater.inflate(R.layout.fragment_myreservation, container, false);
        Log.e(TAG+" 생명 주기", "=== onCreateView ===" );

        /* fragment 관련 코드. 레이아웃 연결 */
        ViewGroup rootView = (ViewGroup)inflater.inflate(R.layout.fragment_myreservation, container, false);



        /* 리사이클러 뷰 관련 코드. 리사이클러 뷰 와 연결 */
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.myreservation_recycle);

        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.scrollToPosition(0);

        adapter = new MyReservationAdapter(getActivity(),myReservationDTOArrayList, this);
        mRecyclerView.setAdapter(adapter);


        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        // 기본 구분선 추가
        DividerItemDecoration dividerItemDecoration =
                new DividerItemDecoration(mRecyclerView.getContext(),new LinearLayoutManager(mainActivity).getOrientation());
        mRecyclerView.addItemDecoration(dividerItemDecoration);


        mRecyclerView.setNestedScrollingEnabled(true);




        /* material 달력 */
        materialCalendarView = rootView.findViewById(R.id.materialCalendarView);

        materialCalendarView.setOnDateChangedListener(this);
        materialCalendarView.addDecorator(new TodayDecorator());

        //CalendarDay maxdate = CalendarDay.from(CalendarDay.today().getYear(), CalendarDay.today().getMonth(), CalendarDay.today().getDay()+14);

        materialCalendarView.setCurrentDate(CalendarDay.today());
        materialCalendarView.setSelectedDate(CalendarDay.today());
        materialCalendarView.state().edit()
                .setFirstDayOfWeek(DayOfWeek.SUNDAY)
                //.setMaximumDate(maxdate)
                .commit();

//        OneDayDecorator oneDayDecorator = new OneDayDecorator();

//        materialCalendarView.addDecorators(
//                new MySelectorDecorator(getActivity()),
//                oneDayDecorator
//        );

        return rootView;
    }

    //onCreate에서 데이터 넣기
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG+" 생명 주기", "onCreate()");

        // 1. 서버에서 리사이클러 뷰에 뿌릴 데이터 가져오기
        // 2. 가져온 데이터를 리사이클러 뷰에 넣을 list에 넣기
        // 3. list를 리사이클러 뷰에 넣기...

        //for Test
        Log.d(TAG, "=== prepareData() ===" );
        prepareData();
    }

    //데이터 준비(최종적으로는 동적으로 추가하거나 삭제할 수 있어야 한다. 이 데이터를 어디에 저장할지 정해야 한다.)
    private void prepareData() {
        Log.d(TAG, "=== prepareData() ===" );
        myReservationDTOArrayList = new ArrayList<>();

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



        String url = "http://52.79.179.66/myReservationList.php?currentUser="+ GlobalApplication.currentUser+"&date="+resultDate;
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
                            myReservationDTOArrayList.clear();
                            final int numberOfItemsInResp = response.length();

                            for (int i = 0; i < numberOfItemsInResp ; i++) {
                                Log.e(TAG, "=== jsonResponse 반복문 이후 ===");
                                Log.e(TAG, "response.length() 전체 반복문 횟수: "+ numberOfItemsInResp);
                                Log.e(TAG, "현재 반복문 시작 번호 :" + i);

                                JSONObject myReservation = (JSONObject) response.get(i);

                                int uid = myReservation.getInt("uid");
                                String currentUser = myReservation.getString("currentUser");
                                String serviceState = myReservation.getString("serviceState");
                                String visitDate = myReservation.getString("visitDate");
                                String visitDay = myReservation.getString("visitDay");

                                int startTime = myReservation.getInt("startTime");
                                int needDefTime = myReservation.getInt("needDefTime");

                                String serviceplus = myReservation.getString("serviceplus");
                                String myplaceDTO_placeName = myReservation.getString("myplaceDTO_placeName");

                                if(serviceplus.contains("냉장고=true")){
                                    resultNeedTime = needDefTime + 120;
                                    needDefTime = resultNeedTime;
                                }
                                if(serviceplus.contains("다림질=true")){
                                    resultNeedTime = needDefTime + 30;
                                }

                                jsonResponse += "uid: " + uid + "\n\n";
                                jsonResponse += "currentUser: " + currentUser + "\n\n";
                                jsonResponse += "serviceState: " + serviceState + "\n\n";

                                jsonResponse += "visitDate: " + visitDate + "\n\n";

                                jsonResponse += "visitDay: " + visitDay + "\n\n";
                                jsonResponse += "startTime: " + startTime + "\n\n";
                                jsonResponse += "needDefTime: " + needDefTime + "\n\n";

                                jsonResponse += "serviceplus: " + serviceplus + "\n\n";
                                jsonResponse += "myplaceDTO_placeName: " + myplaceDTO_placeName + "\n\n";

//                                myReservationDTO = new MyReservationDTO(uid, currentUser, serviceState, visitDate+"("+visitDay+")", serviceState, myplaceDTO_placeName+" / "+timeIntToHourMin(startTime)+" ~ "+timeIntToHourMin(startTime+resultNeedTime)+"("+timeIntToHourMin2(resultNeedTime)+")");
                                myReservationDTO = new MyReservationDTO(uid, currentUser, serviceState, visitDate+"("+visitDay+")", myplaceDTO_placeName, timeIntToHourMin(startTime));
                                myReservationDTOArrayList.add(myReservationDTO);

                                //myReservationDTOArrayList.add(new MyReservationDTO("df", "dd", "dd", "df", "df", "fds"));
                                Log.d(TAG, "=== myReservationDTOArrayList 에 데이터 넣음 ===" + myReservationDTOArrayList.get(i).getServiceDate());

                                //myReservationDTOArrayList.add(new MyReservationDTO("df", "dd","dd", "df", "df", "fds"));


                            }

                            Log.e(TAG, "=== 객체 정렬 ===" );
                            //객체 정렬 위함
                            Collections.sort(myReservationDTOArrayList);

                            //리사이클러 뷰 계속 size 0뜨는 문제 해결 위함
                            adapter.notifyDataSetChanged();

                            Log.d(TAG, "=== jsonResponse 반복문 이후 ===");

                        } catch (JSONException e) {
                            e.printStackTrace();

                            Log.e(TAG, "=== 생명 === Error " + e );
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

    //날짜 클릭했을 때, 리사이클러뷰 다시 보여주는 부분
    private void prepareData2(CalendarDay date) {



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



        String url = "http://52.79.179.66/myReservationList.php?currentUser="+
                GlobalApplication.currentUser+"&date="+resultDate;
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
//                            ArrayList<MyReservationDTO> arrayList = new ArrayList<>();

                            myReservationDTOArrayList.clear();
                            final int numberOfItemsInResp = response.length();

                            for (int i = 0; i < numberOfItemsInResp ; i++) {
                                Log.e(TAG, "=== jsonResponse 반복문 이후 ===");
                                Log.e(TAG, "response.length() 전체 반복문 횟수: "+ numberOfItemsInResp);
                                Log.e(TAG, "현재 반복문 시작 번호 :" + i);

                                JSONObject myReservation = (JSONObject) response.get(i);

                                int uid = myReservation.getInt("uid");
                                String currentUser = myReservation.getString("currentUser");
                                String serviceState = myReservation.getString("serviceState");
                                String visitDate = myReservation.getString("visitDate");
                                String visitDay = myReservation.getString("visitDay");

                                int startTime = myReservation.getInt("startTime");
                                int needDefTime = myReservation.getInt("needDefTime");

                                String serviceplus = myReservation.getString("serviceplus");
                                String myplaceDTO_placeName = myReservation.getString("myplaceDTO_placeName");

                                if(serviceplus.contains("냉장고=true")){
                                    resultNeedTime = needDefTime + 120;
                                    needDefTime = resultNeedTime;
                                }
                                if(serviceplus.contains("다림질=true")){
                                    resultNeedTime = needDefTime + 30;
                                }

                                jsonResponse += "uid: " + uid + "\n\n";
                                jsonResponse += "currentUser: " + currentUser + "\n\n";
                                jsonResponse += "serviceState: " + serviceState + "\n\n";

                                jsonResponse += "visitDate: " + visitDate + "\n\n";

                                jsonResponse += "visitDay: " + visitDay + "\n\n";
                                jsonResponse += "startTime: " + startTime + "\n\n";
                                jsonResponse += "needDefTime: " + needDefTime + "\n\n";

                                jsonResponse += "serviceplus: " + serviceplus + "\n\n";
                                jsonResponse += "myplaceDTO_placeName: " + myplaceDTO_placeName + "\n\n";

                                myReservationDTO = new MyReservationDTO(uid, currentUser, serviceState, visitDate+"("+visitDay+")", myplaceDTO_placeName, timeIntToHourMin(startTime));
                                myReservationDTOArrayList.add(myReservationDTO);

                                Log.d(TAG, "=== myReservationDTOArrayList 에 데이터 넣음 ===" + myReservationDTOArrayList.get(i).getServiceDate());


                            }


//                            Log.d(TAG, "=== myReservationDTOArrayList 에 데이터 넣음 ===" + myReservationDTOArrayList.size());
//
//                            Log.e(TAG, "=== 객체 정렬 ===" );
//                            //객체 정렬 위함
//                            Collections.sort(myReservationDTOArrayList);
//
//                            //리사이클러 뷰 계속 size 0뜨는 문제 해결 위함
//                            myReservationAdapter.notifyDataSetChanged();
//
//                            Log.d(TAG, "=== jsonResponse 반복문 이후 ===");





                            adapter = new MyReservationAdapter(getActivity(),myReservationDTOArrayList, FragmentReservation.this::onItemSelected);
                            mRecyclerView.setAdapter(adapter);



                            Collections.sort(myReservationDTOArrayList);

                            //리사이클러 뷰 계속 size 0뜨는 문제 해결 위함
                            adapter.notifyDataSetChanged();


















                        } catch (JSONException e) {
                            e.printStackTrace();

                            Log.e(TAG, "=== 생명 === Error " + e );
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


    //int 형태의 정수를 "3시간 30분" String으로 나타내는 메서드
    public String timeIntToHourMin(int plusTimeInt){

        long hour = TimeUnit.MINUTES.toHours(plusTimeInt); // 분을 시간으로 변경
        Log.d(TAG, "=== hour ===" +hour);

        long minutes = TimeUnit.MINUTES.toMinutes(plusTimeInt) - TimeUnit.HOURS.toMinutes(hour); // 시간으로 변경하고, 나머지 분
        Log.d(TAG, "=== minutes ==="+minutes );

        String plusTimeStr;

        if(hour==0){
            Log.d(TAG, "=== hour==0  ===" );
            plusTimeStr = ""+ minutes + "분";
            Log.d(TAG, "=== plusTimeStr ===" +plusTimeStr);
        }else if(minutes==0){
            Log.d(TAG, "=== minutes==0 ===" );
            plusTimeStr = ""+ hour + "시";
            Log.d(TAG, "=== plusTimeStr ===" +plusTimeStr);
        }else{
            Log.d(TAG, "=== hour 랑 minutes 둘 다 0이 아닌, 경우 ===" );
            plusTimeStr = ""+ hour +"시 "+ minutes + "분";
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

        String plusTimeStr;

        if(hour==0){
            Log.d(TAG, "=== hour==0  ===" );
            plusTimeStr = ""+ minutes + "분";
            Log.d(TAG, "=== plusTimeStr ===" +plusTimeStr);
        }else if(minutes==0){
            Log.d(TAG, "=== minutes==0 ===" );
            plusTimeStr = ""+ hour + "시간";
            Log.d(TAG, "=== plusTimeStr ===" +plusTimeStr);
        }else{
            Log.d(TAG, "=== hour 랑 minutes 둘 다 0이 아닌, 경우 ===" );
            plusTimeStr = ""+ hour +"시간 "+ minutes + "분";
            Log.d(TAG, "=== plusTimeStr ===" +plusTimeStr);
        }

        return plusTimeStr;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.d(TAG+" 생명 주기", "========================= onAttach()");

        // 1. 서버에서 리사이클러 뷰에 뿌릴 데이터 가져오기
        // 2. 가져온 데이터를 리사이클러 뷰에 넣을 list에 넣기
        // 3. list를 리사이클러 뷰에 넣기...
//        prepareData();

    }

    @Override
    public void onItemSelected(View v, int position) {
        Log.e(TAG, "=== position ===" +position);

        //있으면 넘어감
        Intent intent = new Intent(getActivity(), Service_DetailActivity.class);
        intent.putExtra("uid", myReservationDTOArrayList.get(position).getUid());

        startActivity(intent);
    }

    @Override
    public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
        Log.d(TAG, "=== onDateSelected date ===" +date );
        Log.d(TAG, "=== onDateSelected selected ===" +selected );

        prepareData2(date);

    }

//    @Override
//    public void onViewCreated(View view, Bundle savedInstanceState) {
//        Log.d(TAG+" 생명 주기", "onViewCreated()");
//        super.onViewCreated(view, savedInstanceState);
//    }
//
//    @Override
//    public void onActivityCreated(Bundle savedInstanceState) {
//        Log.d(TAG+" 생명 주기", "onActivityCreated()");
//        super.onActivityCreated(savedInstanceState);
////        prepareData();
//    }
//
//    @Override
//    public void onStart() {
//        Log.d(TAG+" 생명 주기", "onStart()");
//        super.onStart();
//    }
//
//    @Override
//    public void onResume() {
//        Log.d(TAG+" 생명 주기", "onResume()");
//        super.onResume();
//    }
//
//    @Override
//    public void onPause() {
//        Log.d(TAG+" 생명 주기", "onPause()");
//        super.onPause();
//    }
//
//    @Override
//    public void onStop() {
//        Log.d(TAG+" 생명 주기", "onStop()");
//        super.onStop();
//    }
//
//    @Override
//    public void onDestroyView() {
//        Log.d(TAG+" 생명 주기", "onDestroyView()");
//        super.onDestroyView();
//    }
//
//    @Override
//    public void onDestroy() {
//        Log.d(TAG+" 생명 주기", "onDestroy()");
//        super.onDestroy();
//    }
//
//    @Override
//    public void onDetach() {
//        Log.d(TAG+" 생명 주기", "onDetach()");
//        super.onDetach();
//    }
//
//    @Override
//    public void onSaveInstanceState(Bundle outState) {
//        Log.d(TAG+" 생명 주기", "onSaveInstanceState()");
//        super.onSaveInstanceState(outState);
//    }


}
