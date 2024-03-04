package sangwon.wead.service;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sangwon.wead.DTO.BoardDto;
import sangwon.wead.DTO.BoardFormDto;
import sangwon.wead.DTO.BoardMetaDataDto;
import sangwon.wead.entity.Board;
import sangwon.wead.exception.BoardNotExistException;
import sangwon.wead.exception.NoPermissionException;
import sangwon.wead.exception.UserNotExistException;
import sangwon.wead.repository.BoardRepository;
import sangwon.wead.repository.CommentRepository;
import sangwon.wead.repository.UserRepository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;

    public List<BoardMetaDataDto> getListAll() {

        List<BoardMetaDataDto> list = boardRepository.findAll().stream().map((board) -> {
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

        return list;
    }

    public BoardDto read(int boardId) throws Exception {

        // 게시글이 존재하는지 확인
        Optional<Board> optionalBoard = boardRepository.findByBoardId(boardId);
        if(optionalBoard.isEmpty()) throw new BoardNotExistException();

        Board board = optionalBoard.get();
        BoardDto boardDto = new BoardDto();

        boardDto.setBoardId(board.getBoardId());
        boardDto.setTitle(board.getTitle());
        boardDto.setContent(board.getContent());
        boardDto.setUserId(board.getUserId());
        boardDto.setUploadDate(board.getUploadDate());
        boardDto.setNickname(userRepository.findByUserId(board.getUserId()).get().getNickname());

        return boardDto;
    }

    public void create(String userId, BoardFormDto boardFormDto) throws Exception {

        // 사용자가 존재하는지 확인
        if(userRepository.findByUserId(userId).isEmpty()) throw new UserNotExistException();

        Board board = new Board();
        board.setUserId(userId);
        board.setTitle(boardFormDto.getTitle());
        board.setContent(boardFormDto.getContent());
        board.setUploadDate(new Date());

        boardRepository.save(board);
    }

    public void update(String userId, int boardId, BoardFormDto boardFormDto) throws Exception {

        // 사용자가 존재하는지 확인
        if(userRepository.findByUserId(userId).isEmpty()) throw new UserNotExistException();

        // 게시글이 존재하는지 확인
        Optional<Board> optionalBoard = boardRepository.findByBoardId(boardId);
        if(optionalBoard.isEmpty()) throw new BoardNotExistException();

        // 게시글 수정 권한 확인
        Board board = optionalBoard.get();
        if(!board.getUserId().equals(userId)) throw new NoPermissionException();


        board.setTitle(boardFormDto.getTitle());
        board.setContent(boardFormDto.getContent());

        boardRepository.save(board);
    }

    public void delete(String userId, int boardId) throws Exception {

        // 사용자가 존재하는지 확인
        if(userRepository.findByUserId(userId).isEmpty()) throw new UserNotExistException();

        // 게시글이 존재하는지 확인
        Optional<Board> optionalBoard = boardRepository.findByBoardId(boardId);
        if(optionalBoard.isEmpty()) throw new BoardNotExistException();

        // 게시글 수정 권한 확인
        Board board = optionalBoard.get();
        if(!board.getUserId().equals(userId)) throw new NoPermissionException();

        boardRepository.deleteByBoardId(boardId);

    }

}
