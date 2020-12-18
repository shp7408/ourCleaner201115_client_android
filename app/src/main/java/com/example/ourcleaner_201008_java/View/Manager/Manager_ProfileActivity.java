package com.example.ourcleaner_201008_java.View.Manager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import androidx.loader.content.CursorLoader;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.ourcleaner_201008_java.DTO.AddressDTO;
import com.example.ourcleaner_201008_java.DTO.BaddressDTO;
import com.example.ourcleaner_201008_java.GlobalApplication;
import com.example.ourcleaner_201008_java.Interface.ManagerProfileInterface;
import com.example.ourcleaner_201008_java.R;
import com.example.ourcleaner_201008_java.Server.LoginRequest;
import com.example.ourcleaner_201008_java.Server.ManagerSelectRequest;
import com.example.ourcleaner_201008_java.Server.VolleyMultipartRequest;
import com.example.ourcleaner_201008_java.SharedP.PreferenceManager_Manager;

import org.apache.commons.io.output.ByteArrayOutputStream;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;


public class Manager_ProfileActivity extends AppCompatActivity {

    private static final String TAG = "매니저용프로필화면";

    /* 이미지 가져오기 위해서 예제에서 가져옴 */
    private static final String ROOT_URL = "http://52.79.179.66/profileUploads.php";
    private static final int REQUEST_PERMISSIONS = 100;
    private static final int PICK_IMAGE_REQUEST =1 ;
    private Bitmap bitmap;
    private String filePath, fileToServer; //현재 기기에서의 경로, 서버에 저장될 때의 경로

    /* 저장 시, 변경한 부분 체크 하는 변수 -> 완전히 필요한지는 모르겠으나, 일단 작성 */
    int PROFILE_CHANGE_CODE = 0, ADDRESS_CHANGE_CODE = 0, DESIRED_AREA_LIST_CHANGE_CODE=0;
    // 1인 경우, 기본 이미지로 변경
    // 2인 경우, 다른 이미지로 변경

    ImageView profileImage;
    TextView nameTxt, emailTxt, phoneNumChangeTxt, phoneNumTxt, addressChangeTxt, addressTxt, placeNumTxt, placePlusTxt, profNumTxt, profPlusTxt;
    Button place1Btn, place2Btn, place3Btn, place4Btn, place5Btn, modifyBtn;
    Button mungiBtn, semyundaeBtn, gasCleanBtn, hasuguBtn, laundryBtn, windowBtn, verandaBtn, garbageBtn, ironBtn, fridgeBtn;

    HorizontalScrollView scrollView1, scrollView2;

    TextView placeBlankTxt, profBlankTxt;

    /* 서버에 저장하기 위한 주소 객체 */
    AddressDTO addressDTOforServer;
    BaddressDTO baddressDTOforServer;

    /* 서버에 이미지 저장할 때, 서버의 매니저 정보에서 프로필이미지 DB에 경로저장하기 위해서 인스턴스 변수로 */
    long imagename;

    ArrayList<String> stringBAddressDTOArrayList = new ArrayList<>();; //저장, 보여주기 위함. 검색 시, 중복 확인 위함


    /* 서버에서 매니저 정보 불러올 때, 필요한 변수 */
    String uid, nameStr, phoneNumStr, addressStr="", desiredWorkAreaList="", profileImagePathStr="", professionalList="", activeInt="";


    /* 주소 검색 엑티비티에서 주소 변경해서 왔을 때 int */
    private static final int REQUEST_ADDRESS = 200;
    private static final int REQUEST_ACTIVE_ADDRESS = 300;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager__profile);
        Log.d(TAG, "=== onCreate ===" );

        profileImage = findViewById(R.id.profileImage);
        nameTxt = findViewById(R.id.nameTxt);
        emailTxt = findViewById(R.id.emailTxt);
        phoneNumChangeTxt = findViewById(R.id.phoneNumChangeTxt);
        phoneNumTxt = findViewById(R.id.phoneNumTxt);
        addressChangeTxt = findViewById(R.id.addressChangeTxt);
        addressTxt = findViewById(R.id.addressTxt);

        placeNumTxt = findViewById(R.id.placeNumTxt);
        placePlusTxt = findViewById(R.id.placePlusTxt);
        placeBlankTxt = findViewById(R.id.placeBlankTxt);

//        stringBAddressDTOArrayList = new ArrayList<>();

        scrollView1=findViewById(R.id.scrollview1);
//        scrollView2=findViewById(R.id.scrollview2);

        place1Btn = findViewById(R.id.place1Btn);
        place2Btn = findViewById(R.id.place2Btn);
        place3Btn = findViewById(R.id.place3Btn);
        place4Btn = findViewById(R.id.place4Btn);
        place5Btn = findViewById(R.id.place5Btn);

