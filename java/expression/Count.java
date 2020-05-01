package expression;

import expression.generics.Operators;

public class Count<T> extends AbstractUnary<T>  {
    public Count(CommonExpression<T> a, Operators<T> operator) {
        super(a, operator);
    }


    @Override
    public String getChar() {
        return "count";
    }

    public T function(T a) {
        return operator.count(a);
    }
}
