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
import org.apache.catalina.User;
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
     * 친구 추가
     * @param params
     * @return
     */
    public Long saveFriendsInvite(Map<String, Object> params){

        Users responseUser = usersRepository.findByUserIdAndUseYn( (String) params.get("responseUserId"), "Y");
        Users requestUser = usersRepository.findByUserIdAndUseYn( (String) params.get("requestUserId"), "Y");

        FriendsInvite friendsInvite = friendsInviteRepository.findByResponseUserIdx(responseUser.getUserIdx());

        if(friendsInvite != null){
            //동일한 사람에게 보낸건 있으면 삭제처리
            friendsInviteRepository.delete(friendsInvite);
        }

        System.out.println("-=-----------------");
        System.out.println(responseUser.getUserIdx());
        System.out.println(requestUser.getUserIdx());

        FriendsInvite friendsInvite1 = new FriendsInvite();

        //요청자 idx
        friendsInvite1.setResponseUserIdx(responseUser.getUserIdx());

        //받는사람 idx
        friendsInvite1.setRequestUserIdx(requestUser.getUserIdx());

        friendsInvite1.setMessage((String) params.get("message"));

        //저장
        return friendsInviteRepository.save(friendsInvite1).getIdx();
    }

    /**
     * 친구요청 받는사람이 실제로 존재하는지 체크
     * @param params
     * @param principal
     * @return
     */
    public Map<String, Object> friendsExistCheck(Map<String, Object> params, Principal principal){

        Map<String, Object> result = new HashMap<String, Object>();
        result.put("resultCode", "success");
        Users responseUser = usersRepository.findByUserIdAndUseYn((String) params.get("responseUserId"), "Y");

        //해당 사용자가없으면 fail로 return
        if(responseUser == null){
            result.put("resultCode", "fail");
        }

        //해당 사용자가 존재하면
        else{

            Users me = usersRepository.findByUserIdAndUseYn(principal.getName(), "Y");

            //요청 받는사람이 이미 친구인지 체크
            Friends friendCheck = friendsRepository.findByFriendsIdxAndUserIdxAndUseYn(responseUser.getUserIdx(), me.getUserIdx(), "Y");

            //친구이면
            if(friendCheck != null){
                result.put("resultCode", "friends");
            }

        }

        return result;
    }

    // 플래그 업데이트
    public Long responseYnUpdate(Map<String, Object> params, Principal principal){

        Long idx = Long.parseLong((String) params.get("idx"));

        //초대장 정보 조회
        FriendsInvite friendsInvite = friendsInviteRepository.findByIdx(idx);

        //승낙여부 설정
        friendsInvite.setResponseYn( (String) params.get("responseYn") );

        //친구 승낙일때 친구테이블에 데이터 넣기
        if("Y".equals(friendsInvite.getResponseYn())){

            //내정보 조회
            Users me = usersRepository.findByUserIdAndUseYn(principal.getName(), "Y");

            Friends friends = new Friends();
            friends.setUserIdx(me.getUserIdx());

            long friendsIdx = 0;

            if(friendsInvite.getRequestUserIdx() != me.getUserIdx()){
                friendsIdx = friendsInvite.getRequestUserIdx();
            }

            else if(friendsInvite.getResponseUserIdx() != me.getUserIdx()){
                friendsIdx = friendsInvite.getResponseUserIdx();
            }

            friends.setFriendsIdx(friendsIdx);
            friends.setUseYn("Y");
            friendsRepository.save(friends).getIdx();

            //반대도 넣어줘야함
            Friends friends1 = new Friends();
            friends1.setUserIdx(friendsIdx);
            friends1.setFriendsIdx(me.getUserIdx());
            friends1.setUseYn("Y");
            friendsRepository.save(friends1).getIdx();
        }

        return friendsInviteRepository.save(friendsInvite).getIdx();
    }

    //친구 블락처리
    public void blockFriendsDelete(Friends friends){
        Friends friends1 = friendsRepository.findByIdx(friends.getIdx());
        friendsRepository.delete(friends1);
    }

}
