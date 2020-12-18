package com.example.ourcleaner_201008_java.Interface;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/* 매니저가 받은 알람 있는지 확인하는 인터페이스
* POST */

public interface AlarmManagerSelectInterface {

    String JSONURL = "http://52.79.179.66/";
    @FormUrlEncoded
    @POST("alarmManager.php")

//    Call<String> putManagerProfile( @QueryMap Map<String, String> options);

    Call<String> selectAlarmManager(

            @Field("Email") String email
    );
}
