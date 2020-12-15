package com.example.ourcleaner_201008_java.Service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.example.ourcleaner_201008_java.R;
import com.example.ourcleaner_201008_java.View.MainActivity;
import com.example.ourcleaner_201008_java.View.Service_completeActivity;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = "FirebaseMsgService";

    // [START receive_message]
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        //추가한것
        sendNotification(remoteMessage);
    }

    private void sendNotification(RemoteMessage messageBody) {

        String title = messageBody.getData().get("title");
        String message = messageBody.getData().get("message");

        final String CHANNEL_ID = getString(R.string.default_notification_channel_id);

        NotificationManager mManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            final String CHANNEL_NAME = "ChannerName";
            final String CHANNEL_DESCRIPTION = "ChannerDescription";
            final int importance = NotificationManager.IMPORTANCE_HIGH;

            // add in API level 26
            NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, importance);
            mChannel.setDescription(CHANNEL_DESCRIPTION);
            mChannel.enableLights(true);
            mChannel.enableVibration(true);
            mChannel.setVibrationPattern(new long[]{100, 200, 100, 200});
            mChannel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
            mManager.createNotificationChannel(mChannel);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID);
        builder.setSmallIcon(R.drawable.ic_launcher_background);
        builder.setAutoCancel(true);
        builder.setDefaults(Notification.DEFAULT_ALL);
        builder.setWhen(System.currentTimeMillis());
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setContentTitle(title);
        builder.setContentText(message);

        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            builder.setContentTitle(title);
            builder.setVibrate(new long[]{500, 500});
        }

        mManager.notify(0, builder.build());

//        Intent intent = new Intent(this, MainActivity.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
//                PendingIntent.FLAG_ONE_SHOT);
//
//        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
//                .setSmallIcon(R.mipmap.ic_launcher)
//                .setContentTitle("FCM Push Test")
//                .setContentText(messageBody)
//                .setAutoCancel(true)
//                .setSound(defaultSoundUri)
//                .setContentIntent(pendingIntent);
//
//        NotificationManager notificationManager =
//                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//
//        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }



//    /**
//     * 구글 토큰을 얻는 값입니다.
//     * 아래 토큰은 앱이 설치된 디바이스에 대한 고유값으로 푸시를 보낼때 사용됩니다.
//     * **/
//
//    public void onNewToken() {
//        super.onNewToken(s);
//        Log.e(TAG, "FirebaseInstanceIDService : " + s);
//
//        /* 이거 토큰 맞는지 확인해야 함.. */
//        sendRegistrationToServer(s);
//    }

//    private void sendRegistrationToServer(String token) {
//        // Add custom implementation, as needed.
//
//        OkHttpClient client = new OkHttpClient();
//        RequestBody body = new FormBody.Builder()
//                .add("Token", token)
//                .build();
//
//        //request
//        Request request = new Request.Builder()
//                .url("http://52.79.179.66/fcm/register.php")
//                .post(body)
//                .build();
//
//        try {
//            client.newCall(request).execute();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//
//
//    /**
//     * 메세지를 받았을 경우 그 메세지에 대하여 구현하는 부분입니다.
//     * **/
//    @Override
//    public void onMessageReceived(RemoteMessage remoteMessage) {
//        Log.e(TAG, "remoteMessage1 : " + remoteMessage);
//        if (remoteMessage != null && remoteMessage.getData().size() > 0) {
//            sendNotification(remoteMessage);
//            Log.e(TAG, "remoteMessage2 : " + remoteMessage);
//
//
//        }
//    }
//
//    /**
//     * remoteMessage 메세지 안애 getData와 getNotification이 있습니다.
//     * 이부분은 차후 테스트 날릴때 설명 드리겠습니다.
//     * **/
//    private void sendNotification(RemoteMessage remoteMessage) {
//
//        String title = remoteMessage.getData().get("title");
//        String message = remoteMessage.getData().get("message");
//
//        Log.e(TAG, "title : " + title);
//        Log.e(TAG, "message : " + message);
//
//        /**
//         * 오레오 버전부터는 Notification Channel이 없으면 푸시가 생성되지 않는 현상이 있습니다.
//         * **/
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            Log.e(TAG, "오레오 버전 이상임");
//
//            String channel = "채널";
//            String channel_nm = "채널명";
//
//
//            NotificationManager notichannel = (android.app.NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//            NotificationChannel channelMessage = new NotificationChannel(channel, channel_nm,
//                    android.app.NotificationManager.IMPORTANCE_DEFAULT);
//            channelMessage.setDescription("채널에 대한 설명.");
//            channelMessage.enableLights(true);
//            channelMessage.enableVibration(true);
//            channelMessage.setShowBadge(false);
//            channelMessage.setVibrationPattern(new long[]{100, 200, 100, 200});
//            notichannel.createNotificationChannel(channelMessage);
//
//            NotificationCompat.Builder notificationBuilder =
//                    new NotificationCompat.Builder(this, channel)
//                            .setSmallIcon(R.drawable.ic_launcher_background)
//                            .setContentTitle(title)
//                            .setContentText(message)
//                            .setChannelId(channel)
//                            .setAutoCancel(true)
//                            .setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE);
//
//            NotificationManager notificationManager =
//                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//
//            notificationManager.notify(9999, notificationBuilder.build());
//
//        } else {
//            NotificationCompat.Builder notificationBuilder =
//                    new NotificationCompat.Builder(this, "")
//                            .setSmallIcon(R.drawable.ic_launcher_background)
//                            .setContentTitle(title)
//                            .setContentText(message)
//                            .setAutoCancel(true)
//                            .setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE);
//
//            NotificationManager notificationManager =
//                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//
//            notificationManager.notify(9999, notificationBuilder.build());
//
//        }
//    }



//    // [START receive_message]
//    @Override
//    public void onMessageReceived(RemoteMessage remoteMessage) {
//
//        //추가한것
//        sendNotification(remoteMessage.getData().get("message"));
//    }
//
//    public void sendNotification(String messageBody) {
//        Intent intent = new Intent(this, Service_completeActivity.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
//                PendingIntent.FLAG_ONE_SHOT);
//
//        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
//                .setSmallIcon(R.mipmap.ic_launcher)
//                .setContentTitle("FCM Push Test")
//                .setContentText(messageBody)
//                .setAutoCancel(true)
//                .setSound(defaultSoundUri)
//                .setContentIntent(pendingIntent);
//
//        NotificationManager notificationManager =
//                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//
//        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
//    }
}
