package sangwon.wead.service.book;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sangwon.wead.repository.BookRepository;
import sangwon.wead.repository.entity.Book;
import sangwon.wead.service.DTO.BookInfo;

@Service
@Transactional
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;

    public boolean checkBookExistence(String isbn) {
        return bookRepository.existsById(isbn);
    }

    public BookInfo getBookInfo(String isbn) {
        Book book = bookRepository.findById(isbn).get();
        return new BookInfo(book);
    }

    public void saveBook(BookInfo bookInfo) {
        bookRepository.save(bookInfo.toBook());
    }

}
