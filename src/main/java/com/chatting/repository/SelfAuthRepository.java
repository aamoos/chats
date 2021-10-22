package com.chatting.repository;

import com.chatting.dto.SelfAuthDto;
import com.chatting.entity.SelfAuth;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * serialNo 저장된 테이블
 */

@Repository
public interface SelfAuthRepository extends CrudRepository<SelfAuth, Long> {

    //해당 아이디를 가진 사람 삭제
    Long deleteByUserId(String userId);

    //해당 아이디, 인증번호를 가지고있는 사람 체크
    SelfAuth findByUserIdAndSerialNo(String userId, String serialNo);
}
