package sangwon.wead.exception.client;

public class CommentNotFoundException extends ClientException {
    public CommentNotFoundException() {
        super("존재하지 않는 댓글입니다.");
    }
}