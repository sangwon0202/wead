package sangwon.wead.service;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sangwon.wead.DTO.LoginResultDto;
import sangwon.wead.entity.User;
import sangwon.wead.repository.UserRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public LoginResultDto login(String userId, String password) {

        LoginResultDto loginResultDto = new LoginResultDto();
        Optional<User> optionalUser = userRepository.findByUserId(userId);

        if(!optionalUser.isPresent()) {
            loginResultDto.setResult(false);
            loginResultDto.setMessage("존재하지 않는 아이디입니다.");
            return loginResultDto;
        }

        User user = optionalUser.get();
        if(!user.getPassword().equals(password)) {
            loginResultDto.setResult(false);
            loginResultDto.setMessage("비밀번호를 확인해주세요.");
            return loginResultDto;
        }

        loginResultDto.setResult(true);
        return loginResultDto;
    }

    public String getNicknameByUserId(String userId) {
        Optional<User> optionalUser = userRepository.findByUserId(userId);
        if(optionalUser.isPresent()) return optionalUser.get().getNickname();
        else return "NULL";
    }
}
