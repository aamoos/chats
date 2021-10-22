package com.chatting.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import javax.persistence.*;
import java.util.Collection;
import java.util.List;

@Getter
@Setter
@Entity
public class Users implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userIdx;

    //사용자 아이디
    private String userId;

    //사용자 패스워드
    private String password;

    //사용자 핸드폰번호
    private String handPhoneNo;

    //사용자 이름
    private String username;

    //사용여부
    private String useYn;

    //푸시토큰
    private String token;

    public Users() {}

    @Transient
    public List<UsersAuthority> authorities;

    @Builder
    public Users(String userId, String password, String handPhoneNo, String username, String useYn, String token) {
        this.userId = userId;
        this.password = password;
        this.handPhoneNo = handPhoneNo;
        this.username = username;
        this.useYn = useYn;
        this.token = token;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
