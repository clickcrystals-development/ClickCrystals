package io.github.itzispyder.clickcrystals.scripting.expressions;

import io.github.itzispyder.clickcrystals.scripting.ScriptArgs;

/*
this parser uses an algorithm called recursive descent parsing.
the grammar of this evaluator is listed below:

logicalExp = comparison OR logicalExp or comparison OR logicalExp and comparison
comparison = expression > expression
          OR expression < expression ... etc.
          OR true
          OR false
          OR !comparison
          OR logicalExp

p.s. i was too lazy to rewrite the entire logic for logical evaluator
so this just functions as a normal expression evaluator with TRUE being 1.0 and FALSE being 0.0
 */
public class LogicalEvaluator extends ExpressionEvaluator {

    public LogicalEvaluator(ExpressionScope scope) {
        super(scope);
    }

    public LogicalEvaluator() {
        super();
    }

    @Override
    public double eval(String logicalExpression) {
        reset(logicalExpression);
        next();
        return parseLogicalOr();
    }

    public boolean evalLogic(String logicalExpression) {
        return (int) eval(logicalExpression) != 0;
    }

    public boolean evalLogic(ScriptArgs args) {
        String expression = args.getAll().toString();
        boolean result = evalLogic(expression);

        String readExpression = expression.substring(0, pos);
        int nextCursor = readExpression.split(" ").length;
        args.zeroCursor(nextCursor);

        return result;
    }

    private double parseLogicalOr() {
        double x = parseLogicalAnd();
        while (true) {
            if (consume("or") || consume("||"))
                x = (x != 0 || parseLogicalAnd() != 0) ? 1.0 : 0.0;
            else return x;
        }
    }

    private double parseLogicalAnd() {
        double x = parseComparison();
        while (true) {
            if (consume("and") || consume("&&"))
                x = (x != 0 && parseComparison() != 0) ? 1.0 : 0.0;
            else return x;
        }
    }

    private double parseComparison() {
        double x = parseExpression();
        while (true) {
            if (consume(">=")) x = x >= parseExpression() ? 1.0 : 0.0;
            else if (consume("<=")) x = x <= parseExpression() ? 1.0 : 0.0;
            else if (consume("!=")) x = x != parseExpression() ? 1.0 : 0.0;
            else if (consume("==")) x = x == parseExpression() ? 1.0 : 0.0;
            else if (consume("is")) x = x == parseExpression() ? 1.0 : 0.0;
            else if (consume('>')) x = x > parseExpression() ? 1.0 : 0.0;
            else if (consume('<')) x = x < parseExpression() ? 1.0 : 0.0;
            else if (consume('=')) x = x == parseExpression() ? 1.0 : 0.0;
            else return x;
        }
    }

    @Override
    protected double parseFactor() {
        if (consume('!')) return parseFactor() == 0 ? 1.0 : 0.0;
        if (consume("true")) return 1.0;
        if (consume("false")) return 0.0;

        int pos0 = pos;
        char char0 = currChar;
        if (consume('(')) { // opening parenthesis might mean more logical logic
            double x = parseLogicalOr();
            if (consume(')')) return x;

            // failed to parse logical trying math
            pos = pos0;
            currChar = char0;
        }
        return super.parseFactor();
    }
}
