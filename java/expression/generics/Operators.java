package expression.generics;

public interface Operators<T> {
    T sum(T a, T b) ;
    T div(T a, T b) ;
    T mul(T a, T b) ;
    T sub(T a, T b) ;
    T cast(String s);
    T cast(int i);
    T count(T a);
    T min(T a, T b);
    T max(T a, T b);
    T negate(T a);
}
