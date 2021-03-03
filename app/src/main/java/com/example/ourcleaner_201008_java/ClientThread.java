package com.example.ourcleaner_201008_java;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.example.ourcleaner_201008_java.DTO.ChatDTO;

import org.json.simple.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class ClientThread extends Thread{

    BufferedReader bufferR;
    BufferedWriter bufferW;
    Socket client;
    Handler handler;

    ChatDTO chatDTO;

    private static final String TAG = "ClientThread";

    public ClientThread(Socket client, Handler handler) {

        this.handler = handler;

        try {
            Log.d(TAG, "=== ClientThread ===" );
            this.client = client;
            //연결된 소켓으로부터 대화를 나눌 스트림을 얻음
            bufferR = new BufferedReader(new InputStreamReader(client.getInputStream(),"euc-kr"));
            bufferW = new BufferedWriter(new OutputStreamWriter(client.getOutputStream(),"euc-kr"));

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /* JSONObject 로 메세지 보내기 */
    public void send(JSONObject data){

        Log.d(TAG, "=== send 전송 ===" );
        try {
            bufferW.write(data+"\n");
            bufferW.flush();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }


    /* JSONObject 로 메세지 받기 */
    public String listen(){
        Log.d(TAG, "=== listen 받기 ===" );
        String msg=null;

        try {

            while(true){
                msg=bufferR.readLine();
                Log.d(TAG, "=== listen === msg : " + msg );
                Message m = new Message();
                Log.d(TAG, "=== listen === m : " + m );
                Bundle bundle = new Bundle();
                bundle.putString("msg", msg);
                m.setData(bundle);
                handler.sendMessage(m);
                Log.d(TAG, "=== listen listen ===");
            }

        } catch (UnknownHostException uhe) {
            // 소켓 생성 시 전달되는 호스트(www.unknown-host.com)의 IP를 식별할 수 없음.
            Log.e(TAG, " 생성 Error : 호스트의 IP 주소를 식별할 수 없음. (잘못된 주소 값 또는 호스트 이름 사용)");

        } catch (IOException ioe) {
            // 소켓 생성 과정에서 I/O 에러 발생.
            Log.e(TAG, " 생성 Error : 네트워크 응답 없음");

        } catch (SecurityException se) {
            // security manager에서 허용되지 않은 기능 수행.
            Log.e(TAG, " 생성 Error : 보안(Security) 위반에 대해 보안 관리자(Security Manager)에 의해 발생. (프록시(proxy) 접속 거부, 허용되지 않은 함수 호출)");

        } catch (IllegalArgumentException le) {
            // 소켓 생성 시 전달되는 포트 번호(65536)이 허용 범위(0~65535)를 벗어남.

            Log.e(TAG, " 생성 Error : 메서드에 잘못된 파라미터가 전달되는 경우 발생. (0~65535 범위 밖의 포트 번호 사용, null 프록시(proxy) 전달)");

        }
        return msg;

    }

    public void run() {
        super.run();
        listen();

    }
}

