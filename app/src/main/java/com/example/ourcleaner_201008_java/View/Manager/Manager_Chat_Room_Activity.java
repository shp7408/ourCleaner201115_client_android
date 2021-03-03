package com.example.ourcleaner_201008_java.View.Manager;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.loader.content.CursorLoader;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.example.ourcleaner_201008_java.Adapter.Manager_Chat_Adapter;
import com.example.ourcleaner_201008_java.ClientThread;
import com.example.ourcleaner_201008_java.DTO.ChatDTO;
import com.example.ourcleaner_201008_java.Interface.ChatInsertInterface;
import com.example.ourcleaner_201008_java.Interface.ChatSelectInterface;
import com.example.ourcleaner_201008_java.Interface.TokenSelectInterface;
import com.example.ourcleaner_201008_java.R;
import com.example.ourcleaner_201008_java.Server.VolleyMultipartRequest;
import com.example.ourcleaner_201008_java.Service.FcmClient;
import com.example.ourcleaner_201008_java.SharedP.PreferenceManager_LastMessage;

import org.apache.commons.io.output.ByteArrayOutputStream;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

import static android.content.Intent.FLAG_ACTIVITY_NO_HISTORY;
import static com.example.ourcleaner_201008_java.Adapter.Code.ViewType.Manager_Received_Message;
import static com.example.ourcleaner_201008_java.Adapter.Code.ViewType.Manager_Send_Message;

public class Manager_Chat_Room_Activity extends AppCompatActivity {

private static final String TAG = "매니저용채팅방내부";

    /* 이미지 업로드 위한 php url */
    private static final String ROOT_URL = "http://52.79.179.66/profileUploads.php";
    /* 서버에 이미지 저장할 때, 서버의 매니저 정보에서 프로필이미지 DB에 경로저장하기 위해서 인스턴스 변수로 */
    long imagename;
    private static final int REQUEST_PERMISSIONS = 100;
    private static final int PICK_IMAGE_REQUEST = 1 ;
    private String filePath, fileToServer; //현재 기기에서의 경로, 서버에 저장될 때의 경로
    private Bitmap bitmap;


    ListView messageListView;
    EditText messageBox;
    TextView send, roomTxt, sendImg;

    /* 소켓 준비하는 부분 */
    Socket client;
    //String ip = "192.168.35.172"; //192.168.0.3
    //String ip = "192.168.0.3"; //192.168.187.1
    //String ip = "192.168.187.1"; //172.20.10.14
    String ip ="192.168.0.6";

    int port = 8080;

    Thread thread;
    ClientThread clientThread;
    Handler handler;




    String whomSent1, whomSent2, whoSend;

    /* 메세지 보여주는 부분 */
    //private MessageAdapter adapter;
    JSONObject jsonObj;


    private Manager_Chat_Adapter adapter;
    private RecyclerView recyclerView; //clientChatRecyclerview

    ArrayList<ChatDTO> chatDTOArrayList = new ArrayList<>();

    String newRoomId; //방 번호 제일 최근거 가져와서 +1 하는 작업..ㅎㅎ -> 소켓으로 메세지 보낼 때, 넣을 것임.. 시연때 주의**



    //토큰값받아오는 변수. 받는 사람의 이메일 가져옴
    String clientToken;

