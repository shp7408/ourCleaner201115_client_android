package com.example.ourcleaner_201008_java.View;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.ourcleaner_201008_java.Adapter.SectionPageAdapter;
import com.example.ourcleaner_201008_java.CircleTransform;
import com.example.ourcleaner_201008_java.DTO.MyplaceDTO;
import com.example.ourcleaner_201008_java.DTO.ServiceDTO;
import com.example.ourcleaner_201008_java.Dialog.ExampleBottomSheetDialog;
import com.example.ourcleaner_201008_java.Interface.ManagerSelectDetailInterface;
import com.example.ourcleaner_201008_java.R;
import com.example.ourcleaner_201008_java.View.Fragment.Fragment_First;
import com.example.ourcleaner_201008_java.View.Fragment.Fragment_Second;
import com.example.ourcleaner_201008_java.View.Fragment.Fragment_Third;
import com.google.android.material.tabs.TabLayout;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class Service_SelectManager_DetailActivity extends AppCompatActivity
        implements
        ExampleBottomSheetDialog.BottomSheetListener,
        Fragment_First.OnDateCickListener,
        Fragment_First.OnStartTimeClickListener{

    private static final String TAG = "매니저디테일정보";

    public static Context mcontext;

    String managerEmail;
    int managerUidStr;

    ImageView profileImage;
    TextView nameTxt,phoneNumTxt, reviewScoreNumTxt, reviewMoreTxt, activateGuideTxt, managerAddressTxt;
    RatingBar ratingBar;
    Button place1Btn, place2Btn, place3Btn, place4Btn, place5Btn, chooseBtn;
    LinearLayout linearlayout;

    Service_SelectManagersActivity cActivity;

    private ViewPager mViewPager;
    SectionPageAdapter adapter = new SectionPageAdapter(getSupportFragmentManager());

    /* 현재 엑티비티의 정보 담는 객체 */
    ServiceDTO serviceDTO;
    MyplaceDTO myplaceDTO;

//    CalendarView calendar_view;


    /* 회색상자 */
    TextView mainNeedTime, mainStartdate, mainStartTime, mainNeedCost;

    String date, day;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service__select_manager__detail);
        Log.d(TAG, "=== onCreate ===" );

        /* 장소등록엑티비티, 장소등록엑티비티2 삭제하기 위한 코드  3.  */
//        cActivity = (Service_SelectManagersActivity) Service_SelectManagersActivity.CActivity;

        //intent로 받을 때 사용하는 코드
        Intent intent = getIntent();
        Log.d(TAG, "약관동의 엑티비티에서 인텐트 받음 intent :" + intent);

        managerEmail = intent.getStringExtra("managerEmail");
        Log.e(TAG, "managerEmail: " + managerEmail);
        serviceDTO = (ServiceDTO) intent.getSerializableExtra("serviceDTO");
        Log.e(TAG, "serviceDTO getNeedDefCost: "+ serviceDTO.getNeedDefCost());
        Log.e(TAG, "serviceDTO getNeedDefTime: "+ serviceDTO.getNeedDefTime());

        myplaceDTO= serviceDTO.getMyplaceDTO();
        Log.e(TAG, "serviceDTO getNeedDefCost: "+ myplaceDTO.getSizeIndexint());
        Log.e(TAG, "serviceDTO getNeedDefCost: "+ myplaceDTO.getSizeStr());

        linearlayout = findViewById(R.id.linearlayout);

        mcontext=this;

        mainNeedTime = findViewById(R.id.mainNeedTime);
        mainNeedTime.setText(serviceDTO.getNeedDefTime()/60+" 시간");

        mainStartdate = findViewById(R.id.mainStartdate);
        mainStartTime= findViewById(R.id.mainStartTime);
        mainNeedCost = findViewById(R.id.mainNeedCost);






        nameTxt = findViewById(R.id.nameTxt);
        phoneNumTxt = findViewById(R.id.phoneNumTxt);
        reviewScoreNumTxt = findViewById(R.id.reviewScoreNumTxt);
//        reviewMoreTxt = findViewById(R.id.reviewMoreTxt);
        activateGuideTxt = findViewById(R.id.activateGuideTxt);

//        ratingBar = findViewById(R.id.ratingBar);

        place1Btn = findViewById(R.id.place1Btn);
        place2Btn = findViewById(R.id.place2Btn);
        place3Btn = findViewById(R.id.place3Btn);
        place4Btn = findViewById(R.id.place4Btn);
        place5Btn = findViewById(R.id.place5Btn);

        chooseBtn = findViewById(R.id.chooseBtn);

        managerAddressTxt = findViewById(R.id.managerAddressTxt);

        place1Btn.setVisibility(View.GONE);
        place2Btn.setVisibility(View.GONE);
        place3Btn.setVisibility(View.GONE);
        place4Btn.setVisibility(View.GONE);
        place5Btn.setVisibility(View.GONE);

        profileImage = findViewById(R.id.profileImage);


        mViewPager = (ViewPager) findViewById(R.id.container);
