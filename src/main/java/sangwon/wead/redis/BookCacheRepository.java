package sangwon.wead.redis;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;
import sangwon.wead.API.BookResponse;
import sangwon.wead.exception.BookCacheMissException;

import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class BookCacheRepository {

    private final RedisTemplate<String, Object> redisTemplate;

    public void cache(String query, BookResponse bookResponse) {
        ValueOperations<String,Object>  valueOperations = redisTemplate.opsForValue();
        ListOperations<String,Object> listOperations = redisTemplate.opsForList();

        Integer total = (Integer)valueOperations.get("total:" + query);
        if(total != null) return;

        valueOperations.set("total:" + query, Math.min(bookResponse.getTotal(), 1000));
        bookResponse.getItems().stream()
                .map(BookCache::new)
                .forEach(bookCache -> {
                            valueOperations.set("isbn:" + bookCache.getIsbn(), bookCache);
                            listOperations.rightPush("query:" + query, bookCache.getIsbn());
        });
    }

    public BookCache getByIsbn(String isbn) {
        ValueOperations<String,Object>  valueOperations = redisTemplate.opsForValue();
        BookCache bookCache = (BookCache)valueOperations.get("isbn:" + isbn);
        if(bookCache == null) throw new BookCacheMissException();
        return bookCache;
    }

    public int getTotalByQuery(String query) {
        ValueOperations<String,Object>  valueOperations = redisTemplate.opsForValue();
        Integer total = (Integer)valueOperations.get("total:" + query);
        if(total == null) throw new BookCacheMissException();
        return total;
    }

    public List<BookCache> getByQuery(String query) {
        ValueOperations<String,Object>  valueOperations = redisTemplate.opsForValue();
        ListOperations<String,Object> listOperations = redisTemplate.opsForList();

        Integer total = (Integer)valueOperations.get("total:" + query);
        if(total == null) throw new BookCacheMissException();

        List<Object> list = listOperations.range("query:" + query,0,-1);
        if(list == null) throw new BookCacheMissException();

        return list.stream()
                .map(o -> (BookCache)valueOperations.get("isbn:" + (String)o))
                .toList();
    }

}
