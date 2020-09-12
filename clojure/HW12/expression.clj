
; HW 10
(defn constant [v] (constantly v))
(defn variable [v] #(get %1 v))
(defn oper [f] (fn[& args] (fn [vars] (f (mapv (fn [x] (x vars)) args)))))
(defn easy_op [f] (oper (partial apply f)))
(def add (easy_op +))
(def subtract (easy_op -))
(def multiply (easy_op *))
(def divide (oper (partial reduce #(/ (double %1) (double %2)))))
(def negate (easy_op -))
(def med (oper #(nth (sort %) (quot (count %) 2))))
(def avg (oper #(/ (double (apply + %)) (double (count %)))))

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
(def toStringInfix (method :toStringInfix))
(defn constructor [cons proto]
  (fn[& args] (apply cons {:proto proto} args)))

(def OperProto
  {:evaluate (fn[this vars] (apply (_f this) (mapv (fn [x] (evaluate x vars)) (_args this))))
   :toString (fn[this] (str "(" (_c this) (apply str
                                                 (mapv (fn[x] (str " " (toString x))) (_args this))) ")"))
   :toStringInfix (fn[this] (if (= 2 (count (_args this))) (str "(" (toStringInfix (first (_args this))) " " (_c this) " " (toStringInfix (last (_args this))) ")")
                                                           (str (_c this) "(" (toStringInfix (first (_args this))) ")")))
   :diff (fn[this x] ((_difr this) (_args this) (mapv diff (_args this) (mapv (constantly x) (_args this)))))})
(defn OperCons [this f c difr ]
  (fn[& args] (assoc (assoc this
                       :f f
                       :c c
                       :difr difr) :args args)))

(defn Opers[f name diff]  ((constructor OperCons OperProto) f name diff))
(def consDigit (fn[this f] (assoc this :f f)))
(def Constant (constructor  consDigit {
                                       :evaluate (fn[this vars] (_f this))
                                       :toString (fn [this] (format "%.1f" (double (_f this))))
                                       :toStringInfix (fn [this] (format "%.1f" (double (_f this))))
                                       :diff (fn [this x] (Constant 0))}))
(def Variable (constructor consDigit {
                                      :evaluate (fn[this vars] (get vars (_f this)))
                                      :toString (fn[this] (_f this))
                                      :toStringInfix (fn[this] (_f this))
                                      :diff (fn [this x]
                                              (if (= (_f this) x) (Constant 1) (Constant 0)))}))

(declare Multiply)
(defn DifMultiply[args args'] (mapv #(apply (partial Multiply (nth args' %))
                                            (concat (take % args) (take-last (- (count args) % 1) args))) (range 0 (count args))))

(def Add (Opers + "+"
                #(apply Add %2)))
(def Multiply (Opers * "*"
                     #(apply Add (DifMultiply %1 %2))))
(def Subtract (Opers - "-"
                     #(apply Subtract %2)))
(def Divide (Opers (fn[& args] (reduce #(/ (double %1) (double %2)) args)) "/"
                    #(Divide (apply Subtract (DifMultiply %1 %2))
                             (Multiply (apply Multiply (rest %1)) (apply Multiply (rest %1))))))
(def Negate (Opers  #(- %) "negate"
                      #(apply Negate %2)))
(def Sum (fn[& args] (assoc (Add (Constant 0))
                       :c "sum" :args args)))
(def Avg (Opers (fn[& args] (double (/ (apply + args) (count args)))) "avg"
               #(Divide (apply Add %2) (Constant (count %1)))))

(def Pow (Opers #(Math/pow %1 %2) "**" nil))
(def Log (Opers #(/  (double (Math/log (Math/abs %2)))  (double (Math/log (Math/abs %1)))) "//" nil))

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

; HW 12


(defn -return [value tail] {:value value :tail tail})
(def -valid? boolean)
(def -value :value)
(def -tail :tail)

(defn _show [result]
  (if (-valid? result) (str "-> " (pr-str (-value result)) " | " (pr-str (apply str (-tail result))))
                       "!"))
(defn tabulate [parser inputs]
  (run! (fn [input] (printf "    %-10s %s\n" (pr-str input) (_show (parser input)))) inputs))


(defn _empty [value] (partial -return value))
(defn _char [p]
  (fn [[c & cs]]
    (if (and c (p c)) (-return c cs))))
(defn _map [f result]
  (if (-valid? result)
    (-return (f (-value result)) (-tail result))))

(defn _combine [f a b]
  (fn [str]
    (let [ar ((force a) str)]
      (if (-valid? ar)
        (_map (partial f (-value ar))
              ((force b) (-tail ar)))))))

(defn _either [a b]
  (fn [str]
    (let [ar ((force a) str)]
      (if (-valid? ar) ar ((force b) str)))))

(defn _parser [p]
  (fn [input]
    (-value ((_combine (fn [v _] v) p (_char #{\u0000})) (str input \u0000)))))

(defn +char [chars] (_char (set chars)))

(defn +char-not [chars] (_char (comp not (set chars))))

(defn +map [f parser] (comp (partial _map f) parser))

(def +parser _parser)

(def +ignore (partial +map (constantly 'ignore)))

(defn iconj [coll value]
  (if (= value 'ignore) coll (conj coll value)))

(defn +seq [& ps]
  (reduce (partial _combine iconj) (_empty []) ps))

(defn +string [st] (apply +seq (mapv #(+char (str %) )st)))

(defn +seqf [f & ps] (+map (partial apply f) (apply +seq ps)))

(defn +seqn [n & ps] (apply +seqf (fn [& vs] (nth vs n)) ps))

(defn +or [p & ps]
  (reduce _either p ps))
(defn +opt [p]
  (+or p (_empty nil)))

(defn +star [p]
  (letfn [(rec [] (+or (+seqf cons p (delay (rec))) (_empty ())))] (rec)))

(defn +plus [p] (+seqf cons p (+star p)))
(defn +str [p] (+map (partial apply str) p))

(def *digit (+char "0123456789"))
(def *number (+map read-string (+str (+plus *digit))))
(def *double (+map read-string (+seqf str (+opt (+char "-")) *number (+opt (+seqf str (+char ".") *number)))))

(def *space (+char " \t\n\r"))
(def *ws (+ignore (+star *space)))
(defn or_string [args] (apply +or
                              (mapv #(+string (str %))  args)))

(def OPERS [
            [{
              '** Pow
              (symbol "//") Log} 0]
            [{
             '* Multiply
             '/ Divide} 1]
            [{
              '+ Add
             '- Subtract} 1]])

(def UNARY {
               'negate Negate})

(defn setOpers[maps] (+seqf  #(get maps (symbol (apply str %))) (or_string (keys maps))))

(def *Unary (setOpers UNARY))
(def *Vars (+seqf #(Variable (apply str %)) (or_string ['x 'y 'z])))
(def *Const (+seqf #(Constant %) *double))
(def *Ones (+or *Const *Vars ))
(def *arg (+seqf identity *ws *Ones *ws))
(defn left[a b]  (if (empty? b) a
                             (reduce #((first %2) %1 (last %2)) (concat [a] b))))
(defn right[a b] (if (empty? b) a
                             (letfn [(f[op b a] (op a b))]
                               ((reduce #(partial f (first %2) (%1 (last %2))) (concat [identity] (reverse b))) a))))
(defn unary[a b] (a b))
(defn after[op arg] (+star (+seqf (fn[& a] (flatten a)) *ws op arg)))

(declare *expr)

(def *base (+or *arg
             (+seqf unary *ws *Unary *ws (delay *base))
             (+seqn 1 (+seq *ws (+char "(") *ws) (delay *expr) (+seq *ws (+char ")") *ws))))
(def ASSOCIATES {0 right
                 1 left})

(defn setLevels[n] (if (= n -1) *base
                               (let [n' (dec n)
                                     level (nth OPERS n)
                                     associate (get ASSOCIATES (last level))
                                     *Opers (setOpers (first level))
                                     *next (setLevels n')]
                                 (+seqf associate *next (after *Opers *next)))))

(def *expr (setLevels 2))


(defn parseObjectInfix[expr] ((_parser *expr) expr))


