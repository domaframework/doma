package org.seasar.doma;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author taedium
 * 
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface SequenceGenerator {

    String catalog() default "";

    String schema() default "";

    String sequence();

    int initialValue() default 1;

    int allocationSize() default 1;

}
