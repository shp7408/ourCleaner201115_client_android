package com.example.ourcleaner_201008_java.Server;


import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class InsertData extends AsyncTask<String, Void, String> {

    private static final String TAG = "인설트데이터";

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        Log.d(TAG, "=== InsertData . onPreExecute 메서드 실행 ===" );
    }


    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        Log.d(TAG, "=== InsertData . onPostExecute 메서드 실행 ===" );
    }


    @Override
    protected String doInBackground(String... params) {
        Log.d(TAG, "=== InsertData . doInBackground 메서드 실행 ===" );

        // 1. PHP 파일을 실행시킬 수 있는 주소와 전송할 데이터를 준비합니다.
        // POST 방식으로 데이터 전달시에는 데이터가 주소에 직접 입력되지 않습니다.
        String email = (String)params[1];
        String nickname = (String)params[2];
        String phoneNum = (String)params[3];
        Log.d(TAG, "email : " + email );
        Log.d(TAG, "nickname : " + nickname );
        Log.d(TAG, "phoneNum : " + phoneNum );

        String serverURL = (String)params[0];

        Log.d(TAG, "serverURL : " + serverURL );
        Log.d(TAG, "(String)params[0] : " + (String)params[0] );

        // HTTP 메시지 본문에 포함되어 전송되기 때문에 따로 데이터를 준비해야 합니다.
        // 전송할 데이터는 “이름=값” 형식이며 여러 개를 보내야 할 경우에는 항목 사이에 &를 추가합니다.
        // 여기에 적어준 이름을 나중에 PHP에서 사용하여 값을 얻게 됩니다.
        Log.d(TAG, "데이터를 준비한 HTTP 메시지 본문 부분. 데이터는 “이름=값” 형식이며, 여러개인 경우에는 항목 사이에 &" );
        String postParameters = "email=" + email + "&nickname=" + nickname + "&phoneNum=" + phoneNum;
        Log.d(TAG, "postParameters : " + postParameters );


        try {

            Log.d(TAG, "=== try ===" );

            // 2. HttpURLConnection 클래스를 사용하여 POST 방식으로 데이터를 전송합니다.
            URL url = new URL(serverURL); // 주소가 저장된 변수를 이곳에 입력합니다.
            Log.d(TAG, "url : " + url );
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

            httpURLConnection.setReadTimeout(5000);  //5초안에 응답이 오지 않으면 예외가 발생합니다.
            httpURLConnection.setConnectTimeout(5000); //5초안에 연결이 안되면 예외가 발생합니다.
            httpURLConnection.setRequestMethod("POST"); //요청 방식을 POST로 합니다.
            httpURLConnection.connect();


            OutputStream outputStream = httpURLConnection.getOutputStream();
            Log.d(TAG, "outputStream : ");

            outputStream.write(postParameters.getBytes("UTF-8")); //전송할 데이터가 저장된 변수를 이곳에 입력합니다. 인코딩을 고려해줘야 합니다.
            Log.d(TAG, "outputStream.write ");

            outputStream.flush();
            Log.d(TAG, "outputStream flush");

            outputStream.close();
            Log.d(TAG, "outputStream.close()");

            // 3. 응답을 읽습니다.
            int responseStatusCode = httpURLConnection.getResponseCode();

            InputStream inputStream;
            Log.d(TAG, "inputStream "  );
            if(responseStatusCode == HttpURLConnection.HTTP_OK) {
                // 정상적인 응답 데이터
                Log.d(TAG, "정상적인 응답 데이터  == HttpURLConnection.HTTP_OK 코드 : "  +  HttpURLConnection.HTTP_OK);
                inputStream = httpURLConnection.getInputStream();
                Log.d(TAG, "inputStream " );
                Log.d(TAG, "httpURLConnection.getInputStream() ");
            }
            else{
                // 에러 발생
                Log.d(TAG, "에러 발생 == HttpURLConnection.HTTP_OK 에러 코드 : "  +  HttpURLConnection.HTTP_OK);
                inputStream = httpURLConnection.getErrorStream();
                Log.d(TAG, "inputStream ");
                Log.d(TAG, "httpURLConnection.getErrorStream() ");
            }

            // 4. StringBuilder를 사용하여 수신되는 데이터를 저장합니다.
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
            Log.d(TAG, "inputStreamReader ");

            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            Log.d(TAG, "bufferedReader " );

            StringBuilder sb = new StringBuilder();
            Log.d(TAG, "StringBuilder sb " );

            String line = null;
            Log.d(TAG, "line ");

            while((line = bufferedReader.readLine()) != null){
                Log.d(TAG, "line : 널? while 문 시작. 버퍼리더에서 읽어온 값이 널이 아니면 반복");
                sb.append(line);
            }
            Log.d(TAG, "while 문 종료");

            bufferedReader.close();
            Log.d(TAG, "bufferedReader.close()" );

            // 5. 저장된 데이터를 스트링으로 변환하여 리턴합니다.
            return sb.toString();


        } catch (Exception e) {

            Log.d(TAG, "=== Exception e ===" );

            Log.d(TAG, "InsertData: Error ", e);

            return new String("Error: " + e.getMessage());
        }

    }
} //InsertData class 끝남

