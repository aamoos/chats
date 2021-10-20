package com.chatting.controller;

import java.io.UnsupportedEncodingException;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import com.chatting.notification.AndroidPushNotificationsService;
import com.chatting.notification.AndroidPushPeriodicNotifications;
import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@RestController
public class NotificationController {

    Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    AndroidPushNotificationsService androidPushNotificationsService;

    @PostMapping(value = "/pushSend")
    public @ResponseBody ResponseEntity<String> send(@RequestBody Map<String, Object> params) throws Exception {

        System.out.println("title : " + params.get("title"));
        System.out.println("body : " + params.get("body"));
        System.out.println("token : " + params.get("token"));

        String notifications = AndroidPushPeriodicNotifications.PeriodicNotificationJson(params);

        HttpEntity<String> request = new HttpEntity<>(notifications);

        CompletableFuture<String> pushNotification = androidPushNotificationsService.send(request);
        CompletableFuture.allOf(pushNotification).join();

        try{
            String firebaseResponse = pushNotification.get();
            return new ResponseEntity<>(firebaseResponse, HttpStatus.OK);
        }
        catch (InterruptedException e){
            logger.debug("got interrupted!");
            throw new InterruptedException();
        }
        catch (ExecutionException e){
            logger.debug("execution error!");
        }

        return new ResponseEntity<>("Push Notification ERROR!", HttpStatus.BAD_REQUEST);
    }
}
