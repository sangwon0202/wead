package sangwon.wead.page;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.HashMap;
import java.util.Map;

public class PageAdjuster {

    static public class PageBuilder {
        private final PageAdapter pageAdapter;
        private final Map<String, Object> args;
        PageBuilder(PageAdapter pageAdapter) {
            this.pageAdapter = pageAdapter;
            this.args = new HashMap<>();
        }

        public PageBuilder arg(String name, Object value) {
            args.put(name, value);
            return this;
        }

        public <T> Page<T> getPage(int pageNumber, int pageSize, Class<T> cast) {
            if(pageNumber < 1) pageNumber = 1;
            Page<?> page = pageAdapter.getPage(PageRequest.of(pageNumber-1, pageSize), args);
            int totalPages = page.getTotalPages();
            if(totalPages == 0) totalPages = 1;
            if(pageNumber > totalPages) page = pageAdapter.getPage(PageRequest.of(totalPages-1, pageSize), args);
            return (Page<T>)page;
        }
    }

    static public PageBuilder pageAdapter(PageAdapter pageAdapter) {
        return new PageBuilder(pageAdapter);
    }

}
