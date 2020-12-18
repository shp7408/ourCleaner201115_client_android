package com.example.ourcleaner_201008_java.View;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.ourcleaner_201008_java.Adapter.SelectManagerAdapter;
import com.example.ourcleaner_201008_java.DTO.ManagerDTO;
import com.example.ourcleaner_201008_java.GlobalApplication;
import com.example.ourcleaner_201008_java.Interface.AlarmManagerSelectInterface;
import com.example.ourcleaner_201008_java.Interface.TokenInsertInterface;
import com.example.ourcleaner_201008_java.R;

import com.example.ourcleaner_201008_java.SharedP.PreferenceManager_Auto;
import com.example.ourcleaner_201008_java.SharedP.PreferenceManager_Manager;
import com.example.ourcleaner_201008_java.View.Manager.Manager_AlarmActivity;
import com.example.ourcleaner_201008_java.View.Manager.Manager_LoginActivity;
import com.kakao.auth.AuthType;
import com.kakao.auth.ISessionCallback;
import com.kakao.auth.Session;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.MeV2ResponseCallback;
import com.kakao.usermgmt.response.MeV2Response;
import com.kakao.usermgmt.response.model.Profile;
import com.kakao.usermgmt.response.model.UserAccount;
import com.kakao.util.OptionalBoolean;
import com.kakao.util.exception.KakaoException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

//로그인 화면 엑티비티
public class LoginActivity extends AppCompatActivity {


    private static final String TAG = "로그인";



    private Button btn_custom_login; //카톡 연동 로그인 버튼
    private SessionCallback sessionCallback = new SessionCallback(); //세션 콜백 객체 생성 -> 왜 필요할까?
    Session session; //세션 변수 선언-> 왜 필요할까?
    private Button btn_custom_login_out; //카톡 연동 로그아웃 버튼

    String emtx;
    String emailStr,nicknameStr; //TermsActivity에서 받아온 스트링값

    String server_url = "http://52.79.179.66/emailCheck.php";

    //    getdata 할 때 사용하는 변수임
    private String mJsonString;

    //서버에서 받아온 json에서 추출한 email
    String JsonEmail=null;
    private static final String TAG_JSON="ourCleaner";
    private static final String TAG_EMAIL = "email";

    //매니저용 로그인
    TextView managerLoginTxt;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Log.d(TAG, "=== onCreate 실행 ==="); // 셋컨텐트뷰 코드 한 줄에, 로그 한 줄 쓰면 됨.

        //해쉬키 받아오기 위함 -> 카카오톡api
        //getHashKey();


        Log.d(TAG, "=== 쉐어드에서 이메일 있는지 확인. 있으면, 어플리케이션에 해당 이메일, 패스워드 저장하고, 메인 화면으로, 없으면, 현재 엑티비티 진행 ===" );


        emtx = PreferenceManager_Auto.getString(getApplicationContext(),"email");
        Log.d(TAG, "자동로그인 위한 쉐어드에 emtx :" + emtx );

        if(emtx.isEmpty()){
            Log.d(TAG, "=== emtx에 값이 없을 때, 현재 엑티비티 진행 ===" );


            String managerAuto = PreferenceManager_Manager.getString(getApplicationContext(),"idStr");
            /* 여기에서 매니저 자동로그인 쉐어드 저장되어있는지 확인하기 */
            Log.e(TAG, "=== 매니저 자동로그인 managerAuto ===" + managerAuto);

            if(managerAuto.isEmpty()){
                Log.e(TAG, "=== managerAuto.isEmpty() 비어 있음. 그냥 엑티비티 진행 ㄱㄱ ===" );
            }else{
                Log.e(TAG, "=== managerAuto 값 있음 " +
                        "여기서 알람 확인할 것 managerAuto  ===" +managerAuto);
                Log.d(TAG, "=== 백그라운드 알람 클릭 -> 로그인화면 -> 매니저 쉐어드에 저장되어 있으면, 알람 엑티비티로 이동함 ===" );

                /* db에 해당 알람 있는지 없는지 확인해야 함. 없으면 해당 엑티비티 그대로 진행,
                * 있으면, 알람 엑티비티로 이동하기 */

                postEmailAlarm(managerAuto);



            }














        }else {

            Log.d(TAG, "=== emtx에 값이 있을 때, 메인 엑티비티 연결 ===" );

            GlobalApplication.currentUser = emtx;

            Intent intent = new Intent(getApplicationContext(), MainActivity.class);

            startActivity(intent);

            finish();

        }


//        ========== 로그인 화면에서 필요한 버튼 연결 ==========

