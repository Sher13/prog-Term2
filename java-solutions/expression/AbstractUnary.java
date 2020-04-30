package expression;

import expression.generics.Operators;

public abstract class AbstractUnary<T> implements CommonExpression<T> {
    protected final CommonExpression<T> a;
    protected final Operators<T> operator;

    public AbstractUnary(CommonExpression<T> a, Operators<T> operator) {
        this.a = a;
        this.operator = operator;
    }
    protected abstract T function(T a);
    protected abstract String getChar();

    @Override
    public T evaluate(T x, T y, T z) {
        return function(a.evaluate(x,y,z));
    }
}
