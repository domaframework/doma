package org.seasar.doma.internal.apt.lombok;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author nakamura-to
 *
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.SOURCE)
public @interface AllArgsConstructor {

    String staticName() default "";

    AccessLevel access() default AccessLevel.PUBLIC;

}
