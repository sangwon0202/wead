package sangwon.wead;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import sangwon.wead.DTO.PostFormDto;
import sangwon.wead.repository.PostRepository;
import sangwon.wead.service.PostService;

@SpringBootTest
class WeadApplicationTests {

	@Autowired
	private PostService postService;

	@Test
	void contextLoads() {
		for(int i=0; i<1000; i++) {
			PostFormDto postFormDto =new PostFormDto();
			postFormDto.setTitle("title");
			postFormDto.setContent("content");
			postService.create("sangwon", postFormDto);
		}
	}

}
