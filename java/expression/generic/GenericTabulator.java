package expression.generic;

import expression.CommonExpression;
import expression.exception.ExpressionException;
import expression.generic.Tabulator;
import expression.generics.*;
import expression.parser.ExpressionParser;
import expression.parser.Parser;

import java.math.BigInteger;
import java.util.Map;

public class GenericTabulator implements Tabulator {
    // :NOTE: wrong package of this class + Tabulator is missed -> tests failed
    @Override
    public Object[][][] tabulate(String mode, String expression, int x1, int x2, int y1, int y2, int z1, int z2) throws Exception {
        Map<String, Operators<? extends Number> > MODE = Map.of(
                "i", new OpInteger(),
                "d", new OpDouble(),
                "bi", new OpBigInteger(),
                "u", new OpInt(),
                "l", new OpLong(),
                "s", new OpShort()
        );
        if (MODE.containsKey(mode)) {
            return create(MODE.get(mode), expression, x1, x2, y1, y2, z1, z2);
        } else {
            throw new ExpressionException("This mode doesn't exist.");
        }
    }
    private <T extends Number> Object[][][] create(Operators<T> op, String expression, int x1, int x2, int y1, int y2, int z1, int z2) {
        Object[][][] ans = new Object[x2-x1+1][y2-y1+1][z2-z1+1];
        ExpressionParser<T> parser= new ExpressionParser<>(op);
        CommonExpression<T> expr = parser.parse(expression);
        for (int i = x1; i <= x2; i++) {
            for (int j = y1; j <= y2; j++) {
                for (int k = z1; k<= z2; k++) {
                    try {
                        ans[i - x1][j - y1][k - z1] = expr.evaluate(op.cast(i), op.cast(j), op.cast(k));
                    } catch (ExpressionException e) {
                        ans[i-x1][j-y1][k-z1] = null;
                    }
                }
            }
        }
        return ans;
    }
}
