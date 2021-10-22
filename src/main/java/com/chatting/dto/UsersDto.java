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
public class UsersDto implements UserDetails {

    //사용자 아이디
    @NotBlank(message = "아이디는 필수 항목입니다.")
    @Email(message = "이메일 형식이 아닙니다.")
    private String userId;

    //사용자 패스워드
    @NotBlank(message="패스워드는 필수 항목입니다.")
    @Pattern(regexp="(?=.*[0-9])(?=.*[a-zA-Z])(?=.*\\W)(?=\\S+$).{8,20}",
            message = "비밀번호는 영문 대,소문자와 숫자, 특수기호가 적어도 1개 이상씩 포함된 8자 ~ 20자의 비밀번호여야 합니다.")
    private String password;

    private String password1;

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

    public UsersDto() {}

    public UsersDto(String userId, String password, String handPhoneNo, String username, String useYn, String token) {
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
