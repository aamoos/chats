package com.chatting.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;

@Entity
@Getter
@Setter
public class SelfAuth {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long selfIdx;

    //사용자 아이디
    private String userId;

    //사용자 serial 번호
    private String serialNo;
}
