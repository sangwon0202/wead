-- 테이블 순서는 관계를 고려하여 한 번에 실행해도 에러가 발생하지 않게 정렬되었습니다.

-- user Table Create SQL
-- 테이블 생성 SQL - user
CREATE TABLE user
(
    `user_id`        VARCHAR(20)    NOT NULL    COMMENT '사용자 아이디',
    `password`       VARCHAR(20)    NOT NULL    COMMENT '비밀번호',
    `nickname`       VARCHAR(10)    NOT NULL    COMMENT '별명',
    `register_date`  DATE           NOT NULL    COMMENT '가입날짜',
    PRIMARY KEY (user_id)
);

-- 테이블 Comment 설정 SQL - user
ALTER TABLE user COMMENT '사용자 관련 테이블';


-- book Table Create SQL
-- 테이블 생성 SQL - book
CREATE TABLE book
(
    `isbn`     VARCHAR(13)     NOT NULL    COMMENT 'isbn',
    `title`    VARCHAR(100)    NOT NULL    COMMENT '제목',
    `image`    VARCHAR(100)    NOT NULL    COMMENT '이미지',
    `author`   VARCHAR(100)    NULL        COMMENT '작가',
    `pubdate`  DATE            NULL        COMMENT '출판일',
    PRIMARY KEY (isbn)
);

-- 테이블 Comment 설정 SQL - book
ALTER TABLE book COMMENT '책 관련 테이블';


-- post Table Create SQL
-- 테이블 생성 SQL - post
CREATE TABLE post
(
    `post_id`      INT            NOT NULL    AUTO_INCREMENT COMMENT '게시글 아이디',
    `user_id`      VARCHAR(20)    NOT NULL    COMMENT '작성자 아이디',
    `title`        VARCHAR(50)    NOT NULL    COMMENT '제목',
    `content`      TEXT           NOT NULL    COMMENT '내용',
    `upload_date`  DATE           NOT NULL    COMMENT '작성날짜',
    `views`        INT            NOT NULL    COMMENT '조회수',
    `isbn`         VARCHAR(13)    NOT NULL    COMMENT 'isbn',
    PRIMARY KEY (post_id)
);

-- 테이블 Comment 설정 SQL - post
ALTER TABLE post COMMENT '게시글 관련 테이블';

-- Foreign Key 설정 SQL - post(user_id) -> user(user_id)
ALTER TABLE post
    ADD CONSTRAINT FK_post_user_id_user_user_id FOREIGN KEY (user_id)
        REFERENCES user (user_id) ON DELETE RESTRICT ON UPDATE RESTRICT;

-- Foreign Key 삭제 SQL - post(user_id)
-- ALTER TABLE post
-- DROP FOREIGN KEY FK_post_user_id_user_user_id;

-- Foreign Key 설정 SQL - post(isbn) -> book(isbn)
ALTER TABLE post
    ADD CONSTRAINT FK_post_isbn_book_isbn FOREIGN KEY (isbn)
        REFERENCES book (isbn) ON DELETE RESTRICT ON UPDATE RESTRICT;

-- Foreign Key 삭제 SQL - post(isbn)
-- ALTER TABLE post
-- DROP FOREIGN KEY FK_post_isbn_book_isbn;


-- comment Table Create SQL
-- 테이블 생성 SQL - comment
CREATE TABLE comment
(
    `comment_id`   INT             NOT NULL    AUTO_INCREMENT COMMENT '댓글 아이디',
    `post_id`      INT             NOT NULL    COMMENT '게시글 아이디',
    `user_id`      VARCHAR(20)     NOT NULL    COMMENT '작성자 아이디',
    `content`      VARCHAR(400)    NOT NULL    COMMENT '내용',
    `upload_date`  DATE            NOT NULL    COMMENT '작성날짜',
    PRIMARY KEY (comment_id)
);

-- 테이블 Comment 설정 SQL - comment
ALTER TABLE comment COMMENT '댓글 관련 테이블';

-- Foreign Key 설정 SQL - comment(post_id) -> post(post_id)
ALTER TABLE comment
    ADD CONSTRAINT FK_comment_post_id_post_post_id FOREIGN KEY (post_id)
        REFERENCES post (post_id) ON DELETE RESTRICT ON UPDATE RESTRICT;

-- Foreign Key 삭제 SQL - comment(post_id)
-- ALTER TABLE comment
-- DROP FOREIGN KEY FK_comment_post_id_post_post_id;

-- Foreign Key 설정 SQL - comment(user_id) -> user(user_id)
ALTER TABLE comment
    ADD CONSTRAINT FK_comment_user_id_user_user_id FOREIGN KEY (user_id)
        REFERENCES user (user_id) ON DELETE RESTRICT ON UPDATE RESTRICT;

-- Foreign Key 삭제 SQL - comment(user_id)
-- ALTER TABLE comment
-- DROP FOREIGN KEY FK_comment_user_id_user_user_id;


