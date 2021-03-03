package com.example.ourcleaner_201008_java.View.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ourcleaner_201008_java.Adapter.Chat_Room_Adapter;
import com.example.ourcleaner_201008_java.ClientThread;
import com.example.ourcleaner_201008_java.DTO.ChatRoomDTO;
import com.example.ourcleaner_201008_java.GlobalApplication;
import com.example.ourcleaner_201008_java.Interface.ChatRoomLastMessageSelectInterface;
import com.example.ourcleaner_201008_java.Interface.ChatRoomSelectInterface;
import com.example.ourcleaner_201008_java.Interface.ChatRoomUnreadMessageSelectInterface;
import com.example.ourcleaner_201008_java.R;
import com.example.ourcleaner_201008_java.SharedP.PreferenceManager_LastMessage;
import com.example.ourcleaner_201008_java.SharedP.Preference_User_Info;
import com.example.ourcleaner_201008_java.View.Chat_Room_Activity;
import com.example.ourcleaner_201008_java.View.MainActivity;

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

/* 고객용 앱에서
* 나의 채팅 목록 보여주기 */



public class FragmentChat extends Fragment  implements Chat_Room_Adapter.OnListItemSelectedInterface{

    private static final String TAG = "FragmentChat";

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

    /*프래그먼트에서 메인엑티비티 컨텍스트 필요한 경우*/
    MainActivity mainActivity;

    /* 채팅 목록 리사이클러뷰 보여주기 위함 */
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private Chat_Room_Adapter adapter;
    //방번호, 채팅방 상대
    private ArrayList<ChatRoomDTO> chatRoomDTOArrayList = new ArrayList<>(); //리사이클러뷰에 보여주기 위함

    ArrayList<String> memberArrayList = new ArrayList<>(); //현재 채팅방 목록 보여주기 위함


    //private ArrayList<HashMap<String, String>> currentUserChatRoomArrayList = null; //현재 사용자가 포함되어있는 대화창 리스트임 {1, 2}
    private HashMap<Integer, ChatRoomDTO> chatRoomHashMap = new HashMap<>();

    SparseArray<ChatRoomDTO> sparseArray = new SparseArray<>(); //채팅방 id 저장하기 위함


    /* 서버에서 내 장소 정보 받아오기 위한 변수 */
    String jsonResponse;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//            return inflater.inflate(R.layout.fragment_chat, container, false);

        Log.d(TAG, "=== onCreateView ===" );
//        FragmentTransaction ft = getFragmentManager().beginTransaction();
//        ft.detach(this).attach(this).commit();


        // 매니저와의 채팅 목록을 보여주는 화면임
        /* 현재 사용자를 먼저 서버로 보내서 현재 사용자가 포함된 채팅방 목록을 가져옴
        *  그리고 그 채팅방의 상대방의 이름, 가장 마지막 메세지, 가장 마지막 메세지의 시간, 읽지 않은 메세지의 개수를 보여줌
        *  해당 채팅 목록 아이템 1개를 클릭하면, ** 해당 채팅방id, 상대방의 이메일, 상대방 이름, 상대방의 프로필 사진을 인텐트로 넘겨줌
        *
        * 1. 현재 매니저의 아이디를 서버로 보내서, 현재 사용자가 포함된 채팅방id 목록을 가져옴
        * 없는 경우, -> 현재 대화중인 매니저가 없습니다.
        * 있는 경우, -> 채팅방에 대한 정보를 가져옴.
        * 정렬은 마지막 메세지의 시간에 따라서 방 객체의 정렬을 변경함
        *
        * */


        /* fragment 관련 코드. 레이아웃 연결 */
        ViewGroup rootView = (ViewGroup)inflater.inflate(R.layout.fragment_chat, container, false);

