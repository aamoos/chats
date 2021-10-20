package com.chatting.service;

import com.chatting.entity.SelfAuth;
import com.chatting.entity.Users;
import com.chatting.repository.SelfAuthRepository;
import com.chatting.repository.UsersRepository;
import groovy.util.logging.Slf4j;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class UsersService {

    private final SelfAuthRepository selfAuthRepository;
    private final UsersRepository usersRepository;

    /**
     * 인증번호 저장
     * @param selfAuth
     * @return
     */
    public Long saveSerialNo(SelfAuth selfAuth){
        return selfAuthRepository.save(selfAuth).getSelfIdx();
    }

    /**
     * 인증번호 삭제
     * @param userId
     */
    public void deleteSerialNo(String userId){
        selfAuthRepository.deleteByUserId(userId);
    }

    /**
     * 인증번호 체크
     * @param selfAuth
     */
    public Map<String, Object> serialNoCheck(SelfAuth selfAuth, HttpSession session){
        Map<String, Object> result = new HashMap<String, Object>();
        SelfAuth self = selfAuthRepository.findByUserIdAndSerialNo(selfAuth.getUserId(), selfAuth.getSerialNo());

        //인증번호가 틀릴경우
        if(self == null){
            result.put("resultCode", "fail");
            result.put("resultMsg", "인증번호를 확인해주세요.");
        }

        //인증번호 맞을경우
        else{

            //회원 등록 처리
            String userId = ((Users) session.getAttribute("users")).getUserId();
            String password = ((Users) session.getAttribute("users")).getPassword();
            String handPhoneNo = ((Users) session.getAttribute("users")).getHandPhoneNo();
            String userName = ((Users) session.getAttribute("users")).getUserName();

            Users users = new Users(userId, password, handPhoneNo, userName, "Y");
            //회원 등록
            usersRepository.save(users);

            result.put("resultCode", "success");
            result.put("resultMsg", "회원이 정상적으로 등록되었습니다.");
        }

        return result;
    }

}
