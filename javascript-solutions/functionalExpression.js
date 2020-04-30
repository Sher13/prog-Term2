"use strict";

const cnst = value => (...vals) => value;
const variable = (name) => (...vals) => {
    switch (name) {
        case "x" : return vals[0];
        case "y" : return vals[1];
        case "z" : return vals[2];
    }
};

const operation = op => {
    let f = (...args) => (...vals) => {
        let a = [];
        // :NOTE: cycle `for` is too fat for JS, try to use `[].map`
        for (let i = 0; i < args.length; i++) {
            a.push(args[i](...vals));
        }
        return op(...a);
    }
    f.getCount = op.length;
    return f;
};
const multiply = operation((a,b) => a*b);
const add = operation((a,b) => a+b);
const subtract = operation((a,b) => a-b);
const divide = operation((a,b) => a/b);
const negate  = operation((a) => -a);
const avg5 = operation((a,b,c,d,e) => (a+b+c+d+e)/5.);
const med3 = operation((a,b,c) => {
    if (a >= b && b >= c || a <= b && b <= c)
        return b;
    if (b >= a && a >= c || b <= a && a <= c)
        return a;
    if (a >= c && c >= b || a <= c && c <= b)
        return c;
});
const pi = cnst(Math.PI);
const e = cnst(Math.E);
// :NOTE: why this and next are not const?
let OPERS = [];
OPERS["+"] = add;
OPERS["-"] = subtract;
OPERS["/"] = divide;
OPERS["*"] = multiply;
OPERS["avg5"] = avg5;
OPERS["med3"] = med3;
OPERS["negate"] = negate;
let CON_ST = [];
CON_ST["pi"] = pi;
CON_ST["e"] = e;
let VA_R = ["x", "y", "z"];
function parse(expression) {
    let stack = [];
    let expre = expression.trim().split(/\s+/);
    for (let i = 0; i < expre.length; i++) {
        // console.log(stack);
        if (VA_R.includes(expre[i])) {
            stack.push(variable(expre[i]));
            continue;
        }
        if (expre[i] in OPERS) {
            let a = [];
            let op = OPERS[expre[i]];
            for (let j = 0; j < op.getCount; j++) {
                a.push(stack[stack.length - 1]);
                stack.length = stack.length-1;
            }
            a.reverse();
            stack.push(op(...a));
            continue;
        }
        if (expre[i] in CON_ST) {
            stack.push(CON_ST[expre[i]]);
            continue;
        }
        stack.push(cnst(parseFloat(expre[i])));
    }
    // console.log(stack);
    return stack[stack.length-1];
}
