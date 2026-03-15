package io.github.itzispyder.clickcrystals.scripting.expressions.reflection;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD, ElementType.FIELD })
public @interface ScriptReflect {

    String name();
    Type type() default Type.FUNCTION;

    enum Type {
        VARIABLE,
        FUNCTION
    }
}
