package org.seasar.doma.internal.apt.processor.dao;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * @author taedium
 * 
 */
@Target(ElementType.TYPE)
public @interface ClassAnnotation {

    int aaa();

    boolean bbb();
}
