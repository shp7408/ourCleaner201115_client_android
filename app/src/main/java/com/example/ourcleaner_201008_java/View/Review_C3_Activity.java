package com.example.ourcleaner_201008_java.View;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ourcleaner_201008_java.DTO.ReviewDTO;
import com.example.ourcleaner_201008_java.R;

import java.util.HashMap;

public class Review_C3_Activity extends AppCompatActivity {

    private static final String TAG = "리뷰작성3";

    //intent로 받아온 리뷰 객체
    ReviewDTO reviewDTO = new ReviewDTO();

    RadioButton radio1, radio2, radio3, radio4;
    Button nextBtn;

    private HashMap<String, Boolean> radioPosMap = new HashMap<>(); //, radioNegMap
    private HashMap<String, Boolean> radioNegMap = new HashMap<>(); //, radioNegMap


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review__c3_);
        Log.d(TAG, "=== onCreate ===" );

        /* intent로 리뷰 객체 받음 */
        Intent intent = getIntent();
        reviewDTO = (ReviewDTO) intent.getSerializableExtra("reviewDTO");

        /* intent로 받아오는 부분 -> 나중에 받아오면 다시 정리하기*/
        radioPosMap = reviewDTO.getRadioPosMap();

        /* 클릭 여부 저장할 해쉬맵 */
        radioNegMap.put("청결", false);
        radioNegMap.put("시간엄수", false);
        radioNegMap.put("친절", false);
        radioNegMap.put("업무완료", false);

        /* 현재 엑티비티에서 사용하는 라디오 버튼 클릭 저장하는 해쉬맵 */

        radio1 = findViewById(R.id.radio1);
        radio2 = findViewById(R.id.radio2);
        radio3 = findViewById(R.id.radio3);
        radio4 = findViewById(R.id.radio4);


        if(radioPosMap.get("청결")){
            radio1.setVisibility(View.GONE);
        }

        if(radioPosMap.get("시간엄수")){
            radio2.setVisibility(View.GONE);
        }

        if(radioPosMap.get("친절")){
            radio3.setVisibility(View.GONE);
        }

        if(radioPosMap.get("업무완료")){
            radio4.setVisibility(View.GONE);
        }


        nextBtn = findViewById(R.id.nextBtn);

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "=== nextBtn ===");

                if(radio1.isChecked()){
                    radioNegMap.put("청결", true);
                }

                if(radio2.isChecked()){
                    radioNegMap.put("시간엄수", true);
                }

                if(radio3.isChecked()){
                    radioNegMap.put("친절", true);
                }

                if(radio4.isChecked()){
                    radioNegMap.put("업무완료", true);
                }


                //있으면 넘어감
                Intent intent = new Intent(getApplicationContext(), Review_C4_Activity.class);

                reviewDTO.setRadioNegMap(radioNegMap);
                intent.putExtra("reviewDTO", reviewDTO);

                Log.d(TAG, "=== setCreatedEmailStr ===" + reviewDTO.getCreatedEmailStr());
                Log.d(TAG, "=== setCreatedNameStr ===" + reviewDTO.getCreatedNameStr());
                Log.d(TAG, "=== setIdInt ===" + reviewDTO.getIdInt());

                Log.d(TAG, "=== setManagerEmailStr ===" + reviewDTO.getManagerEmailStr());
                Log.d(TAG, "=== setManagerNameStr ===" + reviewDTO.getManagerNameStr());
                Log.d(TAG, "=== setPlaceSizeStr ===" + reviewDTO.getPlaceSizeStr());
                Log.d(TAG, "=== setServiceDateStr ===" + reviewDTO.getServiceDateStr());
                Log.d(TAG, "=== setServiceDayStr ===" + reviewDTO.getServiceDayStr());
                Log.d(TAG, "=== setLikeDouble ===" + reviewDTO.getLikeDouble());
                Log.d(TAG, "=== 추가 setRadioPosMap ===" + reviewDTO.getRadioPosMap().toString());
                Log.d(TAG, "=== 추가 getRadioNegMap ===" + reviewDTO.getRadioNegMap().toString());

                startActivity(intent);


            }
        });
    }
}