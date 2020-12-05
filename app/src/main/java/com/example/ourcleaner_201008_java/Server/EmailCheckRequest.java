package com.example.ourcleaner_201008_java.Server;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class EmailCheckRequest extends StringRequest {
/*
* 매니저용 회원가입 시, 이메일 중복 확인하는 클래스
* */
    //서버 URL 설정(php 파일 연동)
    final static private String URL = "http://52.79.179.66/ManagerEmailCheck.php";
    private Map<String, String> map;
    //private Map<String, String>parameters;

    public EmailCheckRequest(String UserEmail, Response.Listener<String> listener) {
        super(Method.POST, URL, listener, null);

        map = new HashMap<>();
        map.put("UserEmail", UserEmail);
    }

    @Override
    protected Map<String, String>getParams() throws AuthFailureError {
        return map;
    }
}
