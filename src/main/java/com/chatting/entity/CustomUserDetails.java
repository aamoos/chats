package com.chatting.entity;

import com.chatting.dto.UsersAuthorityDto;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@Getter
@Setter
public class CustomUserDetails implements UserDetails {

    private String userId;
    private String password;
    private List<Map<String,String>> authenticatedMenu = new ArrayList<Map<String,String>>();
    private List<GrantedAuthority> roles = new ArrayList<GrantedAuthority>();

    //권한
    public List<UsersAuthorityDto> authorities;

    public CustomUserDetails(String userId, String password) {
        this.userId = userId;
        this.password = password;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getUsername() {
        return userId;
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
