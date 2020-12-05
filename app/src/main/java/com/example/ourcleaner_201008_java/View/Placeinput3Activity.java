package com.example.ourcleaner_201008_java.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.ourcleaner_201008_java.GlobalApplication;
import com.example.ourcleaner_201008_java.R;

import java.util.HashMap;
import java.util.Map;

public class Placeinput3Activity extends AppCompatActivity {

    private static final String TAG = "장소 저장 엑티비티";

    /* 이전 엑티비티Placeinput2Activity 에서 받아오는 변수 */
    String address, sizeStr, detailAddress;
    int sizeIndexint;

    /* 화면에서 쓰는 변수 */
    Button pet1Btn, pet2Btn, pet3Btn, pet4Btn;
    TextView petGuideTxt; //gone 상태. 펫버튼 1~3 클릭 시, 나타나고, 펫버튼 4 클릭 시, 사라짐
    EditText petGuideEdit; // 위 동일
    String petGuideStr="";

    Button childIsBtn, childNotBtn; //아이 있는지 없는지 버튼
    Button ctIsBtn, ctNotBtn;
    Button parkingIsBtn, parkingNotBtn;
    TextView parkingGuideTxt; //gone 상태. parkingIsBtn 클릭시,  나타나고, parkingNotBtn 클릭 시, 사라짐
    EditText parkingGuideEdit; // 위 동일
    String parkingGuideStr="";

    EditText placeNameEdit;
    String placeNameStr;

    Button saveBtn;

    /* 널체크 및 상태 가져오기 변수들 */
    HashMap<String, Boolean> pethashMap; //반려동물 클릭 여부 저장하는 해쉬맵 + 나의 장소 데이터 저장 시에도 사용 함.

    boolean childBool;
    boolean cctvBool;
    boolean parkingBool;

    HashMap<String, Boolean> nexthashMap; //전체 클릭 여부 저장하는 해쉬맵

    /* volley 서버로 데이터 보내기 위한 변수 선언 */
    private RequestQueue queue;

    boolean petDog;
    boolean petCat;
    boolean petEtc;

    PlaceinputActivity aActivity;
    Placeinput2Activity bActivity;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_placeinput3);

        Log.d(TAG, "=== onCreate ===");

        /* 장소등록엑티비티, 장소등록엑티비티2 삭제하기 위한 코드  3.  */
        aActivity = (PlaceinputActivity)PlaceinputActivity.AActivity;
        bActivity = (Placeinput2Activity)Placeinput2Activity.BActivity;


        //intent로 받을 때 사용하는 코드
        Intent intent = getIntent();
        Log.d(TAG, "주소 입력 엑티비티에서 인텐트 받음 intent :" + intent);

        address = intent.getStringExtra("address");
        Log.d(TAG, "주소 입력 엑티비티에서 인텐트 받음 intent.address : " + address);

        sizeStr = intent.getStringExtra("sizeStr");
        Log.d(TAG, "주소 입력 엑티비티에서 인텐트 받음 intent.sizeStr : " + sizeStr);

        sizeIndexint = intent.getIntExtra("sizeIndexint", 0);
        Log.d(TAG, "약관동의 엑티비티에서 인텐트 받음 intent.sizeIndexint : " + sizeIndexint);

        detailAddress = intent.getStringExtra("detailAddress");
        Log.d(TAG, "약관동의 엑티비티에서 인텐트 받음 intent.detailPlaceStr : " + detailAddress);



        //각버튼 아이디 매칭
        pet1Btn = findViewById(R.id.pet1Btn);
        pet2Btn = findViewById(R.id.pet2Btn);
        pet3Btn = findViewById(R.id.pet3Btn);
        pet4Btn = findViewById(R.id.pet4Btn);

        petGuideTxt = findViewById(R.id.petGuideTxt);
        petGuideEdit = findViewById(R.id.petGuideEdit);

        childIsBtn = findViewById(R.id.childIsBtn);
        childNotBtn = findViewById(R.id.childNotBtn);

        ctIsBtn = findViewById(R.id.ctIsBtn);
        ctNotBtn = findViewById(R.id.ctNotBtn);
