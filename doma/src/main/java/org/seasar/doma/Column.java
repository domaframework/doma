package org.seasar.doma;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.seasar.doma.internal.WrapException;
import org.seasar.doma.internal.util.Methods;


/**
 * @author taedium
 * 
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Column {

    static class Default {

        private static final Column column;
        static {
            try {
                column = Methods
                        .getMethod(Default.class, "get", new Class<?>[] {})
                        .getAnnotation(Column.class);
            } catch (WrapException e) {
                throw new DomaUnexpectedException(e.getCause());
            }
        }

        @Column
        public static Column get() {
            return column;
        }
    }

    String name() default "";

    boolean insertable() default true;

    boolean updatable() default true;

}
