package org.seasar.doma;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * パラメータの名前を示します。
 *
 * <p>クラスファイルにパラメータ名を保持させたい場合に使用します。 {@literal Annotation Plaggable API} では、ソースファイルからパラメータ名を取得できるため
 * 、このアノテーションを利用する機会は稀です。ソースファイルではなく、クラスファイルを処理しなければいけない場合に使用されることを想定しています。
 *
 * @author taedium
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface ParameterName {

  /**
   * パラメータの名前を返します。
   *
   * @return パラメータの名前
   */
  String value() default "";
}
