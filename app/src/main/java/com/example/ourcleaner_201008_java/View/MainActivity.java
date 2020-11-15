package com.example.ourcleaner_201008_java.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.ourcleaner_201008_java.GlobalApplication;
import com.example.ourcleaner_201008_java.R;
import com.example.ourcleaner_201008_java.SharedP.PreferenceManager_Auto;
import com.example.ourcleaner_201008_java.View.Fragment.FragmentChat;
import com.example.ourcleaner_201008_java.View.Fragment.FragmentReservation;
import com.example.ourcleaner_201008_java.View.Fragment.FragmentHome;
import com.example.ourcleaner_201008_java.View.Fragment.FragmentMore;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.LogoutResponseCallback;

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