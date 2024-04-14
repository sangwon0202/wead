package sangwon.wead.page;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

@Builder
@AllArgsConstructor
public class PageFactory {

    private PageAdapter pageAdapter;
    @Builder.Default
    private int pageNumber = 1;
    @Builder.Default
    private int pageSize = 10;
    @Builder.Default
    private Sort sort = Sort.unsorted();
    private String query;

    public <T> Page<T> getPage(Class<T> cast) {
        if(pageNumber < 1) pageNumber = 1;
        Page<?> page = pageAdapter.getPage(PageRequest.of(pageNumber-1, pageSize, sort), query);
        int totalPages = page.getTotalPages();
        if(totalPages == 0) totalPages = 1;
        if(pageNumber > totalPages) page = pageAdapter.getPage(PageRequest.of(totalPages-1, pageSize, sort), query);
        return (Page<T>)page;
    }

}
