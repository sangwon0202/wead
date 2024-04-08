package sangwon.wead.service;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sangwon.wead.exception.NonexistentUserException;
import sangwon.wead.service.DTO.UserRegisterForm;
import sangwon.wead.service.DTO.UserInfo;
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

    public boolean login(String userId, String password) {
        if(userRepository.existsById(userId)) {
            User user = userRepository.findById(userId).get();
            return user.getPassword().equals(password);
        }
        else return false;
    }

    public boolean checkUserIdDuplication(String userId) {
        return userRepository.existsById(userId);
    }

    public void register(UserRegisterForm userRegisterForm) {
        if(checkUserIdDuplication(userRegisterForm.getUserId())) throw new UserIdDuplicateException();
        User user = User.builder()
                .id(userRegisterForm.getUserId())
                .password(userRegisterForm.getPassword())
                .nickname(userRegisterForm.getNickname())
                .build();
        userRepository.save(user);
    }
}
