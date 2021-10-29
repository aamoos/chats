package com.chatting.repository;

import com.chatting.entity.FriendsInvite;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FriendsInviteRepository extends CrudRepository<FriendsInvite, Long> {

    FriendsInvite findByFriendsIdx(Long friendsIdx);

    //보낸 이력 조회
    FriendsInvite findByResponseUserId(String responseUserId);

    //받은 이력 조회 (notification)
    @Query(value =
            "SELECT \n"+
                    "T1.FRIENDS_IDX\n"+
                    ",T1.MESSAGE\n"+
                    ",T1.REQUEST_USER_ID\n"+
                    ",T1.RESPONSE_USER_ID\n"+
                    ",T1.RESPONSE_YN\n"+
                    ",T1.REG_DATE\n"+
                    ",T2.NICK_NAME AS REQUEST_USER_NAME\n"+
                    ",T2.PROFILE_IDX AS REQUEST_PROFILE_IDX\n"+
                    "FROM \n"+
                    "friends_invite T1\n"+
                    ",users T2\n"+
                    "WHERE\n"+
                    "T1.REQUEST_USER_ID = T2.USER_ID\n"+
                    "AND T2.USE_YN = 'Y'\n"+
                    "AND T1.RESPONSE_USER_ID = :responseUserId\n"+
                    "AND (T1.RESPONSE_YN IS NULL OR T1.RESPONSE_YN = '')\n"+
                    "ORDER BY T1.FRIENDS_IDX DESC"
            , nativeQuery = true
    )

    List<FriendsInvite> selectReceiveHistory(String responseUserId);

    //받은 이력 조회 (notification)
    @Query(value =
            "SELECT \n"+
                    "T1.FRIENDS_IDX\n"+
                    ",T1.MESSAGE\n"+
                    ",T1.REQUEST_USER_ID\n"+
                    ",T1.RESPONSE_USER_ID\n"+
                    ",T1.RESPONSE_YN\n"+
                    ",T1.REG_DATE\n"+
                    ",T2.NICK_NAME AS REQUEST_USER_NAME\n"+
                    ",T2.PROFILE_IDX AS REQUEST_PROFILE_IDX\n"+
                    "FROM \n"+
                    "friends_invite T1\n"+
                    ",users T2\n"+
                    "WHERE\n"+
                    "T1.REQUEST_USER_ID = T2.USER_ID\n"+
                    "AND T2.USE_YN = 'Y'\n"+
                    "AND T1.RESPONSE_USER_ID = :responseUserId\n"+
                    "AND T1.REQUEST_USER_ID = :requestUserId\n"+
                    "AND (T1.RESPONSE_YN IS NULL OR T1.RESPONSE_YN = '')\n"+
                    "ORDER BY T1.FRIENDS_IDX DESC"
            , nativeQuery = true
    )

    //친구 검색
    List<FriendsInvite> selectReceiveHistorySearch(String responseUserId, String requestUserId);
}
