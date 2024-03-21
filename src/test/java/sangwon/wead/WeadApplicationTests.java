package sangwon.wead;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import sangwon.wead.exception.APICallFailException;
import sangwon.wead.exception.NonexistentBookException;
import sangwon.wead.exception.NonexistentPageException;
import sangwon.wead.service.BookService;

@SpringBootTest
class WeadApplicationTests {

	@Autowired
	private BookService bookService;

	@Test
	void contextLoads() {

		try {
			System.out.println(bookService.getBookList("주",1,10).toString());
		} catch (APICallFailException e) {
			throw new RuntimeException(e);
		} catch (NonexistentPageException e) {
			throw new RuntimeException(e);
		}
	}

}
