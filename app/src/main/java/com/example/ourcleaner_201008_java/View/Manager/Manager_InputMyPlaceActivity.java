package com.example.ourcleaner_201008_java.View.Manager;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.example.ourcleaner_201008_java.DTO.AddressDTO;
import com.example.ourcleaner_201008_java.R;
import com.example.ourcleaner_201008_java.View.DaumWebViewActivity;

public class Manager_InputMyPlaceActivity extends AppCompatActivity {

    private static final String TAG = "매니저용내주소등록화면";

    private WebView daum_webView;
    //private TextView daum_result;
    private Handler handler;

    LinearLayout addressLayout;
    EditText placeEdit, placeDetailEdit;
    Button saveBtn;

    /* 서버에서 받아온 주소 데이터 담는 변수 */
    //bname1Str : ("동"지역일 경우에는 공백, "리"지역일 경우에는 "읍" 또는 "면" 정보가 들어갑니다.)
    String zonecodeStr="", addrStr="", buildingNameStr="", sidoStr="", bcodeStr="", bnameStr="", bname1Str="", bname2Str="";

    AddressDTO addressDTO, addressDTO2; //웹뷰에서 받아올 때, 저장할 때

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager__input_my_place);
        Log.d(TAG, "=== onCreate ===" );

        addressLayout= findViewById(R.id.addressLayout);
        placeEdit= findViewById(R.id.placeEdit);
        placeDetailEdit= findViewById(R.id.placeDetailEdit);
        saveBtn= findViewById(R.id.saveBtn);

        /* 주소입력하는 에디트텍스트 클릭 시, 웹 뷰생성 및 자바스크립트와 연결 */
        placeEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e(TAG, "=== placeEdit ===" );

                // WebView 초기화
                init_webView();

                addressLayout.setVisibility(View.GONE);

                // 핸들러를 통한 JavaScript 이벤트 반응
                handler = new Handler();
            }
        });

        /* 상세 주소 까지 써야 주소 저장 가능 함. */
        placeDetailEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(placeDetailEdit.getText().toString().length()>0 && placeEdit.getText().toString().length()>0){
                    saveBtn.setEnabled(true);
                }else{
                    saveBtn.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e(TAG, "=== saveBtn ===" );

                String placeDetailStr = placeDetailEdit.getText().toString();
                addressDTO2 = new AddressDTO(zonecodeStr, addrStr, buildingNameStr, sidoStr, bcodeStr, bnameStr, bname1Str, bname2Str, placeDetailStr);

                //있으면 넘어감
                Intent intent = new Intent();
                intent.putExtra("addressDTO2", addressDTO2);

                //있으면 넘어감
                setResult(RESULT_OK, intent);
                finish();

            }
        });



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
        daum_webView.addJavascriptInterface(new Manager_InputMyPlaceActivity.AndroidBridge(), "OurCleaner");


        // web client 를 chrome 으로 설정
        daum_webView.setWebChromeClient(new WebChromeClient());

        // webview url load. php 파일 주소
        daum_webView.loadUrl("http://52.79.179.66/daum_address.php");

    }


    final class AndroidBridge {
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