package expression;

import expression.generics.Operators;

public class Divide<T> extends AbstractBinary<T> {

    public Divide(CommonExpression<T> a, CommonExpression<T> b, Operators<T> operator) {
        super(a, b, operator);
    }

    @Override
    public String getChar() {
        return "/";
    }

    public T function(T a, T b) {
        return operator.div(a,b);
    }
    public int getPriority() {
        return 0;
    }
    @Override
    public boolean isCommutative() {
        return false;
    }
}
