package expression.exception;

public class OverflowException extends ExpressionException {
    public OverflowException(String operation, String expression) {
        super("Overflow in " + operation + ": " + expression);
    }
}
