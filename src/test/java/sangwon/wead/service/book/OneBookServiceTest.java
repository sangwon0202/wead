package sangwon.wead.service.book;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
        assertThatThrownBy(() -> oneBookService.getBookInfoPageByQuery(PageRequest.of(-1,10), "주식"))
                .isInstanceOf(IllegalArgumentException.class);


        // pageSize == 10
        page = oneBookService.getBookInfoPageByQuery(PageRequest.of(0,10), "주식");
        assertThat(page.getNumberOfElements()).isEqualTo(1);
        assertThat(page.getTotalElements()).isEqualTo(1);
        assertThat(page.hasPrevious()).isFalse();

        page = oneBookService.getBookInfoPageByQuery(PageRequest.of(1,10), "주식");
        assertThat(page.getNumberOfElements()).isEqualTo(0);
        assertThat(page.getTotalElements()).isEqualTo(1);
        assertThat(page.hasPrevious()).isTrue();

        // pageSize == 5
        page = oneBookService.getBookInfoPageByQuery(PageRequest.of(0,5), "주식");
        assertThat(page.getNumberOfElements()).isEqualTo(1);
        assertThat(page.getTotalElements()).isEqualTo(1);
        assertThat(page.hasPrevious()).isFalse();

        page = oneBookService.getBookInfoPageByQuery(PageRequest.of(1,5), "주식");
        assertThat(page.getNumberOfElements()).isEqualTo(0);
        assertThat(page.getTotalElements()).isEqualTo(1);
        assertThat(page.hasPrevious()).isTrue();

    }


}