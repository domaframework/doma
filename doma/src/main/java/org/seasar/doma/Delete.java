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
public @interface Delete {

    boolean sqlFile() default false;

    int queryTimeout() default -1;

    boolean ignoresVersion() default false;

    boolean suppressesOptimisticLockException() default false;

}
