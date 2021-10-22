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

        Users user = usersRepository.findByUserId(userId);

        if (user == null){
            throw new UsernameNotFoundException(userId + "is not found. ");
        }
        QuickGuideUser quickGuideUser = new QuickGuideUser();
        quickGuideUser.setUsername(user.getUsername());
        quickGuideUser.setPassword(user.getPassword());
        quickGuideUser.setAuthorities(getAuthorities(userId));
        quickGuideUser.setEnabled(true);
        quickGuideUser.setAccountNonExpired(true);
        quickGuideUser.setAccountNonLocked(true);
        quickGuideUser.setCredentialsNonExpired(true);

        return quickGuideUser;
    }

    public Collection<GrantedAuthority> getAuthorities(String username) {
        List<UsersAuthority> authList = usersAuthorityRepository.findByUserId(username);
        List<GrantedAuthority> authorities = new ArrayList<>();
        for (UsersAuthority authority : authList) {
            authorities.add(new SimpleGrantedAuthority(authority.getAuthority()));
        }
        return authorities;
    }

}