        btn_custom_login = (Button) findViewById(R.id.btn_custom_login);
        btn_custom_login_out = (Button) findViewById(R.id.btn_custom_login_out);

        Log.d(TAG, "=== 로그인 화면에서 필요한 버튼 연결 완료 ===" );


//        ========== 세션 인스턴스 생성 및 콜백 등록 ==========
        session = Session.getCurrentSession();
        Log.d(TAG, "=== 카카오톡 로그인 위한 세션 인스턴스 생성 ===" );

        session.addCallback(sessionCallback);
        //addcallback : 세션 상태 변화 콜백을 받고자 할때 콜백을 등록한다. 파라미터 :  추가할 세션 콜백
        //즉, session에 sessionCallback 콜백을 추가하려고 함.
        Log.d(TAG, "=== 카카오톡 로그인 위한 세션에 콜백 등록. 세션에드콜백 ===");



//        ========== 로그인 버튼 클릭 시 -> 어떤 일이 발생? ==========
        btn_custom_login.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                Log.d(TAG, "=== btn_custom_login 클릭 : === ");

                //세션 오픈  :  카카오 인증을 위한 세션이 오픈된다.
                // 파라1 : 인증받을 타입. 예를 들어, 카카오톡 또는 카카오스토리 또는 직접 입력한 카카오계정
                // 파라2 : 세션오픈을 호출한 activity
                session.open(AuthType.KAKAO_LOGIN_ALL, LoginActivity.this); //모든 로그인 방식을 사용하고 싶을 때 지정
                Log.d(TAG, "=== 카카오 인증을 위한 세션 오픈됨 === ");

            }

        });

        //매니저용 로그인 엑티비티로 이동하는 버튼
        managerLoginTxt = (TextView) findViewById(R.id.managerLoginTxt);
        managerLoginTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "=== managerLoginTxt 클릭 : === ");

                //있으면 넘어감
                Intent intent = new Intent(getApplicationContext(), Manager_LoginActivity.class);
                startActivity(intent);

                //finish();
            }
        });


    } //oncreate 끝남

    



    //
    // ========== onDestroy : Activity 가 종료될 때 호출되는 콜백 API , 화면상의 activity가 사라지고 난 뒤 호출 됨!! -> 사용하지 않는 자원 해제하기 위함 ==========
    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "=== onDestroy : ===");
        // 세션 콜백 삭제
        Session.getCurrentSession().removeCallback(sessionCallback);
        Log.d(TAG, "=== 세션 콜백 삭제 ===");
    } //onDestroy 끝남




    /* 레트로핏으로 이메일 보내서 해당 매니저에게 알람 왔는지 확인하는 코드 */
    private void postEmailAlarm(String email){

        Log.e(TAG, "=== postEmailAlarm 시작 ===" );

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(AlarmManagerSelectInterface.JSONURL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();

        AlarmManagerSelectInterface api =retrofit.create(AlarmManagerSelectInterface.class);
        /* 인터페이스에서 정의한 메서드 / 인자로 보낼 값 넣는 곳 */
        Call<String> call = api.selectAlarmManager(email);

        Log.e(TAG, "=== 매니저 email ===" + email);

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                Log.e("Responsestring", response.body().toString());
                //Toast.makeText()
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        Log.e("onSuccess", response.body());

                        String jsonresponse = response.body();
                        writeAlarmInfo(jsonresponse);


                    } else {
                        Log.e("onEmptyResponse", "Returned empty response");
                    }
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

                Log.e(TAG, "=== onFailure call ===" +call+" t"+t);

            }
        });
    }

    private void writeAlarmInfo(String response){

        try {
            //getting the whole json object from the response
            JSONObject obj = new JSONObject(response);
            Log.e(TAG, "=== response ===" +response);

            if(obj.optString("status").equals("true")){

                JSONArray dataArray  = obj.getJSONArray("data");

                //status true 이면, 있으면 알람 엑티비티로 넘어감
                Intent intent = new Intent(getApplicationContext(), Manager_AlarmActivity.class);
                startActivity(intent);
                finish();


//                for (int i = 0; i < dataArray.length(); i++) {
//
//                    JSONObject dataobj = dataArray.getJSONObject(i);
//                    dataobj.getInt("uid");
//
//
//                }

            }else {
                Toast.makeText(LoginActivity.this, obj.optString("message")+"", Toast.LENGTH_SHORT).show();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }







    // 카카오톡 연동 로그인 결과를 전달 받기 위한 Callback 클래스
    public class SessionCallback implements ISessionCallback {

        private static final String TAG = "로그인 세션콜백";

        // 로그인에 성공한 상태
        @Override
        public void onSessionOpened() {
            requestMe();
            Log.d(TAG, "=== 카카오톡 로그인 성공 onSessionOpened ===");
        }

        // 로그인에 실패한 상태
        @Override
        public void onSessionOpenFailed(KakaoException exception) {
            Log.e(TAG, "=== 로그인에 실패 onSessionOpenFailed === 에러코드 : "+ exception.getMessage());
        }


        // 성공 후, 사용자 정보 요청
        public void requestMe() {

            Log.d(TAG, "=== 카카오톡 연동 로그인 결과를 전달 받기 위한 Callback 클래스 requestMe 사용자 정보 요청 ==="); //-> 어떤 클래스 내부에 있는지
            UserManagement.getInstance()
                    .me(new MeV2ResponseCallback() { //버전2임. 날짜 확인해야 함. 이전 버전은 다른 메서드 임.
                        // 사용자정보 요청 결과에 대한 Callback

                        // 세션 오픈 실패. 세션이 삭제된 경우,
                        @Override
                        public void onSessionClosed(ErrorResult errorResult) {
                            Log.e(TAG, "=== KAKAO_API 세션이 닫힘 있음 === // 에러코드 : " + errorResult); //에러코드 라는 설명 넣으면 좋음.
                        }

                        @Override
                        public void onFailure(ErrorResult errorResult) {
                            Log.e(TAG, "=== KAKAO_API 사용자 정보 요청 실패=== // 에러코드 : " + errorResult);
                        }

                        @Override
                        public void onSuccess(MeV2Response result) {
                            Log.d(TAG, "=== onSuccess KAKAO_API=== " );

                            UserAccount kakaoAccount = result.getKakaoAccount();
                            Log.d(TAG, "=== 요청받은 결과에서 카카오 계정을 get 해 옴.=== ");

                                // === 이메일
                                String email = kakaoAccount.getEmail();
                                Log.d(TAG, "카카오 계정에서 이메일 가져옴" + email);



                                if (email != null) {
                                    Log.d(TAG, "KAKAO_API email이 널이 아닌 경우, api에서 이메일가져 올수 있다고 체크 함. ");
                                    Log.d(TAG, "KAKAO_API email: " + email);

                                        // === 프로필
                                    Profile profile = kakaoAccount.getProfile();
                                    Log.d(TAG, "KAKAO_API profile : "+ profile);

                                    if (profile != null) {


                                        Log.d(TAG, "=== 카톡 로그인 성공 함. 회원 디비에서는 아직 검증 안 함. ===" );

                                        Log.d(TAG, "KAKAO_API profile 널이 아닌 경우, ");
                                        Log.d(TAG, "KAKAO_API result.getId(): " + result.getId());


                                        emailStr = email;
                                        nicknameStr =profile.getNickname();
                                        Log.d(TAG, "=== 받아온 제이슨 데이터 맞는지 확인하기 위해서 데이터 넣음 ===" );
                                        //더 많은 정보를 확인하고 싶다면,  https://stickode.com/으로 접속하세요.
                                        Log.d(TAG, "emailStr : " +emailStr );
                                        Log.d(TAG, "nicknameStr : " +nicknameStr );

                                        GetData task2 = new GetData();
                                        task2.execute( server_url, email);


                                    } else if (kakaoAccount.profileNeedsAgreement() == OptionalBoolean.TRUE) {
                                        // 동의 요청 후 프로필 정보 획득 가능
                                        Log.d(TAG, "KAKAO_API profile 널인 경우, 그러나 OptionalBoolean.TRUE 동의 얻은 경우, ");

                                    } else {
                                        Log.d(TAG, "KAKAO_API profile 널인 경우, 프로필 획득 불가의 경우");
                                        // 프로필 획득 불가
                                    }



                                } else if (kakaoAccount.emailNeedsAgreement() == OptionalBoolean.TRUE) {

                                    Log.d(TAG, "KAKAO_API email이 널인 경우, 그러나 OptionalBoolean.TRUE 동의 얻은 경우, ");
                                    // 동의 요청 후 이메일 획득 가능
                                    // 단, 선택 동의로 설정되어 있다면 서비스 이용 시나리오 상에서 반드시 필요한 경우에만 요청해야 합니다.

                                } else {

                                    Log.d(TAG, "KAKAO_API email이 널인 경우, 이메일 획득 불가의 경우");
                                    // 이메일 획득 불가

                                }


                        }
                    });
        }
    } //SessionCallback 끝남












    public class GetData extends AsyncTask<String, Void, String>{

        String errorString = null;

        private static final String TAG = "겟 데이터";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.d(TAG, "=== GetData onPreExecute ===");

        }


        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Log.d(TAG, "=== GetData onPostExecute === ");

            Log.d(TAG, "onPostExecute response - " + result);

            if (result == null){
                Log.d(TAG, "result 널인 경우 errorString 에러코드 : "+ errorString);

            }
            else {
                Log.d(TAG, "result가 값이 있음 result : "+ result);

                mJsonString = result;
                Log.d(TAG, "result가 값이 있음 mJsonString : "+ mJsonString);

                showResult();

            }
        }


        @Override
        protected String doInBackground(String... params) {

            Log.d(TAG, "=== onPostExecute doInBackground ===");

            String serverURL = params[0];
            String postParameters1 = "email=" + params[1];

            Log.d(TAG, "onPostExecute doInBackground serverURL : " + params[0]);
            Log.d(TAG, "onPostExecute doInBackground postParameters1 : " + postParameters1);
            Log.d(TAG, "onPostExecute doInBackground params[1] : " + params[1]);


            try {

                Log.d(TAG, "try 부분 시작");

                URL url = new URL(serverURL);
                Log.d(TAG, "url : " + url);

                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                Log.d(TAG, "httpURLConnection : ");


                Log.d(TAG, "try 부분 시작 serverURL : " + params[0]);
                Log.d(TAG, "try 부분 시작 postParameters1 : " + postParameters1);
                Log.d(TAG, "try 부분 시작 params[1] : " + params[1]);


                httpURLConnection.setReadTimeout(5000);
                httpURLConnection.setConnectTimeout(5000);
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoInput(true);
                httpURLConnection.connect();

                OutputStream outputStream = httpURLConnection.getOutputStream();
                Log.d(TAG, "outputStream :"+outputStream);

                outputStream.write(postParameters1.getBytes("UTF-8"));
                Log.d(TAG, "postParameters1.getBytes(\"UTF-8\") :"+ postParameters1.getBytes("UTF-8"));


                outputStream.flush();

                outputStream.close();

                int responseStatusCode = httpURLConnection.getResponseCode();

                InputStream inputStream;

                if(responseStatusCode == HttpURLConnection.HTTP_OK) {
                    Log.d(TAG, "응답 성공 responseStatusCode :" + responseStatusCode);

                    inputStream = httpURLConnection.getInputStream();
                }
                else{
                    Log.d(TAG, "응답 실패 responseStatusCode 에러코드 :" + responseStatusCode);

                    inputStream = httpURLConnection.getErrorStream();
                }


                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");

                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                StringBuilder sb = new StringBuilder();

                String line;


                while((line = bufferedReader.readLine()) != null){
                    Log.d(TAG, "=== while문 반복 시작 line에다가 bufferedReader.readLine()을 넣고, 이 값이 널이 아닐 때, ===" );

                    sb.append(line);

                }
                Log.d(TAG, "=== while문 반복 종료 ===" );
                bufferedReader.close();
                Log.d(TAG, "bufferedReader.close()" );

                return sb.toString().trim();


            } catch (Exception e) {
                Log.d(TAG, "제이슨 받아오는 과정에서 에러 발생");
                Log.d(TAG, "InsertData: Error ", e);
                errorString = e.toString();
                Log.d(TAG, "errorString : "+ errorString);

                return null;
            }

        }
    }

    private void showResult() {

        Log.d(TAG, "=== showResult 시작 ===" );
        Log.d(TAG, "TAG_JSON : "+TAG_JSON);
        Log.d(TAG, "TAG_EMAIL : "+TAG_EMAIL);
        Log.d(TAG, "mJsonString : "+mJsonString);

        try {

            JSONObject jsonObject = new JSONObject(mJsonString);
            // JSONObject jsonObject = new JSONObject(mJsonString.substring(mJsonString.indexOf("{"),mJsonString.lastIndexOf("}") +1));
            JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);

            Log.d(TAG, "jsonObject : "+jsonObject);
            Log.d(TAG, "jsonArray : "+jsonArray);

            for(int i=0;i<jsonArray.length();i++){
                Log.d(TAG, "=== jsonArray.length() 만큼 반복문 시작 :  ===" );
                Log.d(TAG, "jsonArray.length() : "+jsonArray.length());

                JSONObject item = jsonArray.getJSONObject(i);
                Log.d(TAG, "item : "+item);

                JsonEmail = item.getString(TAG_EMAIL);

                Log.d(TAG, "item.getString(TAG_EMAIL) : "+item.getString(TAG_EMAIL));
                Log.d(TAG, "서버에서 받아온 결과 not_exist이면, 회원 디비에 없다는 것 임 JsonEmail  : "+JsonEmail);


            }


            Log.d(TAG, "반복문 종료 서버에서 받아온 결과 not_exist이면, 회원 디비에 없다는 것 임 Jsonemail  : "+JsonEmail);

            Log.d(TAG, "=== jsonArray.length() 만큼 반복문 종료 :  ===" );


        } catch (JSONException e) {

            Log.d(TAG, "=== 받아온 제이슨 보여주는 과정에서 에러 발생 ===");
            Log.d(TAG, "showResult : ", e);

        }




        if(JsonEmail==null){

            Log.d(TAG, "=== JsonEmail이 없음. 서버에서 받아온 것이 없음 ===" );

        }else if(JsonEmail.equals("not_exist")){

            Log.d(TAG, "=== JsonEmail not_exist equals 같음 JsonEmail : " + JsonEmail );

//            Log.d(TAG, "=== 현재 엑티비티 진행 ===" );
            Log.d(TAG, "=== 로그인-> 약관동의 엑비티로 ===" );

            //있으면 넘어감
            Intent intent = new Intent(getApplicationContext(), TermsActivity.class);

            intent.putExtra("email", emailStr);
            Log.d(TAG, "=== 메인으로 넘어갈 때의 email ===" +emailStr);
            intent.putExtra("nickname", nicknameStr);
            Log.d(TAG, "===  메인으로 넘어갈 때의 nickname ===" +nicknameStr);

            startActivity(intent);

            finish();


        }else{
            Log.d(TAG, "=== JsonEmail 회원디비에 로그인한 이메일이 있음 JsonEmail : "+JsonEmail );

            Log.d(TAG, "=== 약관동의-> 메인엑티비티로 ===" );

            //있으면 넘어감
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);

            intent.putExtra("email", JsonEmail);
            Log.d(TAG, "=== 메인으로 넘어갈 때의 email ===" +JsonEmail);
            intent.putExtra("nickname", nicknameStr);
            Log.d(TAG, "===  메인으로 넘어갈 때의 nickname ===" +nicknameStr);

            PreferenceManager_Auto.setString(getApplicationContext(),"email",JsonEmail );
            PreferenceManager_Auto.setString(getApplicationContext(),"nickname",nicknameStr );

            startActivity(intent);

            finish();

        }







    }











}



