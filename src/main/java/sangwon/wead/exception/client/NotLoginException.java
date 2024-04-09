package sangwon.wead.exception.client;

public class NotLoginException extends ClientException {
    public NotLoginException() {
        super("로그인이 되어 있지 않습니다.");
    }
}

