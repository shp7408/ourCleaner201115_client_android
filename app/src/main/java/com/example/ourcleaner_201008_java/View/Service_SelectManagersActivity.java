package com.example.ourcleaner_201008_java.View;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ourcleaner_201008_java.Adapter.MyManagerAdapter;
import com.example.ourcleaner_201008_java.Adapter.SelectManagerAdapter;
import com.example.ourcleaner_201008_java.DTO.ManagerDTO;
import com.example.ourcleaner_201008_java.DTO.MymanagerDTO;
import com.example.ourcleaner_201008_java.DTO.ServiceDTO;
import com.example.ourcleaner_201008_java.GlobalApplication;
import com.example.ourcleaner_201008_java.Interface.ManagerSelectInterface;
import com.example.ourcleaner_201008_java.R;

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

public class Service_SelectManagersActivity extends AppCompatActivity
        implements SelectManagerAdapter.OnListItemSelectedInterface{

    /* 1. 우리동네 매니저
    *  2. 내가 이용했던 매니저
    * 목록 보여주는 화면*/
    private static final String TAG = "매니저선택엑티비티1";


    private SelectManagerAdapter selectManagerAdapter;
    private RecyclerView recyclerView;

    ArrayList<ManagerDTO> managerDTOArrayList = new ArrayList<>();


    private MyManagerAdapter myManagerAdapter;
    private RecyclerView recyclerView2;

    ArrayList<MymanagerDTO> mymanagerDTOS = new ArrayList<>();


    /* 이전 엑티비티에서 intent로 주소 받아옴 도로명 주소는 나중에.. */
    String siGunGuStr="화성시", eupmyundongStr="진안동", siGunGuStr2;

    /* 현재 엑티비티의 정보 담는 객체 */
    ServiceDTO serviceDTO;

    TextView myplaceAddressTxt;

    Boolean nextBool=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service__select_managers);
        Log.d(TAG, "=== onCreate ===" );

        /* 저장 후, 현재 엑티비티 종료 위함 -> 2. 엑티비티 객체에 현재 클래스를 담음 */
//        CActivity = Service_SelectManagersActivity.this;

        recyclerView = findViewById(R.id.select_manager_recycle);
