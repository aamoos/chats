package com.chatting.controller;

import com.chatting.common.Url;
import com.chatting.entity.Users;
import com.chatting.repository.UsersRepository;
import com.chatting.util.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.security.Principal;

/**
 * 메인 controller
 */

@Controller
@RequiredArgsConstructor
public class MainController {

    private final UsersRepository usersRepository;

    /**
     * 메인
     * @return
     */
    @GetMapping(Url.MAIN.MAIN)
    public String main(HttpServletRequest req, Principal principal, Model model){

        Users users = usersRepository.findByUserId(principal.getName());

        //로그인한 사용자 정보 조회
        model.addAttribute("userInfo", users);
        model.addAttribute("handPhoneNo", StringUtils.phone(users.getHandPhoneNo()));
        model.addAttribute("address", req.getParameter("address"));

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
