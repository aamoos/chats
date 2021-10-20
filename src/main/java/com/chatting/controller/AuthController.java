package com.chatting.controller;

import com.chatting.entity.SelfAuth;
import com.chatting.entity.Users;
import com.chatting.service.UsersService;
import lombok.RequiredArgsConstructor;
import org.aspectj.weaver.patterns.IToken;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.Map;
import java.util.Optional;

/**
 * 로그인 controller
 */

@Controller
@RequiredArgsConstructor
public class AuthController {

    private final UsersService usersService;

    /**
     * 로그인 화면
     * @return
     */
    @GetMapping("/")
    public String login(HttpServletRequest req, HttpSession session){
        System.out.println("token : " + req.getParameter("token"));
        String token = req.getParameter("token");

        if(token == null){
            token = "eS_opLwPSkuntQKR0z6ka3:APA91bFmn4E7cIiCP7SKi-_j1Ctz1mJBywrLlFx00IibYq9PCXzqDymy7RjScniARhFUqatpAoLHI3s-wSyrIdSxJAI_-xdEU8EjxiUW04ClON33qeJ4Ua-_gBm28DZMWq7a4xaCoq2_";
        }

        session.setAttribute("token", token);
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

    /**
     * 회원가입 버튼 클릭할경우 validation
     * @param users
     * @param bindingResult
     * @param session
     * @return
     */
    @PostMapping("/join")
    public String joinSubmit(@Valid Users users, BindingResult bindingResult, HttpSession session){
        System.out.println("userId : " + users.getUserId());
        if (bindingResult.hasErrors()) {
            return "auth/join :: #login-form";
        }

        //push 발송
        session.setAttribute("users", users);

        System.out.println(session.getAttribute("users"));
        System.out.println(session.getAttribute("token"));

        return "auth/joinCheck";
    }

    /**
     * 인증번호 저장
     * @param selfAuth
     */
    @PostMapping("/saveSerialNo")
    @ResponseBody
    public void saveSerialNo(@RequestBody SelfAuth selfAuth){

        System.out.println("serialNo : " + selfAuth.getSerialNo());
        System.out.println("userId : " + selfAuth.getUserId());

        //기존 serialNo 삭제
        usersService.deleteSerialNo(selfAuth.getUserId());

        //새로운 serialNo 저장
        usersService.saveSerialNo(selfAuth);
    }

    /**
     * 인증번호 체크 맞으면 회원가입 처리
     * @param selfAuth
     * @return
     */
    @PostMapping("/serialNoCheck")
    @ResponseBody
    public Map<String, Object> serialNoCheck(@RequestBody SelfAuth selfAuth, HttpSession httpSession){
        return usersService.serialNoCheck(selfAuth, httpSession);
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
