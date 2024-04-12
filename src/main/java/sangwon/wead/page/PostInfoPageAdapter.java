package sangwon.wead.page;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import sangwon.wead.service.PostService;

import java.util.Map;

@RequiredArgsConstructor
public class PostInfoPageAdapter implements PageAdapter {
    private final PostService postService;
    @Override
    public Page<?> getPage(Pageable pageable, Map<String, Object> args) {
        return postService.getPostInfoPage(pageable);
    }
}
