package com.example.ourcleaner_201008_java.View;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.ourcleaner_201008_java.DTO.ServiceDTO;
import com.example.ourcleaner_201008_java.Dialog.Service3Dialog;
import com.example.ourcleaner_201008_java.Interface.Service3DialogClickListener;
import com.example.ourcleaner_201008_java.R;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class Service3_Activity extends AppCompatActivity {

    private static final String TAG = "Service3_Activity";

    /* 현재 엑티비티의 정보 담는 객체 */
    ServiceDTO serviceDTO2, serviceDTO3; //이전 엑티비티에서 객체 받아오기 위한 객체, 다음 엑티비티로 객체 보내기 위한 객체

    /* 화면에서 쓰는 변수 */
    LinearLayout ironingLayout, fridgeLayout;
    CheckBox ironingCheckbox, fridgeCheckbox;

    TextView plusTimeTxt;

    int plusTimeInt = 0;
    String plusTimeStr; //추가 시간

    EditText serCautionEdit;

    Button nextBtn;

    HashMap<String, Boolean> plusServicehashMap; //전체 클릭 여부 저장하는 해쉬맵


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service3_);
        Log.d(TAG, "=== onCreate ===" );

        //intent로 받을 때 사용하는 코드
        Intent intent = getIntent();
        Log.d(TAG, "Service1_TimeActivity에서 인텐트 받음 intent :"+intent);

        serviceDTO2 = (ServiceDTO) intent.getSerializableExtra("serviceDTO2");
        Log.d(TAG, "=== serviceDTO2 ===" + serviceDTO2.getRegularBool() );

        // 레이아웃 연결 -> 클릭 시, 체크박스에 체크
        ironingLayout = findViewById(R.id.ironingLayout);
        fridgeLayout = findViewById(R.id.fridgeLayout);

        // 체크박스 연결
        ironingCheckbox = findViewById(R.id.ironingCheckbox);
        fridgeCheckbox = findViewById(R.id.fridgeCheckbox);

        // 그 외 연결
        plusTimeTxt = findViewById(R.id.plusTimeTxt);
        plusTimeTxt.setText("+"+plusTimeInt+"분");

        serCautionEdit = findViewById(R.id.serCautionEdit);
        nextBtn = findViewById(R.id.nextBtn);

        plusServicehashMap = new HashMap();


        plusServicehashMap.put("다림질", false);
        plusServicehashMap.put("냉장고", false);

        Button.OnClickListener onClickListener = new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    //ironingLayout 클릭 이벤트
                    case R.id.ironingLayout:
                        Log.d(TAG, "=== ironingLayout 클릭 ===" );
                        if(ironingCheckbox.isChecked()){

                            // 이미 체크 된 상태 -> 체크 안 된 상태로 변경
                            ironingCheckbox.setChecked(false);
                            ironingLayout.setBackgroundResource(R.drawable.bordergrey);

                            Log.d(TAG, "=== plusTimeInt ===" + plusTimeInt); //plusTimeInt은 분으로만 나타냄
                            plusTimeInt = plusTimeInt-30; //30분 빼기
                            Log.d(TAG, "=== plusTimeInt 30분 빼고난 후, ===" +plusTimeInt);

                            plusTimeStr = timeIntToHourMin(plusTimeInt);

                            plusTimeTxt.setText(plusTimeStr);

                            plusServicehashMap.put("다림질", ironingCheckbox.isChecked());


                        }else{
                            // 체크 안 된 상태 -> 체크 상태로 변경


                            Log.d(TAG, "=== plusTimeInt ===" +plusTimeInt); //plusTimeInt은 분으로만 나타냄

                            //ironingLayout 추가 클릭 시, 생성되는 다이얼로그
                            //다이얼로그에서 추가 버튼 클릭하면, 해당 시간과 가격이 추가됨.
                            Service3Dialog octDialog = new Service3Dialog(Service3_Activity.this,
                                    "다림질","+30분(7,000원)","1회당 3장 내외로 간단한 일상 다림질만 가능",
                                    new Service3DialogClickListener() {
                                @Override
                                public void onPositiveClick() {
                                    Log.d(TAG, "=== onPositiveClick ===" );

                                    ironingCheckbox.setChecked(true);
                                    ironingLayout.setBackgroundResource(R.drawable.border);

                                    plusTimeInt = plusTimeInt+30; //30분 더하기
                                    Log.d(TAG, "=== plusTimeInt 30분 더하고난 후, ===" +plusTimeInt);

                                    plusTimeStr = timeIntToHourMin(plusTimeInt);

                                    plusTimeTxt.setText(plusTimeStr);

                                    plusServicehashMap.put("다림질", ironingCheckbox.isChecked());

                                }

                                @Override
                                public void onNegativeClick() {
                                    Log.d(TAG, "=== onNegativeClick ===" );
                                }
                            });
                            octDialog.setCanceledOnTouchOutside(true);
                            octDialog.setCancelable(true);
                            octDialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
                            octDialog.show();

                        }

                        break;
                    //fridgeLayout 클릭 이벤트
                    case R.id.fridgeLayout:
                        Log.d(TAG, "=== fridgeLayout 클릭 ===" );
                        if(fridgeCheckbox.isChecked()){
                            // 이미 체크 된 상태 -> 체크 안 된 상태로 변경
                            fridgeCheckbox.setChecked(false);
                            fridgeLayout.setBackgroundResource(R.drawable.bordergrey);

                            Log.d(TAG, "=== plusTimeInt ===" +plusTimeInt); //plusTimeInt은 분으로만 나타냄
                            plusTimeInt = plusTimeInt-120; //120분 빼기
                            Log.d(TAG, "=== plusTimeInt 120분 빼고난 후, ===" +plusTimeInt);

                            plusTimeStr = timeIntToHourMin(plusTimeInt);

                            plusTimeTxt.setText(plusTimeStr);

                            plusServicehashMap.put("냉장고", fridgeCheckbox.isChecked());


                        }else{
                            // 체크 안 된 상태 -> 체크 상태로 변경


                            Log.d(TAG, "=== plusTimeInt ===" + plusTimeInt); //plusTimeInt은 분으로만 나타냄

                            //fridgeLayout 추가 클릭 시, 생성되는 다이얼로그
                            //다이얼로그에서 추가 버튼 클릭하면, 해당 시간과 가격이 추가됨.
                            Service3Dialog octDialog = new Service3Dialog(Service3_Activity.this,
                                    "냉장고 간단청소","+2시간(26,400원)","1대 기준으로 냉장실 청소만 진행", new Service3DialogClickListener() {
                                @Override
                                public void onPositiveClick() {
                                    Log.d(TAG, "=== onPositiveClick ===" );

                                    fridgeCheckbox.setChecked(true);
                                    fridgeLayout.setBackgroundResource(R.drawable.border);

                                    plusTimeInt = plusTimeInt+120; //120분 더하기
                                    Log.d(TAG, "=== plusTimeInt 120분 더하고난 후, ===" +plusTimeInt);

                                    plusTimeStr = timeIntToHourMin(plusTimeInt);

                                    plusTimeTxt.setText(plusTimeStr);

                                    plusServicehashMap.put("냉장고", fridgeCheckbox.isChecked());

                                }

                                @Override
                                public void onNegativeClick() {
                                    Log.d(TAG, "=== onNegativeClick ===" );
                                }
                            });
                            octDialog.setCanceledOnTouchOutside(true);
                            octDialog.setCancelable(true);
                            octDialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
                            octDialog.show();
                        }
                        break;
                }
            }
        };

        ironingLayout.setOnClickListener(onClickListener);
        fridgeLayout.setOnClickListener(onClickListener);

        //serCautionEdit 에 값이 변할 때 마다, 체크하는 메서드. 버튼 활성화 역할 함
        serCautionEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Log.d(TAG, "=== serCautionEdit onTextChanged ===" );
                if(serCautionEdit.getText().toString().isEmpty()){
                    Log.d(TAG, "=== serCautionEdit 비어있음 ===" );
                }else{
                    Log.d(TAG, "=== serCautionEdit 비어있지 않음 ===" );
                    nextBtn.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        //다음 버튼 클릭
        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "=== nextBtn 클릭 ===" );

                serviceDTO3 = new ServiceDTO(serviceDTO2.getCurrentUser(), serviceDTO2.getServiceState(),
                        serviceDTO2.getMyplaceDTO(), serviceDTO2.getManagerName(), serviceDTO2.getRegularBool(),
                        serviceDTO2.getVisitDate(), serviceDTO2.getVisitDay(), serviceDTO2.getStartTime(),
                        serviceDTO2.getNeedDefTime(), serviceDTO2.getNeedDefCost(), serviceDTO2.getServicefocusedhashMap(),
                        serviceDTO2.getLaundryBool(), serviceDTO2.getLaundryCaution(), serviceDTO2.getGarbagerecycleBool(),
                        serviceDTO2.getGarbagenormalBool(), serviceDTO2.getGarbagefoodBool(), serviceDTO2.getGarbagehowto(),
                        plusServicehashMap, serCautionEdit.getText().toString());

                Log.d(TAG, "=== serviceDTO3 getCurrentUser ===" + serviceDTO2.getCurrentUser());
                Log.d(TAG, "=== serviceDTO3 getServiceState ===" + serviceDTO2.getServiceState());
                Log.d(TAG, "=== serviceDTO3 getMyplaceDTO ===" + serviceDTO2.getMyplaceDTO());
                Log.d(TAG, "=== serviceDTO3 getRegularBool ===" + serviceDTO2.getRegularBool());
                Log.d(TAG, "=== serviceDTO3 getVisitDate ===" + serviceDTO2.getVisitDate());
                Log.d(TAG, "=== serviceDTO3 getVisitDay ===" + serviceDTO2.getVisitDay());
                Log.d(TAG, "=== serviceDTO3 getStartTime ===" + serviceDTO2.getStartTime());
                Log.d(TAG, "=== serviceDTO3 getNeedDefTime ===" + serviceDTO2.getNeedDefTime());
                Log.d(TAG, "=== serviceDTO3 getNeedDefCost ===" + serviceDTO2.getNeedDefCost());
                Log.d(TAG, "=== serviceDTO3 getServicefocusedhashMap ===" + serviceDTO2.getServicefocusedhashMap());
                Log.d(TAG, "=== serviceDTO3 getLaundryBool ===" + serviceDTO2.getLaundryBool());
                Log.d(TAG, "=== serviceDTO3 getLaundryCaution ===" + serviceDTO2.getLaundryCaution());
                Log.d(TAG, "=== serviceDTO3 getGarbagerecycleBool ===" + serviceDTO2.getGarbagerecycleBool());
                Log.d(TAG, "=== serviceDTO3 getGarbagenormalBool ===" + serviceDTO2.getGarbagenormalBool());
                Log.d(TAG, "=== serviceDTO3 getGarbagefoodBool ===" + serviceDTO2.getGarbagefoodBool());
                Log.d(TAG, "=== serviceDTO3 getGarbagehowto ===" + serviceDTO2.getGarbagehowto());
                Log.d(TAG, "=== serviceDTO3 getServiceplus ===" + serviceDTO3.getServiceplus());
                Log.d(TAG, "=== serviceDTO3 getServiceCaution ===" + serviceDTO3.getServiceCaution());



                Log.d(TAG, "=== 객체 담고 다음 화면으로 이동 ===" );

                Intent intent = new Intent(getApplicationContext(), Service_completeActivity.class);
                intent.putExtra("serviceDTO3", serviceDTO3);
                startActivity(intent);

            }
        });


    }

    //String 형태의 "3시간 30분" -> int 분 형태 로 나타내는 메서드
