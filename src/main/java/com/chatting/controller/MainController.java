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

    private final UsersRepository usersRepository;
    private final UsersService usersService;
    private final MainService mainService;
    private final FriendsInviteRepository friendsInviteRepository;
    private final FriendsRepository friendsRepository;
    private final ChatService chatService;
    private final ChatRoomContentRepository chatRoomContentRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final PushService pushService;

    @GetMapping("/")
    public void root(HttpServletRequest req, HttpServletResponse res, Principal principal) throws Exception {

        if(principal != null){

            String token = req.getParameter("token");

            //토큰값 업데이트
            if(token != null){
                Users users = usersRepository.findByUserId(principal.getName());
                users.setToken(token);
                usersRepository.save(users);
            }


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
        List<FriendsInvite> friendsInvite = friendsInviteRepository.selectReceiveHistory(principal.getName());

        //친구 리스트
        List<Friends> friends = friendsRepository.selectReciveNotification(principal.getName());

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
    public List<ChatRoomContent> getChatRoom(@RequestBody Map<String, Object> params, Principal principal){

        System.out.println(params.get("roomName"));
        System.out.println(principal.getName());

        //내 채팅방 리스트
        List<ChatRoomContent> chattingRoom = chatRoomContentRepository.selectChattingRoom(principal.getName(), (String) params.get("roomName"));

        List<ChatRoomContent> newChattingRoom = new ArrayList<ChatRoomContent>();
        List<Long> idArray = new ArrayList<Long>();

        //중복제거
        for(int i=0; i<chattingRoom.size(); i++){
            if(!idArray.contains(chattingRoom.get(i).getChatRoomIdx())){
                idArray.add(chattingRoom.get(i).getChatRoomIdx());
                newChattingRoom.add(chattingRoom.get(i));
            }
        }

        return newChattingRoom;
    };

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
     * 친구검색
     * @param friends
     * @param principal
     * @return
     */
    @ResponseBody
    @PostMapping(Url.MAIN.SEARCH_FRIENDS)
    public List<Friends> searchFriends(@RequestBody Friends friends, Principal principal){
        return friendsRepository.selectFriendsList(friends.getFriendsName(), principal.getName());
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
     * 화원탈퇴
     * @param friends
     */
    @ResponseBody
    @PostMapping(Url.MAIN.WITHDRAWAL)
    public void withdrawal(@RequestBody Friends friends){
        usersService.withdrawal(friends.getUserId());
    };



    /**
     * 채팅
     */
    @GetMapping(Url.MAIN.CHAT+"/{chatRoomIdx}")
    public String chat(HttpServletRequest req, Principal principal, Model model, @PathVariable("chatRoomIdx") Long chatRoomIdx){
        System.out.println("chatRoomIdx : " + chatRoomIdx);

        model.addAttribute("chatRoomIdx", chatRoomIdx);
        model.addAttribute("chatUserId", principal.getName());

        Users users = usersRepository.findByUserId(principal.getName());
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
    public List<ChatRoomContent> selectChatList(@RequestBody ChatRoomContent chatRoomContent){
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
        if( ((StringUtils.isStringEmpty((String) params.get("chatRoomIdx"))))
                && "N".equals((String) params.get("multiYn"))){

            System.out.println("타니????????????????");

            String friendsId = (String) params.get("friendsId");
            String userId = principal.getName();

            ChatRoom chatRoom = new ChatRoom();

            chatRoom.setMultiYn("N");
            //방생성
            Long chatRoomIdx = chatService.saveChatRoom(chatRoom);

            System.out.println("chatRoomIdx : " + chatRoomIdx);

            //채팅방 번호 idx 업데이트
            Friends friends = friendsRepository.findByFriendsIdAndUserId(friendsId, principal.getName());
            friends.setChatRoomIdx(chatRoomIdx.toString());
            friendsRepository.save(friends);

            Friends friends2 = friendsRepository.findByFriendsIdAndUserId(principal.getName(), friendsId);
            friends2.setChatRoomIdx(chatRoomIdx.toString());
            friendsRepository.save(friends2);
            return chatRoomIdx;
        }

        //방번호가 있음
        else{
            Long chatRoomIdx = Long.parseLong((String) params.get("chatRoomIdx"));
            return chatRoomIdx;
        }
    }

    /**
     * 채팅 보내기
     * @param chatRoomContent
     * @param req
     * @param principal
     * @param model
     * @return
     */
    @PostMapping(Url.MAIN.SEND_CHAT_MESSAGE)
    @ResponseBody
    public Long sendChatMessage(@RequestBody ChatRoomContent chatRoomContent, HttpServletRequest req, Principal principal, Model model){
        System.out.println(chatRoomContent.getChatRoomIdx());
        System.out.println(chatRoomContent.getChatUserId());
        System.out.println(chatRoomContent.getChatContents());
        return chatService.saveChatRoomContent(chatRoomContent);
    }


}
