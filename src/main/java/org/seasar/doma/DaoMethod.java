package org.seasar.doma;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Daoインタフェースのメソッドのアノテーションであることを示します。
 *
 * <p>{@literal Doma} がアノテーション処理に利用するメタアノテーションです。
 *
 * @author taedium
 */
@Target(ElementType.ANNOTATION_TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface DaoMethod {}
