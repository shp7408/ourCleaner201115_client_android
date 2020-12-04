package com.example.ourcleaner_201008_java.Server;
import java.util.HashMap;
import org.json.simple.JSONObject;
import net.nurigo.java_sdk.api.Message;
import net.nurigo.java_sdk.exceptions.CoolsmsException;

/**
 * @class ExampleSend
 * @brief This sample code demonstrate how to send sms through CoolSMS Rest API PHP
 */
public class ExampleSend {

    private static String phoneNumToInt="01030517408"; //받는 사람
    private static String phoneNumFromInt="01030517408"; //보내는 사람
    private static String certNum4Int="0000"; //4자리 인증번호

    public static void main(String[] args) {
        String api_key = "NCSFULYJ0NDRNTUZ";
        String api_secret = "CVON1XJNMJB7MFO8XYATBVK2ABXTSJFK";
        Message coolsms = new Message(api_key, api_secret);

        // 4 params(to, from, type, text) are mandatory. must be filled
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("to", phoneNumToInt);
        params.put("from", phoneNumFromInt);
        params.put("type", certNum4Int);
        params.put("text", "Coolsms Testing Message!");
        params.put("app_version", "test app 1.2"); // application name and version

        try {
            JSONObject obj = (JSONObject) coolsms.send(params);
            System.out.println(obj.toString());
        } catch (CoolsmsException e) {
            System.out.println(e.getMessage());
            System.out.println(e.getCode());
        }
    }


}
