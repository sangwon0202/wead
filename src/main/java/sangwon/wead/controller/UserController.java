package sangwon.wead.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import sangwon.wead.controller.param.LoginParam;
import sangwon.wead.controller.param.UserRegisterParam;
import sangwon.wead.resolover.annotation.Referer;
import sangwon.wead.service.UserService;


import static sangwon.wead.util.AlertPageRedirector.redirectAlertPage;

@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;


    @PostMapping("/login")
    public String login(HttpServletRequest request,
                        @Valid @ModelAttribute LoginParam loginParam,
                        BindingResult bindingResult,
                        @Referer String referer,
                        Model model) {
        // 모두 입력하지 않은 경우
        if(bindingResult.hasErrors()) {
            return redirectAlertPage("아이디와 비밀번호를 모두 입력해주세요.", referer, model);
        }
        // 로그인을 실패한 경우
        if(!userService.login(loginParam.getUserId(), loginParam.getPassword())) {
            return redirectAlertPage("아이디와 비밀번호를 확인해주세요.", referer, model);
        }
        // 로그인 성공
        request.getSession().setAttribute("userId", loginParam.getUserId());
        return "redirect:/";
    }

    @PostMapping("/logout")
    public String logout(HttpServletRequest request) {
        // 로그아웃 세션 처리
        request.getSession().removeAttribute("userId");
        return "redirect:/";
    }

    @GetMapping("/users/register")
    public String registerForm(Model model) {
        // 회원가입 폼 전송
        model.addAttribute(new UserRegisterParam());
        return "page/register";
    }

    @PostMapping("/users/register")
    public String register(@Valid @ModelAttribute UserRegisterParam userRegisterParam,
                           BindingResult bindingResult,
                           Model model) {
        if(userService.checkUserIdDuplication(userRegisterParam.getUserId())) {
            bindingResult.rejectValue("userId", "duplication", "아이디가 중복되었습니다.");
        }
        if(bindingResult.hasErrors()) {
            model.addAttribute("userRegisterParam", userRegisterParam);
            return "page/register";
        }

        userService.register(userRegisterParam.toUserRegisterForm());
        return redirectAlertPage("회원가입에 성공하였습니다.", "/", model);
    }
}
