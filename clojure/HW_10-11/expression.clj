; HW10 - review
; HW11 - delay

; Parser for two homeworks at the end file

; HW 10
(defn constant [v] (fn [a] v))
(defn variable [v] #(get %1 v))
(defn oper [f] (fn[& args] (fn [vars] (apply f (mapv (fn [x] (x vars)) args)))))
(def add (oper +))
(def subtract (oper -))
(def multiply (oper *))
(comment ":NOTE: do not pass variables values explicitly (copy-paste)")
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
(defn OperCons [this f c difr ]
  (fn[& args] (assoc (assoc this
                       :f f
                       :c c
                       :difr difr
                       :args args) :args args)))

(defn Opers[a b c]  ((constructor OperCons OperProto) a b c))
(def consDigit (fn[this f] (assoc this :f f)))
(def Constant (constructor  consDigit {
                                       :evaluate (fn[this vars] (_f this))
                                       :toString (fn [this] (format "%.1f" (_f this)))
                                       :diff (fn [this x] (Constant 0))}))
(def Variable (constructor consDigit {
                                      :evaluate (fn[this vars] (get vars (_f this)))
                                      :toString (fn[this] (_f this))
                                      :diff (fn [this x]
                                              (if (= (_f this) x) (Constant 1) (Constant 0)))}))

(def Multiply)
(defn difr[f] (fn [x & args]
                (let[fo (partial f x)]
                  (if (= (count args) 2) (apply fo args )
                                         (fo (first args) (apply Multiply (rest args)))))))
(def Add (Opers + "+"
                  (fn[x & args] (apply Add (mapv #(diff % x) args)))))
(def Multiply (Opers * "*"
                       (difr (fn[x a b] (Add (Multiply a (diff b x)) (Multiply b (diff a x)))))))
(def Subtract (Opers - "-"
                       (fn[x & args] (apply Subtract (mapv #(diff % x) args)))))
(def Divide (Opers (fn[& args] (reduce #(/ (double %1) (double %2)) args)) "/"
                     (difr (fn[x a b] (Divide (Subtract (Multiply  (diff a x) b) (Multiply a (diff b x))) (Multiply b b))))))
(def Negate (Opers  #(- %) "negate"
                     (fn[x a] (Negate (diff a x)))))
(def Sum (fn[& args] (assoc (Add (Constant 0))
                       :c "sum" :args args)))
(def Avg (Opers (fn[& args] (double (/ (apply + args) (count args)))) "avg"
                (fn[x & args] (diff (Divide (apply Add args) (Constant (count args))) x))))
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
                'negate Negate
                'sum Sum
                'avg Avg})
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
