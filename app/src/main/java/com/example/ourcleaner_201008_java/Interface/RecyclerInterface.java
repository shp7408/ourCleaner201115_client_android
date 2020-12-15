package com.example.ourcleaner_201008_java.Interface;

/* 레트로핏-리사이클러 뷰 테스트용 : 서버에서 데이터 받아오기 위한 코드 */
import retrofit2.Call;
import retrofit2.http.GET;

public interface RecyclerInterface {

    String JSONURL = "https://demonuts.com/Demonuts/JsonTest/Tennis/";

    @GET("json_parsing.php")
    Call<String> getString();
}
