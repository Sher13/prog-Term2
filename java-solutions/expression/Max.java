package expression;


import expression.generics.Operators;

public class Max<T> extends AbstractBinary<T> {

    public Max(CommonExpression<T> a, CommonExpression<T> b, Operators<T> operator) {
        super(a, b, operator);
    }


    @Override
    public String getChar() {
        return "max";
    }

    public T function(T a, T b) {
        return operator.max(a,b);
    }
}
