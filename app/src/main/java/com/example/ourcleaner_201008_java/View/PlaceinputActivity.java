package com.example.ourcleaner_201008_java.View;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.example.ourcleaner_201008_java.DTO.AddressDTO;
import com.example.ourcleaner_201008_java.R;
import com.example.ourcleaner_201008_java.View.Manager.Manager_InputMyPlaceActivity;

public class PlaceinputActivity extends AppCompatActivity {

    private static final String TAG = "장소 입력";
    public static final int REQUEST_CODE_PLACE_INPUT = 1000;


    private WebView daum_webView;
    //private TextView daum_result;
    private Handler handler;

    EditText placeEdit;
    String address;
    Button nextBtn;

    LinearLayout addressLayout;

    /* 서버에서 받아온 주소 데이터 담는 변수 */
    //bname1Str : ("동"지역일 경우에는 공백, "리"지역일 경우에는 "읍" 또는 "면" 정보가 들어갑니다.)
    String zonecodeStr="", addrStr="", buildingNameStr="", sidoStr="", bcodeStr="", bnameStr="", bname1Str="", bname2Str="";

    AddressDTO addressDTO, addressDTO2; //웹뷰에서 받아올 때, 저장할 때



    /* 저장 후, 현재 엑티비티 종료 위함 */
    public static Activity AActivity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_placeinput);

        Log.d(TAG, "=== onCreate ===" );

        /* 저장 후, 현재 엑티비티 종료 위함 */
        AActivity = PlaceinputActivity.this;
        addressLayout= findViewById(R.id.addressLayout);

        placeEdit = findViewById(R.id.placeEdit);

        nextBtn = findViewById(R.id.nextBtn);
//        placeEdit.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View view, MotionEvent motionEvent) {
//
//                if(motionEvent.getAction() == MotionEvent.ACTION_DOWN){
//                    //클릭했을 경우 발생할 이벤트 작성
//                    Log.e(TAG, "=== placeEdit.setOnTouchListener ===" );
//                    Intent intent = new Intent(getApplicationContext(), DaumWebViewActivity.class);
//                    //데이터 넘겨받기 위함
//                    startActivityForResult(intent, REQUEST_CODE_PLACE_INPUT );
//                }
//
//                return false;
//
//            }
//        });

        placeEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                nextBtn.setEnabled(true);
                Log.d(TAG, "=== nextBtn.setEnabled(true); ===" );
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e(TAG, "=== nextBtn onClick ===" );

                addressDTO2 = new AddressDTO(zonecodeStr, addrStr, buildingNameStr, sidoStr, bcodeStr, bnameStr, bname1Str, bname2Str);

                Intent intent = new Intent(getApplicationContext(), Placeinput2Activity.class);

                intent.putExtra("addressDTO2 no detail", addressDTO2);

                startActivity(intent);


//                //있으면 넘어감
//                setResult(RESULT_OK, intent);
//                finish();



            }
        });

        placeEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // WebView 초기화
                init_webView();

                addressLayout.setVisibility(View.GONE);

                // 핸들러를 통한 JavaScript 이벤트 반응
                handler = new Handler();
                
            }
        });

    };






