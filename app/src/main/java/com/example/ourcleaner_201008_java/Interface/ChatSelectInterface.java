package com.example.ourcleaner_201008_java.Interface;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/* 채팅내용 조회 인터페이스
* POST */

public interface ChatSelectInterface {

    String JSONURL = "http://52.79.179.66/";
    @FormUrlEncoded
    @POST("ChatSelect.php")

//    Call<String> putManagerProfile( @QueryMap Map<String, String> options);

    Call<String>  chatSelect(
            @Field("managerEmail") String managerEmail,
            @Field("clientEmail") String clientEmail
    );

}
