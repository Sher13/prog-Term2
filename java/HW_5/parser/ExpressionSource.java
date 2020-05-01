package expression.parser;

import expression.exception.ExpressionException;

public interface ExpressionSource {
    boolean hasNext();
    char next();
    ExpressionException error(final String message);
    default String errorMessage() {
        return "";
    }
}
