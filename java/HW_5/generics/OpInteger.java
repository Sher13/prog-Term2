package expression.generics;

import expression.exception.DBZException;
import expression.exception.ExpressionException;
import expression.exception.OverflowException;

public class OpInteger implements Operators<Integer> {
    public Integer sum(Integer x, Integer y) throws ExpressionException {
        if ((x > 0 && Integer.MAX_VALUE - x < y)
                || (x < 0 && Integer.MIN_VALUE - x > y)) {
            throw new OverflowException("add", x + " + " + y);
        }
        return x + y;
    }
    public Integer sub(Integer x, Integer y) throws ExpressionException{
        if ((y > 0 && Integer.MIN_VALUE + y > x)
                || (y < 0 && Integer.MAX_VALUE + y < x)) {
            throw new OverflowException("subtract", x + " - " + y);
        }
        return x - y;
    }

    @Override
    public Integer cast(String s) {
        return Integer.valueOf(s);
    }

    @Override
    public Integer cast(int i) {
        return i;
    }

    public Integer mul(Integer x, Integer y) throws ExpressionException {
        if (x != 0 && y != 0 && ((x * y) / y != x || (x * y) / x != y)) {
            throw new OverflowException("multiply", x + " * " + y);
        }
        return x * y;
    }
    public Integer div(Integer x, Integer y) throws ExpressionException {
        if (y == 0) {
            throw new DBZException(x, y);
        }
        if (y == -1 && x == Integer.MIN_VALUE) {
            throw new OverflowException("divide", x + " / " + y);
        }
        return x / y;
    }

    @Override
    public Integer count(Integer a) {
        return Integer.bitCount(a);
    }

    @Override
    public Integer min(Integer a, Integer b) {
        return a > b ? b : a;
    }

    @Override
    public Integer max(Integer a, Integer b) {
        return a < b ? b : a;
    }

    @Override
    public Integer negate(Integer a) {
        if (a == Integer.MIN_VALUE) {
            throw new OverflowException("negate", " - " + a);
        }
        return -a;
    }
}
