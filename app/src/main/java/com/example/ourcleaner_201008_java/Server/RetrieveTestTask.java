package com.example.ourcleaner_201008_java.Server;

import android.os.AsyncTask;
import android.util.Log;

public class RetrieveTestTask extends AsyncTask<String, Void, CoolsmsUnitTest> {

    private Exception exception;

    protected CoolsmsUnitTest doInBackground(String... urls) {
        try {
            CoolsmsUnitTest coolsmsUnitTest = new CoolsmsUnitTest();
            coolsmsUnitTest.MessageTest();
            Log.e("dddddddddddddddd", "=== MessageTest ===" );
        } catch (Exception e) {
            this.exception = e;

            return null;
        }
        return null;
    }


    protected void onPostExecute(CoolsmsUnitTest test) {
        // TODO: check this.exception
        // TODO: do something with the test
    }
}