    List<JSONObject> messageList2 = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager__chat__room_);
        Log.d(TAG, "=== onCreate ===" );

        Intent intent = getIntent();

        whomSent1 = intent.getStringExtra("whomSent1"); //메세지 받는 고객 이메일
        Log.d(TAG, "whomSent1 : "+ whomSent1);

        whomSent2 = intent.getStringExtra("whomSent2"); // 메세지 받는 고객 이름
        Log.d(TAG, "whomSent2 : "+ whomSent2);

        whoSend = intent.getStringExtra("whoSend");
        Log.d(TAG, "whoSend : "+ whoSend);


        if(whomSent1.isEmpty() || whomSent2.isEmpty() || whoSend.isEmpty()){
            Log.d(TAG, "=== 알람클릭해서 들어온 경우 ===" );
        }else{
            Log.d(TAG, "=== 값있음 ===" );
        }


        /*
         * 1. 토큰 받아오기 -> fcm 알람을 위함 -> 백그라운드에서만 메세지가 뜨고, 채팅방 엑티비티로 들어와야 하는데.. 음..
         * 생각해볼것.ㅠㅠㅠㅠ 아마도.. 시연할 때, 매니저 혹은 고객쪽 만 메세지 가도록 해야 할 듯..
         * 아니면, fcm에서 데이터를 넘길 수 있는지 메서드 확인해볼 것.
         *
         * 2. 채팅 메세지 존재하는지 확인
         * 있으면,
         *   1) 채팅 내용 가져오기
         *   2) 채팅내용에서 채팅방번호 따로 변수에 저장하기
         *   3) 그 다음 메세지 보낼때, 채팅방번호 넣기
         *   4) arraylist에서 가장 마지막 채팅내용을 마지막메세지 쉐어드에 저장하기
         *
         * 없으면,
         *   1) 가장 마지막 채팅방id 가져와서 1 더한값을 변수에 담아두기 -> 소켓으로 채팅내용 보낼때 위함
         *   2) 그냥 리사이클러 뷰 올려두기...(메세지 아무것도 없을 때, add 되는지 꼭 확인할 것!!)
         *
         * 3. 채팅메세지 전송
         *   1) 소켓으로 메세지 전송
         *   2) listen 상태에서 모든 메세지를 쉐어드에 저장할 것.
         *   3) 이 때, 메세지객체(소켓)에 있던 방id로 쉐어드에 꼭 저장할것!!
         *
         * */





        /* 3) 허가 코드 작성
        연결에 앞서, 안드로이드 3.0 부터는 밑의 코드를 추가해 놓지 않으면 에러가 납니다. */
        int SDK_INT = android.os.Build.VERSION.SDK_INT;
        if (SDK_INT > 8) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }


        /* 널포인트익셉션이 자주떠서.. 핸들러로 소켓 준비 */
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try  {
                    Log.e(TAG, "=== thread ===" );

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
                Log.e(TAG, "thread : run instantiate");

                /* 소켓 준비 */
                instantiate();

            }

        }, 0);