//
        parkingIsBtn = findViewById(R.id.parkingIsBtn);
        parkingNotBtn = findViewById(R.id.parkingNotBtn);

        parkingGuideTxt = findViewById(R.id.parkingGuideTxt);
        parkingGuideEdit = findViewById(R.id.parkingGuideEdit);

        placeNameEdit = findViewById(R.id.placeNameEdit);

        saveBtn = findViewById(R.id.saveBtn);



        //반려동물 클릭 여부 저장하는 해쉬맵
        Log.d(TAG, "=== pethashMap 객체 생성 ===" );
        pethashMap = new HashMap<String, Boolean>();

        Log.d(TAG, "=== pethashMap 클리어함 ===" );
        pethashMap.clear();



        //반려동물 클릭 여부 저장하는 해쉬맵
        Log.d(TAG, "=== nexthashMap 객체 생성 ===" );
        nexthashMap = new HashMap<String, Boolean>();

        Log.d(TAG, "=== nexthashMap 클리어함 ===" );
        nexthashMap.clear();



        /* 반려동물 있는지 확인 버튼 */
        Button.OnClickListener onClickListener = new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    //개 버튼 행동
                    case R.id.pet1Btn:
                        Log.d(TAG, "=== pet1Btn ===" );

                        pet1Btn.setSelected(true);
                        pet1Btn.setTextColor(getResources().getColor(R.color.white));

                        pet4Btn.setSelected(false);
                        pet4Btn.setTextColor(getResources().getColor(R.color.black));

                        Log.d(TAG, "=== 반려동물 유의사항 입력란 나타남 ===" );
                        petGuideTxt.setVisibility(View.VISIBLE);
                        petGuideEdit.setVisibility(View.VISIBLE);

                        pethashMap.put("개",true);
                        pethashMap.put("고양이",false);
                        pethashMap.put("기타",false);
                        nexthashMap.put("반려동물", true);

                        nullCheckforNextBtnActivate();

                        break;
                    //고양이 버튼 행동
                    case R.id.pet2Btn:
                        Log.d(TAG, "=== pet2Btn ===" );
                        pet2Btn.setSelected(true);
                        pet2Btn.setTextColor(getResources().getColor(R.color.white));

                        pet4Btn.setSelected(false);
                        pet4Btn.setTextColor(getResources().getColor(R.color.black));

                        Log.d(TAG, "=== 반려동물 유의사항 입력란 나타남 ===" );
                        petGuideTxt.setVisibility(View.VISIBLE);
                        petGuideEdit.setVisibility(View.VISIBLE);

                        pethashMap.put("개",false);
                        pethashMap.put("고양이",true);
                        pethashMap.put("기타",false);

                        nexthashMap.put("반려동물", true);

                        nullCheckforNextBtnActivate();

                        break;
