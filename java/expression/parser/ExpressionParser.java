package expression.parser;

import expression.*;
import expression.exception.*;
import expression.generics.Operators;

import java.util.List;

public class ExpressionParser<T extends Number> implements Parser {
    protected final Operators<T> type;

    public ExpressionParser(Operators<T> type) {
        this.type = type;
    }

    @Override
    public CommonExpression<T> parse(String expression) {
        return new OutParser(new StringSource(expression)).parseTop();
    }

    private final static List<List<String>> Bin_Opers = List.of(
            List.of("min", "max"),
            List.of("+", "-"),
            List.of("*", "/")
    );

    private final static List<String> Un_Opers = List.of(
            "count");
    private final static List<String> Var_name = List.of(
            "x", "y", "z"
    );
    private final static List<Character> Spec_Symb = List.of(
            '+', '*', '/', '&', '|', '^', '%'
    );

    private class OutParser extends BaseParser {
        String lastOperators = "";
        protected OutParser(ExpressionSource source) {
            super(source);
            nextChar();
        }

        private CommonExpression<T> parseTop() throws ParserException {
            CommonExpression<T> res = parse(0);
            if (!theEnd()) {
                throw new NoEndException("");
            }
            return res;
        }
        private String getOperators()  {
            if (!lastOperators.equals(""))
                return lastOperators;
            skipWhitespace();
            if (ch == '-') {
                nextChar();
                return "-";
            }
            if (Spec_Symb.contains(ch)) {
                StringBuilder res = new StringBuilder();
                while (Spec_Symb.contains(ch)) {
                    res.append(ch);
                    nextChar();
                }
                return res.toString();
            }
            if (Character.isAlphabetic(ch)) {
                StringBuilder res = new StringBuilder();
                while (Character.isLetterOrDigit(ch)) {
                    res.append(ch);
                    nextChar();
                }
                return res.toString();
            }
            return "";
        }
        CommonExpression<T> createBin(String op, CommonExpression<T> a, CommonExpression<T> b) {
            switch(op) {
                // :NOTE: where is parameterization?
                case "+": return new Add<T>(a,b,type);
                case "-": return new Subtract<T>(a,b,type);
                case "/": return new Divide<T>(a,b,type);
                case "*": return new Multiply<T>(a,b,type);
                case "min": return new Min<T>(a,b,type);
                default:return new Max<T>(a,b,type);
            }
        }

        CommonExpression<T> createUn(String op, CommonExpression<T> a) {
            switch (op) {
                default: return new Count(a, type);
            }
        }

        private CommonExpression<T> parse(int type) throws ParserException {
            if (type == 3) {
                return parseBase();
            }

            CommonExpression<T> left = parse(type + 1);
            List <String> level =Bin_Opers.get(type);
            boolean fl;
            do {
                fl = false;
                String op = getOperators();
                lastOperators = op;
                if (level.contains(op)) {
                    lastOperators = "";
                    left = createBin(op,left, parse(type+1));
                    fl = true;
                }
            } while(fl);
            if (!lastOperators.equals("") && type == 0)
                throw exceptionSwitch(lastOperators);
            return left;
        }
        private CommonExpression<T> parseBase() throws ParserException {
            skipWhitespace();
            if (test('-')) {
                if(between('0','9')) {
                    return checkedParseInteger('-' + parseNumber());
                } else {
                    return new Negate(parseBase(), type);
                }
            } else if (between('0','9')) {
                return checkedParseInteger(parseNumber());
            } else if (test('(')) {
                CommonExpression<T> res = parse(0);
                if (!test(')')) {
                    throw new NotClosedBracketsException(errorMessage());
                }
                return res;
            } else {
                String op = getOperators();
                lastOperators = op;
                if (Un_Opers.contains(op) || Var_name.contains(op)) {
                    lastOperators = "";
                    if (Un_Opers.contains(op)) {
                        return createUn(op, parseBase());
                    } else {
                        return new Variable(op);
                    }
                } else {
                    throw exceptionSwitch(op);
                }
            }
         }
         private Const checkedParseInteger(String s) throws ParserException {
             try {
                 return new Const(type.cast(s));
             } catch (NumberFormatException e) {
                 throw new InvalidConstantException("Invalid integer: " + s + " in " + errorMessage());
             }
         }
        private ParserException exceptionSwitch(String op) {
            for (List<String> level : Bin_Opers) {
                if (level.contains(op)){
                    return new ParserException("No argument for " + op + " in: " + errorMessage());
                }
            }
            if (op.length() == 0) {
                return new ParserException("Empty expression: " + errorMessage());
            }
            return new ParserException("Invalid expression: " + op);
        }
        public String parseNumber() {
            final StringBuilder sb = new StringBuilder();
            StringBuilder result = new StringBuilder();
            while (between('0', '9')) {
                result.append(ch);
                nextChar();
            }
            if (test('.')) {
                sb.append('.');
                while (between('0', '9')) {
                    result.append(ch);
                    nextChar();
                }
            }
            try {
                return result.toString();
            } catch (NumberFormatException e) {
                throw new ParserException("Invalid number " + sb);
            }
        }
    }



}
