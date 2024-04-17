package sangwon.wead.service;


import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sangwon.wead.aspect.annotation.Log;
import sangwon.wead.repository.BookRepository;
import sangwon.wead.repository.entity.Book;
import sangwon.wead.service.DTO.PostUpdateForm;
import sangwon.wead.repository.UserRepository;
import sangwon.wead.repository.entity.Post;
import sangwon.wead.repository.PostRepository;
import sangwon.wead.service.DTO.PostInfo;
import sangwon.wead.service.DTO.PostUploadForm;
import sangwon.wead.repository.entity.User;

@Service
@Transactional
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final BookRepository bookRepository;

    public boolean checkPostExistence(Long postId) {
        return postRepository.existsById(postId);
    }

    public PostInfo getPostInfo(Long postId) {
        Post post = postRepository.findById(postId).get();
        return new PostInfo(post);
    }

    public Page<PostInfo> getPostInfoPage(Pageable pageable) {
        return postRepository.findAll(pageable).map(PostInfo::new);
    }

    public Page<PostInfo> getPostInfoPageByTitle(Pageable pageable, String title) {
        return postRepository.findByTitleContains(pageable, title).map(PostInfo::new);
    }

    public Page<PostInfo> getPostInfoPageByBookTitle(Pageable pageable, String bookTitle) {
        return postRepository.findByBookTitleContains(pageable, bookTitle).map(PostInfo::new);
    }

    public Page<PostInfo> getPostInfoPageByNickname(Pageable pageable, String nickname) {
        return postRepository.findByNicknameContains(pageable, nickname).map(PostInfo::new);
    }

    public void increasePostViews(Long postId) {
        Post post = postRepository.findById(postId).get();
        post.increaseViews();
    }

    public Long uploadPost(PostUploadForm postUploadForm) {
        User user = userRepository.findById(postUploadForm.getUserId()).get();
        Book book = bookRepository.findById(postUploadForm.getIsbn()).get();
        Post post = Post.builder()
                .user(user)
                .title(postUploadForm.getTitle())
                .content(postUploadForm.getContent())
                .book(book)
                .build();
        postRepository.save(post);
        return post.getId();
    }

    public void updatePost(PostUpdateForm postUpdateForm) {
        Post post = postRepository.findById(postUpdateForm.getPostId()).get();
        post.update(postUpdateForm.getTitle(), postUpdateForm.getContent());
    }

    public void deletePost(Long postId) {
        Post post = postRepository.findById(postId).get();
        postRepository.delete(post);
    }

}
