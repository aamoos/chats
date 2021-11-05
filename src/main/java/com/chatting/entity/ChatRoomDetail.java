package com.chatting.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Getter
@Setter
public class ChatRoomDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long chatDetailIdx;

    private Long chatRoomIdx;
    private Long userIdx;

}
