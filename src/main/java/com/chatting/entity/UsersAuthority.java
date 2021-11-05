package com.chatting.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.security.core.GrantedAuthority;
import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class UsersAuthority implements GrantedAuthority{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long authIdx;

    //사용자 id
    @Column(columnDefinition = "varchar(45) not null comment '사용자 아이디'")
    private Long userIdx;

    //사용자 권한
    @Column(columnDefinition = "varchar(45) not null comment '권한'")
    private String authority;

    //insert시에 현재시간을 읽어서 저장
    @CreationTimestamp
    private LocalDateTime regDate;

    public UsersAuthority() {}

    @Builder
    public UsersAuthority(String userId, String authority) {
        this.userIdx = userIdx;
        this.authority = authority;
    }

}
