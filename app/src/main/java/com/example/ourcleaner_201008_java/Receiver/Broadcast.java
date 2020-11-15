package com.example.ourcleaner_201008_java.Receiver;

import java.util.Date;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

import com.example.ourcleaner_201008_java.View.PhoneAuthActivity;

public class Broadcast extends BroadcastReceiver {

    private static final String TAG = "브로드캐스트";
    private Context mContext;

    @Override
    public void onReceive(Context context, Intent intent) {
        // 수신한 액션을 이 onReceive메소드에서 처리하게 됩니다

        Log.d(TAG, "=== onReceive ===" );

        if (intent.getAction().equals(Intent.ACTION_POWER_CONNECTED)){
            Toast.makeText(context, "전원 연결", Toast.LENGTH_SHORT).show();
            Log.d(TAG, "=== 전원 연결 ===" );
        } else if (intent.getAction().equals(Intent.ACTION_POWER_DISCONNECTED)){
            Toast.makeText(context, "전원 해제", Toast.LENGTH_SHORT).show();
            Log.d(TAG, "=== 전원 해제 ===" );
        }

        // TODO: 2020-10-17 지금 넣은 부분임.
        mContext= context;
        String action = "START";

        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            {
                context.startForegroundService(new Intent(context, PhoneAuthActivity.class));
            }
            else
            {
                context.startService(new Intent(context, PhoneAuthActivity.class));
            }
        }
        
        
        

        Log.d(TAG +"onReceive", "=== 수신한 액션을 이 onReceive메소드에서 처리하게 됩니다 ===" );

        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())){
            Log.d(TAG,"onReceive() // 부팅완료");
        }
        if (Intent.ACTION_SCREEN_ON == intent.getAction()) {
            Log.d(TAG,"onReceive()// 화면 켜짐");
        }
        if (Intent.ACTION_SCREEN_OFF == intent.getAction()) {
            Log.d(TAG,"onReceive()  // 화면 꺼짐");
        }
        if ("android.provider.Telephony.SMS_RECEIVED".equals(intent.getAction())) {
            Log.d(TAG,"onReceive() // sms 수신");

            // SMS 메시지를 파싱합니다.
            Log.d(TAG, "=== SMS 메시지를 파싱합니다. ===" );
            Bundle bundle = intent.getExtras();
            Object messages[] = (Object[])bundle.get("pdus");
            SmsMessage smsMessage[] = new SmsMessage[messages.length];

            for(int i = 0; i < messages.length; i++) {
                // PDU 포맷으로 되어 있는 메시지를 복원합니다.
                smsMessage[i] = SmsMessage.createFromPdu((byte[])messages[i]);
                Log.d(TAG, "=== PDU 포맷으로 되어 있는 메시지를 복원합니다. === i : "+ messages.length);
            }
            Log.d(TAG, "=== 반복문 종료 ===" );

            // SMS 수신 시간 확인
            Date curDate = new Date(smsMessage[0].getTimestampMillis());
            Log.d(TAG, "=== SMS 수신 시간 확인 curDate.toString() ===" +curDate.toString() );

            // SMS 발신 번호 확인
            String origNumber = smsMessage[0].getOriginatingAddress();
            Log.d(TAG, "=== SMS 발신 번호 확인  origNumber === :" +origNumber );


            // SMS 메시지 확인
            String message = smsMessage[0].getMessageBody().toString();
            Log.d(TAG, " SMS 메시지 확인 origNumber : "  +origNumber+"origNumber : " + message);

            // abortBroadcast();

        }
    }

    private boolean isServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
}

