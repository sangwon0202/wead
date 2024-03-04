package sangwon.wead.service;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sangwon.wead.DTO.BoardDto;
import sangwon.wead.DTO.BoardFormDto;
import sangwon.wead.DTO.BoardMetaDataDto;
import sangwon.wead.entity.Board;
import sangwon.wead.repository.BoardRepository;
import sangwon.wead.repository.CommentRepository;
import sangwon.wead.repository.UserRepository;

import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;

    public List<BoardMetaDataDto> getListAll() {

        return boardRepository.findAll().stream().map((board) -> {
            BoardMetaDataDto metaDataDto = new BoardMetaDataDto();

            // 메타데이터 형성
            metaDataDto.setUserId(board.getUserId());
            metaDataDto.setBoardId(board.getBoardId());
            metaDataDto.setTitle(board.getTitle());
            metaDataDto.setUploadDate(board.getUploadDate());
            metaDataDto.setNickname(userRepository.findByUserId(board.getUserId()).get().getNickname());
            metaDataDto.setCommentNumber(commentRepository.findAllByBoardId(board.getBoardId()).size());

            return metaDataDto;
        }).toList();
    }

    public boolean boardExist(int boardId) {
        return boardRepository.findByBoardId(boardId).isPresent();
    }

    public boolean isWriter(String userId, int boardId) {
        return boardRepository.findByBoardId(boardId).get().getUserId().equals(userId);
    }

    public BoardDto read(int boardId) {

        Board board = boardRepository.findByBoardId(boardId).get();
        BoardDto boardDto = new BoardDto();

        boardDto.setBoardId(board.getBoardId());
        boardDto.setTitle(board.getTitle());
        boardDto.setContent(board.getContent());
        boardDto.setUserId(board.getUserId());
        boardDto.setUploadDate(board.getUploadDate());
        boardDto.setNickname(userRepository.findByUserId(board.getUserId()).get().getNickname());

        return boardDto;
    }

    public void create(String userId, BoardFormDto boardFormDto) {

        Board board = new Board();
        board.setUserId(userId);
        board.setTitle(boardFormDto.getTitle());
        board.setContent(boardFormDto.getContent());
        board.setUploadDate(new Date());

        boardRepository.save(board);
    }

    public void update(int boardId, BoardFormDto boardFormDto) {

        Board board = boardRepository.findByBoardId(boardId).get();
        board.setTitle(boardFormDto.getTitle());
        board.setContent(boardFormDto.getContent());

        boardRepository.save(board);
    }

    public void delete(String userId, int boardId) throws Exception {
        boardRepository.deleteByBoardId(boardId);
    }

}
