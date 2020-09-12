function create(constructor, evaluate, toString, diff) {
    constructor.prototype.evaluate = evaluate;
    constructor.prototype.toString = toString;
    constructor.prototype.diff = diff;
}

function Const(value) {
    this.value = value;
}
create(
    Const,
    function () {
        return this.value;
    },
    function () {
        return "" + this.value;
    },
    function () {
        return Const.ZERO;
    });

Const.ZERO = new Const(0);
Const.ONE = new Const(1);
Const.E = new Const(Math.E);

function Variable(name) {
    this.value = name;
}

create(
    Variable,
    function (...vals) {
        switch (this.value) {
            case "x" : return vals[0];
            case "y" : return vals[1];
            case "z" : return vals[2];
        }
    },
    function() {
        return this.value;
    },
    function (d) {
        if (this.value === d)
            return Const.ONE;
        return Const.ZERO;
    });

function operation( ...args) {
        this.args = args;
}
create(
    operation,
        function(...vals){ return this.f(...this.args.map(el => el.evaluate(...vals)))},
        function() {
            return this.args.map(el => el.toString()).join(" ") + " " + this.char;
        },
        function(d) {
            return this.difr(d, ...this.args);
    });
function createOperation(f, char, difr, count) {
    let constructor = function (...args) {
        operation.call(this,...args);
    };
    constructor.getCount = count;
    constructor.prototype = Object.create(operation.prototype);
    constructor.prototype.f = f;
    constructor.prototype.char = char;
    constructor.prototype.difr = difr;
    return constructor;
};

const Multiply = createOperation(
    (a,b) => (a*b),
     "*",
     function(d,a,b){
        return new Add(new Multiply(a.diff(d),b),new Multiply(a, b.diff(d)));
    },
    2);

const Add = createOperation(
     (a,b) => (a+b),
    "+",
    function (d,a,b) {
        return new Add(a.diff(d), b.diff(d));
    },
    2);

const Subtract = createOperation(
    (a,b) => (a-b),
    "-",
    function (d,a,b) {
        return new Subtract(a.diff(d), b.diff(d));
    },
    2);

const Divide = createOperation(
    (a,b) => (a/b),
    "/",
    function (d,a,b) {
        return new Divide(new Subtract(new Multiply(a.diff(d),b), new Multiply(a,b.diff(d))), new Multiply(b,b));
    },
    2);

const Negate = createOperation(
    (a) => (-a),
    "negate",
    function (d,a) {
        return new Negate(a.diff(d));
    },
    1);
const Power = createOperation(
    (a,b) => (Math.pow(a,b)),
    "pow",
    function (d,a,b) {
        return new Multiply(new Power(a, new Subtract(b, Const.ONE)), new Add(new Multiply(a, new Multiply(new Log(Const.E,a),b.diff(d))), new Multiply(b,a.diff(d))));
    },
    2);
const Log = createOperation(
    (a,b) => (Math.log(Math.abs(b))/Math.log(Math.abs(a))),
    "log",
    function (d, a, b) {
        return new Divide(
            new Subtract(
                new Divide(new Multiply(new Log(Const.E, a),b.diff(d)),b),
                new Divide(new Multiply(new Log(Const.E,b),a.diff(d)),a)),
            new Multiply(new Log(Const.E,a), new Log(Const.E,a)));
     },
    2);
const OPERS = [];
OPERS["+"] = Add;
OPERS["-"] = Subtract;
OPERS["/"] = Divide;
OPERS["*"] = Multiply;
OPERS["negate"] = Negate;
OPERS["pow"] = Power;
OPERS["log"] = Log;
const VA_R = ["x", "y", "z"];

function parse(expression) {
    let stack = [];
    let expre = expression.trim().split(/\s+/);
    for (let i = 0; i < expre.length; i++) {
        if (VA_R.includes(expre[i])) {
            stack.push(new Variable(expre[i]));
            continue;
        }
        if (expre[i] in OPERS) {
            let op = OPERS[expre[i]];
            stack.push(new op(...stack.splice(-op.getCount)));
            continue;
        }
        stack.push(new Const(parseFloat(expre[i])));
    }
    return stack[stack.length-1];
}
function parsePrefix(expression) {

    let ans = outParser(expression);
    if (expression.length != 0)
        throw new Error("Non end exception");
}
function check(number) {
    let fl = 0;
    for (let i = 0; i < number.length; i++) {
        if (!((number.charCodeAt(i) >= 48 && number.charCodeAt(i) <= 57) || (number.charAt(i) == '.')) || (number.charAt(i) == '.' && fl == 1))
            return false;
        if (number.charAt(i) == '.')
            fl = 1;
    }
    return true;
}
const SPEC_SYMBOLS = ['*','/','+','-'];
function getOperators(source) {
    let op = "";
    if (SPEC_SYMBOLS.includes(source.charAt(0))) {
        while (SPEC_SYMBOLS.includes(source.charAt(0))) {
            op += source.charAt(0);

        }
        return op;
    }
    if (source.charAt(0).match(/[a-z]/) != null) {
        while (source.charAt(0).match(/[a-z]/) != null) {
            op += source.charAt(0);
            source = source.substr(1);
        }
        return op;
    }
}
function outParser(source) {
    source.trim();
    if (source.charAt(0) == '(') {
        source = source.substr(1);
        let ans = outParser(source);
        if (source.charAt(0) != ')')
            throw new Error("Non closest brackets");
        source = source.substr(1);
        return ans;
    }
    let op = getOperators(source);

}

