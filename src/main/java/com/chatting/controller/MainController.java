package com.chatting.controller;

import com.chatting.common.Url;
import com.chatting.dto.UsersDto;
import com.chatting.entity.Users;
import com.chatting.repository.UsersRepository;
import com.chatting.service.UsersService;
import com.chatting.util.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.security.Principal;

/**
 * 메인 controller
 */

@Controller
@RequiredArgsConstructor
public class MainController {

    private final UsersRepository usersRepository;
    private final UsersService usersService;

    @GetMapping("/")
    public void root(HttpServletResponse res, Principal principal) throws Exception {

        if(principal != null){
            res.sendRedirect(Url.MAIN.MAIN);
        }

        else{
            res.sendRedirect(Url.AUTH.LOGIN);
        }
    }

    /**
     * 메인
     * @return
     */
    @GetMapping(Url.MAIN.MAIN)
    public String main(HttpServletRequest req, Principal principal, Model model){

        Users users = usersRepository.findByUserId(principal.getName());

        System.out.println("address : " + req.getParameter("address"));

        //로그인한 사용자 정보 조회
        model.addAttribute("userInfo", users);
        model.addAttribute("handPhoneNo", StringUtils.phone(users.getHandPhoneNo()));
        model.addAttribute("address", req.getParameter("address"));

        return Url.MAIN.MAIN_HTML;
    }

    /**
     * 프로필 이미지 저장
     * @param usersDto
     * @return
     */
    @ResponseBody
    @PostMapping(Url.MAIN.SAVE_PROFILE_IMG)
    public Long writeSubmit(@RequestBody UsersDto usersDto){

        return usersService.saveProfileImg(usersDto);
    };

    /**
     * 채팅
     */
    @GetMapping(Url.MAIN.CHAT)
    public String chat(){
        return Url.MAIN.CHAT_HTML;
    }

}
