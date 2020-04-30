package expression;

import expression.generics.Operators;

public class Multiply<T> extends AbstractBinary<T> {

    public Multiply(CommonExpression<T> a, CommonExpression<T> b, Operators<T> operator) {
        super(a, b, operator);
    }

    @Override
    public String getChar() {
        return "*";
    }

    public T function(T a, T b) {
        return operator.mul(a,b);
    }
    public int getPriority() {
        return 0;
    }
}
