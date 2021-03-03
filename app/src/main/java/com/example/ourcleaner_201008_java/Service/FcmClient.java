package com.example.ourcleaner_201008_java.Service;

import com.google.gson.JsonObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class FcmClient {

    //구글 인증 서버키 -> 서버 키 변경 함20201228
    private final String AUTH_KEY_FCM = "AAAAyzEIzdE:APA91bEJRMmOb-Z_zvraHqydEEEHQrrPqr30XDnPzVDoZJytnzkBmvJEyHLpUdPtMvVG7ZoRDadN49y4x-2uWeqpVsfG5Fgw-SFUiHuNnOEzNnXP9q3zNS3bK1hgVbcacOCEBoF1WwuS";
    private final String API_URL_FCM = "https://fcm.googleapis.com/fcm/send";

    //기기별 앱 토큰
    private String userDeviceIdKey = "";

    private HttpURLConnection conn;
    private OutputStreamWriter wr;
    private BufferedReader br;
    private URL url;

    private static final String TAG = "FcmPushTest";

    public FcmClient(){
    }

    /**
     * 구글서버로 푸시 request
     * pushFCMNotification
     * @throws Exception
     */
    public void pushFCMNotification(String msgBody,int whichClientManager, String whoEmailSent) throws Exception {
        /*
            기본적인 페이로드 전송시
            { "data": {
                "score": "5x1",
                "time": "15:10"
              },
              "to" : "bk3RNwTe3H0:CI2k_HHwgIpoDKCIZvvDMExUdFQ3P1..."
            }

            선택적인 필드의 조합시
            { "collapse_key": "score_update",
              "time_to_live": 108,
              "data": {
                "score": "4x8",
                "time": "15:16.2342"
              },
              "to" : "bk3RNwTe3H0:CI2k_HHwgIpoDKCIZvvDMExUdFQ3P1..."
            }
        */




        //if(pushKeyAuth()){
        url = new URL(API_URL_FCM);
        conn = (HttpURLConnection) url.openConnection();
        conn.setUseCaches(false);
        conn.setDoInput(true);
        conn.setDoOutput(true);
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Authorization", "key=" + AUTH_KEY_FCM);
        conn.setRequestProperty("Content-Type", "application/json");
        //일반 텍스트 전달시 Content-Type , application/x-www-form-urlencoded;charset=UTF-8

        //알림 + 데이터 메세지 형태의 전달
        JsonObject json = new JsonObject();
        JsonObject info = new JsonObject();
        JsonObject dataJson = new JsonObject();

        //1. 앱 백그라운드 발송 - 앱이 안띄워져 있는 경우임 -> 여기 클릭하면, 앱이 실행 됨
        info.addProperty("title", "우리집 청소 알람");
        info.addProperty("body", msgBody); // Notification body
        info.addProperty("sound", "default");
        info.addProperty("click_action", "OPEN_ACTIVITY_CLIENT");

        //noti 알림 부분
        json.add("notification", info);

        //디바이스전송 (앱단에서 생성된 토큰키)
        json.addProperty("to", whoEmailSent); // deviceID
        //json.addProperty("to", "/topics/" + topicsKey);

        //여러주제 전송
        //"condition": "'dogs' in topics || 'cats' in topics",




        //데이터 페이로드
        //푸시수신후 다음 로직 처리를 위한 데이터
        /*type1 의 경우*/
        //dataJson.addProperty("type", "aqua");
        //dataJson.addProperty("message", "아쿠아필드 순번대기 메세지");
        //dataJson.addProperty("bcn_cd", "02");

        // 2. 앱 포그라운드 -
        dataJson.addProperty("type", "PK");
        dataJson.addProperty("title", "우리집 청소 알람");
        dataJson.addProperty("message", msgBody);
        dataJson.addProperty("bcn_cd", "02");

        /* 여기는 뭐하는 건지 모르겠음.. 안됨..ㅎㅎㅎ */
        JsonObject pushData = new JsonObject();
        pushData.addProperty("pushTitle", "");
        pushData.addProperty("pushImageUrl", "");
        pushData.addProperty("pushLinkYn", "");
        pushData.addProperty("pushLinkType", "");
        pushData.addProperty("pushLinkSeq", "");
        pushData.addProperty("bcnCd", "");
        pushData.addProperty("bcnNm", "");
        pushData.addProperty("pushPopTitle1", "");
        pushData.addProperty("pushPopTitle2", "");
        pushData.addProperty("pushSeq", "");

        dataJson.add("pushData", pushData);
        /*type2 의 경우---------------------------------*/

        json.add("data", dataJson);

        try{
            wr = new OutputStreamWriter(conn.getOutputStream(), "UTF-8");
            wr.write(json.toString());
            wr.flush();

        }catch(Exception e){
            connFinish();
            throw new Exception("OutputStreamException : " + e);
        }

        if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
            //400, 401, 500 등
            connFinish();
            throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
        }else{
            br = new BufferedReader(new InputStreamReader((conn.getInputStream())));

            String output;
            System.out.println("Output from Server .... \n");
            while ((output = br.readLine()) != null) {
                System.out.println(output);
            }
            //200 + error 는 재전송 등등의 로직
        }
        //}
    }


    /**
     * 네트웍크관련 finalize
     * connFinish
     */
    private void connFinish(){
        if(br != null){
            try {
                br.close();
                br = null;
            } catch (IOException e) {
            }
        }
        if(wr != null){
            try {
                wr.close();
                wr = null;
            } catch (IOException e) {
            }

        }
        if(conn != null){
            conn.disconnect();
            conn = null;
        }
    }

}


