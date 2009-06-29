package org.seasar.doma;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author taedium
 * 
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Table {

    @Table
    static class Default {

        private static Table table = Default.class.getAnnotation(Table.class);

        public static Table get() {
            return table;
        }
    }

    String catalog() default "";

    String schema() default "";

    String name() default "";
}
