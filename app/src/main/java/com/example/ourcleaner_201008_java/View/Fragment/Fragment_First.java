package com.example.ourcleaner_201008_java.View.Fragment;

import android.content.Context;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.example.ourcleaner_201008_java.Dialog.ExampleBottomSheetDialog;
import com.example.ourcleaner_201008_java.Dialog.NumberPickerDialog;
import com.example.ourcleaner_201008_java.Ffament_First_Handler;
import com.example.ourcleaner_201008_java.R;

import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class Fragment_First extends Fragment implements NumberPicker.OnValueChangeListener,ExampleBottomSheetDialog.BottomSheetListener {

    ViewPager viewPager;
    private static final String TAG = "매니저디테일정보 예약탭";
    TextView feeGuideTxt;

    public TextView needDefTimeTxt;

    Integer getNeedDefCost, getNeedDefTime;

    CalendarView calendar_view;

    /* 캘린더 관련 변수 */
    private long currentMinLong, currentMaxLong;

    String dateStr, dayStr;

    public static Context context;

    Ffament_First_Handler ffament_first_handler = new Ffament_First_Handler(this);

    public Fragment_First(){

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        Log.e(TAG+" 생명 주기", "=== onCreateView ===" );


        /* fragment 관련 코드. 레이아웃 연결 */
        ViewGroup rootView = (ViewGroup)inflater.inflate(R.layout.fragment_first, container, false);
//        View view = inflater.inflate(R.layout.fragment_first, container, false);

        feeGuideTxt= (TextView)rootView.findViewById(R.id.feeGuideTxt);
        needDefTimeTxt= (TextView)rootView.findViewById(R.id.needDefTimeTxt);

        /*텍스트 뷰 밑줄 긋기*/
        feeGuideTxt.setPaintFlags((feeGuideTxt.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG));
        needDefTimeTxt.setText(timeIntToHourMin(getNeedDefTime)+" (추천)");


        context = getContext();

        calendar_view = rootView.findViewById(R.id.calendar_view);
        settingdateCalendar();

        calendar_view.setOnDateChangeListener(new CalendarView.OnDateChangeListener(){
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth){

                Log.d(TAG, "=== 방문 날짜에서 날짜 클릭 시 ==="+String.format("%d / %d / %d",year,month+1,dayOfMonth));

                //월.일
                dateStr = String.format("%d.%d",month+1,dayOfMonth);
                Log.e(TAG, "=== dateStr ===" +dateStr);

                dayStr = getDayOfweek(String.format("%d%d%d",year,month+1,dayOfMonth)); //요일 가져오기 위한 형식
                Log.e(TAG, "=== dayStr ===" + dayStr);
            }
        });


        needDefTimeTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "=== needDefTimeTxt 클릭 ===" );
//                NumberPickerDialog numberPickerDialog = new NumberPickerDialog();
//                numberPickerDialog.show(getParentFragmentManager(), "exampleBottomSheet");

//                ExampleBottomSheetDialog bottomSheet = new ExampleBottomSheetDialog(getActivity());
                ExampleBottomSheetDialog bottomSheet = new ExampleBottomSheetDialog(ffament_first_handler);
//                bottomSheet.show(getSupportFragmentManager(), "exampleBottomSheet");

                Bundle bundle = new Bundle();
                bundle.putInt("getNeedDefTime", getNeedDefTime);
                bottomSheet.setArguments(bundle);

                bottomSheet.show(getParentFragmentManager(), "exampleBottomSheet");
            }
        });

        feeGuideTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "=== feeGuideTxt 클릭 ===" );



            }
        });

        return rootView;
    }


    //onCreate에서 데이터 넣기
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG+" 생명 주기", "onCreate()");

        Bundle extra = this.getArguments();
        if(extra != null) {
            extra = getArguments();
            getNeedDefCost = extra.getInt("getNeedDefCost");
            getNeedDefTime = extra.getInt("getNeedDefTime");

            Log.d(TAG, "=== getNeedDefCost ==="+getNeedDefCost);
            Log.d(TAG, "=== getNeedDefTime ==="+getNeedDefTime);
        }else{
            Log.d(TAG, "=== getNeedDefCost 비어있음 ===");
        }

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


    //Dialog를 시작하는 함수, 특정 버튼을 눌렀을 때 이 함수를 실행 시키면 된다
    public void showNumberPicker(View view, String title, String subtitle, int maxvalue, int minvalue, int step, int defvalue){
        NumberPickerDialog newFragment = new NumberPickerDialog();

        //Dialog에는 bundle을 이용해서 파라미터를 전달한다
        Bundle bundle = new Bundle(6); // 파라미터는 전달할 데이터 개수
        bundle.putString("title", title); // key , value
        bundle.putString("subtitle", subtitle); // key , value
        bundle.putInt("maxvalue", maxvalue); // key , value
        bundle.putInt("minvalue", minvalue); // key , value
        bundle.putInt("step", step); // key , value
        bundle.putInt("defvalue", defvalue); // key , value
        newFragment.setArguments(bundle);
        //class 자신을 Listener로 설정한다
        newFragment.setValueChangeListener(this);
        newFragment.show(getParentFragmentManager(), "number picker");
    }


    //valueChangeListener.onValueChange가 수행되었을 때 수행될 함수
    @Override
    public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
        //int setting_value = getActivity().sensor_minimum_distance + numberPicker.getValue()*getActivity().distance_setting_step;

        //원하는 동작 수행
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
        Log.d(TAG, "=== 자바 날짜 오늘 14일 후 today 밀리세컨드로 ===" + currentMaxLong);

        //dateCalendar.setMaxDate(1570668133353L);
        calendar_view.setMinDate(currentMinLong);
        calendar_view.setMaxDate(currentMaxLong);
        Log.d(TAG, "=== 자바 날짜 예약날짜 범위 지정 완료 ===");

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
            System.out.println(date + "는 " + week[w] +"요일 입니다");

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

    @Override
    public void onButtonClicked(int getNeedDefTime) {
        Log.d(TAG, "=== onButtonClicked ===" +getNeedDefTime);
    }
}
