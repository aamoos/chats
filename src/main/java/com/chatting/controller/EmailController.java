package com.chatting.controller;

import com.chatting.common.Url;
import com.chatting.util.EmailUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import java.util.Map;

/**
 * 이메일 컨트롤러
 */

@Slf4j
@RequiredArgsConstructor
@RestController
public class EmailController {

    private final EmailUtil emailUtil;

    /**
     * 이메일 발송
     * @param params
     * @return
     */
    @PostMapping(Url.AUTH.SEND_EMAIL)
    public Map<String, Object> sendEmail(@RequestBody Map<String, Object> params){
        System.out.println("userId : "+params.get("userId"));
        System.out.println("serialNo : "+params.get("serialNo"));

        log.info("email params={}", params);

        return emailUtil.sendEmail( (String) params.get("userId")
                , (String) params.get("subject")
                , (String) params.get("body")
        );
    }
}
