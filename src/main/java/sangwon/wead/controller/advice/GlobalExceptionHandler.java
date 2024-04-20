package sangwon.wead.controller.advice;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import sangwon.wead.exception.*;

import static sangwon.wead.util.AlertPageRedirector.redirectAlertPage;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public String methodArgumentTypeMismatchException(Model model) {
        return redirectAlertPage("잘못된 요청입니다", "/", model);
    }

    @ExceptionHandler(NoLoginException.class)
    public String noLoginException(Model model) {
        return redirectAlertPage("로그인을 먼저 해주세요.", "/", model);
    }

    @ExceptionHandler(AlreadyLoginException.class)
    public String alreadyLoginException(Model model) {
        return redirectAlertPage("로그아웃을 먼저 해주세요.", "/", model);
    }

    @ExceptionHandler(NonExistentPostException.class)
    public String nonExistentPostException(Model model) {
        return redirectAlertPage("존재하지 않는 게시글입니다.", "/posts", model);
    }

    @ExceptionHandler(NonExistentCommentException.class)
    public String nonExistentCommentException (Model model) {
        return redirectAlertPage("존재하지 않는 댓글입니다.", "/posts", model);
    }

    @ExceptionHandler(BookSearchFailException.class)
    public String bookSearchFailException (Model model) {
        return redirectAlertPage("책 정보를 불러올 수 없습니다.", "/books", model);
    }

    @ExceptionHandler(NoPermissionException.class)
    public String noPermissionException(Model model) {
        return redirectAlertPage("권한이 없습니다.", "/", model);
    }

    @ExceptionHandler(RuntimeException.class)
    public String runtimeException(Exception e, Model model) {
        log.error(e.getMessage());
        e.printStackTrace();
        return redirectAlertPage("서버 문제: 잠시 후 다시 시도해주세요.", "/", model);
    }
}
