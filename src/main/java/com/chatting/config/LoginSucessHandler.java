package com.chatting.config;

import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import com.chatting.common.CoTopComponent;
import com.chatting.dto.UsersDto;
import com.chatting.entity.Users;
import com.chatting.entity.UsersAuthority;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import com.google.gson.JsonObject;

/**
 * 로그인 성공시 타는 핸들러
 */
@Component
@Slf4j
public class LoginSucessHandler extends CoTopComponent implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication auth) throws IOException, ServletException {

        //default 성공
        String resultCode = "00";

        HttpSession session = request.getSession(true);
        session.setMaxInactiveInterval(60 * 60 * 3);

        List<UsersAuthority> authorities = (List<UsersAuthority>) ((Users) auth.getPrincipal()).getAuthorities();

        log.info("sessUserInfo={}", session.getAttribute("sessUserInfo"));

        session.setAttribute("sessUserInfo",((Users) auth.getPrincipal()));
        session.setAttribute("authority", authorities.get(0).getAuthority());

        //Response 결과 값을 넣어줌
        JsonObject loginResult = new JsonObject();
        loginResult.addProperty("resultCode", resultCode);
        loginResult.addProperty("targetUrl", request.getContextPath()+"/");

        //응답 전송
        writeResponse(response, loginResult);
    }

}
