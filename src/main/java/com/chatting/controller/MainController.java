package com.chatting.controller;

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
    @GetMapping("/main")
    public String main(){
        return "chat/main";
    }

    /**
     * 채팅
     */
    @GetMapping("/chat")
    public String chat(){
        return "chat/chat";
    }

}
