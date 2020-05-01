package expression;

// :NOTE: where is bounds on generic type? Now I can divide String by String
// :NOTE 2: still no bounds
public interface CommonExpression<T> extends  TripleExpression<T> {
    default int getPriority() {
        return -1;
    }
    default boolean isCommutative() {
        return true;
    }
}
