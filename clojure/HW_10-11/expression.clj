; review

; Parser for two homeworks at the end file

; HW 10
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

; HW 11

(defn proto-get [obj key]
  (cond
    (contains? obj key) (obj key)
    (contains? obj :proto) (proto-get (obj :proto) key)))

(defn proto-call [obj key & args]
  (apply (proto-get obj key) obj args))

(defn field [key] #(proto-get % key))

(defn proto-call [obj key & args]
  (apply (proto-get obj key) obj args))

(defn method [key]
  (fn [obj & args] (apply proto-call obj key args)))

(def _f (field :f))
(def _c (field :c))
(def _args (field :args))
(def _difr (field :difr))
(def diff (method :diff))
(def evaluate (method :evaluate))
(def toString (method :toString))
(defn constructor [cons proto]
  (fn[& args] (apply cons {:proto proto} args)))

(def OperProto
  {:evaluate (fn[this vars] (apply (_f this) (mapv (fn [x] (evaluate x vars)) (_args this))))
   :toString (fn[this] (str "(" (_c this) (apply str
                                                     (mapv (fn[x] (str " " (toString x))) (_args this))) ")"))
   :diff (fn[this x] (apply (partial (_difr this) x) (_args this)))})
(defn OperCons [this f c difr & args]
  (assoc this
    :f f
    :c c
    :difr difr
    :args args))

(def Opers (constructor OperCons OperProto))
(def Constant (constructor (fn[this f] (assoc this :f f)) {
                                                           :evaluate (fn[this vars] (_f this))
                                                           :toString (fn [this] (format "%.1f" (_f this)))
                                                           :diff (fn [this x] (Constant 0))}))
(def Variable (constructor (fn[this f] (assoc this :f f)) {
                                                           :evaluate (fn[this vars] (get vars (_f this)))
                                                           :toString (fn[this] (_f this))
                                                           :diff (fn [this x]
                                                                   (if (= (_f this) x) (Constant 1) (Constant 0)))}))

(def Multiply)
(def Add (partial Opers + "+"
                  (fn[x & args] (apply Add (mapv #(diff % x) args)))))
(def Multiply (partial Opers * "*"
                       (fn[x a b] (Add (Multiply a (diff b x)) (Multiply b (diff a x))))))
(def Subtract (partial Opers - "-"
                       (fn[x & args] (apply Subtract (mapv #(diff % x) args)))))
(def Divide (partial Opers (fn[& args] (reduce #(/ (double %1) (double %2)) args)) "/"
                     (fn[x a b] (Divide (Subtract (Multiply  (diff a x) b) (Multiply a (diff b x))) (Multiply b b)))))
(def Negate (partial Opers (fn[a] (- a)) "negate"
                     (fn[x a] (Negate (diff a x)))))

; Parser

(def OPERS {
            '+ add
            '- subtract
            '* multiply
            '/ divide
            'negate negate
            'med med
            'avg avg})

(def OPERS_OBJ {
                '+ Add
                '- Subtract
                '* Multiply
                '/ Divide
                'negate Negate})
(def Operators (vector OPERS OPERS_OBJ))

(defn parseDigits [x fl]
  (if (number? x) (if (= fl 0) (constant x) (Constant x))
                  (if (= fl 0) (variable (name x)) (Variable (name x)))))
(defn parseArgs[expr fl]
  (letfn [(parse [expr] (apply (get (nth Operators fl) (first expr)) (parseArgs (rest expr) fl)))]
    (mapv #(if (list? %1) (parse %1) (parseDigits %1 fl)) expr)))
(defn parseFunction [s]
  (let [expr (read-string s)]
    (if (list? expr)
      (apply (get OPERS (first expr)) (parseArgs (rest expr) 0))
      (parseDigits expr 0))))

(defn parseObject [s]
  (let [expr (read-string s)]
    (if (list? expr)
      (apply (get OPERS_OBJ (first expr)) (parseArgs (rest expr) 1))
      (parseDigits expr 1))))
