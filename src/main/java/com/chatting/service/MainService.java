package com.chatting.service;

import com.chatting.dto.UsersDto;
import com.chatting.entity.Friends;
import com.chatting.entity.FriendsInvite;
import com.chatting.entity.Users;
import com.chatting.repository.FriendsInviteRepository;
import com.chatting.repository.FriendsRepository;
import com.chatting.repository.UsersRepository;
import groovy.util.logging.Slf4j;
import lombok.RequiredArgsConstructor;
import org.codehaus.groovy.transform.SourceURIASTTransformation;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.security.Principal;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class MainService {

    private final FriendsInviteRepository friendsInviteRepository;
    private final UsersRepository usersRepository;
    private final FriendsRepository friendsRepository;

    /**
     * 친구추가 요청
     * @param friendsInvite
     * @return
     */
    public Long saveFriendsInvite(FriendsInvite friendsInvite){

        FriendsInvite friendsInvite1 = friendsInviteRepository.findByRequestUserIdAndResponseUserId(friendsInvite.getRequestUserId(), friendsInvite.getResponseUserId());

        if(friendsInvite1 != null){
            //동일한 사람에게 보낸건 있으면 삭제처리
            friendsInviteRepository.delete(friendsInvite1);
        }

        //저장
        return friendsInviteRepository.save(friendsInvite).getFriendsIdx();
    }

    /**
     * 친구요청 받는사람이 실제로 존재하는지 체크
     * @param friendsInvite
     * @return
     */
    public Map<String, Object> friendsExistCheck(FriendsInvite friendsInvite, Principal principal){

        Map<String, Object> result = new HashMap<String, Object>();
        result.put("resultCode", "success");
        Users responseUser = usersRepository.findByUserIdAndUseYn(friendsInvite.getResponseUserId(), "Y");

        //해당 사용자가없으면 fail로 return
        if(responseUser == null){
            result.put("resultCode", "fail");
        }

        //요청 받는사람이 이미 친구인지 체크
        Friends friendCheck = friendsRepository.findByFriendsIdAndUserId(friendsInvite.getResponseUserId(), principal.getName());

        //친구이면
        if(friendCheck != null){
            result.put("resultCode", "friends");
        }

        return result;
    }

    // 플래그 업데이트
    public Long responseYnUpdate(FriendsInvite friendsInvite, Principal principal){

        FriendsInvite friendsInvite1 = friendsInviteRepository.findByFriendsIdx(friendsInvite.getFriendsIdx());
        friendsInvite1.setResponseYn(friendsInvite.getResponseYn());

        //친구 승낙일때 친구테이블에 데이터 넣기
        if("Y".equals(friendsInvite.getResponseYn())){
            Friends friends = new Friends();
            friends.setUserId(principal.getName());

            String friendsId = "";

            if(friendsInvite.getRequestUserId() != principal.getName()){
                friendsId = friendsInvite.getRequestUserId();
            }

            else if(friendsInvite.getResponseUserId() != principal.getName()){
                friendsId = friendsInvite.getResponseUserId();
            }

            friends.setFriendsId(friendsId);
            friendsRepository.save(friends).getFriendsIdx();
        }

        return friendsInviteRepository.save(friendsInvite1).getFriendsIdx();
    }

}
