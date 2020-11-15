package com.example.ourcleaner_201008_java.View;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ourcleaner_201008_java.Dialog.CustomDialog;
import com.example.ourcleaner_201008_java.GlobalApplication;
import com.example.ourcleaner_201008_java.R;
import com.example.ourcleaner_201008_java.SharedP.PreferenceManager_Auto;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class TermsActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener {
    private static final String TAG = "약관 동의";



    CheckBox checkBox_all, checkbox_1, checkbox_2, checkbox_3, checkbox_4, checkbox_5, checkbox_6;
    String emailStr,nicknameStr; //loginactivity에서 받아온 스트링값
    Button nextBtn;
    Boolean nextCan;
    Button btn_all;


//    String server_url = "http://52.79.179.66/emailCheck.php";
////    getdata 할 때 사용하는 변수임
//    private String mJsonString;
//    //서버에서 받아온 json에서 추출한 email
//    String JsonEmail=null;
//    private static final String TAG_JSON="ourCleaner";
//    private static final String TAG_EMAIL = "email";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms);
        Log.d(TAG, "=== onCreate 실행 ===");



        Log.d(TAG, "=== get data 완료 ===" );

        Log.d(TAG, "=== 레이아웃의 요소들 연결 ===");
        checkBox_all= (CheckBox)findViewById(R.id.checkbox_all);
        checkbox_1= (CheckBox)findViewById(R.id.checkbox_1);
        checkbox_2= (CheckBox)findViewById(R.id.checkbox_2);
        checkbox_3= (CheckBox)findViewById(R.id.checkbox_3);
        checkbox_4= (CheckBox)findViewById(R.id.checkbox_4);
        checkbox_5= (CheckBox)findViewById(R.id.checkbox_5);
        checkbox_6= (CheckBox)findViewById(R.id.checkbox_6);

        checkBox_all.setOnCheckedChangeListener(this);

        nextBtn = (Button)findViewById(R.id.nextBtn);

        btn_all = (Button) findViewById(R.id.btn_all);
        btn_all.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                    Log.d(TAG, "=== btn_all 클릭 : === ");

                    Log.d(TAG, "=== btn_all 클릭 후, 더보기 ===" );

                    //있으면 넘어감
                    Intent intent = new Intent(getApplicationContext(),TermsMoreActivity .class);

                    startActivity(intent);

                    //finish();

                    }
            });

        //로그인 엑티비티에서 회원 데이터 수신
        Intent intent = getIntent();
        Log.d(TAG, "로그인 엑티비티에서 인텐트 받음 intent :"+intent);

        emailStr = intent.getStringExtra("email");
        Log.d(TAG, "로그인 엑티비티에서 인텐트 받음 intent.email : "+ emailStr);

        nicknameStr = intent.getStringExtra("nickname");
        Log.d(TAG, "로그인 엑티비티에서 인텐트 받음 intent.profile.getNickname() : "+ nicknameStr);


//        InsertData task = new InsertData();
//        task.execute(server_url, email);
//        Log.d(TAG, "=== insert data 완료 ===" );








        // 커스텀 다이얼로그에서 입력한 메시지를 출력할 TextView 를 준비한다. https://sharp57dev.tistory.com/10
        final TextView main_label = (TextView) findViewById(R.id.main_label);

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "=== 다음 버튼 클릭 ===" );
                if(checkbox_1.isChecked()&&checkbox_2.isChecked()&&checkbox_3.isChecked()&&checkbox_4.isChecked()&&checkbox_5.isChecked()){

                    Log.d(TAG, "=== 필수 버튼 모두 선택 ===" );
                    Log.d(TAG, "=== 휴대폰 인증 화면으로 이동 ===" );



                    //없으면 폰 인증 엑티비티로
                    Intent intent = new Intent(getApplicationContext(), PhoneAuthActivity.class);

                    intent.putExtra("email", emailStr);
                    Log.d(TAG, "=== 메인으로 넘어갈 때의 email ===" +emailStr);
                    intent.putExtra("nickname", nicknameStr);
                    Log.d(TAG, "===  메인으로 넘어갈 때의 nickname ===" +nicknameStr);
                    startActivity(intent);

                    finish();


                }else{
                    Log.d(TAG, "=== 필수 버튼 중 하나라도 선택 안되어 있는 경우 ===" );


//                    CustomDialog customDialog = new CustomDialog(TermsActivity.this);
//
//                    // 커스텀 다이얼로그를 호출한다.
//                    // 커스텀 다이얼로그의 결과를 출력할 TextView를 매개변수로 같이 넘겨준다.
//                    customDialog.callFunction(main_label);

                    Toast.makeText(getBaseContext(), "필수 항목에 동의해주세요.", Toast.LENGTH_SHORT).show();


                }
            }
        });



    }

    // 체크박스를 클릭해서 상태가 바꾸었을 경우 호출되는 콜백 메서드
    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        // 체크박스를 클릭해서 상태가 바꾸었을 경우 호출되는 콜백 메서드
        Log.d(TAG, "=== 체크박스를 클릭해서 상태가 바꾸었을 경우 호출되는 콜백 메서드 ===" );

