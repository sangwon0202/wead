package sangwon.wead.service;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sangwon.wead.DTO.RegisterFormDto;
import sangwon.wead.DTO.UserInfoDto;
import sangwon.wead.entity.User;
import sangwon.wead.exception.DuplicateUserIdException;
import sangwon.wead.exception.LoginFailedException;
import sangwon.wead.repository.UserRepository;

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

    public void login(String userId, String password) throws LoginFailedException {
        User user = userRepository.findByUserId(userId).orElseThrow(() -> new LoginFailedException());
        if(!user.getPassword().equals(password)) throw new LoginFailedException();
    }

    public void register(RegisterFormDto registerFormDto) throws DuplicateUserIdException {

        if(userRepository.findByUserId(registerFormDto.getUserId()).isPresent()) throw new DuplicateUserIdException();

        User user = new User();
        user.setUserId(registerFormDto.getUserId());
        user.setPassword(registerFormDto.getPassword());
        user.setNickname(registerFormDto.getNickname());
        userRepository.save(user);

    }

}
