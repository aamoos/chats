package com.chatting.entity;

import lombok.Getter;
import lombok.Setter;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Entity
@Getter
@Setter
public class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userIdx;

    //사용자 아이디
    @NotBlank(message="아이디는 필수 항목입니다.")
    private String userId;

    //사용자 패스워드
    @NotBlank(message="패스워드는 필수 항목입니다.")
    private String password;

    //사용자 핸드폰번호
    @NotBlank(message="핸드폰 번호는 필수 항목입니다.")
    private String handPhoneNo;

    //사용자 이름
    @NotBlank(message="이름은 필수 항목입니다.")
    private String userName;
}
