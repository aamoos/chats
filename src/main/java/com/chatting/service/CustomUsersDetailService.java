package com.chatting.service;

import com.chatting.dto.QuickGuideUser;
import com.chatting.entity.Users;
import com.chatting.entity.UsersAuthority;
import com.chatting.repository.UsersAuthorityRepository;
import com.chatting.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class CustomUsersDetailService implements UserDetailsService{

    private final UsersRepository usersRepository;
    private final UsersAuthorityRepository usersAuthorityRepository;

    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {

        Users user = usersRepository.findByUserIdAndUseYn(userId, "Y");

        if (user == null){
            throw new UsernameNotFoundException(userId + "is not found. ");
        }

        return user;
    }

    public Collection<GrantedAuthority> getAuthorities(String username) {
        Users user = usersRepository.findByUserIdAndUseYn(username, "Y");
        List<UsersAuthority> authList = usersAuthorityRepository.findByUserIdx(user.getUserIdx());
        List<GrantedAuthority> authorities = new ArrayList<>();
        for (UsersAuthority authority : authList) {
            authorities.add(new SimpleGrantedAuthority(authority.getAuthority()));
        }
        return authorities;
    }

}
