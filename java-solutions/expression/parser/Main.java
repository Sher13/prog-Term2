package expression.parser;

import expression.generics.*;

import java.math.BigInteger;

public class Main {
    public static void main(String[] args) {
        Object a = new ExpressionParser<>(new OpDouble()).parse("count - y")
                .evaluate((double)0,(double)-12,(double)0);
        System.out.println(a);
        Object b = new ExpressionParser<>(new OpInteger()).parse("count -5")
                .evaluate(0,-12,0);
        System.out.println(b);

    }
}
