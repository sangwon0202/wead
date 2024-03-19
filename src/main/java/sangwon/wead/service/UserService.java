package sangwon.wead.service;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sangwon.wead.exception.NonexistentUserException;
import sangwon.wead.service.DTO.RegisterDto;
import sangwon.wead.service.DTO.UserDto;
import sangwon.wead.repository.entity.User;
import sangwon.wead.exception.DuplicateUserIdException;
import sangwon.wead.repository.UserRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public UserDto getUser(String userId) throws NonexistentUserException {
        User user = userRepository.findByUserId(userId).orElseThrow(() -> new NonexistentUserException());
        return new UserDto(user);
    }

    public boolean login(String userId, String password) {
        Optional<User> optionalUser = userRepository.findByUserId(userId);
        if(optionalUser.isEmpty()) return false;
        else {
            User user = optionalUser.get();
            return user.getPassword().equals(password);
        }
    }

    public void register(RegisterDto registerDto) throws DuplicateUserIdException {
        if(userRepository.findByUserId(registerDto.getUserId()).isPresent()) throw new DuplicateUserIdException();
        userRepository.save(registerDto.toUser());
    }

}
