package sangwon.wead.page;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import sangwon.wead.service.PostService;


@RequiredArgsConstructor
public class PostInfoPageByTitleAdapter implements PageAdapter {

    private final PostService postService;

    @Override
    public Page<?> getPage(Pageable pageable, String query) {
        return postService.getPostInfoPageByTitle(pageable, query);
    }
}
