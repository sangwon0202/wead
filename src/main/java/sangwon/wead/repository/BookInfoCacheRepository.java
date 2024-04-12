package sangwon.wead.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sangwon.wead.repository.entity.BookInfoCache;

public interface BookInfoCacheRepository extends JpaRepository<BookInfoCache, String> {
}
