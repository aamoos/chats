package com.chatting.notification;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AndroidPushPeriodicNotifications {

    public static String PeriodicNotificationJson(Map<String, Object> params) throws Exception {
        LocalDate localDate = LocalDate.now();

        //String sampleData[] = {"device token value 1","device token value 2","device token value 3"};
        String token = (String) params.get("token");
        String sampleData[] = token.split(",");

        JSONObject body = new JSONObject();

        List<String> tokenlist = new ArrayList<String>();

        for(int i=0; i<sampleData.length; i++){
            tokenlist.add(sampleData[i]);
        }

        JSONArray array = new JSONArray();

        for(int i=0; i<tokenlist.size(); i++) {
            array.put(tokenlist.get(i));
        }

        body.put("registration_ids", array);

        JSONObject notification = new JSONObject();

        notification.put("title", URLEncoder.encode((String) params.get("title"), "UTF-8"));
        notification.put("body", URLEncoder.encode((String) params.get("body"), "UTF-8"));

        body.put("notification", notification);

        System.out.println(body.toString());

        return body.toString();
    }
}
