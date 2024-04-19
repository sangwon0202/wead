package sangwon.wead.service;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sangwon.wead.service.DTO.UserDto;
import sangwon.wead.service.DTO.UserRegisterDto;
import sangwon.wead.repository.entity.User;
import sangwon.wead.repository.UserRepository;
import sangwon.wead.service.exception.LoginFailException;
import sangwon.wead.service.exception.NonExistentUserException;
import sangwon.wead.service.exception.UserIdDuplicateException;


@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public UserDto getUser(String userId) {
        return userRepository.findById(userId)
                .map(UserDto::new)
                .orElseThrow(NonExistentUserException::new);
    }

    public void login(String userId, String password) {
        User user = userRepository.findById(userId)
                .orElseThrow(LoginFailException::new);
        if(!user.getPassword().equals(password))
            throw new LoginFailException();
    }

    public void register(UserRegisterDto userRegisterDto) {
        if(!userRepository.existsById(userRegisterDto.getUserId()))
            throw new UserIdDuplicateException();
        User user = User.builder()
                .userId(userRegisterDto.getUserId())
                .password(userRegisterDto.getPassword())
                .nickname(userRegisterDto.getNickname())
                .build();
        userRepository.save(user);
    }

}
