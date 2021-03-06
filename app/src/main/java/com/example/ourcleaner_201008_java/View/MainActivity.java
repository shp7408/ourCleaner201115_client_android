package com.example.ourcleaner_201008_java.View;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.ourcleaner_201008_java.GlobalApplication;
import com.example.ourcleaner_201008_java.Interface.TokenInsertInterface;
import com.example.ourcleaner_201008_java.R;
import com.example.ourcleaner_201008_java.SharedP.PreferenceManager_Auto;
import com.example.ourcleaner_201008_java.View.Fragment.FragmentChat;
import com.example.ourcleaner_201008_java.View.Fragment.FragmentHome;
import com.example.ourcleaner_201008_java.View.Fragment.FragmentMore;
import com.example.ourcleaner_201008_java.View.Fragment.FragmentReservation;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.iid.FirebaseInstanceId;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class MainActivity extends AppCompatActivity  {

    private Button btn_custom_login_out; //카톡 연동 로그아웃 버튼

    String email;
    TextView emailtext;

//    //앱종료시간체크
//    long backKeyPressedTime;    //앱종료 위한 백버튼 누른시간
//
    private FragmentManager fragmentManager = getSupportFragmentManager();
    private FragmentHome fragmentHome = new FragmentHome();
    private FragmentReservation fragmentReservation = new FragmentReservation();
    private FragmentMore fragmentMore = new FragmentMore();
    private FragmentChat fragmentChat = new FragmentChat();

    private static final String TAG = "메인";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "=== onCreate 실행 ===");



        /* 이미 로그인된 상태임. 여기서 현재 사용자로 알람온것이 있는지 확인하기 */
        

//        System.out.println("token : "+ FirebaseInstanceId.getInstance().getToken());

        //추가한 라인
//        Log.e(TAG, "FirebaseApp.initializeApp(this)");
//        FirebaseApp.initializeApp(this);
//
//        Log.e(TAG, "subscribeToTopic(\"news\")");
//        FirebaseMessaging.getInstance().subscribeToTopic("news");

        Log.e(TAG, "getInstance().getToken();");
        String token = FirebaseInstanceId.getInstance().getToken();
        Log.e(TAG, "getInstance().getToken();"+token);

        /* 서버에 토큰 저장하는 코드 레트로핏 사용 */
        postTokenInsert(token);



        /* fcmpush 보내는 코드
        * 1. 클래스 만들어야 함
        * 2. fcmPushTest.pushFCMNotification 의 첫 번째 인자 : 메세지 내용 / 두 번째 인자 : 매니저인지, 고객인지 / 받는 사람 이메일 - 이걸로 찾음 */
//        FcmPushTest fcmPushTest = new FcmPushTest();
//
//        Thread thread = new Thread(new Runnable() {
//                @Override
//                public void run() {
//                    try  {
//                        Log.e(TAG, "=== fcmPushTest ==="+fcmPushTest.toString() );
//
//                        fcmPushTest.pushFCMNotification("받앙라ㅏㅏ",1,"");
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                        Log.e(TAG, "fcmPushTest 에러 코드 : "+ e);
//                    }
//                }
//        });
//
//        thread.start();
//        Handler mHandler = new Handler(Looper.getMainLooper());
//        mHandler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                Log.e(TAG, "fcmPushTest : run");
//
//            }
//
//        }, 0);






        btn_custom_login_out = (Button) findViewById(R.id.btn_custom_login_out);
        Log.d(TAG, "btn_custom_login_out 연결" + btn_custom_login_out);


        //로그인 엑티비티에서 회원 데이터 수신
        Intent intent = getIntent();
        Log.d(TAG, "로그인 엑티비티에서 인텐트 받음 intent :"+intent);

        //[수정함] 메인 + 알람에서 intent 받아온 경우
        email = intent.getStringExtra("email");
        Log.d(TAG, "email : "+ email);

        if(email==null){
            email=   PreferenceManager_Auto.getString(getApplicationContext(),"email");
            Log.d(TAG, "=== intent로 받아온 이메일 없는 경우에는 쉐어드에서 가져옴 ===" );
        }

        GlobalApplication.currentUser = email;
        Log.d(TAG, "=== 이제 현재 유저를 저장해서 로그인 상태 유지함 === 현재 사용자 : " +GlobalApplication.currentUser );


