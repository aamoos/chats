package com.chatting.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class Friends {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idx;

    //친구 요청한 유저 idx
    private Long userIdx;

    //친구 요청받은 유저 idx
    private Long friendsIdx;

    //insert시에 현재시간을 읽어서 저장
    @CreationTimestamp
    private LocalDateTime regDate;

    private String friendsName;

    private Long friendsProfileIdx;

    private String chatRoomIdx;

    private String token;

    private String useYn;
}

