package sangwon.wead.service;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sangwon.wead.DTO.BoardDto;
import sangwon.wead.DTO.BoardFormDto;
import sangwon.wead.DTO.BoardMetaDataDto;
import sangwon.wead.DTO.PageBarDto;
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
            metaDataDto.setViews(board.getView());

            return metaDataDto;
        }).toList();
    }

    public List<BoardMetaDataDto> getListWithPaging(int pageNumber, int boardCountPerPage) {

        return boardRepository.findWithPaging(pageNumber,boardCountPerPage).stream().map((board) -> {
            BoardMetaDataDto metaDataDto = new BoardMetaDataDto();

            // 메타데이터 형성
            metaDataDto.setUserId(board.getUserId());
            metaDataDto.setBoardId(board.getBoardId());
            metaDataDto.setTitle(board.getTitle());
            metaDataDto.setUploadDate(board.getUploadDate());
            metaDataDto.setNickname(userRepository.findByUserId(board.getUserId()).get().getNickname());
            metaDataDto.setCommentNumber(commentRepository.findAllByBoardId(board.getBoardId()).size());
            metaDataDto.setViews(board.getView());

            return metaDataDto;
        }).toList();
    }

    public boolean isPageNumberValid(int pageNumber, int boardCountPerPage) {
        if(pageNumber <= 0) return false;

        int boardCount = boardRepository.getCount();
        if(boardCount == 0) return pageNumber == 1;

        int pageCount = (boardCount-1)/boardCountPerPage + 1;
        return pageNumber <= pageCount;
    }

    public PageBarDto getPageBar(int pageNumber, int boardCountPerPage, int pageCountPerPageBar) {
        int boardCount = boardRepository.getCount();
        int pageCount = (boardCount-1)/boardCountPerPage + 1;
        int pageBarNumber = (pageNumber-1)/pageCountPerPageBar + 1;
        int pageBarCount = (pageCount-1)/pageCountPerPageBar + 1;

        PageBarDto pageBarDto = new PageBarDto();
        pageBarDto.setCurrent(pageNumber);
        pageBarDto.setPrev(pageBarNumber > 1);
        pageBarDto.setNext(pageBarNumber != pageBarCount);
        pageBarDto.setStart((pageBarNumber-1)*pageCountPerPageBar + 1);
        if(pageBarDto.isNext()) pageBarDto.setEnd(pageBarDto.getStart() + pageCountPerPageBar - 1);
        else pageBarDto.setEnd(pageCount);

        return pageBarDto;
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
        boardDto.setViews(board.getView());

        return boardDto;
    }

    public void create(String userId, BoardFormDto boardFormDto) {
        Board board = new Board();
        board.setUserId(userId);
        board.setTitle(boardFormDto.getTitle());
        board.setContent(boardFormDto.getContent());
        board.setUploadDate(new Date());
        board.setView(0);

        boardRepository.save(board);
    }

    public void update(int boardId, BoardFormDto boardFormDto) {
        Board board = boardRepository.findByBoardId(boardId).get();
        board.setTitle(boardFormDto.getTitle());
        board.setContent(boardFormDto.getContent());

        boardRepository.save(board);
    }

    public void delete(int boardId) {
        boardRepository.deleteByBoardId(boardId);
    }

    public void increaseViews(int boardId) {
        Board board = boardRepository.findByBoardId(boardId).get();
        board.setView(board.getView()+1);
        boardRepository.save(board);
    }
}
