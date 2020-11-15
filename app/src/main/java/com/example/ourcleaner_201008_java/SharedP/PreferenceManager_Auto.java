package com.example.ourcleaner_201008_java.SharedP;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import java.nio.ByteBuffer;
import java.security.AlgorithmParameters;
import java.security.SecureRandom;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

public class PreferenceManager_Auto {

    public static final String PREFERENCES_NAME = "AutoUser";
    private static final String TAG = "자동 로그인 쉐어드";
    private static final String DEFAULT_VALUE_STRING = "";


    private static SharedPreferences getPreferences(Context context) {

        return context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);

    }


    /**

     * 키 값 삭제

     * @param context

     * @param key

     */

    public static void removeKey(Context context, String key) {

        SharedPreferences prefs = getPreferences(context);

        SharedPreferences.Editor edit = prefs.edit();

        edit.remove(key);

        edit.commit();

    }



    /**

     * 모든 저장 데이터 삭제

     * @param context

     */

    public static void clear(Context context) {

        SharedPreferences prefs = getPreferences(context);

        SharedPreferences.Editor edit = prefs.edit();

        edit.clear();

        edit.commit();

    }




    /**

     * String 값 저장

     * @param context

     * @param key

     * @param value

     */

    public static void setString(Context context, String key, String value) {


        SharedPreferences prefs = getPreferences(context);

        SharedPreferences.Editor editor = prefs.edit();

        editor.putString(key, value);

        editor.commit();

    }




    /**

     * String 값 로드

     * @param context

     * @param key

     * @return

     */

    public static String getString(Context context, String key) {

        SharedPreferences prefs = getPreferences(context);

        String value = prefs.getString(key, DEFAULT_VALUE_STRING);

        return value;

    }






}

