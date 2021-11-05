package com.chatting.repository;

import com.chatting.entity.ChatRoomContent;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface ChatRoomContentRepository extends CrudRepository<ChatRoomContent, Long> {

    @Query(value =
            "SELECT \n"+
                    "T1.CONTENT_IDX\n"+
                    ",T1.CHAT_CONTENTS\n"+
                    ",T1.CHAT_ROOM_IDX\n"+
                    ",(SELECT T2.ROOM_NAME FROM CHAT_ROOM T2 WHERE T2.CHAT_ROOM_IDX = T1.CHAT_ROOM_IDX) AS CHAT_ROOM_NAME\n"+
                    ",T1.CHAT_USER_IDX\n"+
                    ",T1.FILE_IDXS\n"+
                    ",(SELECT GROUP_CONCAT(T3.CONTENT_FILE_URL SEPARATOR '|') FROM CHAT_ROOM_CONTENT_FILE T3 WHERE T3.CONTENT_IDX = T1.CONTENT_IDX) AS CONTENT_FILE_URL\n"+
                    ",(SELECT PROFILE_IDX FROM USERS T2 WHERE T2.USER_IDX = T1.CHAT_USER_IDX) AS CHAT_PROFILE_IDX\n"+
                    ",T1.REG_DATE\n"+
                    ",T1.CHAT_NICK_NAME\n"+
                    ",T1.CHAT_ROOM_TYPE\n"+
                    "FROM \n"+
                    "chat_room_content T1\n"+
                    "WHERE\n"+
                    "T1.CHAT_ROOM_IDX = :chatRoomIdx\n"+
                    "ORDER BY T1.CONTENT_IDX ASC"
            , nativeQuery = true
    )
    List<Map<String, Object>> findAllByChatRoomIdx(Long chatRoomIdx);

    @Query(value =
            "SELECT  \n" +
                    "  T1.CHAT_DETAIL_IDX\n" +
                    " ,T1.CHAT_ROOM_IDX\n" +
                    " ,T1.USER_IDX\n" +
                    ", T2.ROOM_NAME AS CHAT_ROOM_NAME\n" +
                    ", (SELECT T3.CHAT_CONTENTS FROM CHAT_ROOM_CONTENT T3 WHERE T3.CHAT_ROOM_IDX = T1.CHAT_ROOM_IDX ORDER BY CONTENT_IDX DESC LIMIT 1 ) AS CHAT_CONTENTS\n" +
                    ", (SELECT T3.REG_DATE FROM CHAT_ROOM_CONTENT T3 WHERE T3.CHAT_ROOM_IDX = T1.CHAT_ROOM_IDX ORDER BY CONTENT_IDX DESC LIMIT 1 ) AS REG_DATE\n" +
                    ", (SELECT T4.PROFILE_IDX FROM USERS T4 WHERE T4.USER_IDX = ((SELECT T5.USER_IDX FROM CHAT_ROOM_DETAIL T5 WHERE T5.CHAT_ROOM_IDX = T1.CHAT_ROOM_IDX AND T5.USER_IDX != :chatUserIdx)) ) AS CHAT_PROFILE_IDX\n"+
                    "FROM  \n" +
                    " CHAT_ROOM_DETAIL T1\n" +
                    ",CHAT_ROOM T2\n" +
                    "WHERE \n" +
                    "T1.USER_IDX = :chatUserIdx\n" +
                    "AND\n" +
                    "T1.CHAT_ROOM_IDX = T2.CHAT_ROOM_IDX\n"+
                    "AND\n" +
                    "T2.ROOM_NAME LIKE CONCAT('%',:roomName,'%')"
            , nativeQuery = true
    )
    List<Map<String, Object>> selectChattingRoom(Long chatUserIdx, String roomName);

    ChatRoomContent findByRegDateContainingAndChatRoomType(String regDate, String chatRoomType);
}