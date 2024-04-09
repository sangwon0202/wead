package sangwon.wead.exception.client;

public class InvalidIsbnException extends ClientException {
    public InvalidIsbnException() {
        super("유효하지 않은 ISBN 입니다.");
    }
}
