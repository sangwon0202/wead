package sangwon.wead.service.book;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sangwon.wead.repository.BookRepository;
import sangwon.wead.service.DTO.BookDto;
import sangwon.wead.exception.NonExistentBookException;

@Service
@Transactional
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;

    public BookDto getBook(String isbn) {
        return bookRepository.findById(isbn)
                .map(BookDto::new)
                .orElseThrow(NonExistentBookException::new);
    }

    public void saveBook(BookDto bookDto) {
        if(!bookRepository.existsById(bookDto.getIsbn()))
            bookRepository.save(bookDto.toBook());
    }

}
