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
public @interface TableGenerator {

    String catalog() default "";

    String schema() default "";

    String table() default "ID_GENERATOR";

    String pkColumnName() default "PK";

    String valueColumnName() default "VALUE";

    String pkColumnValue();

    int initialValue() default 1;

    int allocationSize() default 1;
}
