package sangwon.wead.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import sangwon.wead.DTO.RegisterDto;
import sangwon.wead.exception.DuplicateUserIdException;
import sangwon.wead.exception.LoginFailedException;
import sangwon.wead.requestParam.RegisterRequestParam;
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

        // 아이디를 입력하지 않은 경우
        if(userId.isEmpty()) {
            model.addAttribute("message", "아이디를 입력해주세요.");
            return "alert";
        }

        // 비밀번호를 입력하지 않은 경우
        if(password.isEmpty()) {
            model.addAttribute("message", "비밀번호를 입력해주세요.");
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
                           @Valid @ModelAttribute RegisterRequestParam registerRequestParam,
                           BindingResult bindingResult,
                           Model model) {

        HttpSession session = request.getSession();

        // 이미 로그인되어 있을 경우
        if(session.getAttribute("userId") != null) {
            model.addAttribute("message", "로그아웃 후 회원가입을 진행해주세요.");
            return "alert";
        }

        // registerRequestParam 유효성 검사
        if(bindingResult.hasErrors()) {
            model.addAttribute("message", bindingResult.getFieldError().getDefaultMessage());
            model.addAttribute("redirect", "/register");
            return "alert";
        }

        try {
            RegisterDto registerDto = toRegisterDto(registerRequestParam);
            userService.register(registerDto);
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

    private static RegisterDto toRegisterDto(RegisterRequestParam registerRequestParam) {
        RegisterDto registerDto = new RegisterDto();
        registerDto.setUserId(registerRequestParam.getUserId());
        registerDto.setPassword(registerRequestParam.getPassword());
        registerDto.setNickname(registerRequestParam.getNickname());
        return registerDto;
    }

}
