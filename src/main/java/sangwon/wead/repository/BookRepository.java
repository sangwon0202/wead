package sangwon.wead.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sangwon.wead.repository.entity.Book;

public interface BookRepository extends JpaRepository<Book, String> {
}
