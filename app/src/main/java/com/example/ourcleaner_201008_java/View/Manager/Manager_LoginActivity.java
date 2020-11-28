package com.example.ourcleaner_201008_java.View.Manager;
import android.widget.Toast;


import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;


import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.ourcleaner_201008_java.GlobalApplication;
import com.example.ourcleaner_201008_java.R;
import com.example.ourcleaner_201008_java.Server.LoginRequest;
import com.example.ourcleaner_201008_java.SharedP.PreferenceManager_Auto;
import com.example.ourcleaner_201008_java.SharedP.PreferenceManager_Manager;
import com.example.ourcleaner_201008_java.View.LoginActivity;
import com.example.ourcleaner_201008_java.View.MainActivity;
import com.example.ourcleaner_201008_java.View.TermsActivity;

public class Manager_LoginActivity extends AppCompatActivity {

    private static final String TAG = "매니저용 로그인";

    TextView signinTxt;

    EditText idEdit, passwordEdit;
    String idStr, passwordStr;

    Button loginBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager__login);
        Log.d(TAG, "=== onCreate ===" );

        idEdit = findViewById(R.id.idEdit);
        passwordEdit = findViewById(R.id.passwordEdit);

        if(PreferenceManager_Manager.getString(getApplicationContext(), "idStr").isEmpty()){

            Log.d(TAG, "=== 매니저 쉐어드에 매니저 아이디 없는 경우 ===" );

        }else{

            Log.d(TAG, "=== 매니저 쉐어드에 매니저 아이디 있음 ===" );

            //현재 세션 유지를 위한 글로벌 어플리케이션에 idStr 저장하기
//            GlobalApplication.currentManager = PreferenceManager_Manager.getString(getApplicationContext(), "idStr");

            Response.Listener<String> responseListener = new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.e(TAG, "=== response ===" +response);
                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        if(jsonArray.length()>0){
                            Log.e(TAG, "=== 아이디 비밀번호 맞는 경우 jsonArray 값이 있음 ===" );

                            for(int i = 0 ; i<jsonArray.length(); i++){

                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                String nameStr = jsonObject.getString( "nameStr" );
                                String idStr = jsonObject.getString( "idStr" );
                                String passwordStr = jsonObject.getString( "passwordStr" );
                                String phoneNumStr = jsonObject.getString( "phoneNumStr" );

                                Log.e(TAG, "=== nameStr ===" +nameStr);
                                Log.e(TAG, "=== idStr ===" +idStr);
                                Log.e(TAG, "=== passwordStr ===" +passwordStr);
                                Log.e(TAG, "=== phoneNumStr ===" +phoneNumStr);

                                //현재 세션 유지를 위한 글로벌 어플리케이션에 idStr 저장하기
                                GlobalApplication.currentManager= idStr;
                                GlobalApplication.currentManagerName = nameStr;
                                GlobalApplication.currentManagerPhonNum =phoneNumStr;

                                PreferenceManager_Manager.setString(getApplicationContext(),"idStr", idStr);
                                PreferenceManager_Manager.setString(getApplicationContext(),"nameStr",nameStr);

                                Toast.makeText( getApplicationContext(), nameStr+" 매니저님 안녕하세요.", Toast.LENGTH_SHORT ).show();
                                Intent intent = new Intent( Manager_LoginActivity.this, Manager_MainActivity.class );

                                startActivity(intent);
                            }

                        }else{
                            Log.e(TAG, "=== 아이디 비밀번호 틀린 경우 jsonArray 값이 없음===" );
                            Toast.makeText( getApplicationContext(), "아이디 비밀번호를 확인해주세요.", Toast.LENGTH_SHORT ).show();

                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.e(TAG, "=== response === 에러코드 : " +e);
                    }
                }
            };
            LoginRequest loginRequest = new LoginRequest(PreferenceManager_Manager.getString(getApplicationContext(), "idStr"), "auto", responseListener);
            RequestQueue queue = Volley.newRequestQueue( Manager_LoginActivity.this );
            queue.add( loginRequest );

            //있으면 넘어감
            Intent intent = new Intent(getApplicationContext(), Manager_MainActivity.class);
            startActivity(intent);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            finish();
        }

        loginBtn = findViewById(R.id.loginBtn);
        loginBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Log.d(TAG, "=== loginBtn 클릭 : === ");

                idStr = idEdit.getText().toString();
                passwordStr = passwordEdit.getText().toString();

                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e(TAG, "=== response ===" +response);
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            if(jsonArray.length()>0){
                                Log.e(TAG, "=== 아이디 비밀번호 맞는 경우 jsonArray 값이 있음 ===" );

                                for(int i = 0 ; i<jsonArray.length(); i++){

                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    String nameStr = jsonObject.getString( "nameStr" );
                                    String idStr = jsonObject.getString( "idStr" );
                                    String passwordStr = jsonObject.getString( "passwordStr" );
                                    String phoneNumStr = jsonObject.getString( "phoneNumStr" );

                                    Log.e(TAG, "=== nameStr ===" +nameStr);
                                    Log.e(TAG, "=== idStr ===" +idStr);
                                    Log.e(TAG, "=== passwordStr ===" +passwordStr);
                                    Log.e(TAG, "=== phoneNumStr ===" +phoneNumStr);

                                    GlobalApplication.currentManager= idStr;
                                    GlobalApplication.currentManagerName = nameStr;
                                    GlobalApplication.currentManagerPhonNum =phoneNumStr;

                                    PreferenceManager_Manager.setString(getApplicationContext(),"idStr", idStr);
                                    PreferenceManager_Manager.setString(getApplicationContext(),"nameStr",nameStr);

                                    Toast.makeText( getApplicationContext(), nameStr+" 매니저님 안녕하세요.", Toast.LENGTH_SHORT ).show();
                                    Intent intent = new Intent( Manager_LoginActivity.this, Manager_MainActivity.class );
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                }

                            }else{
                                Log.e(TAG, "=== 아이디 비밀번호 틀린 경우 jsonArray 값이 없음===" );
                                Toast.makeText( getApplicationContext(), "아이디 비밀번호를 확인해주세요.", Toast.LENGTH_SHORT ).show();

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.e(TAG, "=== response === 에러코드 : " +e);
                        }
                    }
                };
                LoginRequest loginRequest = new LoginRequest(idStr, passwordStr, responseListener);
                RequestQueue queue = Volley.newRequestQueue( Manager_LoginActivity.this );
                queue.add( loginRequest );



            }
        });

        signinTxt = (TextView) findViewById(R.id.signinTxt);
        signinTxt.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                    Log.d(TAG, "=== signinTxt 클릭 : === ");

                    //있으면 넘어감
                    Intent intent = new Intent(getApplicationContext(), Manager_SigninActivity.class);

                    startActivity(intent);

                    //finish();
                    }
            });
    }







}