package com.chatting.notification;

import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;
import com.chatting.interceptor.HeaderRequestInterceptor;
import org.springframework.http.HttpEntity;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

// firebase_server_key = firebase project > cloud messaging > server key

@Service
public class AndroidPushNotificationsService {
    private static final String firebase_server_key="AAAAiE_Wuw0:APA91bGolUBLMlHgFDVBTBymb5-6qPdoT1ArUdkeozd-h5RbfNcBXAUjtjn7herV37fR3T9GTS_62y8sahd2yfvHfDWzYPAtN2giLWdzEgq4ZrxPKyrSuKka9pLPVnvMJYZnBhCbYlyW";
    private static final String firebase_api_url="https://fcm.googleapis.com/fcm/send";

    @Async
    public CompletableFuture<String> send(HttpEntity<String> entity) {

        RestTemplate restTemplate = new RestTemplate();

        ArrayList<ClientHttpRequestInterceptor> interceptors = new ArrayList<>();

        interceptors.add(new HeaderRequestInterceptor("Authorization",  "key=" + firebase_server_key));
        interceptors.add(new HeaderRequestInterceptor("Content-Type", "application/json; UTF-8 "));
        restTemplate.setInterceptors(interceptors);

        String firebaseResponse = restTemplate.postForObject(firebase_api_url, entity, String.class);

        return CompletableFuture.completedFuture(firebaseResponse);
    }
}