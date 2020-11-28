package com.example.ourcleaner_201008_java.View.Manager;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.ourcleaner_201008_java.R;
import com.example.ourcleaner_201008_java.View.MoreService_Acount_Activity;
import com.example.ourcleaner_201008_java.View.MoreService_Area_Activity;
import com.example.ourcleaner_201008_java.View.MoreService_MyPlace_Activity;
import com.example.ourcleaner_201008_java.View.MoreService_Payment_Activity;

public class Manager_MoreActivity extends AppCompatActivity {

    private static final String TAG = "매니저용더보기";

    /* 매니저용 전체 메뉴 변수 */
    TextView waitingTxt, myWorkListTxt, chatListTxt, moreTxt;
    TextView moreMyAcount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager__more);
        Log.d(TAG, "=== onCreate ===" );


        moreMyAcount = findViewById(R.id.moreMyAcount);
        moreMyAcount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "=== moreMyAcount 버튼 클릭 ===" );

                //있으면 넘어감
                Intent intent = new Intent(getApplicationContext(), Manager_Acount_Activity.class);
                startActivity(intent);

            }
        });




        /* 매니저용 전체 메뉴 코드 */
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