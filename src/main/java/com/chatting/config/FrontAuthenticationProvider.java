package com.chatting.config;

import java.util.ArrayList;
import java.util.List;

import com.chatting.entity.Users;
import com.chatting.entity.UsersAuthority;
import com.chatting.exception.UserAuthException;
import com.chatting.exception.UserIdException;
import com.chatting.repository.UsersAuthorityRepository;
import com.chatting.repository.UsersRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * 로그인 provider
 */
@Slf4j
@Component
public class FrontAuthenticationProvider implements AuthenticationProvider{

    @Autowired
    UsersRepository usersRepository;

    @Autowired
    UsersAuthorityRepository userAuthorityRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @SuppressWarnings("unchecked")
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        UsernamePasswordAuthenticationToken authToken = (UsernamePasswordAuthenticationToken) authentication;
        //아이디
        String id = (authToken.getName()).toLowerCase();

        //비밀번호
        String pwd = (String) authToken.getCredentials();

        log.info("id={}, pwd={}", id, pwd);

        //해당 사용자 정보 조회
        Users userInfo = usersRepository.findByUserIdAndUseYn(id, "Y");

        //해당 사용자가 있을경우
        if(userInfo != null ) {
            //입력한 비밀번호가 현재 비밀번호와 같지 않으면
            if(!(passwordEncoder.matches(pwd, userInfo.getPassword()))) {
                throw new UserIdException("접속 할 수 없습니다. \n아이디 또는 비밀번호를 확인해주세요.");
            }

            //입력한 비밀번호가 현재 비밀번호와 같으면
            else {
                List<UsersAuthority> authorities = new ArrayList<>();

                //권한 조회
                List<UsersAuthority> getUserAuthorities = userAuthorityRepository.findByUserIdx(userInfo.getUserIdx());

                log.info("getUserAuthorities={}",getUserAuthorities);

                //권한이 있을경우
                if(getUserAuthorities != null) {
                    authorities.add(getUserAuthorities.get(0));
                    userInfo.setAuthorities(authorities);
                }

                //권한이 없을 경우
                else {
                    throw new UserAuthException("접속권한이 없습니다. \n관리자에게 권한을 요청해주세요.");
                }
            }
        }

        //해당 사용자가 없을경우
        else {
            throw new UserIdException("접속 할 수 없습니다. \n아이디 또는 비밀번호를 확인해주세요.");
        }

        List<GrantedAuthority> authorities = (List<GrantedAuthority>) userInfo.getAuthorities();
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(userInfo, "1", authorities);

        return token;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }

}