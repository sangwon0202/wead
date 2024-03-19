package sangwon.wead.controller;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import sangwon.wead.controller.model.*;
import sangwon.wead.exception.NonexistentCommentException;
import sangwon.wead.exception.NonexistentPageException;
import sangwon.wead.exception.NonexistentPostException;
import sangwon.wead.exception.NonexistentUserException;
import sangwon.wead.service.CommentService;
import sangwon.wead.service.DTO.PostDto;
import sangwon.wead.service.DTO.UserDto;
import sangwon.wead.service.PostService;
import sangwon.wead.service.UserService;

import java.util.List;


@Controller
@RequiredArgsConstructor
public class MainController {

    private final PostService postService;
    private final UserService userService;
    private final CommentService commentService;

    private final int postCountPerPage = 10;
    private final int pageCountPerPageBar = 10;

    @GetMapping("/")
    public String post(@RequestParam(value = "page", required = false, defaultValue = "1") int pageNumber,
                       HttpServletRequest request,
                       Model model) {

        // 로그인 정보
        HttpSession session = request.getSession();
        String userId = (String)session.getAttribute("userId");

        try { if(userId != null) model.addAttribute("userInfo", new UserInfoModel(userService.getUser(userId))); }
        // 존재하지 않는 회원인 경우 (회원 탈퇴했으나 세션이 동기화 안된 경우)
        catch (NonexistentUserException e) {
            session.removeAttribute("userId");
            return "redirect:/";
        }

        // 게시글 리스트 및 페이지바
        try {
            model.addAttribute("list", buildList(postService.getPostList(pageNumber, postCountPerPage)));
            model.addAttribute("pageBar",new PageBarModel(postService.getPageBar(pageNumber, postCountPerPage, pageCountPerPageBar)));
            return "page/main";
        }
        // 해당 페이지가 존재하지 않을 경우
        catch (NonexistentPageException e) {
            model.addAttribute("message", "존재하지 않는 페이지입니다.");
            return "alert";
        }
    }

    private List<PostLineModel> buildList(List<PostDto> postDtoList) {
        return postDtoList.stream().map((postDto -> {
            String nickname;
            try {
                UserDto userDto = userService.getUser(postDto.getUserId());
                nickname = userDto.getNickname();
            }
            catch (NonexistentUserException e) { nickname = "알 수 없음"; }

            int commentCount;
            try {
                 commentCount = commentService.getCommentList(postDto.getPostId()).size();
            }
            catch (NonexistentPostException e) { commentCount = 0; }

            return PostLineModel.builder()
                    .postId(postDto.getPostId())
                    .title(postDto.getTitle())
                    .commentCount(commentCount)
                    .nickname(nickname)
                    .uploadDate(postDto.getUploadDate())
                    .views(postDto.getViews())
                    .build();
        })).toList();
    }

}
