package sangwon.wead.page;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import sangwon.wead.service.PostService;

import java.util.Map;

@RequiredArgsConstructor
public class PostInfoPageByTitleAdapter implements PageAdapter {

    private final PostService postService;

    @Override
    public Page<?> getPage(Pageable pageable, Map<String, Object> args) {
        String title = (String)args.get("title");
        if(title == null) throw new RuntimeException("title 이 인자로 전달되지 않았습니다.");
        return postService.getPostInfoPageByTitle(title, pageable);
    }
}
