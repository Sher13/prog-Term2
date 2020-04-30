package expression.generics;

import expression.exception.DBZException;
import expression.exception.ExpressionException;

import java.math.BigInteger;

public class OpBigInteger implements Operators<BigInteger> {
    @Override
    public BigInteger sum(BigInteger a, BigInteger b) {
        return a.add(b);
    }

    @Override
    public BigInteger div(BigInteger a, BigInteger b) throws ExpressionException {
        // :NOTE: use `BigInteger.ZERO` instead
        if (b.equals(BigInteger.ZERO))
            throw new DBZException(2,0);
        return a.divide(b);
    }

    @Override
    public BigInteger mul(BigInteger a, BigInteger b) {
        return a.multiply(b);
    }

    @Override
    public BigInteger sub(BigInteger a, BigInteger b) {
        return a.subtract(b);
    }

    @Override
    public BigInteger cast(String s) {
        return new BigInteger(s);
    }

    @Override
    public BigInteger cast(int i) {
        return BigInteger.valueOf(i);
    }

    @Override
    public BigInteger count(BigInteger a) { return BigInteger.valueOf(a.bitCount());}

    @Override
    public BigInteger min(BigInteger a, BigInteger b) {
        // :NOTE: use `BigInteger#min` instead
        return a.min(b);
    }

    @Override
    public BigInteger max(BigInteger a, BigInteger b) {
        return a.max(b);
    }

    @Override
    public BigInteger negate(BigInteger a) {
        return a.negate();
    }
}
