package com.example.ourcleaner_201008_java.Interface;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/* 이메일을 보내서 토큰 값 받아오는 인터페이스
* POST */

public interface TokenSelectInterface {

    String JSONURL = "http://52.79.179.66/fcm/";
    @FormUrlEncoded
    @POST("selectToken.php")

//    Call<String> putManagerProfile( @QueryMap Map<String, String> options);

    Call<String> selectToken(

            @Field("Email") String email,
            @Field("whichClientManager") int whichClientManager

    );
}
