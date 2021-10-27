package com.chatting.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
public class File {

    //파일 idx
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long fileIdx;

    //파일 오리지널 이름
    private String origNm;

    //서버에 올라간 파일 이름
    private String logiNm;

    //서버에 올라간 파일 패스
    private String logiPath;

    //확장자
    private String ext;

    //사이즈
    private Long size;

    private String contentType;

    //등록일자
    @CreationTimestamp
    private LocalDateTime regDate;

//    @OneToOne(mappedBy = "file")
//    private BoardFile boardFile;

    public File(String origNm, String logiNm, String logiPath, String ext, Long size, String contentType) {
        this.origNm = origNm;
        this.logiNm = logiNm;
        this.logiPath = logiPath;
        this.ext = ext;
        this.size = size;
        this.contentType = contentType;
    }
}
