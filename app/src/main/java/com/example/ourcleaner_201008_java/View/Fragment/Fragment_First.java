package com.example.ourcleaner_201008_java.View.Fragment;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.example.ourcleaner_201008_java.Dialog.ExampleBottomSheetDialog;
import com.example.ourcleaner_201008_java.Ffament_First_Handler;
import com.example.ourcleaner_201008_java.R;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import org.threeten.bp.DayOfWeek;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;



public class Fragment_First extends Fragment implements OnDateSelectedListener{

    ViewPager viewPager;
    private static final String TAG = "매니저디테일정보 예약탭";
    TextView feeGuideTxt;

    public TextView needDefTimeTxt;
    Integer getNeedDefCost, getNeedDefTime, getSizeIndexint;
    String getSizeStr,nowTimeStr;

//    CalendarView calendar_view;

    MaterialCalendarView materialCalendarView;


    private OnDateCickListener onDateCickListener;
    private OnStartTimeClickListener onStartTimeClickListener;


    /* 캘린더 관련 변수 */
    private long currentMinLong, currentMaxLong;
    String dateStr, dayStr;

    public static Context context;
    Ffament_First_Handler ffament_first_handler = new Ffament_First_Handler(this);

    /* 시간 선택 변수 */
    HorizontalScrollView scrollview;
    Button timeBtn7, timeBtn8, timeBtn9, timeBtn10, timeBtn11, timeBtn12, timeBtn13, timeBtn14, timeBtn15,
            timeBtn16, timeBtn17, timeBtn18, timeBtn19;
    public LinearLayout timeLayout;

    int startTimeInt;

    public Fragment_First(){
    }

    /* material 캘린더뷰 리스너 */
    @Override
    public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
        Log.d(TAG, "=== onDateSelected date ===" +date);

        String dateStr = date.toString();
        String year = dateStr.substring(12,16);
        Log.d(TAG, "=== onDateSelected year ===" +year);

        //월 일 붙어있는거
        String monthDayAll = dateStr.substring(17);
        Log.d(TAG, "=== onDateSelected monthDayAll ===" +monthDayAll);

        //월
        String month = monthDayAll.substring(0, monthDayAll.indexOf("-"));
        Log.d(TAG, "=== onDateSelected month ===" +month);

        //일
        String dayOfMonth = monthDayAll.substring(monthDayAll.lastIndexOf("-")+1, monthDayAll.length()-1);
        Log.d(TAG, "=== onDateSelected dayOfMonth ===" +dayOfMonth);

        int yearInt = Integer.parseInt(year); //2021
        int monthInt = Integer.parseInt(month); //1
        int dayOfMonthInt = Integer.parseInt(dayOfMonth); //6

        Log.d(TAG, "=== onDateSelected ==="+String.format("%d / %d / %d",yearInt,monthInt,dayOfMonthInt));

        //월.일
        String formatdateStr = String.format("%02d.%02d",monthInt,dayOfMonthInt);
        Log.e(TAG, "=== formatdateStr ===" +formatdateStr);

        String formatdayStr = getDayOfweek(String.format("%d%02d%02d",yearInt,monthInt,dayOfMonthInt)); //요일 가져오기 위한 형식
        Log.e(TAG, "=== onDateSelected formatdayStr ===" + formatdayStr);

        /* 리스너에 날짜 요일 데이터 넣는 곳 */
        onDateCickListener.onDateSetFinish(formatdateStr, formatdayStr);

        timeLayout.setVisibility(View.VISIBLE);
    }

    public interface OnDateCickListener{
        void onDateSetFinish(String date, String day);
    }

    public interface OnStartTimeClickListener{
        void onStartTimeFinish(int startTime); //7시 -> 420
    }

    public void onDateSetFinish(String date, String day){
        Log.d(TAG, "=== onDateSetFinish() ===" );

        dateStr=date;
        dayStr=day;
    }

    public void onStartTimeFinish(int startTime){
        startTimeInt=startTime;
    }

    /* 오늘 날짜 표시하는 부분 */
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        Log.e(TAG+" 생명 주기", "=== onCreateView ===" );

        /* fragment 관련 코드. 레이아웃 연결 */
        ViewGroup rootView = (ViewGroup)inflater.inflate(R.layout.fragment_first, container, false);
