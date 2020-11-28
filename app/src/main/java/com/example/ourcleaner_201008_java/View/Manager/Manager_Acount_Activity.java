package com.example.ourcleaner_201008_java.View.Manager;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.ourcleaner_201008_java.GlobalApplication;
import com.example.ourcleaner_201008_java.R;
import com.example.ourcleaner_201008_java.SharedP.PreferenceManager_Auto;
import com.example.ourcleaner_201008_java.SharedP.PreferenceManager_Manager;
import com.example.ourcleaner_201008_java.View.LoginActivity;
import com.example.ourcleaner_201008_java.View.MoreService_Acount_Activity;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.LogoutResponseCallback;

public class Manager_Acount_Activity extends AppCompatActivity {

    private static final String TAG = "매니저용더보기계정관리";

    TextView profileTxt, phonNumTxt, logoutTxt, accountDeleteTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager__acount_);
        Log.d(TAG, "=== onCreate ===" );

        //각버튼 아이디 매칭
        profileTxt = findViewById(R.id.profileTxt);
        phonNumTxt = findViewById(R.id.phonNumTxt);
        logoutTxt = findViewById(R.id.logoutTxt);
        accountDeleteTxt = findViewById(R.id.accountDeleteTxt);

        Button.OnClickListener onClickListener = new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    //profileTxt 버튼 행동
                    case R.id.profileTxt:
                        Log.d(TAG, "=== profileTxt ===" );
                        break;
                    //phonNumTxt 버튼 행동
                    case R.id.phonNumTxt:
                        Log.d(TAG, "=== phonNumTxt ===" );
                        break;

                    //logoutTxt 버튼 행동
                    case R.id.logoutTxt:
                        Log.d(TAG, "=== logoutTxt ===" );

                        AlertDialog.Builder builder = new AlertDialog.Builder(Manager_Acount_Activity.this);
                        builder.setMessage("정말 로그아웃 하시겠습니까?");
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
                                                        Log.d(TAG, "=== onCompleteLogout : 로그아웃 되었습니다. ===");
                                                        //Toast.makeText(MainActivity.this, "로그아웃 되었습니다.", Toast.LENGTH_SHORT).show();
                                                        PreferenceManager_Manager.clear(getApplicationContext());
                                                        GlobalApplication.currentManager=null;
                                                        GlobalApplication.currentManagerPhonNum=null;
                                                        GlobalApplication.currentManagerName=null;

                                                        Log.d(TAG, "=== 자동로그인 해제 ===" );

                                                        Intent intent = new Intent(Manager_Acount_Activity.this, LoginActivity.class);
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

                        break;

                    //accountDeleteTxt 버튼 행동
                    case R.id.accountDeleteTxt:
                        Log.d(TAG, "=== accountDeleteTxt ===" );
                        break;
                }
            }
        };

        profileTxt.setOnClickListener(onClickListener);
        phonNumTxt.setOnClickListener(onClickListener);
        logoutTxt.setOnClickListener(onClickListener);
        accountDeleteTxt.setOnClickListener(onClickListener);


    }
}