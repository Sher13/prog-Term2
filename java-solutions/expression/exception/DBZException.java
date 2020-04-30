package expression.exception;

public class DBZException extends ExpressionException{
    public DBZException(int x, int y) {
        super("Division by zero in: " + x + " / " + y);
    }
}