//        View view = inflater.inflate(R.layout.fragment_first, container, false);

        feeGuideTxt= (TextView)rootView.findViewById(R.id.feeGuideTxt);
        needDefTimeTxt= (TextView)rootView.findViewById(R.id.needDefTimeTxt);

        /* 텍스트 뷰 밑줄 긋기 */
        feeGuideTxt.setPaintFlags((feeGuideTxt.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG));

        /* 진행시간 */
        needDefTimeTxt.setText(timeIntToHourMin(getNeedDefTime)+" (추천)");

        context = getContext();

//        calendar_view = rootView.findViewById(R.id.calendar_view);
//        settingdateCalendar();

        materialCalendarView = rootView.findViewById(R.id.materialCalendarView);

        scrollview= rootView.findViewById(R.id.scrollview);
        timeLayout= rootView.findViewById(R.id.timeLayout);

        timeBtn7 = rootView.findViewById(R.id.timeBtn7);
        timeBtn8 = rootView.findViewById(R.id.timeBtn8);
        timeBtn9 = rootView.findViewById(R.id.timeBtn9);
        timeBtn10 = rootView.findViewById(R.id.timeBtn10);
        timeBtn11 = rootView.findViewById(R.id.timeBtn11);
        timeBtn12 = rootView.findViewById(R.id.timeBtn12);
        timeBtn13 = rootView.findViewById(R.id.timeBtn13);
        timeBtn14 = rootView.findViewById(R.id.timeBtn14);
        timeBtn15 = rootView.findViewById(R.id.timeBtn15);
        timeBtn16 = rootView.findViewById(R.id.timeBtn16);
        timeBtn17 = rootView.findViewById(R.id.timeBtn17);
        timeBtn18 = rootView.findViewById(R.id.timeBtn18);
        timeBtn19 = rootView.findViewById(R.id.timeBtn19);


        Button.OnClickListener onClickListener = new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.timeBtn7:
                        timeBtn7.setSelected(true);
                        timeBtn8.setSelected(false);
                        timeBtn9.setSelected(false);
                        timeBtn10.setSelected(false);
                        timeBtn11.setSelected(false);
                        timeBtn12.setSelected(false);
                        timeBtn13.setSelected(false);
                        timeBtn14.setSelected(false);
                        timeBtn15.setSelected(false);
                        timeBtn16.setSelected(false);
                        timeBtn17.setSelected(false);
                        timeBtn18.setSelected(false);
                        timeBtn19.setSelected(false);

                        onStartTimeClickListener.onStartTimeFinish(420);
                        timeBtn7.setTextColor(getResources().getColor(R.color.white));
                        break;
                    case R.id.timeBtn8:
                        timeBtn7.setSelected(false);
                        timeBtn8.setSelected(true);
                        timeBtn9.setSelected(false);
                        timeBtn10.setSelected(false);
                        timeBtn11.setSelected(false);
                        timeBtn12.setSelected(false);
                        timeBtn13.setSelected(false);
                        timeBtn14.setSelected(false);
                        timeBtn15.setSelected(false);
                        timeBtn16.setSelected(false);
                        timeBtn17.setSelected(false);
                        timeBtn18.setSelected(false);
                        timeBtn19.setSelected(false);

                        onStartTimeClickListener.onStartTimeFinish(480);
                        timeBtn8.setTextColor(getResources().getColor(R.color.white));
                        break;
                    case R.id.timeBtn9:
                        timeBtn7.setSelected(false);
                        timeBtn8.setSelected(false);
                        timeBtn9.setSelected(true);
                        timeBtn10.setSelected(false);
                        timeBtn11.setSelected(false);
                        timeBtn12.setSelected(false);
                        timeBtn13.setSelected(false);
                        timeBtn14.setSelected(false);
                        timeBtn15.setSelected(false);
                        timeBtn16.setSelected(false);
                        timeBtn17.setSelected(false);
                        timeBtn18.setSelected(false);
                        timeBtn19.setSelected(false);

                        onStartTimeClickListener.onStartTimeFinish(540);
                        timeBtn9.setTextColor(getResources().getColor(R.color.white));
                        break;
                    case R.id.timeBtn10:
                        timeBtn7.setSelected(false);
                        timeBtn8.setSelected(false);
                        timeBtn9.setSelected(false);
                        timeBtn10.setSelected(true);
                        timeBtn11.setSelected(false);
                        timeBtn12.setSelected(false);
                        timeBtn13.setSelected(false);
                        timeBtn14.setSelected(false);
                        timeBtn15.setSelected(false);
                        timeBtn16.setSelected(false);
                        timeBtn17.setSelected(false);
                        timeBtn18.setSelected(false);
                        timeBtn19.setSelected(false);

                        onStartTimeClickListener.onStartTimeFinish(600);
                        timeBtn10.setTextColor(getResources().getColor(R.color.white));
                        break;
                    case R.id.timeBtn11:
                        timeBtn7.setSelected(false);
                        timeBtn8.setSelected(false);
                        timeBtn9.setSelected(false);
                        timeBtn10.setSelected(false);
                        timeBtn11.setSelected(true);
                        timeBtn12.setSelected(false);
                        timeBtn13.setSelected(false);
                        timeBtn14.setSelected(false);
                        timeBtn15.setSelected(false);
                        timeBtn16.setSelected(false);
                        timeBtn17.setSelected(false);
                        timeBtn18.setSelected(false);
                        timeBtn19.setSelected(false);

                        onStartTimeClickListener.onStartTimeFinish(660);
                        timeBtn11.setTextColor(getResources().getColor(R.color.white));
                        break;
                    case R.id.timeBtn12:
                        timeBtn7.setSelected(false);
                        timeBtn8.setSelected(false);
                        timeBtn9.setSelected(false);
                        timeBtn10.setSelected(false);
                        timeBtn11.setSelected(false);
                        timeBtn12.setSelected(true);
                        timeBtn13.setSelected(false);
                        timeBtn14.setSelected(false);
                        timeBtn15.setSelected(false);
                        timeBtn16.setSelected(false);
                        timeBtn17.setSelected(false);
                        timeBtn18.setSelected(false);
                        timeBtn19.setSelected(false);

                        onStartTimeClickListener.onStartTimeFinish(720);
                        timeBtn12.setTextColor(getResources().getColor(R.color.white));
                        break;
                    case R.id.timeBtn13:
                        timeBtn7.setSelected(false);
                        timeBtn8.setSelected(false);
                        timeBtn9.setSelected(false);
                        timeBtn10.setSelected(false);
                        timeBtn11.setSelected(false);
                        timeBtn12.setSelected(false);
                        timeBtn13.setSelected(true);
                        timeBtn14.setSelected(false);
                        timeBtn15.setSelected(false);
                        timeBtn16.setSelected(false);
                        timeBtn17.setSelected(false);
                        timeBtn18.setSelected(false);
                        timeBtn19.setSelected(false);

                        onStartTimeClickListener.onStartTimeFinish(780);
                        timeBtn13.setTextColor(getResources().getColor(R.color.white));
                        break;
                    case R.id.timeBtn14:
                        timeBtn7.setSelected(false);
                        timeBtn8.setSelected(false);
                        timeBtn9.setSelected(false);
                        timeBtn10.setSelected(false);
                        timeBtn11.setSelected(false);
                        timeBtn12.setSelected(false);
                        timeBtn13.setSelected(false);
                        timeBtn14.setSelected(true);
                        timeBtn15.setSelected(false);
                        timeBtn16.setSelected(false);
                        timeBtn17.setSelected(false);
                        timeBtn18.setSelected(false);
                        timeBtn19.setSelected(false);

                        onStartTimeClickListener.onStartTimeFinish(840);
                        timeBtn14.setTextColor(getResources().getColor(R.color.white));
                        break;
                    case R.id.timeBtn15:
                        timeBtn7.setSelected(false);
                        timeBtn8.setSelected(false);
                        timeBtn9.setSelected(false);
                        timeBtn10.setSelected(false);
                        timeBtn11.setSelected(false);
                        timeBtn12.setSelected(false);
                        timeBtn13.setSelected(false);
                        timeBtn14.setSelected(false);
                        timeBtn15.setSelected(true);
                        timeBtn16.setSelected(false);
                        timeBtn17.setSelected(false);
                        timeBtn18.setSelected(false);
                        timeBtn19.setSelected(false);

                        onStartTimeClickListener.onStartTimeFinish(900);
                        timeBtn15.setTextColor(getResources().getColor(R.color.white));
                        break;
                    case R.id.timeBtn16:
                        timeBtn7.setSelected(false);
                        timeBtn8.setSelected(false);
                        timeBtn9.setSelected(false);
                        timeBtn10.setSelected(false);
                        timeBtn11.setSelected(false);
                        timeBtn12.setSelected(false);
                        timeBtn13.setSelected(false);
                        timeBtn14.setSelected(false);
                        timeBtn15.setSelected(false);
                        timeBtn16.setSelected(true);
                        timeBtn17.setSelected(false);
                        timeBtn18.setSelected(false);
                        timeBtn19.setSelected(false);

                        onStartTimeClickListener.onStartTimeFinish(960);
                        timeBtn16.setTextColor(getResources().getColor(R.color.white));
                        break;
                    case R.id.timeBtn17:
                        timeBtn7.setSelected(false);
                        timeBtn8.setSelected(false);
                        timeBtn9.setSelected(false);
                        timeBtn10.setSelected(false);
                        timeBtn11.setSelected(false);
                        timeBtn12.setSelected(false);
                        timeBtn13.setSelected(false);
                        timeBtn14.setSelected(false);
                        timeBtn15.setSelected(false);
                        timeBtn16.setSelected(false);
                        timeBtn17.setSelected(true);
                        timeBtn18.setSelected(false);
                        timeBtn19.setSelected(false);

                        onStartTimeClickListener.onStartTimeFinish(1020);
                        timeBtn17.setTextColor(getResources().getColor(R.color.white));
                        break;
                    case R.id.timeBtn18:
                        timeBtn7.setSelected(false);
                        timeBtn8.setSelected(false);
                        timeBtn9.setSelected(false);
                        timeBtn10.setSelected(false);
                        timeBtn11.setSelected(false);
                        timeBtn12.setSelected(false);
                        timeBtn13.setSelected(false);
                        timeBtn14.setSelected(false);
                        timeBtn15.setSelected(false);
                        timeBtn16.setSelected(false);
                        timeBtn17.setSelected(false);
                        timeBtn18.setSelected(true);
                        timeBtn19.setSelected(false);

                        onStartTimeClickListener.onStartTimeFinish(1080);
                        timeBtn18.setTextColor(getResources().getColor(R.color.white));
                        break;
                    case R.id.timeBtn19:
                        timeBtn7.setSelected(false);
                        timeBtn8.setSelected(false);
                        timeBtn9.setSelected(false);
                        timeBtn10.setSelected(false);
                        timeBtn11.setSelected(false);
                        timeBtn12.setSelected(false);
                        timeBtn13.setSelected(false);
                        timeBtn14.setSelected(false);
                        timeBtn15.setSelected(false);
                        timeBtn16.setSelected(false);
                        timeBtn17.setSelected(false);
                        timeBtn18.setSelected(false);
                        timeBtn19.setSelected(true);
                        onStartTimeClickListener.onStartTimeFinish(1140);
                        timeBtn19.setTextColor(getResources().getColor(R.color.white));
                        break;
                }
            }
        };

        timeBtn7.setOnClickListener(onClickListener);
        timeBtn8.setOnClickListener(onClickListener);
        timeBtn9.setOnClickListener(onClickListener);
        timeBtn10.setOnClickListener(onClickListener);
        timeBtn11.setOnClickListener(onClickListener);
        timeBtn12.setOnClickListener(onClickListener);
        timeBtn13.setOnClickListener(onClickListener);
        timeBtn14.setOnClickListener(onClickListener);
        timeBtn15.setOnClickListener(onClickListener);
        timeBtn16.setOnClickListener(onClickListener);
        timeBtn17.setOnClickListener(onClickListener);
        timeBtn18.setOnClickListener(onClickListener);
        timeBtn19.setOnClickListener(onClickListener);

