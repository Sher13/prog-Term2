package expression;


public class Const<T> implements CommonExpression<T> {
    private T c;

    public Const(T c) {
        this.c = c;
    }

    @Override
    public T evaluate(T x, T y, T z) {
        return this.c;
    }

}
