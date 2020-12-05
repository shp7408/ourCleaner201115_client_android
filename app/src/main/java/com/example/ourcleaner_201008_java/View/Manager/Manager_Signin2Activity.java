package com.example.ourcleaner_201008_java.View.Manager;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.ourcleaner_201008_java.GenerateCertNumber4;
import com.example.ourcleaner_201008_java.R;

import net.nurigo.java_sdk.api.Message;
import net.nurigo.java_sdk.exceptions.CoolsmsException;

import org.json.simple.JSONObject;
import org.junit.Test;
import org.junit.runner.Result;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class Manager_Signin2Activity extends AppCompatActivity {

    private static final String TAG = "매니저회원가입2전화인증2";

    /* 이전 엑티비티에서 intent로 받아온 값 */
    String phoneNumStr;

    TextView phonNumTxt, timeLimitTxt, timeAlertTxt, certResendTxt;
    EditText certNumEdit;
    Button nextBtn;

    /* 인증번호 4자리, 메인에서 비교하기 위함*/
    String certNumStr;

    /* 타이머 설정 위한 변수 */
    CountDownTimer countDownTimer;
    final int MILLISINFUTURE = 30 * 1000; //총 시간 (숫자1 * 숫자2 => 숫자1 부분 숫자 60 => 60초. 1분 설정하고 싶으면, 숫자1에 60으로 설정할 것.)
    final int COUNT_DOWN_INTERVAL = 1000; //onTick 메소드를 호출할 간격 (1초)

    int value;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager__signin2);
        Log.d(TAG, "=== onCreate ===" );

        //intent로 받을 때 사용하는 코드
        Intent intent = getIntent();

        phoneNumStr = intent.getStringExtra("phoneNumStr");
        Log.d(TAG, "휴대폰번호 입력 엑티비티에서 intent 받음 intent.phoneNumStr : "+ phoneNumStr);

        phonNumTxt = findViewById(R.id.phonNumTxt);
        phonNumTxt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                BackgroundTask backgroundTask = new BackgroundTask();
                backgroundTask.execute();
            }

            @Override
            public void afterTextChanged(Editable editable) {
                /* testSend 메서드 실행시키면, 메인스레드 오류뜸. 그래서 스레드로 해당 메서드 실행시킴 */
//                Thread thread = new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        try  {
//                            Log.e(TAG, "dddddddddddddddd : testSend");
//
//                            testSend();
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                            Log.e(TAG, "dddddddddddddddd : "+ e);
//                        }
//                    }
//                });
//                thread.start();
//                Handler mHandler = new Handler(Looper.getMainLooper());
//                mHandler.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        Log.e(TAG, "dddddddddddddddd : testSend");
//                        testSend();
//                    }
//
//                }, 0);
            }

        });
        phonNumTxt.setText(phoneNumStr+"로 받으신\n인증번호 4자리를 입력해주세요.");


        timeLimitTxt = findViewById(R.id.timeLimitTxt);
        timeAlertTxt = findViewById(R.id.timeAlertTxt);
        certResendTxt = findViewById(R.id.certResendTxt);

        certResendTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                phonNumTxt.setText(phoneNumStr+"로 받으신\n인증번호 4자리를 입력해주세요.");
                timeAlertTxt.setVisibility(View.GONE);
            }
        });

        certNumEdit = findViewById(R.id.certNumEdit);
        nextBtn = findViewById(R.id.nextBtn);
        certNumEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(certNumEdit.getText().toString().length()==4){
                    nextBtn.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "=== nextBtn 클릭 : === ");
                if(certNumEdit.getText().toString().equals(certNumStr)){
                    Log.e(TAG, "=== 에디트 텍스트에 입력한 값과, 보낼 때의 랜덤 번호가 같은 경우 ===" );

                    certNumEdit.setText("");
                    certNumStr=null;
                    timeAlertTxt.setVisibility(View.GONE);

                    //있으면 넘어감
                    Intent intent = new Intent(getApplicationContext(), Manager_Signin3Activity.class);

                    intent.putExtra("phoneNumStr",phoneNumStr);

                    startActivity(intent);

                    //finish();

                }else{
                    Log.e(TAG, "=== 다름 다름 ===" );

                    Toast.makeText(getApplicationContext(), "인증번호를 다시 확인해주세요.", Toast.LENGTH_SHORT).show();
                }
            }
        });



    }

    @Test
    public void testSend() {
        String api_key = "NCSFULYJ0NDRNTUZ";
        String api_secret = "CVON1XJNMJB7MFO8XYATBVK2ABXTSJFK";
        Message coolsms = new Message(api_key, api_secret);
        HashMap<String, String> params = new HashMap<String, String>();

        /* 인증번호 랜덤 4자리 생성 */
        GenerateCertNumber4 generateCertNumber4 = new GenerateCertNumber4();
        certNumStr = generateCertNumber4.excuteGenerate();
        Log.d(TAG, "=== certNumStr ===" +certNumStr);

        String phoneNumToString=phoneNumStr; //받는 사람
        String phoneNumFromString="01030517408"; //보내는 사람
        String SMSMessage="[청소 연구소] 인증번호  "+certNumStr+"  를 입력해주세요."; //4자리 인증번호

        params.put("to", phoneNumToString);
        params.put("from", phoneNumFromString); //사전에 사이트에서 번호를 인증하고 등록하여야 함
        params.put("type", "SMS");
        params.put("text", SMSMessage); //메시지 내용
        params.put("app_version", "test app 1.2");

        try {
            JSONObject jsonObject = coolsms.send(params);
            Log.d(TAG, "=== dddddddddddddddd jsonObject.toString() 결과 값 출력 ===" +jsonObject.toString());


       } catch (CoolsmsException e) {
            Log.d(TAG, "=== dddddddddddddddd 에러 메세지 출력 ===" +e.getMessage());
            Log.d(TAG, "=== dddddddddddddddd 에러 코드 출력 ===" +e.getCode());
        }
    }


    public void countDownTimer() { //카운트 다운 메소드
        Log.d(TAG, "=== countDownTimer()메서드 시작 ===" );
        timeLimitTxt.setVisibility(View.VISIBLE);
        certNumEdit.setFocusable(true);

        countDownTimer = new CountDownTimer(MILLISINFUTURE, COUNT_DOWN_INTERVAL) {
            @Override
            public void onTick(long millisUntilFinished) { //(300초에서 1초 마다 계속 줄어듬)

                Log.d(TAG, "=== onTick ===" );
                long smsAuthCount = millisUntilFinished / 1000;
                Log.d(TAG, "smsAuthCount : "+smsAuthCount);

                if ((smsAuthCount - ((smsAuthCount / 60) * 60)) >= 10) { //초가 10보다 크면 그냥 출력

                    timeLimitTxt.setText((smsAuthCount / 60) + " : " + (smsAuthCount - ((smsAuthCount / 60) * 60))+"내에 입력");
                    Log.d(TAG, "=== 초가 10보다 크면 그냥 출력 === :" +(smsAuthCount / 60) + " : " + (smsAuthCount - ((smsAuthCount / 60) * 60)));

                } else { //초가 10보다 작으면 앞에 '0' 붙여서 같이 출력. ex) 02,03,04...

                    timeLimitTxt.setText((smsAuthCount / 60) + " : 0" + (smsAuthCount - ((smsAuthCount / 60) * 60))+"내에 입력");
                    Log.d(TAG, "=== 초가 10보다 작으면 앞에 '0' 붙여서 같이 출력 === : " +(smsAuthCount / 60) + " : 0" + (smsAuthCount - ((smsAuthCount / 60) * 60)));

                }

                //emailAuthCount은 종료까지 남은 시간임. 1분 = 60초 되므로,
                // 분을 나타내기 위해서는 종료까지 남은 총 시간에 60을 나눠주면 그 몫이 분이 된다.
                // 분을 제외하고 남은 초를 나타내기 위해서는, (총 남은 시간 - (분*60) = 남은 초) 로 하면 된다.

            }


            @Override
            public void onFinish() { //시간이 다 되면

                Log.d(TAG, "=== countDownTimer 에서 15초 다 됨 ===" );

                timeAlertTxt.setVisibility(View.VISIBLE);
                Log.d(TAG, "=== timeAlertTxt 보임 ===" );

                certResendTxt.setVisibility(View.VISIBLE);
                Log.d(TAG, "=== certResendTxt 보임 ===" );

                timeLimitTxt.setVisibility(View.GONE);
                Log.d(TAG, "=== timeLimitTxt 안보임 ===" );

                certNumEdit.setText("");

                Log.e(TAG, "=== 제한시간이 다 되었습니다. ===" );
//                Toast.makeText(getBaseContext(), "제한 시간이 초과되었습니다. 재전송 버튼을 눌러주세요.", Toast.LENGTH_SHORT).show();
            }
        }.start();

    }


    //새로운 TASK정의 (AsyncTask)
    // < >안에 들은 자료형은 순서대로 doInBackground, onProgressUpdate, onPostExecute의 매개변수 자료형을 뜻한다.(내가 사용할 매개변수타입을 설정하면된다)
    class BackgroundTask extends AsyncTask<String,Void,String> {

        public String result;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.e(TAG, "=== onPreExecute ===" );
        }

        @Override
        protected String doInBackground(String... params) {
            Log.e(TAG, "=== doInBackground ===" );

            testSend();
            return result;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
            Log.e(TAG, "=== onProgressUpdate ===" );

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.e(TAG, "=== onPostExecute ===" );
            /* 메세지 전송에 성공하고나면, 카운트 다운 시작 */
            countDownTimer();

        }

    }
}



