package com.example.ourcleaner_201008_java.View;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.ourcleaner_201008_java.R;

public class PlaceinputActivity extends AppCompatActivity {

    private static final String TAG = "장소 입력";
    public static final int REQUEST_CODE_PLACE_INPUT = 1000;

    EditText placeEdit;
    String address;
    Button nextBtn;

    /* 저장 후, 현재 엑티비티 종료 위함 */
    public static Activity AActivity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_placeinput);

        Log.d(TAG, "=== onCreate ===" );

        /* 저장 후, 현재 엑티비티 종료 위함 */
        AActivity = PlaceinputActivity.this;


        placeEdit = (EditText)findViewById(R.id.placeEdit);
        placeEdit.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                if(motionEvent.getAction() == MotionEvent.ACTION_DOWN){
                    //클릭했을 경우 발생할 이벤트 작성
                    Log.d(TAG, "=== placeEdit.setOnTouchListener ===" );
                    Intent intent = new Intent(getApplicationContext(), DaumWebViewActivity.class);
                    //데이터 넘겨받기 위함
                    startActivityForResult(intent, REQUEST_CODE_PLACE_INPUT );
                }

                return false;

            }
        });




    }

    //요청코드 받을 시 발생하는 이벤트
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //요청 코드 받았을 때 발생하는 이벤트
        if (requestCode == REQUEST_CODE_PLACE_INPUT) {

            //회원가입 요청 코드 받기 성공
            if (resultCode == RESULT_OK) {
                Log.d(TAG, "=== RESULT_OK ===" );

                //intent로 받을 때 사용하는 코드
                Intent intent = getIntent();
                Log.d(TAG, "다음 주소 찾기 엑티비티에서 인텐트 받음 intent :"+intent);

                address = data.getStringExtra("address"); //onActivityResult메서드의 파라미터
                Log.d(TAG, "다음 주소 찾기 엑티비티에서 인텐트 받음 intent.address : "+ address);

                placeEdit = (EditText)findViewById(R.id.placeEdit);
                placeEdit.setText(address);
                Log.d(TAG, "=== 받아온 주소 에디트 텍스트에 넣음 ===" );

                Intent intent2 = new Intent(getApplicationContext(), Placeinput2Activity.class);
                intent2.putExtra("address", address);

                intent2.addFlags(intent.FLAG_ACTIVITY_CLEAR_TOP);
                Log.d(TAG, "=== FLAG_ACTIVITY_CLEAR_TOP ===" );

                startActivity(intent2);
                finish();


            } else {
                Log.d(TAG, "=== RESULT_OK 못 받음 ===" );
            }

        }//비밀번호 변경 요청 코드 받았을 때 발생하는 이벤트 종료

    }//onActivityResult 종료


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Log.d(TAG, "=== onBackPressed 현재 엑티비티 종료 ===" );
        finish();
    }
}