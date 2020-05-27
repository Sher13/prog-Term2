comp(1).
dv(X, N) :- (X mod N) =:= 0.
check(X, N) :- N > 1, (dv(X, N); N1 is N - 1, check(X, N1)).
next(Z, Y) :- not comp(Z), Y is Z, !; Z1 is Z + 1, next(Z1, Y).
set(R, X, Mx) :- R < Mx, assertz(comp(R)), R1 is R + X, set(R1, X, Mx).
eurat(T, Mx) :- T * T < Mx, T3 is T * T, not set(T3,T,Mx), T1 is T + 1, next(T1, T2), eurat(T2, Mx).
init(X) :- eurat(2, X).
prime(N) :- not comp(N), !.
composite(N) :- N > 2, not prime(N).
prime_divisors(1, []).
prime_divisors(N, [N]) :- prime(N), !.
min(N, X, Ans) :- X*X =< N, dv(N, X), prime(X), Ans is X; X*X =< N, X1 is X + 1, min(N, X1,Ans). 
sort([H]).
sort([H, H1 | T]) :- H =< H1.
prime_divisors(N, [H | T]) :-
  number(N),
	N > 1,
	min(N, 2, H),
	N1 is N / H,
	print(N1),
	prime_divisors(N1, T),
	sort([H | T]), !.
prime_divisors(N, [H | T]) :-
 not number(N),
 prime(H),
 prime_divisors(N1, T),
 sort([H | T]),
 N is N1 * H, !.