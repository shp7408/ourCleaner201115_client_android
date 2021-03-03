package com.example.ourcleaner_201008_java.Interface;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/* 채팅 입력 인터페이스
* POST */

public interface ChatInsertInterface {

    String JSONURL = "http://52.79.179.66/";
    @FormUrlEncoded
    @POST("ChatInsert.php")

//    Call<String> putManagerProfile( @QueryMap Map<String, String> options);

    Call<String>  putChatInsert(

            @Field("msgStr") String msgStr,
            @Field("getDate") String getDate,
            @Field("getTime") String getTime,
            @Field("whomSent") String whomSent, //메세지 받는 사람
            @Field("whoSend") String whoSend // 메세지 보내는 사람

            );
}
