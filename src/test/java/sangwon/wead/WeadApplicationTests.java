package sangwon.wead;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;
import sangwon.wead.service.CommentService;
import sangwon.wead.service.DTO.BookRowDto;
import sangwon.wead.service.DTO.CommentRowDto;
import sangwon.wead.service.DTO.ListDto;
import sangwon.wead.service.ListService;
import sangwon.wead.service.PostService;
import sangwon.wead.service.book.search.ApiBookSearchService;

@SpringBootTest
class WeadApplicationTests {


	@Autowired
	private PostService postService;

	@Autowired
	private ListService listService;
	@Autowired
	private ApiBookSearchService bookSearchService;
	@Autowired
	private CommentService commentService;

	@Test
	@Transactional
	void listService() {

		Long postId = 9L;

		ListDto<CommentRowDto> listDto = listService
				.builder(pageable -> commentService.getCommentByPostId(pageable, postId))
				.setUrl("/books")
				.build(-199,10);

		System.out.println(listDto);
	}

}
