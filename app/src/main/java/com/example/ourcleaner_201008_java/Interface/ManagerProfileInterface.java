package com.example.ourcleaner_201008_java.Interface;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;

/* 매니저 프로필 업데이트 위한 인터페이스
* POST */

public interface ManagerProfileInterface {

    String JSONURL = "http://52.79.179.66/";
    @FormUrlEncoded
    @POST("managerprofileEditSelect.php")

//    Call<String> putManagerProfile( @QueryMap Map<String, String> options);

    Call<String>  putManagerProfile(

            @Field("idStr") String idStr,
            @Field("addressStr") String addressStr,
            @Field("desiredWorkAreaList") String desiredWorkAreaList,
            @Field("profileImagePathStr") String profileImagePathStr
    );
}
