package com.example.ourcleaner_201008_java.View.Manager;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ourcleaner_201008_java.Adapter.Manager_Chat_Room_Adapter;
import com.example.ourcleaner_201008_java.ClientThread;
import com.example.ourcleaner_201008_java.DTO.ChatRoomDTO;
import com.example.ourcleaner_201008_java.GlobalApplication;
import com.example.ourcleaner_201008_java.Interface.ChatRoomLastMessageSelectInterface;
import com.example.ourcleaner_201008_java.Interface.ChatRoomSelectInterface;
import com.example.ourcleaner_201008_java.Interface.ChatRoomUnreadMessageSelectInterface;
import com.example.ourcleaner_201008_java.R;
import com.example.ourcleaner_201008_java.SharedP.PreferenceManager_LastMessage;
import com.example.ourcleaner_201008_java.SharedP.Preference_User_Info;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class Manager_ChatActivity extends AppCompatActivity implements Manager_Chat_Room_Adapter.OnListItemSelectedInterface{

    private static final String TAG = "매니저용채팅방목록화면";

    /* 매니저용 전체 메뉴 변수 */
    TextView waitingTxt, myWorkListTxt, chatListTxt, moreTxt;

    //private ArrayList<HashMap<String, String>> currentUserChatRoomArrayList = null; //현재 사용자가 포함되어있는 대화창 리스트임 {1, 2}
    private HashMap<Integer, ChatRoomDTO> chatRoomHashMap = new HashMap<>();

    /* 소켓 준비용 변수 */
    Socket client;
    //String ip = "192.168.35.172"; //192.168.0.3
    //String ip = "192.168.0.3"; //192.168.187.1
    //String ip = "192.168.187.1"; //172.20.10.14
    String ip ="192.168.0.6";
    int port = 8080;

    Thread thread;
    ClientThread clientThread;
    Handler handler;
    org.json.simple.JSONObject jsonObj;



    /* 채팅 목록 리사이클러뷰 보여주기 위함 */
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private Manager_Chat_Room_Adapter adapter;
    //방번호, 채팅방 상대
    private ArrayList<ChatRoomDTO> chatRoomDTOArrayList = new ArrayList<>(); //리사이클러뷰에 보여주기 위함

    ArrayList<String> memberArrayList = new ArrayList<>(); //현재 채팅방 목록 보여주기 위함

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager__chat);
        Log.d(TAG, "=== onCreate ===" );

        /* 소켓 준비코드 */
        instantiate();






        /* 매니저용 전체 메뉴 코드 */
        //각버튼 아이디 매칭
        waitingTxt = findViewById(R.id.waitingTxt);
        myWorkListTxt = findViewById(R.id.myWorkListTxt);
        chatListTxt = findViewById(R.id.chatListTxt);
        moreTxt = findViewById(R.id.moreTxt);

        prepareData(); //[1,2] 채팅방 목록 가져오는 코드




        /* 리사이클러 뷰 관련 코드. 리사이클러 뷰 와 연결 */
        mRecyclerView = (RecyclerView) findViewById(R.id.manager_chat_recycle);

        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getApplicationContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.scrollToPosition(0);

        /* 데이터 없다고.. 에러가 뜨나 */
        adapter = new Manager_Chat_Room_Adapter(chatRoomDTOArrayList, getApplicationContext(), this::onItemSelected);
        mRecyclerView.setAdapter(adapter);

        mRecyclerView.setItemAnimator(new DefaultItemAnimator());






