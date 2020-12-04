package com.example.ourcleaner_201008_java.View.Manager;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.ourcleaner_201008_java.R;
import com.example.ourcleaner_201008_java.Server.CoolsmsUnitTest;
import com.example.ourcleaner_201008_java.Server.RetrieveTestTask;

import net.nurigo.java_sdk.api.Message;
import net.nurigo.java_sdk.exceptions.CoolsmsException;

import org.json.simple.JSONObject;
import org.junit.Test;

import java.util.HashMap;

public class Manager_Signin2Activity extends AppCompatActivity {

    private static final String TAG = "매니저회원가입2전화인증2";

    /* 이전 엑티비티에서 intent로 받아온 값 */
    String phoneNumStr;

    TextView phonNumTxt, timeLimitTxt, timeAlertTxt, certResendTxt;
    EditText certNumEdit;
    Button nextBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager__signin2);
        Log.d(TAG, "=== onCreate ===" );

        //intent로 받을 때 사용하는 코드
        Intent intent = getIntent();

        phoneNumStr = intent.getStringExtra("phoneNumStr");
        Log.d(TAG, "휴대폰번호 입력 엑티비티에서 intent 받음 intent.phoneNumStr : "+ phoneNumStr);



        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try  {
                    Log.e(TAG, "dddddddddddddddd : testSend");

                    testSend();
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e(TAG, "dddddddddddddddd : "+ e);
                }
            }
        });
        thread.start();


    }

    @Test
    public void testSend() {
        String api_key = "NCSFULYJ0NDRNTUZ";
        String api_secret = "CVON1XJNMJB7MFO8XYATBVK2ABXTSJFK";
        Message coolsms = new Message(api_key, api_secret);
        HashMap<String, String> params = new HashMap<String, String>();

        String phoneNumToInt="01030517408"; //받는 사람
        String phoneNumFromInt="01030517408"; //보내는 사람
        String certNum4Int="0000"; //4자리 인증번호

        params.put("to", phoneNumToInt);
        params.put("from", phoneNumFromInt); //사전에 사이트에서 번호를 인증하고 등록하여야 함
        params.put("type", "SMS");
        params.put("text", certNum4Int); //메시지 내용
        params.put("app_version", "test app 1.2");
        try {
            JSONObject jsonObject = coolsms.send(params);
            System.out.println(jsonObject.toString()); //전송 결과 출력
        } catch (CoolsmsException e) {
            System.out.println(e.getMessage());
            System.out.println(e.getCode());
        }
    }

}