//        profNumTxt = findViewById(R.id.profNumTxt);
//        profPlusTxt = findViewById(R.id.profPlusTxt);
//        profBlankTxt = findViewById(R.id.profBlankTxt);
//
//        mungiBtn = findViewById(R.id.mungiBtn);
//        semyundaeBtn = findViewById(R.id.semyundaeBtn);
//        gasCleanBtn = findViewById(R.id.gasCleanBtn);
//        hasuguBtn = findViewById(R.id.hasuguBtn);
//        laundryBtn = findViewById(R.id.laundryBtn);
//        windowBtn = findViewById(R.id.windowBtn);
//        verandaBtn = findViewById(R.id.verandaBtn);
//        garbageBtn = findViewById(R.id.garbageBtn);
//        ironBtn = findViewById(R.id.ironBtn);
//        fridgeBtn = findViewById(R.id.fridgeBtn);

        modifyBtn = findViewById(R.id.modifyBtn);




        /* 서버에서 매니저 데이터 받아오는 부분. */
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e(TAG, "=== response ===" +response);
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    if(jsonArray.length()>0){
                        Log.e(TAG, "===매니저의 프로필 정보 받아옴 ===" + response);

                            JSONObject jsonObject = jsonArray.getJSONObject(0);

                            uid = jsonObject.getString( "uid" );
                            nameStr = jsonObject.getString( "nameStr" );
                            phoneNumStr = jsonObject.getString( "phoneNumStr" );

                            addressStr = jsonObject.getString( "addressStr" );

                            desiredWorkAreaList = jsonObject.getString( "desiredWorkAreaList" );
                            profileImagePathStr = jsonObject.getString( "profileImagePathStr" );
                            professionalList = jsonObject.getString( "professionalList" );

                            activeInt = jsonObject.getString( "activeInt" );



                            Log.e(TAG, "=== uid ===" +uid);
                            Log.e(TAG, "=== nameStr ===" +nameStr);
                            Log.e(TAG, "=== phoneNumStr ===" +phoneNumStr);

                            Log.e(TAG, "=== addressStr ===" +addressStr);

                            Log.e(TAG, "=== desiredWorkAreaList ===" +desiredWorkAreaList);
                            Log.e(TAG, "=== profileImagePathStr ===" +profileImagePathStr);
                            Log.e(TAG, "=== professionalList ===" +professionalList);

                            Log.e(TAG, "=== activeInt ===" +activeInt);



                            // TODO: 2020-12-10 받아온 데이터 사용할 수 있도록 가공하기
                            /*
                            * 1. addressStr(객체)
                            * 2. desiredWorkAreaList(리스트)
                            * 3. professionalList(리스트트)
                           *
                            * */




                        /* 1. 프로필 이미지 */
                        if(profileImagePathStr.equals("null") || profileImagePathStr.equals("")){
                            Log.d(TAG, "=== 저장된 프로필 이미지가 없는 경우,==="+profileImagePathStr );
                            Log.e(TAG, "=== PROFILE_CHANGE_CODE ===" +PROFILE_CHANGE_CODE);

                            Glide.with(Manager_ProfileActivity.this)
                                    .load(getDrawable(R.drawable.ic_baseline_person_24))
                                    .circleCrop()
                                    .into(profileImage);



                            profileImage.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Log.d(TAG, "=== profileImage 클릭 : 갤러리 이동 선택하는 다이얼로그 생성 함 === ");

                                    /* 이미지 뷰 클릭 시, 일어나는 클릭 이벤트 */
                                    if(PROFILE_CHANGE_CODE==0){
                                        Log.e(TAG, "=== profileImage 클릭 : 갤러리 이동 선택하는 다이얼로그 생성 함 === ");

                                        if ((ContextCompat.checkSelfPermission(getApplicationContext(),
                                                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) && (ContextCompat.checkSelfPermission(getApplicationContext(),
                                                Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)) {
                                            if ((ActivityCompat.shouldShowRequestPermissionRationale(Manager_ProfileActivity.this,
                                                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) && (ActivityCompat.shouldShowRequestPermissionRationale(Manager_ProfileActivity.this,
                                                    Manifest.permission.READ_EXTERNAL_STORAGE))) {

                                            } else {
                                                ActivityCompat.requestPermissions(Manager_ProfileActivity.this,
                                                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE},
                                                        REQUEST_PERMISSIONS);
                                            }
                                        } else {
                                            Log.e(TAG, "퍼미션 허용 함");
                                            showFileChooser();
                                        }

                                    }else{
                                        /* 이미지 가져오고 난 후에 또, 기본이미지로 변경하고싶을 수도 있음음 */
                                       Log.e(TAG, "=== profileImage 클릭 : 기본 이미지와 갤러리이동 을 선택 === ");

                                        AlertDialog.Builder builder = new AlertDialog.Builder(Manager_ProfileActivity.this);
                                        builder.setItems(R.array.ProfileImageChoose, new DialogInterface.OnClickListener(){
                                            @Override
                                            public void onClick(DialogInterface dialog, int pos)
                                            {
                                                String[] items = getResources().getStringArray(R.array.ProfileImageChoose);
//                                                Toast.makeText(getApplicationContext(),items[pos],Toast.LENGTH_LONG).show();

                                                if(pos==0){
                                                    Log.e(TAG, "=== pos== 0 ===" );
                                                    Log.e(TAG, "=== 기본 이미지로 변경 === ");

                                                    Glide.with(Manager_ProfileActivity.this)
                                                            .load(getDrawable(R.drawable.ic_baseline_person_24))
                                                            .circleCrop()
                                                            .into(profileImage);

                                                    /* 이미지에 변경 있음 1 : 기본 이미지 / 2: 다른 이미지*/
                                                    PROFILE_CHANGE_CODE = 1;

                                                }else{
                                                    Log.e(TAG, "=== pos==1 ===" );
                                                    Log.e(TAG, "=== 갤러리로 이동 : === ");

                                                    if ((ContextCompat.checkSelfPermission(getApplicationContext(),
                                                            Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) && (ContextCompat.checkSelfPermission(getApplicationContext(),
                                                            Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)) {
                                                        if ((ActivityCompat.shouldShowRequestPermissionRationale(Manager_ProfileActivity.this,
                                                                Manifest.permission.WRITE_EXTERNAL_STORAGE)) && (ActivityCompat.shouldShowRequestPermissionRationale(Manager_ProfileActivity.this,
                                                                Manifest.permission.READ_EXTERNAL_STORAGE))) {

                                                        } else {
                                                            ActivityCompat.requestPermissions(Manager_ProfileActivity.this,
                                                                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE},
                                                                    REQUEST_PERMISSIONS);
                                                        }
                                                    } else {
                                                        Log.e(TAG, "퍼미션 허용 함");
                                                        showFileChooser();
                                                    }
                                                }
                                            }
                                        });
                                        AlertDialog alertDialog = builder.create();
                                        alertDialog.show();
                                    }
                                }
                            });

                        }else{
                            Log.d(TAG, "=== 저장된 프로필 이미지가 있는 경우, 다이얼로그 띄워서, 기본 이미지와 갤러리이동 을 선택하게끔 ===" +profileImagePathStr);

                            // TODO: 2020-12-10 서버에 있는 이미지 불러오기. 이미지 저장후에 변경할 것
                            profileImage = findViewById(R.id.profileImage);
                            Glide.with(Manager_ProfileActivity.this)
                                    .load(profileImagePathStr)
                                    .circleCrop()
                                    .into(profileImage);

                            profileImage.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Log.d(TAG, "=== profileImage 클릭 : 기본 혹은 갤러리 이동 선택하는 다이얼로그 생성 함 === ");
                                    AlertDialog.Builder builder = new AlertDialog.Builder(Manager_ProfileActivity.this);
                                    builder.setItems(R.array.ProfileImageChoose, new DialogInterface.OnClickListener(){
                                        @Override
                                        public void onClick(DialogInterface dialog, int pos)
                                        {
                                            String[] items = getResources().getStringArray(R.array.ProfileImageChoose);
//                                            Toast.makeText(getApplicationContext(),items[pos],Toast.LENGTH_LONG).show();

                                            if(pos==0){
                                                Log.e(TAG, "=== pos== 0 ===" );
                                                Log.e(TAG, "=== 기본 이미지로 변경 === ");

                                                Glide.with(Manager_ProfileActivity.this)
                                                        .load(getDrawable(R.drawable.ic_baseline_person_24))
                                                        .circleCrop()
                                                        .into(profileImage);

                                                /* 이미지에 변경 있음 1 : 기본 이미지 / 2: 다른 이미지*/
                                                PROFILE_CHANGE_CODE = 1;

                                            }else{
                                                Log.e(TAG, "=== pos==1 ===" );
                                                Log.e(TAG, "=== 갤러리로 이동 : === ");

                                                if ((ContextCompat.checkSelfPermission(getApplicationContext(),
                                                        Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) && (ContextCompat.checkSelfPermission(getApplicationContext(),
                                                        Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)) {
                                                    if ((ActivityCompat.shouldShowRequestPermissionRationale(Manager_ProfileActivity.this,
                                                            Manifest.permission.WRITE_EXTERNAL_STORAGE)) && (ActivityCompat.shouldShowRequestPermissionRationale(Manager_ProfileActivity.this,
                                                            Manifest.permission.READ_EXTERNAL_STORAGE))) {

                                                    } else {
                                                        ActivityCompat.requestPermissions(Manager_ProfileActivity.this,
                                                                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE},
                                                                REQUEST_PERMISSIONS);
                                                    }
                                                } else {
                                                    Log.e(TAG, "퍼미션 허용 함");
                                                    showFileChooser();
                                                }
                                            }
                                        }
                                    });
                                    AlertDialog alertDialog = builder.create();
                                    alertDialog.show();
                                }
                            });
                        }

                        /* 2. 이름 */
                        nameTxt.setText(nameStr);

                        /* 3. 이메일 - 글로벌어플리케이션에서 받아옴 */
                        emailTxt.setText(GlobalApplication.currentManager);

                        /* 4. 휴대전화번호 */
                        // TODO: 2020-12-10 시간 남으면, 부트페이 휴대전화번호 인증

                        phoneNumTxt.setText(phoneNumStr);

                        /* 5. 내 주소 등록 */
                        if(addressStr.equals("null")){
                            Log.e(TAG, "=== addressStr 가 없는 경우 ===" +addressStr);
                            addressTxt.setText("내 주소를 등록해 주세요.");
                            addressChangeTxt.setText("등록하기 +");
                        }else{
                            Log.e(TAG, "=== addressStr 가 있는 경우 . 이미 가공된 데이터여야 함. onResume ===" +addressStr);

                            addressTxt.setText(addressStr);
                            addressChangeTxt.setText("변경하기 >");
                        }


                        /* 아예 싹 안보이게 하고, 필요하면, refresh 메서드에서 보이게 하자 */
                        place1Btn.setVisibility(View.GONE);
                        place2Btn.setVisibility(View.GONE);
                        place3Btn.setVisibility(View.GONE);
                        place4Btn.setVisibility(View.GONE);
                        place5Btn.setVisibility(View.GONE);

                        /* 6. 내 활동 지역 설정 */
                        if(desiredWorkAreaList.equals("null")|| desiredWorkAreaList.equals("[]")){
                            Log.e(TAG, "=== 등록한 활동 지역이 없음 === desiredWorkAreaList " +desiredWorkAreaList);
                            placeNumTxt.setText("( 0/5 )");
                            placePlusTxt.setText("등록하기 +");


                        }else{
                            Log.d(TAG, "=== 등록한 활동 지역이 있음. 이미 가공된 데이터여야 함. desiredWorkAreaList  ==="+desiredWorkAreaList );

                            /* addressStr 리스트 문자열
                            1. [장평면, 기장읍, 사등면, 진안동] [] 제거
                            2. 콤마로 나누어서 arraylist에 담기*/

                            desiredWorkAreaList = desiredWorkAreaList.replace("[",""); //"[진안동]" 이므로 2개 잘라야 함
                            desiredWorkAreaList = desiredWorkAreaList.replace("]",""); //"[진안동]" 이므로 2개 잘라야 함
                            desiredWorkAreaList = desiredWorkAreaList.replace(" ",""); //"[진안동]" 이므로 2개 잘라야 함


                            Log.e(TAG, "=== 등록한 활동 지역이 있음. 이미 가공된 데이터여야 함. desiredWorkAreaList 문자열 자름 === "+desiredWorkAreaList );

                            String[] array = desiredWorkAreaList.split(",");
                            stringBAddressDTOArrayList = new ArrayList<>(Arrays.asList(array));

                            // TODO: 2020-12-11 저장하고 불러올 때, 확인해야 함. 갯수 부분 예외 처리 해야 함.
                            Log.e(TAG, "desiredWorkAreaList 가공 완료 === stringBAddressDTOArrayList === 리프레쉬 " +stringBAddressDTOArrayList.toString() );


                            refreshStringBAddressDTOArrayList();

                            placeNumTxt.setText("( "+stringBAddressDTOArrayList.size()+"/5 )");
                            placePlusTxt.setText("추가하기 +");
                            placeBlankTxt.setVisibility(View.GONE);
                        }

