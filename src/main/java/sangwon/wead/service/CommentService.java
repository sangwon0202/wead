package sangwon.wead.service;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sangwon.wead.service.DTO.CommentUploadForm;
import sangwon.wead.repository.UserRepository;
import sangwon.wead.repository.entity.Post;
import sangwon.wead.repository.entity.User;
import sangwon.wead.service.DTO.CommentInfo;
import sangwon.wead.repository.entity.Comment;
import sangwon.wead.repository.CommentRepository;
import sangwon.wead.repository.PostRepository;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public boolean checkCommentExistence(Long commentId) {
        return commentRepository.existsById(commentId);
    }

    public CommentInfo getCommentInfo(Long commentId) {
        Comment comment = commentRepository.findById(commentId).get();
        return new CommentInfo(comment);
    }

    public List<CommentInfo> getCommentInfoList(Long postId) {
        Post post = postRepository.findById(postId).get();
        return post.getComments().stream().map((CommentInfo::new)).toList();
    }

    public void uploadComment(CommentUploadForm commentUploadForm) {
        User user = userRepository.findById(commentUploadForm.getUserId()).get();
        Post post = postRepository.findById(commentUploadForm.getPostId()).get();
        Comment comment = Comment.builder()
                .user(user)
                .post(post)
                .content(commentUploadForm.getContent())
                .build();
        commentRepository.save(comment);
    }

    public void deleteComment(Long commentId) {
        Comment comment = commentRepository.findById(commentId).get();
        commentRepository.delete(comment);
    }

}
