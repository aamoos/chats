package com.chatting.controller;

import com.chatting.common.Url;
import com.chatting.dto.UsersDto;
import com.chatting.entity.Friends;
import com.chatting.entity.FriendsInvite;
import com.chatting.entity.Users;
import com.chatting.repository.FriendsInviteRepository;
import com.chatting.repository.FriendsRepository;
import com.chatting.repository.UsersRepository;
import com.chatting.service.MainService;
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
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 메인 controller
 */

@Controller
@RequiredArgsConstructor
public class MainController {

    private final UsersRepository usersRepository;
    private final UsersService usersService;
    private final MainService mainService;
    private final FriendsInviteRepository friendsInviteRepository;
    private final FriendsRepository friendsRepository;

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

        //내정보 조회
        Users users = usersRepository.findByUserId(principal.getName());

        //친구 초대 리스트
        List<FriendsInvite> friendsInvite = friendsInviteRepository.findByResponseUserId(principal.getName());

        //친구 리스트
        List<Friends> friends = friendsRepository.findByUserId(principal.getName());

        //로그인한 사용자 정보 조회
        model.addAttribute("userInfo", users);
        model.addAttribute("handPhoneNo", StringUtils.phone(users.getHandPhoneNo()));
        model.addAttribute("address", req.getParameter("address"));
        model.addAttribute("notification", friendsInvite);
        model.addAttribute("friends", friends);


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
     *  친구 요청 보내기
     */
    @ResponseBody
    @PostMapping(Url.MAIN.FRIENDS_INVITE)
    public Long friendsInvite(@RequestBody FriendsInvite friendsInvite){
        return mainService.saveFriendsInvite(friendsInvite);
    };

    /**
     * 친구요청 받는사람이 실제로 존재하는지 체크
     * @param friendsInvite
     * @return
     */
    @ResponseBody
    @PostMapping(Url.MAIN.FRIENDS_EXIST_CHECK)
    public Map<String, Object> friendsExistCheck(@RequestBody FriendsInvite friendsInvite, Principal principal){
        return mainService.friendsExistCheck(friendsInvite, principal);
    };

    /**
     * 플래그 업데이트
     * @param friendsInvite
     * @return
     */
    @ResponseBody
    @PostMapping(Url.MAIN.RESPONSE_YN_UPDATE)
    public Long responseYnUpdate(@RequestBody FriendsInvite friendsInvite, Principal principal){
        return mainService.responseYnUpdate(friendsInvite, principal);
    };


    /**
     * 채팅
     */
    @GetMapping(Url.MAIN.CHAT)
    public String chat(){
        return Url.MAIN.CHAT_HTML;
    }

}
