package com.chatting.service;

import com.chatting.entity.ChatRoom;
import com.chatting.entity.ChatRoomContent;
import com.chatting.repository.ChatRoomContentRepository;
import com.chatting.repository.ChatRoomRepository;
import groovy.util.logging.Slf4j;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class ChatService {

    private final ChatRoomRepository chatRoomRepository;
    private final ChatRoomContentRepository chatRoomContentRepository;

    //채팅방 만들기
    public Long saveChatRoom(ChatRoom chatRoom){
        return chatRoomRepository.save(chatRoom).getChatRoomIdx();
    }

    //채팅보내기
    public Long saveChatRoomContent(ChatRoomContent chatRoomContent){
        return chatRoomContentRepository.save(chatRoomContent).getContentIdx();
    }

}
