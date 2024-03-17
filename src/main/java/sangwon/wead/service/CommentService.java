package sangwon.wead.service;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sangwon.wead.DTO.CommentDto;
import sangwon.wead.entity.Comment;
import sangwon.wead.exception.NonexistentCommentException;
import sangwon.wead.exception.NonexistentPostException;
import sangwon.wead.exception.PermissionException;
import sangwon.wead.repository.CommentRepository;
import sangwon.wead.repository.PostRepository;
import sangwon.wead.repository.UserRepository;

import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;


    public List<CommentDto> getCommentList(int postId) throws NonexistentPostException {
        if(postRepository.findByPostId(postId).isEmpty()) throw new NonexistentPostException();
        return commentRepository.findAllByPostId(postId).stream().map((comment) -> {
            CommentDto commentDto = new CommentDto();
            commentDto.setCommentId(comment.getCommentId());
            commentDto.setUserId(comment.getUserId());
            commentDto.setNickname(userRepository.findByUserId(comment.getUserId()).get().getNickname());
            commentDto.setContent(comment.getContent());
            commentDto.setUploadDate(comment.getUploadDate());
            return commentDto;
        }).toList();
    }

    public void create(String userId, int postId, String content) throws NonexistentPostException
    {
        if(postRepository.findByPostId(postId).isEmpty()) throw new NonexistentPostException();
        Comment comment = new Comment();
        comment.setUserId(userId);
        comment.setPostId(postId);
        comment.setContent(content);
        comment.setUploadDate(new Date());
        commentRepository.save(comment);
    }

    public void permissionCheck(String userId, int commentId) throws NonexistentCommentException, PermissionException {
        Comment comment = commentRepository.findByCommentId(commentId).orElseThrow(() -> new NonexistentCommentException());
        if(!comment.getUserId().equals(userId)) throw new PermissionException();
    }

    public void delete(int commentId) throws NonexistentCommentException {
        Comment comment = commentRepository.findByCommentId(commentId).orElseThrow(() -> new NonexistentCommentException());
        commentRepository.deleteByCommentId(commentId);
    }

    public int getPostId(int commentId) throws NonexistentCommentException {
        Comment comment = commentRepository.findByCommentId(commentId).orElseThrow(() -> new NonexistentCommentException());
        return comment.getPostId();
    }

    public void deleteAllByPostId(int postId) throws NonexistentPostException {
        if(postRepository.findByPostId(postId).isEmpty()) throw new NonexistentPostException();
        commentRepository.deleteAllByPostId(postId);
    }

}
