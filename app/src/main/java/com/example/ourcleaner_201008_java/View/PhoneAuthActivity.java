package com.example.ourcleaner_201008_java.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.nfc.Tag;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.PowerManager;
import android.telephony.SmsManager;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ourcleaner_201008_java.GenerateCertNumber;
import com.example.ourcleaner_201008_java.R;
import com.example.ourcleaner_201008_java.Receiver.Broadcast;

public class PhoneAuthActivity extends AppCompatActivity {

    private static final String TAG = "폰인증";
    Button sendBtn,nextBtn;
    EditText phoneNumEdit,authNumEdit;
    String phoneNumStr,authNumStr;
    Boolean sendCan, nextCan;
    String emailStr,nicknameStr; //TermsActivity에서 받아온 스트링값

    TextView time_counter; //시간을 보여주는 TextView

    CountDownTimer countDownTimer;
    final int MILLISINFUTURE = 15 * 1000; //총 시간 (숫자1 * 숫자2 => 숫자1 부분 숫자 60 => 60초. 1분 설정하고 싶으면, 숫자1에 60으로 설정할 것.)
    final int COUNT_DOWN_INTERVAL = 1000; //onTick 메소드를 호출할 간격 (1초)

    BroadcastReceiver broadcastReceiver;


    String SMSContents = "1234";//해당 스트링이랑 입력한 스트링이랑 같은지 확인 해야 함.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_auth);

        Log.d(TAG, "=== onCreate ===" );

        sendCan=false;
        nextCan=false;
        Log.d(TAG, "=== onCreate sendCan ===" +sendCan);
        Log.d(TAG, "=== onCreate nextCan ===" +nextCan);


        //로그인 엑티비티에서 회원 데이터 수신
        Intent intent = getIntent();
        Log.d(TAG, "약관동의 엑티비티에서 인텐트 받음 intent :"+intent);

        emailStr = intent.getStringExtra("email");
        Log.d(TAG, "약관동의 엑티비티에서 인텐트 받음 intent.email : "+ emailStr);

        nicknameStr = intent.getStringExtra("nickname");
        Log.d(TAG, "약관동의 엑티비티에서 인텐트 받음 intent.profile.getNickname() : "+ nicknameStr);


        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            Log.d(TAG, "=== sms전송을 위한 퍼미션 확인 ===" );

