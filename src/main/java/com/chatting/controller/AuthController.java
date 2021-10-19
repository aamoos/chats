package com.chatting.controller;

import com.chatting.entity.Users;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import javax.validation.Valid;

/**
 * 로그인 controller
 */

@Controller
@RequiredArgsConstructor
public class AuthController {

    /**
     * 로그인 화면
     * @return
     */
    @GetMapping("/")
    public String login(){
        return "auth/login";
    }

    /**
     * 회원가입 화면
     * @return
     */
    @GetMapping("/join")
    public String join(Users users){
        return "auth/join";
    }

    @PostMapping("/join")
    public String joinSubmit(@Valid Users users, BindingResult bindingResult){
        System.out.println("userId : " + users.getUserId());
        if (bindingResult.hasErrors()) {
            return "auth/join :: #login-form";
        }

        //push 발송


        return "auth/joinCheck::#login-form";
    }

    /**
     * 인증번호 확인 화면
     * @return
     */
    @GetMapping("/joinCheck")
    public String joinCheck(){
        return "auth/joinCheck";
    }
}
