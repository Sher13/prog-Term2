package expression;


import expression.generics.Operators;

public class Add<T> extends AbstractBinary<T> {

    public Add(CommonExpression<T> a, CommonExpression<T> b, Operators<T> operator) {
        super(a, b, operator);
    }


    @Override
    public String getChar() {
        return "+";
    }

    public T function(T a, T b) {
        return operator.sum(a,b);
    }
    public int getPriority() {
        return 1;
    }
}
