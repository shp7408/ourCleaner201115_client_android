package com.example.ourcleaner_201008_java.Interface;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;

/* 근처 매니저의 정보 불러오는 인터페이스
* POST */

public interface ManagerSelectInterface {

    String JSONURL = "http://52.79.179.66/";
    @GET("managerSelect.php")
    Call<String>  getNearManager(@QueryMap Map<String, String> options);

}
