package com.example.ourcleaner_201008_java;

import android.app.Application;
import android.content.Context;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.kakao.auth.ApprovalType;
import com.kakao.auth.AuthType;
import com.kakao.auth.IApplicationConfig;
import com.kakao.auth.ISessionConfig;
import com.kakao.auth.KakaoAdapter;
import com.kakao.auth.KakaoSDK;



public class GlobalApplication extends Application {

    private static GlobalApplication instance;

    //현재 사용자 가져오기 위한 변수
    public static String currentUser;
    public static String currentManager;
    public static String currentManagerName;
    public static String currentManagerPhonNum;
    public static String currentManagerAddress;



    //volley 사용을 위한 코드
    public static final String TAG = GlobalApplication.class
            .getSimpleName();

    private RequestQueue mRequestQueue;
    private ImageLoader mImageLoader;



    public static GlobalApplication getGlobalApplicationContext() {
        if (instance == null) {
            throw new IllegalStateException("This Application does not inherit com.kakao.GlobalApplication");
        }

        return instance;
    }



    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

        // Kakao Sdk 초기화
        KakaoSDK.init(new KakaoSDKAdapter());

        //현재 사용자 초기화
        currentUser = null;
    }



    public static synchronized GlobalApplication getInstance() {
        return instance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        return mRequestQueue;
    }

//    public ImageLoader getImageLoader() {
//        getRequestQueue();
//        if (mImageLoader == null) {
//            mImageLoader = new ImageLoader(this.mRequestQueue,
//                    new LruBitmapCache());
//        }
//        return this.mImageLoader;
//    }

    public <T> void addToRequestQueue(Request<T> req, String tag) {
        // set the default tag if tag is empty
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }





    @Override
    public void onTerminate() {
        super.onTerminate();
        instance = null;
    }


    public class KakaoSDKAdapter extends KakaoAdapter {

        // 로그인 시 사용 될, Session의 옵션 설정을 위한 인터페이스
        @Override
        public ISessionConfig getSessionConfig() {
            return new ISessionConfig() {
                // 로그인 시에 인증 타입을 지정
                @Override
                public AuthType[] getAuthTypes() {
                    // Auth Type
                    // KAKAO_TALK  : 카카오톡 로그인 타입
                    // KAKAO_STORY : 카카오스토리 로그인 타입
                    // KAKAO_ACCOUNT : 웹뷰 다이얼로그를 통한 계정연결 타입
                    // KAKAO_TALK_EXCLUDE_NATIVE_LOGIN : 카카오톡 로그인 타입과 함께 계정생성을 위한 버튼을 함께 제공
                    // KAKAO_LOGIN_ALL : 모든 로그인 방식을 제공

                    return new AuthType[] {AuthType.KAKAO_ACCOUNT};
                }

                // 로그인 웹뷰에서 pause와 resume시에 타이머를 설정하여, CPU의 소모를 절약 할 지의 여부를 지정합니다.
                // true로 지정할 경우, 로그인 웹뷰의 onPuase()와 onResume()에 타이머를 설정해야 합니다.
                @Override
                public boolean isUsingWebviewTimer() {
                    return false;
                }

                // 로그인 시 토큰을 저장할 때의 암호화 여부를 지정합니다.
                @Override
                public boolean isSecureMode() {
                    return false;
                }

                // 일반 사용자가 아닌 Kakao와 제휴 된 앱에서 사용되는 값입니다.
                // 값을 지정하지 않을 경우, ApprovalType.INDIVIDUAL 값으로 사용됩니다.
                @Override
                public ApprovalType getApprovalType() {
                    return ApprovalType.INDIVIDUAL;
                }

                // 로그인 웹뷰에서 email 입력 폼의 데이터를 저장할 지 여부를 지정합니다.
                @Override
                public boolean isSaveFormData() {
                    return true;
                }

            };
        }

        // Application이 가지고 있는 정보를 얻기 위한 인터페이스
        @Override
        public IApplicationConfig getApplicationConfig() {
            return new IApplicationConfig() {
                @Override
                public Context getApplicationContext() {
                    return GlobalApplication.getGlobalApplicationContext();
                }
            };
        }
    }

    public String getCurrentUser(){
        return currentUser;
    }

    public String getCurrentManager(){
        return currentManager;
    }

    public static void setCurrentUser(String currentUser) {
        GlobalApplication.currentUser = currentUser;
    }

    public static void setCurrentManager(String currentManager) {
        GlobalApplication.currentManager = currentManager;
    }

    public static String getCurrentManagerName() {
        return currentManagerName;
    }

    public static void setCurrentManagerName(String currentManagerName) {
        GlobalApplication.currentManagerName = currentManagerName;
    }

    public static String getCurrentManagerPhonNum() {
        return currentManagerPhonNum;
    }

    public static void setCurrentManagerPhonNum(String currentManagerPhonNum) {
        GlobalApplication.currentManagerPhonNum = currentManagerPhonNum;
    }

    public static void setCurrentManagerAddress(String currentManagerAddress) {
        GlobalApplication.currentManagerAddress = currentManagerAddress;
    }
}