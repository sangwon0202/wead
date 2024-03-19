package sangwon.wead.service;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sangwon.wead.exception.NonexistentPageException;
import sangwon.wead.exception.NonexistentPostException;
import sangwon.wead.repository.entity.Post;
import sangwon.wead.repository.PostRepository;
import sangwon.wead.repository.CommentRepository;
import sangwon.wead.service.DTO.PageBarDto;
import sangwon.wead.service.DTO.PostDto;

import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final CommentRepository commentRepository;

    public List<PostDto> getPostList(int pageNumber, int postCountPerPage) throws NonexistentPageException {
        if(!checkPageExistence(pageNumber, postCountPerPage)) throw new NonexistentPageException();
        return postRepository.findAll(pageNumber,postCountPerPage).stream().map((post) -> new PostDto(post)).toList();
    }

    public PageBarDto getPageBar(int pageNumber, int postCountPerPage, int pageCountPerPageBar) throws NonexistentPageException {
        if(!checkPageExistence(pageNumber, postCountPerPage)) throw new NonexistentPageException();

        int postCount = postRepository.getCount();
        int pageCount = (postCount-1) / postCountPerPage + 1;
        int pageBarNumber = (pageNumber-1)/pageCountPerPageBar + 1;
        int pageBarCount = (pageCount-1)/pageCountPerPageBar + 1;

        PageBarDto pageBarDto = new PageBarDto();
        pageBarDto.setCurrent(pageNumber);
        pageBarDto.setPrev(pageBarNumber > 1);
        pageBarDto.setNext(pageBarNumber != pageBarCount);
        pageBarDto.setStart((pageBarNumber-1)*pageCountPerPageBar + 1);
        if(pageBarDto.isNext()) pageBarDto.setEnd(pageBarDto.getStart() + pageCountPerPageBar - 1);
        else pageBarDto.setEnd(pageCount);

        return pageBarDto;
    }

    private boolean checkPageExistence(int pageNumber, int postCountPerPage) {
        int postCount = postRepository.getCount();
        if(postCount == 0 && pageNumber != 1) return false;
        int pageCount = (postCount-1)/postCountPerPage + 1;
        return pageNumber <= pageCount;
    }

    public PostDto read(int postId) throws NonexistentPostException {
        Post post = postRepository.findByPostId(postId).orElseThrow(() -> new NonexistentPostException());
        return new PostDto(post);
    }

    public void create(String userId, String title, String content) {
        Post post = Post.builder()
                .userId(userId)
                .title(title)
                .content(content)
                .uploadDate(new Date())
                .views(0)
                .build();
        postRepository.save(post);
    }

    public void update(int postId, String title, String content) throws NonexistentPostException {
        Post post = postRepository.findByPostId(postId).orElseThrow(() -> new NonexistentPostException());
        post.setTitle(title);
        post.setContent(content);
        postRepository.save(post);
    }

    public void delete(int postId) throws NonexistentPostException {
        if(postRepository.findByPostId(postId).isEmpty()) throw new NonexistentPostException();
        commentRepository.deleteAllByPostId(postId);
        postRepository.deleteByPostId(postId);
    }

    public boolean verifyPermission(String userId, int postId) throws NonexistentPostException {
        Post post = postRepository.findByPostId(postId).orElseThrow(() -> new NonexistentPostException());
        return post.getUserId().equals(userId);
    }

    public void increaseViews(int postId) throws NonexistentPostException {
        Post post = postRepository.findByPostId(postId).orElseThrow(() -> new NonexistentPostException());
        post.setViews(post.getViews()+1);
        postRepository.save(post);
    }

}
