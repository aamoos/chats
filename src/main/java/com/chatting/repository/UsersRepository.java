package com.chatting.repository;

import com.chatting.entity.Users;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsersRepository extends CrudRepository<Users, Long> {

    //userId 또는 핸드폰번호로 찾기
    Users findByUserIdOrHandPhoneNoAndUseYn(String userId, String handPhoneNo, String useYn);

}
