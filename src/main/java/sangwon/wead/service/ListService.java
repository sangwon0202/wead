package sangwon.wead.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import sangwon.wead.service.DTO.ListDto;
import sangwon.wead.service.DTO.PageBarDto;
import sangwon.wead.exception.IllegalPageBarException;

import java.util.HashMap;
import java.util.Map;

@Service
public class ListService {


    static public class Builder<T> {
        PageFactory<T> pageFactory;
        String url;
        Map<String, Object> queryMap;

        public Builder(PageFactory<T> pageFactory) {
            this.pageFactory = pageFactory;
            this.queryMap = new HashMap<>();
        }

        public Builder<T> setUrl(String url) {
            this.url = url;
            return this;
        }

        public Builder<T> setQuery(String name, Object value) {
            queryMap.put(name, value);
            return this;
        }

        public ListDto<T> build(Pageable pageable) {
            Page<T> page = getPage(pageable);
            PageBarDto pageBar = getPageBar(page.getNumber()+1, page.getTotalPages());
            return new ListDto<>(page.getContent(), pageBar);
        }

        private Page<T> getPage(Pageable pageable) {
            int pageNumber = pageable.getPageNumber();
            int pageSize = pageable.getPageSize();
            Sort sort = pageable.getSort();
            if(pageNumber < 1) pageNumber = 1;
            Page<T> page = pageFactory.getPage(PageRequest.of(pageNumber-1, pageSize, sort));
            int totalPages = page.getTotalPages();
            if(totalPages == 0) totalPages = 1;
            if(pageNumber > totalPages) return pageFactory.getPage(PageRequest.of(totalPages-1,pageSize, sort));
            return page;
        }

        private PageBarDto getPageBar(int number, int total) {
            int pageBarSize = 10;
            int current, start, end;
            boolean prev, next;

            if(total == 0) {
                if(number != 1) throw new IllegalPageBarException();
                return PageBarDto.builder()
                        .url(url)
                        .queryMap(queryMap)
                        .current(1)
                        .prev(false)
                        .next(false)
                        .start(1)
                        .end(1)
                        .build();
            }
            if(number > total) throw new IllegalPageBarException();

            current = number;
            prev = number > pageBarSize;
            next = number <= ((total-1)/pageBarSize)*pageBarSize;
            start = ((number-1)/pageBarSize)*pageBarSize + 1;
            end = next ? start + pageBarSize - 1 : total;

            return PageBarDto.builder()
                    .url(url)
                    .queryMap(queryMap)
                    .current(current)
                    .prev(prev)
                    .next(next)
                    .start(start)
                    .end(end)
                    .build();
        }
    }

    @FunctionalInterface
    public interface PageFactory<T> {
        Page<T> getPage(Pageable pageable);
    }

    public <T> Builder<T> builder(PageFactory<T> pageFactory) {
        return new Builder<>(pageFactory);
    }
}





