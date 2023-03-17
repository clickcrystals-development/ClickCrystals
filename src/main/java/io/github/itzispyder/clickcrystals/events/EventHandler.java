package io.github.itzispyder.clickcrystals.events;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({
        ElementType.METHOD
})
/**
 * Add this to methods that are responsible for handling events
 */
public @interface EventHandler {

}
