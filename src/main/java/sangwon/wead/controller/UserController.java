package sangwon.wead.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import sangwon.wead.controller.DTO.LoginParam;
import sangwon.wead.controller.DTO.UserPasswordParam;
import sangwon.wead.controller.DTO.UserRegisterParam;
import sangwon.wead.exception.NoPermissionException;
import sangwon.wead.resolover.annotation.Referer;
import sangwon.wead.resolover.annotation.UserId;
import sangwon.wead.service.DTO.ListDto;
import sangwon.wead.service.DTO.PostRowDto;
import sangwon.wead.service.DTO.UserDetailDto;
import sangwon.wead.service.ListService;
import sangwon.wead.service.PostService;
import sangwon.wead.service.UserService;
import sangwon.wead.exception.LoginFailException;
import sangwon.wead.exception.UserIdDuplicateException;


import static sangwon.wead.util.AlertPageRedirector.redirectAlertPage;

@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final PostService postService;
    private final ListService listService;


    @PostMapping("/login")
    public String login(HttpServletRequest request,
                        @Valid @ModelAttribute LoginParam loginParam,
                        BindingResult bindingResult,
                        @Referer String referer,
                        Model model) {
        if(bindingResult.hasErrors()) return redirectAlertPage("아이디와 비밀번호를 모두 입력해주세요.", referer, model);
        try {
            userService.login(loginParam.getUserId(), loginParam.getPassword());
            request.getSession().setAttribute("userId", loginParam.getUserId());
            return "redirect:/";
        }
        catch (LoginFailException e) {
            return redirectAlertPage("아이디와 비밀번호를 확인해주세요.", referer, model);
        }
    }

    @PostMapping("/logout")
    public String logout(HttpServletRequest request) {
        request.getSession().removeAttribute("userId");
        return "redirect:/";
    }

    @GetMapping("/users/{userId}")
    public String myPage(@UserId String loginUserId,
                         @PathVariable("userId") String userId,
                         @RequestParam(value = "page", required = false, defaultValue = "1") int pageNumber,
                         Model model) {
        if(!loginUserId.equals(userId)) throw new NoPermissionException();
        UserDetailDto userDetailDto = userService.getUserDetail(userId);
        ListDto<PostRowDto> listDto = listService.builder(pageable -> postService.getPostByUserId(pageable, userId))
                .setUrl("/users/" + userId)
                .build(PageRequest.of(pageNumber, 5, Sort.by("postId").descending()));

        model.addAttribute("user", userDetailDto);
        model.addAttribute("list", listDto.getList());
        model.addAttribute("pageBar", listDto.getPageBar());
        return "page/my_page";
    }

    @GetMapping("/users/register")
    public String registerForm(Model model) {
        model.addAttribute(new UserRegisterParam());
        return "page/register";
    }

    @PostMapping("/users/register")
    public String register(@Valid @ModelAttribute UserRegisterParam userRegisterParam,
                           BindingResult bindingResult,
                           Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("userRegisterParam", userRegisterParam);
            return "page/register";
        }
        try {
            userService.register(userRegisterParam.toUserRegisterForm());
            return redirectAlertPage("회원가입에 성공하였습니다.", "/", model);
        } catch (UserIdDuplicateException e) {
            bindingResult.rejectValue("userId", "duplication", "아이디가 중복되었습니다.");
            model.addAttribute("userRegisterParam", userRegisterParam);
            return "page/register";
        }
    }

    @PostMapping("/users/{userId}/change-password")
    public String changePassword(@UserId String loginUserId,
                         @PathVariable("userId") String userId,
                         @Valid @ModelAttribute UserPasswordParam UserPasswordParam,
                         BindingResult bindingResult,
                         @Referer String referer,
                         Model model) {
        if(!loginUserId.equals(userId)) throw new NoPermissionException();
        if(bindingResult.hasErrors())
            return redirectAlertPage( "영문자와 숫자를 포함하고 5자 이상 15자 이하여야합니다.", referer, model);

        userService.changePassword(userId, UserPasswordParam.getPassword());
        return redirectAlertPage("비밀번호가 변경되었습니다.", referer, model);
    }

}


