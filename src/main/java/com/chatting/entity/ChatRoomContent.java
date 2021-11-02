package com.chatting.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Entity
@Getter
@Setter
public class ChatRoomContent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long contentIdx;

    private Long chatRoomIdx;

    private String chatContents;

    private String chatUserId;

    private String fileIdxs;

    private Long chatProfileIdx;

    private String chatNickName;

    private String chatRoomName;

    @CreatedDate
    private String regDate;

    @PrePersist
    public void onPrePersist(){
        this.regDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }
}
