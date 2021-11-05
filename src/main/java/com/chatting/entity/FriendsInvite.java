package com.chatting.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 친구 추가 요청 entity
 */
@Entity
@Getter
@Setter
public class FriendsInvite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idx;

    //친구 요청한 유저 idx
    private Long requestUserIdx;

    //친구 요청받은 유저 idx
    private Long responseUserIdx;

    //아직 응답을 읽지않는 상태 null, 거절하면 N, 받으면 Y
    private String responseYn;

    //메시지
    private String message;

    private String requestUserName;

    private String requestProfileIdx;

    //insert시에 현재시간을 읽어서 저장
    @CreationTimestamp
    private LocalDateTime regDate;
}