// TODO: 2020-10-15 선택 부분 예외처리 안 함.
        if(checkBox_all.isChecked()) {

            checkbox_1.setChecked(true);
            checkbox_2.setChecked(true);
            checkbox_3.setChecked(true);
            checkbox_4.setChecked(true);
            checkbox_5.setChecked(true);
            checkbox_6.setChecked(true);

                nextBtn.setEnabled(true);
                Log.d(TAG, "=== nextBtn클릭 가능 ===" );

//            nextCan=true;
//            Log.d(TAG, "=== nextCan === : " +nextCan);
//
//            if(nextCan){
//                Log.d(TAG, "=== nextCan === : " +nextCan);
//                nextBtn.setEnabled(true);
//                Log.d(TAG, "=== nextBtn클릭 가능 ===" );
//
//            }else{
//                Log.d(TAG, "=== nextCan === : " +nextCan);
//                nextBtn.setEnabled(false);
//                Log.d(TAG, "=== nextBtn클릭 안됨 ===" );
//            }


        }else{
            checkbox_1.setChecked(false);
            checkbox_2.setChecked(false);
            checkbox_3.setChecked(false);
            checkbox_4.setChecked(false);
            checkbox_5.setChecked(false);
            checkbox_6.setChecked(false);
        }




    }


//
//
//    public class GetData extends AsyncTask<String, Void, String>{
//
//        String errorString = null;
//
//        private static final String TAG = "겟 데이터";
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//            Log.d(TAG, "=== GetData onPreExecute ===");
//
//        }
//
//
//        @Override
//        protected void onPostExecute(String result) {
//            super.onPostExecute(result);
//            Log.d(TAG, "=== GetData onPostExecute === ");
//
//            Log.d(TAG, "onPostExecute response - " + result);
//
//            if (result == null){
//                Log.d(TAG, "result 널인 경우 errorString 에러코드 : "+ errorString);
//
//            }
//            else {
//                Log.d(TAG, "result가 값이 있음 result : "+ result);
//
//                mJsonString = result;
//                Log.d(TAG, "result가 값이 있음 mJsonString : "+ mJsonString);
//
//                showResult();
//
//            }
//        }
//
//
//        @Override
//        protected String doInBackground(String... params) {
//
//            Log.d(TAG, "=== onPostExecute doInBackground ===");
//
//            String serverURL = params[0];
//            String postParameters1 = "email=" + params[1];
//
//            Log.d(TAG, "onPostExecute doInBackground serverURL : " + params[0]);
//            Log.d(TAG, "onPostExecute doInBackground postParameters1 : " + postParameters1);
//            Log.d(TAG, "onPostExecute doInBackground params[1] : " + params[1]);
//
//
//            try {
//
//                Log.d(TAG, "try 부분 시작");
//
//                URL url = new URL(serverURL);
//                Log.d(TAG, "url : " + url);
//
//                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
//                Log.d(TAG, "httpURLConnection : ");
//
//
//                Log.d(TAG, "try 부분 시작 serverURL : " + params[0]);
//                Log.d(TAG, "try 부분 시작 postParameters1 : " + postParameters1);
//                Log.d(TAG, "try 부분 시작 params[1] : " + params[1]);
//
//
//                httpURLConnection.setReadTimeout(5000);
//                httpURLConnection.setConnectTimeout(5000);
//                httpURLConnection.setRequestMethod("POST");
//                httpURLConnection.setDoInput(true);
//                httpURLConnection.connect();
//
//                OutputStream outputStream = httpURLConnection.getOutputStream();
//                Log.d(TAG, "outputStream :"+outputStream);
//
//                outputStream.write(postParameters1.getBytes("UTF-8"));
//                Log.d(TAG, "postParameters1.getBytes(\"UTF-8\") :"+ postParameters1.getBytes("UTF-8"));
//
//
//                outputStream.flush();
//
//                outputStream.close();
//
//                int responseStatusCode = httpURLConnection.getResponseCode();
//
//                InputStream inputStream;
//
//                if(responseStatusCode == HttpURLConnection.HTTP_OK) {
//                    Log.d(TAG, "응답 성공 responseStatusCode :" + responseStatusCode);
//
//                    inputStream = httpURLConnection.getInputStream();
//                }
//                else{
//                    Log.d(TAG, "응답 실패 responseStatusCode 에러코드 :" + responseStatusCode);
//
//                    inputStream = httpURLConnection.getErrorStream();
//                }
//
//
//                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
//
//                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
//
//                StringBuilder sb = new StringBuilder();
//
//                String line;
//
//
//                while((line = bufferedReader.readLine()) != null){
//                    Log.d(TAG, "=== while문 반복 시작 line에다가 bufferedReader.readLine()을 넣고, 이 값이 널이 아닐 때, ===" );
//
//                    sb.append(line);
//
//                }
//                Log.d(TAG, "=== while문 반복 종료 ===" );
//                bufferedReader.close();
//                Log.d(TAG, "bufferedReader.close()" );
//
//                return sb.toString().trim();
//
//
//            } catch (Exception e) {
//                Log.d(TAG, "제이슨 받아오는 과정에서 에러 발생");
//                Log.d(TAG, "InsertData: Error ", e);
//                errorString = e.toString();
//                Log.d(TAG, "errorString : "+ errorString);
//
//                return null;
//            }
//
//        }
//    }
//
//    private void showResult() {
//
//        Log.d(TAG, "=== showResult 시작 ===" );
//
//        Log.d(TAG, "TAG_JSON : "+TAG_JSON);
//        Log.d(TAG, "TAG_EMAIL : "+TAG_EMAIL);
//
//        Log.d(TAG, "mJsonString : "+mJsonString);
//
//        try {
//
//            JSONObject jsonObject = new JSONObject(mJsonString);
//           // JSONObject jsonObject = new JSONObject(mJsonString.substring(mJsonString.indexOf("{"),mJsonString.lastIndexOf("}") +1));
//            JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);
//
//            Log.d(TAG, "jsonObject : "+jsonObject);
//            Log.d(TAG, "jsonArray : "+jsonArray);
//
//            for(int i=0;i<jsonArray.length();i++){
//                Log.d(TAG, "=== jsonArray.length() 만큼 반복문 시작 :  ===" );
//                Log.d(TAG, "jsonArray.length() : "+jsonArray.length());
//
//                JSONObject item = jsonArray.getJSONObject(i);
//                Log.d(TAG, "item : "+item);
//
//                JsonEmail = item.getString(TAG_EMAIL);
//
//                Log.d(TAG, "item.getString(TAG_EMAIL) : "+item.getString(TAG_EMAIL));
//                Log.d(TAG, "서버에서 받아온 결과 not_exist이면, 회원 디비에 없다는 것 임 JsonEmail  : "+JsonEmail);
//
//
//            }
//
//
//            Log.d(TAG, "반복문 종료 서버에서 받아온 결과 not_exist이면, 회원 디비에 없다는 것 임 Jsonemail  : "+JsonEmail);
//
//            Log.d(TAG, "=== jsonArray.length() 만큼 반복문 종료 :  ===" );
//
//
//        } catch (JSONException e) {
//
//            Log.d(TAG, "=== 받아온 제이슨 보여주는 과정에서 에러 발생 ===");
//            Log.d(TAG, "showResult : ", e);
//
//        }
//
//
//
//
//
//
//
//        if(JsonEmail==null){
//
//            Log.d(TAG, "=== JsonEmail이 없음. 서버에서 받아온 것이 없음 ===" );
//
//        }else if(JsonEmail.equals("not_exist")){
//
//            Log.d(TAG, "=== JsonEmail not_exist equals 같음 JsonEmail : " + JsonEmail );
//
//            Log.d(TAG, "=== 현재 엑티비티 진행 ===" );
//
//        }else{
//            Log.d(TAG, "=== JsonEmail 회원디비에 로그인한 이메일이 있음 JsonEmail : "+JsonEmail );
//
//            Log.d(TAG, "=== 약관동의-> 메인엑티비티로 ===" );
//
//            //있으면 넘어감
//            Intent intent2 = new Intent(getApplicationContext(), MainActivity.class);
//
//            intent2.putExtra("email", JsonEmail);
//            Log.d(TAG, "=== 메인으로 넘어갈 때의 email ===" +JsonEmail);
//            intent2.putExtra("nickname", nicknameStr);
//            Log.d(TAG, "===  메인으로 넘어갈 때의 nickname ===" +nicknameStr);
//
//            startActivity(intent2);
//
//            finish();
//
//        }
//
//
//
//
//
//
//
//
//    }
//
    @Override
    protected void onResume() {
        super.onResume();




    }
}