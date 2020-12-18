package com.example.ourcleaner_201008_java.Interface;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/* 일반 고객 기기 토큰 저장 위한 인터페이스
* POST */

public interface TokenInsertInterface {

    String JSONURL = "http://52.79.179.66/fcm/";
    @FormUrlEncoded
    @POST("register.php")

//    Call<String> putManagerProfile( @QueryMap Map<String, String> options);

    Call<String> putTokenInsert(

            @Field("Token") String token,
            @Field("Email") String email
    );
}
