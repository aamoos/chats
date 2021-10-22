package com.chatting.repository;

import com.chatting.dto.UsersAuthorityDto;
import com.chatting.entity.Users;
import com.chatting.entity.UsersAuthority;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsersAuthorityRepository extends CrudRepository<UsersAuthority, Long> {

    //아이디로 검색
    UsersAuthority findByUserId(String userId);

    //아이디로 삭제
    UsersAuthority deleteByUserId(String userId);

}