//    public int hourMinToTimeInt(String hourMinStr){
//
//        return
//    }

    //int 분 형태의 정수를 "3시간 30분" String으로 나타내는 메서드
    public String timeIntToHourMin(int plusTimeInt){

        long hour = TimeUnit.MINUTES.toHours(plusTimeInt); // 분을 시간으로 변경
        Log.d(TAG, "=== hour ===" +hour);

        long minutes = TimeUnit.MINUTES.toMinutes(plusTimeInt) - TimeUnit.HOURS.toMinutes(hour); // 시간으로 변경하고, 나머지 분
        Log.d(TAG, "=== minutes ==="+minutes );
        if(hour==0){
            Log.d(TAG, "=== hour==0  ===" );
            plusTimeStr = "+ "+ minutes + "분";
            Log.d(TAG, "=== plusTimeStr ===" +plusTimeStr);
        }else if(minutes==0){
            Log.d(TAG, "=== minutes==0 ===" );
            plusTimeStr = "+ "+ hour + "시간";
            Log.d(TAG, "=== plusTimeStr ===" +plusTimeStr);
        }else{
            Log.d(TAG, "=== hour 랑 minutes 둘 다 0이 아닌, 경우 ===" );
            plusTimeStr = "+ "+ hour +"시간 "+ minutes + "분";
            Log.d(TAG, "=== plusTimeStr ===" +plusTimeStr);
        }

        return plusTimeStr;
    }


}