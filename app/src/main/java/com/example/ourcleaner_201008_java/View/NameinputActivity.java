package com.example.ourcleaner_201008_java.View;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ourcleaner_201008_java.R;
import com.example.ourcleaner_201008_java.Server.InsertData;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class NameinputActivity extends AppCompatActivity {
    private static final String TAG = "이름입력엑티비티";

    String emailStr,nicknameStr,phoneNumStr; //PhoneAuthActivity에서 받아온 스트링값
    EditText nameEdit;
    Boolean nextCan = false;
    Button nextBtn;
    String server_url = "http://52.79.179.66/insertData.php";




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nameinput);

        Log.d(TAG, "=== onCreate ===" );

        //intent로 받을 때 사용하는 코드
        Intent intent = getIntent();
        Log.d(TAG, "휴대폰 인증 엑티비티에서 인텐트 받음 intent :"+intent);

        emailStr = intent.getStringExtra("email");
        Log.d(TAG, "휴대폰 인증 엑티비티에서 인텐트 받음 intent.email : "+ emailStr);

        nicknameStr = intent.getStringExtra("nickname");
        Log.d(TAG, "휴대폰 인증 엑티비티에서 인텐트 받음 intent.profile.getNickname() : "+ nicknameStr);

        phoneNumStr = intent.getStringExtra("phoneNum");
        Log.d(TAG, "휴대폰 인증 엑티비티에서 인텐트 받음 intent.phoneNum : "+ phoneNumStr);

        nameEdit = (EditText)findViewById(R.id.nameEdit);
        if(isKorean(nicknameStr)){
            nameEdit.setText(nicknameStr);
            Log.d(TAG, "=== nicknameStr이 한글임 ===" );
            nextCan=true;
            Log.d(TAG, "=== nextCan === 다음으로 넘어갈 수 있으면 true / nextCan :"+nextCan );

            if(nextCan){
                nextBtn = (Button) findViewById(R.id.nextBtn);
                nextBtn.setEnabled(true);
            }

        }else{
            Log.d(TAG, "=== nicknameStr이 한글아님 ===" );
            Log.d(TAG, "=== nextCan === 다음으로 넘어갈 수 있으면 true / nextCan :"+nextCan );
        }

        //에디트 텍스트에 에디트 텍스트가 변화될 때 입력하는 리스너
        nameEdit.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                nicknameStr = nameEdit.getText().toString();
                if(isKorean(nicknameStr)){

                    Log.d(TAG, "=== 한글입니다. ===" );
                    nextCan=true;
                    Log.d(TAG, "=== nextCan === 다음으로 넘어갈 수 있으면 true / nextCan :"+nextCan );
                    if(nextCan){
                        nextBtn = (Button) findViewById(R.id.nextBtn);
                        nextBtn.setEnabled(true);
                        Log.d(TAG, "=== nextBtn 클릭 가능 ===" );
                    }

                }else{

                    Log.d(TAG, "=== 한글아닙니다. ===" );
                    Log.d(TAG, "=== nextCan === 다음으로 넘어갈 수 있으면 true / nextCan :"+nextCan );
                    nextCan=false;
                    nextBtn.setEnabled(false);
                    Log.d(TAG, "=== nextBtn 클릭 불가 ===" );

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

                    InsertData task = new InsertData();
                    task.execute(server_url, emailStr, nicknameStr, phoneNumStr);
                    Log.d(TAG, "=== insert data 완료 ===" );



                    Log.d(TAG, "=== 메인으로 이동함 ===" );

                    //있으면 넘어감
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);

                    intent.putExtra("email",emailStr);
                    intent.putExtra("nickname",nicknameStr);
                    intent.putExtra("phoneNum", phoneNumStr);

                    Log.d(TAG, "=== email ===" +emailStr);
                    Log.d(TAG, "=== nickname ===" +nicknameStr);
                    Log.d(TAG, "=== phoneNum ===" +phoneNumStr);

                    startActivity(intent);

                    finish();

                    }

            });



    }
    public boolean isKorean(String str) {
        return str.matches("^[가-힣ㄱ-ㅎㅏ-ㅣ]*$");
    }




}