package com.chatting.controller;

import com.chatting.common.Url;
import com.chatting.dto.UsersDto;
import com.chatting.entity.*;
import com.chatting.repository.*;
import com.chatting.service.ChatService;
import com.chatting.service.MainService;
import com.chatting.service.PushService;
import com.chatting.service.UsersService;
import com.chatting.util.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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

    //사용자 repository
    private final UsersRepository usersRepository;

    //사용자 servivce
    private final UsersService usersService;

    //메인 service
    private final MainService mainService;

    //친구 초대 repository
    private final FriendsInviteRepository friendsInviteRepository;

    //친구 repository
    private final FriendsRepository friendsRepository;

    //채팅 service
    private final ChatService chatService;

    //채팅방 repository (1depth)
    private final ChatRoomRepository chatRoomRepository;

    //채팅방 내용 repository (2depth)
    private final ChatRoomContentRepository chatRoomContentRepository;

    //push service
    private final PushService pushService;

    private final ChatRoomDetailRepository chatRoomDetailRepository;

    /**
     * 메인화면
     * @param req
     * @param res
     * @param principal
     * @throws Exception
     */
    @GetMapping("/")
    public void root(HttpServletRequest req, HttpServletResponse res, Principal principal) throws Exception {

        //세션이 있을경우
        if(principal != null){

            //푸시 토큰값
            String token = req.getParameter("token");
            //토큰값 업데이트
            if(token != null){
                Users users = usersRepository.findByUserIdAndUseYn(principal.getName(), "Y");
                users.setToken(token);
                usersRepository.save(users);
            }

            //메인 화면으로
            res.sendRedirect(Url.MAIN.MAIN);
        }

        //세션이 없을경우
        else{

            //로그인화면으로
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
        Users users = usersRepository.findByUserIdAndUseYn(principal.getName(), "Y");

        //친구 초대 리스트
        List<FriendsInvite> friendsInvite = friendsInviteRepository.selectReceiveHistory(users.getUserIdx());

        //친구 리스트
        List<Friends> friends = friendsRepository.selectReciveNotification(users.getUserIdx());

        //로그인한 사용자 정보 조회
        model.addAttribute("userInfo", users);
        model.addAttribute("handPhoneNo", StringUtils.phone(users.getHandPhoneNo()));
        model.addAttribute("address", req.getParameter("address"));
        model.addAttribute("notification", friendsInvite);
        model.addAttribute("friends", friends);

        return Url.MAIN.MAIN_HTML;
    }

    /**
     * 채팅방 파라미터 리턴
     * @param params
     * @param principal
     * @return
     */
    @ResponseBody
    @PostMapping(Url.MAIN.GET_CHAT_ROOM)
    public List<Map<String, Object>> getChatRoom(@RequestBody Map<String, Object> params, Principal principal){

        System.out.println(params.get("roomName"));
        System.out.println(principal.getName());

        Users users = usersRepository.findByUserIdAndUseYn(principal.getName(), "Y");

        //내 채팅방 리스트
        List<Map<String, Object>> chattingRoom = chatRoomContentRepository.selectChattingRoom(users.getUserIdx(), (String) params.get("roomName"));

        return chattingRoom;
    };

    /**
     * 프로필 이미지 저장
     * @param users
     * @return
     */
    @ResponseBody
    @PostMapping(Url.MAIN.SAVE_PROFILE_IMG)
    public Long writeSubmit(@RequestBody Users users){

        return usersService.saveProfileImg(users);
    };

    /**
     *  친구 요청 보내기
     */
    @ResponseBody
    @PostMapping(Url.MAIN.FRIENDS_INVITE)
    public Long friendsInvite(@RequestBody Map<String, Object> params){
        return mainService.saveFriendsInvite(params);
    };

    /**
     * 친구요청 받는사람이 실제로 존재하는지 체크
     * @param params
     * @param principal
     * @return
     */
    @ResponseBody
    @PostMapping(Url.MAIN.FRIENDS_EXIST_CHECK)
    public Map<String, Object> friendsExistCheck(@RequestBody Map<String, Object> params, Principal principal){
        return mainService.friendsExistCheck(params, principal);
    };

    /**
     * 플래그 업데이트
     * @param params
     * @param principal
     * @return
     */
    @ResponseBody
    @PostMapping(Url.MAIN.RESPONSE_YN_UPDATE)
    public Long responseYnUpdate(@RequestBody Map<String, Object> params, Principal principal){
        return mainService.responseYnUpdate(params, principal);
    };

    /**
     * 친구검색
     * @param friends
     * @param principal
     * @return
     */
    @ResponseBody
    @PostMapping(Url.MAIN.SEARCH_FRIENDS)
    public List<Friends> searchFriends(@RequestBody Friends friends, Principal principal){
        Users users = usersRepository.findByUserIdAndUseYn(principal.getName(), "Y");
        return friendsRepository.selectFriendsList(friends.getFriendsName(), users.getUserIdx());
    };

    /**
     * 친구 삭제처리
     * @param friends
     */
    @ResponseBody
    @PostMapping(Url.MAIN.BLOCK_FRIENDS_DELETE)
    public void blockFriendsDelete(@RequestBody Friends friends){
        mainService.blockFriendsDelete(friends);
    };

    /**
     * 회원 탈퇴
     * @param params
     */
    @ResponseBody
    @PostMapping(Url.MAIN.WITHDRAWAL)
    public void withdrawal(@RequestBody Map<String, Object> params){
        usersService.withdrawal(params);
    };

    /**
     * 채팅
     */
    @GetMapping(Url.MAIN.CHAT+"/{chatRoomIdx}")
    public String chat(HttpServletRequest req, Principal principal, Model model, @PathVariable("chatRoomIdx") Long chatRoomIdx){
        System.out.println("chatRoomIdx : " + chatRoomIdx);
        System.out.println("머야 : " + req.getParameter("friendsName"));

        model.addAttribute("chatRoomIdx", chatRoomIdx);

        Users users = usersRepository.findByUserIdAndUseYn(principal.getName(), "Y");
        model.addAttribute("chatUserIdx", users.getUserIdx());
        model.addAttribute("chatUserName", users.getNickName());

        //방이름 설정
        ChatRoom chatRoom =  chatRoomRepository.findByChatRoomIdx(chatRoomIdx);
        chatRoom.setRoomName(req.getParameter("friendsName"));
        chatRoomRepository.save(chatRoom);

        model.addAttribute("chatRoomName", chatRoom.getRoomName());

        return Url.MAIN.CHAT_HTML;
    }

    /**
     * 채팅 메시지 돌려주기
     * @param chatRoomContent
     * @return
     */
    @ResponseBody
    @PostMapping(Url.MAIN.GET_CHAT_MESSAGE)
    public List<Map<String, Object>> selectChatList(@RequestBody ChatRoomContent chatRoomContent){
        return chatRoomContentRepository.findAllByChatRoomIdx(chatRoomContent.getChatRoomIdx());
    }

    /**
     * 푸시 저장하기
     * @param params
     * @param principal
     */
    @ResponseBody
    @PostMapping(Url.MAIN.SEND_CHAT_PUSH)
    public void getChatPushToken(@RequestBody Map<String, Object> params, Principal principal){
        System.out.println(params.get("chatRoomIdx"));
        pushService.savePush(params, principal);

    }


    /**
     * 채팅방 생성 (메시지 보내기시)
     * @param params
     * @param req
     * @param principal
     * @param model
     * @return
     */
    @PostMapping(Url.MAIN.MAKE_CHAT_ROOM)
    @ResponseBody
    public Long makeChatRoom(@RequestBody Map<String, Object> params, HttpServletRequest req, Principal principal, Model model){

        //개인 톡일경우~~~~
        System.out.println("chatRoomIdx : " + (String) params.get("chatRoomIdx"));
        System.out.println("multipleYn : " + (String) params.get("multiYn"));

        //생성된 룸번호가 없을경우 방생성
        //multiYn N: 개인 Y: 단톡
        if( ((StringUtils.isStringEmpty((String) params.get("chatRoomIdx"))))
                && "N".equals((String) params.get("multiYn"))){

            Long friendsIdx = Long.parseLong( (String) params.get("friendsIdx"));
            Users users = usersRepository.findByUserIdAndUseYn(principal.getName(), "Y");

            ChatRoom chatRoom = new ChatRoom();
            chatRoom.setMultiYn("N");

            //방생성
            Long chatRoomIdx = chatService.saveChatRoom(chatRoom);

            System.out.println("chatRoomIdx : " + chatRoomIdx);

            System.out.println("friendsIdx :" + friendsIdx);
            System.out.println("users.getUserIdx() :" + users.getUserIdx());

            //채팅방 번호 idx 업데이트
            Friends friends = friendsRepository.findByFriendsIdxAndUserIdxAndUseYn(friendsIdx, users.getUserIdx(), "Y");

            if(friends != null){
                friends.setChatRoomIdx(chatRoomIdx.toString());
                friendsRepository.save(friends);
            }

            Friends friends2 = friendsRepository.findByFriendsIdxAndUserIdxAndUseYn(users.getUserIdx(), friendsIdx, "Y");

            if(friends2 != null){
                friends2.setChatRoomIdx(chatRoomIdx.toString());
                friendsRepository.save(friends2);
            }

            //친구 방생성
            ChatRoomDetail chatRoomDetail = new ChatRoomDetail();
            chatRoomDetail.setChatRoomIdx(chatRoomIdx);
            chatRoomDetail.setUserIdx(friendsIdx);
            chatRoomDetailRepository.save(chatRoomDetail);

            //내 방 생성
            ChatRoomDetail chatRoomDetail2 = new ChatRoomDetail();
            chatRoomDetail2.setChatRoomIdx(chatRoomIdx);
            chatRoomDetail2.setUserIdx(users.getUserIdx());
            chatRoomDetailRepository.save(chatRoomDetail2);

            return chatRoomIdx;
        }

        //방번호가 있음
        else{
            Long chatRoomIdx = Long.parseLong((String) params.get("chatRoomIdx"));
            return chatRoomIdx;
        }
    }

    /**
     * 채팅보내기
     * @param params
     * @param req
     * @param principal
     * @param model
     * @return
     */
    @PostMapping(Url.MAIN.SEND_CHAT_MESSAGE)
    @ResponseBody
    public Long sendChatMessage(@RequestBody Map<String, Object> params, HttpServletRequest req, Principal principal, Model model){
        ChatRoomContent chatRoomContent = new ChatRoomContent();
        chatRoomContent.setChatRoomIdx(Long.parseLong( (String) params.get("chatRoomIdx")  ));
        chatRoomContent.setChatUserIdx(Long.parseLong( (String) params.get("chatUserIdx") ));
        chatRoomContent.setChatContents((String)params.get("chatContents"));

        //넘어오는 파일 url
        List<String> contentFileUrl = (List<String>) params.get("contentFileUrl");

        return chatService.saveChatRoomContent(chatRoomContent, contentFileUrl);
    }


}
