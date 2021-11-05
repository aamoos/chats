package com.chatting.repository;

import com.chatting.entity.Friends;
import com.chatting.entity.FriendsInvite;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FriendsRepository extends CrudRepository<Friends, Long> {

    //받은 이력 조회
    @Query(value =
            "SELECT \n"+
                    "T1.IDX\n"+
                    ",T1.USER_IDX\n"+
                    ",(SELECT T2.NICK_NAME FROM USERS T2 WHERE T2.USER_IDX = T1.FRIENDS_IDX) AS FRIENDS_NAME\n"+
                    ",(SELECT T2.PROFILE_IDX FROM USERS T2 WHERE T2.USER_IDX = T1.FRIENDS_IDX) AS FRIENDS_PROFILE_IDX\n"+
                    ",T1.FRIENDS_IDX\n"+
                    ",T1.REG_DATE\n"+
                    ",T1.CHAT_ROOM_IDX\n"+
                    ",T1.TOKEN\n"+
                    ",T1.USE_YN\n"+
                    "FROM \n"+
                    "friends T1\n"+
                    "WHERE\n"+
                    "T1.USER_IDX = :userIdx\n"
            , nativeQuery = true
    )
    List<Friends> selectReciveNotification(Long userIdx);

    //친구검색
    @Query(value =
            "SELECT \n"+
                    "T1.IDX\n"+
                    ",T1.USER_IDX\n"+
                    ",(SELECT T2.NICK_NAME FROM USERS T2 WHERE T2.USER_IDX = T1.FRIENDS_IDX) AS FRIENDS_NAME\n"+
                    ",(SELECT T2.PROFILE_IDX FROM USERS T2 WHERE T2.USER_IDX = T1.FRIENDS_IDX) AS FRIENDS_PROFILE_IDX\n"+
                    ",T1.FRIENDS_IDX\n"+
                    ",T1.REG_DATE\n"+
                    ",T1.CHAT_ROOM_IDX\n"+
                    ",T1.TOKEN\n"+
                    ",T1.USE_YN\n"+
                    "FROM \n"+
                    "friends T1\n"+
                    "WHERE\n"+
                    "T1.USER_IDX = :userIdx\n"+
                    "AND (SELECT T2.NICK_NAME FROM USERS T2 WHERE T2.USER_IDX = T1.FRIENDS_IDX) LIKE CONCAT('%',:friendsName,'%')\n"+
                    "AND T1.USE_YN = 'Y'\n"
            , nativeQuery = true
    )
    List<Friends> selectFriendsList(String friendsName, Long userIdx);

    Friends findByFriendsIdxAndUserIdxAndUseYn(Long friendsIdx, Long userIdx, String useYn);

    Friends findByIdx(Long idx);

    List<Friends> findAllByFriendsIdxOrUserIdx(Long friendsIdx, Long userIdx);

    @Query(value =
            "SELECT \n"+
                    "T1.IDX\n"+
                    ",T1.FRIENDS_IDX\n"+
                    ",T1.CHAT_ROOM_IDX\n"+
                    ",T1.FRIENDS_NAME\n"+
                    ",T1.FRIENDS_PROFILE_IDX\n"+
                    ",T1.REG_DATE\n"+
                    ",T1.USER_IDX\n"+
                    ",T2.TOKEN\n"+
                    ",T1.USE_YN\n"+
                    "FROM \n"+
                    "friends T1\n"+
                    ",users T2\n"+
                    "WHERE\n"+
                    "T1.USER_IDX = T2.USER_IDX\n"+
                    "AND T1.CHAT_ROOM_IDX = :chatRoomIdx\n"+
                    "AND T1.USER_IDX != :userIdx\n"+
                    "AND T1.USE_YN = 'Y'\n"
            , nativeQuery = true
    )
    List<Friends> selectChatPushList(String chatRoomIdx, Long userIdx);
}