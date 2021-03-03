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

public class Review_C2_Activity extends AppCompatActivity {

    private static final String TAG = "리뷰작성2";

    //intent로 받아온 리뷰 객체
    ReviewDTO reviewDTO = new ReviewDTO();

    RadioButton radio1, radio2, radio3, radio4;
    Button nextBtn;

    private HashMap<String, Boolean> radioPosMap = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review__c2_);
        Log.d(TAG, "=== onCreate ===" );

        /* intent로 리뷰 객체 받음 */
        Intent intent = getIntent();
        reviewDTO = (ReviewDTO) intent.getSerializableExtra("reviewDTO");

        radioPosMap.put("청결", false);
        radioPosMap.put("시간엄수", false);
        radioPosMap.put("친절", false);
        radioPosMap.put("업무완료", false);

        radio1 = findViewById(R.id.radio1);
        radio2 = findViewById(R.id.radio2);
        radio3 = findViewById(R.id.radio3);
        radio4 = findViewById(R.id.radio4);

        nextBtn = findViewById(R.id.nextBtn);

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "=== nextBtn ===");

                if(radio1.isChecked()){
                    radioPosMap.put("청결", true);
                }

                if(radio2.isChecked()){
                    radioPosMap.put("시간엄수", true);
                }

                if(radio3.isChecked()){
                    radioPosMap.put("친절", true);
                }

                if(radio4.isChecked()){
                    radioPosMap.put("업무완료", true);
                }

                //Toast.makeText(getBaseContext(), radioPosMap.toString() , Toast.LENGTH_SHORT).show();
//                Toast.makeText(getBaseContext(), "radio1 : " +radio1.isChecked() + " / radio2 : " +radio2.isChecked() +
//                        " / radio3 : " +radio3.isChecked() +" / radio4 : " + radio4.isChecked() , Toast.LENGTH_SHORT).show();


                Intent intent = new Intent(getApplicationContext(), Review_C3_Activity.class);
                reviewDTO.setRadioPosMap(radioPosMap);
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
                Log.d(TAG, "=== setRadioPosMap ===" + reviewDTO.getRadioPosMap().toString());

                startActivity(intent);

                //finish();

            }
        });


    }
}