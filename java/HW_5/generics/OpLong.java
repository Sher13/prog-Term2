package expression.generics;

import expression.exception.DBZException;
import expression.exception.ExpressionException;

public class OpLong implements Operators<Long> {
    @Override
    public Long sum(Long a, Long b) {
        return a + b;
    }

    @Override
    public Long div(Long a, Long b) throws ExpressionException {
        if (b == 0)
            throw new DBZException(2,0);
        return a / b;
    }

    @Override
    public Long mul(Long a, Long b) {
        return a * b;
    }

    @Override
    public Long sub(Long a, Long b) {
        return a - b;
    }

    @Override
    public Long cast(String s) {
        return Long.valueOf(s);
    }

    @Override
    public Long cast(int i) {
        return (long)i;
    }

    @Override
    public Long count(Long a) {
        return (long)Long.bitCount(a);
    }

    @Override
    public Long min(Long a, Long b) {
        return a > b ? b : a;
    }

    @Override
    public Long max(Long a, Long b) {
        return a < b ? b : a;
    }

    @Override
    public Long negate(Long a) {
        return a*(-1);
    }
}
