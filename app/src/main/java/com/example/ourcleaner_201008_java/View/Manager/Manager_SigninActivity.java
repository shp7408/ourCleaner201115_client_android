package com.example.ourcleaner_201008_java.View.Manager;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;


import java.util.HashMap;
import java.util.Map;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.ourcleaner_201008_java.GlobalApplication;
import com.example.ourcleaner_201008_java.R;
import com.example.ourcleaner_201008_java.SharedP.PreferenceManager_Manager;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.LogoutResponseCallback;

public class Manager_SigninActivity extends AppCompatActivity {

    //더 많은 정보를 확인하고 싶다면,  https://stickode.com/으로 접속하세요.
    private static final String TAG = "매니저 가입";

    EditText nameEdit, idEdit, passwordEdit, phoneNumEdit;
    String nameStr, idStr, passwordStr, phoneNumStr;

    Button signinBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager__signin);
        Log.d(TAG, "=== onCreate ===" );

        nameEdit = (EditText)findViewById(R.id.nameEdit);
        idEdit = (EditText)findViewById(R.id.idEdit);
        passwordEdit = (EditText)findViewById(R.id.passwordEdit);
        phoneNumEdit = (EditText)findViewById(R.id.phoneNumEdit);

        signinBtn = findViewById(R.id.signinBtn);


        signinBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "=== signinBtn ===" );

                nameStr = nameEdit.getText().toString();
                idStr = idEdit.getText().toString();
                passwordStr = passwordEdit.getText().toString();
                phoneNumStr = phoneNumEdit.getText().toString();

                Log.d(TAG, "=== nameStr ===" + nameStr);

                postData();

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
                Log.d(TAG, "=== stringRequest === onResponse" );

                AlertDialog.Builder builder = new AlertDialog.Builder(Manager_SigninActivity.this);
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
                                                PreferenceManager_Manager.setString(getApplicationContext(),"idStr",idStr);
                                                PreferenceManager_Manager.setString(getApplicationContext(),"nameStr",nameStr);

                                                // 글로벌어플리케이션에 저장 -> 로그인 상태 저장을 위함
                                                GlobalApplication.currentManager = idStr;
                                                GlobalApplication.currentManagerName = nameStr;
                                                GlobalApplication.currentManagerPhonNum = phoneNumStr;

                                                Intent intent = new Intent(Manager_SigninActivity.this, Manager_MainActivity.class);
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
                params.put("idStr", idStr);
                params.put("passwordStr", passwordStr);
                params.put("phoneNumStr", phoneNumStr);

                Log.d(TAG, "=== params === : "+params );

                return params;
            }
        };

        stringRequest.setTag(TAG);
        queue.add(stringRequest);
    }
}