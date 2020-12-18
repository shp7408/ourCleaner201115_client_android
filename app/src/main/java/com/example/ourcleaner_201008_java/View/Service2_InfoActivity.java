package com.example.ourcleaner_201008_java.View;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.ourcleaner_201008_java.DTO.ServiceDTO;
import com.example.ourcleaner_201008_java.GlobalApplication;
import com.example.ourcleaner_201008_java.R;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class Service2_InfoActivity extends AppCompatActivity {

    private static final String TAG = "Service2_정보 엑티비티";

    /* 화면에서 쓰는 변수 */
    Button roomBtn, bathRoomBtn, livingRoomBtn, kitchenBtn;

    LinearLayout laudryLayout,laundryCautionLayout, windowdustLayout, verandaLayout; //각 레이아웃 클릭시, 체크박스 체크됨
    //laundryCautionLayout의 경우에는, 세탁 체크시, 보여짐
    EditText laundryCautionEdit;
    String laundryCautionStr;
    CheckBox laundryCheckbox, windowdustCheckbox, verandaCheckbox;

    LinearLayout garbagerecycleLayout, garbageCautionLayout, garbagenormalLayout, garbagefoodLayout;
    //garbageCautionLayout의 경우에는, 쓰레기 배출 중 하나라도 체크시, 보여짐
    EditText garbageCautionEdit;
    String garbageCautionStr;
    CheckBox garbagerecycleCheckbox, garbagenormalCheckbox, garbagefoodCheckbox;

    Button nextBtn;

    /* 널체크 및 상태 가져오기 변수들 */
    HashMap<String, Boolean> servicefocusedhashMap; //집중받고싶은 서비스 클릭 여부 저장하는 해쉬맵
    HashMap<String, Boolean> serviceFreeAddhashMap; //무료 추가 서비스 저장하는 해쉬맵

    boolean roomfocusBool, bathRoomfocusBool, livingRoomfocusBool, kitchenfocusBool;

    /* 현재 엑티비티의 정보 담는 객체 */
    ServiceDTO serviceDTO1, serviceDTO2; //이전 엑티비티에서 객체 받아오기 위한 객체, 다음 엑티비티로 객체 보내기 위한 객체


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service2__info);
        Log.d(TAG, "=== onCreate ===" );

        //intent로 받을 때 사용하는 코드
        Intent intent = getIntent();
        Log.d(TAG, "Service1_TimeActivity에서 인텐트 받음 intent :"+intent);

        serviceDTO1 = (ServiceDTO) intent.getSerializableExtra("serviceDTO");
        Log.d(TAG, "=== serviceDTO1 ==="+serviceDTO1.getCurrentUser() );


        //집중 서비스 구역 선택 버튼 연결
        roomBtn = findViewById(R.id.roomBtn);
        bathRoomBtn = findViewById(R.id.bathRoomBtn);
        livingRoomBtn = findViewById(R.id.livingRoomBtn);
        kitchenBtn = findViewById(R.id.kitchenBtn);

        //서비스 집중 구간 선택
        Log.d(TAG, "=== servicefocusedhashMap 객체 생성 ===" );
        servicefocusedhashMap = new HashMap<String, Boolean>();

        Log.d(TAG, "=== servicefocusedhashMap 클리어함 ===" );
        servicefocusedhashMap.clear();

        servicefocusedhashMap.put("roomBtn",false);
        servicefocusedhashMap.put("bathRoomBtn",false);
        servicefocusedhashMap.put("livingRoomBtn",false);
        servicefocusedhashMap.put("kitchenBtn",false);

        Button.OnClickListener onClickListener = new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    //첫번째 버튼 행동
                    case R.id.roomBtn:

                        if(roomfocusBool){
                            Log.d(TAG, "=== roomBtn 클릭 : === ");
                            roomBtn.setSelected(false);
                            roomBtn.setTextColor(getResources().getColor(R.color.black));
                            Drawable top = getResources().getDrawable(R.drawable.baseline_night_shelter_black_18dp);
                            roomBtn.setCompoundDrawablesWithIntrinsicBounds(null, top , null, null);
                            roomfocusBool=false;

                            // 클릭 여부 저장하는 해쉬맵
                            servicefocusedhashMap.put("roomBtn",false);
                            Log.d(TAG, "=== servicefocusedhashMap roomBtn ===" + servicefocusedhashMap.get("roomBtn") );


                        }else{
                            Log.d(TAG, "=== roomBtn 클릭 : === ");
                            roomBtn.setSelected(true);
                            roomBtn.setTextColor(getResources().getColor(R.color.white));
                            Drawable top = getResources().getDrawable(R.drawable.outline_night_shelter_white_18dp);
                            roomBtn.setCompoundDrawablesWithIntrinsicBounds(null, top , null, null);
                            roomfocusBool=true;

                            // 다음 버튼 활성화
                            nextBtn.setEnabled(true);

                            // 클릭 여부 저장하는 해쉬맵
                            servicefocusedhashMap.put("roomBtn",true);
                            Log.d(TAG, "=== servicefocusedhashMap roomBtn ===" + servicefocusedhashMap.get("roomBtn") );
                        }

                        break;
                    //화장실 버튼 행동
                    case R.id.bathRoomBtn:

                        if(bathRoomfocusBool){
                            Log.d(TAG, "=== bathRoomBtn 클릭 : === ");
                            bathRoomBtn.setSelected(false);
                            bathRoomBtn.setTextColor(getResources().getColor(R.color.black));
                            Drawable top = getResources().getDrawable(R.drawable.baseline_bathtub_black_18dp);
                            bathRoomBtn.setCompoundDrawablesWithIntrinsicBounds(null, top , null, null);
                            bathRoomfocusBool=false;

                            // 클릭 여부 저장하는 해쉬맵
                            servicefocusedhashMap.put("bathRoomBtn",false);
                            Log.d(TAG, "=== servicefocusedhashMap bathRoomBtn ===" + servicefocusedhashMap.get("bathRoomBtn") );

                        }else{
                            Log.d(TAG, "=== bathRoomBtn 클릭 : === ");
                            bathRoomBtn.setSelected(true);
                            bathRoomBtn.setTextColor(getResources().getColor(R.color.white));
                            Drawable top = getResources().getDrawable(R.drawable.outline_bathtub_white_18dp);
                            bathRoomBtn.setCompoundDrawablesWithIntrinsicBounds(null, top , null, null);
                            bathRoomfocusBool=true;
                            // 다음 버튼 활성화
                            nextBtn.setEnabled(true);

                            // 클릭 여부 저장하는 해쉬맵
                            servicefocusedhashMap.put("bathRoomBtn",true);
                            Log.d(TAG, "=== servicefocusedhashMap bathRoomBtn ===" + servicefocusedhashMap.get("bathRoomBtn") );
                        }


                        break;

                    //거실 버튼 행동
                    case R.id.livingRoomBtn:

                        if(livingRoomfocusBool){
                            Log.d(TAG, "=== livingRoomBtn 클릭 : === ");
                            livingRoomBtn.setSelected(false);
                            livingRoomBtn.setTextColor(getResources().getColor(R.color.black));
                            Drawable top = getResources().getDrawable(R.drawable.baseline_family_restroom_black_18dp);
                            livingRoomBtn.setCompoundDrawablesWithIntrinsicBounds(null, top , null, null);
                            livingRoomfocusBool=false;

                            // 클릭 여부 저장하는 해쉬맵
                            servicefocusedhashMap.put("livingRoomBtn",false);
                            Log.d(TAG, "=== servicefocusedhashMap livingRoomBtn ===" + servicefocusedhashMap.get("livingRoomBtn") );

                        }else{
                            Log.d(TAG, "=== livingRoomBtn 클릭 : === ");
                            livingRoomBtn.setSelected(true);
                            livingRoomBtn.setTextColor(getResources().getColor(R.color.white));
                            Drawable top = getResources().getDrawable(R.drawable.outline_family_restroom_white_18dp);
                            livingRoomBtn.setCompoundDrawablesWithIntrinsicBounds(null, top , null, null);
                            livingRoomfocusBool=true;

                            // 다음 버튼 활성화
                            nextBtn.setEnabled(true);

                            // 클릭 여부 저장하는 해쉬맵
                            servicefocusedhashMap.put("livingRoomBtn",true);
                            Log.d(TAG, "=== servicefocusedhashMap livingRoomBtn ===" + servicefocusedhashMap.get("livingRoomBtn") );
                        }
                        break;

                    //두번째 버튼 행동
                    case R.id.kitchenBtn:

                        if(kitchenfocusBool){
                            Log.d(TAG, "=== kitchenBtn 클릭 : === ");
                            kitchenBtn.setSelected(false);
                            kitchenBtn.setTextColor(getResources().getColor(R.color.black));
                            Drawable top = getResources().getDrawable(R.drawable.baseline_kitchen_black_18dp);
                            kitchenBtn.setCompoundDrawablesWithIntrinsicBounds(null, top , null, null);
                            kitchenfocusBool=false;

                            // 클릭 여부 저장하는 해쉬맵
                            servicefocusedhashMap.put("kitchenBtn",false);
                            Log.d(TAG, "=== servicefocusedhashMap kitchenBtn ===" + servicefocusedhashMap.get("kitchenBtn") );

                        }else{
                            Log.d(TAG, "=== kitchenBtn 클릭 : === ");
                            kitchenBtn.setSelected(true);
                            kitchenBtn.setTextColor(getResources().getColor(R.color.white));
                            Drawable top = getResources().getDrawable(R.drawable.outline_kitchen_white_18dp);
                            kitchenBtn.setCompoundDrawablesWithIntrinsicBounds(null, top , null, null);
                            kitchenfocusBool=true;

                            // 다음 버튼 활성화
                            nextBtn.setEnabled(true);

                            // 클릭 여부 저장하는 해쉬맵
                            servicefocusedhashMap.put("kitchenBtn",true);
                            Log.d(TAG, "=== servicefocusedhashMap kitchenBtn ===" + servicefocusedhashMap.get("kitchenBtn") );

                        }

                        break;

                }
            }
        };

        roomBtn.setOnClickListener(onClickListener);
        bathRoomBtn.setOnClickListener(onClickListener);
        livingRoomBtn.setOnClickListener(onClickListener);
        kitchenBtn.setOnClickListener(onClickListener);

        //무료 추가 선택의 체크박스 연결 -> 레이아웃 클릭 이벤트에서 사용하기 위해서, 미리 레이아웃 연결함.
        laundryCheckbox = findViewById(R.id.laundryCheckbox);
        windowdustCheckbox = findViewById(R.id.windowdustCheckbox);
        verandaCheckbox = findViewById(R.id.verandaCheckbox);

        //무료 추가 선택 레이아웃 내부의 레이아웃
        laudryLayout = findViewById(R.id.laudryLayout);
        laundryCautionLayout = findViewById(R.id.laundryCautionLayout);
        windowdustLayout = findViewById(R.id.windowdustLayout);
        verandaLayout = findViewById(R.id.verandaLayout);

        Button.OnClickListener onClickListener2 = new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    //laudryLayout 클릭 이벤트
                    case R.id.laudryLayout:
                        Log.d(TAG, "=== laudryLayout 클릭 : === ");

                        // 1. 이미 체크한 경우,
                        //내부 체크 박스 check 안 됨으로 변경
                        //아래 cautionlayout 없어지기

                        // 2. 체크 안 되어 있는 경우,
                        //내부 체크 박스에 체크,
                        //아래 cautionlayout 등장함.

                        if(laundryCheckbox.isChecked()){
                            Log.d(TAG, "=== laundryCheckbox 이미 체크된 경우, ===" );
                            laundryCheckbox.setChecked(false);

                            laundryCautionLayout.setVisibility(View.GONE);

                        }else{
                            Log.d(TAG, "=== laundryCheckbox 체크 안 되어 있는 경우, ===" );
                            laundryCheckbox.setChecked(true);

                            laundryCautionLayout.setVisibility(View.VISIBLE);
                        }

                        break;
                    //laundryCautionLayout 클릭 이벤트
                    case R.id.laundryCautionLayout:
                        Log.d(TAG, "=== laundryCautionLayout 클릭 : === ");
                        break;

                    //windowdustLayout 클릭 이벤트
                    case R.id.windowdustLayout:
                        Log.d(TAG, "=== windowdustLayout 클릭 : === ");

                        if(windowdustCheckbox.isChecked()){
                            Log.d(TAG, "=== windowdustCheckbox 이미 체크된 경우, ===" );
                            windowdustCheckbox.setChecked(false);

                        }else{
                            Log.d(TAG, "=== windowdustCheckbox 체크 안 되어 있는 경우, ===" );
                            windowdustCheckbox.setChecked(true);
                        }

                        break;

                    //verandaLayout 클릭 이벤트
                    case R.id.verandaLayout:
                        Log.d(TAG, "=== verandaLayout 클릭 : === ");

                        if(verandaCheckbox.isChecked()){
                            Log.d(TAG, "=== verandaCheckbox 이미 체크된 경우, ===" );
                            verandaCheckbox.setChecked(false);

                        }else{
                            Log.d(TAG, "=== verandaCheckbox 체크 안 되어 있는 경우, ===" );
                            verandaCheckbox.setChecked(true);
                        }
                        break;
                }
            }
        };

        laudryLayout.setOnClickListener(onClickListener2);
        laundryCautionLayout.setOnClickListener(onClickListener2);
        windowdustLayout.setOnClickListener(onClickListener2);
        verandaLayout.setOnClickListener(onClickListener2);

        //레이아웃 내부에서 세탁 선택 시, 등장하는 에디트 텍스트. 세탁 주의사항 입력란
        laundryCautionEdit = findViewById(R.id.laundryCautionEdit);


        //쓰레기 배출 선택의 레이아웃 연결
        garbagerecycleLayout = findViewById(R.id.garbagerecycleLayout);
        garbageCautionLayout = findViewById(R.id.garbageCautionLayout);
        garbagenormalLayout = findViewById(R.id.garbagenormalLayout);
        garbagefoodLayout = findViewById(R.id.garbagefoodLayout);

        //쓰레기 배출 선택의 체크박스 연결
        garbagerecycleCheckbox = findViewById(R.id.garbagerecycleCheckbox);
        garbagenormalCheckbox = findViewById(R.id.garbagenormalCheckbox);
        garbagefoodCheckbox = findViewById(R.id.garbagefoodCheckbox);


        Button.OnClickListener onClickListener4 = new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    //garbageLayout 클릭 이벤트
                    case R.id.garbagerecycleLayout:
                        Log.d(TAG, "=== garbagerecycleLayout 클릭 : === ");
                        if(garbagerecycleCheckbox.isChecked()){
                            Log.d(TAG, "=== garbagerecycleCheckbox 이미 체크된 경우, ===" );
                            garbagerecycleCheckbox.setChecked(false);

                            if(!garbagenormalCheckbox.isChecked() && !garbagefoodCheckbox.isChecked())
                            garbageCautionLayout.setVisibility(View.GONE);

                        }else{
                            Log.d(TAG, "=== garbagerecycleCheckbox 체크 안 되어 있는 경우, ===" );
                            garbagerecycleCheckbox.setChecked(true);

                            garbageCautionLayout.setVisibility(View.VISIBLE);

                        }
                        break;
                    //garbageCautionLayout 클릭 이벤트
                    case R.id.garbageCautionLayout:
                        Log.d(TAG, "=== garbageCautionLayout 클릭 : === ");

                        break;

                    //garbagenormalLayout 클릭 이벤트
                    case R.id.garbagenormalLayout:
                        Log.d(TAG, "=== garbagenormalLayout 클릭 : === ");
                        if(garbagenormalCheckbox.isChecked()){
                            Log.d(TAG, "=== garbagenormalCheckbox 이미 체크된 경우, ===" );
                            garbagenormalCheckbox.setChecked(false);

                            if(!garbagerecycleCheckbox.isChecked() && !garbagefoodCheckbox.isChecked())
                                garbageCautionLayout.setVisibility(View.GONE);

                        }else{
                            Log.d(TAG, "=== garbagenormalCheckbox 체크 안 되어 있는 경우, ===" );
                            garbagenormalCheckbox.setChecked(true);

                            garbageCautionLayout.setVisibility(View.VISIBLE);

                        }
                        break;

                    //garbagefoodLayout 클릭 이벤트
                    case R.id.garbagefoodLayout:
                        Log.d(TAG, "=== garbagefoodLayout 클릭 : === ");
                        if(garbagefoodCheckbox.isChecked()){
                            Log.d(TAG, "=== garbagefoodCheckbox 이미 체크된 경우, ===" );
                            garbagefoodCheckbox.setChecked(false);

                            if(!garbagenormalCheckbox.isChecked() && !garbagerecycleCheckbox.isChecked())
                                garbageCautionLayout.setVisibility(View.GONE);

                        }else{
                            Log.d(TAG, "=== garbagefoodCheckbox 체크 안 되어 있는 경우, ===" );
                            garbagefoodCheckbox.setChecked(true);

                            garbageCautionLayout.setVisibility(View.VISIBLE);

                        }
                        break;


                }
            }
        };

        garbagerecycleLayout.setOnClickListener(onClickListener4);
        garbageCautionLayout.setOnClickListener(onClickListener4);
        garbagenormalLayout.setOnClickListener(onClickListener4);
        garbagefoodLayout.setOnClickListener(onClickListener4);

        //쓰레기 배출 선택에서 하나라도 체크 시, 배출 주의 사항, 안내사항 입력하는 에디트 텍스트
        garbageCautionEdit = findViewById(R.id.garbageCautionEdit);


        // TODO: 2020-11-17  다음 버튼, 구간에 1개 이상이라도, 입력하면, 다음 버튼 활성화
        // 다음 버튼, 구간에 1개 이상이라도, 입력하면, 다음 버튼 활성화
        nextBtn = (Button) findViewById(R.id.nextBtn);

        nextBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                    Log.d(TAG, "=== nextBtn 클릭 : === ");

                    //edittext에서 string 가져오기
                    laundryCautionStr = laundryCautionEdit.getText().toString();
                    garbageCautionStr = garbageCautionEdit.getText().toString();

                    //laundryCautionStr, garbageCautionStr이 비어 있는 경우, 예외처리
                    if(laundryCautionStr.isEmpty()){
                        laundryCautionStr="";
                    }

                    if(garbageCautionStr.isEmpty()){
                        garbageCautionStr="";
                    }

                    serviceDTO2 = new ServiceDTO(serviceDTO1.getCurrentUser(), serviceDTO1.getServiceState(),
                            serviceDTO1.getMyplaceDTO(), serviceDTO1.getManagerName(),
                            serviceDTO1.getRegularBool(), serviceDTO1.getVisitDate(),
                            serviceDTO1.getVisitDay(), serviceDTO1.getStartTime(),
                            serviceDTO1.getNeedDefTime(),serviceDTO1.getNeedDefCost(),
                            servicefocusedhashMap, laundryCheckbox.isChecked(),
                            laundryCautionStr, garbagerecycleCheckbox.isChecked(),
                            garbagenormalCheckbox.isChecked(),
                            garbagefoodCheckbox.isChecked(),
                            garbageCautionStr
                            );

                    Log.d(TAG, "=== serviceDTO2 객체 담고 다음 화면으로 이동 ===" );
                    Log.d(TAG, "=== serviceDTO2 getCurrentUser ===" + serviceDTO2.getCurrentUser());
                    Log.d(TAG, "=== serviceDTO2 getServiceState ===" + serviceDTO2.getServiceState());
                    Log.d(TAG, "=== serviceDTO2 getMyplaceDTO ===" + serviceDTO2.getMyplaceDTO());
                    Log.d(TAG, "=== serviceDTO2 getRegularBool ===" + serviceDTO2.getRegularBool());
                    Log.d(TAG, "=== serviceDTO2 getVisitDate ===" + serviceDTO2.getVisitDate());
                    Log.d(TAG, "=== serviceDTO2 getVisitDay ===" + serviceDTO2.getVisitDay());
                    Log.d(TAG, "=== serviceDTO2 getStartTime ===" + serviceDTO2.getStartTime());
                    Log.d(TAG, "=== serviceDTO2 getNeedDefTime ===" + serviceDTO2.getNeedDefTime());
                    Log.d(TAG, "=== serviceDTO2 getNeedDefCost ===" + serviceDTO2.getNeedDefCost());
                    Log.d(TAG, "=== serviceDTO2 servicefocusedhashMap ===" + servicefocusedhashMap);
                    Log.d(TAG, "=== serviceDTO2 laundryCheckbox.isChecked() ===" + laundryCheckbox.isChecked());
                    Log.d(TAG, "=== serviceDTO2 laundryCautionStr ===" + laundryCautionStr);
                    Log.d(TAG, "=== serviceDTO2 garbagerecycleCheckbox.isChecked() ===" + garbagerecycleCheckbox.isChecked());
                    Log.d(TAG, "=== serviceDTO2 garbagenormalCheckbox.isChecked() ===" + garbagenormalCheckbox.isChecked());
                    Log.d(TAG, "=== serviceDTO2 garbagefoodCheckbox.isChecked() ===" + garbagefoodCheckbox.isChecked());
                    Log.d(TAG, "=== serviceDTO2 garbagefoodCheckbox.isChecked() ===" + garbagefoodCheckbox.isChecked());
                    Log.d(TAG, "=== serviceDTO2 garbageCautionStr ===" + garbageCautionStr);

                    Intent intent = new Intent(getApplicationContext(), Service3_Activity.class);
                    intent.putExtra("serviceDTO2", serviceDTO2);
                    startActivity(intent);

                    }

            });

    }

}