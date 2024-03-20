package sangwon.wead.naverAPI;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
class NaverAPIBookClientTest {

    @Autowired
    private NaverAPIBookClient naverAPIBookClient;

    @Test
    public void search() {
        NaverAPIBookResponse result = naverAPIBookClient.search("공룡", 1, 10, "sim");
        System.out.println(result);
    }

}