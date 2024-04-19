package sangwon.wead.service;


import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sangwon.wead.service.DTO.CommentRowDto;
import sangwon.wead.service.DTO.CommentUploadDto;
import sangwon.wead.repository.UserRepository;
import sangwon.wead.repository.entity.Post;
import sangwon.wead.repository.entity.User;
import sangwon.wead.repository.entity.Comment;
import sangwon.wead.repository.CommentRepository;
import sangwon.wead.repository.PostRepository;
import sangwon.wead.service.exception.NonAuthorException;
import sangwon.wead.service.exception.NonExistentCommentException;
import sangwon.wead.service.exception.NonExistentPostException;
import sangwon.wead.service.exception.NonExistentUserException;


@Service
@Transactional
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public Page<CommentRowDto> getCommentByPostId(Pageable pageable, Long postId) {
        return commentRepository.findByPostIdFetchJoin(pageable,postId)
                .map(CommentRowDto::new);
    }

    public void checkCommentPermission(Long commentId, String userId) {
        if(!userRepository.existsById(userId))
            throw new NonExistentUserException();
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(NonExistentCommentException::new);
        if(!comment.getUser().getUserId().equals(userId))
            throw new NonAuthorException();
    }

    public void uploadComment(CommentUploadDto commentUploadDto) {
        User user = userRepository.findById(commentUploadDto.getUserId())
                .orElseThrow(NonExistentUserException::new);
        Post post = postRepository.findById(commentUploadDto.getPostId())
                .orElseThrow(NonExistentPostException::new);
        Comment comment = Comment.builder()
                .user(user)
                .post(post)
                .content(commentUploadDto.getContent())
                .build();
        commentRepository.save(comment);
    }

    public void deleteComment(Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(NonExistentCommentException::new);
        commentRepository.delete(comment);
    }

}
