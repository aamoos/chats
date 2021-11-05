package com.chatting.service;

import com.chatting.entity.ChatRoom;
import com.chatting.entity.ChatRoomContent;
import com.chatting.entity.ChatRoomContentFile;
import com.chatting.entity.ChatRoomDetail;
import com.chatting.repository.ChatRoomContentFileRepository;
import com.chatting.repository.ChatRoomContentRepository;
import com.chatting.repository.ChatRoomDetailRepository;
import com.chatting.repository.ChatRoomRepository;
import groovy.util.logging.Slf4j;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class ChatService {

    private final ChatRoomRepository chatRoomRepository;
    private final ChatRoomContentRepository chatRoomContentRepository;
    private final ChatRoomDetailRepository chatRoomDetailRepository;
    private final ChatRoomContentFileRepository chatRoomContentFileRepository;

    //채팅방 만들기
    public Long saveChatRoom(ChatRoom chatRoom){
        return chatRoomRepository.save(chatRoom).getChatRoomIdx();
    }

    //채팅보내기
    public Long saveChatRoomContent(ChatRoomContent chatRoomContent, List<String> contentFileUrl){

        // 현재 날짜 구하기
        LocalDate now = LocalDate.now();
        // 포맷 정의

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("yyyy년 MM월 dd일");

        // 포맷 적용
        String formatedNow = now.format(formatter);
        String formatedNow2 = now.format(formatter2)+" "+getDayOfTheWeek();

        ChatRoomContent chatRoomContent1 = chatRoomContentRepository.findByRegDateContainingAndChatRoomType(formatedNow, "day");

        //날짜가 입력이 안되있으면
        if(chatRoomContent1 == null){
            ChatRoomContent chatRoomContent2 = new ChatRoomContent();
            chatRoomContent2.setChatRoomType("day");
            chatRoomContent2.setChatContents(formatedNow2);
            chatRoomContent2.setChatRoomIdx(chatRoomContent.getChatRoomIdx());
            chatRoomContentRepository.save(chatRoomContent2);
        }

        Long contentsIdx = chatRoomContentRepository.save(chatRoomContent).getContentIdx();

        //base 64 이미지 저장
        for(String baseImageUrl : contentFileUrl){
            System.out.println("baseImageUrl : " + baseImageUrl);
            System.out.println("ㄴㅁㅇㅇㄴㅁㄴㅇㅁㅇㄴㅁㄴㅇㅁ : " + baseImageUrl.getBytes(StandardCharsets.UTF_8));
            ChatRoomContentFile chatRoomContentFile = new ChatRoomContentFile();
            chatRoomContentFile.setContentIdx(contentsIdx);
            chatRoomContentFile.setContentFileUrl(baseImageUrl);
            chatRoomContentFileRepository.save(chatRoomContentFile);
        }

        return contentsIdx;
    }

    private Long saveChatRoomDetail(ChatRoomDetail chatRoomDetail){
        return chatRoomDetailRepository.save(chatRoomDetail).getChatDetailIdx();
    }

    //요일구하기
    private String getDayOfTheWeek() {
        String[] weekDay = {"일요일", "월요일", "화요일", "수요일", "목요일", "금요일", "토요일"};
        Calendar cal = Calendar.getInstance();
        int num = cal.get(Calendar.DAY_OF_WEEK) - 1;
        String today = weekDay[num];
        System.out.println(num);
        System.out.println("오늘의 요일 : " + today);
        return today;
    }
}
