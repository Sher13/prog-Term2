package expression;

import expression.generics.Operators;

public class Subtract<T> extends AbstractBinary<T> {

    public Subtract(CommonExpression<T> a, CommonExpression<T> b, Operators<T> operator) {
        super(a, b, operator);
    }

    @Override
    public String getChar() {
        return "-";
    }

    public T function(T a, T b) {
        return operator.sub(a,b);
    }
    public int getPriority() {
        return 1;
    }
    @Override
    public boolean isCommutative() {
        return false;
    }
}
