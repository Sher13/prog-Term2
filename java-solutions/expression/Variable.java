package expression;

import java.util.Objects;

public class Variable<T> implements CommonExpression<T> {
    final String val;

    public Variable(String val) {
        this.val = val;
    }

    @Override
    public T evaluate(T x, T y, T z) {
        switch (this.val) {
            case ("x"):
                return x;
            case ("y"):
                return y;
            case ("z"):
                return z;
            default:
                throw new IllegalArgumentException("Unknown variable");
        }
    }
}
