package com.example.ourcleaner_201008_java.View;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ourcleaner_201008_java.R;

import java.util.ArrayList;
import java.util.List;

public class Placeinput2Activity extends AppCompatActivity {

    private static final String TAG = "장소 정보 입력 엑티비티";

    TextView placeText;
    String address;
    String sizeStr;
    int sizeIndexint;
    Button size1Btn, size2Btn, nextBtn;
    EditText detailPlaceEdit;
    String detailAddress;

    /* 저장 후, 현재 엑티비티 종료 위함 -> 1. static으로 엑티비티 선언 */
    public static Activity BActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_placeinput2);
        Log.d(TAG, "=== onCreate ===" );

        /* 저장 후, 현재 엑티비티 종료 위함 -> 2. 엑티비티 객체에 현재 클래스를 담음 */
        BActivity = Placeinput2Activity.this;

                //intent로 받을 때 사용하는 코드
                Intent intent = getIntent();
                Log.d(TAG, "다음 주소 찾기 엑티비티에서 인텐트 받음 intent :" + intent);

                address = intent.getStringExtra("address"); //onActivityResult메서드의 파라미터
                Log.d(TAG, "다음 주소 찾기 엑티비티에서 인텐트 받음 intent.address : "+ address);

                placeText = (TextView) findViewById(R.id.placeText);
                placeText.setText(address);
                Log.d(TAG, "=== 받아온 주소 에디트 텍스트에 넣음 ===" );

                size2Btn = (Button) findViewById(R.id.size2Btn);
                nextBtn = (Button) findViewById(R.id.nextBtn);
                detailPlaceEdit = (EditText)findViewById(R.id.detailPlaceEdit);

                //sizeStr=null;
        // TODO: 2020-10-22 다이얼로그에서 평 수 선택안 한 경우 예외 처리 안함. -> 다이얼로그 콜백 메서드로 구현해야 함.

                //평 수 선택하는 다이얼로그 생성
                size1Btn = (Button) findViewById(R.id.size1Btn);

                size1Btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                            Log.d(TAG, "=== size1Btn 클릭 : === ");
                            CreateListDialog();
                            Log.d(TAG, "=== CreateListDialog(); 메서드 종료 ===" );


                            }
                    });

        size2Btn.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Log.d(TAG, "=== size2Btn 텍스트 변화 전 ===" );
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Log.d(TAG, "=== size2Btn 텍스트 변화할 때 ===" );

                size2Btn.setVisibility(View.VISIBLE);
                Log.d(TAG, "=== size2Btn 보임 ===" );

                nextBtn.setEnabled(true);
                Log.d(TAG, "=== 다음 버튼 활성화 ===" );

            }

            @Override
            public void afterTextChanged(Editable editable) {
                Log.d(TAG, "=== size2Btn 텍스트 변화후 ===" );
            }
        });


        nextBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                Log.d(TAG, "=== 다음 버튼 클릭 : === ");
                detailAddress = detailPlaceEdit.getText().toString();

                try{
                    if(sizeStr.isEmpty() || detailAddress.isEmpty()){

                        if(sizeStr.isEmpty()) {
                            Log.d(TAG, "=== 선택한 평 수가 없을 때, ===");
                            Toast.makeText(getBaseContext(), "평 수(공급 면적)를 선택해주세요.", Toast.LENGTH_SHORT).show();
                        }else if(detailAddress.isEmpty()){
                            Log.d(TAG, "=== 평수는 입력했는데, 상세주소를 입력 안 함. ===" );
                            Toast.makeText(getBaseContext(), "상세 주소를 입력해 주세요.", Toast.LENGTH_SHORT).show();
                        }else{
                            Log.d(TAG, "=== 둘 다 입력 안 함 ===" );
                            Toast.makeText(getBaseContext(), "평 수(공급 면적)를 선택해주세요.", Toast.LENGTH_SHORT).show();
                        }


                    }else{
                        Log.d(TAG, "=== 선택한 평 수가 있음 ===" );
                        Log.d(TAG, "=== detailAddress === : " + detailAddress );

                        //있으면 넘어감
                        Intent intent = new Intent(getApplicationContext(), Placeinput3Activity.class);
                        intent.putExtra("address", address);
                        intent.putExtra("detailAddress", detailAddress);
                        intent.putExtra("sizeStr", sizeStr);
                        intent.putExtra("sizeIndexint", sizeIndexint);

                        Log.d(TAG, "=== 장소 부가 정보 입력란 으로 이동 ===" );
                        startActivity(intent);

                        //finish();
                    }

                }catch (Exception e){
                    Log.d(TAG, "=== sizeStr 에러코드 === " +e);

                    if(sizeStr.isEmpty()) {
                        Log.d(TAG, "=== 선택한 평 수가 없을 때, ===");
                        Toast.makeText(getBaseContext(), "평 수(공급 면적)를 선택해주세요.", Toast.LENGTH_SHORT).show();
                    }else if(detailAddress.isEmpty()){
                        Log.d(TAG, "=== 평수는 입력했는데, 상세주소를 입력 안 함. ===" );
                        Toast.makeText(getBaseContext(), "상세 주소를 입력해 주세요.", Toast.LENGTH_SHORT).show();
                    }else{
                        Log.d(TAG, "=== 둘 다 입력 안 함 ===" );
                        Toast.makeText(getBaseContext(), "평 수(공급 면적)를 선택해주세요.", Toast.LENGTH_SHORT).show();
                    }

                    size1Btn = (Button) findViewById(R.id.size1Btn);
//                                                    size1Btn.setVisibility(View.INVISIBLE);
//                                                    Log.d(TAG, "=== 1버튼 안 보이게 함 ===" );
//                                                    nextBtn = (Button) findViewById(R.id.nextBtn);
//                                                    nextBtn.setEnabled(false);
//                                                    Log.d(TAG, "=== 다음버트 비활성화 ===" );
                }

            }

        });