//dd
                    //기타 버튼 행동
                    case R.id.pet3Btn:
                        Log.d(TAG, "=== pet3Btn ===" );
                        pet3Btn.setSelected(true);
                        pet3Btn.setTextColor(getResources().getColor(R.color.white));

                        pet4Btn.setSelected(false);
                        pet4Btn.setTextColor(getResources().getColor(R.color.black));

                        Log.d(TAG, "=== 반려동물 유의사항 입력란 나타남 ===" );
                        petGuideTxt.setVisibility(View.VISIBLE);
                        petGuideEdit.setVisibility(View.VISIBLE);

                        pethashMap.put("기타",true);
                        pethashMap.put("고양이",false);
                        pethashMap.put("기타",false);

                        nexthashMap.put("반려동물", true);

                        nullCheckforNextBtnActivate();

                        break;

                    //없음 버튼 행동
                    case R.id.pet4Btn:
                        Log.d(TAG, "=== pet4Btn ===" );
                        pet4Btn.setSelected(true);
                        pet4Btn.setTextColor(getResources().getColor(R.color.white));

                        pet1Btn.setSelected(false);
                        pet1Btn.setTextColor(getResources().getColor(R.color.black));
                        pet2Btn.setSelected(false);
                        pet2Btn.setTextColor(getResources().getColor(R.color.black));
                        pet3Btn.setSelected(false);
                        pet3Btn.setTextColor(getResources().getColor(R.color.black));

                        Log.d(TAG, "=== 반려동물 유의사항 입력란 사라짐 ===" );
                        petGuideTxt.setVisibility(View.GONE);
                        petGuideEdit.setVisibility(View.GONE);

                        //pethashMap 을 모두 false로 변경
                        pethashMap.put("개",false);
                        pethashMap.put("고양이",false);
                        pethashMap.put("기타",false);

                        nexthashMap.put("반려동물", true);

                        //안전 관련 주의사항 에디트 비우기
                        petGuideEdit.setText("");

                        nullCheckforNextBtnActivate();

                        break;
                }
            }
        };

        pet1Btn.setOnClickListener(onClickListener);
        pet2Btn.setOnClickListener(onClickListener);
        pet3Btn.setOnClickListener(onClickListener);
        pet4Btn.setOnClickListener(onClickListener);




        /* 영유아 있는지 확인 버튼 */
        Button.OnClickListener onClickListener2 = new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.childIsBtn:
                        Log.d(TAG, "=== 영유아 있음 버튼 클릭 ===" );
                        childIsBtn.setSelected(true);
                        childIsBtn.setTextColor(getResources().getColor(R.color.white));

                        childNotBtn.setSelected(false);
                        childNotBtn.setTextColor(getResources().getColor(R.color.black));

                        childBool = true;

                        nexthashMap.put("영유아", true);

                        nullCheckforNextBtnActivate();

                        break;

                        case R.id.childNotBtn:
                            Log.d(TAG, "=== 영유아 없음 버튼 클릭 ===" );
                            childIsBtn.setSelected(false);
                            childIsBtn.setTextColor(getResources().getColor(R.color.black));

                            childNotBtn.setSelected(true);
                            childNotBtn.setTextColor(getResources().getColor(R.color.white));
                            childBool = false;

                            nexthashMap.put("영유아", true);

                            nullCheckforNextBtnActivate();

                            break;

                }
            }
        };

        childIsBtn.setOnClickListener(onClickListener2);
        childNotBtn.setOnClickListener(onClickListener2);




        /* cctv 있는지 확인 버튼 */
        Button.OnClickListener onClickListener3 = new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.ctIsBtn:
                        Log.d(TAG, "=== cctv 있음 버튼 클릭 ===" );
                        ctIsBtn.setSelected(true);
                        ctIsBtn.setTextColor(getResources().getColor(R.color.white));

                        ctNotBtn.setSelected(false);
                        ctNotBtn.setTextColor(getResources().getColor(R.color.black));

                        cctvBool = true;

                        nexthashMap.put("CCTV", true);

                        nullCheckforNextBtnActivate();

                        break;

                    case R.id.ctNotBtn:
                        Log.d(TAG, "=== cctv 없음 버튼 클릭 ===" );
                        ctIsBtn.setSelected(false);
                        ctIsBtn.setTextColor(getResources().getColor(R.color.black));

                        ctNotBtn.setSelected(true);
                        ctNotBtn.setTextColor(getResources().getColor(R.color.white));

                        cctvBool = false;

                        nexthashMap.put("CCTV", true);

                        nullCheckforNextBtnActivate();

                        break;

                }
            }
        };

        ctIsBtn.setOnClickListener(onClickListener3);
        ctNotBtn.setOnClickListener(onClickListener3);


        /* 무료주차 가능 불가능 버튼 */
        Button.OnClickListener onClickListener4 = new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.parkingIsBtn:
                        Log.d(TAG, "=== 무료주차 가능 버튼 클릭 ===" );
                        parkingIsBtn.setSelected(true);
                        parkingIsBtn.setTextColor(getResources().getColor(R.color.white));

                        parkingNotBtn.setSelected(false);
                        parkingNotBtn.setTextColor(getResources().getColor(R.color.black));

                        Log.d(TAG, "=== 무료주차 주차안내 텍스트, 에디트 나타남 ===" );
                        parkingGuideTxt.setVisibility(View.VISIBLE);
                        parkingGuideEdit.setVisibility(View.VISIBLE);

                        parkingBool =true;

                        nexthashMap.put("무료주차", true);

                        nullCheckforNextBtnActivate();

                        break;

                    case R.id.parkingNotBtn:
                        Log.d(TAG, "=== 무료주차 불가능 버튼 클릭 ===" );
                        parkingIsBtn.setSelected(false);
                        parkingIsBtn.setTextColor(getResources().getColor(R.color.black));

                        parkingNotBtn.setSelected(true);
                        parkingNotBtn.setTextColor(getResources().getColor(R.color.white));

                        Log.d(TAG, "=== 무료주차 주차안내 텍스트, 에디트 사라짐 ===" );
                        parkingGuideTxt.setVisibility(View.GONE);
                        parkingGuideEdit.setVisibility(View.GONE);

                        parkingBool = false;

                        nexthashMap.put("무료주차", true);

                        //주차방법 에디트 비우기
                        parkingGuideEdit.setText("");

                        nullCheckforNextBtnActivate();

                        break;

                }
            }
        };

        parkingIsBtn.setOnClickListener(onClickListener4);
        parkingNotBtn.setOnClickListener(onClickListener4);

        /* volley 관련 변수. 요청 변수 생성 */
        queue = Volley.newRequestQueue(this);




        /* 저장 버튼 클릭 -> 반려동물 존재 여부, 영유야 존재 여부, cctv 존재 여부, 무료 주차 가능 여부, 집 명칭에 대한 정보를 서버로 전송 */

        saveBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Log.d(TAG, "=== saveBtn 클릭 : === ");
                //현재 입력한 데이터를 모두 받아서 저장함

                placeNameStr = placeNameEdit.getText().toString();

                petGuideStr = petGuideEdit.getText().toString();
                parkingGuideStr = parkingGuideEdit.getText().toString();

                //집 명칭이 비어있으면, 토스트메시지
                //반려동물에서 개, 고양이, 기타를 선택한 경우,

                    if(placeNameStr.isEmpty()){

                        Log.d(TAG, "=== 장소 이름 입력 안 한 경우 ===" );
                        Toast.makeText(getBaseContext(), "집 명칭을 입력해주세요.", Toast.LENGTH_SHORT).show();

                    }else if(petGuideStr.isEmpty() && (pethashMap.get("개") || pethashMap.get("고양이") || pethashMap.get("기타"))){

                        Log.d(TAG, "=== pethashMap === :"+ pethashMap );

                        Log.d(TAG, "=== 개, 고양이, 기타 모두 선택 한 경우 ===" );
                        Toast.makeText(getBaseContext(), "반려동물 관련 주의사항을 입력해주세요.", Toast.LENGTH_SHORT).show();


                    }else if(parkingGuideStr.isEmpty() && parkingBool) {

                        Log.d(TAG, "=== 주차 가능 경우 ===");
                        Toast.makeText(getBaseContext(), "주차 방법을 입력해주세요.", Toast.LENGTH_SHORT).show();

                    }else{
                        //Toast.makeText(getBaseContext(), "if문 종료 placeNameStr "+placeNameStr, Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "=== placeNameStr ===" +placeNameStr);
                        Log.d(TAG, "=== petGuideStr ===" +petGuideStr);
                        Log.d(TAG, "=== parkingGuideStr ===" +parkingGuideStr);

                        /* 서버로 데이터 전송 편하게 하기 위함. */
                        if(pethashMap.get("개")){
                            petDog = true;
                        }

                        if(pethashMap.get("고양이")){
                            petCat = true;
                        }

                        if(pethashMap.get("기타")){
                            petEtc = true;
                        }

                        postData();




                    }

                }

            });

    }

    /* 엑티비티 종료 후 volley 연결 끊기 위함 */
    @Override
    protected void onStop() {
        super.onStop();
        if (queue != null) {
            queue.cancelAll(TAG);
            Log.d(TAG, "=== 생명주기 onStop queue.cancelAll(TAG); 캔슬 === queue : "+queue);
        }
    }



    /* 서버로 데이터 전송하는 코드 volley */
    public void postData(){
        Log.d(TAG, "=== postData() ===" );
        // Request를 보낼 queue를 생성한다. 필요시엔 전역으로 생성해 사용하면 된다.
        RequestQueue queue = Volley.newRequestQueue(this);
        // 대표적인 예로 androidhive의 테스트 url을 삽입했다. 이부분을 자신이 원하는 부분으로 바꾸면 될 터
        String url = "http://52.79.179.66/insertPlace.php";

        final StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "=== stringRequest === onResponse" );

                Log.d(TAG, "=== postData() 완료 후, 장소 등록 이전 엑티비티로 이동 ===" );

                /* 장소등록엑티비티, 장소등록엑티비티2 삭제하기 위한 코드  4. finish() */
                aActivity.finish();
                bActivity.finish();

                finish();
                Log.d(TAG, "=== 장소 저장하는 엑티비티 종료 ===" );

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "=== stringRequest === error :" +error);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<String, String>();
                params.put("currentUser",GlobalApplication.currentUser);

                params.put("placeNameStr", placeNameStr);
                params.put("address", address);
                params.put("detailAddress", detailAddress);
                params.put("sizeStr", sizeStr);
                params.put("sizeIndexint", String.valueOf(sizeIndexint));

                params.put("petDog", String.valueOf(petDog));
                params.put("petCat", String.valueOf(petCat));
                params.put("petEtc", String.valueOf(petEtc));

                params.put("childBool", String.valueOf(childBool));
                params.put("cctvBool", String.valueOf(cctvBool));
                params.put("parkingBool", String.valueOf(parkingBool));
                params.put("petGuideStr", petGuideStr);
                params.put("parkingGuideStr", parkingGuideStr);

                Log.d(TAG, "=== params === : "+params );

                return params;
            }
        };

        stringRequest.setTag(TAG);

        queue.add(stringRequest);
    }





    public void nullCheckforNextBtnActivate(){
        /* 반려동물 외에 다른 버튼 입력했는지 안했는지 확인하기 위한 코드 try ~ catch
         * 했으면, 다음 버튼 활성화,
         * 안 했으면, 비활상태 유지 */
        try{

            if(nexthashMap.get("반려동물")==true
                    && nexthashMap.get("영유아")==true
                    && nexthashMap.get("CCTV")==true
                    && nexthashMap.get("무료주차")==true){

                Log.d(TAG, "=== 해당 조건 말고, 다른 버튼 모두 선택한 경우 -> 다음 버튼 활성화 ===" );

                Log.d(TAG, "=== 반려동물 클릭여부 === : "+ nexthashMap.get("반려동물") );
                Log.d(TAG, "=== 영유아 클릭여부 === : "+ nexthashMap.get("영유아") );
                Log.d(TAG, "=== CCTV 클릭여부 === : "+ nexthashMap.get("CCTV") );
                Log.d(TAG, "=== 무료주차 클릭여부 === : "+ nexthashMap.get("무료주차") );

                saveBtn.setEnabled(true);
                Log.d(TAG, "-> 다음 버튼 활성화 ===" );


            }else{

                Log.d(TAG, "=== 해당 조건 말고,  다른 버튼 입력 안 함. 다음 버튼 비활 상태 유지 ===" );

            }

        }catch (Exception e){

            Log.d(TAG, "=== 반려동물 외에 다른 버튼 입력 안 함. 다음 버튼 비활 상태 유지 === e :" +e);

            Log.d(TAG, "=== 반려동물 클릭여부 === : "+nexthashMap.get("반려동물") );
            Log.d(TAG, "=== 영유아 클릭여부 === : "+nexthashMap.get("영유아") );
            Log.d(TAG, "=== CCTV 클릭여부 === : "+nexthashMap.get("CCTV") );
            Log.d(TAG, "=== 무료주차 클릭여부 === : "+nexthashMap.get("무료주차") );

        }
    }



}