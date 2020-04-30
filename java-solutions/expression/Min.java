package expression;


import expression.generics.Operators;

public class Min<T> extends AbstractBinary<T> {

    public Min(CommonExpression<T> a, CommonExpression<T> b, Operators<T> operator) {
        super(a, b, operator);
    }


    @Override
    public String getChar() {
        return "min";
    }

    public T function(T a, T b) {
        return operator.min(a,b);
    }
}
