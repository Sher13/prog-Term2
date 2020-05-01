package expression.exception;

public class NotClosedBracketsException extends ParserException {
    public NotClosedBracketsException(String message) {
        super("Expected" + message);
    }
}
