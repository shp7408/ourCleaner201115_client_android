package com.example.ourcleaner_201008_java.SharedP;

import android.content.Context;
import android.content.SharedPreferences;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

public class Preference_User_Info {

    public static final String PREFERENCES_NAME = "User_Info";
    private static final String TAG = "회원정보 저장 쉐어드";
    private static final String DEFAULT_VALUE_STRING = "";


    private static SharedPreferences getPreferences(Context context) {
        return context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
    }


    /**
     * 리스트 저장
     * @param context
     * @param key
     */

    public static void setStringArrayPref(Context context, String key, ArrayList<String> values) {

        SharedPreferences prefs = getPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        JSONArray array = new JSONArray();

        for (int i =0  ; i < values.size() ; i++) {
            array.put(values.get(i));
        }

        if(!values.isEmpty()){
            editor.putString(key, array.toString());
        }
        else {
            editor.putString(key,null);
        }


        editor.commit();
        //commit apply 동기 비동기 확인할 것

    }

    /**
     * 리스트 가져오기
     * @param context
     * @param key
     */

    public static ArrayList<String> getStringArrayPref(Context context, String key) {

        SharedPreferences prefs = getPreferences(context);

        String json = prefs.getString(key, null);

        ArrayList<String> urls = new ArrayList<String>();
        if (json != null) {
            try {
                JSONArray a = new JSONArray(json);
                for (int i = 0; i < a.length(); i++) {
                    String url = a.optString(i);
                    urls.add(url);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return urls;
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

