package org.seasar.doma;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.seasar.doma.internal.jdbc.NullEntityListener;
import org.seasar.doma.jdbc.EntityListener;


/**
 * @author taedium
 * 
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Entity {

    Class<? extends EntityListener<?>> listener() default NullEntityListener.class;
}
