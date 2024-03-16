package sangwon.wead.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import sangwon.wead.entity.Board;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class BoardRepository {


    private final JdbcTemplate jdbcTemplate;

    public List<Board> findAll() {
        List<Board> result = jdbcTemplate.query("select * from board", boardRowMapper());
        return result;
    }

    public List<Board> findWithPaging(int pageNumber, int count) {
        int offset = (pageNumber-1)*count;
        List<Board> result = jdbcTemplate.query("select * from board ORDERS LIMIT ? OFFSET ?", boardRowMapper(), count,offset);
        return result;
    }

    public int getCount() {
        int result = jdbcTemplate.queryForObject("select count(*) from board", Integer.class);
        return result;
    }

    public Optional<Board> findByBoardId(int boardId) {
        List<Board> result = jdbcTemplate.query("select * from board where board_id = ?", boardRowMapper(), boardId);
        return result.stream().findAny();
    }

    public void save(Board board) {
        Optional<Board> optionalBoard = this.findByBoardId(board.getBoardId());

        // update
        if(optionalBoard.isPresent()) {
            jdbcTemplate.update("update board set user_id = ?, title = ?, content = ?, upload_date = ?, views = ? " +
                    "where board_id = ?",
                    board.getUserId(),
                    board.getTitle(),
                    board.getContent(),
                    board.getUploadDate(),
                    board.getView(),
                    board.getBoardId()
                    );
        }
        // insert
        else {
            jdbcTemplate.update("insert board(user_id, title, content, upload_date, views) values(?, ?, ?, ?, ?)",
                    board.getUserId(),
                    board.getTitle(),
                    board.getContent(),
                    board.getUploadDate(),
                    board.getView()
            );
        }
    }

    public void deleteByBoardId(int boardId) {
        jdbcTemplate.update("delete from board where board_id = ?", boardId);
    }

    private RowMapper<Board> boardRowMapper() {
        return (rs, rowNum) -> {
            Board board = new Board();
            board.setBoardId(rs.getInt("board_id"));
            board.setUserId(rs.getString("user_id"));
            board.setTitle(rs.getString("title"));
            board.setContent(rs.getString("content"));
            board.setUploadDate(rs.getDate("upload_date"));
            board.setView(rs.getInt("views"));
            return board;
        };
    }

}
