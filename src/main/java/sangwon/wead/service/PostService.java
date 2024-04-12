package sangwon.wead.service;


import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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

    public void increasePostViews(Long postId) {
        Post post = postRepository.findById(postId).get();
        post.increaseViews();
    }

    public Long uploadPost(PostUploadForm postUploadForm) {
        User user = userRepository.findById(postUploadForm.getUserId()).get();
        Post post = Post.builder()
                .user(user)
                .title(postUploadForm.getTitle())
                .content(postUploadForm.getContent())
                .isbn(postUploadForm.getIsbn())
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