//        setupViewPager(mViewPager);

        /* 예약 탭 프래그먼트 부분 */
        Fragment_First fragment_first = new Fragment_First();

        Bundle bundle = new Bundle();
        bundle.putInt("getNeedDefCost", serviceDTO.getNeedDefCost());
        bundle.putInt("getNeedDefTime", serviceDTO.getNeedDefTime());
        bundle.putInt("getSizeIndexint", myplaceDTO.getSizeIndexint());
        bundle.putString("getSizeStr", myplaceDTO.getSizeStr());

        fragment_first.setArguments(bundle);

        adapter.addFragment(fragment_first, "예약 하기");


        /* 서비스 및 후기 부분 프래그먼트 부분 */
        adapter.addFragment(new Fragment_Second(), "서비스");
        adapter.addFragment(new Fragment_Third(), "후기");
        mViewPager.setAdapter(adapter);



        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        fetchJSON();




        chooseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e(TAG, "=== chooseBtn 클릭 ===" );

                //있으면 넘어감
//                Intent intent = new Intent();
//                intent.putExtra("managerNameEmailStr", nameTxt.getText().toString()+","+managerEmail);

//                setResult(1, intent); //Service1_TimeActivity.java 에서 미리 설정한 코드를 넣음
//
//                /* 장소등록엑티비티, 장소등록엑티비티2 삭제하기 위한 코드  4. finish() */
//                cActivity.finish();
//                finish();


                Log.d(TAG, "=== 객체 넣고 다음 화면으로 이동 ===" );
                Intent intent = new Intent(getApplicationContext(), Service2_InfoActivity.class);
                intent.putExtra("serviceDTO", serviceDTO);
                startActivity(intent);

            }
        });


        mainStartdate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if(mainStartdate.getText().toString()==""){
                    Log.d(TAG, "=== 빈 상태임 안되면 null로 바꾸기 ===" );

                }else{
                    Log.d(TAG, "=== 안 비었음 ===" );
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });





    }






    public void setupViewPager(ViewPager viewPager) {
        adapter.addFragment(new Fragment_First(), "예약 하기");
        adapter.addFragment(new Fragment_Second(), "서비스");
        adapter.addFragment(new Fragment_Third(), "후기");
        viewPager.setAdapter(adapter);
    }


    /* 매니저 이메일 서버로 보내서, 매니저 정보 가져오는 부분
    * 이 때, 서비스 객체에
    * 박서현,shp7400@naver.com
    * 매니저이름 부분
    * 셋함 */
    private void fetchJSON(){

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ManagerSelectDetailInterface.JSONURL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();

        ManagerSelectDetailInterface api = retrofit.create(ManagerSelectDetailInterface.class);

        Map<String, String> mapdata = new HashMap<>();
        mapdata.put("managerEmail", managerEmail);

        Log.e(TAG, "managerEmail" + managerEmail);

        Call<String> call = api.getManagerDetail(mapdata);

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Log.e(TAG, "Responsestring" + response.body());
                //Toast.makeText()
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        Log.e(TAG, "onSuccess" + response.body());

                        String jsonresponse = response.body();
                        Log.e(TAG, "=== jsonresponse ===" +jsonresponse );

                        try {
                            JSONObject obj = new JSONObject(jsonresponse);
                            Log.e(TAG, "=== response ===" +response);

                            if(obj.optString("status").equals("true")){

                                JSONArray dataArray  = obj.getJSONArray("data");

                                // TODO: 2020-12-15 여기서 해당 텍스트 뷰에 데이터 넣음

                                for (int i = 0; i < dataArray.length(); i++) {

                                    JSONObject dataobj = dataArray.getJSONObject(i);

                                    managerUidStr = dataobj.getInt("uid");
                                    nameTxt.setText(dataobj.getString("nameStr"));
                                    phoneNumTxt.setText(dataobj.getString("phoneNumStr"));


                                    serviceDTO.setManagerName(dataobj.getString("nameStr")+","+managerEmail);

                                    activateGuideTxt.setText(dataobj.getString("nameStr")+" 매니저 님이 주로 활동하는 지역입니다.");

                                    Picasso.get()
                                            .load( dataobj.getString("profileImagePathStr"))
                                            .transform(new CircleTransform())
                                            .into(profileImage);

                                    String addressStr = dataobj.getString("addressStr");

                                    /* 경기 화성시 까지 입력해서 일반 유저 고객이 알 수 있도록 함 */
                                    managerAddressTxt.setText(addressStr.substring(8,14));



                                    String string = dataobj.getString("desiredWorkAreaList");
                                    string = string.replace("[","");
                                    string = string.replace("]","");

                                    String[] array = string.split(",");
                                    Log.e(TAG, "=== array ===" + array );

                                    /* 어레이리스트로 변환 */
                                    ArrayList<String> arrayList = new ArrayList<>();

                                    for(String temp : array){
                                        arrayList.add(temp);
                                    }

                                    /* 어레이리스트 */
                                    if(arrayList.size()==5){

                                        place1Btn.setVisibility(View.VISIBLE);
                                        place2Btn.setVisibility(View.VISIBLE);
                                        place3Btn.setVisibility(View.VISIBLE);
                                        place4Btn.setVisibility(View.VISIBLE);
                                        place5Btn.setVisibility(View.VISIBLE);

                                        place1Btn.setText(arrayList.get(0));
                                        place2Btn.setText(arrayList.get(1));
                                        place3Btn.setText(arrayList.get(2));
                                        place4Btn.setText(arrayList.get(3));
                                        place5Btn.setText(arrayList.get(4));

                                    }else if(arrayList.size()==4){

                                        place1Btn.setVisibility(View.VISIBLE);
                                        place2Btn.setVisibility(View.VISIBLE);
                                        place3Btn.setVisibility(View.VISIBLE);
                                        place4Btn.setVisibility(View.VISIBLE);

                                        place1Btn.setText(arrayList.get(0));
                                        place2Btn.setText(arrayList.get(1));
                                        place3Btn.setText(arrayList.get(2));
                                        place4Btn.setText(arrayList.get(3));

                                    }else if(arrayList.size()==3){

                                        place1Btn.setVisibility(View.VISIBLE);
                                        place2Btn.setVisibility(View.VISIBLE);
                                        place3Btn.setVisibility(View.VISIBLE);

                                        place1Btn.setText(arrayList.get(0));
                                        place2Btn.setText(arrayList.get(1));
                                        place3Btn.setText(arrayList.get(2));

                                    }else if(arrayList.size()==2){

                                        place1Btn.setVisibility(View.VISIBLE);
                                        place2Btn.setVisibility(View.VISIBLE);

                                        place1Btn.setText(arrayList.get(0));
                                        place2Btn.setText(arrayList.get(1));

                                    }else if(arrayList.size()==1){

                                        place1Btn.setVisibility(View.VISIBLE);

                                        place1Btn.setText(arrayList.get(0));

                                    }else{

                                    }

                                }
                       }else {
                                Toast.makeText(Service_SelectManager_DetailActivity.this, obj.optString("message")+"", Toast.LENGTH_SHORT).show();
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
                Log.e(TAG, "onFailure");

            }
        });
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






