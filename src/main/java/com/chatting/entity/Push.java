package com.chatting.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Getter
@Setter
public class Push {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long pushIdx;

    private String title;
    private String body;
    private String token;
    private String sendYn;
    private String useYn;
}