//        calendar_view.setOnDateChangeListener(new CalendarView.OnDateChangeListener(){
//            @Override
//            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth){
//
//                Log.d(TAG, "=== 방문 날짜에서 날짜 클릭 시 ==="+String.format("%d / %d / %d",year,month+1,dayOfMonth));
//
//                //월.일
//                dateStr = String.format("%02d.%02d",month+1,dayOfMonth);
//                Log.e(TAG, "=== dateStr ===" +dateStr);
//
//                dayStr = getDayOfweek(String.format("%d%02d%02d",year,month+1,dayOfMonth)); //요일 가져오기 위한 형식
//                Log.e(TAG, "=== dayStr ===" + dayStr);
//
//                /* 리스너에 날짜 요일 데이터 넣는 곳 */
//                onDateCickListener.onDateSetFinish(dateStr, dayStr);
//
//                timeLayout.setVisibility(View.VISIBLE);
//            }
//        });

        materialCalendarView.setOnDateChangedListener(this);

        // TODO: 2021-01-06 월 넘어가면... 바꿔야 함..ㅎㅎ + 이미 예약된 거 있는거 보여주는 것도 좋겠다..



        try{
            CalendarDay mindate = CalendarDay.from(CalendarDay.today().getYear(), CalendarDay.today().getMonth(), CalendarDay.today().getDay()+1);
            CalendarDay maxdate = CalendarDay.from(CalendarDay.today().getYear(), CalendarDay.today().getMonth(), CalendarDay.today().getDay()+14);
            /* 오늘 날짜 설정하기 -> 나중에 사용할 것 */
//        materialCalendarView.setCurrentDate(CalendarDay.today());
//        materialCalendarView.setSelectedDate(CalendarDay.today());
            materialCalendarView.state().edit()
                    .setFirstDayOfWeek(DayOfWeek.SUNDAY)
                    .setMinimumDate(mindate)
                    .setMaximumDate(maxdate)
                    .commit();

            materialCalendarView.addDecorator(new TodayDecorator());

        }catch (Exception e){
            Log.e(TAG, "=== 14일 더했을 때, 날짜가 28-31 넘어가는 경우 에러 ===" + e);
            Log.d(TAG, "=== e ===" +e.getLocalizedMessage().lastIndexOf(":"));
            Log.d(TAG, "=== e ===" +e.getLocalizedMessage().substring(55));

            Log.d(TAG, "=== e ===" + Integer.parseInt(e.getLocalizedMessage().substring(55)));

            int dayInt;

            /* 14일 더한 경우, 날짜가 많아지면... */
            if(CalendarDay.today().getMonth()==1 ||CalendarDay.today().getMonth()==3
                    ||CalendarDay.today().getMonth()==5 ||CalendarDay.today().getMonth()==7
                    || CalendarDay.today().getMonth()==8 || CalendarDay.today().getMonth()==10
                    || CalendarDay.today().getMonth()==12){
                Log.d(TAG, "=== 캘린더뷰에서 월 31일 경우의 달 1월 3월 5월 7월 8월 10월 12월 ===" );

                dayInt = Integer.parseInt(e.getLocalizedMessage().substring(55)) - 31;

            }else if(CalendarDay.today().getMonth()==2){
                Log.d(TAG, "=== 캘린더뷰에서 월 28일 경우의 달 2월 ===" );

                dayInt = Integer.parseInt(e.getLocalizedMessage().substring(55)) - 28;

            }else {
                Log.d(TAG, "=== 캘린더뷰에서 월 30일 경우의 달 ===" );
                dayInt = Integer.parseInt(e.getLocalizedMessage().substring(55)) - 30;

            }


            CalendarDay mindate = CalendarDay.from(CalendarDay.today().getYear(), CalendarDay.today().getMonth(), CalendarDay.today().getDay()+1);
            CalendarDay maxdate = CalendarDay.from(CalendarDay.today().getYear(), CalendarDay.today().getMonth()+1, dayInt);
            /* 오늘 날짜 설정하기 -> 나중에 사용할 것 */
//        materialCalendarView.setCurrentDate(CalendarDay.today());
//        materialCalendarView.setSelectedDate(CalendarDay.today());
            materialCalendarView.state().edit()
                    .setFirstDayOfWeek(DayOfWeek.SUNDAY)
                    .setMinimumDate(mindate)
                    .setMaximumDate(maxdate)
                    .commit();

            materialCalendarView.addDecorator(new TodayDecorator());
        }







        needDefTimeTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "=== needDefTimeTxt 클릭 ===" );
