package com.chatting.entity;

import lombok.Getter;
import lombok.Setter;
import org.w3c.dom.Text;

import javax.persistence.*;

@Entity
@Getter
@Setter
public class ChatRoomContentFile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long contentFileIdx;

    private Long contentIdx;

    @Lob
    private String contentFileUrl;

}
