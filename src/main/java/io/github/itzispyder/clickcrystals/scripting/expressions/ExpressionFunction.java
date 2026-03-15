package io.github.itzispyder.clickcrystals.scripting.expressions;

@FunctionalInterface
public interface ExpressionFunction {

    double f(double[] args, String[] names);
}
