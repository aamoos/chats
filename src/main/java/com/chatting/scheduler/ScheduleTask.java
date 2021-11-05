package com.chatting.scheduler;

import com.chatting.entity.Push;
import com.chatting.notification.AndroidPushNotificationsService;
import com.chatting.notification.AndroidPushPeriodicNotifications;
import com.chatting.repository.PushRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.codehaus.groovy.transform.SourceURIASTTransformation;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Component
@Slf4j
@RequiredArgsConstructor
public class ScheduleTask {

    private final AndroidPushNotificationsService androidPushNotificationsService;
    private final PushRepository pushRepository;

   // @Scheduled(fixedDelay = 2000)
    public void sendPush() throws Exception {

        System.out.println("푸시발송");

        List<Push> pushList = pushRepository.findAllBySendYnOrUseYn("N", "Y");

        //푸시 발송
        for(Push push : pushList){

            try{

                //발송되지 않고 사용중인 푸시 리스트 항목 조회
                Push selectPush = pushRepository.findByPushIdxAndUseYnAndSendYn(push.getPushIdx(), "Y", "N");

                if(selectPush != null){
                    String notifications = AndroidPushPeriodicNotifications.PeriodicNotificationJson(push);
                    HttpEntity<String> request = new HttpEntity<>(notifications);
                    CompletableFuture<String> pushNotification = androidPushNotificationsService.send(request);
                    CompletableFuture.allOf(pushNotification).join();
                    String firebaseResponse = pushNotification.get();
                    System.out.println("머야 : " + firebaseResponse);
                    selectPush.setSendYn("Y");
                    pushRepository.save(selectPush);
                }
            }
            catch (InterruptedException e){
                log.debug("got interrupted!");
                throw new InterruptedException();
            }
            catch (ExecutionException e){
                log.debug("execution error!");

            }
        }
    }

}
