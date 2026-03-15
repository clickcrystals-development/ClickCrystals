package io.github.itzispyder.clickcrystals.scripting.expressions;

import java.util.HashMap;
import java.util.Map;

public class ExpressionScope {

    private final Map<String, ExpressionVariableSupplier> variables;
    private final Map<String, ExpressionFunction> functions;

    public ExpressionScope() {
        this.variables = new HashMap<>();
        this.functions = new HashMap<>();
    }

    public void importScope(ExpressionScope scope) {
        importFunctions(scope.functions);
        importVariables(scope.variables);
    }

    public void importFunctions(Map<String, ExpressionFunction> functions) {
        this.functions.putAll(functions);
    }

    public void importVariables(Map<String, ExpressionVariableSupplier> variables) {
        this.variables.putAll(variables);
    }

    public double readVar(String name) {
        return variables.get(name).get();
    }

    public double callFun(String name, double[] args, String[] raw) {
        return functions.get(name).f(args, raw);
    }

    public void defVar(String name, double value) {
        variables.put(name, () -> value);
    }

    public void defVar(String name, ExpressionVariableSupplier supplier) {
        variables.put(name, supplier);
    }

    public void defFun(String name, ExpressionFunction function) {
        functions.put(name, function);
    }

    public boolean isValidVar(String name) {
        return variables.containsKey(name);
    }

    public boolean isValidFun(String name) {
        return functions.containsKey(name);
    }
}
