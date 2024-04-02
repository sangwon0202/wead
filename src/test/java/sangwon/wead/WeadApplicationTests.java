package sangwon.wead;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import sangwon.wead.exception.NonexistentPostException;
import sangwon.wead.service.PostService;

@SpringBootTest
class WeadApplicationTests {

	@Autowired
	private PostService postService;

	@Test
	@DisplayName("DB 연결 테스트")
	void DBConnectTest() {
	}

}
