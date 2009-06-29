package org.seasar.doma;

/**
 * @author taedium
 * 
 */
public @interface Function {

    String catalog() default "";

    String schema() default "";

    String name() default "";

    int queryTimeout() default -1;
}
