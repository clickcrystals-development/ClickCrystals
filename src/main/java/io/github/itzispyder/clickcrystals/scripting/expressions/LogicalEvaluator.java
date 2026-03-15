package io.github.itzispyder.clickcrystals.scripting.expressions;

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
 */
public class LogicalEvaluator {

    private static final int LOGICAL_GREATER = 0;
    private static final int LOGICAL_LESS = 1;
    private static final int LOGICAL_EQUAL = 2;
    private static final int LOGICAL_LEQUAL = 3;
    private static final int LOGICAL_GEQUAL = 4;

    private final ExpressionEvaluator evaluator;
    private String logicalExp;
    private int pos;
    private char currChar;

    public LogicalEvaluator(ExpressionEvaluator evaluator) {
        this.evaluator = evaluator;
    }

    public LogicalEvaluator() {
        this(new ExpressionEvaluator());
    }

    public boolean eval(String logicalExpression) {
        reset(logicalExpression);
        next();
        return parseLogicalExp();
    }

    private void reset(String logicalExp) {
        this.logicalExp = logicalExp;
        this.pos = -1;
        this.currChar = (char) -1;
    }

    private void next() {
        currChar = ++pos < logicalExp.length()
                ? logicalExp.charAt(pos)
                : (char) -1;
    }

    private boolean consume(char toConsume) {
        while (currChar == ' ')
            next();

        if (currChar == toConsume) {
            next();
            return true;
        }
        return false;
    }

    private boolean consume(String charSeqToConsume) {
        while (currChar == ' ')
            next();

        int pos0 = pos;
        char char0 = currChar;
        for (int i = 0; i < charSeqToConsume.length(); i++) {
            if (!consume(charSeqToConsume.charAt(i))) {
                pos = pos0;
                currChar = char0;
                // this resets the position back to beginning of charSequence
                // for next consumption
                return false;
            }
        }
        return true;
    }

    private void assertUnclosedParenthesis() {
        if (!consume(')'))
            throw new IllegalArgumentException("Unclosed parenthesis");
    }

    private boolean needsClosingParenthesis(String str) {
        int open = 0, close = 0;
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            if (c == '(') open++;
            else if (c == ')') close++;
        }
        return open > close;
    }

    private boolean isExpressionChar(char c, int exprStartPos) {
        return c == '_' || c == '.' || c == ' '
                || ('0' <= c && c <= '9')
                || ('A' <= c && c <= 'Z')
                || ('a' <= c && c <= 'z')
                || c == '*' || c == '/' || c == '+' || c == '-' || c == '^'
                || c == '('
                || (c == ')' && needsClosingParenthesis(logicalExp.substring(exprStartPos, pos)));
    }

    private boolean parseLogicalExp() {
        boolean x = parseComparison();
        while (true) {
            if (consume("and")) x = x && parseComparison();
            else if (consume("or")) x = x || parseComparison();
            else return x;
        }
    }

    private boolean parseComparison() {
        if (consume('!')) return !parseComparison();
        else if (consume("true")) return true;
        else if (consume("false")) return false;

        boolean x;
        int pos0 = pos;

        if (consume('(')) {
            x = parseLogicalExp();
            assertUnclosedParenthesis();
        }
        else if (isExpressionChar(currChar, pos0)) {
            while (isExpressionChar(currChar, pos0)) next();
            String exprA = logicalExp.substring(pos0, pos);
            double valA = evaluator.eval(exprA);

            int compOp = consumeOperator();
            int pos1 = pos;

            while (isExpressionChar(currChar, pos1)) next();
            String exprB = logicalExp.substring(pos1, pos);
            double valB = evaluator.eval(exprB);

            switch (compOp) {
                case LOGICAL_EQUAL -> x = valA == valB;
                case LOGICAL_GREATER -> x = valA > valB;
                case LOGICAL_LESS -> x = valA < valB;
                case LOGICAL_LEQUAL -> x = valA <= valB;
                case LOGICAL_GEQUAL -> x = valA >= valB;
                default -> throw new IllegalArgumentException("Unknown comparator operator: " + currChar);
            }
        }
        else throw new IllegalArgumentException("Unexpected character in logical expression: " + currChar);

        return x;
    }

    private int consumeOperator() {
        int op;

        if (consume(">=")) op = LOGICAL_GEQUAL;
        else if (consume("<=")) op = LOGICAL_LEQUAL;
        else if (consume("==")) op = LOGICAL_EQUAL;
        else if (consume('>')) op = LOGICAL_GREATER;
        else if (consume('<')) op = LOGICAL_LESS;
        else if (consume('=')) op = LOGICAL_EQUAL;
            // exceptions here
        else if (currChar == ')') throw new IllegalArgumentException("Unknown comparator operator: ( Maybe try surrounding your expression with eval()");
        else throw new IllegalArgumentException("Unknown comparator operator: " + currChar);

        return op;
    }
}
