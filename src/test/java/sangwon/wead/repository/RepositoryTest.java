package sangwon.wead.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import sangwon.wead.DTO.BoardFormDto;
import sangwon.wead.entity.Board;
import sangwon.wead.entity.User;
import sangwon.wead.service.BoardService;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class RepositoryTest {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BoardRepository boardRepository;
    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private BoardService boardService;

    @Test
    void test() {
        for(int i=0; i<1000; i++) {
            BoardFormDto boardFormDto = new BoardFormDto();
            boardFormDto.setTitle(i + "번째 게시글 작성");
            boardFormDto.setContent("내용");
            boardService.create("sangwon",boardFormDto);
        }
    }

    @Test
    void getCount() {
        System.out.println(boardRepository.getCount());
    }
}