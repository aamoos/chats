package com.chatting.dto;

import com.chatting.entity.SelfAuth;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SelfAuthDto {

    //사용자 아이디
    private String userId;

    //사용자 serial 번호
    private String serialNo;

    public SelfAuthDto() {}

    public SelfAuthDto(String userId, String serialNo) {
        this.userId = userId;
        this.serialNo = serialNo;
    }

    // 엔티티변환
    public SelfAuth toEntity() {
        return SelfAuth.builder()
                .userId(this.userId)
                .serialNo(this.serialNo)
                .build();
    }

}
