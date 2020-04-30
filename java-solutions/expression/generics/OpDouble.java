package expression.generics;

import static java.lang.Math.abs;

public class OpDouble implements Operators<Double> {

    @Override
    public Double sum(Double a, Double b) {
        return a + b;
    }

    @Override
    public Double div(Double a, Double b) {
        return a / b;
    }

    @Override
    public Double mul(Double a, Double b) {
        return a * b;
    }

    @Override
    public Double sub(Double a, Double b) {
        return a - b;
    }

    @Override
    public Double cast(String s) {
        return Double.valueOf(s);
    }

    @Override
    public Double cast(int i) {
        return (double)i;
    }

    @Override
    public Double count(Double a) {
        return (double)Long.bitCount(Double.doubleToLongBits(a));
    }

    @Override
    public Double min(Double a, Double b) {
        return a >= b ? b : a;
    }

    @Override
    public Double max(Double a, Double b) {
        return a <= b ? b : a;
    }

    @Override
    public Double negate(Double a) {
        return a*(-1);
    }
}
