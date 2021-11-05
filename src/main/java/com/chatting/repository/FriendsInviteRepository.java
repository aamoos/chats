package com.chatting.repository;

import com.chatting.entity.FriendsInvite;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FriendsInviteRepository extends CrudRepository<FriendsInvite, Long> {

    FriendsInvite findByIdx(Long idx);

    //보낸 이력 조회
    FriendsInvite findByResponseUserIdx(Long responseUseIdx);

    //받은 이력 조회 (notification)
    @Query(value =
            "SELECT \n"+
                    "T1.IDX\n"+
                    ",T1.MESSAGE\n"+
                    ",T1.REQUEST_USER_IDX\n"+
                    ",T1.RESPONSE_USER_IDX\n"+
                    ",T1.RESPONSE_YN\n"+
                    ",T1.REG_DATE\n"+
                    ",T2.NICK_NAME AS REQUEST_USER_NAME\n"+
                    ",T2.PROFILE_IDX AS REQUEST_PROFILE_IDX\n"+
                    "FROM \n"+
                    "friends_invite T1\n"+
                    ",users T2\n"+
                    "WHERE\n"+
                    "T1.REQUEST_USER_IDX = T2.USER_IDX\n"+
                    "AND T2.USE_YN = 'Y'\n"+
                    "AND T1.RESPONSE_USER_IDX = :responseUserIdx\n"+
                    "AND (T1.RESPONSE_YN IS NULL OR T1.RESPONSE_YN = '')\n"+
                    "ORDER BY T1.IDX DESC"
            , nativeQuery = true
    )

    List<FriendsInvite> selectReceiveHistory(Long responseUserIdx);

    //받은 이력 조회 (notification)
    @Query(value =
            "SELECT \n"+
                    "T1.IDX\n"+
                    ",T1.MESSAGE\n"+
                    ",T1.REQUEST_USER_IDX\n"+
                    ",T1.RESPONSE_USER_IDX\n"+
                    ",T1.RESPONSE_YN\n"+
                    ",T1.REG_DATE\n"+
                    ",T2.NICK_NAME AS REQUEST_USER_NAME\n"+
                    ",T2.PROFILE_IDX AS REQUEST_PROFILE_IDX\n"+
                    "FROM \n"+
                    "friends_invite T1\n"+
                    ",users T2\n"+
                    "WHERE\n"+
                    "T1.REQUEST_USER_IDX = T2.USER_ID\n"+
                    "AND T2.USE_YN = 'Y'\n"+
                    "AND T1.RESPONSE_USER_IDX = :responseUserIdx\n"+
                    "AND T1.REQUEST_USER_IDX = :requestUserIdx\n"+
                    "AND (T1.RESPONSE_YN IS NULL OR T1.RESPONSE_YN = '')\n"+
                    "ORDER BY T1.IDX DESC"
            , nativeQuery = true
    )

    //친구 검색
    List<FriendsInvite> selectReceiveHistorySearch(String responseUserIdx, String requestUserIdx);
}
