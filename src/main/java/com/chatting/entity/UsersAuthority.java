package com.chatting.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import javax.persistence.*;

@Entity
@Getter
@Setter
public class UsersAuthority implements GrantedAuthority{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long authIdx;

    //사용자 id
    @Column(columnDefinition = "varchar(45) not null comment '사용자 아이디'")
    private String userId;

    //사용자 권한
    @Column(columnDefinition = "varchar(45) not null comment '권한'")
    private String authority;

    public UsersAuthority() {}

    @Builder
    public UsersAuthority(String userId, String authority) {
        this.userId = userId;
        this.authority = authority;
    }

}
