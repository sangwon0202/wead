package sangwon.wead.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import sangwon.wead.DTO.RegisterFormDto;
import sangwon.wead.exception.DuplicateUserIdException;
import sangwon.wead.exception.LoginFailedException;
import sangwon.wead.service.UserService;

@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/login")
    public String login(HttpServletRequest request,
                        @RequestParam String userId,
                        @RequestParam String password,
                        Model model) {

        HttpSession session = request.getSession();

        // 이미 로그인되어 있을 경우
        if(session.getAttribute("userId") != null) {
            model.addAttribute("message", "이미 로그인되어 있습니다.");
            return "alert";
        }

        try {
            userService.login(userId, password);
            session.setAttribute("userId", userId);
            return "redirect:/";
        }
        // 로그인 실패
        catch (LoginFailedException e) {
            model.addAttribute("message", "아이디와 비밀번호를 확인해주세요.");
            return "alert";
        }

    }

    @PostMapping("/logout")
    public String logout(HttpServletRequest request, Model model) {
        HttpSession session = request.getSession();

        // 로그인되어 있지 않는 경우
        if(session.getAttribute("userId") == null) {
            model.addAttribute("message", "로그인되어 있지 않습니다.");
            return "alert";
        }

        // 로그아웃
        session.removeAttribute("userId");
        return "redirect:/";
    }

    @GetMapping("/register")
    public String registerForm(HttpServletRequest request, Model model) {
        HttpSession session = request.getSession();

        // 이미 로그인되어 있을 경우
        if(session.getAttribute("userId") != null) {
            model.addAttribute("message", "로그아웃 후 회원가입을 진행해주세요.");
            return "alert";
        }

        // 회원가입 폼 전송
        return "page/register";
    }

    @PostMapping("/register")
    public String register(HttpServletRequest request,
                           @RequestParam String userId,
                           @RequestParam String password,
                           @RequestParam String nickname,
                        Model model) {

        HttpSession session = request.getSession();

        // 이미 로그인되어 있을 경우
        if(session.getAttribute("userId") != null) {
            model.addAttribute("message", "로그아웃 후 회원가입을 진행해주세요.");
            return "alert";
        }

        // 빈칸이 있을 경우 <- @valid 도입하면 삭제할 예정
        if(userId.equals("") || password.equals("") || nickname.equals("")) {
            model.addAttribute("message", "모든 빈칸을 채워주세요.");
            model.addAttribute("redirect", "/register");
            return "alert";
        }

        try {
            RegisterFormDto registerFormDto = new RegisterFormDto();
            registerFormDto.setUserId(userId);
            registerFormDto.setPassword(password);
            registerFormDto.setNickname(nickname);
            userService.register(registerFormDto);
            model.addAttribute("message", "회원가입에 성공하였습니다!");
            return "alert";
        }
        // 아이디가 중복될 경우
        catch (DuplicateUserIdException e) {
            model.addAttribute("message", "이미 존재하는 아이디입니다.");
            model.addAttribute("redirect", "/register");
            return "alert";
        }

    }

}
