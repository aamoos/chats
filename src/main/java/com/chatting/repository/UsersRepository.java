package com.chatting.repository;

import com.chatting.dto.UsersDto;
import com.chatting.entity.Users;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsersRepository extends CrudRepository<Users, Long> {

    //userId 또는 핸드폰번호로 찾기
    Users findByUserIdOrHandPhoneNoAndUseYn(String userId, String handPhoneNo, String useYn);

    //해당 아이디가 존재하는지 체크
    boolean existsByUserIdAndUseYn(String userId, String useYn);

    //아이디로 검색
    Users findByUserIdxAndUseYn(Long userIdx, String useYn);

    //아이디 찾기
    Users findByHandPhoneNoAndNickNameAndUseYn(String handPhoneNo, String nickName, String useYn);

    //아이디 찾기
    Users findByUserIdAndUseYn(String userId,String useYn);

}
