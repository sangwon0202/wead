package sangwon.wead.aspect;


import lombok.RequiredArgsConstructor;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import sangwon.wead.repository.BookInfoCacheRepository;
import sangwon.wead.repository.entity.BookInfoCache;
import sangwon.wead.service.DTO.BookInfo;


@Aspect
@Component
@RequiredArgsConstructor
public class CacheBookInfoAspect {

    private final BookInfoCacheRepository bookInfoCacheRepository;

    @AfterReturning(value = "@annotation(sangwon.wead.aspect.annotation.CacheBookInfo)", returning = "bookInfo")
    public void cacheBookInfo(BookInfo bookInfo) {
        if(!bookInfoCacheRepository.existsById(bookInfo.getIsbn())) bookInfoCacheRepository.save(new BookInfoCache(bookInfo));
    }

}
