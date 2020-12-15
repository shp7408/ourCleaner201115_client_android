package com.example.ourcleaner_201008_java.Interface;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/* 법정명 주소 서버에서 받아오기 위한 인터페이스
* POST */

public interface BResearchInterface {

    String JSONURL = "http://52.79.179.66/";
    @FormUrlEncoded
    @POST("researchBAddress.php")
    Call<String>  getBAddress(
            @Field("nowAddress") String nowAddress,
            @Field("researchKeyword") String researchKeyword
    );

}
