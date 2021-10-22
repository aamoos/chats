package com.chatting.controller;

import com.chatting.common.Url;
import com.chatting.dto.IdCheckDto;
import com.chatting.dto.PwdCheckDto;
import com.chatting.dto.SelfAuthDto;
import com.chatting.dto.UsersDto;
import com.chatting.service.UsersService;
import com.chatting.validator.UsersDtoValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.Map;

/**
 * 로그인 controller
 */

@Controller
@RequiredArgsConstructor
public class AuthController {

    private final UsersService usersService;
    private final UsersDtoValidator usersDtoValidator;

    /**
     * 로그인 화면
     * @return
     */
    @GetMapping(Url.AUTH.LOGIN)
    public String login(HttpServletRequest req, HttpSession session){
        System.out.println("token : " + req.getParameter("token"));
        String token = req.getParameter("token");

        if(token == null){
            token = "eS_opLwPSkuntQKR0z6ka3:APA91bFmn4E7cIiCP7SKi-_j1Ctz1mJBywrLlFx00IibYq9PCXzqDymy7RjScniARhFUqatpAoLHI3s-wSyrIdSxJAI_-xdEU8EjxiUW04ClON33qeJ4Ua-_gBm28DZMWq7a4xaCoq2_";
        }

        session.setAttribute("token", token);
        return Url.AUTH.LOGIN_HTML;
    }

    /**
     * 회원가입 화면
     * @return
     */
    @GetMapping(Url.AUTH.JOIN)
    public String join(Model model){
        model.addAttribute("usersDto", new UsersDto());
        return Url.AUTH.JOIN_HTML;
    }

    /**
     * 회원가입
     * @param usersDto
     * @param result
     * @param session
     * @return
     */
    @PostMapping(Url.AUTH.JOIN)
    public String joinSubmit(@Valid UsersDto usersDto, BindingResult result, HttpSession session){

        usersDtoValidator.validate(usersDto, result);
        System.out.println(result.getAllErrors());
        if (result.hasErrors()) {
            return Url.AUTH.JOIN_HTML;
        }

        System.out.println("userId : " + usersDto.getUserId());

        //session 설정
        session.setAttribute("userId", usersDto.getUserId());
        session.setAttribute("password", usersDto.getPassword());
        session.setAttribute("handPhoneNo", usersDto.getHandPhoneNo());
        session.setAttribute("userName", usersDto.getUsername());

        return Url.AUTH.JOIN_CHECK_HTML;
    }

    /**
     * 중복체크
     * @param usersDto
     * @return
     */
    @PostMapping(Url.AUTH.DUPLICATE_CHECK)
    @ResponseBody
    public Map<String, Object> duplicateCheck(@RequestBody UsersDto usersDto){
        return usersService.duplicateCheck(usersDto);
    }

    /**
     * 인증번호 저장
     * @param SelfAuthDto
     */
    @PostMapping(Url.AUTH.SAVE_SERIAL_NO)
    @ResponseBody
    public void saveSerialNo(@RequestBody SelfAuthDto SelfAuthDto){

        //기존 serialNo 삭제
        usersService.deleteSerialNo(SelfAuthDto.getUserId());

        //새로운 serialNo 저장
        usersService.saveSerialNo(SelfAuthDto);
    }

    /**
     * 인증번호 체크 맞으면 회원가입 처리
     * @param SelfAuthDto
     * @return
     */
    @PostMapping(Url.AUTH.SERIAL_NO_CHECK)
    @ResponseBody
    public Map<String, Object> serialNoCheck(@RequestBody SelfAuthDto SelfAuthDto, HttpSession httpSession){
        return usersService.serialNoCheck(SelfAuthDto, httpSession);
    }

    /**
     * 아이디 찾기
     * @param model
     * @return
     */
    @GetMapping(Url.AUTH.FIND_ID_CHECK)
    public String findIdCheck(Model model){
        model.addAttribute("idCheckDto", new UsersDto());
        return Url.AUTH.FIND_ID_CHECK_HTML;
    }

    @PostMapping(Url.AUTH.FIND_ID_CHECK)
    public String findIdCheckSubmit(@Valid IdCheckDto idCheckDto, BindingResult result, HttpSession session, Model model){

        if (result.hasErrors()) {
            return Url.AUTH.FIND_ID_CHECK_HTML;
        }

        model.addAttribute("userId", usersService.findByUserId(idCheckDto));

        return Url.AUTH.FIND_ID_CHECK_RESULT_HTML;
    }

    /**
     * 비밀번호 찾기
     * @param model
     * @return
     */
    @GetMapping(Url.AUTH.FIND_PWD_CHECK)
    public String finPwdCheck(Model model){
        model.addAttribute("pwdCheckDto", new PwdCheckDto());
        return Url.AUTH.FIND_PWD_CHECK_HTML;
    }

    @PostMapping(Url.AUTH.FIND_PWD_CHECK)
    public String findPwdCheckSubmit(@Valid PwdCheckDto pwdCheckDto, BindingResult result, HttpSession session, Model model){

        if (result.hasErrors()) {
            return Url.AUTH.FIND_PWD_CHECK_HTML;
        }

        model.addAttribute("userId", pwdCheckDto.getUserId());

        return Url.AUTH.FIND_PWD_CHECK_RESULT_HTML;
    }

    /**
     * 비밀번호 아이디와 동일하게 변경
     * @param usersDto
     * @param httpSession
     * @return
     */
    @PostMapping(Url.AUTH.PWD_RESET)
    @ResponseBody
    public Long pwdReset(@RequestBody UsersDto usersDto, HttpSession httpSession){
        return usersService.pwdReset(usersDto);
    }


}
