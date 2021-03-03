package com.example.ourcleaner_201008_java.View.Manager;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.ourcleaner_201008_java.ClientThread;
import com.example.ourcleaner_201008_java.GlobalApplication;
import com.example.ourcleaner_201008_java.R;
import com.example.ourcleaner_201008_java.Server.LoginRequest;
import com.example.ourcleaner_201008_java.SharedP.PreferenceManager_Manager;
import com.example.ourcleaner_201008_java.View.GPSInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Manager_LoginActivity extends AppCompatActivity {

    private static final String TAG = "매니저용 로그인";

    TextView signinTxt;

    EditText idEdit, passwordEdit;
    String idStr, passwordStr;

    Button loginBtn;

    /* 로그인 시, 현재 위치 받아와서 글로벌 어플리케이션에 저장하기 */
    private boolean isPermission = false;
    private GPSInfo gps;

    /* 현재 위치 주소 결과 리스트  [경기도, 화성시, 진안동] */
    ArrayList<String> arrayList;
    String nowAddressStr;

    private final int PERMISSIONS_ACCESS_FINE_LOCATION = 1000;
    private final int PERMISSIONS_ACCESS_COARSE_LOCATION = 1001;


    /* 소켓 준비하는 부분 */
    Socket client;
    //String ip = "192.168.35.172"; //192.168.0.3
    //String ip = "192.168.0.3"; //192.168.187.1
    //String ip = "192.168.187.1"; //172.20.10.14
    String ip ="192.168.0.6";

    int port = 8080;

    Thread thread;
    ClientThread clientThread;
    Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager__login);
        Log.d(TAG, "=== onCreate ===" );

        idEdit = findViewById(R.id.idEdit);
        passwordEdit = findViewById(R.id.passwordEdit);


        /* 계속 호출이 안 돼서..ㅎㅎ */
        if(GlobalApplication.currentManagerAddress==null){
            for(int i=0; i<1; i++) {
                //whereIsHere();
            }
        }

        if(PreferenceManager_Manager.getString(getApplicationContext(), "idStr").isEmpty()){

            Log.d(TAG, "=== 매니저 쉐어드에 매니저 아이디 없는 경우 현재 엑티비티 그대로 진행 ===" );

        }else{

            Log.d(TAG, "=== 매니저 쉐어드에 매니저 아이디 있음 ===" );

            //현재 세션 유지를 위한 글로벌 어플리케이션에 idStr 저장하기
//            GlobalApplication.currentManager = PreferenceManager_Manager.getString(getApplicationContext(), "idStr");

            whereIsHere();

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


                                /* 소켓 준비 */
                                //instantiate();


                                //현재 세션 유지를 위한 글로벌 어플리케이션에 idStr 저장하기
                                GlobalApplication.currentManager= idStr;
                                GlobalApplication.currentManagerName = nameStr;
                                GlobalApplication.currentManagerPhonNum =phoneNumStr;

                                PreferenceManager_Manager.setString(getApplicationContext(),"idStr", idStr);
                                PreferenceManager_Manager.setString(getApplicationContext(),"nameStr",nameStr);
                                PreferenceManager_Manager.setString(getApplicationContext(),"passwordStr",passwordStr);

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
            LoginRequest loginRequest = new LoginRequest(PreferenceManager_Manager.getString(getApplicationContext(), "idStr"), PreferenceManager_Manager.getString(getApplicationContext(), "passwordStr"), responseListener);
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
                                    PreferenceManager_Manager.setString(getApplicationContext(),"passwordStr",passwordStr);

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
                    Intent intent = new Intent(getApplicationContext(), Manager_SigninGuide1Activity.class);

                    startActivity(intent);

                    //finish();
                    }
            });
    }



    /* oncreate 후, 서버로 데이터 보내서 검색까지 */
    private void whereIsHere(){
        Log.e(TAG, "whereIsHere()");
        if(!isPermission){
            callPermission();
            return;
        }
        Log.e(TAG, "whereIsHere() isPermission");
        gps = new GPSInfo(getApplicationContext());
        if (gps.isGetLocation()) {
            Log.e(TAG, "whereIsHere() gps.isGetLocation()");
            //GPSInfo를 통해 알아낸 위도값과 경도값
            double latitude = gps.getLatitude();
            double longitude = gps.getLongitude();

            Log.e(TAG, "whereIsHere() gps.isGetLocation() latitude"+latitude);
            Log.e(TAG, "whereIsHere() gps.isGetLocation() longitude"+longitude);

            //Geocoder
            Geocoder gCoder = new Geocoder(getApplicationContext(), Locale.getDefault());
            List<Address> addr = null;

            try{
                addr = gCoder.getFromLocation(latitude,longitude,1);
                Address a = addr.get(0);

                for (int i=0;i <= a.getMaxAddressLineIndex();i++) {
                    //여기서 변환된 주소 확인할  수 있음
                    Log.e(TAG, "whereIsHere AddressLine(" + i + ") : " + a.getAddressLine(i) + "\n ");

                    // TODO: 2020-12-12 위치 바뀔 시, 고쳐야 할 수 있음..
                    nowAddressStr = a.getAddressLine(i);
                    Log.e(TAG, "whereIsHere nowAddressStr  : " + nowAddressStr);

                    nowAddressStr = nowAddressStr.replace("대한민국 ", "");
                    Log.e(TAG, "whereIsHere nowAddressStr 대한민국 제거 : " + nowAddressStr);

                    nowAddressStr = nowAddressStr.replaceAll("-", "");
                    Log.e(TAG, "whereIsHere nowAddressStr 문자 제거 : " + nowAddressStr);

                    nowAddressStr = nowAddressStr.replaceAll("[0-9]", "");
                    Log.e(TAG, "whereIsHere nowAddressStr 숫자 제거 : " + nowAddressStr);

                    String[] array = nowAddressStr.split(" ");

                    arrayList= new ArrayList<>();

                    for(int j=0;j<array.length;j++) {
                        Log.e(TAG, "whereIsHere array[j] : " + array[j]);
                        arrayList.add(array[j]);
                    }

                    Log.e(TAG, "whereIsHere arrayList : " + arrayList.toString());

                    GlobalApplication.setCurrentManagerAddress(arrayList.get(1));
                    Log.e(TAG, "whereIsHere 위치 저장 " + GlobalApplication.currentManagerAddress);


//                    fetchJSON(arrayList.get(1), ""); //"화성시"로 검색하기

                }

            } catch (IOException e){
                e.printStackTrace();
                Log.e(TAG, "whereIsHere() gCoder.getFromLocation 에러코드 e"+e);

            }

            if (addr != null) {
                if (addr.size()==0) {
                    Toast.makeText(getApplicationContext(),"주소정보 없음", Toast.LENGTH_LONG).show();
                }

                Log.e(TAG, "whereIsHere() addr != null");
            }
        } else {
            // GPS 를 사용할수 없으므로
            gps.showSettingsAlert();
            Log.e(TAG, "whereIsHere() GPS 를 사용할수 없으므로 gps.showSettingsAlert()");
        }

    }

    // 전화번호 권한 요청
    private void callPermission() {
        // Check the SDK version and whether the permission is already granted or not.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                && ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_ACCESS_FINE_LOCATION);

        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                && ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED){
            requestPermissions(
                    new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION},
                    PERMISSIONS_ACCESS_COARSE_LOCATION);
        } else {
            isPermission = true;
        }
    }


    /* 소켓 준비하는 부분 */
    private void instantiate(){
        Log.d(TAG, "=== connect() ===" );
        thread = new Thread(){
            public void run() {
                super.run();
                try {
                    client = new Socket(ip, port);
                    clientThread = new ClientThread(client, handler);
                    clientThread.start();
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        thread.start();
    }


}