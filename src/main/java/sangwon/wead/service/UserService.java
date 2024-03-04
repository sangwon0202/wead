package sangwon.wead.service;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sangwon.wead.DTO.RegisterFormDto;
import sangwon.wead.DTO.UserInfoDto;
import sangwon.wead.entity.User;
import sangwon.wead.exception.UserIdDuplicateException;
import sangwon.wead.exception.UserNotExistException;
import sangwon.wead.exception.WrongPasswordException;
import sangwon.wead.repository.UserRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;


    public UserInfoDto getUserInfo(String userId) {
        User user = userRepository.findByUserId(userId).get();
        UserInfoDto userInfoDto = new UserInfoDto();
        userInfoDto.setUserId(user.getUserId());
        userInfoDto.setNickname(user.getNickname());
        return userInfoDto;
    }

    public void login(String userId, String password) throws Exception {

        Optional<User> optionalUser = userRepository.findByUserId(userId);

        // 아이디가 존재하지 않음
        if(!optionalUser.isPresent()) throw new UserNotExistException();

        // 비밀번호가 틀림
        User user = optionalUser.get();
        if(!user.getPassword().equals(password)) throw new WrongPasswordException();

    }

    public void register(RegisterFormDto registerFormDto) throws Exception {

        Optional<User> optionalUser = userRepository.findByUserId(registerFormDto.getUserId());

        // 아이디 중복
        if(!optionalUser.isPresent()) throw new UserIdDuplicateException();

        // 회원가입
        User user = new User();
        user.setUserId(registerFormDto.getUserId());
        user.setPassword(registerFormDto.getPassword());
        user.setNickname(registerFormDto.getNickname());
        userRepository.save(user);

    }

}
