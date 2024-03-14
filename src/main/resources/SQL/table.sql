-- 테이블 순서는 관계를 고려하여 한 번에 실행해도 에러가 발생하지 않게 정렬되었습니다.

-- user Table Create SQL
-- 테이블 생성 SQL - user
CREATE TABLE user
(
    `user_id`   VARCHAR(320)    NOT NULL    COMMENT '사용자 아이디',
    `password`  VARCHAR(20)     NOT NULL    COMMENT '비밀번호',
    `nickname`  VARCHAR(10)     NOT NULL    COMMENT '별명',
    PRIMARY KEY (user_id)
);

-- 테이블 Comment 설정 SQL - user
ALTER TABLE user COMMENT '사용자 관련 테이블';


-- board Table Create SQL
-- 테이블 생성 SQL - board
CREATE TABLE board
(
    `board_id`     INT             NOT NULL    AUTO_INCREMENT COMMENT '게시판 아이디',
    `user_id`      VARCHAR(320)    NOT NULL    COMMENT '작성자 아이디',
    `title`        VARCHAR(50)     NOT NULL    COMMENT '제목',
    `content`      TEXT            NOT NULL    COMMENT '내용',
    `upload_date`  DATE            NOT NULL    COMMENT '작성날짜',
    `views`        INT             NULL        COMMENT '조회수',
    PRIMARY KEY (board_id)
);

-- 테이블 Comment 설정 SQL - board
ALTER TABLE board COMMENT '게시판 관련 테이블';

-- Foreign Key 설정 SQL - board(user_id) -> user(user_id)
ALTER TABLE board
    ADD CONSTRAINT FK_board_user_id_user_user_id FOREIGN KEY (user_id)
        REFERENCES user (user_id) ON DELETE RESTRICT ON UPDATE RESTRICT;

-- Foreign Key 삭제 SQL - board(user_id)
-- ALTER TABLE board
-- DROP FOREIGN KEY FK_board_user_id_user_user_id;


-- comment Table Create SQL
-- 테이블 생성 SQL - comment
CREATE TABLE comment
(
    `comment_id`   INT             NOT NULL    AUTO_INCREMENT COMMENT '댓글 아이디',
    `board_id`     INT             NOT NULL    COMMENT '게시판 아이디',
    `user_id`      VARCHAR(320)    NOT NULL    COMMENT '작성자 아이디',
    `content`      VARCHAR(400)    NOT NULL    COMMENT '내용',
    `upload_date`  DATE            NOT NULL    COMMENT '작성날짜',
    PRIMARY KEY (comment_id)
);

-- 테이블 Comment 설정 SQL - comment
ALTER TABLE comment COMMENT '댓글 관련 테이블';

-- Foreign Key 설정 SQL - comment(board_id) -> board(board_id)
ALTER TABLE comment
    ADD CONSTRAINT FK_comment_board_id_board_board_id FOREIGN KEY (board_id)
        REFERENCES board (board_id) ON DELETE RESTRICT ON UPDATE RESTRICT;

-- Foreign Key 삭제 SQL - comment(board_id)
-- ALTER TABLE comment
-- DROP FOREIGN KEY FK_comment_board_id_board_board_id;

-- Foreign Key 설정 SQL - comment(user_id) -> user(user_id)
ALTER TABLE comment
    ADD CONSTRAINT FK_comment_user_id_user_user_id FOREIGN KEY (user_id)
        REFERENCES user (user_id) ON DELETE RESTRICT ON UPDATE RESTRICT;

-- Foreign Key 삭제 SQL - comment(user_id)
-- ALTER TABLE comment
-- DROP FOREIGN KEY FK_comment_user_id_user_user_id;