//                NumberPickerDialog numberPickerDialog = new NumberPickerDialog();
//                numberPickerDialog.show(getParentFragmentManager(), "exampleBottomSheet");

//                ExampleBottomSheetDialog bottomSheet = new ExampleBottomSheetDialog(getActivity());
                ExampleBottomSheetDialog bottomSheet = new ExampleBottomSheetDialog(ffament_first_handler);
//                bottomSheet.show(getSupportFragmentManager(), "exampleBottomSheet");


                nowTimeStr = needDefTimeTxt.getText().toString().substring(0,1);
                Log.d(TAG, "=== nowTimeStr ===" );

                Bundle bundle = new Bundle();
                bundle.putString("nowTimeStr", nowTimeStr); //텍스트 뷰에 있는 스트링 3/4/5 그냥 이런 숫자
                bundle.putInt("getNeedDefCost", getNeedDefCost);
                bundle.putInt("getNeedDefTime", getNeedDefTime);
                bundle.putInt("getSizeIndexint", getSizeIndexint);
                bundle.putString("getSizeStr", getSizeStr);
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
            getSizeIndexint = extra.getInt("getSizeIndexint");
            getSizeStr = extra.getString("getSizeStr");

            Log.d(TAG, "=== getNeedDefCost ===" + getNeedDefCost);
            Log.d(TAG, "=== getNeedDefTime ===" + getNeedDefTime);
            Log.d(TAG, "=== getSizeIndexint ===" + getSizeIndexint);
            Log.d(TAG, "=== getSizeStr ===" + getSizeStr);

        }else{
            Log.d(TAG, "=== getNeedDefCost 비어있음 ===");
        }

    }




    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if(context instanceof OnDateCickListener || context instanceof OnStartTimeClickListener){
            onDateCickListener = (OnDateCickListener) context;
            onStartTimeClickListener =(OnStartTimeClickListener) context;
        }else{
            throw new RuntimeException(context.toString()
                    +" must implement OnDateClickListener");
        }

    }

    @Override
    public void onDetach() {
        super.onDetach();
        onDateCickListener = null;
        onStartTimeClickListener = null;
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




//    public void settingdateCalendar(){
//        // 현재시간을 msec 으로 구한다.
//        long now = System.currentTimeMillis();
//
//        // 현재시간을 date 변수에 저장한다.
//        Date date = new Date(now);
//
//        // 시간을 나타냇 포맷을 정한다 ( yyyy/MM/dd 같은 형태로 변형 가능 )
//        SimpleDateFormat sdfNow = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
//        // nowDate 변수에 값을 저장한다.
//        String formatDate = sdfNow.format(date);
//
//        Log.d(TAG, "=== 자바 날짜 long now ===" + now);
//        Log.d(TAG, "=== 자바 날짜 date ===" + date);
//        Log.d(TAG, "=== 자바 날짜 sdfNow ===" + sdfNow);
//        Log.d(TAG, "=== 자바 날짜 formatDate ===" + formatDate);
//
//        // Java 시간 더하기
//        Calendar cal = Calendar.getInstance();
//        cal.setTime(date);
//        String today = null;
//
//        today = sdfNow.format(cal.getTime());
//        Log.d(TAG, "=== 자바 날짜 지금 today ===" + today);
//
//        // 이틀 후
//        cal.add(Calendar.DATE, +2);
//        today = sdfNow.format(cal.getTime());
//        Log.d(TAG, "=== 자바 날짜 오늘 부터 이틀 후 today ===" + today);
//
//        Date currentDay = sdfNow.parse(today, new ParsePosition(0));
//        currentMinLong = currentDay.getTime();
//        Log.d(TAG, "=== 자바 날짜 오늘 부터 이틀 후 today 밀리세컨드로 ==="+currentMinLong);
//
//        // 오늘 부터 14일 후
//        cal.add(Calendar.DATE, +12);
//        today = sdfNow.format(cal.getTime());
//        Log.d(TAG, "=== 자바 날짜 오늘 부터 14일 후 today ===" + today);
//
//        currentDay = sdfNow.parse(today, new ParsePosition(0));
//        currentMaxLong = currentDay.getTime();
//        Log.d(TAG, "=== 자바 날짜 오늘 14일 후 today 밀리세컨드로 ===" + currentMaxLong);
//
//        //dateCalendar.setMaxDate(1570668133353L);
//        calendar_view.setMinDate(currentMinLong);
//        calendar_view.setMaxDate(currentMaxLong);
//        Log.d(TAG, "=== 자바 날짜 예약날짜 범위 지정 완료 ===");
//
//    }

    /** * 날짜로 요일 구하기 * @param date - 요일 구할 날짜 yyyyMMdd */
    public String getDayOfweek(String date){

        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        String[] week = {"일","월","화","수","목","금","토"};
        Calendar cal = Calendar.getInstance(); Date getDate;

        String result = null;

        try {
            getDate = format.parse(date);
            cal.setTime(getDate);
            int w = cal.get(Calendar.DAY_OF_WEEK)-1;

            result= week[w];

            Log.d(TAG, "=== 요일 === : " + week[w]);
        }
        catch (ParseException e) {
            e.printStackTrace();
            Log.e(TAG, "=== getDayOfweek e ===" +e);
        }
        catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "=== getDayOfweek e ===" +e);

        }
        return result;
    }


}

