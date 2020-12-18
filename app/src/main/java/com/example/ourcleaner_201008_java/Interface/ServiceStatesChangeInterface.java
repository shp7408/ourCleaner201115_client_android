package com.example.ourcleaner_201008_java.Interface;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/* 해당 서비스의 상태를 변경하는 인터페이스
* POST */

public interface ServiceStatesChangeInterface {

    String JSONURL = "http://52.79.179.66/";
    @FormUrlEncoded
    @POST("serviceStateChange.php")


    Call<String> updateServiceState(

            @Field("ServiceId") int serviceId,
            @Field("State") String state

    );
}