//PreferenceManager_LastMessage.clear(getApplicationContext());




        /* 서버 소켓으로 메세지 받는 부분임 */
        handler = new Handler(){
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                Bundle bundle = msg.getData();

                /* 받을 때, String으로만 되서.. 여기서 다시 JSONObject 으로 변환해주어야 함. */
                String msgStr = bundle.getString("msg");
                Log.d(TAG, "=== handleMessage msgStr === " + msgStr );

                /* 소켓 연결 끊을 때 exit 포함여부로.. 일단 지금은 필요없을듯.. 고민해볼 것.. */
                Log.e(TAG, "=== 메세지에 exit XXXXXXXXXXXX ===" );

                // String 형태의 msgStr을 JSONObject 으로 변경
                JSONParser parser = new JSONParser();
                Object obj = null;
                try {
                    obj = parser.parse( msgStr );
                } catch (ParseException e) {
                    e.printStackTrace();
                }


                jsonObj = (org.json.simple.JSONObject) obj;
                Log.d(TAG, "=== handleMessage msgStr을 변경 완료 -> jsonObj === " + jsonObj );

                if(jsonObj == null){

                    Log.e(TAG, "=== jsonObj.isEmpty() ===" );

                }else{
                    Log.e(TAG, "=== jsonObj 값이 있음 ===" );
                    Log.d(TAG, "=== 현재 chatRoomDTOArrayList.size() ===" + chatRoomDTOArrayList.size());

                    int position  = Integer.parseInt((String) jsonObj.get("roomId"));
                    Log.d(TAG, "=== sparseArray position ===" +position);


                    if(chatRoomHashMap.containsKey(position)){
                        Log.d(TAG, "=== chatRoomHashMap 에 메세지의 roomId가 있음 ===" );
                            /* 1. 현재 사용자가 참여하고 있는 채팅방 어레이리스트 sparseArray
                            key : roomId
                            value : chatRoom 객체

                            현재 소켓으로 받은 메세지의 roomId 가 sparesArray key에 있음
                            즉, 현재 참여하고 있는 채팅방이 존재 함!!

                            채팅방 상태만 변경하면 됨.
                            */


                        //메세지 받는 사람이 현재 사용자인 경우,
                        //보낸 사람이 현재 멤버 어레이 리스트에 포함되어 있는 경우
                        Log.d(TAG, "=== 메세지 받는 사람이 현재 사용자인 경우,보낸 사람이 현재 멤버 어레이 리스트에 포함되어 있는 경우 ===" );

                        // index 담는 어레이리스트 생성
                        ArrayList<Integer> arrayIndexList = new ArrayList<>();

                        // index 변수를 선언
                        int index = 0;
                        for (ChatRoomDTO chatRoomDTO : chatRoomDTOArrayList){
                            if(chatRoomDTO.getRoomId().equals(jsonObj.get("roomId"))){
                                arrayIndexList.add(index);
                                Log.d(TAG, "=== 반복문 index 를 찾자 ===" +index);
                                break;
                            }
                            // index 증가
                            index++;
                        }


                        Log.d(TAG, "=== 챗룸어레이리스트 에서 index 값 찾음 : ===" + index );
                        Log.d(TAG, "=== chatRoomDTOArrayList.get(index).getRoomId() :  ===" +chatRoomDTOArrayList.get(index).getRoomId());


                        ChatRoomDTO chatRoomDTO = chatRoomDTOArrayList.get(index);

                        chatRoomDTO.setRoomId(chatRoomDTO.getRoomId());
                        chatRoomDTO.setWhenSend((String) jsonObj.get("getTime"));
                        chatRoomDTO.setLastMessage((String) jsonObj.get("msgStr"));
                        chatRoomDTO.setWhomSent1(chatRoomDTO.getWhomSent1());
                        chatRoomDTO.setWhomSent2(chatRoomDTO.getWhomSent2());
                        chatRoomDTO.setAlldateStr((String) jsonObj.get("regiDate"));

                        /* 다시 해당 index에 객체 넣기 */
                        chatRoomDTOArrayList.remove(index);

                        chatRoomDTOArrayList.add(index, chatRoomDTO);
                        Collections.sort(chatRoomDTOArrayList);
                        adapter.notifyDataSetChanged();

                        /* 계속 추가되는 것 막음 */
                        int roomId = Integer.parseInt((String) jsonObj.get("roomId"));
                        chatRoomHashMap.put(roomId, chatRoomDTO);
                        Log.d(TAG, "=== chatRoomHashMap 에 객체 넣음 ===" +roomId );
                        Log.d(TAG, "=== chatRoomHashMap ===" +chatRoomHashMap.toString());



//                        if(jsonObj.get("whoSend").equals(GlobalApplication.currentUser)
//                                && memberArrayList.contains(jsonObj.get("whomSent"))){
//                        /* 1.
//                        메세지 보낸 사람이 현재 사용자인 경우,
//                        받는 사람이 현재 멤버 어레이 리스트에 포함되어 있는 경우
//                        */
//                            Log.d(TAG, "=== 메세지 보낸 사람이 현재 사용자인 경우, 받는 사람이 현재 멤버 어레이 리스트에 포함되어 있는 경우 ===" );
//
//
////                            ChatRoomDTO hasMapChatRoomDTO = chatRoomHashMap.get(Integer.parseInt((String) jsonObj.get("roomId")));
////                            Log.d(TAG, "=== 해쉬맵의 dto 마지막 메세지 ===" +hasMapChatRoomDTO.getLastMessage());
////                            Log.d(TAG, "===  ==="+hasMapChatRoomDTO.getUnreadNum() );
////                            Log.d(TAG, "===  ==="+hasMapChatRoomDTO.getRoomId() );
////                            Log.d(TAG, "===  ==="+hasMapChatRoomDTO.getWhenSend() );
////                            Log.d(TAG, "===  ==="+hasMapChatRoomDTO.getWhomSent1() );
////                            Log.d(TAG, "===  ==="+hasMapChatRoomDTO.getAlldateStr() );
////                            Log.d(TAG, "===  ==="+hasMapChatRoomDTO.getCurrentUser() );
////                            Log.d(TAG, "===  ==="+hasMapChatRoomDTO.getWhomSent2() );
////                            Log.d(TAG, "===  ==="+hasMapChatRoomDTO.getWhomSent3() );
//
//                            // index 담는 어레이리스트 생성
//                            ArrayList<Integer> arrayIndexList = new ArrayList<>();
//
//                            // index 변수를 선언
//                            int index = 0;
//                            for (ChatRoomDTO chatRoomDTO : chatRoomDTOArrayList){
//                                if(chatRoomDTO.getRoomId().equals(jsonObj.get("roomId"))){
//                                    arrayIndexList.add(index);
//                                }
//                                // index 증가
//                                index++;
//                            }
//                            Log.d(TAG, "=== 챗룸어레이리스트 에서 index 값 찾음 : ===" + index );
//                            Log.d(TAG, "=== chatRoomDTOArrayList.get(index).getRoomId() :  ===" +chatRoomDTOArrayList.get(index).getRoomId());
//
//
//                            ChatRoomDTO chatRoomDTO = chatRoomDTOArrayList.get(index);
//
//                            chatRoomDTO.setRoomId(chatRoomDTO.getRoomId());
//                            chatRoomDTO.setWhenSend((String) jsonObj.get("getTime"));
//                            chatRoomDTO.setLastMessage((String) jsonObj.get("msgStr"));
//                            chatRoomDTO.setWhomSent1(chatRoomDTO.getWhomSent1());
//                            chatRoomDTO.setWhomSent2(chatRoomDTO.getWhomSent2());
//
//                            /* 다시 해당 index에 객체 넣기 */
//                            chatRoomDTOArrayList.add(index, chatRoomDTO);
//                            Collections.sort(chatRoomDTOArrayList);
//                            adapter.notifyDataSetChanged();
//
//                            /* 리사이클러뷰의 어레이리스트의 순서를 실시간으로 변경하기 위함.
//                            * 해쉬맵에 해당하는 룸id로 객체 찾아오기
//                            * 어레이리스트에서 해당 객체를 찾음 -> 방아이디로
//                            * 그리고 그 객체를 해쉬맵의 객체로 변경함
//                            * 그리고 정렬함 */
//
////                            chatRoomDTOArrayList.remove(i);
////                            chatRoomDTOArrayList.add(i, chatRoomDTO);
////
////                            Collections.sort(chatRoomDTOArrayList);
////
////                            mRecyclerView.setAdapter(adapter);
////                            adapter.notifyDataSetChanged();
////                            //chatRoomDTOArrayList.add(chatRoomDTO);
////                            //adapter.notifyDataSetChanged();
//
//                            /* 쉐어드에 마지막 메세지 저장하기 */
//                            ArrayList<String> arrayList = new ArrayList<>();
//
//                            arrayList.add(chatRoomDTO.getRoomId());
//                            arrayList.add((String) jsonObj.get("getTime"));
//                            arrayList.add((String) jsonObj.get("msgStr"));
//                            arrayList.add(chatRoomDTO.getWhomSent1());
//                            arrayList.add(chatRoomDTO.getWhomSent2());
//
//                            PreferenceManager_LastMessage.setStringArrayPref(getActivity(), chatRoomDTO.getWhomSent1(), arrayList);
//
//
//
//
//                        }else if (jsonObj.get("whomSent").equals(GlobalApplication.currentUser)
//                                && memberArrayList.contains(jsonObj.get("whoSend"))){
//
//
//
//                        }else{
//                            Log.d(TAG, "=== 왜 안들어가 ===" );
//                        }













                    }else if( jsonObj.get("whomSent").equals(GlobalApplication.currentManager) ){
                        Log.d(TAG, "=== chatRoomHashMap 에 메세지의 roomId가 없음 + 현재사용자에게 온 새로운 메세지 ===" );
                        /*
                         * 2. 현재 사용자가 참여하고 있는 채팅방이 없음
                         *
                         * 이 경우에, 두 가지 가능성 존재
                         *   1) 나랑 상관없는 메세지
                         *   2) 새로 생긴 채팅방 => 이 경우에, 소켓으로 메세지 받는 사람에 현재 사용자. 매니저가 포함되어 있음
                         * */

                        Log.d(TAG, "=== chatRoomDTOArrayList size ===" +chatRoomDTOArrayList.size() );

                        /* 쉐어드에서 현재 마지막 메세지 / 상대방의 정보 가져오기 위함 */
                        ArrayList<String> arrayList, arrayList2;

                        //arrayList = PreferenceManager_LastMessage.getStringArrayPref(getApplicationContext(),chatRoomDTO.getWhomSent1());
                        arrayList2 = Preference_User_Info.getStringArrayPref(getApplicationContext(), (String) jsonObj.get("whoSend"));


                        ChatRoomDTO chatRoomDTO = new ChatRoomDTO();
                        chatRoomDTO.setCurrentUser(GlobalApplication.currentManager);
                        chatRoomDTO.setAlldateStr((String) jsonObj.get("regiDate")); //정렬 필요
                        chatRoomDTO.setWhomSent1((String) jsonObj.get("whoSend")); //메세지 상대방
                        chatRoomDTO.setRoomId((String) jsonObj.get("roomId"));
                        chatRoomDTO.setLastMessage((String) jsonObj.get("msgStr"));
                        chatRoomDTO.setUnreadNum(1); //일단 1로 하고.. 계속 보낼땐 어떻게 할지 고민하자
                        chatRoomDTO.setWhomSent2(arrayList2.get(1)); //이름
                        chatRoomDTO.setWhenSend((String) jsonObj.get("getTime"));

                        chatRoomDTOArrayList.add(chatRoomDTO); //위치가 어디로 들어가는지 확인할것
                        Collections.sort(chatRoomDTOArrayList);
                        adapter.notifyDataSetChanged();
                        Log.d(TAG, "=== chatRoomDTOArrayList size ===" +chatRoomDTOArrayList.size() );


                    }else{
                        Log.d(TAG, "=== chatRoomHashMap 에 메세지의 roomId가 없음 + 현재 사용자에게 온 메세지가 아님 -> 아무 작업할 필요없음===" );
                    }


                }

            }
        };













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
//                            Intent intent3 = new Intent(getApplicationContext(), Manager_ChatActivity.class);
//                            startActivity(intent3);
//                            finish();
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

    // 현재 사용자가 포함된 chatRoom 데이터의 아이디 가져오는 코드
    private void prepareData() {
        Log.d(TAG, "=== prepareData() ===" );

        //String url = "http://52.79.179.66/chatRoomSelect.php?currentUser="+ GlobalApplication.currentUser+"&whoClientManager=1";


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ChatRoomSelectInterface.JSONURL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();

        ChatRoomSelectInterface api = retrofit.create(ChatRoomSelectInterface.class);

        Map<String, String> mapdata = new HashMap<>();
        mapdata.put("currentUser", GlobalApplication.currentManager);
        mapdata.put("whoClientManager", "2");

        Call<String> call = api.getChatRoomSelect(mapdata);

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


    //채팅방 목록 가져와서 currentUserChatRoomArrayList에 담는 코드
    private void writeRecycler2(String response){
        try {
            //getting the whole json object from the response
            JSONObject obj = new JSONObject(response);
            Log.e(TAG, "=== response ===" +response);

            if(obj.optString("status").equals("true")){

                JSONArray dataArray  = obj.getJSONArray("data");

                chatRoomHashMap = new HashMap<>();

                for (int i = 0; i < dataArray.length(); i++) {
                    JSONObject dataobj = dataArray.getJSONObject(i);

                    ChatRoomDTO chatRoomDTO = new ChatRoomDTO(); //리사이클러뷰의 방한개 아이템


                    String memberList, member;
                    memberList = dataobj.getString("memberList");
                    member = memberList.replace(GlobalApplication.currentManager, ""); //현재 사용자 제거하고
                    member = member.replace("/", "");// 문자/ 제거하면, 상대방만 남음
                    Log.d(TAG, "=== member 상대방 맞는지 확인하기 ===" +member);


                    chatRoomDTO.setCurrentUser(GlobalApplication.currentManager);
                    chatRoomDTO.setWhomSent1(member); //상대방의 메일은 일단 가져올 수 있음
                    chatRoomDTO.setRoomId(dataobj.getString("id"));

                    /* 1. 지금 알고 있는 정보
                    setCurrentUser
                    setWhomSent1
                    setRoomId


                    * 2. 필요한 정보
                    setLastMessage
                    setUnreadNum -> 쉐어드에서 현재 메세지 가져와서, 차이 갯수 가져와야 함
                    setWhenSend

                    * */

                    postForLastMessage(chatRoomDTO);

                }


            }else if(obj.optString("status").equals("false")){
                Log.e(TAG, "=== message 현재 사용자의 이메일을 보냈을 때, 채팅방이 없는 경우 임. ===" + obj.optString("message"));

                /* 채팅방 목록 정렬 및 어댑터에 리스트 넣기 */
                Collections.sort(chatRoomDTOArrayList);
                mRecyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }

        } catch (JSONException e) {
            e.printStackTrace();
            Log.e(TAG, "=== 에러코드  ===" + e );

            /* 채팅방 목록 정렬 및 어댑터에 리스트 넣기 */
            Collections.sort(chatRoomDTOArrayList);
            mRecyclerView.setAdapter(adapter);
            adapter.notifyDataSetChanged();

        }

    }


    /* 해당 채팅방의 마지막 메세지 가져오기 위한 코드 */
    private void postForLastMessage(ChatRoomDTO chatRoomDTO){
        Log.d(TAG, "=== postForLastMessage(ChatRoomDTO chatRoomDTO) 메서드 몇 번 호출되는지 확인해야 함 방의 개수에 해당!! ===" );

    /* 1. 지금 알고 있는 정보
    setCurrentUser
    setWhomSent1
    setRoomId

    * 2. 필요한 정보
    setLastMessage
    setUnreadNum -> 쉐어드에서 현재 메세지 가져와서, 차이 갯수 가져와야 함
    setWhenSend

    * */

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ChatRoomLastMessageSelectInterface.JSONURL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();

        ChatRoomLastMessageSelectInterface api = retrofit.create(ChatRoomLastMessageSelectInterface.class);

        Map<String, String> mapdata = new HashMap<>();
        mapdata.put("currentUser", chatRoomDTO.getCurrentUser()); //현재 사용자 이메일. 매니저
        mapdata.put("whomSent1", chatRoomDTO.getWhomSent1()); // 상대방 이메일. 고객
        mapdata.put("roomId", chatRoomDTO.getRoomId()); // 채팅방 roomId
        mapdata.put("whoClientManager", "2"); // TODO: 2021-01-23 매니저**

        Call<String> call = api.getLastMessageSelect(mapdata);

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Log.e(TAG, "postForLastMessage Responsestring : "+response.body());
                //Toast.makeText()
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        Log.e(TAG, "postForLastMessage onSuccess"+response.body());

                        String jsonresponse = response.body();

                        JSONObject obj = null;
                        try {
                            obj = new JSONObject(jsonresponse);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.d(TAG, "=== postForLastMessage 에러코드 ==="+e );
                        }

                        if(obj.optString("status").equals("true")){

                            try {
                                JSONArray dataArray  = obj.getJSONArray("data");
                                Log.d(TAG, "=== postForLastMessage status true ===" +dataArray);

                                    /* 1. 지금 알고 있는 정보
                                    setCurrentUser
                                    setWhomSent1
                                    setRoomId

                                    * 2. 필요한 정보
                                    setLastMessage
                                    setUnreadNum -> 쉐어드에서 현재 메세지 가져와서, 차이 갯수 가져와야 함
                                    setWhenSend
                                    (추가) 전체 날짜 가져오기 -> 정렬위함
                                    * */

                                for (int i = 0; i < dataArray.length(); i++) {
                                    JSONObject dataobj = dataArray.getJSONObject(i);

                                    /* 쉐어드에서 현재 마지막 메세지 / 상대방의 정보 가져오기 위함 */
                                    ArrayList<String> arrayList, arrayList2;

                                    arrayList = PreferenceManager_LastMessage.getStringArrayPref(getApplicationContext(),chatRoomDTO.getWhomSent1());
                                    arrayList2 = Preference_User_Info.getStringArrayPref(getApplicationContext(), chatRoomDTO.getWhomSent1());

                                    ChatRoomDTO chatRoomDTO2 = new ChatRoomDTO();

                                    chatRoomDTO2.setCurrentUser(chatRoomDTO.getCurrentUser());
                                    chatRoomDTO2.setWhomSent1(chatRoomDTO.getWhomSent1());
                                    chatRoomDTO2.setWhomSent2(arrayList2.get(1)); //대화 상대방 매니저 이름
                                    //chatRoomDTO2.setWhomSent3(arrayList2.get(2)); //대화 상대방 프로필
                                    chatRoomDTO2.setRoomId(chatRoomDTO.getRoomId());

                                    /* 객체에는 모두 서버에서 받아온거 그대로!! */
                                    chatRoomDTO2.setLastMessage(dataobj.getString("msgStr"));
                                    chatRoomDTO2.setWhenSend(dataobj.getString("getTime"));
                                    chatRoomDTO2.setAlldateStr(dataobj.getString("regiDate"));
                                    Log.d(TAG, "=== setAlldateStr 정렬 위함 ===" +dataobj.getString("regiDate"));


                                    /* 쉐어드에 저장된 마지막 메세지와
                                     * 현재 가져온 마지막 메세지가 같은지 아닌지
                                     * 체크하는 구간
                                     * 1. 같으면, setUnreadNum 0이고, 객체리스트에 넣고, 정렬하기
                                     * 2. 다르면, setUnreadNum 개수 받아오기 위해서 객체를 메서드에 담아서 다시 넘겨야 함..ㅠㅠ */

                                    if(arrayList.size()>0){
                                        Log.d(TAG, "=== 쉐어드에 마지막메세지 리스트가 있는 경우   어레이 크기 :  ===" +arrayList.size());

                                        //채팅방번호랑, 시간, 내용이 같아야 함.
                                        if(chatRoomDTO2.getLastMessage().equals(arrayList.get(2)) //메세지 내용
                                                && chatRoomDTO2.getWhenSend().equals(arrayList.get(1)) //시간
                                                && chatRoomDTO2.getRoomId().equals(arrayList.get(0))) //채팅방번호
                                        {// 1. 같으면
                                            chatRoomDTO2.setUnreadNum(0);

                                            chatRoomDTOArrayList.add(chatRoomDTO2);

                                            /* 아래 코드에서 채팅방 객체가 모두 준비되면, 쓸 코드임 위에 add 코드 까지 해서...ㅎㅎ */
                                            Collections.sort(chatRoomDTOArrayList);
                                            adapter.notifyDataSetChanged();

                                            int roomId = Integer.parseInt(chatRoomDTO2.getRoomId());
                                            chatRoomHashMap.put(roomId, chatRoomDTO2);
                                            Log.d(TAG, "=== chatRoomHashMap음 ===" +roomId );
                                            Log.d(TAG, "=== chatRoomHashMap ===" +chatRoomHashMap.toString());



                                        }else{
                                            /* 2. 다름 === >  안읽은 메세지 가져오기 위해서 객체 다시 옮김 */
                                            postForGetUnreadNum(chatRoomDTO2, dataobj.getInt("id")); //서버 마지막 메세지의 id임
                                            Log.d(TAG, "=== 원래 방 있는데, 마지막 메세지가 다른 경우 chatRoomHashMap 안읽은 메세지 개수 가져온 뒤에 객체 넣기기 ===");
                                            Log.d(TAG, "=== chatRoomHashMap ===" +chatRoomHashMap.toString());


                                            /* 1. 지금 알고 있는 정보
                                            setCurrentUser
                                            setWhomSent1
                                            setRoomId
                                            setLastMessage
                                            setWhenSend
                                            setAlldateStr -> 정렬 위함

                                            * 2. 필요한 정보
                                            setUnreadNum -> 쉐어드에서 현재 메세지 가져와서, 차이 갯수 가져와야 함
                                            * */

                                            /* 채팅방 목록 정렬 및 어댑터에 리스트 넣기 */
                                            Collections.sort(chatRoomDTOArrayList);
                                            mRecyclerView.setAdapter(adapter);
                                            adapter.notifyDataSetChanged();
                                        }


                                    }else{

                                        Log.d(TAG, "=== 쉐어드에 마지막메세지 리스트가 전혀 없는 경우 즉, 방이 새로 생긴 경우 ");

                                        /* 안읽은 메세지 가져오기 위해서 객체 다시 옮김 */
                                        postForGetUnreadNum(chatRoomDTO2, dataobj.getInt("id")); //서버 마지막 메세지의 id임
                                        Log.d(TAG, "=== 메세지 id ===" );
                                        Log.d(TAG, "=== 원래 방이 없음. 새로생긴 경우, chatRoomHashMap 안읽은 메세지 개수 가져온 뒤에 객체 넣기기 ===");
                                        Log.d(TAG, "=== chatRoomHashMap ===" +chatRoomHashMap.toString());

                                        /* 채팅방 목록 정렬 및 어댑터에 리스트 넣기 */
                                        Collections.sort(chatRoomDTOArrayList);
                                        mRecyclerView.setAdapter(adapter);
                                        adapter.notifyDataSetChanged();

                                    }






                                }



                            } catch (JSONException e) {
                                e.printStackTrace();
                                Log.d(TAG, "=== status true 이후 에러코드 ==="+ e );

                                /* 채팅방 목록 정렬 및 어댑터에 리스트 넣기 */
                                Collections.sort(chatRoomDTOArrayList);
                                mRecyclerView.setAdapter(adapter);
                                adapter.notifyDataSetChanged();
                            }

                        }else if(obj.optString("status").equals("false")){

                            Log.e(TAG, "=== obj.optString status false ===" + obj.optString("message"));

                            /* 채팅방 목록 정렬 및 어댑터에 리스트 넣기 */
                            Collections.sort(chatRoomDTOArrayList);
                            mRecyclerView.setAdapter(adapter);
                            adapter.notifyDataSetChanged();



                        }

                    } else {
                        Log.e(TAG, "=== 응답이 그냥 널인 경우 채팅방 목록 가져오는 서버 부분 ===");

                        /* 채팅방 목록 정렬 및 어댑터에 리스트 넣기 */
                        Collections.sort(chatRoomDTOArrayList);
                        mRecyclerView.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e(TAG, "onFailure");

                /* 채팅방 목록 정렬 및 어댑터에 리스트 넣기 */
                Collections.sort(chatRoomDTOArrayList);
                mRecyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();

            }
        });
    }

    //안읽은 메세지 개수 가져오기 위함
    private void postForGetUnreadNum(ChatRoomDTO chatRoomDTO, Integer id){ // Integer id : 서버 마지막 메세지의 id임
        Log.d(TAG, "=== postForGetUnreadNum(ChatRoomDTO chatRoomDTO) 메서드 몇 번 호출되는지 확인해야 함 ===" );

    /* 1. 지금 알고 있는 정보
    setCurrentUser
    setWhomSent1
    setRoomId
    setLastMessage
    setWhenSend
    setAlldateStr -> 정렬 위함

    * 2. 필요한 정보
    setUnreadNum -> 쉐어드에서 현재 메세지 가져와서, 차이 갯수 가져와야 함
    * */

        /* 쉐어드에서 현재 마지막 메세지 / 상대방의 정보 가져오기 위함 */
        ArrayList<String> arrayList, arrayList2;

        arrayList = PreferenceManager_LastMessage.getStringArrayPref(getApplicationContext(),chatRoomDTO.getWhomSent1());
        arrayList2 = Preference_User_Info.getStringArrayPref(getApplicationContext(), chatRoomDTO.getWhomSent1());



        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ChatRoomUnreadMessageSelectInterface.JSONURL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();

        ChatRoomUnreadMessageSelectInterface api = retrofit.create(ChatRoomUnreadMessageSelectInterface.class);

        Map<String, String> mapdata = new HashMap<>();
        mapdata.put("currentUser", chatRoomDTO.getCurrentUser()); //현재 이용자 이메일. 고객
        mapdata.put("whomSent1", chatRoomDTO.getWhomSent1()); //채팅 상대방 이메일. 매니저
        mapdata.put("roomId", chatRoomDTO.getRoomId()); //채팅방id

        //마지막 메세지 찾는 단서임.ㅠㅠㅠ + 더하기 보낸사람이 상대방이어야 겠지ㅣㅣ???

        if(arrayList.size()>0){
            Log.d(TAG, "=== arrayList.size() 0보다 큰 경우 ===" );
            mapdata.put("whenSend", arrayList.get(1)); // ** 쉐어드에 저장된 채팅 보낸 시간 -> 오전 03시 48분
            mapdata.put("lastMessage", arrayList.get(2)); // ** 쉐어드에 저장된 채팅 보낸 시간 -> 오전 03시 48분
        }else{
            Log.d(TAG, "=== arrayList.size() 0인 경우 ===" );
            mapdata.put("whenSend", "blank"); // ** 쉐어드에 저장된 채팅 보낸 시간 -> 오전 03시 48분
            mapdata.put("lastMessage", "blank"); // ** 쉐어드에 저장된 채팅 보낸 시간 -> 오전 03시 48분
        }

        mapdata.put("id", Integer.toString(id)); //채팅 id 가져오기

        mapdata.put("whoClientManager", "2");

        Call<String> call = api.getUnreadMessageSelect(mapdata);

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Log.e(TAG, "postForGetUnreadNum Responsestring : "+response.body());
                //Toast.makeText()
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        Log.e(TAG, "postForGetUnreadNum onSuccess"+response.body());

                        String jsonresponse = response.body();

                        JSONObject obj = null;
                        try {
                            obj = new JSONObject(jsonresponse);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.d(TAG, "=== postForGetUnreadNum 에러코드 ==="+e );
                        }

                        if(obj.optString("status").equals("true")){

                            try {
                                JSONArray dataArray  = obj.getJSONArray("data");
                                Log.d(TAG, "=== postForGetUnreadNum status true ===" +dataArray);

                                Log.e(TAG, "=== postForGetUnreadNum dataArray.length() 안읽은 메세지 개수 ===" +dataArray.length());
                                    /* 1. 지금 알고 있는 정보
                                    setCurrentUser
                                    setWhomSent1
                                    setRoomId

                                    * 2. 필요한 정보
                                    setLastMessage
                                    setUnreadNum -> 쉐어드에서 현재 메세지 가져와서, 차이 갯수 가져와야 함
                                    setWhenSend
                                    (추가) 전체 날짜 가져오기 -> 정렬위함
                                    * */

                                chatRoomDTO.setUnreadNum(dataArray.length()); //안읽은 메세지 개수만 가져오면 됨
                                Log.d(TAG, "=== 안읽은 메세지 ===" +dataArray.length());

                                chatRoomDTOArrayList.add(chatRoomDTO);

                                /* 아래 코드에서 채팅방 객체가 모두 준비되면, 쓸 코드임 위에 add 코드 까지 해서...ㅎㅎ */
                                Collections.sort(chatRoomDTOArrayList);

                                adapter.notifyDataSetChanged();

                                int roomId = Integer.parseInt(chatRoomDTO.getRoomId());
                                chatRoomHashMap.put(roomId, chatRoomDTO);
                                Log.d(TAG, "=== chatRoomHashMap 에 객체 넣음 ===" +roomId );




                            } catch (JSONException e) {
                                e.printStackTrace();
                                Log.d(TAG, "=== dataArray 에러코드 ==="+ e );
                            }

                        }else if(obj.optString("status").equals("false")){

                            Log.e(TAG, "=== 안읽은 메세지 개수 못 가져 옴 일단 그래도 채팅방 목록 띄우자 ===" + obj.optString("message"));

                            chatRoomDTOArrayList.add(chatRoomDTO);
                            adapter.notifyDataSetChanged();
                            int roomId = Integer.parseInt(chatRoomDTO.getRoomId());
                            chatRoomHashMap.put(roomId, chatRoomDTO);
                            Log.d(TAG, "=== chatRoomHashMap 에 객체 넣음 ===" +roomId );


                            /* 채팅방 목록 정렬 및 어댑터에 리스트 넣기 */
                            Collections.sort(chatRoomDTOArrayList);
                            mRecyclerView.setAdapter(adapter);
                            adapter.notifyDataSetChanged();



                        }


                    } else {

                        Log.e(TAG, "onEmptyResponse"+"Returned empty response");
                        /* 채팅방 목록 정렬 및 어댑터에 리스트 넣기 */
                        Collections.sort(chatRoomDTOArrayList);
                        mRecyclerView.setAdapter(adapter);
                        adapter.notifyDataSetChanged();                    }
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e(TAG, "onFailure");

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

                }  catch (UnknownHostException uhe) {
// 소켓 생성 시 전달되는 호스트(www.unknown-host.com)의 IP를 식별할 수 없음.

                    Log.e(TAG," 생성 Error : 호스트의 IP 주소를 식별할 수 없음.(잘못된 주소값 또는 호스트이름 사용)");

                } catch (IOException ioe) {
// 소켓 생성 과정에서 I/O 에러 발생. 주로 네트워크 응답 없음.

                    Log.e(TAG," 생성 Error : 네트워크 응답 없음");

                } catch (SecurityException se) {
// security manager에서 허용되지 않은 기능 수행.

                    Log.e(TAG," 생성 Error : 보안(Security) 위반에 대해 보안 관리자(Security Manager)에 의해 발생. (프록시(proxy) 접속 거부, 허용되지 않은 함수 호출)");

                } catch (IllegalArgumentException le) {
// 소켓 생성 시 전달되는 포트 번호(65536)이 허용 범위(0~65535)를 벗어남.

                    Log.e(TAG," 생성 Error : 메서드에 잘못된 파라미터가 전달되는 경우 발생.(0~65535 범위 밖의 포트 번호 사용, null 프록시(proxy) 전달)");

                }
            }
        };
        thread.start();
    }


    @Override
    public void onItemSelected(View v, int position) {
        Log.d(TAG, "=== onItemSelected ===" +position);


        Intent intent = new Intent(getApplicationContext(), Manager_Chat_Room_Activity.class);

        intent.putExtra("whomSent1", chatRoomDTOArrayList.get(position).getWhomSent1()); //고객의 이메일
        intent.putExtra("whomSent2", chatRoomDTOArrayList.get(position).getWhomSent2()); //고객의 지역/ 이름 화성시 박용현
        intent.putExtra("whoSend", GlobalApplication.currentManager); //나. 매니저의 이메일


        startActivity(intent);

        finish();

    }
}