package io.github.itzispyder.clickcrystals.scripting.expressions;

import java.util.ArrayList;
import java.util.List;

/*
this parser uses an algorithm called recursive descent parsing.
the grammar of this evaluator is listed below:

expression = term OR expression+term OR expression-term
term = factor OR term*factor OR term/factor
factor = -factor OR +factor OR number OR expression OR function(expression) OR variable
 */
public class ExpressionEvaluator {

    private final ExpressionScope scope;
    private String expression;
    private int pos;
    private char currChar;

    public ExpressionEvaluator(ExpressionScope scope) {
        this.scope = scope;
        this.pos = -1;
    }

    public ExpressionEvaluator() {
        this(new ExpressionScope());
    }

    public ExpressionScope getScope() {
        return scope;
    }

    public double eval(String expression) {
        reset(expression);
        next();
        return parseExpression();
    }

    private void reset(String expression) {
        this.expression = expression;
        pos = -1;
        currChar = (char) -1;
    }

    private void next() {
        currChar = ++pos < expression.length()
                ? expression.charAt(pos)
                : (char) -1;
    }

    private boolean consume(char toConsume) {
        while (currChar == ' ')
            next();

        if (currChar == toConsume) {
            next();
            return true;
        }
        // future developers don't call next() here because if
        // this consumption attempt fails, leave it for another call to consume
        return false;
    }

    private double parseExpression() {
        double x = parseTerm();
        while (true) {
            if (consume('+')) x += parseTerm();
            else if (consume('-')) x -= parseTerm();
            else return x;
        }
    }

    private double parseTerm() {
        double x = parseFactor();
        while (true) {
            if (consume('*')) x *= parseFactor();
            else if (consume('/')) x /= parseFactor();
            else return x;
        }
    }

    private boolean isNumber(char c) {
        return c == '.'
                || ('0' <= c && c <= '9');
    }

    private boolean isAlphaNumeric(char c) {
        return c == '_' || c == '.'
                || ('0' <= c && c <= '9')
                || ('A' <= c && c <= 'Z')
                || ('a' <= c && c <= 'z');
    }

    private void assertUnclosedParenthesis() {
        if (!consume(')'))
            throw new IllegalArgumentException("Unclosed parenthesis");
    }

    private void assertValidFunction(String name) {
        if (!scope.isValidFun(name))
            throw new IllegalArgumentException("Unknown function: " + name);
    }

    private void assertValidVariable(String name) {
        if (!scope.isValidVar(name))
            throw new IllegalArgumentException("Unknown variable: " + name);
    }

    private double parseFactor() {
        if (consume('-')) return -parseFactor();
        if (consume('+')) return +parseFactor();

        double x;
        int pos0 = pos;

        if (consume('(')) {
            x = parseExpression();
            assertUnclosedParenthesis();
        }
        else if (isNumber(currChar)) {
            while (isNumber(currChar)) next();
            x = Double.parseDouble(expression.substring(pos0, pos));
        }
        else if (isAlphaNumeric(currChar)) {
            while (isAlphaNumeric(currChar)) next();
            String name = expression.substring(pos0, pos);

            if (consume('(')) {

                List<Double> args = new ArrayList<>();
                List<String> raw = new ArrayList<>();
                if (!consume(')')) {
                    do {
                        int pos1 = pos;
                        args.add(parseExpression());
                        raw.add(expression.substring(pos1, pos).trim());
                    }
                    while (consume(','));
                    assertUnclosedParenthesis();
                }

                assertValidFunction(name);
                x = scope.callFun(name,
                        args.stream().mapToDouble(Double::doubleValue).toArray(),
                        raw.toArray(String[]::new));
            }
            else {
                assertValidVariable(name);
                x = scope.readVar(name);
            }
        }
        else throw new IllegalArgumentException("Unexpected character in expression: " + currChar);

        if (consume('^'))
            x = Math.pow(x, parseFactor());

        return x;
    }
}
