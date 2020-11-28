package com.example.ourcleaner_201008_java.View.Manager;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.ourcleaner_201008_java.R;

public class Manager_ChatActivity extends AppCompatActivity {

    private static final String TAG = "매니저용채팅화면";

    /* 매니저용 전체 메뉴 변수 */
    TextView waitingTxt, myWorkListTxt, chatListTxt, moreTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager__chat);
        Log.d(TAG, "=== onCreate ===" );

        /* 매니저용 전체 메뉴 코드 */
        //각버튼 아이디 매칭
        waitingTxt = findViewById(R.id.waitingTxt);
        myWorkListTxt = findViewById(R.id.myWorkListTxt);
        chatListTxt = findViewById(R.id.chatListTxt);
        moreTxt = findViewById(R.id.moreTxt);

        Button.OnClickListener onClickListener = new Button.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switch (v.getId()){
                        //waitingTxt 버튼 행동
                        case R.id.waitingTxt:
                            Log.d(TAG, "=== waitingTxt ===" );
                            //있으면 넘어감
                            Intent intent = new Intent(getApplicationContext(), Manager_MainActivity.class);
                            startActivity(intent);
                            finish();

                            break;
                        //myWorkListTxt 버튼 행동
                        case R.id.myWorkListTxt:
                            Log.d(TAG, "=== myWorkListTxt ===" );
                            //있으면 넘어감
                            Intent intent2 = new Intent(getApplicationContext(), Manager_ReservationActivity.class);
                            startActivity(intent2);
                            finish();
                            break;

                        //chatListTxt 버튼 행동
                        case R.id.chatListTxt:
                            Log.d(TAG, "=== chatListTxt ===" );
                            //있으면 넘어감
                            Intent intent3 = new Intent(getApplicationContext(), Manager_ChatActivity.class);
                            startActivity(intent3);
                            finish();
                            break;

                        //moreTxt 버튼 행동
                        case R.id.moreTxt:
                            Log.d(TAG, "=== moreTxt ===" );
                            //있으면 넘어감
                            Intent intent4 = new Intent(getApplicationContext(), Manager_MoreActivity.class);
                            startActivity(intent4);
                            finish();
                            break;
                    }
                }
                };

        waitingTxt.setOnClickListener(onClickListener);
        myWorkListTxt.setOnClickListener(onClickListener);
        chatListTxt.setOnClickListener(onClickListener);
        moreTxt.setOnClickListener(onClickListener);
    }
}