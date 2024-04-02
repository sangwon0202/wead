package sangwon.wead.service;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sangwon.wead.DTO.LoginForm;
import sangwon.wead.exception.NonexistentUserException;
import sangwon.wead.DTO.UserRegisterForm;
import sangwon.wead.DTO.UserInfo;
import sangwon.wead.exception.UserIdDuplicateException;
import sangwon.wead.repository.entity.User;
import sangwon.wead.repository.UserRepository;


@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public UserInfo getUserInfo(String userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NonexistentUserException());
        return new UserInfo(user);
    }

    public boolean login(LoginForm loginForm) {
        if(userRepository.existsById(loginForm.getUserId())) {
            User user = userRepository.findById(loginForm.getUserId()).get();
            return user.getPassword().equals(loginForm.getPassword());
        }
        else return false;
    }

    public void register(UserRegisterForm userRegisterForm) {
        if(userRepository.existsById(userRegisterForm.getUserId())) throw new UserIdDuplicateException();
        User user = User.builder()
                .id(userRegisterForm.getUserId())
                .password(userRegisterForm.getPassword())
                .nickname(userRegisterForm.getNickname())
                .build();
        userRepository.save(user);
    }
}