//        recyclerView2 = findViewById(R.id.myManagerRecyclerView);

        myplaceAddressTxt = findViewById(R.id.myplaceAddressTxt);

        //intent로 받을 때 사용하는 코드
        Intent intent = getIntent();

        serviceDTO = (ServiceDTO) intent.getSerializableExtra("serviceDTO");

        siGunGuStr = serviceDTO.getMyplaceDTO().getAddress().substring(8,14); //경기 화성시
        Log.e(TAG, "=== siGunGuStr ===" +siGunGuStr);

        siGunGuStr2 = serviceDTO.getMyplaceDTO().getAddress().substring(11,14); //화성시
        Log.e(TAG, "=== siGunGuStr2 ===" +siGunGuStr2);

        myplaceAddressTxt.setText(siGunGuStr);


        fetchJSON2();

        try{
            Thread.sleep(100);

            fetchJSON();

        }catch(InterruptedException e){
            e.printStackTrace();
        }







    }

    /* 현재 사용자 이메일 보내서, 최근 방문한 매니저 목록 가져오기 */
    private void fetchJSON2(){

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ManagerSelectInterface.JSONURL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();

        ManagerSelectInterface api = retrofit.create(ManagerSelectInterface.class);

        Map<String, String> mapdata = new HashMap<>();
        mapdata.put("siGunGuStr", siGunGuStr2);
        mapdata.put("user", GlobalApplication.currentUser);
        mapdata.put("visit", "dd");

        Log.e(TAG, "siGunGuStr"+siGunGuStr2);
        Log.e(TAG, "user"+GlobalApplication.currentUser);

        Call<String> call = api.getNearManager(mapdata);

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Log.e(TAG, "Responsestring"+response.body());
                //Toast.makeText()
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        Log.e(TAG, "onSuccess"+response.body());

                        String jsonresponse = response.body();
                        writeRecycler2(jsonresponse);

                        nextBool=true;

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

    private void writeRecycler2(String response){

        String TAG2 = "3333333333";

        try {
            //getting the whole json object from the response
            JSONObject obj = new JSONObject(response);
            Log.e(TAG2, "=== response ===" +response);

            if(obj.optString("status").equals("true")){


//                    managerDTOArrayList = new ArrayList<>();
                    JSONArray dataArray  = obj.getJSONArray("data");

                    ManagerDTO managerDTOGide = new ManagerDTO();

                    managerDTOGide.setNameStr("회원님이 " +"'"+serviceDTO.getMyplaceDTO().getPlaceNameStr()+"'"+"에 예약했던 매니저님입니다.");
                    managerDTOGide.setViewType(0);

                    managerDTOArrayList.add(managerDTOGide);

                    for (int i = 0; i < dataArray.length(); i++) {

                        ManagerDTO managerDTO = new ManagerDTO();
                        JSONObject dataobj = dataArray.getJSONObject(i);

                        managerDTO.setViewType(1);
                        managerDTO.setId(dataobj.getInt("uid"));
                        managerDTO.setNameStr(dataobj.getString("nameStr"));
                        managerDTO.setEmail(dataobj.getString("idStr"));
                        managerDTO.setPhoneNumStr(dataobj.getString("phoneNumStr"));
                        managerDTO.setAddressStr(dataobj.getString("addressStr"));


                        managerDTO.setImagePathStr(dataobj.getString("profileImagePathStr"));
                        Log.e(TAG, "=== managerImgView ===" +managerDTO.getImagePathStr());


                        managerDTOArrayList.add(managerDTO);


                    }

                selectManagerAdapter = new SelectManagerAdapter(this, managerDTOArrayList, this);




                recyclerView.setAdapter(selectManagerAdapter);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
//                retrofitAdapter = new RetrofitAdapter(this,modelRecyclerArrayList);
//                recyclerView.setAdapter(retrofitAdapter);
//                recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));















//                managerDTOArrayList = new ArrayList<>();
//                JSONArray dataArray  = obj.getJSONArray("data");
//
//                ManagerDTO managerDTOGide = new ManagerDTO();
//
//                managerDTOGide.setNameStr("나의 매니저");
//                managerDTOGide.setViewType(0);
//
//                managerDTOArrayList.add(managerDTOGide);
//
//                MymanagerDTO mymanagerDTO = new MymanagerDTO();
//
//                for (int i = 0; i < dataArray.length(); i++) {
//
//                    ManagerDTO managerDTO = new ManagerDTO();
//                    JSONObject dataobj = dataArray.getJSONObject(i);
//
//                    managerDTO.setViewType(2);
//                    managerDTO.setId(dataobj.getInt("uid"));
//                    managerDTO.setNameStr(dataobj.getString("nameStr"));
//                    managerDTO.setEmail(dataobj.getString("idStr"));
//                    managerDTO.setPhoneNumStr(dataobj.getString("phoneNumStr"));
//                    managerDTO.setAddressStr(dataobj.getString("addressStr"));
//
//
//                    managerDTO.setImagePathStr(dataobj.getString("profileImagePathStr"));
//                    Log.e(TAG, "=== managerImgView ===" +managerDTO.getImagePathStr());
//
//
//                    managerDTOArrayList.add(managerDTO);
//
//                    mymanagerDTO.setId(dataobj.getInt("uid"));
//                    mymanagerDTO.setNameStr(dataobj.getString("nameStr"));
//                    mymanagerDTO.setEmail(dataobj.getString("idStr"));
//                    mymanagerDTO.setPhoneNumStr(dataobj.getString("phoneNumStr"));
//                    mymanagerDTO.setAddressStr(dataobj.getString("addressStr"));
//                    mymanagerDTO.setImagePathStr(dataobj.getString("profileImagePathStr"));
//
//                    mymanagerDTOS.add(mymanagerDTO);
//
//                }
//
//                selectManagerAdapter = new SelectManagerAdapter(this, managerDTOArrayList, this );
//
//                recyclerView.setAdapter(selectManagerAdapter);
//                recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
//
////                myManagerAdapter = new MyManagerAdapter(this,  mymanagerDTOS);
////
////                recyclerView2.setAdapter(myManagerAdapter);
////                recyclerView2.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));
//

            }else {
                Toast.makeText(Service_SelectManagersActivity.this, obj.optString("message")+"", Toast.LENGTH_SHORT).show();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void fetchJSON(){

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ManagerSelectInterface.JSONURL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();

        ManagerSelectInterface api = retrofit.create(ManagerSelectInterface.class);

        Map<String, String> mapdata = new HashMap<>();
        mapdata.put("siGunGuStr", siGunGuStr2);
        mapdata.put("user", GlobalApplication.currentUser);


        Call<String> call = api.getNearManager(mapdata);

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Log.e(TAG, "Responsestring"+response.body());
                //Toast.makeText()
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        Log.e(TAG, "onSuccess"+response.body());

                        String jsonresponse = response.body();
                        writeRecycler(jsonresponse);

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

    private void writeRecycler(String response){

        String TAG2 = "3333333333";

        try {
            //getting the whole json object from the response
            JSONObject obj = new JSONObject(response);
            Log.e(TAG2, "=== response ===" +response);

            if(obj.optString("status").equals("true")){

//                managerDTOArrayList = new ArrayList<>();
                JSONArray dataArray  = obj.getJSONArray("data");

                ManagerDTO managerDTOGide = new ManagerDTO();

                managerDTOGide.setNameStr("우리 동네 매니저");
                managerDTOGide.setViewType(0);

                managerDTOArrayList.add(managerDTOGide);

                for (int i = 0; i < dataArray.length(); i++) {

                    ManagerDTO managerDTO = new ManagerDTO();
                    JSONObject dataobj = dataArray.getJSONObject(i);

                    managerDTO.setViewType(1);
                    managerDTO.setId(dataobj.getInt("uid"));
                    managerDTO.setNameStr(dataobj.getString("nameStr"));
                    managerDTO.setEmail(dataobj.getString("idStr"));
                    managerDTO.setPhoneNumStr(dataobj.getString("phoneNumStr"));
                    managerDTO.setAddressStr(dataobj.getString("addressStr"));


                    managerDTO.setImagePathStr(dataobj.getString("profileImagePathStr"));
                    Log.e(TAG, "=== managerImgView ===" +managerDTO.getImagePathStr());


                    managerDTOArrayList.add(managerDTO);


                }

                selectManagerAdapter = new SelectManagerAdapter(this, managerDTOArrayList, this);



                recyclerView.setAdapter(selectManagerAdapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
//                retrofitAdapter = new RetrofitAdapter(this,modelRecyclerArrayList);
//                recyclerView.setAdapter(retrofitAdapter);
//                recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));

            }else {
                Toast.makeText(Service_SelectManagersActivity.this, obj.optString("message")+"", Toast.LENGTH_SHORT).show();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onItemSelected(View v, int position) {
        Log.e(TAG, "=== onItemSelected === position : "+ position);

        //있으면 넘어감
        Intent intent = new Intent(getApplicationContext(), Service_SelectManager_DetailActivity.class);

        /* 엑티비티 2개 넘는 경우,  */
        intent.addFlags(Intent.FLAG_ACTIVITY_FORWARD_RESULT);
        intent.putExtra("managerEmail", managerDTOArrayList.get(position).getEmail());
        intent.putExtra("serviceDTO", serviceDTO);

        startActivity(intent);

        //finish();

    }
}