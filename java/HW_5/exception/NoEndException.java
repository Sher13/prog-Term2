package expression.exception;

public class NoEndException extends ParserException {
    public NoEndException(String message) {
        super("Non-empty tail after parsing expression: " + message);
    }
}
