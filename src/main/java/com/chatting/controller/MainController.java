package com.chatting.controller;

import com.chatting.common.Url;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * 메인 controller
 */

@Controller
@RequiredArgsConstructor
public class MainController {

    /**
     * 메인
     * @return
     */
    @GetMapping(Url.MAIN.MAIN)
    public String main(){
        return Url.MAIN.MAIN_HTML;
    }

    /**
     * 채팅
     */
    @GetMapping(Url.MAIN.CHAT)
    public String chat(){
        return Url.MAIN.CHAT_HTML;
    }

}
