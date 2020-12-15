package com.example.ourcleaner_201008_java.View;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.TextView;

import com.example.ourcleaner_201008_java.R;

public class DaumWebViewActivity extends AppCompatActivity {

    private WebView daum_webView;
    //private TextView daum_result;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daum_web_view);

//        daum_webview = (TextView) findViewById(R.id.daum_webview);

        // WebView 초기화
        init_webView();

        // 핸들러를 통한 JavaScript 이벤트 반응
        handler = new Handler();

    }


    public void init_webView() {
        // WebView 설정
        daum_webView = (WebView) findViewById(R.id.daum_webview);

        // JavaScript 허용
        daum_webView.getSettings().setJavaScriptEnabled(true);

        // JavaScript의 window.open 허용
        daum_webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);

        // JavaScript이벤트에 대응할 함수를 정의 한 클래스를 붙여줌
        daum_webView.addJavascriptInterface(new AndroidBridge(), "OurCleaner");


        // web client 를 chrome 으로 설정
        daum_webView.setWebChromeClient(new WebChromeClient());

        // webview url load. php 파일 주소
        daum_webView.loadUrl("http://52.79.179.66/daum_address.php");

    }


    private class AndroidBridge {

        private static final String TAG = "AndroidBridge";

        @JavascriptInterface //젤리빈 이상인 경우, 추가해야 함.
        public void setAddress(final String arg1) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    //daum_result.setText(String.format("(%s) %s %s", arg1, arg2, arg3));

                    // WebView를 초기화 하지않으면 재사용할 수 없음
                    init_webView();

                    Log.d(TAG, "=== AndroidBridge ===" );
                    Log.d(TAG, "=== 입력한 주소 postcode ===" +String.format("%s", arg1));
//                    Log.d(TAG, "=== 입력한 주소 address ===" +String.format("%s", arg2));
//                    Log.d(TAG, "=== 입력한 주소 bcode ===" +String.format("%s", arg3));
//                    Log.d(TAG, "=== 입력한 주소 bname ===" +String.format("%s", arg4));
//                    Log.d(TAG, "=== 입력한 주소 bname1 ===" +String.format("%s", arg5));
//                    Log.d(TAG, "=== 입력한 주소 bname2 ===" +String.format("%s", arg6));


                    //있으면 넘어감
//                    Intent intent = new Intent(getApplicationContext(), PlaceinputActivity.class);
//                    intent.putExtra("postcode", String.format("%s", arg1));
//                    intent.putExtra("address", String.format("%s", arg2));
//                    intent.putExtra("bcode", String.format("%s", arg3));
//                    intent.putExtra("bname", String.format("%s", arg4));
//                    intent.putExtra("bname1", String.format("%s", arg5));
//                    intent.putExtra("bname2", String.format("%s", arg6));
//                    setResult(RESULT_OK, intent);
//                    finish();

                }
            });
        }

    }
}
