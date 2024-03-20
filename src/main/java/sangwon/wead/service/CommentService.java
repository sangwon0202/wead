package sangwon.wead.service;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sangwon.wead.exception.PermissionException;
import sangwon.wead.service.DTO.CommentDto;
import sangwon.wead.repository.entity.Comment;
import sangwon.wead.exception.NonexistentCommentException;
import sangwon.wead.exception.NonexistentPostException;
import sangwon.wead.repository.CommentRepository;
import sangwon.wead.repository.PostRepository;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;

    public CommentDto getComment(int commentId) throws NonexistentCommentException {
        Comment comment = commentRepository.findByCommentId(commentId).orElseThrow(() -> new NonexistentCommentException());
        return new CommentDto(comment);
    }

    public List<CommentDto> getCommentList(int postId) throws NonexistentPostException {
        if(postRepository.findByPostId(postId).isEmpty()) throw new NonexistentPostException();
        return commentRepository.findAllByPostId(postId).stream().map((comment) -> new CommentDto(comment)).toList();
    }

    public void create(String userId, int postId, String content) throws NonexistentPostException {
        if(postRepository.findByPostId(postId).isEmpty()) throw new NonexistentPostException();

        Comment comment = Comment.builder()
                .userId(userId)
                .postId(postId)
                .content(content)
                .uploadDate(LocalDate.now())
                .build();

        commentRepository.save(comment);
    }

    public void delete(String userId, int commentId) throws NonexistentCommentException, PermissionException {
        Comment comment = commentRepository.findByCommentId(commentId).orElseThrow(() -> new NonexistentCommentException());
        if(!comment.getUserId().equals(userId)) throw new PermissionException();
        commentRepository.deleteByCommentId(commentId);
    }

}
