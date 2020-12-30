%review

tree(X, Key, Left, Right, tree(X, Key, Left, Right)).
% y - max в вершине
tree(null, null, null, null, null).

greater((X, Y), (X1, Y1)) :- X > X1.

split(null, K, TR1, TR2) :- TR1 = null, TR2 = null, !.
split(tree(X, Key, Left, Right), K, TR1, TR2) :-
    greater(K, X), split(Right, K, TR, TR2), tree(X, Key, Left, TR, TR1), !.
split(tree(X, Key, Left, Right), K, TR1, TR2) :-
    split(Left, K, TR1, TR), tree(X, Key, TR, Right, TR2), !.

merge(T1, null, Res) :- Res = T1, !.
merge(null, T2, Res) :- Res = T2, !.
merge(tree(X1, K1, L1, R1), tree(X2, K2, L2, R2), Res) :- 
    K1 > K2, merge(R1, tree(X2, K2, L2, R2), R), tree(X1, K1, L1, R, Res), !.
merge(tree(X1, K1, L1, R1), tree(X2, K2, L2, R2), Res) :- 
    merge(tree(X1, K1, L1, R1), L2, L), tree(X2, K2, L, R2, Res), !.

insert(T, K, Res) :- split(T, K, T1, T2), rand_int(31414, Y), tree(K, Y, null, null, KK), merge(T1, KK, T3), merge(T3, T2, Res), !.
remove(T, K, Res) :- split(T, (K, 0), T1, T2), K1 is K+1, split(T2, (K1, 0), T3, T4), merge(T1, T4, Res), !.

find(tree((Key, Value), _, _, _), Key, Value).
find(tree(X, K, L, R), Key, Value) :- 
    greater(X, (Key, Value)), find(L, Key, Value), !.
find(tree(X, K, L, R), Key, Value) :-  find(R, Key, Value), !.

map_get(T, K, V) :- find(T, K, V).
map_put(T, K, V, Res) :- remove(T, K, X1), insert(X1, (K, V), Res), !.
map_remove(T, K, Res) :- remove(T, K, Res).
build([], T, Res) :- Res = T, !.
build([H | T], Tr, Res) :- insert(Tr, H, T1), build(T, T1, Res), !.
map_build(List, Tree) :- build(List, null, Tree), !.

find_left(tree((X, Y), K, null, R), Res) :- Res = X, !.
find_left(tree(X, K, L, R), Res) :- find_left(L, Res), !.

map_ceilingKey(T, K, Res) :- split(T, (K, 0), T1, T2), find_left(T2, Res), !.