//                nextBtn = (Button) findViewById(R.id.nextBtn);
//                nextBtn.setEnabled(true);
//                Log.d(TAG, "=== 다음 버튼을 클릭 가능하도록 함 ===" );
//                nextBtn.setOnClickListener(new View.OnClickListener() {
//
//                    @Override
//                    public void onClick(View view) {
//
//                            Log.d(TAG, "=== nextBtn 클릭 : === ");
//                            Log.d(TAG, "===  ===" );
//
//                            //있으면 넘어감
//                            Intent intent = new Intent(getApplicationContext(), Placeinput3Activity.class);
//                            startActivity(intent);
//                            finish();
//
//
//                            }
//
//                    });
    }





    public void CreateListDialog() {
        final List<String> ListItems = new ArrayList<>();
        ListItems.add("8평 이하");

        for (int i = 9; i < 100; i++){
            Log.d(TAG, "=== i ===" +i);
            ListItems.add(i+"평");
        }

        ListItems.add("100평 이상");

        final String[] items =  ListItems.toArray(new String[ListItems.size()]);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("서비스 받을 집의 평 수를 선택해 주세요.");
        builder.setSingleChoiceItems(items, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int index) {

                //Toast.makeText(getApplicationContext(), items[index], Toast.LENGTH_SHORT).show();
                Log.d(TAG, "=== CreateListDialog === items[index] :" + items[index] );
                
                sizeStr = items[index];
                Log.d(TAG, "=== sizeStr ===" + sizeStr);

                sizeIndexint = index;
                Log.d(TAG, "=== sizeIndexint ===" + sizeIndexint);
                
                size2Btn.setText(sizeStr);
                Log.d(TAG, "=== sizeStr에 값 입력  ===" );

                size2Btn.setTextColor(getResources().getColor(R.color.white));
                Log.d(TAG, "=== size2Btn 텍스트 색상 변경 ===" );

                dialog.dismiss();
                Log.d(TAG, "=== 다이얼로그 닫음 ===" );

            }
        });
        AlertDialog dialog = builder.create();    // 알림창 객체 생성
        dialog.show();    // 알림창 띄우기


    }


}