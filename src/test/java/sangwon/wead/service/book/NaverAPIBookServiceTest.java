package sangwon.wead.service.book;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import sangwon.wead.service.DTO.BookInfo;


import static org.assertj.core.api.Assertions.*;

@SpringBootTest
class NaverAPIBookServiceTest {

    @Autowired
    private NaverAPIBookService naverAPIBookService;

    @Test
    void getBookInfo() {
        String isbn = "9788960773417";
        BookInfo bookInfo = naverAPIBookService.getBookInfo(isbn);
        assertThat(bookInfo.getIsbn()).isEqualTo(isbn);
    }

    @Test
    void getBookInfoPageByQuery() {
        Page<BookInfo> page;

        // pageNumber < 0
        assertThatThrownBy(() -> naverAPIBookService.getBookInfoPageByQuery("주식", -1, 10))
                .isInstanceOf(IllegalArgumentException.class);

        // pageNumber > 100
        assertThatThrownBy(() -> naverAPIBookService.getBookInfoPageByQuery("주식", 0, 101))
                .isInstanceOf(IllegalArgumentException.class);

        // pageSize == 10
        page = naverAPIBookService.getBookInfoPageByQuery("주식", 0, 10);
        assertThat(page.getNumberOfElements()).isEqualTo(10);
        assertThat(page.getTotalElements()).isEqualTo(1000);
        assertThat(page.hasPrevious()).isFalse();
        assertThat(page.hasNext()).isTrue();

        page = naverAPIBookService.getBookInfoPageByQuery("주식", 5, 10);
        assertThat(page.getNumberOfElements()).isEqualTo(10);
        assertThat(page.getTotalElements()).isEqualTo(1000);
        assertThat(page.hasPrevious()).isTrue();
        assertThat(page.hasNext()).isTrue();

        page = naverAPIBookService.getBookInfoPageByQuery("주식", 99, 10);
        assertThat(page.getNumberOfElements()).isEqualTo(10);
        assertThat(page.getTotalElements()).isEqualTo(1000);
        assertThat(page.hasPrevious()).isTrue();
        assertThat(page.hasNext()).isFalse();

        page = naverAPIBookService.getBookInfoPageByQuery("주식", 100, 10);
        assertThat(page.getNumberOfElements()).isEqualTo(0);
        assertThat(page.getTotalElements()).isEqualTo(1000);
        assertThat(page.hasPrevious()).isTrue();
        assertThat(page.hasNext()).isFalse();

    }

    @Test
    void getBookInfoPageByQuery16() {
        Page<BookInfo> page;

        page = naverAPIBookService.getBookInfoPageByQuery("주식", 0, 16);
        assertThat(page.getNumberOfElements()).isEqualTo(16);
        assertThat(page.getTotalElements()).isEqualTo(1000);
        assertThat(page.hasPrevious()).isFalse();
        assertThat(page.hasNext()).isTrue();

        page = naverAPIBookService.getBookInfoPageByQuery("주식", 5, 16);
        assertThat(page.getNumberOfElements()).isEqualTo(16);
        assertThat(page.getTotalElements()).isEqualTo(1000);
        assertThat(page.hasPrevious()).isTrue();
        assertThat(page.hasNext()).isTrue();


        page = naverAPIBookService.getBookInfoPageByQuery("주식", 62, 16);
        assertThat(page.getNumberOfElements()).isEqualTo(8);
        assertThat(page.getTotalElements()).isEqualTo(1000);
        assertThat(page.hasPrevious()).isTrue();
        assertThat(page.hasNext()).isFalse();

        page = naverAPIBookService.getBookInfoPageByQuery("주식", 63, 16);
        assertThat(page.getNumberOfElements()).isEqualTo(0);
        assertThat(page.getTotalElements()).isEqualTo(1000);
        assertThat(page.hasPrevious()).isTrue();
        assertThat(page.hasNext()).isFalse();

    }

}