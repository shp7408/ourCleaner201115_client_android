package com.example.ourcleaner_201008_java.View.Manager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModel;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;

import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ourcleaner_201008_java.Adapter.ReseachBAddressAdapter;
import com.example.ourcleaner_201008_java.Adapter.RetrofitAdapter;
import com.example.ourcleaner_201008_java.DTO.AddressDTO;
import com.example.ourcleaner_201008_java.DTO.BaddressDTO;
import com.example.ourcleaner_201008_java.GlobalApplication;
import com.example.ourcleaner_201008_java.Interface.BResearchInterface;
import com.example.ourcleaner_201008_java.Interface.RecyclerInterface;
import com.example.ourcleaner_201008_java.Model.ModelRecycler;
import com.example.ourcleaner_201008_java.R;
import com.example.ourcleaner_201008_java.View.GPSInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class Manager_InputActivePlaceActivity extends AppCompatActivity implements ReseachBAddressAdapter.OnListItemSelectedInterface{

    private static final String TAG = "매니저용활동지역검색화면";

    private RetrofitAdapter retrofitAdapter; //테스트용
    private ReseachBAddressAdapter reseachBAddressAdapter;
    private RecyclerView recyclerView2; // 테스트용
    private RecyclerView recyclerView;

    EditText searchEdit;
    ImageButton delete_btn;
    TextView searchResultTxt;

    String searchStr;

    Button nowAddressBtn;

    String nowAddressStr, nowAddressStr2;

    /* 현재 위치 주소 결과 리스트  [경기도, 화성시, 진안동] */
    ArrayList<String> arrayList;

    ArrayList<BaddressDTO> baddressDTOArrayList;

    private final int PERMISSIONS_ACCESS_FINE_LOCATION = 1000;
    private final int PERMISSIONS_ACCESS_COARSE_LOCATION = 1001;
    private boolean isAccessFineLocation = false;
    private boolean isAccessCoarseLocation = false;
    private boolean isPermission = false;

    private GPSInfo gps;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager__input_active_place);
        Log.d(TAG, "=== onCreate ===" );


        recyclerView = findViewById(R.id.activeAddress_recyclerview);

        searchEdit= findViewById(R.id.searchEdit);
        delete_btn=findViewById(R.id.delete_btn);

        nowAddressBtn = findViewById(R.id.nowAddressBtn);
        searchResultTxt=findViewById(R.id.searchResultTxt);


        /* 계속 호출이 안 돼서..ㅎㅎ */
        if(!GlobalApplication.currentManagerAddress.isEmpty()){
            for(int i=0; i<3; i++) {
                whereIsHere();
            }
        }

        Toast.makeText(getBaseContext(), "현재 주소 확인 : "+nowAddressStr2, Toast.LENGTH_SHORT).show();

        fetchJSON(GlobalApplication.currentManagerAddress, null); //"화성시"로 검색하기


        /* 검색어 입력 시 이벤트 */
        searchEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Log.e(TAG, "=== beforeTextChanged 언제 호출되는지 확인하기 ===" );

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                searchStr=searchEdit.getText().toString();
                if(searchStr.length()==0){
                    Log.e(TAG, "=== searchEdit에 아무것도 없는 경우, 현재 주소를 서버에 보낼 것 ===" );
                    searchResultTxt.setVisibility(View.GONE);

                    fetchJSON(GlobalApplication.currentManagerAddress, null); //"화성시"로 검색하기

                }else{
                    Log.e(TAG, "=== searchEdit에 값이 있는 경우 키워드를 서버로 보낼 것 ===" );
                    searchResultTxt.setText("'"+searchStr+"' 검색 결과");
                    searchResultTxt.setVisibility(View.VISIBLE);
                    fetchJSON(GlobalApplication.currentManagerAddress,searchStr);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        /* 검색어 지우기 */
        delete_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e(TAG, "=== delete_btn ===" );
                searchEdit.setText("");
                searchStr="";
            }
        });


        /* 현재 위치 검색하기 */
        nowAddressBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                whereIsHere2();
            }
        });

    }

    @Override

    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        if (requestCode == PERMISSIONS_ACCESS_FINE_LOCATION
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            isAccessFineLocation = true;
        } else if (requestCode == PERMISSIONS_ACCESS_COARSE_LOCATION
                && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            isAccessCoarseLocation = true;
        }

        if (isAccessFineLocation && isAccessCoarseLocation) {
            isPermission = true;
        }
    }

    /* oncreate 후, 서버로 데이터 보내서 검색까지 */
    private void whereIsHere(){
        Log.e(TAG, "whereIsHere()");
        if(!isPermission){
            callPermission();
            return;
        }
        Log.e(TAG, "whereIsHere() isPermission");
        gps = new GPSInfo(getApplicationContext());
        if (gps.isGetLocation()) {
            Log.e(TAG, "whereIsHere() gps.isGetLocation()");
            //GPSInfo를 통해 알아낸 위도값과 경도값
            double latitude = gps.getLatitude();
            double longitude = gps.getLongitude();

            Log.e(TAG, "whereIsHere() gps.isGetLocation() latitude"+latitude);
            Log.e(TAG, "whereIsHere() gps.isGetLocation() longitude"+longitude);

            //Geocoder
            Geocoder gCoder = new Geocoder(getApplicationContext(), Locale.getDefault());
            List<Address> addr = null;

            try{
                addr = gCoder.getFromLocation(latitude,longitude,1);
                Address a = addr.get(0);

                for (int i=0;i <= a.getMaxAddressLineIndex();i++) {
                    //여기서 변환된 주소 확인할  수 있음
                    Log.e(TAG, "whereIsHere AddressLine(" + i + ") : " + a.getAddressLine(i) + "\n ");

                    // TODO: 2020-12-12 위치 바뀔 시, 고쳐야 할 수 있음..
                    nowAddressStr2 = a.getAddressLine(i);
                    Log.e(TAG, "whereIsHere nowAddressStr  : " + nowAddressStr);

                    nowAddressStr = nowAddressStr2.replace("대한민국 ", "");
                    Log.e(TAG, "whereIsHere nowAddressStr 대한민국 제거 : " + nowAddressStr);

                    nowAddressStr = nowAddressStr.replaceAll("-", "");
                    Log.e(TAG, "whereIsHere nowAddressStr 문자 제거 : " + nowAddressStr);

                    nowAddressStr = nowAddressStr.replaceAll("[0-9]", "");
                    Log.e(TAG, "whereIsHere nowAddressStr 숫자 제거 : " + nowAddressStr);

                    String[] array = nowAddressStr.split(" ");

                    arrayList= new ArrayList<>();

                    for(int j=0;j<array.length;j++) {
                        Log.e(TAG, "whereIsHere array[j] : " + array[j]);
                        arrayList.add(array[j]);
                    }

                    Log.e(TAG, "whereIsHere arrayList : " + arrayList.toString());

//                    fetchJSON(arrayList.get(1), ""); //"화성시"로 검색하기

                }

            } catch (IOException e){
                e.printStackTrace();
                Log.e(TAG, "whereIsHere() gCoder.getFromLocation 에러코드 e"+e);

            }

            if (addr != null) {
                if (addr.size()==0) {
                    Toast.makeText(getApplicationContext(),"주소정보 없음", Toast.LENGTH_LONG).show();
                }

                Log.e(TAG, "whereIsHere() addr != null");
            }
        } else {
            // GPS 를 사용할수 없으므로
            gps.showSettingsAlert();
            Log.e(TAG, "whereIsHere() GPS 를 사용할수 없으므로 gps.showSettingsAlert()");
        }

    }

    private void whereIsHere2(){
        Log.e(TAG, "whereIsHere2()");

        if(!isPermission){
            callPermission();
            return;
        }

        gps = new GPSInfo(getApplicationContext());
        if (gps.isGetLocation()) {

            //GPSInfo를 통해 알아낸 위도값과 경도값
            double latitude = gps.getLatitude();
            double longitude = gps.getLongitude();

            //Geocoder
            Geocoder gCoder = new Geocoder(getApplicationContext(), Locale.getDefault());
            List<Address> addr = null;

            try{
                addr = gCoder.getFromLocation(latitude,longitude,1);
                Address a = addr.get(0);

                for (int i=0;i <= a.getMaxAddressLineIndex();i++) {
                    //여기서 변환된 주소 확인할  수 있음
                    Log.e(TAG, "whereIsHere AddressLine(" + i + ") : " + a.getAddressLine(i) + "\n ");

                    // TODO: 2020-12-12 위치 바뀔 시, 고쳐야 할 수 있음..
                    nowAddressStr2 = a.getAddressLine(i);
                    Log.e(TAG, "whereIsHere nowAddressStr  : " + nowAddressStr);

                    nowAddressStr = nowAddressStr2.replace("대한민국 ", "");
                    Log.e(TAG, "whereIsHere nowAddressStr 대한민국 제거 : " + nowAddressStr);

                    nowAddressStr = nowAddressStr.replaceAll("-", "");
                    Log.e(TAG, "whereIsHere nowAddressStr 문자 제거 : " + nowAddressStr);

                    nowAddressStr = nowAddressStr.replaceAll("[0-9]", "");
                    Log.e(TAG, "whereIsHere nowAddressStr 숫자 제거 : " + nowAddressStr);

                    String[] array = nowAddressStr.split(" ");

                    arrayList= new ArrayList<>();

                    for(int j=0;j<array.length;j++) {
                        Log.e(TAG, "whereIsHere array[j] : " + array[j]);
                        arrayList.add(array[j]);
                    }

                    Log.e(TAG, "whereIsHere arrayList : " + arrayList.toString());
                    GlobalApplication.currentManagerAddress=arrayList.toString();

                }

            } catch (IOException e){
                e.printStackTrace();
            }

            if (addr != null) {
                if (addr.size()==0) {
                    Toast.makeText(getApplicationContext(),"주소정보 없음", Toast.LENGTH_LONG).show();
                }
            }
        } else {
            // GPS 를 사용할수 없으므로
            gps.showSettingsAlert();
        }

    }

    // 전화번호 권한 요청
    private void callPermission() {
        // Check the SDK version and whether the permission is already granted or not.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                && ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_ACCESS_FINE_LOCATION);

        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                && ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED){
            requestPermissions(
                    new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION},
                    PERMISSIONS_ACCESS_COARSE_LOCATION);
        } else {
            isPermission = true;
        }
    }


    private void fetchJSON(String nowAddress, String researchKeyword){

        /* timeout 문제 해결을 위함 */
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(120, TimeUnit.SECONDS)
                .writeTimeout(120, TimeUnit.SECONDS)
                .readTimeout(120, TimeUnit.SECONDS)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BResearchInterface.JSONURL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .client(client)
                .build();

        BResearchInterface api = retrofit.create(BResearchInterface.class);

        /* 인터페이스에서 정의한 메서드 / 인자로 보낼 값 넣는 곳 */
        Call<String> call = api.getBAddress(nowAddress, researchKeyword);

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Log.e("Responsestring", response.body().toString());
                //Toast.makeText()
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        Log.e("onSuccess", response.body().toString());

                        String jsonresponse = response.body().toString();
                        writeRecycler(jsonresponse);

                    } else {
                        Log.e("onEmptyResponse", "Returned empty response");
                        //Toast.makeText(getContext(),"Nothing returned",Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

                Log.e(TAG, "=== onFailure call ===" +call+" t"+t);

            }
        });
    }


    private void writeRecycler(String response){

        try {
            //getting the whole json object from the response
            JSONObject obj = new JSONObject(response);
            if(obj.optString("status").equals("true")){

                baddressDTOArrayList = new ArrayList<>();
                JSONArray dataArray  = obj.getJSONArray("data");

                for (int i = 0; i < dataArray.length(); i++) {

                    BaddressDTO baddressDTO = new BaddressDTO();
                    JSONObject dataobj = dataArray.getJSONObject(i);

                    baddressDTO.setbCode(dataobj.getString("bCode"));
                    baddressDTO.setSidoName(dataobj.getString("sidoName"));
                    baddressDTO.setSigunguName(dataobj.getString("sigunguName"));
                    baddressDTO.setEupmyundongName(dataobj.getString("eupmyundongName"));
                    baddressDTO.setDongliName(dataobj.getString("dongliName"));
                    baddressDTO.setBirth(dataobj.getString("birth"));

                    /* 읍면동 없는 경우는 제외하고 보여줌 */
                    if(dataobj.getString("eupmyundongName").replace(" ", "").length()==0){
                        Log.d(TAG, "=== 읍면동 값 없음 ===" );
                    }else{
                        baddressDTOArrayList.add(baddressDTO);
                    }


                }

                reseachBAddressAdapter = new ReseachBAddressAdapter(this,baddressDTOArrayList, this);
                recyclerView.setAdapter(reseachBAddressAdapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));

                /* 무한 스크롤 페이징 처리. 라이브러리 안씀.. */
                recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

                    @Override
                    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                        super.onScrollStateChanged(recyclerView, newState);

                        Log.d(TAG, "=== addOnScrollListener onScrollStateChanged ===" );
                    }

                    @Override
                    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                        super.onScrolled(recyclerView, dx, dy);

                        Log.d(TAG, "=== addOnScrollListener onScrolled ===" );

                        int lastVisibleItemPosition = ((LinearLayoutManager) recyclerView.getLayoutManager()).findLastCompletelyVisibleItemPosition();
                        int itemTotalCount = recyclerView.getAdapter().getItemCount();

                        if (lastVisibleItemPosition == itemTotalCount) {
                            //리스트 마지막(바닥) 도착!!!!! 다음 페이지 데이터 로드!!
                            Log.e(TAG, "===리스트 마지막(바닥) 도착!!!!! 다음 페이지 데이터 로드!! ===lastVisibleItemPosition" +lastVisibleItemPosition);
                            Log.e(TAG, "===리스트 마지막(바닥) 도착!!!!! 다음 페이지 데이터 로드!! ===itemTotalCount" +itemTotalCount);

                        }
                    }
                });


            }else {
                Log.e(TAG, "=== 읍면동 값 없음 ===" + obj.optString("message"));
//                Toast.makeText(Manager_InputActivePlaceActivity.this, obj.optString("message")+"", Toast.LENGTH_SHORT).show();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    @Override
    public void onItemSelected(View v, int position) {
        Log.e(TAG, "=== onItemSelected ===" );

        BaddressDTO baddressDTO = baddressDTOArrayList.get(position);

        //있으면 넘어감
        Intent intent = new Intent();
        intent.putExtra("baddressDTO", baddressDTO);

        //있으면 넘어감
        setResult(RESULT_OK, intent);
        finish();
    }
}