        /* 리사이클러 뷰 관련 코드. 리사이클러 뷰 와 연결 */
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.manager_chat_recycle);

        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.scrollToPosition(0);

        /* 데이터 없다고.. 에러가 뜨나 */
        adapter = new Chat_Room_Adapter(chatRoomDTOArrayList, getActivity(), this::onItemSelected);
        mRecyclerView.setAdapter(adapter);

        mRecyclerView.setItemAnimator(new DefaultItemAnimator());


        /* 리사이클러뷰에 계속 아이템 추가되는거 막기 */
        chatRoomDTOArrayList.clear();


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

                //prepareData(); //[1,2] 채팅방 목록 가져오는 코드

                jsonObj = (org.json.simple.JSONObject) obj;
                Log.d(TAG, "=== handleMessage msgStr을 변경 완료 -> jsonObj === " + jsonObj );

                if(jsonObj==null){

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

                        /* 읽지 않은 메세지 개수 가져와서 더하기1 */
                        int unreadNum = chatRoomDTO.getUnreadNum();
                        Log.d(TAG, "=== unreadNum 더하기 1 하기 ===" +unreadNum);
                        unreadNum = unreadNum + 1;
                        Log.d(TAG, "=== unreadNum  ===" +unreadNum);

                        chatRoomDTO.setRoomId(chatRoomDTO.getRoomId());
                        chatRoomDTO.setWhenSend((String) jsonObj.get("getTime"));
                        chatRoomDTO.setLastMessage((String) jsonObj.get("msgStr"));
                        chatRoomDTO.setWhomSent1(chatRoomDTO.getWhomSent1());
                        chatRoomDTO.setWhomSent2(chatRoomDTO.getWhomSent2());
                        chatRoomDTO.setAlldateStr((String) jsonObj.get("regiDate"));
                        chatRoomDTO.setUnreadNum(unreadNum);

                        /* 다시 해당 index에 객체 넣기 */
                        chatRoomDTOArrayList.remove(index);

                        chatRoomDTOArrayList.add(index, chatRoomDTO);
                        Collections.sort(chatRoomDTOArrayList);
                        adapter.notifyDataSetChanged();








                    }else if(jsonObj.get("whoSend").equals(GlobalApplication.currentUser) ||
                            jsonObj.get("whomSent").equals(GlobalApplication.currentUser) ){
                        Log.d(TAG, "=== chatRoomHashMap 에 메세지의 roomId가 없음 + 현재사용자에게 온 새로운 메세지 ===" );
                        /*
                         * 2. 현재 사용자가 참여하고 있는 채팅방이 없음
                         *
                         * 이 경우에, 두 가지 가능성 존재
                         *   1) 나랑 상관없는 메세지
                         *   2) 새로 생긴 채팅방 => 이 경우에, 소켓으로 받은 메세지의 보낸 사람 혹은 받은 사람이 현재 사용자를 포함하고 있음
                         * */

                        Log.d(TAG, "=== chatRoomDTOArrayList size ===" +chatRoomDTOArrayList.size() );

                        /* 쉐어드에서 현재 마지막 메세지 / 상대방의 정보 가져오기 위함 */
                        ArrayList<String> arrayList, arrayList2;

                        //arrayList = PreferenceManager_LastMessage.getStringArrayPref(getApplicationContext(),chatRoomDTO.getWhomSent1());
                        arrayList2 = Preference_User_Info.getStringArrayPref(getActivity(), (String) jsonObj.get("whoSend"));


                        ChatRoomDTO chatRoomDTO = new ChatRoomDTO();
                        chatRoomDTO.setCurrentUser(GlobalApplication.currentUser);
                        chatRoomDTO.setAlldateStr((String) jsonObj.get("regiDate")); //정렬 필요
                        chatRoomDTO.setWhomSent1((String) jsonObj.get("whoSend")); //메세지 상대방
                        chatRoomDTO.setRoomId((String) jsonObj.get("roomId"));
                        chatRoomDTO.setLastMessage((String) jsonObj.get("msgStr"));
                        chatRoomDTO.setUnreadNum(1); //일단 1로 하고.. 계속 보낼땐 어떻게 할지 고민하자
                        chatRoomDTO.setWhomSent2(arrayList2.get(1)); //이름
                        chatRoomDTO.setWhenSend((String) jsonObj.get("getTime"));
                        chatRoomDTO.setWhomSent3(arrayList2.get(2)); //프로필 사진

                        chatRoomDTOArrayList.add(chatRoomDTO); //위치가 어디로 들어가는지 확인할것 ㄱㅊㄱㅊ,... 근데 계속 추가됨..
                        Collections.sort(chatRoomDTOArrayList);
                        adapter.notifyDataSetChanged();
                        Log.d(TAG, "=== chatRoomDTOArrayList size ===" +chatRoomDTOArrayList.size() );



                        /* 계속 추가되는 것 막음 */
                        int roomId = Integer.parseInt((String) jsonObj.get("roomId"));
                        chatRoomHashMap.put(roomId, chatRoomDTO);
                        Log.d(TAG, "=== chatRoomHashMap 에 객체 넣음 ===" +roomId );
                        Log.d(TAG, "=== chatRoomHashMap ===" +chatRoomHashMap.toString());







                    }else{
                        Log.d(TAG, "=== chatRoomHashMap 에 메세지의 roomId가 없음 + 현재 사용자에게 온 메세지가 아님 -> 아무 작업할 필요없음===" );
                    }























                }

            }
        };

        return rootView;
    }

    //onCreate에서 데이터 넣기
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG+" 생명 주기", "onCreate()");



        //PreferenceManager_LastMessage.clear(getActivity());



        // 1. 서버에서 리사이클러 뷰에 뿌릴 데이터 가져오기
        // 2. 가져온 데이터를 리사이클러 뷰에 넣을 list에 넣기
        // 3. list를 리사이클러 뷰에 넣기...

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

        //for Test
        Log.d(TAG, "=== prepareData() ===" );

        prepareData(); //[1,2] 채팅방 목록 가져오는 코드

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
        mapdata.put("currentUser", GlobalApplication.currentUser);
        mapdata.put("whoClientManager", "1");

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


    // 채팅방 목록 가져오는 코드
    private void writeRecycler2(String response){
        try {
            //getting the whole json object from the response
            JSONObject obj = new JSONObject(response);
            Log.e(TAG, "=== response ===" +response);

            if(obj.optString("status").equals("true")){

                JSONArray dataArray  = obj.getJSONArray("data");

                //currentUserChatRoomArrayList = new ArrayList<>(); //ArrayList<HashMap<String, String>>
                //chatRoomDTOArrayList.clear();

                for (int i = 0; i < dataArray.length(); i++) {
                    JSONObject dataobj = dataArray.getJSONObject(i);

                    ChatRoomDTO chatRoomDTO = new ChatRoomDTO(); //리사이클러뷰의 방한개 아이템


                    String memberList, member;
                    memberList = dataobj.getString("memberList");
                    member = memberList.replace(GlobalApplication.currentUser, ""); //현재 사용자 제거하고
                    member = member.replace("/", "");// 문자/ 제거하면, 상대방만 남음
                    Log.d(TAG, "=== member 상대방 맞는지 확인하기 ===" +member);


                    chatRoomDTO.setCurrentUser(GlobalApplication.currentUser);
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
                Log.e(TAG, "=== message 현재 사용자의 이메일을 보냈을 때, 참여하고 있는 채팅방이 하나도 없는 경우 임. ===" + obj.optString("message"));

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
        Log.d(TAG, "=== postForLastMessage(ChatRoomDTO chatRoomDTO) 메서드 몇 번 호출되는지 확인해야 함 ===" );

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
        mapdata.put("currentUser", chatRoomDTO.getCurrentUser()); //현재 이용자 이메일. 고객
        mapdata.put("whomSent1", chatRoomDTO.getWhomSent1()); //채팅 상대방 이메일. 매니저
        mapdata.put("roomId", chatRoomDTO.getRoomId()); //채팅방id
        mapdata.put("whoClientManager", "1");

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

                                    arrayList = PreferenceManager_LastMessage.getStringArrayPref(getActivity(),chatRoomDTO.getWhomSent1());
                                    arrayList2 = Preference_User_Info.getStringArrayPref(getActivity(), chatRoomDTO.getWhomSent1());

                                    ChatRoomDTO chatRoomDTO2 = new ChatRoomDTO();

                                    chatRoomDTO2.setCurrentUser(chatRoomDTO.getCurrentUser());
                                    chatRoomDTO2.setWhomSent1(chatRoomDTO.getWhomSent1());
                                    chatRoomDTO2.setWhomSent2(arrayList2.get(1)); //대화 상대방 매니저 이름
                                    chatRoomDTO2.setWhomSent3(arrayList2.get(2)); //대화 상대방 프로필
                                    // TODO: 2021-01-22 매니저로 옮길때 빼야 함
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
                                        Log.d(TAG, "=== 마지막메세지 리스트가 있는 경우   어레이 크기 :  ===" +arrayList.size());

                                        //채팅방번호랑, 시간, 내용이 같아야 함.
                                        if(chatRoomDTO2.getLastMessage().equals(arrayList.get(2)) //메세지 내용
                                                && chatRoomDTO2.getWhenSend().equals(arrayList.get(1)) //시간
                                                && chatRoomDTO2.getRoomId().equals(arrayList.get(0))) //채팅방번호
                                        {// 1. 같으면 === > 채팅방 번호, 시간, 내용이 현재 쉐어드에 저장되어 있는 마지막 메세지와 같음
                                            chatRoomDTO2.setUnreadNum(0);

                                            chatRoomDTOArrayList.add(chatRoomDTO2);

                                            /* 아래 코드에서 채팅방 객체가 모두 준비되면, 쓸 코드임 위에 add 코드 까지 해서...ㅎㅎ */
                                            Collections.sort(chatRoomDTOArrayList);
                                            adapter.notifyDataSetChanged();

                                            int roomId = Integer.parseInt(chatRoomDTO2.getRoomId());
                                            chatRoomHashMap.put(roomId, chatRoomDTO2);
                                            Log.d(TAG, "=== chatRoomHashMap 에 객체 넣음 ===" +roomId );
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
                                        Log.d(TAG, "=== 마지막메세지 리스트가 전혀 없는 경우 즉, 방이 새로 생긴 경우");

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

        arrayList = PreferenceManager_LastMessage.getStringArrayPref(getActivity(),chatRoomDTO.getWhomSent1());
        arrayList2 = Preference_User_Info.getStringArrayPref(getActivity(), chatRoomDTO.getWhomSent1());



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
            Log.d(TAG, "=== arrayList.size() 0보다 큰 경우 -> 이미 기존에 방이 있는 경우 ===" );
            mapdata.put("whenSend", arrayList.get(1)); // ** 쉐어드에 저장된 채팅 보낸 시간 -> 오전 03시 48분
            mapdata.put("lastMessage", arrayList.get(2)); // ** 쉐어드에 저장된 채팅 보낸 시간 -> 오전 03시 48분
        }else{
            Log.d(TAG, "=== arrayList.size() 0인 경우 -> 방이 새로 만들어진 경우. 저장되어 있는 마지막 메세지가 없음 ===" );
            mapdata.put("whenSend", "blank"); // ** 쉐어드에 저장된 채팅 보낸 시간 -> 오전 03시 48분
            mapdata.put("lastMessage", "blank"); // ** 쉐어드에 저장된 채팅 보낸 시간 -> 오전 03시 48분
        }

       mapdata.put("id", Integer.toString(id)); //채팅 id 가져오기

        mapdata.put("whoClientManager", "1");

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

                                chatRoomDTO.setUnreadNum(dataArray.length());
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
                        adapter.notifyDataSetChanged();
                    }
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

        //있으면 넘어감
        Intent intent = new Intent(getActivity(), Chat_Room_Activity.class);

        intent.putExtra("whomSent1", chatRoomDTOArrayList.get(position).getWhomSent1()); //고객의 이메일
        intent.putExtra("whomSent2", chatRoomDTOArrayList.get(position).getWhomSent2()); //고객의 지역/ 이름 화성시 박용현
        intent.putExtra("whomSent3", chatRoomDTOArrayList.get(position).getWhomSent3()); //고객의 지역/ 이름 화성시 박용현

        intent.putExtra("whoSend", GlobalApplication.currentUser); //나. 매니저의 이메일

        startActivity(intent);

    }
}





