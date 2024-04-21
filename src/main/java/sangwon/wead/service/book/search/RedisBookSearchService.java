package sangwon.wead.service.book.search;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import sangwon.wead.API.BookClient;
import sangwon.wead.API.BookResponse;
import sangwon.wead.exception.BookCacheMissException;
import sangwon.wead.exception.CacheUnavailableException;
import sangwon.wead.redis.BookCache;
import sangwon.wead.redis.BookCacheRepository;
import sangwon.wead.service.DTO.BookDto;
import sangwon.wead.service.DTO.BookRowDto;

import java.util.ArrayList;
import java.util.List;

@Primary
@Service
@RequiredArgsConstructor
public class RedisBookSearchService implements BookSearchService {

    private final BookClient bookClient;
    private final ApiBookSearchService apiBookSearchService;
    private final BookCacheRepository bookCacheRepository;


    @Override
    public BookDto getBook(String isbn) {
        try {
            return new BookDto(bookCacheRepository.getByIsbn(isbn));
        }
        catch (Exception e) {
            return apiBookSearchService.getBook(isbn);
        }
    }

    @Override
    public Page<BookRowDto> getBookByQuery(Pageable pageable, String query) {
        if(query == null || query.isBlank()) return new PageImpl<>(new ArrayList<>(), pageable, 0);
        try {
            return getBookByQueryUsingCache(pageable, query);
        }
        catch (Exception e) {
            return apiBookSearchService.getBookByQuery(pageable, query);
        }
    }

    public Page<BookRowDto> getBookByQueryUsingCache(Pageable pageable, String query) {
        int pageNumber = pageable.getPageNumber();
        int pageSize = pageable.getPageSize();
        int start = pageNumber*pageSize;
        int end = (pageNumber+1)*pageSize-1;

        int total;
        List<BookCache> list;
        boolean cacheFlag = true;

        while(true) {
            try {
                total = bookCacheRepository.getTotalByQuery(query);
                list = bookCacheRepository.getByQuery(query);
                break;
            }
            catch (BookCacheMissException e) {
                if(cacheFlag) {
                    BookResponse bookResponse = bookClient.searchBook(query, 100, 1, "sim");
                    bookCacheRepository.cache(query, bookResponse);
                    cacheFlag = false;
                }
                else throw new CacheUnavailableException();
            }
        }

        if(start >= total) return new PageImpl<>(new ArrayList<>(), pageable, total);
        if(end >= list.size()) throw new CacheUnavailableException();

        List<BookRowDto> content = list.stream()
                .skip(start)
                .limit(pageSize)
                .map(BookRowDto::new)
                .toList();

        return new PageImpl<>(content, pageable, total);
    }

}