//        messageListView = findViewById(R.id.messageList); //managerChatRecyclerview
        recyclerView = findViewById(R.id.managerChatRecyclerview);



        messageBox = findViewById(R.id.messageBox);
        send = findViewById(R.id.send);

        roomTxt = findViewById(R.id.roomTxt);

        String clientIdStr = whomSent1.substring(0, 7);

        roomTxt.setText(whomSent2+" 고객님 ("+clientIdStr+")과의 대화");

        sendImg = findViewById(R.id.sendImg);

        messageBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Log.d(TAG, "=== messageBox onTextChanged ===" );
                if(messageBox.getText().toString().length()==0){
                    //sendImg.setVisibility(View.VISIBLE);
                    //send.setVisibility(View.GONE);
                    send.setEnabled(false);
                }else{
                    //sendImg.setVisibility(View.GONE);
                    send.setVisibility(View.VISIBLE);
                    send.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        /* 대화 내용 받아오는 코드 */
        postChatSelect(whoSend, whomSent1); //순서 바뀌어도 상관없을듯??

        /* 해당 일반고객의 토큰 값 받아오기  managerToken 미리 받아옴.*/
        postSelectToken(whomSent1, 1);
        Log.d(TAG, "=== whomSent1 ===" +whomSent1);





        handler = new Handler(){
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                Bundle bundle = msg.getData();

                /* 받을 때, String으로만 되서.. 여기서 다시 JSONObject 으로 변환해주어야 함. */
                String msgStr = bundle.getString("msg");
                Log.d(TAG, "=== handleMessage msgStr === " + msgStr );

                /* 소켓 연결 끊을 때 exit 포함여부로.. 일단 지금은 필요없을듯.. 고민해볼 것.. */
                if(msgStr.contains("***************************************************")){
                    Log.e(TAG, "=== 메세지에 *************************************************** 포함된 경우 ===" );}

                else{

                    Log.e(TAG, "=== 메세지에 exit XXXXXXXXXXXX ===" );
                    // String 형태의 msgStr을 JSONObject 으로 변경
                    JSONParser parser = new JSONParser();
                    Object obj = null;
                    try {
                        obj = parser.parse( msgStr );
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    jsonObj = (JSONObject) obj;
                    Log.d(TAG, "=== handleMessage msgStr을 변경 완료 -> jsonObj === " + jsonObj );

                    if(jsonObj == null){
                        Log.e(TAG, "=== jsonObj.isEmpty() XXXXXXXXXXXX ===" );
                    }else{
                        Log.e(TAG, "=== jsonObj.isEmpty()  ===" );
                        if(jsonObj.get("whoSend").equals(whoSend) && jsonObj.get("whomSent").equals(whomSent1)
                                || jsonObj.get("whoSend").equals(whomSent1) && jsonObj.get("whomSent").equals(whoSend)){
                            /* 이 부분에서 화면 변경함 */
//                            adapter.addItem(jsonObj);
//                            Log.d(TAG, "=== handleMessage adapter.getCount() ==="+ adapter.getCount() );


                            if(jsonObj.get("whoSend").equals(whoSend) && jsonObj.get("whomSent").equals(whomSent1)){
                                /* 보낸 메세지 */

                                ChatDTO chatDTO = new ChatDTO();
                                chatDTO.setViewType(Manager_Send_Message);
                                chatDTO.setMsgStr((String) jsonObj.get("msgStr"));
                                chatDTO.setGetDate((String) jsonObj.get("getDate"));
                                chatDTO.setGetTime((String) jsonObj.get("getTime"));
                                chatDTO.setWhomSent((String) jsonObj.get("whomSent"));
                                chatDTO.setWhoSend((String) jsonObj.get("whoSend"));

                                chatDTOArrayList.add(chatDTO);

                                adapter.notifyItemInserted(chatDTOArrayList.size()-1);

                                if(chatDTOArrayList.size()<5){
                                    //채팅 내용 개수가 5개 이하면 스크롤 내릴 필요없음
                                    messageBox.requestFocus();

                                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                                    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);

                                }else{
                                    /* 채팅 내용 맨 하단으로 위치시키기 */
                                    recyclerView.scrollToPosition(chatDTOArrayList.size()-1);


                                    Handler handler = new Handler();
                                    handler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {

                                            //키패드 올라오고난 뒤에 채팅내용 맨아래로??
                                            /* 키패드 올라오게 하기 위함 */
                                            messageBox.requestFocus();
//                                            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//                                            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
//
//                                            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);


                                            recyclerView.scrollToPosition(recyclerView.getAdapter().getItemCount() - 1);
                                            recyclerView.scrollToPosition(chatDTOArrayList.size()-1);

                                        }
                                    }, 500); //딜레이 타임 조절
                                }



                                /* 쉐어드에 마지막 메세지 저장하기 */
                                ArrayList<String> arrayList = new ArrayList<>();

                                arrayList.add((String) jsonObj.get("roomId"));
                                arrayList.add((String) jsonObj.get("getTime"));
                                arrayList.add((String) jsonObj.get("msgStr"));
                                arrayList.add(whomSent1);
                                arrayList.add(whomSent2);

                                PreferenceManager_LastMessage.setStringArrayPref(getApplicationContext(), whomSent1, arrayList);






                            }else if(jsonObj.get("whoSend").equals(whomSent1) && jsonObj.get("whomSent").equals(whoSend)){

                                /* 받은 메세지 */
                                ChatDTO chatDTO = new ChatDTO();
                                chatDTO.setViewType(Manager_Received_Message);
                                chatDTO.setMsgStr((String) jsonObj.get("msgStr"));
                                chatDTO.setGetDate((String) jsonObj.get("getDate"));
                                chatDTO.setGetTime((String) jsonObj.get("getTime"));
                                chatDTO.setWhomSent((String) jsonObj.get("whomSent"));
                                //chatDTO.setWhoSend((String) jsonObj.get("whoSend"));
                                chatDTO.setWhoSend(whomSent2 + " 고객님"); //화면 보여지기 위함 이름으로 변경

                                chatDTOArrayList.add(chatDTO);

                                adapter.notifyItemInserted(chatDTOArrayList.size()-1);

                                if(chatDTOArrayList.size()<5){
                                    //채팅 내용 개수가 5개 이하면 스크롤 내릴 필요없음
                                    messageBox.requestFocus();

                                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                                    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);

                                }else{
                                    /* 채팅 내용 맨 하단으로 위치시키기 */
                                    recyclerView.scrollToPosition(chatDTOArrayList.size()-1);


                                    Handler handler = new Handler();
                                    handler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {

                                            //키패드 올라오고난 뒤에 채팅내용 맨아래로??
                                            /* 키패드 올라오게 하기 위함 */
                                            messageBox.requestFocus();
//                                            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//                                            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
//
//                                            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);


                                            recyclerView.scrollToPosition(recyclerView.getAdapter().getItemCount() - 1);
                                            recyclerView.scrollToPosition(chatDTOArrayList.size()-1);

                                        }
                                    }, 500); //딜레이 타임 조절



                                }






                                /* 쉐어드에 마지막 메세지 저장하기 */
                                ArrayList<String> arrayList = new ArrayList<>();

                                arrayList.add((String) jsonObj.get("roomId"));
                                arrayList.add((String) jsonObj.get("getTime"));
                                arrayList.add((String) jsonObj.get("msgStr"));
                                arrayList.add(whomSent1);
                                arrayList.add(whomSent2);

                                PreferenceManager_LastMessage.setStringArrayPref(getApplicationContext(), whomSent1, arrayList);


                            }



                        }else{

                        }
                    }

                }

            }
        };




        /* 메세지 보내는 코드 -> 실시간으로도 보내고, 디비에도 저장하고 */
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "=== send 클릭 ===" );



                String message = messageBox.getText().toString();

                if(!message.isEmpty()){
                    /* 현재 시간 가져오기 */
                    long now = System.currentTimeMillis();

                    Date mDate = new Date(now);

                    SimpleDateFormat simpleDate = new SimpleDateFormat(" MM월 dd일");
                    String getDate = simpleDate.format(mDate);

                    SimpleDateFormat simpleTime = new SimpleDateFormat(" a KK시 mm분");
                    String getTime = simpleTime.format(mDate);

                    /* 채팅방 목록 정렬 위함 */
                    SimpleDateFormat sdfNow = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    // nowDate 변수에 값을 저장한다.
                    String formatDate = sdfNow.format(mDate);


                    /* 그냥 객체로 하면.. 여러개 메세지가 가서, 하나의 메세지로 보내기 위해서 제이슨오브젝트 사용함. */
                    JSONObject data = new JSONObject();

                    data.put("msgStr", messageBox.getText().toString());
                    data.put("getDate", getDate);
                    data.put("getTime", getTime);
                    data.put("whomSent", whomSent1);
                    data.put("whoSend", whoSend);
                    data.put("byServer", false);
                    data.put("roomId", newRoomId);
                    data.put("regiDate", formatDate);

                    postChatInsert(messageBox.getText().toString(), getDate, getTime, whomSent1, whoSend);



                    clientThread.send(data);
                    messageBox.setText("");


                    /* fcmpush 보내는 코드
                     * 1. 클래스 만들어야 함
                     * 2. fcmPushMessage.pushFCMNotification 의 첫 번째 인자
                     * : 메세지 내용 / 두 번째 인자 : 매니저인지, 고객인지 / 받는 사람 이메일 - 이걸로 찾음
                     * 3. 받는 사람 기준으로 클래스 명 생성함
                     * */

                    FcmClient fcmPushTest = new FcmClient();

                    /* 1. 푸쉬 알람 스레드 */
                    Thread thread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try  {
                                Log.e(TAG, "=== FcmClient ==="+fcmPushTest.toString() );

                                fcmPushTest.pushFCMNotification("메세지가 도착했습니다."
                                        , 1, clientToken);

                            } catch (Exception e) {
                                e.printStackTrace();
                                Log.e(TAG, "fcmPushTest 에러 코드 : "+ e);
                            }
                        }
                    });

                    thread.start();
                    Handler mHandler = new Handler(Looper.getMainLooper());
                    mHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Log.e(TAG, "fcmPushTest : run");

                        }

                    }, 0);




                }


            }
        });



        sendImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "=== sendImg 클릭 갤러리 열기 ===" );


                /* 키보드 내리기 */
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(messageBox.getWindowToken(), 0);


                if ((ContextCompat.checkSelfPermission(getApplicationContext(),
                        Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) && (ContextCompat.checkSelfPermission(getApplicationContext(),
                        Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)) {
                    if ((ActivityCompat.shouldShowRequestPermissionRationale(Manager_Chat_Room_Activity.this,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE)) && (ActivityCompat.shouldShowRequestPermissionRationale(Manager_Chat_Room_Activity.this,
                            Manifest.permission.READ_EXTERNAL_STORAGE))) {

                    } else {
                        ActivityCompat.requestPermissions(Manager_Chat_Room_Activity.this,
                                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE},
                                REQUEST_PERMISSIONS);
                    }
                } else {
                    Log.e(TAG, "퍼미션 허용 함");
                    showFileChooser();
                }


            }
        });



    }

    /* 레트로핏으로 서버에 있는 채팅 내용 가져오는 코드 */
    private void postChatSelect(String managerEmail, String clientEmail){

        Log.e(TAG, "=== postChatInsert 시작 ===" );

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ChatSelectInterface.JSONURL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();

        ChatSelectInterface api =retrofit.create(ChatSelectInterface.class);
        /* 인터페이스에서 정의한 메서드 / 인자로 보낼 값 넣는 곳 */
        Call<String> call = api.chatSelect(managerEmail, clientEmail);

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                Log.e("postChatSelect ", response.body());

                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        Log.e(TAG, "postChatSelect onSuccess" + response.body());


                        //writeListView(response.body()); 리스트뷰를 리사이클러뷰로 변경 중
                        writeRecyclerView(response.body());

                    } else {
                        Log.e(TAG, "Returned empty response");
                    }
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

                Log.e(TAG, "=== onFailure call ===" +call+" t"+t);

            }
        });
    }




    /* 상대방에게 알람 보내기 위해서 토큰 받아옴 */
    private void postSelectToken(String email, int whichClientManager){

        Log.e(TAG, "=== postManagerProfile 시작 ===" );

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(TokenSelectInterface.JSONURL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();

        TokenSelectInterface api =retrofit.create(TokenSelectInterface.class);
        /* 인터페이스에서 정의한 메서드 / 인자로 보낼 값 넣는 곳 */
        Call<String> call = api.selectToken(email, whichClientManager);

        Log.e(TAG, "=== email ===" +email);
        Log.e(TAG, "=== whichClientManager 1이면 클라, 2면 매니저===" +whichClientManager);

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                Log.e("Responsestring", response.body());
                //Toast.makeText()
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        Log.e(TAG, "onSuccess" + response.body());

                        String jsonresponse = response.body();
                        Log.e(TAG, "=== jsonresponse ===" +jsonresponse );

                        try {
                            org.json.JSONObject obj = new org.json.JSONObject(jsonresponse);
                            Log.e(TAG, "=== response ===" +response);

                            if(obj.optString("status").equals("true")){

                                JSONArray dataArray  = obj.getJSONArray("data");

                                // TODO: 2020-12-15 여기서 해당 텍스트 뷰에 데이터 넣음

                                for (int i = 0; i < dataArray.length(); i++) {

                                    org.json.JSONObject dataobj = dataArray.getJSONObject(i);

                                    clientToken = dataobj.getString("Token");

                                    Log.e(TAG, "=== postSelectToken === clientToken" +clientToken);



                                }
                            }else {
                                Toast.makeText(Manager_Chat_Room_Activity.this, obj.optString("message")+"", Toast.LENGTH_SHORT).show();
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

                Log.e(TAG, "=== onFailure call ===" +call+" t"+t);

            }
        });


    }



    /* 서버에서 받아온 채팅 내용 리사이클러뷰에 뿌리는 코드 */
    private void writeRecyclerView(String response){
        try {
            //getting the whole json object from the response
            org.json.JSONObject obj = new org.json.JSONObject(response);
            Log.e(TAG, "=== writeListView response ===" +response);

            if(obj.optString("status").equals("true")){

                JSONArray dataArray  = obj.getJSONArray("data");

                for (int i = 0; i < dataArray.length(); i++) {

                    org.json.JSONObject dataobj = dataArray.getJSONObject(i);

                    /* 소켓으로 메세지 보낼때 방번호 보내기 위함 */
                    newRoomId = (String) dataobj.get("chatRoomId");
                    Log.e(TAG, "=== newRoomId ===" +newRoomId);

                    /* 쉐어드에 마지막 메세지 저장하기 */
                    ArrayList<String> arrayList = new ArrayList<>();

                    arrayList.add((String) dataobj.get("chatRoomId")); //방번호 필요하면 넣고..ㅎㅎ
                    arrayList.add((String) dataobj.get("getTime")); //메세지의 시간
                    arrayList.add((String) dataobj.get("msgStr")); //메세지 내용
                    arrayList.add(whomSent1); //기기 사용 기준, 내가 아닌 상대방 저장하는 것임..
                    arrayList.add(whomSent2);

                    PreferenceManager_LastMessage.setStringArrayPref(getApplicationContext(),whomSent1, arrayList);


                    //리사이클러뷰에 넣기 위함
                    ChatDTO chatDTO = new ChatDTO();

                    //chatDTO.setWhoSend((String) dataobj.get("whoSend"));
                    chatDTO.setWhoSend((String) dataobj.get("whoSend"));
                    chatDTO.setWhomSent((String) dataobj.get("whomSent"));
                    chatDTO.setGetDate((String) dataobj.get("getDate"));
                    chatDTO.setGetTime((String) dataobj.get("getTime"));
                    chatDTO.setMsgStr((String) dataobj.get("msgStr"));

                    if(dataobj.get("whoSend").equals(whoSend) && dataobj.get("whomSent").equals(whomSent1)){
                        /* 메세지 보낸사람이 현재 채팅방의 보낸 사람. 매니저 같은 경우. */
                        chatDTO.setViewType(Manager_Send_Message);

                    }else if(dataobj.get("whoSend").equals(whomSent1)&& dataobj.get("whomSent").equals(whoSend)){

                        /* 메세지 보낸사람이 현재 채팅방의 보낸 사람. 고객과 같은 경우. */
                        chatDTO.setViewType(Manager_Received_Message);
                        chatDTO.setWhoSend(whomSent2 +" 고객님"); //화면 보여지기 위함
                    }

                    chatDTOArrayList.add(chatDTO);

                }

                adapter = new Manager_Chat_Adapter(chatDTOArrayList, this);

                recyclerView.setAdapter(adapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));









                if(chatDTOArrayList.size()<5){
                    //채팅 내용 개수가 5개 이하면 스크롤 내릴 필요없음
                    messageBox.requestFocus();

                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);

                }else{
                    /* 채팅 내용 맨 하단으로 위치시키기 */
                    recyclerView.scrollToPosition(chatDTOArrayList.size()-1);

                    //키패드 올라오고난 뒤에 채팅내용 맨아래로??
                    /* 키패드 올라오게 하기 위함 */
                    messageBox.requestFocus();
//                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//                    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
//
//                    getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);



                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {


                            recyclerView.scrollToPosition(recyclerView.getAdapter().getItemCount() - 1);
                            recyclerView.scrollToPosition(chatDTOArrayList.size()-1);

                        }
                    }, 500); //딜레이 타임 조절
                }







            }else {
                Log.d(TAG, "=== status false :  message : ===" + obj.optString("message") );

                if(obj.optString("message").contains("채팅 내용이 없습니다.")){
                    Log.e(TAG, "=== 서버에서 받아온 채팅 내용이 없는 경우 그냥 리사이클러뷰 보여주기 ===" );

                    JSONArray dataArray  = obj.getJSONArray("data");

                    for (int i = 0; i < dataArray.length(); i++) {

                        org.json.JSONObject dataobj = dataArray.getJSONObject(i);


                        newRoomId = dataobj.getString("id");
                        Log.d(TAG, "=== newRoomId ===" +newRoomId);

                        int id = Integer.parseInt(newRoomId);
                        Log.d(TAG, "=== id ===" + Integer.toString(id));

                        id = id+1;
                        Log.d(TAG, "=== id 1더하기 완료 ===" + Integer.toString(id));

                        newRoomId = Integer.toString(id);
                        Log.d(TAG, "=== newRoomId 결과 ===" +newRoomId);

                    }


                    /* 리사이클러 뷰 준비 */
                    adapter = new Manager_Chat_Adapter(chatDTOArrayList, this);

                    recyclerView.setAdapter(adapter);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));

                }else{
                    Log.d(TAG, "=== 오류 메세지 확인하기 ===" );
                }
            }

        } catch ( JSONException e) {
            e.printStackTrace();
        }
    }












    /* 레트로핏으로 서버에 채팅 내용 저장하는 코드 */
    private void postChatInsert(String msgStr, String getDate, String getTime, String whomSent, String whoSend){

        Log.e(TAG, "=== postChatInsert 시작 ===" );

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ChatInsertInterface.JSONURL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();

        ChatInsertInterface api =retrofit.create(ChatInsertInterface.class);
        /* 인터페이스에서 정의한 메서드 / 인자로 보낼 값 넣는 곳 */
        Call<String> call = api.putChatInsert(msgStr, getDate, getTime, whomSent, whoSend);

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                Log.e("postChatInsert ", response.body());

                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        Log.e(TAG, "postChatInsert onSuccess" + response.body());


                    } else {
                        Log.e(TAG, "Returned empty response");
                    }
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

                Log.e(TAG, "=== onFailure call ===" +call+" t"+t);

            }
        });


    }

    /* 소켓 준비하는 부분 */
    private void instantiate(){
        Log.d(TAG, "=== connect() ===" );
        thread = new Thread(){
            public void run() {
                super.run();
                try {
                    client = new Socket(ip, port);
                    clientThread = new ClientThread(client, handler);
                    clientThread.start();
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        thread.start();
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
            Log.e(TAG, "=== filePath === " +filePath);


            filePath = getPath(picUri);
            Log.e(TAG, "=== picUri ===" +picUri);
            if (filePath != null) {
                try {
                    Log.e(TAG, "=== File Selected ===" );
                    Log.e("filePath", String.valueOf(filePath));
                    bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), picUri);
                    Log.e(TAG, "=== setImageBitmap ===" +bitmap);
                    //profileImage.setImageBitmap(bitmap);

                    /* 1. 서버에 이미지 업로드
                     *  2. 소켓으로 메세지 보내기. 메세지 내용에 이미지 서버 경로 넣기
                     */

                    /* 저장 클릭한 시간으로 이미지 이름 설정 함. */
                    imagename = System.currentTimeMillis();
                    Log.e(TAG, "=== modifyBtn 클릭 : === imagename : " + imagename);


                    Log.e(TAG, "=== PROFILE_CHANGE_CODE=2인 경우, 변경 있음. 새로운 이미지 서버에 업로드 해야 함. filePath ===" +filePath);
                    uploadBitmap(bitmap);
                    fileToServer="http://52.79.179.66/uploads/" + imagename + ".png";
                    //절대 경로 복사 : /var/www/html/uploads/16075223001607522295030.png
                    //이미지 위치 : http://52.79.179.66/uploads/16075223001607522295030.png


                } catch (IOException e) {
                    e.printStackTrace();
                    Log.e(TAG, "=== 갤러리에서 이미지 못 받아옴 ===" +e);
                }
            }else{
                Log.e(TAG, "=== no image selected ===" +data);
            }
        }


    }

    /* 이미지 경로 가져오는 코드 */
    public String getPath(Uri uri) {

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
                            org.json.JSONObject obj = new org.json.JSONObject(new String(response.data));
//                            Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();



                            /* 이미지 로드가 안되어, 파일 저장 후, 응답 오면 소켓으로 보내고, 저장하는걸로!!!  */
                            /* 현재 시간 가져오기 */
                            long now = System.currentTimeMillis();

                            Date mDate = new Date(now);

                            SimpleDateFormat simpleDate = new SimpleDateFormat("MM월 dd일");
                            String getDate = simpleDate.format(mDate);

                            SimpleDateFormat simpleTime = new SimpleDateFormat(" a KK시 mm분");
                            String getTime = simpleTime.format(mDate);

                            /* 채팅방 목록 정렬 위함 */
                            SimpleDateFormat sdfNow = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            // nowDate 변수에 값을 저장한다.
                            String formatDate = sdfNow.format(mDate);


                            /* 그냥 객체로 하면.. 여러개 메세지가 가서, 하나의 메세지로 보내기 위해서 제이슨오브젝트 사용함. */
                            JSONObject data2 = new JSONObject();



                            data2.put("msgStr", fileToServer); //이미지 서버 경로로 변경
                            //절대 경로 복사 : /var/www/html/uploads/16075223001607522295030.png
                            //이미지 위치 : http://52.79.179.66/uploads/16075223001607522295030.png

                            data2.put("roomId", newRoomId);
                            data2.put("getDate", getDate);
                            data2.put("getTime", getTime);
                            data2.put("whomSent", whomSent1);
                            data2.put("whoSend", whoSend);
                            data2.put("byServer", false);
                            data2.put("regiDate", formatDate);

                            postChatInsert(fileToServer, getDate, getTime, whomSent1, whoSend);


                            clientThread.send(data2);
                            messageBox.setText("");


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


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Log.d(TAG, "=== onBackPressed ===" );

        Log.d(TAG, "===  ===" );

        //있으면 넘어감
        Intent intent = new Intent(getApplicationContext(), Manager_ChatActivity.class);
        intent.addFlags(FLAG_ACTIVITY_NO_HISTORY);

        startActivity(intent);

        finish();


    }
}