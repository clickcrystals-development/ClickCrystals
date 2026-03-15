package io.github.itzispyder.clickcrystals.scripting.expressions.reflection;

import io.github.itzispyder.clickcrystals.scripting.expressions.ExpressionFunction;
import io.github.itzispyder.clickcrystals.scripting.expressions.ExpressionScope;
import io.github.itzispyder.clickcrystals.scripting.expressions.ExpressionVariableSupplier;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class ScriptReflectFactory {

    private static final Map<Class<? extends ScriptReflectObject>, ExpressionScope> registry = new HashMap<>();

    public static <T extends ScriptReflectObject> ExpressionScope extract(Class<T> object) {
        return registry.computeIfAbsent(object, ScriptReflectFactory::register);
    }

    private static <T extends ScriptReflectObject> ExpressionScope register(Class<T> object) {
        ExpressionScope scope = new ExpressionScope();

        for (Method method : object.getDeclaredMethods()) {
            ScriptReflect ann = method.getAnnotation(ScriptReflect.class);

            if (ann == null)
                continue;

            method.setAccessible(true);
            switch (ann.type()) {
                case FUNCTION -> scope.defFun(ann.name(), mapMethod(method));
                case VARIABLE -> scope.defVar(ann.name(), mapGetterMethod(method));
            }
        }

        for (Field field : object.getDeclaredFields()) {
            ScriptReflect ann = field.getAnnotation(ScriptReflect.class);
            if (ann == null)
                continue;
            if (ann.type() != ScriptReflect.Type.VARIABLE)
                throw new IllegalArgumentException("ScriptReflect: cannot treat field as method");

            field.setAccessible(true);
            scope.defVar(ann.name(), mapField(field));
        }

        return registry.put(object, scope);
    }

    private static ExpressionFunction mapMethod(Method method) {
        return (args, names) -> {
            try {
                return (double) method.invoke(null, args, names);
            }
            catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        };
    }

    private static ExpressionVariableSupplier mapGetterMethod(Method method) {
        return () -> {
            try {
                return (double) method.invoke(null);
            }
            catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        };
    }

    private static ExpressionVariableSupplier mapField(Field field) {
        return () -> {
            try {
                return field.getDouble(null);
            }
            catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        };
    }
}
