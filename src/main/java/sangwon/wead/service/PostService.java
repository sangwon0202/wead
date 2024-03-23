package sangwon.wead.service;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sangwon.wead.exception.NonexistentPageException;
import sangwon.wead.exception.NonexistentPostException;
import sangwon.wead.exception.PermissionException;
import sangwon.wead.repository.entity.Post;
import sangwon.wead.repository.PostRepository;
import sangwon.wead.repository.CommentRepository;
import sangwon.wead.service.DTO.PageBarDto;
import sangwon.wead.service.DTO.PostDto;
import sangwon.wead.service.DTO.PostUpdateFormDto;
import sangwon.wead.service.util.PageUtil;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final CommentRepository commentRepository;


    public List<PostDto> getPostList(int pageNumber, int postCountPerPage) throws NonexistentPageException {
        int count = postRepository.getCount();
        if(!PageUtil.checkPageExistence(count, pageNumber, postCountPerPage)) throw new NonexistentPageException();
        return postRepository.findAll(pageNumber,postCountPerPage).stream().map((post) -> new PostDto(post)).toList();
    }

    public PageBarDto getPageBar(int pageNumber, int countPerPage, int pageCountPerPageBar) throws NonexistentPageException {
        int count = postRepository.getCount();
        if(!PageUtil.checkPageExistence(count, pageNumber, countPerPage)) throw new NonexistentPageException();
        return PageUtil.buildPageBar(count, pageNumber, countPerPage, pageCountPerPageBar);
    }


    public PostDto read(int postId) throws NonexistentPostException {
        Post post = postRepository.findByPostId(postId).orElseThrow(() -> new NonexistentPostException());
        post.setViews(post.getViews()+1);
        postRepository.save(post);
        return new PostDto(post);
    }

    public void create(String userId, String title, String content, String isbn) {
        Post post = Post.builder()
                .userId(userId)
                .title(title)
                .content(content)
                .uploadDate(LocalDate.now())
                .views(0)
                .isbn(isbn)
                .build();
        postRepository.save(post);
    }

    public PostUpdateFormDto updateForm(String userId, int postId) throws NonexistentPostException, PermissionException {
        Post post = postRepository.findByPostId(postId).orElseThrow(() -> new NonexistentPostException());
        if(!post.getUserId().equals(userId)) throw new PermissionException();
        return new PostUpdateFormDto(post);
    }

    public void update(String userId, int postId, String title, String content) throws NonexistentPostException, PermissionException {
        Post post = postRepository.findByPostId(postId).orElseThrow(() -> new NonexistentPostException());
        if(!post.getUserId().equals(userId)) throw new PermissionException();
        post.setTitle(title);
        post.setContent(content);
        postRepository.save(post);
    }

    public void delete(String userId, int postId) throws NonexistentPostException, PermissionException {
        Post post = postRepository.findByPostId(postId).orElseThrow(() -> new NonexistentPostException());
        if(!post.getUserId().equals(userId)) throw new PermissionException();
        commentRepository.deleteAllByPostId(postId);
        postRepository.deleteByPostId(postId);
    }

}
