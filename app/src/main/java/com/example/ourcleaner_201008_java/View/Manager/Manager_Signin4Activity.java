package com.example.ourcleaner_201008_java.View.Manager;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
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
import com.example.ourcleaner_201008_java.GlobalApplication;
import com.example.ourcleaner_201008_java.R;
import com.example.ourcleaner_201008_java.Server.EmailCheckRequest;
import com.example.ourcleaner_201008_java.SharedP.PreferenceManager_Manager;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.LogoutResponseCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class Manager_Signin4Activity extends AppCompatActivity {

    private static final String TAG = "매니저회원가입4마지막";

    /* 이전 엑티비티에서 intent로 받아온 값 */
    String phoneNumStr;
    int termsAgreeInt;

    EditText nameEdit, emailEdit, passwordEdit, passwordReEdit, identiEdit, sexEdit;
    TextView emailTxt, passwordTxt, passwordReTxt;
    String nameStr, emailStr, passwordStr, passwordReStr, identiStr, sexStr;

    Button signinBtn;

    int nextInt1=0, nextInt2=0, nextInt3=0, nextInt4=0, nextInt5=0, nextInt6=0;
    //이메일중복및 유효성, 비밀번호유효성, 비밀번호재확인, 주민번호 유효성, 성별 확인, 이름도 마지막에 추가함..

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager__signin4);

        Log.e(TAG, "=== onCreate ===" );

        //intent로 받을 때 사용하는 코드
        Intent intent = getIntent();

        phoneNumStr = intent.getStringExtra("phoneNumStr");
        Log.e(TAG, "휴대폰번호 인증 엑티비티에서 intent 받음 intent.phoneNumStr : "+ phoneNumStr);

        termsAgreeInt = intent.getIntExtra("termsAgreeInt",0);
        Log.e(TAG, "휴대폰번호 인증 엑티비티에서 intent 받음 intent.termsAgreeInt : "+ termsAgreeInt);

        nameEdit = findViewById(R.id.nameEdit);
        emailEdit = findViewById(R.id.emailEdit);
        passwordEdit = findViewById(R.id.passwordEdit);
        passwordReEdit = findViewById(R.id.passwordReEdit);
        identiEdit = findViewById(R.id.identiEdit);
        sexEdit = findViewById(R.id.sexEdit);

        emailTxt = findViewById(R.id.emailTxt);
        passwordTxt = findViewById(R.id.passwordTxt);
        passwordReTxt = findViewById(R.id.passwordReTxt);

        signinBtn = findViewById(R.id.signinBtn);

        /* 이메일 유효성 검사, 중복 검사 동시 진행 */
        emailEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                /* 여기에서 유효성 검사, 중복검사 둘 다 진행 */

                emailStr = emailEdit.getText().toString();
                if(!android.util.Patterns.EMAIL_ADDRESS.matcher(emailStr).matches()) {
                    emailTxt.setText("* 이메일 형식을 확인해주세요. ");
                    emailTxt.setTextColor(Color.parseColor("#FF0000"));

                    nextInt1=0;
                    Log.e(TAG, "=== nextInt1 ===" +nextInt1);
                }else{
                    Response.Listener<String> responseListener = new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.e(TAG, "=== onResponse ===" +response );
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                boolean success = jsonObject.getBoolean("success");
                                if (success) {
                                    Log.e(TAG, "=== success 사용할 수 있는 아이디입니다. ===" );
                                    emailTxt.setText("* 사용할 수 있는 이메일입니다. ");
                                    nextInt1=1;
                                    Log.e(TAG, "=== nextInt1 ===" +nextInt1);
                                }
                                else {
                                    Log.e(TAG, "=== 사용할 수 없는 아이디입니다. ===" );
                                    emailTxt.setText("* 이미 존재하는 이메일입니다. ");
                                    emailTxt.setTextColor(Color.parseColor("#FF0000"));

                                    nextInt1=0;
                                    Log.e(TAG, "=== nextInt1 ===" +nextInt1);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    };
                    EmailCheckRequest emailCheckRequest = new EmailCheckRequest(emailStr, responseListener);
                    RequestQueue queue = Volley.newRequestQueue(Manager_Signin4Activity.this);
                    queue.add(emailCheckRequest);
                }


            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        /* 비밀번호 유효성 검사 */
        passwordEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                passwordStr = passwordEdit.getText().toString();
