package com.chatting.service;

import com.chatting.dto.IdCheckDto;
import com.chatting.dto.SelfAuthDto;
import com.chatting.dto.UsersDto;
import com.chatting.entity.FriendsInvite;
import com.chatting.entity.SelfAuth;
import com.chatting.entity.Users;
import com.chatting.entity.UsersAuthority;
import com.chatting.repository.FriendsInviteRepository;
import com.chatting.repository.SelfAuthRepository;
import com.chatting.repository.UsersAuthorityRepository;
import com.chatting.repository.UsersRepository;
import groovy.util.logging.Slf4j;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;

import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class UsersService {

    private final SelfAuthRepository selfAuthRepository;
    private final UsersRepository usersRepository;
    private final UsersAuthorityRepository usersAuthorityRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * 인증번호 저장
     * @param SelfAuthDto
     * @return
     */
    public Long saveSerialNo(SelfAuthDto SelfAuthDto){
        return selfAuthRepository.save(SelfAuthDto.toEntity()).getSelfIdx();
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
     * @param SelfAuthDto
     */
    public Map<String, Object> serialNoCheck(SelfAuthDto SelfAuthDto, HttpSession session){
        Map<String, Object> result = new HashMap<String, Object>();
        System.out.println("===================");
        System.out.println(SelfAuthDto.getUserId());
        System.out.println(SelfAuthDto.getSerialNo());
        SelfAuth self = selfAuthRepository.findByUserIdAndSerialNo(SelfAuthDto.getUserId(), SelfAuthDto.getSerialNo());

        //인증번호가 틀릴경우
        if(self == null){
            result.put("resultCode", "fail");
            result.put("resultMsg", "인증번호를 확인해주세요.");
        }

        //인증번호 맞을경우
        else{
            //회원 등록 처리
            String userId = (String) session.getAttribute("userId");
            String password = passwordEncoder.encode((String) session.getAttribute("password"));
            String handPhoneNo = (String) session.getAttribute("handPhoneNo");
            String nickName = (String) session.getAttribute("nickName");
            String token = (String) session.getAttribute("token");

            UsersDto usersDto = new UsersDto(userId, password, handPhoneNo, nickName, "Y", token, 0L);

            //회원 등록
            usersRepository.save(usersDto.toEntity());

            //권한설정
            UsersAuthority userAuthority = new UsersAuthority();
            userAuthority.setUserId(usersDto.getUserId());
            userAuthority.setAuthority("USER");
            usersAuthorityRepository.save(userAuthority);

            result.put("resultCode", "success");
            result.put("resultMsg", "회원이 정상적으로 등록되었습니다.");
        }

        return result;
    }

    /**
     * 중복체크
     * @param usersDto
     * @return
     */
    public Map<String, Object> duplicateCheck(UsersDto usersDto){
        Map<String, Object> result = new HashMap<String, Object>();
        Users selectUser = usersRepository.findByUserIdOrHandPhoneNoAndUseYn(usersDto.getUserId(), usersDto.getHandPhoneNo(), "Y");

        //생성가능
        if(selectUser == null){
            result.put("resultCode", "success");
        }

        //생성불가능
        else{
            result.put("resultCode", "fail");
            result.put("resultMsg", "해당 아이디로 가입한 아이디 또는 핸드폰번호가 있습니다.");
        }

        return result;
    }

    //회원가입시 유효성체크
    public Map<String, String> validateHandling(Errors errors) {
        Map<String, String> validatorResult = new HashMap<>();

        for (FieldError error : errors.getFieldErrors()) {
            String validKeyName = String.format("valid_%s", error.getField());
            System.out.println("validKeyName : " + validKeyName);
            validatorResult.put(validKeyName, error.getDefaultMessage());
        }

        return validatorResult;
    }

    //아이디찾기
    public String findByUserId(IdCheckDto idCheckDto){
        Users users = usersRepository.findByHandPhoneNoAndNickNameAndUseYn(idCheckDto.getHandPhoneNo(), idCheckDto.getUsername(), "Y");
        return users.getUserId();
    }

    /**
     * 비밀번호 초기화
     * @param usersDto
     * @return
     */
    public Long pwdReset(UsersDto usersDto){

        Users users = usersRepository.findByUserIdAndUseYn(usersDto.getUserId(), "Y");

        //패스워드 암호화
        String password = passwordEncoder.encode(users.getUserId());
        users.setPassword(password);

        return usersRepository.save(users).getUserIdx();
    }

    /**
     * 프로필 이미지 저장
     * @param usersDto
     * @return
     */
    public Long saveProfileImg(UsersDto usersDto){
        //프로필 이미지 저장
        Users users = usersRepository.findByUserId(usersDto.getUserId());
        users.setProfileIdx(usersDto.getProfileIdx());
        return usersRepository.save(users).getUserIdx();
    }
}
