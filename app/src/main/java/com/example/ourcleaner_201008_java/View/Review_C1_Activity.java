package com.example.ourcleaner_201008_java.View;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ourcleaner_201008_java.DTO.ReviewDTO;
import com.example.ourcleaner_201008_java.R;

public class Review_C1_Activity extends AppCompatActivity {

    private static final String TAG = "리뷰작성1";

    RadioGroup radioGroup;
    RadioButton radio1, radio2, radio3;

    //intent로 받아온 리뷰 객체
    ReviewDTO reviewDTO = new ReviewDTO();

    TextView dateTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review__c1_);
        Log.d(TAG, "=== onCreate ===" );

        radioGroup = findViewById(R.id.radioGroup);
        radio1 = findViewById(R.id.radio1);
        radio2 = findViewById(R.id.radio2);
        radio3 = findViewById(R.id.radio3);
        dateTxt = findViewById(R.id.dateTxt);

        /* intent로 리뷰 객체 받음 */
        Intent intent = getIntent();
        reviewDTO = (ReviewDTO) intent.getSerializableExtra("reviewDTO");

        dateTxt.setText(reviewDTO.getServiceDateStr()+"("+reviewDTO.getServiceDayStr()+") 에 받은\n"+reviewDTO.getManagerNameStr()+" 매니저님의 \n서비스는 어땠나요?");



        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch(checkedId){

                    case R.id.radio1 :

                        Log.d(TAG, "=== radio1 클릭 ===" );
                        Thread thread = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try  {
                                    Log.e(TAG, "=== thread ===" );

                                    reviewDTO.setLikeDouble(1);

                                } catch (Exception e) {
                                    e.printStackTrace();
                                    Log.e(TAG, "thread 에러 코드 : "+ e);
                                }
                            }
                        });

                        thread.start();
                        Handler mHandler = new Handler(Looper.getMainLooper());
                        mHandler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Log.e(TAG, "thread : run");

                                //있으면 넘어감
                                Intent intent = new Intent(getApplicationContext(), Review_C2_Activity.class);
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

                                startActivity(intent);
                            }

                        }, 500);


                        break;

                    case R.id.radio2 :

                        Log.d(TAG, "=== radio2 클릭 ===" );
                        Thread thread2 = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try  {
                                    Log.e(TAG, "=== thread ===" );

                                    reviewDTO.setLikeDouble(2);

                                } catch (Exception e) {
                                    e.printStackTrace();
                                    Log.e(TAG, "thread 에러 코드 : "+ e);
                                }
                            }
                        });

                        thread2.start();
                        Handler mHandler2 = new Handler(Looper.getMainLooper());
                        mHandler2.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Log.e(TAG, "thread : run");

                                //있으면 넘어감
                                Intent intent = new Intent(getApplicationContext(), Review_C2_Activity.class);
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

                                startActivity(intent);
                            }

                        }, 500);


                        break;

                    case R.id.radio3 :

                        Log.d(TAG, "=== radio3 클릭 ===" );
                        Thread thread3 = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try  {
                                    Log.e(TAG, "=== thread ===" );

                                    reviewDTO.setLikeDouble(3);

                                } catch (Exception e) {
                                    e.printStackTrace();
                                    Log.e(TAG, "thread 에러 코드 : "+ e);
                                }
                            }
                        });

                        thread3.start();
                        Handler mHandler3 = new Handler(Looper.getMainLooper());
                        mHandler3.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Log.e(TAG, "thread : run");

                                //있으면 넘어감
                                Intent intent = new Intent(getApplicationContext(), Review_C2_Activity.class);
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

                                startActivity(intent);
                            }

                        }, 500);


                        break;

                  default:
                      Toast.makeText(getBaseContext(), "만족도를 체크해주세요~", Toast.LENGTH_SHORT).show();
                    break;
                }
            }
        });




    }
}