//    @Override
//    public void onButtonClicked(String text) {
//        Log.d(TAG, "=== onButtonClicked ===" +text);
//    }
//
    @Override
    public void onButtonClicked(int getNeedDefTime) { // 3~
        Log.d(TAG, "=== onButtonClicked ===" +getNeedDefTime);
        Log.d(TAG, "=== onButtonClicked*60 ===" +getNeedDefTime*60);
        /* 데이터 객체에서 시간도 바꾸고, 뷰에도 셋텍스트 해야 함 */
        serviceDTO.setNeedDefTime(getNeedDefTime*60);
        Log.d(TAG, "=== 전달할 객체 setNeedDefTime 바뀌었는지 확인하기 ===" +serviceDTO.getNeedDefTime());


        mainNeedTime.setText(getNeedDefTime+" 시간");

        int cost;

        cost = 30000+(getNeedDefTime-3)*12000;


        //숫자 천 자리에 콤마 찍기
        DecimalFormat formatter = new DecimalFormat("###,###");

        mainNeedCost.setText(formatter.format(cost)+ " 원");

    }

   @Override
    public void onDateSetFinish(String date, String day) {
        Log.e(TAG, "=== date day === " +date + day);


        String monthStr, dateStr;

        /* date 01.05 를 나누는 작업 */
        monthStr = date.substring(0,2);
        dateStr = date.substring(3);
        Log.d(TAG, "=== monthStr ===" +monthStr);
        Log.d(TAG, "=== dateStr ===" +dateStr);

        mainStartdate.setText(remove0Word(monthStr)+"월 "+remove0Word(dateStr) +"일 ("+day+")");

        serviceDTO.setVisitDate(remove0Word(monthStr)+"."+remove0Word(dateStr));
        serviceDTO.setVisitDay(day);
    }

    /* String 날짜의 경우, 앞에 0 제거 */
    public String remove0Word(String word){

        String sum = "";
        String firstWord = "";

        for (int i = 0; i < word.length(); i++) {
            String dic = word.substring(i, i+1);
            if(i==0){firstWord=dic;}
            if(i!=0 && !firstWord.equals("0")){

                sum += dic;

            }else if(!dic.equals("0")){
                sum += dic;
                firstWord = dic;
            }
            System.out.println(i + " : " + dic);

        }

        return sum;

    }


    @Override
    public void onStartTimeFinish(int startTime) {
        Log.d(TAG, "=== startTime ===" +startTime);
        mainStartTime.setText(startTime/60+"시 시작");

        serviceDTO.setStartTime(startTime);
        chooseBtn.setEnabled(true);


    }
}