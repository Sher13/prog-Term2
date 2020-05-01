package expression.generics;

import expression.exception.DBZException;
import expression.exception.ExpressionException;
import expression.exception.OverflowException;

import java.net.Inet4Address;

public class OpInt implements Operators<Integer> {
    public Integer sum(Integer x, Integer y) {
        return x + y;
    }
    public Integer sub(Integer x, Integer y) {
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

    @Override
    public Integer mul(Integer x, Integer y) {
        return x * y;
    }

    @Override
    public Integer div(Integer x, Integer y) {
        if (y == 0) {
            throw new DBZException(x, y);
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
        return a*(-1);
    }
}
