package com.chatting.service;

import com.chatting.entity.Friends;
import com.chatting.entity.Push;
import com.chatting.repository.FriendsRepository;
import com.chatting.repository.PushRepository;
import groovy.util.logging.Slf4j;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.security.Principal;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class PushService {

    private final FriendsRepository friendsRepository;
    private final PushRepository pushRepository;

    public void savePush(Map<String, Object> params, Principal principal){
        String title = (String) params.get("title");
        String body = (String) params.get("body");

        List<Friends> pushList =  friendsRepository.findAllByChatRoomIdx((String) params.get("chatRoomIdx"), principal.getName());

        for(Friends friends : pushList){
            Push push = new Push();
            push.setTitle(title);
            push.setBody(body);
            push.setToken(friends.getToken());
            push.setSendYn("N");
            push.setUseYn("Y");
            pushRepository.save(push);
        }
    }

}
