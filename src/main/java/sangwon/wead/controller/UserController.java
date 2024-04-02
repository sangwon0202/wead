package sangwon.wead.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import sangwon.wead.DTO.LoginForm;
import sangwon.wead.DTO.PageBar;
import sangwon.wead.DTO.UserRegisterForm;
import sangwon.wead.exception.UserIdDuplicateException;
import sangwon.wead.service.UserService;

@Controller
@RequiredArgsConstructor
public class UserController {


    private final UserService userService;

    @PostMapping("/login")
    public String login(HttpServletRequest request,
                        @ModelAttribute LoginForm loginForm,
                        Model model) {

        HttpSession session = request.getSession();

        // 이미 로그인되어 있을 경우
        if(session.getAttribute("userId") != null) {
            model.addAttribute("message", "이미 로그인되어 있습니다.");
            return "alert";
        }

        // 로그인을 실패한 경우
        if(!userService.login(loginForm)) {
            model.addAttribute("message", "아이디와 비밀번호를 확인해주세요.");
            return "alert";
        }

        session.setAttribute("userId", loginForm.getUserId());
        return "redirect:/";
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
                           @ModelAttribute UserRegisterForm userRegisterForm,
                           Model model) {

        HttpSession session = request.getSession();

        // 이미 로그인되어 있을 경우
        if(session.getAttribute("userId") != null) {
            model.addAttribute("message", "로그아웃 후 회원가입을 진행해주세요.");
            return "alert";
        }

        try {
            userService.register(userRegisterForm);
            model.addAttribute("message", "회원가입에 성공하였습니다.");
            return "alert";
        }
        catch(UserIdDuplicateException e) {
            model.addAttribute("message", "아이디가 중복되었습니다.");
            model.addAttribute("redirect", "/register");
            return "alert";
        }

    }

}
