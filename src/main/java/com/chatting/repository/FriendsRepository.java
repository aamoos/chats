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
                    "T1.FRIENDS_IDX\n"+
                    ",T1.USER_ID\n"+
                    ",(SELECT T2.NICK_NAME FROM USERS T2 WHERE T2.USER_ID = T1.FRIENDS_ID) AS FRIENDS_NAME\n"+
                    ",(SELECT T2.PROFILE_IDX FROM USERS T2 WHERE T2.USER_ID = T1.FRIENDS_ID) AS FRIENDS_PROFILE_IDX\n"+
                    ",T1.FRIENDS_ID\n"+
                    ",T1.REG_DATE\n"+
                    "FROM \n"+
                    "friends T1\n"+
                    "WHERE\n"+
                    "T1.USER_ID = :userId\n"
            , nativeQuery = true
    )
    List<Friends> findByUserId(String userId);

    Friends findByFriendsIdAndUserId(String friendsId, String userId);
}
