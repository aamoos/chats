package com.chatting.dto;

import com.chatting.entity.UsersAuthority;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;

import java.util.List;

@Getter
@Setter
public class UsersAuthorityDto implements GrantedAuthority {

    //사용자 id
    private String userId;

    //사용자 권한
    private String authority;

    @Override
    public String getAuthority() {
        // TODO Auto-generated method stub
        return this.authority;
    }

    public UsersAuthorityDto() {}

    public UsersAuthorityDto(String userId, String authority) {
        this.userId = userId;
        this.authority = authority;
    }

    // 엔티티변환
    public UsersAuthority toEntity() {
        return UsersAuthority.builder()
                .userId(this.userId)
                .authority(this.authority)
                .build();
    }

}
