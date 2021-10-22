package com.chatting.dto;

import com.chatting.entity.Users;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.util.Collection;
import java.util.List;

@Getter
@Setter
public class IdCheckDto implements UserDetails {

    //사용자 아이디
    private String userId;

    //사용자 패스워드
    private String password;

    //사용자 핸드폰번호
    @NotBlank(message="핸드폰 번호는 필수 항목입니다.")
    @Pattern(regexp = "[0-9]{10,11}", message = "10~11자리의 숫자만 입력가능합니다")
    private String handPhoneNo;

    //사용자 이름
    @NotBlank(message="이름은 필수 항목입니다.")
    private String username;

    //사용여부
    private String useYn;

    //푸시 토큰
    private String token;

    //권한
    public List<UsersAuthorityDto> authorities;

    public IdCheckDto() {}

    public IdCheckDto(String userId, String password, String handPhoneNo, String username, String useYn, String token) {
        this.userId = userId;
        this.password = password;
        this.handPhoneNo = handPhoneNo;
        this.username = username;
        this.useYn = useYn;
        this.token = token;
    }

    // 엔티티변환
    public Users toEntity() {
        return Users.builder()
                .userId(this.userId)
                .password(this.password)
                .handPhoneNo(this.handPhoneNo)
                .username(this.username)
                .useYn(this.useYn)
                .token(this.token)
                .build();
    }

    @Override
    public String getUsername() {
        return username;
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
