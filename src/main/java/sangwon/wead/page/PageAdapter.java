package sangwon.wead.page;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Map;

public interface PageAdapter {
    Page<?> getPage(Pageable pageable, Map<String, Object> args);

}
