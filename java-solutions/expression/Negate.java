package expression;

import expression.exception.OverflowException;
import expression.generics.Operators;

public class Negate<T> extends AbstractUnary<T>{
    public Negate(CommonExpression<T> a,  Operators<T> operator) {
        super(a,operator);
    }
    @Override
    protected String getChar() {
        return  "-";
    }

    @Override
    public T function(T x) {
        return operator.negate(x);
    }
}
