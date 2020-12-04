package com.example.ourcleaner_201008_java.View.Manager;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.ourcleaner_201008_java.R;

public class Manager_Signin1Activity extends AppCompatActivity {

    private static final String TAG = "매니저회원가입1전화인증";

    EditText phoneNumEdit;
    String phoneNumStr;
    Button cetNumBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager__signin1);
        Log.d(TAG, "=== onCreate ===" );

        phoneNumEdit = findViewById(R.id. phoneNumEdit);
        /* phoneNumEdit 전화번호 입력 시, 전화번호 형식으로 -> xml에서는 inputype phone으로 해야 함.*/
        phoneNumEdit.addTextChangedListener(new PhoneNumberFormattingTextWatcher());

        /* 글자 수 11자 되는 경우, 다음 버튼 활성화 위함 */
        phoneNumEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Log.d(TAG, "=== phoneNumEdit에 값이 입력된 경우, ===" );
                if(phoneNumEdit.getText().toString().length()==11){
                    cetNumBtn.setEnabled(true);
                    Log.d(TAG, "=== phoneNumEdit의 자릿 수가, 11인 경우, cetNumBtn 활성화 ===" );
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        cetNumBtn = (Button) findViewById(R.id.cetNumBtn);
        cetNumBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "=== cetNumBtn 클릭 : === ");
                phoneNumStr = phoneNumEdit.getText().toString();

                //있으면 넘어감
                Intent intent = new Intent(getApplicationContext(), Manager_Signin2Activity.class);

                intent.putExtra("phoneNumStr", phoneNumStr);

                startActivity(intent);

                //finish();
            }
            });
    }
}