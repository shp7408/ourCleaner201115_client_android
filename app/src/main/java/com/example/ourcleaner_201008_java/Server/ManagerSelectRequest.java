package com.example.ourcleaner_201008_java.Server;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;


public class ManagerSelectRequest extends StringRequest {

    /* 매니저 정보 열람을 위한 클래스
    * */

    //서버 URL 설정(php 파일 연동)
    final static private String URL = "http://52.79.179.66/selectManager.php";
    private Map<String, String> map;

    public ManagerSelectRequest(String managerEmail, Response.Listener<String> listener) {
        super(Method.POST, URL, listener, null);

        map = new HashMap<>();
        map.put("managerEmail", managerEmail);

    }

    @Override
    protected Map<String, String>getParams() throws AuthFailureError {
        return map;
    }
}