//        //연결
//        emailtext = findViewById(R.id.email_tv);
//
//        //이메일을 화면에 띄우기
//        emailtext.setText(email);
//
//
//        //        ========== 로그아웃 버튼 클릭 시 -> 어떤 일이 발생? ==========
//        btn_custom_login_out.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //UserManagement API 요청을 담당
//                UserManagement.getInstance()
//                        //requestLogout : 로그아웃 요청
//                        //파라미터 : logout 요청 결과에 대한 callback
//                        .requestLogout(new LogoutResponseCallback() {
//                            @Override
//                            public void onCompleteLogout() {
//                                Log.d(TAG, "=== onCompleteLogout : 로그아웃 되었습니다. ==="+btn_custom_login_out);
//                                //Toast.makeText(MainActivity.this, "로그아웃 되었습니다.", Toast.LENGTH_SHORT).show();
//                                PreferenceManager_Auto.clear(getApplicationContext());
//                                Log.d(TAG, "=== 자동로그인 해제 ===" );
//
//                                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
//                                //            엑티비티 정리
//                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                                Log.d(TAG, "=== 엑티비티 정리 ===" );
//
//                                startActivity(intent);
//                            }
//                        });
//            }
//        });


        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.frameLayout, fragmentHome).commitAllowingStateLoss();

        BottomNavigationView bottomNavigationView = findViewById(R.id.navigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(new ItemSelectedListener());

    }






    /* 레트로핏으로 서버에 토큰 보내는 코드 - 서버에서 mysql에 저장 함*/
    private void postTokenInsert(String token){

        Log.e(TAG, "=== postManagerProfile 시작 ===" );

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(TokenInsertInterface.JSONURL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();

        TokenInsertInterface api =retrofit.create(TokenInsertInterface.class);
        /* 인터페이스에서 정의한 메서드 / 인자로 보낼 값 넣는 곳 */
        Call<String> call = api.putTokenInsert(token, GlobalApplication.currentUser);

        Log.e(TAG, "=== GlobalApplication.currentUser ===" +GlobalApplication.currentUser);

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                Log.e("Responsestring", response.body().toString());
                //Toast.makeText()
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        Log.e("onSuccess", response.body().toString());

                    } else {
                        Log.e("onEmptyResponse", "Returned empty response");
                        //Toast.makeText(getContext(),"Nothing returned",Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

                Log.e(TAG, "=== onFailure call ===" +call+" t"+t);

            }
        });


    }

    class ItemSelectedListener implements BottomNavigationView.OnNavigationItemSelectedListener{

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            FragmentTransaction transaction = fragmentManager.beginTransaction();

            switch(menuItem.getItemId())
            {

                case R.id.homeItem:
                    transaction.replace(R.id.frameLayout, fragmentHome).commitAllowingStateLoss();
                    break;
                case R.id.myreservationItem:
                    transaction.replace(R.id.frameLayout, fragmentReservation).commitAllowingStateLoss();
                    break;
                case R.id.moreItem:
                    transaction.replace(R.id.frameLayout, fragmentMore).commitAllowingStateLoss();
                    break;
                case R.id.mychatItem:
                    transaction.replace(R.id.frameLayout, fragmentChat).commitAllowingStateLoss();
                    break;
            }
            return true;
        }
    }

    //    @Override
//    public void onBackPressed() {
//        super.onBackPressed();
//
//        //1번째 백버튼 클릭
//        if(System.currentTimeMillis()>backKeyPressedTime+2000){
//            backKeyPressedTime = System.currentTimeMillis();
//            Toast.makeText(this, "\'뒤로\'버튼을 한번 더 누르시면 종료됩니다.", Toast.LENGTH_SHORT).show();
//            return;
//        }
//        //2번째 백버튼 클릭 (종료)
//        else{
//            AppFinish();
//        }
//
//
//    }
//
//    //앱종료
//    public void AppFinish(){
//        finish();
//        System.exit(0);
//        android.os.Process.killProcess(android.os.Process.myPid());
//    }

}