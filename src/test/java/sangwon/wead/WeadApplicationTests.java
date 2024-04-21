package sangwon.wead;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import sangwon.wead.API.BookClient;
import sangwon.wead.API.BookResponse;
import sangwon.wead.redis.BookCacheRepository;




@SpringBootTest
class WeadApplicationTests {

	@Autowired
	private BookClient bookClient;
	@Autowired
	private BookCacheRepository bookCacheRepository;

	@Test
	void test() {
		String query = "주식";
		BookResponse bookResponse = bookClient.searchBook(query, 10, 1, "sim");

		bookCacheRepository.cache(query, bookResponse);
		System.out.println(bookCacheRepository.findByQuery(query));
	}

}
