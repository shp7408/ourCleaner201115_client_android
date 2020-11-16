package com.example.ourcleaner_201008_java.View;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.ourcleaner_201008_java.R;

import java.util.HashMap;

public class Service2_InfoActivity extends AppCompatActivity {

    private static final String TAG = "Service2_정보 엑티비티";

    /* 화면에서 쓰는 변수 */
    Button roomBtn, bathRoomBtn, livingRoomBtn, kitchenBtn;

    LinearLayout laudryLayout,laundryCautionLayout, windowdustLayout, verandaLayout; //각 레이아웃 클릭시, 체크박스 체크됨
    //laundryCautionLayout의 경우에는, 세탁 체크시, 보여짐
    EditText laundryCautionEdit;
    String laundryCautionStr;
    CheckBox laundryCheckbox, windowdustCheckbox, verandaCheckbox;

    LinearLayout garbageLayout, garbageCautionLayout, garbagenormalLayout, garbagefoodLayout;
    //garbageCautionLayout의 경우에는, 쓰레기 배출 중 하나라도 체크시, 보여짐
    EditText garbageCautionEdit;
    String garbageCautionStr;
    CheckBox garbagerecycleCheckbox, garbagenormalCheckbox, garbagefoodCheckbox;

    Button nextBtn;

    /* 널체크 및 상태 가져오기 변수들 */
    HashMap<String, Boolean> servicefocusedhashMap; //집중받고싶은 서비스 클릭 여부 저장하는 해쉬맵

    boolean roomfocusBool;
    boolean bathRoomfocusBool;
    boolean livingRoomfocusBool;
    boolean kitchenfocusBool;

    HashMap<String, Boolean> nexthashMap; //전체 클릭 여부 저장하는 해쉬맵




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service2__info);
        Log.d(TAG, "=== onCreate ===" );








    }
}