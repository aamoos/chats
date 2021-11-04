package com.chatting.repository;

import com.chatting.entity.Friends;
import com.chatting.entity.Push;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PushRepository extends CrudRepository<Push, Long> {
    List<Push> findAllBySendYnOrUseYn(String sendYn, String useYn);

    Push findByPushIdxAndUseYnAndSendYn(Long pushIdx, String useYn, String sendYn);

}
