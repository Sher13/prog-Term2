package expression.generics;

import expression.exception.DBZException;
import expression.exception.ExpressionException;

public class OpShort implements Operators<Short> {
    @Override
    public Short sum(Short a, Short b) {
        return (short)(a+b);
    }

    @Override
    public Short div(Short a, Short b) throws ExpressionException {
        if (b == 0)
            throw new DBZException(a,b);
        return (short)(a/b);
    }

    @Override
    public Short mul(Short a, Short b) {
        return (short)(a*b);
    }

    @Override
    public Short sub(Short a, Short b) {
        return (short)(a-b);
    }

    @Override
    public Short cast(String s) {
        return Short.valueOf(s);
    }

    @Override
    public Short cast(int i) {
        return (short)i;
    }

    @Override
    public Short count(Short a) {
        short b =  (short)(Integer.bitCount(a));
        return b >= 16 ? (short)(b-16) : b;
    }

    @Override
    public Short min(Short a, Short b) {
        return a > b ? b : a;
    }

    @Override
    public Short max(Short a, Short b) {
        return a < b ? b : a;
    }

    @Override
    public Short negate(Short a) {
        return (short)(a*(-1));
    }
}
