package expression;

import expression.generics.Operators;

public abstract class AbstractBinary<T> implements CommonExpression<T>{
    protected final CommonExpression<T> a, b;
    protected final Operators<T> operator;

    public AbstractBinary(CommonExpression<T> a, CommonExpression<T> b, Operators<T> operator) {
        this.a = a;
        this.b = b;
        this.operator = operator;
    }
    protected abstract T function(T a, T b);
    protected abstract String getChar();

    @Override
    public T evaluate(T x, T y, T z) {
        return function(a.evaluate(x,y,z),b.evaluate(x,y,z));
    }
}
