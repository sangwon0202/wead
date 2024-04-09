package sangwon.wead.exception.client;

public class PostNotFoundException extends ClientException {
    public PostNotFoundException() {
        super("존재하지 않는 게시글입니다.");
    }
}
