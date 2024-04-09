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
import sangwon.wead.controller.DTO.LoginParam;
import sangwon.wead.controller.DTO.UserRegisterParam;
import sangwon.wead.exception.client.AlreadyLoginException;
import sangwon.wead.exception.client.NotLoginException;
import sangwon.wead.service.UserService;

import static sangwon.wead.controller.util.AlertPageRedirector.redirectAlertPage;

@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;


    @PostMapping("/login")
    public String login(HttpServletRequest request,
                        @Valid @ModelAttribute LoginParam loginParam,
                        Model model) {

        // 로그인 정보
        HttpSession session = request.getSession();
        String userId = (String)session.getAttribute("userId");

        // 이전 URL
        String referer = request.getHeader("referer");

        // 이미 로그인되어 있을 경우
        if(userId != null) throw new AlreadyLoginException();

        // 로그인을 실패한 경우
        if(!userService.login(loginParam.getUserId(), loginParam.getPassword())) {
            return redirectAlertPage("아이디와 비밀번호를 확인해주세요.", referer, model);
        }

        // 로그인 성공
        session.setAttribute("userId", loginParam.getUserId());
        return "redirect:" + referer;
    }

    @PostMapping("/logout")
    public String logout(HttpServletRequest request) {

        // 로그인 정보
        HttpSession session = request.getSession();
        String userId = (String)session.getAttribute("userId");

        // 이미 로그인되어 있을 경우
        if(userId != null) throw new AlreadyLoginException();

        // 로그인되어 있지 않는 경우
        if(session.getAttribute("userId") == null) throw new NotLoginException();

        // 로그아웃 세션 처리
        session.removeAttribute("userId");
        String referer = request.getHeader("referer");
        return "redirect:" + referer;
    }

    @GetMapping("/register")
    public String registerForm(HttpServletRequest request, Model model) {

        // 로그인 정보
        HttpSession session = request.getSession();
        String userId = (String)session.getAttribute("userId");

        // 이미 로그인되어 있을 경우
        if(userId != null) throw new AlreadyLoginException();

        // 회원가입 폼 전송
        model.addAttribute(new UserRegisterParam());
        return "page/register";
    }

    @PostMapping("/register")
    public String register(HttpServletRequest request,
                           @Valid @ModelAttribute UserRegisterParam userRegisterParam,
                           BindingResult bindingResult,
                           Model model) {
        // 로그인 정보
        HttpSession session = request.getSession();
        String userId = (String)session.getAttribute("userId");

        // 이전 URL
        String referer = request.getHeader("referer");

        // 이미 로그인되어 있을 경우
        if(userId != null) throw new AlreadyLoginException();

        // 아이디 중복 검사
        if(userService.checkUserIdDuplication(userRegisterParam.getUserId())) {
            return redirectAlertPage("아이디가 중복되었습니다.", referer, model);
        }

        // 모든 값을 입력하지 않은 경우
        if(bindingResult.hasErrors()) {
            return redirectAlertPage("모든 값을 입력해주세요.", referer, model);
        }

        userService.register(userRegisterParam.toUserRegisterForm());
        return redirectAlertPage("회원가입에 성공하였습니다.", referer, model);

    }

}
