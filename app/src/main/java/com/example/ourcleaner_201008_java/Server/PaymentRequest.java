package com.example.ourcleaner_201008_java.Server;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class PaymentRequest extends StringRequest {

    /* 예약 정보 상세보기 리퀘스트 클래스
    * 회원, 매니저 둘 모두 볼 때, 사용함
    * */

    //서버 URL 설정(php 파일 연동)
    final static private String URL = "http://52.79.179.66/server_php-master/test/CardBillingSubscribe.php";
    private Map<String, String> map;

    public PaymentRequest(String order_id, String biling_key , String price , Response.Listener<String> listener) {
        super(Method.POST, URL, listener, null);

        map = new HashMap<>();
        map.put("order_id", order_id);
        map.put("biling_key", biling_key);
        map.put("price", price);
    }

    @Override
    protected Map<String, String>getParams() throws AuthFailureError {
        return map;
    }
}
