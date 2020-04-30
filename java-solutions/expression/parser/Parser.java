package expression.parser;

import expression.CommonExpression;
import expression.exception.ExpressionException;

public interface Parser {
    // :NOTE: still no parameterization :(
    CommonExpression parse(String expression) throws ExpressionException;
}
