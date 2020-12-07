package com.example.ourcleaner_201008_java.View.Manager;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.example.ourcleaner_201008_java.R;
import com.example.ourcleaner_201008_java.View.PhoneAuthActivity;
import com.example.ourcleaner_201008_java.View.TermsMoreActivity;

public class Manager_Signin3Activity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener {

    private static final String TAG = "매니저회원가입3정보입력완료";

    /* 이전 엑티비티에서 intent로 받아온 값 */
    String phoneNumStr;

    CheckBox checkBox_all, checkbox_1, checkbox_2, checkbox_3, checkbox_4;
    Button nextBtn;

    Button btn_all;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager__signin3);
        Log.d(TAG, "=== onCreate ===" );

        //intent로 받을 때 사용하는 코드
        Intent intent = getIntent();

        phoneNumStr = intent.getStringExtra("phoneNumStr");
        Log.d(TAG, "휴대폰번호 인증 엑티비티에서 intent 받음 intent.phoneNumStr : "+ phoneNumStr);

        Log.d(TAG, "=== 레이아웃의 요소들 연결 ===");
        checkBox_all= (CheckBox)findViewById(R.id.checkbox_all);
        checkbox_1= (CheckBox)findViewById(R.id.checkbox_1);
        checkbox_2= (CheckBox)findViewById(R.id.checkbox_2);
        checkbox_3= (CheckBox)findViewById(R.id.checkbox_3);
        checkbox_4= (CheckBox)findViewById(R.id.checkbox_4);


        checkBox_all.setOnCheckedChangeListener(this);

        nextBtn = (Button)findViewById(R.id.nextBtn);

        btn_all = (Button) findViewById(R.id.btn_all);
        btn_all.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Log.d(TAG, "=== btn_all 클릭 : === ");

                //있으면 넘어감
                Intent intent = new Intent(getApplicationContext(), Manager_TermsMoreActivity.class);

                startActivity(intent);

            }
        });


        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "=== 다음 버튼 클릭 ===" );
                if(checkbox_1.isChecked()&&checkbox_2.isChecked()&&checkbox_3.isChecked()){

                    Log.d(TAG, "=== 필수 버튼 모두 선택 0===" );

                    //없으면 폰 인증 엑티비티로
                    Intent intent = new Intent(getApplicationContext(), Manager_Signin4Activity.class);

                    intent.putExtra("phoneNumStr", phoneNumStr);
                    Log.d(TAG, "=== 메인으로 넘어갈 때의 email ===" +phoneNumStr);

                    intent.putExtra("termsAgreeInt",0);

                    startActivity(intent);


                }else if(checkbox_1.isChecked()&&checkbox_2.isChecked()&&checkbox_3.isChecked()&&checkbox_4.isChecked()){
                    Log.d(TAG, "=== 필수 버튼 모두 선택 1===" );

                    //없으면 폰 인증 엑티비티로
                    Intent intent = new Intent(getApplicationContext(), Manager_Signin4Activity.class);

                    intent.putExtra("phoneNumStr", phoneNumStr);
                    Log.d(TAG, "=== 메인으로 넘어갈 때의 email ===" +phoneNumStr);

                    intent.putExtra("termsAgreeInt",1);

                    startActivity(intent);

                }
                else{
                    Log.d(TAG, "=== 필수 버튼 중 하나라도 선택 안되어 있는 경우 ===" );

                    Toast.makeText(getBaseContext(), "필수 항목에 동의해주세요.", Toast.LENGTH_SHORT).show();


                }
            }
        });

    }

    // 체크박스를 클릭해서 상태가 바꾸었을 경우 호출되는 콜백 메서드
    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        // 체크박스를 클릭해서 상태가 바꾸었을 경우 호출되는 콜백 메서드
        Log.d(TAG, "=== 체크박스를 클릭해서 상태가 바꾸었을 경우 호출되는 콜백 메서드 ===" );

        if(checkBox_all.isChecked()) {

            checkbox_1.setChecked(true);
            checkbox_2.setChecked(true);
            checkbox_3.setChecked(true);
            checkbox_4.setChecked(true);

            nextBtn.setEnabled(true);
            Log.d(TAG, "=== nextBtn클릭 가능 ===" );


        }else{
            checkbox_1.setChecked(false);
            checkbox_2.setChecked(false);
            checkbox_3.setChecked(false);
            checkbox_4.setChecked(false);
        }

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}