            // For device above MarshMallow
            boolean permission = getWritePermission();
            if(permission) {
                // If permission Already Granted
                // Send You SMS here
                Log.d(TAG, "=== 퍼미션 허용 ===" );
            }
        }
        else{
            // Send Your SMS. You don't need Run time permission
            Log.d(TAG, "=== 퍼미션 필요 없는 버전임 ===" );
        }











        phoneNumEdit=(EditText)findViewById(R.id.phoneNumEdit);
        phoneNumEdit.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                phoneNumStr=phoneNumEdit.getText().toString();
                if(phoneNumStr.length()>9){
                    sendCan=true;
                    Log.d(TAG, "=== sendCan === :" +sendCan );

                    sendBtn = (Button) findViewById(R.id.sendBtn);
                    if(sendCan){
                        Log.d(TAG, "=== sendCan === : " +sendCan);
                        sendBtn.setEnabled(true);

                    }else{
                        Log.d(TAG, "=== sendCan === : " +sendCan);
                        sendBtn.setEnabled(false);
                    }

                }else{
                    sendCan=false;
                    Log.d(TAG, "=== sendCan === :" +sendCan );
                }
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                phoneNumStr=phoneNumEdit.getText().toString();
            }

            @Override
            public void afterTextChanged(Editable editable) {
                phoneNumStr=phoneNumEdit.getText().toString();
            }});





        sendBtn = (Button) findViewById(R.id.sendBtn);
        sendBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                Log.d(TAG, "=== sendBtn 클릭 : === ");
                phoneNumEdit=(EditText)findViewById(R.id.phoneNumEdit);
                phoneNumEdit.setEnabled(false);
                Log.d(TAG, "=== phoneNumEdit 수정 불가능 ===" );

                phoneNumStr=phoneNumEdit.getText().toString();
                Log.d(TAG, "=== phoneNumStr ===" +phoneNumStr);

                // TODO: 2020-10-15 전화번호 형식 안 잡음. 그냥 글자 수로만 함. 10자 이상.
                if(phoneNumStr.length()>9 && phoneNumStr.length()<12){
                    Log.d(TAG, "=== 전화번호 9자리 넘는 경우 === phoneNumStr : " + phoneNumStr );

                    try {
                        Log.d(TAG, "=== 문자 전송 시작 ===" );

                        // 2020-10-15 랜덤 함수로 보내야 함. generatecertnumber 클래스 활용이 안 됨.

                        GenerateCertNumber generateCertNumber = new GenerateCertNumber();
                        SMSContents = generateCertNumber.excuteGenerate();

                        Log.d(TAG, "=== 랜덤 숫자 생성 === SMSContents :" +SMSContents);

                        sendCan=true;
                        Log.d(TAG, "=== sendCan === :" +sendCan );

                        //전송
                        SmsManager smsManager = SmsManager.getDefault();
                        smsManager.sendTextMessage(phoneNumStr, null, "[우리집 청소]의 인증번호는 "+SMSContents +"입니다.", null, null);
                        Log.d(TAG, "=== 문자 전송 완료 ===" );

                        countDownTimer();

                        // TODO: 2020-10-15 sms 서비스로 받는거 나중에 하기
                        // TODO: 2020-10-15 sms 입력 시간 필요함




                    } catch (Exception e) {
                        Log.d(TAG, "=== 문자 전송 실패 === 에러코드 e : "+e );
                        e.printStackTrace();

                        sendCan=false;
                        Log.d(TAG, "=== sendCan === :" +sendCan );
                    }



                }else if(phoneNumStr.length() == 0){
                    Log.d(TAG, "=== 전화번호 입력 안 함 ===" );
                    Toast.makeText(getBaseContext(), "전화번호를 입력해주세요.", Toast.LENGTH_SHORT).show();
                    sendCan=false;
                    Log.d(TAG, "=== sendCan === :" +sendCan );
                }else{
                    Log.d(TAG, "=== 전화번호 형식 아님 ===" );
                    Toast.makeText(getBaseContext(), "전화번호 형식을 확인해주세요.", Toast.LENGTH_SHORT).show();
                    sendCan=false;
                    Log.d(TAG, "=== sendCan === :" +sendCan );
                }



            }
        });

        authNumEdit=(EditText)findViewById(R.id.authNumEdit);
        //에디트 텍스트에 에디트 텍스트가 변화될 때 입력하는 리스너
        authNumEdit.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Log.d(TAG, "=== authNumEdit에 입력한 값이 변화 있을 때 ===" );
                authNumStr = authNumEdit.getText().toString();


                    Log.d(TAG, "=== nextCan === : " +nextCan);
                    if(authNumStr.length()==6 || nextCan){
                        Log.d(TAG, "=== 입력한 인증번호가 6자리인 경우 ===" );
                        nextCan=true;
                        Log.d(TAG, "=== nextCan === : " +nextCan);
                        nextBtn = (Button) findViewById(R.id.nextBtn);

                        if(nextCan){
                            Log.d(TAG, "=== nextCan === : " +nextCan);
                            nextBtn.setEnabled(true);
                            Log.d(TAG, "=== nextBtn클릭 가능 ===" );

                        }else{
                            Log.d(TAG, "=== nextCan === : " +nextCan);
                            nextBtn.setEnabled(false);
                            Log.d(TAG, "=== nextBtn클릭 안됨 ===" );
                        }


                    }else{
                        Log.d(TAG, "=== 입력한 인증번호가 6자리아닌 경우 ===" );
                        nextCan=false;
                        if(nextCan){
                            Log.d(TAG, "=== nextCan === : " +nextCan);
                            nextBtn.setEnabled(true);
                            Log.d(TAG, "=== nextBtn클릭 가능 ===" );

                        }else{
                            Log.d(TAG, "=== nextCan === : " +nextCan);
                            nextBtn.setEnabled(false);
                            Log.d(TAG, "=== nextBtn클릭 안됨 ===" );
                        }

                    }





            }

            @Override
            public void afterTextChanged(Editable editable) {

            }});



        nextBtn = (Button) findViewById(R.id.nextBtn);
        nextBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                Log.d(TAG, "=== nextBtn 클릭 : === ");

                authNumEdit=(EditText)findViewById(R.id.authNumEdit);
                authNumStr = authNumEdit.getText().toString();

                if(authNumStr.equals(SMSContents)){
                    Log.d(TAG, "=== 입력한 인증번호와 문자로 보낸 인증번호가 같은 경우 ===" );


                    Log.d(TAG, "=== authNumStr === : " + authNumStr);
                    Log.d(TAG, "=== SMSContents === : " +SMSContents);

                    //있으면 넘어감
                    Intent intent = new Intent(getApplicationContext(), NameinputActivity.class);

                    intent.putExtra("email",emailStr);
                    intent.putExtra("nickname",nicknameStr);
                    intent.putExtra("phoneNum", phoneNumStr);

                    Log.d(TAG, "=== email ===" +emailStr);
                    Log.d(TAG, "=== nickname ===" +nicknameStr);
                    Log.d(TAG, "=== phoneNum ===" +phoneNumStr);

                    startActivity(intent);

                    finish();


                }else{
                    Log.d(TAG, "=== 입력한 인증번호와 문자로 보낸 인증번호가 다름 ===" );
                    Toast.makeText(getBaseContext(), "인증번호를 확인해주세요.", Toast.LENGTH_SHORT).show();

                    Log.d(TAG, "=== authNumStr === : " + authNumStr);
                    Log.d(TAG, "=== SMSContents === : " +SMSContents);

                }
            }
        });














    }


    @Override
    protected void onResume() {
        super.onResume();
        //BroadCastReceiver 에 Action 등록
        Log.d(TAG, "=== onResume()  ===" );

        try{
            broadcastReceiver = new Broadcast();
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(Intent.ACTION_POWER_CONNECTED);
            intentFilter.addAction(Intent.ACTION_POWER_DISCONNECTED);
            this.registerReceiver(broadcastReceiver,intentFilter);
            Log.d(TAG+"브로드캐스트 리시버","onResume() 브로드캐스트리시버 등록됨");
        }catch (IllegalArgumentException e){
            e.printStackTrace();
            Log.d(TAG, "=== 브로드 캐스트 등록 안 됨 === 에러코드 : " +e);
        }


        // TODO: 2020-10-17 브로드 캐스트 class 파일 자체가 실행이 안 됨.  인증번호 보낸 문자를 받아오는 작업
//        IntentFilter intentFilter = new IntentFilter(Intent.ACTION_SCREEN_ON);
//        intentFilter.addAction(Intent.ACTION_SCREEN_OFF);
//        intentFilter.addAction(Intent.ACTION_BOOT_COMPLETED);
//        intentFilter.addAction("android.provider.Telephony.SMS_RECEIVED");
//
//        registerReceiver(broadcastReceiver, intentFilter);
//        Log.d(TAG+"브로드캐스트 리시버","브로드캐스트리시버 등록됨");
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();

        Log.d(TAG, "=== onDestroy() 브로드캐스트 해제===" );
        unregisterReceiver(broadcastReceiver);

        countDownTimer.cancel();

    }




    public void countDownTimer() { //카운트 다운 메소드

        time_counter = (TextView) findViewById(R.id.time_counter);
        //줄어드는 시간을 나타내는 TextView
        authNumEdit = (EditText) findViewById(R.id.authNumEdit);
        //사용자 인증 번호 입력창
        sendBtn = (Button) findViewById(R.id.sendBtn);
        //인증하기 버튼

        Log.d(TAG, "=== countDownTimer()메서드 시작 ===" );

        countDownTimer = new CountDownTimer(MILLISINFUTURE, COUNT_DOWN_INTERVAL) {
            @Override
            public void onTick(long millisUntilFinished) { //(300초에서 1초 마다 계속 줄어듬)

                Log.d(TAG, "=== onTick ===" );
                long smsAuthCount = millisUntilFinished / 1000;
                Log.d(TAG, "smsAuthCount : "+smsAuthCount);

                if ((smsAuthCount - ((smsAuthCount / 60) * 60)) >= 10) { //초가 10보다 크면 그냥 출력

                    time_counter.setText((smsAuthCount / 60) + " : " + (smsAuthCount - ((smsAuthCount / 60) * 60)));
                    Log.d(TAG, "=== 초가 10보다 크면 그냥 출력 === :" +(smsAuthCount / 60) + " : " + (smsAuthCount - ((smsAuthCount / 60) * 60)));

                } else { //초가 10보다 작으면 앞에 '0' 붙여서 같이 출력. ex) 02,03,04...

                    time_counter.setText((smsAuthCount / 60) + " : 0" + (smsAuthCount - ((smsAuthCount / 60) * 60)));
                    Log.d(TAG, "=== 초가 10보다 작으면 앞에 '0' 붙여서 같이 출력 === : " +(smsAuthCount / 60) + " : 0" + (smsAuthCount - ((smsAuthCount / 60) * 60)));

                }

                //emailAuthCount은 종료까지 남은 시간임. 1분 = 60초 되므로,
                // 분을 나타내기 위해서는 종료까지 남은 총 시간에 60을 나눠주면 그 몫이 분이 된다.
                // 분을 제외하고 남은 초를 나타내기 위해서는, (총 남은 시간 - (분*60) = 남은 초) 로 하면 된다.


                sendBtn.setEnabled(false);
                Log.d(TAG, "=== 인증번호 보내기 버튼 클릭 불가 ===" );

            }


            @Override
            public void onFinish() { //시간이 다 되면

                Log.d(TAG, "=== countDownTimer 에서 15초 다 됨 ===" );

                sendBtn.setEnabled(true);
                Log.d(TAG, "=== 인증번호 보내기 버튼 클릭가능 ===" );

                sendBtn.setText("재전송");
                Log.d(TAG, "=== sendBtn 재전송 버튼으로 변경 ===" );

                SMSContents = "1234";

                phoneNumEdit = (EditText)findViewById(R.id.phoneNumEdit);
                phoneNumEdit.setEnabled(true);
                Log.d(TAG, "=== phoneNumEdit 수정 가능 ===" );

                nextBtn = (Button) findViewById(R.id.nextBtn);
                nextBtn.setEnabled(false);
                nextCan = false;

                authNumEdit = (EditText)findViewById(R.id.authNumEdit);
                authNumEdit.setText("");

                Toast.makeText(getBaseContext(), "제한 시간이 초과되었습니다. 재전송 버튼을 눌러주세요.", Toast.LENGTH_SHORT).show();


            }
        }.start();

    }


    public boolean getWritePermission(){
        boolean hasPermission = (ContextCompat.checkSelfPermission(this,
                Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED);
        if (!hasPermission) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, 10);
        }
        return hasPermission;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode)
        {
            case 10: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    // Permission is Granted
                    // Send Your SMS here
                }
            }
        }
    }



}