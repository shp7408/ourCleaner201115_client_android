package com.example.ourcleaner_201008_java.Server;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class ServiceSelectRequest extends StringRequest {

    /* 예약 정보 상세보기 리퀘스트 클래스
    * 회원, 매니저 둘 모두 볼 때, 사용함
    * */

    //서버 URL 설정(php 파일 연동)
    final static private String URL = "http://52.79.179.66/serviceDetailSelect.php";
    private Map<String, String> map;

    public ServiceSelectRequest(String uid, String serviceState, String whoSelect, Response.Listener<String> listener) {
        super(Method.POST, URL, listener, null);

        map = new HashMap<>();
        map.put("uid", uid);
        map.put("serviceState", serviceState);
        map.put("whoSelect", whoSelect);

    }

    @Override
    protected Map<String, String>getParams() throws AuthFailureError {
        return map;
    }
}
