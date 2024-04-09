package sangwon.wead.exception.client;

public class AlreadyLoginException extends ClientException {
    public AlreadyLoginException() {
        super("이미 로그인되어 있습니다.");
    }
}