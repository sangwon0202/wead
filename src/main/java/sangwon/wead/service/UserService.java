package sangwon.wead.service;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sangwon.wead.repository.CommentRepository;
import sangwon.wead.repository.PostRepository;
import sangwon.wead.service.DTO.UserDetailDto;
import sangwon.wead.service.DTO.UserDto;
import sangwon.wead.service.DTO.UserRegisterDto;
import sangwon.wead.repository.entity.User;
import sangwon.wead.repository.UserRepository;
import sangwon.wead.exception.LoginFailException;
import sangwon.wead.exception.NonExistentUserException;
import sangwon.wead.exception.UserIdDuplicateException;



@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;

    public UserDto getUser(String userId) {
        return userRepository.findById(userId)
                .map(UserDto::new)
                .orElseThrow(NonExistentUserException::new);
    }

    public UserDetailDto getUserDetail(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(NonExistentUserException::new);
        int postCount = postRepository.countByUserUserId(userId);
        int commentCount = commentRepository.countByUserUserId(userId);
        return new UserDetailDto(user, postCount, commentCount);
    }

    public void login(String userId, String password) {
        User user = userRepository.findById(userId)
                .orElseThrow(LoginFailException::new);
        if(!user.getPassword().equals(password))
            throw new LoginFailException();
    }

    public void register(UserRegisterDto userRegisterDto) {
        if(userRepository.existsById(userRegisterDto.getUserId()))
            throw new UserIdDuplicateException();
        User user = User.builder()
                .userId(userRegisterDto.getUserId())
                .password(userRegisterDto.getPassword())
                .nickname(userRegisterDto.getNickname())
                .build();
        userRepository.save(user);
    }

    public void changePassword(String userId, String password) {
        User user = userRepository.findById(userId)
                .orElseThrow(NonExistentUserException::new);
        user.changePassword(password);
    }

}
