package org.seasar.doma;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

// TODO 名前
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@EntityField
public @interface ModifiedProperties {
}
