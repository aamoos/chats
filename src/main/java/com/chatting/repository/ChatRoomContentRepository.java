package com.chatting.repository;

import com.chatting.entity.ChatRoomContent;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatRoomContentRepository extends CrudRepository<ChatRoomContent, Long> {

    @Query(value =
            "SELECT \n"+
                    "T1.CONTENT_IDX\n"+
                    ",T1.CHAT_CONTENTS\n"+
                    ",T1.CHAT_ROOM_IDX\n"+
                    ",(SELECT T2.ROOM_NAME FROM CHAT_ROOM T2 WHERE T2.CHAT_ROOM_IDX = T1.CHAT_ROOM_IDX) AS CHAT_ROOM_NAME\n"+
                    ",T1.CHAT_USER_ID\n"+
                    ",T1.FILE_IDXS\n"+
                    ",(SELECT PROFILE_IDX FROM USERS T2 WHERE T2.USER_ID = T1.CHAT_USER_ID) AS CHAT_PROFILE_IDX\n"+
                    ",T1.REG_DATE\n"+
                    ",T1.CHAT_NICK_NAME\n"+
                    "FROM \n"+
                    "chat_room_content T1\n"+
                    "WHERE\n"+
                    "T1.CHAT_ROOM_IDX = :chatRoomIdx\n"+
                    "ORDER BY T1.CONTENT_IDX ASC"
            , nativeQuery = true
    )
    List<ChatRoomContent> findAllByChatRoomIdx(Long chatRoomIdx);

    @Query(value =
            "SELECT \n"+
                    "T1.CONTENT_IDX\n"+
                    ",(SELECT T4.CHAT_CONTENTS FROM CHAT_ROOM_CONTENT T4 WHERE T4.CHAT_ROOM_IDX = T1.CHAT_ROOM_IDX ORDER BY T4.CONTENT_IDX DESC LIMIT 1) AS CHAT_CONTENTS\n"+
                    ",T1.CHAT_ROOM_IDX\n"+
                    ",T1.CHAT_USER_ID\n"+
                    ",T1.FILE_IDXS\n"+
                    ",(SELECT PROFILE_IDX FROM USERS T2 WHERE T2.USER_ID = T1.CHAT_USER_ID) AS CHAT_PROFILE_IDX\n"+
                    ",T2.ROOM_NAME AS CHAT_ROOM_NAME\n"+
                    ",(SELECT T4.REG_DATE FROM CHAT_ROOM_CONTENT T4 WHERE T4.CHAT_ROOM_IDX = T1.CHAT_ROOM_IDX ORDER BY T4.CONTENT_IDX DESC LIMIT 1) AS REG_DATE\n"+
                    ",(SELECT T5.ROOM_NAME FROM CHAT_ROOM T5 WHERE T5.CHAT_ROOM_IDX = T1.CHAT_ROOM_IDX ) AS CHAT_NICK_NAME\n"+
                    "FROM \n"+
                    "chat_room_content T1\n"+
                    ",chat_room T2\n"+
                    ",users T3\n"+
                    "WHERE\n"+
                    "T1.CHAT_USER_ID = :chatUserId\n"+
                    "AND T1.CHAT_ROOM_IDX = T2.CHAT_ROOM_IDX\n"+
                    "AND T2.ROOM_NAME LIKE CONCAT('%',:roomName,'%')\n"+
                    "AND T3.USER_ID = T1.CHAT_USER_ID\n\n"+
                    "ORDER BY T1.CONTENT_IDX DESC\n"
            , nativeQuery = true
    )
    List<ChatRoomContent> selectChattingRoom(String chatUserId, String roomName);
}