//                        /* 7. 내 전문 분야 설정 */
//                        if(professionalList.equals("null")){
//
//                            Log.e(TAG, "=== professionalList 가 비어 있는 경우 ===" + professionalList);
//                            profNumTxt.setText("( 0/5 )");
//                            profPlusTxt.setText("등록하기 +");
//
//                            mungiBtn.setVisibility(View.GONE);
//                            semyundaeBtn.setVisibility(View.GONE);
//                            gasCleanBtn.setVisibility(View.GONE);
//                            hasuguBtn.setVisibility(View.GONE);
//                            laundryBtn.setVisibility(View.GONE);
//                            windowBtn.setVisibility(View.GONE);
//                            verandaBtn.setVisibility(View.GONE);
//                            garbageBtn.setVisibility(View.GONE);
//                            ironBtn.setVisibility(View.GONE);
//                            fridgeBtn.setVisibility(View.GONE);
//
//                        }else{
//                            Log.e(TAG, "=== professionalList 가 존재하는 경우. 가공된 데이터여야 함. onResume ===" +professionalList);
//                            profNumTxt.setText("( 갯수/5 )");
//                            profPlusTxt.setText("추가하기 +");
//
//                            profBlankTxt.setVisibility(View.GONE);
//                        }





                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e(TAG, "=== response === 에러코드 : " +e);
                }
            }
        };
        ManagerSelectRequest managerSelectRequest = new ManagerSelectRequest(GlobalApplication.currentManager, responseListener);
        RequestQueue queue = Volley.newRequestQueue( Manager_ProfileActivity.this );
        queue.add( managerSelectRequest );



        /* 주소 등록 및 변경 클릭. result 코드 부분에서 주소 정보 객체로 받아옴 */
        addressChangeTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e(TAG,"=== addressChangeTxt ===REQUEST_ADDRESS:"+REQUEST_ADDRESS);
                Intent intent = new Intent(getApplicationContext(), Manager_InputMyPlaceActivity.class);
                startActivityForResult(intent, REQUEST_ADDRESS );
            }
        });

        /* 내 활동 장소 등록 및 변경 result 코드 부분에서 활동지 객체로 받아옴. 사용하는 건 그냥 "진안동" 문자열만 사용.*/
        placePlusTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e(TAG,"=== placePlusTxt ===REQUEST_ACTIVE_ADDRESS:"+REQUEST_ACTIVE_ADDRESS);
                Intent intent = new Intent(getApplicationContext(), Manager_InputActivePlaceActivity.class);
                startActivityForResult(intent, REQUEST_ACTIVE_ADDRESS );
            }
        });


        /* 수정 완료 버튼 클릭 */
        modifyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e(TAG, "=== modifyBtn 클릭 : === Uploading file path :" + filePath);




                /* 저장 클릭한 시간으로 이미지 이름 설정 함. */
                imagename = System.currentTimeMillis();
                Log.e(TAG, "=== modifyBtn 클릭 : === imagename : " + imagename);

                /* 이미지 변경 시, 서버에 이미지 올릴 것 0은 변경 없음. 1은 기본으로 변경. 2는 이미지 추가 함.*/
                if(PROFILE_CHANGE_CODE==0){
                    Log.e(TAG, "=== PROFILE_CHANGE_CODE=0인 경우, 변경 없음. ===" );
                    fileToServer = profileImagePathStr;
                }else if(PROFILE_CHANGE_CODE==1){
                    Log.e(TAG, "=== PROFILE_CHANGE_CODE=1인 경우, 변경 있음. 기본 이미지로  ===" );
                    fileToServer=null;
                }else if(PROFILE_CHANGE_CODE==2){
                    Log.e(TAG, "=== PROFILE_CHANGE_CODE=2인 경우, 변경 있음. 새로운 이미지 서버에 업로드 해야 함. filePath ===" +filePath);
                    uploadBitmap(bitmap);
                    fileToServer="http://52.79.179.66/uploads/" + imagename + ".png";
                    //절대 경로 복사 : /var/www/html/uploads/16075223001607522295030.png
                    //이미지 위치 : http://52.79.179.66/uploads/16075223001607522295030.png
                }
                Log.d(TAG, "=== filePath 폰 경로 ===" +filePath);
                Log.d(TAG, "=== fileToServer 서버 경로 ===" +fileToServer);





                /* 주소 변경 시, addressStr 서버에서 받아온거 그대로 쓸 지, 아니면, 현재 텍스트 뷰에 있는 스트링 가져올 지 */
                if(ADDRESS_CHANGE_CODE==0){
                    Log.e(TAG, "=== addressStr ADDRESS_CHANGE_CODE==0 인 경우, 변경 없음.===addressStr " +addressStr );
                }else if(ADDRESS_CHANGE_CODE==1){
                    Log.e(TAG, "===  addressStrADDRESS_CHANGE_CODE==1 인 경우, 변경 함.==addressStr"+addressStr );
                    addressStr=addressTxt.getText().toString();
                    Log.d(TAG, "=== addressStr ===" +addressStr );
                }



                postManagerProfile(GlobalApplication.currentManager, addressStr, stringBAddressDTOArrayList.toString(), fileToServer);




            }
        });
    }


    /* 레트로핏으로 서버에 프로필 정보 보내는 코드 */
    private void postManagerProfile(String idStr, String addressStr, String desiredWorkAreaList, String profileImagePathStr){

        Log.e(TAG, "=== postManagerProfile 시작 ===" );

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ManagerProfileInterface.JSONURL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();

        ManagerProfileInterface api =retrofit.create(ManagerProfileInterface.class);
        /* 인터페이스에서 정의한 메서드 / 인자로 보낼 값 넣는 곳 */
        Call<String> call = api.putManagerProfile(idStr, addressStr, desiredWorkAreaList, profileImagePathStr);

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                Log.e("Responsestring", response.body().toString());
                //Toast.makeText()
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        Log.e("onSuccess", response.body().toString());

                        /* 저장했으니, 변화 없는 상태로 */
                        ADDRESS_CHANGE_CODE = 0;
                        PROFILE_CHANGE_CODE = 0;
                        DESIRED_AREA_LIST_CHANGE_CODE =0;

                        String jsonresponse = response.body().toString();
                        parseProfileEditData(jsonresponse);

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
//        Map<String, String> mapdata = new HashMap<>();
//        mapdata.put("idStr", GlobalApplication.currentManager);
//        mapdata.put("addressStr", addressStr);
//        mapdata.put("desiredWorkAreaList", stringBAddressDTOArrayList.toString());
//        mapdata.put("profileImagePathStr", fileToServer);
//
//        Call<String> call = api.putManagerProfile(mapdata);
//
//        call.enqueue(new Callback<String>() {
////            @Override
////            public void onResponse(Call<String> call, Response<String> response) {
////
////            }
//
//            @Override
//            public void onResponse(Call<String> call, retrofit2.Response<String> response) {
//                Log.e("Responsestring", response.body());
//                //Toast.makeText()
//                if (response.isSuccessful()) {
//                    if (response.body() != null) {
//                        Log.e("onSuccess", response.body());
//
//                        /* 저장했으니, 변화 없는 상태로 */
//                        ADDRESS_CHANGE_CODE = 0;
//                        PROFILE_CHANGE_CODE = 0;
//                        DESIRED_AREA_LIST_CHANGE_CODE =0;
//
//                        String jsonresponse = response.body();
//
//                        parseProfileEditData(jsonresponse);
////                        parseLoginData(jsonresponse);
//
//                    } else {
//                        Log.e("onEmptyResponse", "Returned empty response");//Toast.makeText(getContext(),"Nothing returned",Toast.LENGTH_LONG).show();
//                    }
//                }
//            }
//
//            @Override
//            public void onFailure(Call<String> call, Throwable t) {
//
//            }
//        });

    }

    public void parseProfileEditData(String response) {

        try {
            JSONObject jsonObject = new JSONObject(response);

            Log.e(TAG,"parseProfileEditData response"+ response);

            if (jsonObject.getString("status").equals("true")) {

//                JSONArray dataArray = jsonObject.getJSONArray("data");
//                for (int i = 0; i < dataArray.length(); i++) {
//
//                    JSONObject dataobj = dataArray.getJSONObject(i);
//
//                    Log.e(TAG,"parseProfileEditData dataobj"+ dataobj);
//
////                    firstName = dataobj.getString("name");
////                    hobby = dataobj.getString("hobby");
//                }

                Toast.makeText(getBaseContext(), "수정이 완료되었습니다.", Toast.LENGTH_SHORT).show();

//                Intent intent = new Intent(Manager_ProfileActivity.this,NameActivity.class);
//                startActivity(intent);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e(TAG,"parseProfileEditData e"+ e);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e(TAG, "=== onResume ===" );

    }

    /* 갤러리에서 받아온 이미지 보여주기 */
    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_PICK);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    /* 1. 갤러리에서 이미지 받아오는 경우,
    *  2. 내 주소 받아오는 경우,
    *  3. 활동 지역 "진안동" 받아오는 경우, */
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.e(TAG, "=== requestCode ===" +requestCode);
        Log.e(TAG, "=== resultCode ===" +resultCode);
        Log.e(TAG, "=== data ===" +data);

        /* 1. 갤러리에서 이미지 받아오는 경우 */
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri picUri = data.getData();
            Log.e(TAG, "=== filePath ===" +filePath);

            /* 이미지에 변경 있음 1 : 기본 이미지 / 2: 다른 이미지 */
            PROFILE_CHANGE_CODE = 2;

            filePath = getPath(picUri);
            Log.e(TAG, "=== picUri ===" +picUri);
            if (filePath != null) {
                try {

                    Log.e(TAG, "=== File Selected ===" );
                    Log.e("filePath", String.valueOf(filePath));
                    bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), picUri);
                    Log.e(TAG, "=== setImageBitmap ===" +bitmap);
//                    profileImage.setImageBitmap(bitmap);

                    /* 이미지 적용하기 */
                    Glide.with(this)
                            .load(bitmap)
                            .circleCrop()
                            .into(profileImage);

                } catch (IOException e) {
                    e.printStackTrace();
                    Log.e(TAG, "=== 갤러리에서 이미지 못 받아옴 ===" +e);
                }
            }else
            {   Log.e(TAG, "=== no image selected ===" +data);
//                Toast.makeText(
//                        Manager_ProfileActivity.this,"no image selected",
//                        Toast.LENGTH_LONG).show();
            }
        }
        /* 2. 내 주소 받아오는 경우 */
        else if(requestCode == REQUEST_ADDRESS && resultCode == RESULT_OK && data != null){
            Log.e(TAG, "=== REQUEST_ADDRESS 주소 받아오는 경우 ===" );

            /* 변경됨 알림 */
            ADDRESS_CHANGE_CODE=1;

            addressDTOforServer = (AddressDTO) data.getSerializableExtra("addressDTO2");
            Log.e(TAG, "=== addressDTOforServer 들어왔는지 확인 ===" +addressDTOforServer.getAddrStr() );

            addressTxt.setText("["+addressDTOforServer.getZonecodeStr()+"] "+ addressDTOforServer.getAddrStr()+", "+addressDTOforServer.getDetailStr());

            addressChangeTxt.setText("변경하기 >");

        }
        /* 3. 활동 지역 "진안동" 받아오는 경우, arraylist로 저장하고, 불러올 것. */
        else if(requestCode == REQUEST_ACTIVE_ADDRESS && resultCode == RESULT_OK && data != null){
            Log.e(TAG, "=== REQUEST_ACTIVE_ADDRESS 지역 받아오는 경우 ===" );

            /* 변경됨 알림 */
            DESIRED_AREA_LIST_CHANGE_CODE=1;

            baddressDTOforServer = (BaddressDTO) data.getSerializableExtra("baddressDTO");
            Log.e(TAG, "=== baddressDTOforServer 들어왔는지 확인 ===" +baddressDTOforServer.getEupmyundongName() );

            place1Btn.setText(baddressDTOforServer.getEupmyundongName());

            /* scrollview2 보여야 함.
            * 기존 리스트에 추가되어야 함
            * 내 활동 지역 숫자 늘려야 함-인스턴스 변수
            *  */

//            scrollView2.setVisibility(View.VISIBLE);
            placeBlankTxt.setVisibility(View.GONE);

            /* 어레이 리스트 사이즈 만큼 데이터 넣기 */
            stringBAddressDTOArrayList.add(baddressDTOforServer.getEupmyundongName());
            Log.d(TAG, "=== 어레이 ===" +stringBAddressDTOArrayList.toString());

            refreshStringBAddressDTOArrayList();


        }

    }

    /* 이미지 경로 가져오는 코드 */
    public String getPath(Uri uri) {

        /* Cursor sdk 버전30에서는 사용 못 함. 그 아래 코드로 변경함.*/
//        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
//        cursor.moveToFirst();
//        String document_id = cursor.getString(0);
//        document_id = document_id.substring(document_id.lastIndexOf(":") + 1);
//        cursor.close();
//
//        cursor = getContentResolver().query(
//                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
//                null, MediaStore.Images.Media._ID + " = ? ", new String[]{document_id}, null);
//        cursor.moveToFirst();
//        String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
//        cursor.close();
//
//        return path;
        String[] proj= {MediaStore.Images.Media.DATA};
        CursorLoader loader= new CursorLoader(this, uri, proj, null, null, null);
        Cursor cursor= loader.loadInBackground();
        int column_index= cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String result= cursor.getString(column_index);
        cursor.close();
        return  result;
    }

    // TODO: 2020-12-10 아직, 변경된 이미지를 언제 저장할 지 못 정함. 이미지를 서버에 저장하고, 해당 경로를 db에도 저장해야 하는데..?
    /* 변경된 이미지 서버에 저장하는 코드 */
    private void uploadBitmap(final Bitmap bitmap) {
        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, ROOT_URL,
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {
                        try {
                            Log.e(TAG, "=== uploadBitmap onResponse ===" +response);
                            JSONObject obj = new JSONObject(new String(response.data));
//                            Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.e(TAG, "=== uploadBitmap e ===" +e);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
//                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                        Log.e("GotError",""+error.getMessage());
                    }
                }) {


            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();

                params.put("image", new DataPart(imagename + ".png", getFileDataFromDrawable(bitmap)));

                return params;
            }
        };

        //adding the request to volley
        Volley.newRequestQueue(this).add(volleyMultipartRequest);
    }

    /* drawable 로 가져오기  */
    public byte[] getFileDataFromDrawable(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 80, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    /* 어레이 전체 사이즈 만큼, 버튼 활성화 및 버튼에 데이터 넣기 */
    public void refreshStringBAddressDTOArrayList(){
        if(stringBAddressDTOArrayList.size()==5){

            place1Btn.setVisibility(View.VISIBLE);
            place1Btn.setText(stringBAddressDTOArrayList.get(0));

            place2Btn.setVisibility(View.VISIBLE);
            place2Btn.setText(stringBAddressDTOArrayList.get(1));

            place3Btn.setVisibility(View.VISIBLE);
            place3Btn.setText(stringBAddressDTOArrayList.get(2));

            place4Btn.setVisibility(View.VISIBLE);
            place4Btn.setText(stringBAddressDTOArrayList.get(3));

            place5Btn.setVisibility(View.VISIBLE);
            place5Btn.setText(stringBAddressDTOArrayList.get(4));

        }else if(stringBAddressDTOArrayList.size()==4){

            place1Btn.setVisibility(View.VISIBLE);
            place1Btn.setText(stringBAddressDTOArrayList.get(0));

            place2Btn.setVisibility(View.VISIBLE);
            place2Btn.setText(stringBAddressDTOArrayList.get(1));

            place3Btn.setVisibility(View.VISIBLE);
            place3Btn.setText(stringBAddressDTOArrayList.get(2));

            place4Btn.setVisibility(View.VISIBLE);
            place4Btn.setText(stringBAddressDTOArrayList.get(3));

        }else if(stringBAddressDTOArrayList.size()==3){

            place1Btn.setVisibility(View.VISIBLE);
            place1Btn.setText(stringBAddressDTOArrayList.get(0));

            place2Btn.setVisibility(View.VISIBLE);
            place2Btn.setText(stringBAddressDTOArrayList.get(1));

            place3Btn.setVisibility(View.VISIBLE);
            place3Btn.setText(stringBAddressDTOArrayList.get(2));

        }else if(stringBAddressDTOArrayList.size()==2){

            place1Btn.setVisibility(View.VISIBLE);
            place1Btn.setText(stringBAddressDTOArrayList.get(0));

            place2Btn.setVisibility(View.VISIBLE);
            place2Btn.setText(stringBAddressDTOArrayList.get(1));

        }else if(stringBAddressDTOArrayList.size()==1){

            place1Btn.setVisibility(View.VISIBLE);
            place1Btn.setText(stringBAddressDTOArrayList.get(0));

        }else if(stringBAddressDTOArrayList.size()==0){
            Log.e(TAG, "=== addressStr 가 없는 경우 ===" +addressStr);
            placeBlankTxt.setVisibility(View.VISIBLE);
            scrollView1.setVisibility(View.GONE);

            placeBlankTxt.setText("내 주소를 등록해 주세요.");

        }




        placeNumTxt.setText("( "+stringBAddressDTOArrayList.size()+"/5 )");


        //각버튼 아이디 매칭
        Button.OnClickListener onClickListener = new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    //place1Btn 버튼 클릭 이벤트
                    case R.id.place1Btn:
                        place1Btn.setVisibility(View.GONE);
                        stringBAddressDTOArrayList.remove(0);
                        refreshStringBAddressDTOArrayList();

                        /* 변경됨 알림 */
                        DESIRED_AREA_LIST_CHANGE_CODE=1;

                        placeNumTxt.setText("( "+stringBAddressDTOArrayList.size()+"/5 )");

                        break;
                    //place2Btn 버튼 클릭 이벤트
                    case R.id.place2Btn:
                        place2Btn.setVisibility(View.GONE);
                        stringBAddressDTOArrayList.remove(1);
                        refreshStringBAddressDTOArrayList();

                        /* 변경됨 알림 */
                        DESIRED_AREA_LIST_CHANGE_CODE=1;

                        placeNumTxt.setText("( "+stringBAddressDTOArrayList.size()+"/5 )");

                        break;

                    //place3Btn 버튼 클릭 이벤트
                    case R.id.place3Btn:
                        place3Btn.setVisibility(View.GONE);
                        stringBAddressDTOArrayList.remove(2);
                        refreshStringBAddressDTOArrayList();

                        /* 변경됨 알림 */
                        DESIRED_AREA_LIST_CHANGE_CODE=1;

                        placeNumTxt.setText("( "+stringBAddressDTOArrayList.size()+"/5 )");

                        break;

                    //place4Btn 버튼 클릭 이벤트
                    case R.id.place4Btn:
                        place4Btn.setVisibility(View.GONE);
                        stringBAddressDTOArrayList.remove(3);
                        refreshStringBAddressDTOArrayList();

                        /* 변경됨 알림 */
                        DESIRED_AREA_LIST_CHANGE_CODE=1;

                        placeNumTxt.setText("( "+stringBAddressDTOArrayList.size()+"/5 )");

                        break;

                    //place5Btn 버튼 클릭 이벤트
                    case R.id.place5Btn:
                        place5Btn.setVisibility(View.GONE);
                        stringBAddressDTOArrayList.remove(4);
                        refreshStringBAddressDTOArrayList();

                        /* 변경됨 알림 */
                        DESIRED_AREA_LIST_CHANGE_CODE=1;

                        placeNumTxt.setText("( "+stringBAddressDTOArrayList.size()+"/5 )");

                        break;
                }
            }
        };

        place1Btn.setOnClickListener(onClickListener);
        place2Btn.setOnClickListener(onClickListener);
        place3Btn.setOnClickListener(onClickListener);
        place4Btn.setOnClickListener(onClickListener);
        place5Btn.setOnClickListener(onClickListener);

        Log.e(TAG, "=== stringBAddressDTOArrayList ===" +stringBAddressDTOArrayList.toString() );

    }



}