package sangwon.wead.service;


import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sangwon.wead.DTO.PostUpdateForm;
import sangwon.wead.exception.*;
import sangwon.wead.repository.UserRepository;
import sangwon.wead.repository.entity.Post;
import sangwon.wead.repository.PostRepository;
import sangwon.wead.DTO.PostInfo;
import sangwon.wead.DTO.PostUploadForm;
import sangwon.wead.repository.entity.User;

@Service
@Transactional
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public PostInfo getPostInfo(Long postId) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new NonexistentPostException());
        return new PostInfo(post);
    }

    public Page<PostInfo> getPostInfoPage(int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        return postRepository.findAll(pageable).map(post -> new PostInfo(post));
    }

    public void increasePostViews(Long postId) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new NonexistentPostException());
        post.increaseViews();
    }

    public void uploadPost(String userId, PostUploadForm postUploadForm) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NonexistentUserException());
        Post post = Post.builder()
                .user(user)
                .title(postUploadForm.getTitle())
                .content(postUploadForm.getContent())
                .isbn(postUploadForm.getIsbn())
                .build();
        postRepository.save(post);
    }

    public void updatePost(Long postId, PostUpdateForm postUpdateForm) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new NonexistentPostException());
        post.update(postUpdateForm.getTitle(), postUpdateForm.getContent());
    }

    public void deletePost(Long postId) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new NonexistentPostException());
        postRepository.delete(post);
    }

}
