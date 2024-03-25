package sangwon.wead;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import sangwon.wead.API.book.NaverAPIBookClient;
import sangwon.wead.exception.APICallFailException;
import sangwon.wead.exception.NonexistentBookException;
import sangwon.wead.service.BookService;

@SpringBootTest
class WeadApplicationTests {

	@Test
	void contextLoads() {
	}

}
