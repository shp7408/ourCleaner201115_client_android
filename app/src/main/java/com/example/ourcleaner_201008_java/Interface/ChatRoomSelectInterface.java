package com.example.ourcleaner_201008_java.Interface;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;

/* 채팅방 목록 가져오기위함
* GET */

public interface ChatRoomSelectInterface {

    String JSONURL = "http://52.79.179.66/";
    @GET("chatRoomSelect.php")
    Call<String>  getChatRoomSelect(@QueryMap Map<String, String> options);

}
