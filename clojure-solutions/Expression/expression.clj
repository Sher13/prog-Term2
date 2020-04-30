(defn constant [v] (fn [a] v))
(defn variable [v] #(get %1 v))
(defn oper [f args] (fn [vars] (apply f (mapv (fn [x] (x vars)) args))))
(defn add [& a] (oper + a))
(defn subtract [& a] (oper - a))
(defn multiply [& a] (oper * a))
(defn divide [& a] (fn[vars] (reduce #(/ (double %1) (double %2))  (mapv #(%1 vars) a))))
(defn negate [a] (fn[vars] (- (a vars))))
(defn med[& a] (fn[vars] (#(nth (sort %1) (quot (count a) 2)) (mapv #(%1 vars) a))))
(defn avg[& a] (fn[vars] (/ (double (apply + (mapv (fn[x] (x vars)) a))) (double (count a)))))

(def OPERS {
            '+ add
            '- subtract
            '* multiply
            '/ divide
            'negate negate
            'med med
            'avg avg})

(defn parseDigits [x]
  (if (number? x) (constant x) (variable (name x))))
(defn parseArgs[expr]
  (letfn [(parse [expr] (apply (get OPERS (first expr)) (parseArgs (rest expr))))]
    (mapv #(if (list? %1) (parse %1) (parseDigits %1)) expr)))
(defn parseFunction [s]
  (let [expr (read-string s)]
    (if (list? expr)
    (apply (get OPERS (first expr)) (parseArgs (rest expr)))
    (parseDigits expr))))