package sangwon.wead.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import sangwon.wead.DTO.BoardDto;
import sangwon.wead.DTO.BoardFormDto;
import sangwon.wead.DTO.PageBarDto;
import sangwon.wead.service.BoardService;
import sangwon.wead.service.CommentService;
import sangwon.wead.service.UserService;

import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;
    private final UserService userService;
    private final CommentService commentService;

    private final int boardCountPerPage = 10;
    private final int pageCountPerPageBar = 10;


    @GetMapping(value = {"/", "/board/{boardId}"})
    public String board(@PathVariable(value = "boardId" ,required = false) Optional<Integer> optionalBoardId,
                        @RequestParam(value = "page", required = false, defaultValue = "1") int pageNumber,
                        HttpServletRequest request,
                        Model model) {

        // 로그인 정보
        HttpSession session = request.getSession();
        String userId = (String)session.getAttribute("userId");
        if(userId != null) model.addAttribute("user", userService.getUserInfo(userId));


        // 게시글 조회 시
        if(optionalBoardId.isPresent()) {
            int boardId = optionalBoardId.get();

            // 게시글 존재 확인
            if(!boardService.boardExist(boardId)) {
                model.addAttribute("message", "존재하지 않는 게시물입니다.");
                return "alert";
            };

            // 조회수 증가
            boardService.increaseViews(boardId);

            // 게시글 및 댓글 모델 전달
            model.addAttribute("board",boardService.read(boardId));
            model.addAttribute("comment", commentService.getCommentList(boardId));
        }

        // 리스트 모델 전달
        model.addAttribute("list", boardService.getListWithPaging(pageNumber,boardCountPerPage));

        // 페이지바 더미 데이터 전달
        PageBarDto pageBarDto = new PageBarDto();
        pageBarDto.setPrev(true);
        pageBarDto.setNext(false);
        pageBarDto.setStart(1);
        pageBarDto.setEnd(10);
        pageBarDto.setCurrent(5);
        model.addAttribute("pageBar", pageBarDto);

        return "page/main";
    }

    @GetMapping("/board/upload")
    public String uploadForm(HttpServletRequest request, Model model) {

        HttpSession session = request.getSession();

        // 로그인이 안되어 있을 경우
        if(session.getAttribute("userId") == null) {
            model.addAttribute("message", "로그인을 먼저 해주세요.");
            return "alert";
        }

        model.addAttribute("action", "/board/upload");
        return "page/upload";
    }

    @PostMapping("/board/upload")
    public String upload(HttpServletRequest request,
                         @RequestParam String title,
                         @RequestParam String content,
                         Model model) {

        HttpSession session = request.getSession();

        // 로그인이 안되어 있을 경우
        if(session.getAttribute("userId") == null) {
            model.addAttribute("message", "로그인을 먼저 해주세요.");
            return "alert";
        }

        // 빈칸이 있을 경우
        if(title.equals("") || content.equals("")) {
            model.addAttribute("message", "제목 또는 내용을 입력해주세요.");
            model.addAttribute("redirect", "/board/upload");
            return "alert";
        }

        String userId = (String)session.getAttribute("userId");
        BoardFormDto boardFormDto = new BoardFormDto();
        boardFormDto.setTitle(title);
        boardFormDto.setContent(content);
        boardService.create(userId, boardFormDto);
        return "redirect:/";
    }

    @GetMapping("/board/update/{boardId}")
    public String updateForm(HttpServletRequest request,
                             @PathVariable("boardId") int boardId,
                             Model model) {

        HttpSession session = request.getSession();

        // 로그인이 안되어 있을 경우
        if(session.getAttribute("userId") == null) {
            model.addAttribute("message", "로그인을 먼저 해주세요.");
            return "alert";
        }

        // 게시글 존재 확인
        if(!boardService.boardExist(boardId)) {
            model.addAttribute("message", "존재하지 않는 게시물입니다.");
            return "alert";
        };

        String userId = (String)session.getAttribute("userId");
        // 게시글 수정 권한 확인
        if(!boardService.isWriter(userId, boardId)) {
            model.addAttribute("message", "권한이 없습니다.");
            model.addAttribute("redirect", "/board/" + boardId);
            return "alert";
        };

        BoardDto boardDto = boardService.read(boardId);
        model.addAttribute("action", "/board/update/" + boardId);
        model.addAttribute("board", boardDto);
        return "page/upload";
    }

    @PostMapping("/board/update/{boardId}")
    public String update(HttpServletRequest request,
                             @PathVariable("boardId") int boardId,
                             @RequestParam("title") String title,
                             @RequestParam("content") String content,
                             RedirectAttributes redirectAttributes,
                             Model model) {

        HttpSession session = request.getSession();

        // 로그인이 안되어 있을 경우
        if(session.getAttribute("userId") == null) {
            model.addAttribute("message", "로그인을 먼저 해주세요.");
            return "alert";
        }

        // 게시글 존재 확인
        if(!boardService.boardExist(boardId)) {
            model.addAttribute("message", "존재하지 않는 게시물입니다.");
            return "alert";
        };

        String userId = (String)session.getAttribute("userId");
        // 게시글 수정 권한 확인
        if(!boardService.isWriter(userId, boardId)) {
            model.addAttribute("message", "권한이 없습니다.");
            model.addAttribute("redirect", "/board/" + boardId);
            return "alert";
        };

        // 빈칸이 있을 경우
        if(title.equals("") || content.equals("")) {
            model.addAttribute("message", "제목 또는 내용을 입력해주세요.");
            model.addAttribute("redirect", "/board/update/" + boardId);
            return "alert";
        }

        BoardFormDto boardFormDto = new BoardFormDto();
        boardFormDto.setTitle(title);
        boardFormDto.setContent(content);
        boardService.update(boardId, boardFormDto);

        redirectAttributes.addAttribute("boardId", boardId);
        return "redirect:/board/{boardId}";
    }

    @PostMapping("/board/delete/{boardId}")
    public String delete(HttpServletRequest request,
                             @PathVariable("boardId") int boardId,
                             Model model) {

        HttpSession session = request.getSession();

        // 로그인이 안되어 있을 경우
        if(session.getAttribute("userId") == null) {
            model.addAttribute("message", "로그인을 먼저 해주세요.");
            return "alert";
        }

        // 게시글 존재 확인
        if(!boardService.boardExist(boardId)) {
            model.addAttribute("message", "존재하지 않는 게시물입니다.");
            return "alert";
        };

        String userId = (String)session.getAttribute("userId");
        // 게시글 수정 권한 확인
        if(!boardService.isWriter(userId, boardId)) {
            model.addAttribute("message", "권한이 없습니다.");
            model.addAttribute("redirect", "/board/" + boardId);
            return "alert";
        };

        commentService.deleteAllByBoardId(boardId);
        boardService.delete(boardId);
        return "redirect:/";
    }

}