//                if(!Pattern.matches("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*\\\\W)(?=\\\\S+$).{8,20}$\n", passwordStr)){
                String regExp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*\\W)(?=\\S+$).{8,20}$";

                if(passwordStr.matches(regExp)){
                    passwordTxt.setText("");

                    nextInt2=1;
                    Log.e(TAG, "=== nextInt2 ===" +nextInt2);

                }else{

                    passwordTxt.setText("* 비밀번호 형식을 확인해주세요. ");
                    passwordTxt.setTextColor(Color.parseColor("#FF0000"));

                    nextInt2=0;
                    Log.e(TAG, "=== nextInt2 ===" +nextInt2);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        /* 비밀번호 재확인 */
        passwordReEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                passwordStr = passwordEdit.getText().toString();
                passwordReStr = passwordReEdit.getText().toString();

                if(nextInt2==1 && passwordReStr.equals(passwordStr)){
                    passwordReTxt.setText("");

                    nextInt3=1;
                    Log.e(TAG, "=== nextInt3 ===" +nextInt3);
                }else{
                    passwordReTxt.setText("* 비밀번호를 다시 입력해주세요.");
                    passwordReTxt.setTextColor(Color.parseColor("#FF0000"));

                    nextInt3=0;
                    Log.e(TAG, "=== nextInt3 ===" +nextInt3);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        /* 주민번호 유효성 검사, 나이 확인 */
        identiEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                identiStr = identiEdit.getText().toString();

                if(identiStr.length()==6){
                    Calendar cal =Calendar.getInstance();
                    int year = cal.get(Calendar.YEAR);

                    String birthYear = identiStr.substring(0,2);
                    int age = year - (1900 + Integer.parseInt(birthYear)) + 1;

                    Log.e(TAG, "=== year ===" +year);
                    Log.e(TAG, "=== birthYear ===" +birthYear);
                    Log.e(TAG, "=== age ===" +age);

                    if(30<age && age<65){
                        Log.e(TAG, "=== 나이 조건 만족 ===" );
                        nextInt4=1;
                        Log.e(TAG, "=== nextInt4 ===" +nextInt4);
                    }else{
                        Log.e(TAG, "=== 나이 조건 만족 안 함 ===" );
                        nextInt4=0;
                        Log.e(TAG, "=== nextInt4 ===" +nextInt4);
                    }
                }else{
                    nextInt4=0;
                    Log.e(TAG, "=== nextInt4 ===" +nextInt4);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        /* 성별 확인 */
        sexEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                sexStr = sexEdit.getText().toString();
                if(sexStr.equals("2")){
                    nextInt5=1;
                }else{
                    nextInt5=0;
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        nameEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                nameStr = nameEdit.getText().toString();
                if(nameStr.length()>=3 && nameStr.length()<=5){
                    nextInt6=1;
                }else{
                    nextInt6=0;
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        signinBtn = findViewById(R.id.signinBtn);
        signinBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.e(TAG, "=== signinBtn ===nextInt1" +nextInt1);
                Log.e(TAG, "=== signinBtn ===nextInt2" +nextInt2);
                Log.e(TAG, "=== signinBtn ===nextInt3" +nextInt3);
                Log.e(TAG, "=== signinBtn ===nextInt4" +nextInt4);
                Log.e(TAG, "=== signinBtn ===nextInt5" +nextInt5);
                Log.e(TAG, "=== signinBtn ===nextInt6" +nextInt6);

                if(nextInt1==1 && nextInt2==1 && nextInt3==1 && nextInt4==1 && nextInt5==1 && nextInt6==1){
                    postData();
                }else if(nextInt6==0){
                    Toast.makeText(getBaseContext(), "이름을 다시 확인해주세요.", Toast.LENGTH_SHORT).show();
                }else if(nextInt1==0){
                    Toast.makeText(getBaseContext(), "이메일을 다시 확인해주세요.", Toast.LENGTH_SHORT).show();
                }else if(nextInt2==0 || nextInt3==0){
                    Toast.makeText(getBaseContext(), "비밀번호를 다시 확인해주세요.", Toast.LENGTH_SHORT).show();
                }else if(nextInt4==0 || nextInt5==0){
                    Toast.makeText(getBaseContext(), "30세 ~ 65세의 여성만 가능합니다.", Toast.LENGTH_SHORT).show();
                }
            }
        });



    }

    // 서버로 데이터 보냄
    public void postData(){
        Log.d(TAG, "=== postData() ===" );
        // Request를 보낼 queue를 생성한다. 필요시엔 전역으로 생성해 사용하면 된다.
        RequestQueue queue = Volley.newRequestQueue(this);
        // 대표적인 예로 androidhive의 테스트 url을 삽입했다. 이부분을 자신이 원하는 부분으로 바꾸면 될 터
        String url = "http://52.79.179.66/insertManagerInfo.php";

        final StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "=== stringRequest === onResponse" +response);

                AlertDialog.Builder builder = new AlertDialog.Builder(Manager_Signin4Activity.this);
                builder.setMessage("매니저 가입이 완료 되었습니다.");
                builder.setPositiveButton("확인",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Log.d(TAG, "=== 확인 클릭  ===" );

                                //UserManagement API 요청을 담당
                                UserManagement.getInstance()
                                        //requestLogout : 로그아웃 요청
                                        //파라미터 : logout 요청 결과에 대한 callback
                                        .requestLogout(new LogoutResponseCallback() {
                                            @Override
                                            public void onCompleteLogout() {
                                                Log.d(TAG, "=== onCompleteLogout : 매니저 가입이 완료 되었습니다. ===");

                                                // 쉐어드에 저장 -> 나중에 다시 들어왔을 때 자동 로그인 위함
                                                PreferenceManager_Manager.setString(getApplicationContext(),"idStr",emailStr);
                                                PreferenceManager_Manager.setString(getApplicationContext(),"nameStr",nameStr);

                                                // 글로벌어플리케이션에 저장 -> 로그인 상태 저장을 위함
                                                GlobalApplication.currentManager = emailStr;
                                                GlobalApplication.currentManagerName = nameStr;
                                                GlobalApplication.currentManagerPhonNum = phoneNumStr;

                                                Intent intent = new Intent(Manager_Signin4Activity.this, Manager_MainActivity.class);
                                                //            엑티비티 정리
                                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                                Log.d(TAG, "=== 엑티비티 정리 ===" );

                                                startActivity(intent);
                                            }
                                        });
                            }
                        });
                builder.setNegativeButton("취소",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Log.d(TAG, "=== 취소 클릭 ===" );
                            }
                        });
                builder.show();

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
                params.put("nameStr", nameStr);
                params.put("idStr", emailStr);
                params.put("passwordStr", passwordStr);
                params.put("phoneNumStr", phoneNumStr);
                params.put("termsAgreeInt", String.valueOf(termsAgreeInt));

                Log.d(TAG, "=== params === : "+params );
                return params;
            }
        };

        stringRequest.setTag(TAG);
        queue.add(stringRequest);
    }
}