package sangwon.wead.service.book;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import sangwon.wead.repository.BookInfoCacheRepository;
import sangwon.wead.repository.entity.BookInfoCache;
import sangwon.wead.service.DTO.BookInfo;

import java.util.Optional;

@Primary
@Service
@RequiredArgsConstructor
public class CacheBookService implements BookService {

    private final BookInfoCacheRepository bookInfoCacheRepository;
    private final NaverAPIBookService bookService;

    @Override
    public boolean checkBookExistence(String isbn) {
        Optional<BookInfoCache> bookInfoCache = bookInfoCacheRepository.findById(isbn);
        if(bookInfoCache.isPresent()) return true;
        else return bookService.checkBookExistence(isbn);
    }

    @Override
    public BookInfo getBookInfo(String isbn) {
        Optional<BookInfoCache> bookInfoCache = bookInfoCacheRepository.findById(isbn);
        if(bookInfoCache.isPresent()) return bookInfoCache.get().toBookInfo();
        else return bookService.getBookInfo(isbn);
    }

    @Override
    public Page<BookInfo> getBookInfoPageByQuery(Pageable pageable, String query) {
        return bookService.getBookInfoPageByQuery(pageable,query);
    }
}
