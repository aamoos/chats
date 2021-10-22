package com.chatting.validator;

import com.chatting.dto.UsersDto;
import com.chatting.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
@RequiredArgsConstructor
public class UsersDtoValidator implements Validator {

    private final UsersRepository usersRepository;
    @Override
    public boolean supports(Class<?> aClass) {
        return aClass.isAssignableFrom(UsersDto.class);
    }

    @Override
    public void validate(Object object, Errors errors) {
        UsersDto usersDto = (UsersDto) object;

        //아이디가 중복일경우우
       if(usersRepository.existsByUserIdAndUseYn(usersDto.getUserId(), "Y")){
            errors.rejectValue("userId", "userId",
                    new Object[]{usersDto.getUserId()}, "이미 사용중인 아이디 입니다.");
        }

        //패스워드와 패스워드 확인이 동일하지 않을경우
        if(!usersDto.getPassword().equals(usersDto.getPassword1())){
            errors.rejectValue("password", "password",
                    new Object[]{usersDto.getUserId()}, "패스워드 확인과 동일하지 않습니다.");
        }

    }
}