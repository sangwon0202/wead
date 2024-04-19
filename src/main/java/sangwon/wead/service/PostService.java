package sangwon.wead.service;


import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sangwon.wead.repository.BookRepository;
import sangwon.wead.repository.entity.Book;
import sangwon.wead.service.DTO.*;
import sangwon.wead.repository.UserRepository;
import sangwon.wead.repository.entity.Post;
import sangwon.wead.repository.PostRepository;
import sangwon.wead.repository.entity.User;
import sangwon.wead.service.exception.NonAuthorException;
import sangwon.wead.service.exception.NonExistentBookException;
import sangwon.wead.service.exception.NonExistentPostException;
import sangwon.wead.service.exception.NonExistentUserException;

@Service
@Transactional
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final BookRepository bookRepository;

    public Page<PostRowDto> getPostALL(Pageable pageable) {
        return postRepository.findAllFetchJoin(pageable)
                .map(post -> new PostRowDto(post, post.getCommentCount()));
    }

    public Page<PostRowDto> getPostByUserId(Pageable pageable, String userId) {
        return postRepository.findByUserIdFetchJoin(pageable, userId)
                .map(post -> new PostRowDto(post, post.getCommentCount()));
    }

    public Page<PostRowDto> getPostByTitle(Pageable pageable, String title) {
        return postRepository.findByTitleContainsFetchJoin(pageable, title)
                .map(post -> new PostRowDto(post, post.getCommentCount()));
    }

    public Page<PostRowDto> getPostByBookTitle(Pageable pageable, String bookTitle) {
        return postRepository.findByBookTitleContainsFetchJoin(pageable, bookTitle)
                .map(post -> new PostRowDto(post, post.getCommentCount()));
    }

    public Page<PostRowDto> getPostByNickname(Pageable pageable, String nickname) {
        return postRepository.findByNicknameContainsFetchJoin(pageable, nickname)
                .map(post -> new PostRowDto(post, post.getCommentCount()));
    }

    public PostDto getPost(Long postId) {
        return postRepository.findById(postId)
                .map(PostDto::new)
                .orElseThrow(NonExistentPostException::new);
    }

    public void increasePostViews(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(NonExistentPostException::new);
        post.increaseViews();
    }

    public Long uploadPost(PostUploadDto postUploadDto) {
        User user = userRepository.findById(postUploadDto.getUserId())
                .orElseThrow(NonExistentUserException::new);
        Book book = bookRepository.findById(postUploadDto.getIsbn())
                .orElseThrow(NonExistentBookException::new);
        Post post = Post.builder()
                .user(user)
                .book(book)
                .title(postUploadDto.getTitle())
                .content(postUploadDto.getContent())
                .build();
        postRepository.save(post);
        return post.getPostId();
    }

    public void checkPostPermission(Long postId, String userId) {
        if(!userRepository.existsById(userId))
            throw new NonExistentUserException();
        Post post = postRepository.findById(postId)
                .orElseThrow(NonExistentPostException::new);
        if(!post.getUser().getUserId().equals(userId))
            throw new NonAuthorException();
    }

    public void updatePost(PostUpdateDto postUpdateDto) {
        Post post = postRepository.findById(postUpdateDto.getPostId())
                        .orElseThrow(NonExistentPostException::new);
        post.update(postUpdateDto.getTitle(), postUpdateDto.getContent());
    }

    public void deletePost(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(NonExistentPostException::new);
        postRepository.delete(post);
    }

}
