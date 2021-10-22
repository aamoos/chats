package com.chatting.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.sql.Timestamp;
import java.time.LocalDateTime;

@Entity
public class PersistentLogins {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long persistentIdx;
    private String username;
    private String series;
    private String token;
    private LocalDateTime lastUsed;


}