//    //요청코드 받을 시 발생하는 이벤트
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        //요청 코드 받았을 때 발생하는 이벤트
//        if (requestCode == REQUEST_CODE_PLACE_INPUT) {
//            //회원가입 요청 코드 받기 성공
//            if (resultCode == RESULT_OK) {
//                Log.d(TAG, "=== RESULT_OK ===" );
//
//                //intent로 받을 때 사용하는 코드
//                Intent intent = getIntent();
//                Log.d(TAG, "다음 주소 찾기 엑티비티에서 인텐트 받음 intent :"+intent);
//
//                address = data.getStringExtra("address"); //onActivityResult메서드의 파라미터
//                Log.d(TAG, "다음 주소 찾기 엑티비티에서 인텐트 받음 intent.address : "+ address);
//
//                placeEdit = (EditText)findViewById(R.id.placeEdit);
//                placeEdit.setText(address);
//                Log.d(TAG, "=== 받아온 주소 에디트 텍스트에 넣음 ===" );
//
//                Intent intent2 = new Intent(getApplicationContext(), Placeinput2Activity.class);
//                intent2.putExtra("address", address);
//
//                intent2.addFlags(intent.FLAG_ACTIVITY_CLEAR_TOP);
//                Log.d(TAG, "=== FLAG_ACTIVITY_CLEAR_TOP ===" );
//
//                startActivity(intent2);
//                finish();
//
//
//            } else {
//                Log.d(TAG, "=== RESULT_OK 못 받음 ===" );
//            }
//
//        }
//    }//onActivityResult 종료
//

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Log.d(TAG, "=== onBackPressed 현재 엑티비티 종료 ===" );
        finish();
    }

    public void init_webView() {
        // WebView 설정 및 등장
        daum_webView = (WebView) findViewById(R.id.daum_webview);
        daum_webView.setVisibility(View.VISIBLE);

        // JavaScript 허용
        daum_webView.getSettings().setJavaScriptEnabled(true);

        // JavaScript의 window.open 허용
        daum_webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);

        // JavaScript이벤트에 대응할 함수를 정의 한 클래스를 붙여줌
        daum_webView.addJavascriptInterface(new AndroidBridge2(), "OurCleaner");


        // web client 를 chrome 으로 설정
        daum_webView.setWebChromeClient(new WebChromeClient());

        // webview url load. php 파일 주소
        daum_webView.loadUrl("http://52.79.179.66/daum_address.php");

    }

    final class AndroidBridge2 {
        private static final String TAG = "AndroidBridge";
        @JavascriptInterface //젤리빈 이상인 경우, 추가해야 함.
        public void setAddress(final String zonecode, String addr, String buildingName, String sido, String bcode, String bname, String bname1, String bname2) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    //daum_result.setText(String.format("(%s) %s %s", arg1, arg2, arg3));

                    // WebView를 초기화 하지않으면 재사용할 수 없음
                    init_webView();

                    Log.d(TAG, "=== AndroidBridge ===" );
                    Log.d(TAG, "=== 입력한 주소 zonecode ===" +String.format("%s", zonecode));
                    Log.d(TAG, "=== 입력한 주소 addr ===" +String.format("%s", addr));
                    Log.d(TAG, "=== 입력한 주소 buildingName ===" +String.format("%s", buildingName));
                    Log.d(TAG, "=== 입력한 주소 sido ===" +String.format("%s", sido));
                    Log.d(TAG, "=== 입력한 주소 sido ===" +String.format("%s", bcode));
                    Log.d(TAG, "=== 입력한 주소 bname ===" +String.format("%s", bname));
                    Log.d(TAG, "=== 입력한 주소 bname1 ===" +String.format("%s", bname1));
                    Log.d(TAG, "=== 입력한 주소 bname2 ===" +String.format("%s", bname2));

                    daum_webView.setVisibility(View.GONE);
                    addressLayout.setVisibility(View.VISIBLE);

//                    String zonecodeStr, addrStr, buildingNameStr, sidoStr, bcodeStr, bnameStr, bname1Str, bname2Str;

                    zonecodeStr=String.format("%s", zonecode);
                    addrStr=String.format("%s", addr);
                    buildingNameStr=String.format("%s", buildingName);
                    sidoStr=String.format("%s", sido);
                    bcodeStr=String.format("%s", bcode);
                    bnameStr=String.format("%s", bname);
                    bname1Str=String.format("%s", bname1);
                    bname2Str=String.format("%s", bname2);

                    //addressDTO에 데이터 넣어서 관리하기
                    addressDTO = new AddressDTO(zonecodeStr, addrStr, buildingNameStr, sidoStr, bcodeStr, bnameStr, bname1Str, bname2Str);

                    placeEdit.setText("["+addressDTO.getZonecodeStr()+"] "+ addressDTO.getAddrStr());
                }
            });
        }
    }
}