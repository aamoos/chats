package com.chatting.notification;

import com.chatting.entity.Push;
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

    public static String PeriodicNotificationJson(Push push) throws Exception {
        LocalDate localDate = LocalDate.now();

        //String sampleData[] = {"device token value 1","device token value 2","device token value 3"};
        String token = push.getToken();

        JSONObject body = new JSONObject();

        JSONArray array = new JSONArray();
        array.put(token);

        body.put("registration_ids", array);

        JSONObject notification = new JSONObject();

        notification.put("title", URLEncoder.encode(push.getTitle(), "UTF-8"));
        notification.put("body", URLEncoder.encode(push.getBody(), "UTF-8"));
        body.put("notification", notification);

        System.out.println(body.toString());

        return body.toString();
    }
}
