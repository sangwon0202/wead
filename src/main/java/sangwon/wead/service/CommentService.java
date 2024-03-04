package sangwon.wead.service;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sangwon.wead.DTO.CommentDto;
import sangwon.wead.entity.Comment;
import sangwon.wead.repository.CommentRepository;
import sangwon.wead.repository.UserRepository;

import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;


    public boolean commentExist(int commentId) {
        return commentRepository.findByCommentId(commentId).isPresent();
    }
    public boolean isWriter(String userId, int commentId) {
        return commentRepository.findByCommentId(commentId).get().getUserId().equals(userId);
    }

    public List<CommentDto> getCommentList(int boardId) {
        return commentRepository.findAllByBoardId(boardId).stream().map((comment) -> {
            CommentDto commentDto = new CommentDto();
            commentDto.setCommentId(comment.getCommentId());
            commentDto.setUserId(comment.getUserId());
            commentDto.setNickname(userRepository.findByUserId(comment.getUserId()).get().getNickname());
            commentDto.setContent(comment.getContent());
            commentDto.setUploadDate(comment.getUploadDate());
            return commentDto;
        }).toList();
    }

    public void create(String userId, int boardId, String content) {
        Comment comment = new Comment();
        comment.setUserId(userId);
        comment.setBoardId(boardId);
        comment.setContent(content);
        comment.setUploadDate(new Date());

        commentRepository.save(comment);
    }

    public void delete(int commentId) {
        commentRepository.deleteByCommentId(commentId);
    }

}
