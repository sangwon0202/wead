package sangwon.wead.service.book;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import sangwon.wead.service.DTO.BookInfo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
class OneBookServiceTest {

    @Autowired
    private OneBookService oneBookService;

    @Test
    void getBookInfo() {
        String isbn = "9788960773417";
        BookInfo bookInfo = oneBookService.getBookInfo(isbn);
        assertThat(bookInfo.getIsbn()).isEqualTo(isbn);
    }

    @Test
    void getBookInfoPageByQuery() {
        Page<BookInfo> page;

        // pageNumber < 0
        assertThatThrownBy(() -> oneBookService.getBookInfoPageByQuery("주식", -1, 10))
                .isInstanceOf(IllegalArgumentException.class);


        // pageSize == 10
        page = oneBookService.getBookInfoPageByQuery("주식", 0, 10);
        assertThat(page.getNumberOfElements()).isEqualTo(1);
        assertThat(page.getTotalElements()).isEqualTo(1);
        assertThat(page.hasPrevious()).isFalse();

        page = oneBookService.getBookInfoPageByQuery("주식", 1, 10);
        assertThat(page.getNumberOfElements()).isEqualTo(0);
        assertThat(page.getTotalElements()).isEqualTo(1);
        assertThat(page.hasPrevious()).isTrue();

        // pageSize == 5
        page = oneBookService.getBookInfoPageByQuery("주식", 0, 5);
        assertThat(page.getNumberOfElements()).isEqualTo(1);
        assertThat(page.getTotalElements()).isEqualTo(1);
        assertThat(page.hasPrevious()).isFalse();

        page = oneBookService.getBookInfoPageByQuery("주식", 1, 5);
        assertThat(page.getNumberOfElements()).isEqualTo(0);
        assertThat(page.getTotalElements()).isEqualTo(1);
        assertThat(page.hasPrevious()).isTrue();

    }


}