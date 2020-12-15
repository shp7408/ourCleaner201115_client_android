package com.example.ourcleaner_201008_java.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ourcleaner_201008_java.Adapter.SelectManagerAdapter;
import com.example.ourcleaner_201008_java.CircleTransform;
import com.example.ourcleaner_201008_java.DTO.ManagerDTO;
import com.example.ourcleaner_201008_java.Interface.ManagerSelectDetailInterface;
import com.example.ourcleaner_201008_java.Interface.ManagerSelectInterface;
import com.example.ourcleaner_201008_java.R;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class Service_SelectManager_DetailActivity extends AppCompatActivity {

    private static final String TAG = "매니저디테일정보";

    String managerEmail;
    int managerUidStr;

    ImageView profileImage;
    TextView nameTxt,phoneNumTxt, reviewScoreNumTxt, reviewMoreTxt;
    RatingBar ratingBar;
    Button place1Btn, place2Btn, place3Btn, place4Btn, place5Btn, chooseBtn;


    Service_SelectManagersActivity cActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service__select_manager__detail);
        Log.d(TAG, "=== onCreate ===" );

        /* 장소등록엑티비티, 장소등록엑티비티2 삭제하기 위한 코드  3.  */
        cActivity = (Service_SelectManagersActivity) Service_SelectManagersActivity.CActivity;

        //intent로 받을 때 사용하는 코드
        Intent intent = getIntent();
        Log.d(TAG, "약관동의 엑티비티에서 인텐트 받음 intent :"+intent);

        managerEmail = intent.getStringExtra("managerEmail");
        Log.d(TAG, "이전 엑티비티에서 managerEmail 받아옴 이걸로 본인 데이터 받아오기 : "+ managerEmail);

        profileImage = findViewById(R.id.profileImage);

        nameTxt = findViewById(R.id.nameTxt);
        phoneNumTxt = findViewById(R.id.phoneNumTxt);
        reviewScoreNumTxt = findViewById(R.id.reviewScoreNumTxt);
        reviewMoreTxt = findViewById(R.id.reviewMoreTxt);

        ratingBar = findViewById(R.id.ratingBar);

        place1Btn = findViewById(R.id.place1Btn);
        place2Btn = findViewById(R.id.place2Btn);
        place3Btn = findViewById(R.id.place3Btn);
        place4Btn = findViewById(R.id.place4Btn);
        place5Btn = findViewById(R.id.place5Btn);

        chooseBtn = findViewById(R.id.chooseBtn);

        place1Btn.setVisibility(View.GONE);
        place2Btn.setVisibility(View.GONE);
        place3Btn.setVisibility(View.GONE);
        place4Btn.setVisibility(View.GONE);
        place5Btn.setVisibility(View.GONE);

        fetchJSON();

        chooseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e(TAG, "=== chooseBtn ===" );

                //있으면 넘어감
                Intent intent = new Intent();
                intent.putExtra("managerNameEmailStr", nameTxt.getText().toString()+","+managerEmail);

                setResult(1, intent); //Service1_TimeActivity.java 에서 미리 설정한 코드를 넣음

                /* 장소등록엑티비티, 장소등록엑티비티2 삭제하기 위한 코드  4. finish() */
                cActivity.finish();
                finish();

            }
        });

    }

    private void fetchJSON(){

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ManagerSelectDetailInterface.JSONURL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();

        ManagerSelectDetailInterface api = retrofit.create(ManagerSelectDetailInterface.class);

        Map<String, String> mapdata = new HashMap<>();
        mapdata.put("managerEmail", managerEmail);

        Log.e(TAG, "managerEmail" + managerEmail);

        Call<String> call = api.getManagerDetail(mapdata);

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Log.e(TAG, "Responsestring" + response.body());
                //Toast.makeText()
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        Log.e(TAG, "onSuccess" + response.body());

                        String jsonresponse = response.body();
                        Log.e(TAG, "=== jsonresponse ===" +jsonresponse );

                        try {
                            JSONObject obj = new JSONObject(jsonresponse);
                            Log.e(TAG, "=== response ===" +response);

                            if(obj.optString("status").equals("true")){

                                JSONArray dataArray  = obj.getJSONArray("data");

                                // TODO: 2020-12-15 여기서 해당 텍스트 뷰에 데이터 넣음

                                for (int i = 0; i < dataArray.length(); i++) {

                                    JSONObject dataobj = dataArray.getJSONObject(i);

                                    managerUidStr = dataobj.getInt("uid");
                                    nameTxt.setText(dataobj.getString("nameStr"));
                                    phoneNumTxt.setText(dataobj.getString("phoneNumStr"));

                                    Picasso.get()
                                            .load( dataobj.getString("profileImagePathStr"))
                                            .transform(new CircleTransform())
                                            .into(profileImage);

                                    dataobj.getString("addressStr");
                                    String string = dataobj.getString("desiredWorkAreaList");
                                    string = string.replace("[","");
                                    string = string.replace("]","");

                                    String[] array = string.split(",");
                                    Log.e(TAG, "=== array ===" + array );

                                    /* 어레이리스트로 변환 */
                                    ArrayList<String> arrayList = new ArrayList<>();

                                    for(String temp : array){
                                        arrayList.add(temp);
                                    }

                                    /* 어레이리스트 */
                                    if(arrayList.size()==5){

                                        place1Btn.setVisibility(View.VISIBLE);
                                        place2Btn.setVisibility(View.VISIBLE);
                                        place3Btn.setVisibility(View.VISIBLE);
                                        place4Btn.setVisibility(View.VISIBLE);
                                        place5Btn.setVisibility(View.VISIBLE);

                                        place1Btn.setText(arrayList.get(0));
                                        place2Btn.setText(arrayList.get(1));
                                        place3Btn.setText(arrayList.get(2));
                                        place4Btn.setText(arrayList.get(3));
                                        place5Btn.setText(arrayList.get(4));

                                    }else if(arrayList.size()==4){

                                        place1Btn.setVisibility(View.VISIBLE);
                                        place2Btn.setVisibility(View.VISIBLE);
                                        place3Btn.setVisibility(View.VISIBLE);
                                        place4Btn.setVisibility(View.VISIBLE);

                                        place1Btn.setText(arrayList.get(0));
                                        place2Btn.setText(arrayList.get(1));
                                        place3Btn.setText(arrayList.get(2));
                                        place4Btn.setText(arrayList.get(3));

                                    }else if(arrayList.size()==3){

                                        place1Btn.setVisibility(View.VISIBLE);
                                        place2Btn.setVisibility(View.VISIBLE);
                                        place3Btn.setVisibility(View.VISIBLE);

                                        place1Btn.setText(arrayList.get(0));
                                        place2Btn.setText(arrayList.get(1));
                                        place3Btn.setText(arrayList.get(2));

                                    }else if(arrayList.size()==2){

                                        place1Btn.setVisibility(View.VISIBLE);
                                        place2Btn.setVisibility(View.VISIBLE);

                                        place1Btn.setText(arrayList.get(0));
                                        place2Btn.setText(arrayList.get(1));

                                    }else if(arrayList.size()==1){

                                        place1Btn.setVisibility(View.VISIBLE);

                                        place1Btn.setText(arrayList.get(0));

                                    }else{

                                    }

                                }
                       }else {
                                Toast.makeText(Service_SelectManager_DetailActivity.this, obj.optString("message")+"", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    } else {
                        Log.e(TAG, "onEmptyResponse"+"Returned empty response");//Toast.makeText(getContext(),"Nothing returned",Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e(TAG, "onFailure");

            }
        });
    }

}