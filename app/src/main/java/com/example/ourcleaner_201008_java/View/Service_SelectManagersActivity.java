package com.example.ourcleaner_201008_java.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.ourcleaner_201008_java.Adapter.SelectManagerAdapter;
import com.example.ourcleaner_201008_java.DTO.ManagerDTO;
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

public class Service_SelectManagersActivity extends AppCompatActivity implements SelectManagerAdapter.OnListItemSelectedInterface{

    private static final String TAG = "매니저선택엑티비티1";

    Spinner spinner;

    private SelectManagerAdapter selectManagerAdapter;
    private RecyclerView recyclerView;

    ArrayList<ManagerDTO> managerDTOArrayList;

    /* 이전 엑티비티에서 intent로 주소 받아옴 도로명 주소는 나중에.. */
    String siGunGuStr="화성시", eupmyundongStr="진안동";

    /* 저장 후, 현재 엑티비티 종료 위함 -> 1. static으로 엑티비티 선언 */
    public static Activity CActivity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service__select_managers);
        Log.d(TAG, "=== onCreate ===" );

        /* 저장 후, 현재 엑티비티 종료 위함 -> 2. 엑티비티 객체에 현재 클래스를 담음 */
        CActivity = Service_SelectManagersActivity.this;

        spinner = findViewById(R.id.spinner);

        recyclerView = findViewById(R.id.select_manager_recycle);

        fetchJSON();

//        spinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                Log.e(TAG, "=== onItemClick ===" +i);
//            }
//        });



    }

    private void fetchJSON(){

        //인스턴스 변수로 해서
//        final String siGunGuStr = etUname.getText().toString().trim();
//        final String eupmyundongStr = etPass.getText().toString().trim();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ManagerSelectInterface.JSONURL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();

        ManagerSelectInterface api = retrofit.create(ManagerSelectInterface.class);

        Map<String, String> mapdata = new HashMap<>();
        mapdata.put("siGunGuStr", siGunGuStr);
        mapdata.put("eupmyundongStr", eupmyundongStr);

        Log.e(TAG, "3333333333 siGunGuStr"+siGunGuStr);
        Log.e(TAG, "3333333333 eupmyundongStr"+eupmyundongStr);

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

                managerDTOArrayList = new ArrayList<>();
                JSONArray dataArray  = obj.getJSONArray("data");

                for (int i = 0; i < dataArray.length(); i++) {

                    ManagerDTO managerDTO = new ManagerDTO();
                    JSONObject dataobj = dataArray.getJSONObject(i);

                    managerDTO.setId(dataobj.getInt("uid"));
                    managerDTO.setNameStr(dataobj.getString("nameStr"));
                    managerDTO.setEmail(dataobj.getString("idStr"));
                    managerDTO.setPhoneNumStr(dataobj.getString("phoneNumStr"));
                    managerDTO.setAddressStr(dataobj.getString("addressStr"));


                    managerDTO.setImagePathStr(dataobj.getString("profileImagePathStr"));


//                    JSONArray jsonArray = dataobj.getJSONArray("desiredWorkAreaList");
//
//                    /* jsonarray를 array로  */
//                    ArrayList<String> desiredWorkAreaList = new ArrayList<>();
//
//                    if (jsonArray != null) {
//                        for ( int j=0 ; j<jsonArray.length() ; j++ ){
//                            desiredWorkAreaList.add(jsonArray.getString(j));
//                        }
//                    }
//                    Log.e(TAG2, "=== desiredWorkAreaList ===" +desiredWorkAreaList.toString());
//
//                    managerDTO.setDesiredWorkAreaList(desiredWorkAreaList);

                    managerDTOArrayList.add(managerDTO);
/*
                    modelRecycler.setImgURL(dataobj.getString("imgURL"));
                    modelRecycler.setName(dataobj.getString("name"));
                    modelRecycler.setCountry(dataobj.getString("country"));
                    modelRecycler.setCity(dataobj.getString("city"));

                    modelRecyclerArrayList.add(modelRecycler);
*/

                }

                selectManagerAdapter = new SelectManagerAdapter(this, managerDTOArrayList, this );

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


        startActivity(intent);

        //finish();

    }
}