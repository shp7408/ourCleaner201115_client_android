package com.example.ourcleaner_201008_java.Interface;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;

/* 안읽은 메세지 개수 가져오기 위함
* GET */

public interface ChatRoomUnreadMessageSelectInterface {

    String JSONURL = "http://52.79.179.66/";
    @GET("chatRoomUnreadMessageSelect.php")
    Call<String>  getUnreadMessageSelect(@QueryMap Map<String, String> options);

}
