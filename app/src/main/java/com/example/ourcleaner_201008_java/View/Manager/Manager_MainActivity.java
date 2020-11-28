package com.example.ourcleaner_201008_java.View.Manager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.ourcleaner_201008_java.Adapter.ManagerReservationAdapter;
import com.example.ourcleaner_201008_java.Adapter.MyPlaceAdapter;
import com.example.ourcleaner_201008_java.Adapter.RecyclerDecoration;
import com.example.ourcleaner_201008_java.DTO.ManagerWaitingDTO;
import com.example.ourcleaner_201008_java.GlobalApplication;
import com.example.ourcleaner_201008_java.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Manager_MainActivity extends AppCompatActivity implements ManagerReservationAdapter.OnListItemSelectedInterface, MyPlaceAdapter.OnListItemSelectedInterface {

    /* 매니저용 전체 메뉴 변수 */
    TextView waitingTxt, myWorkListTxt, chatListTxt, moreTxt;

    private static final String TAG = "매니저용메인";


    /* 리사이클러뷰에 보여주기 위한 변수 */
    ManagerWaitingDTO managerWaitingDTO;
    ArrayList<ManagerWaitingDTO> managerWaitingDTOArrayList = new ArrayList<>();

    // PlaceInputAdapter 리사이클러뷰 작업 2 단계.
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    /* 서버에서 내 장소 정보 받아오기 위한 변수 */
    String jsonResponse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager__main);
        Log.d(TAG, "=== onCreate ===" );

        makeStringRequestGet();
        Log.d(TAG, "=== makeJsonArrayRequest() 메서드 종료 ===" );


        /* 현재 내 장소 정보를 모두 가져온 상태. 추가한 후에도 가져온 상태임. 이제는 리사이클러 뷰에 내 장소 목록을 보여줄 것임 */
        //리사이클러 뷰가 있는 엑티비티의 리사이클러 뷰 id 연결
        recyclerView = findViewById(R.id.waiting_recycle);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);

        recyclerView.setLayoutManager(layoutManager);

        // 기본 구분선 추가
        DividerItemDecoration dividerItemDecoration =
                new DividerItemDecoration(recyclerView.getContext(),new LinearLayoutManager(this).getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);

        // 아이템 간격 처리
        RecyclerDecoration spaceDecoration = new RecyclerDecoration(5);
        recyclerView.addItemDecoration(spaceDecoration);

        recyclerView.setNestedScrollingEnabled(true);

        mAdapter = new ManagerReservationAdapter(getApplicationContext(),managerWaitingDTOArrayList, this);//리스너 구현을 위해 context 와 리스너 인자 추가함
        recyclerView.setAdapter(mAdapter);



        Log.d(TAG, "=== 리사이클러 뷰 생성 ===" );

        /* 매니저용 전체 메뉴 관련 코드 */
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


    // 현재 사용자를 url에 넣어서 보내면, 사용자가 등록한 장소 목록들을 받아오는 메서드
    private void makeStringRequestGet() {

        managerWaitingDTOArrayList = new ArrayList<>();

//        String url = "http://52.79.179.66/managerWaitingMatching.php";
        String url = "http://52.79.179.66/managerWaitingMatching.php?serviceState="+"매칭 대기 중";
        JsonArrayRequest req = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, "makeStringRequestGet onResponse 성공!!!!!!!!!!!!!!!!!!"+response.toString());

                        try {
                            // Parsing json array response
                            // loop through each json object
                            jsonResponse = "";
                            Log.d(TAG, "=== jsonResponse 반복문 이전 ===" +jsonResponse);

                            //리사이클러뷰에 여러개 추가되는 것 막음
                            managerWaitingDTOArrayList.clear();
                            final int numberOfItemsInResp = response.length();

                            for (int i = 0; i < numberOfItemsInResp; i++) {

                                Log.d(TAG, "===  === i :" + i);

                                JSONObject managerWaiting = (JSONObject) response.get(i);

                                String uid = managerWaiting.getString("uid");
                                String currentUser = managerWaiting.getString("currentUser");
                                String serviceState = managerWaiting.getString("serviceState");
                                String myplaceDTO_address = managerWaiting.getString("myplaceDTO_address");
                                String myplaceDTO_sizeStr = managerWaiting.getString("myplaceDTO_sizeStr");
                                String visitDate = managerWaiting.getString("visitDate");
                                String visitDay = managerWaiting.getString("visitDay");
                                String startTime = managerWaiting.getString("startTime");
                                String needDefTime = managerWaiting.getString("needDefTime");
                                String needDefCost = managerWaiting.getString("needDefCost");


                                jsonResponse += "uid: " + uid + "\n\n";
                                jsonResponse += "currentUser: " + currentUser + "\n\n";
                                jsonResponse += "serviceState: " + serviceState + "\n\n";
                                jsonResponse += "myplaceDTO_address: " + myplaceDTO_address + "\n\n";
                                jsonResponse += "myplaceDTO_sizeStr: " + myplaceDTO_sizeStr + "\n\n";
                                jsonResponse += "visitDate: " + visitDate + "\n\n";
                                jsonResponse += "visitDay: " + visitDay + "\n\n";
                                jsonResponse += "startTime: " + startTime + "\n\n";
                                jsonResponse += "needDefTime: " + needDefTime + "\n\n";
                                jsonResponse += "needDefCost: " + needDefCost + "\n\n";

                                String dateDayStr= (visitDate+"("+visitDay+")"); //11.26(목) 형태

                                managerWaitingDTO = new ManagerWaitingDTO(Integer.parseInt(uid), currentUser, dateDayStr, Integer.parseInt(startTime), Integer.parseInt(needDefTime), Integer.parseInt(needDefCost), myplaceDTO_address.substring(8,14), myplaceDTO_sizeStr,serviceState);
                                Log.d(TAG, "=== myplaceDTO 객체 생성 ===");


                                if(serviceState.equals("매칭 대기 중")){
                                    Log.e(TAG, "=== 매칭 대기 중인 경우에만, 리스트에 넣기 ===" );
                                    managerWaitingDTOArrayList.add(managerWaitingDTO);
                                }

                                Log.d(TAG, "=== myplaceDTOArrayList.add(myplaceDTO)에 더함 ===");

                            }

                            mAdapter.notifyDataSetChanged();
                            Log.d(TAG, "=== jsonResponse 반복문 이후 ===" +jsonResponse);

                        } catch (JSONException e) {
                            e.printStackTrace();

                            Log.d(TAG, "===  === Error " + e);
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                Log.d(TAG, "===  === Error " + String.valueOf(error));
            }
        });

        // Adding request to request queue
        GlobalApplication.getInstance().addToRequestQueue(req);
    }


    @Override
    public void onItemSelected(View v, int position) {
        Log.e(TAG, "=== position ===" +position);

        //있으면 넘어감
        Intent intent = new Intent(getApplicationContext(), Manager_Detail_MatchingPostActivity.class);

        intent.putExtra("uid", managerWaitingDTOArrayList.get(position).getUidInt());

        startActivity(intent);

        //finish();


    }

}