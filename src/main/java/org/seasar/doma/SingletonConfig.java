package org.seasar.doma;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.seasar.doma.jdbc.Config;

/**
 * {@link Config} の実装クラスがシングルトンであることを示します。
 *
 * <p>このアノテーションが注釈されたクラスは、自身のシングルトンを提供する static なメソッドを持たなければいけません。 また、コンストラクタはすべて private
 * でなければいけません。
 *
 * @author nakamura-to
 * @since 2.0.0
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface SingletonConfig {

  /**
   * シングルトンを提供するメソッドの名前です。
   *
   * <p>対応するメソッドは次の条件を満たす必要があります。
   *
   * <ul>
   *   <li>修飾子として public static を持つ
   *   <li>戻り値の型はこのアノテーションが注釈されたクラス
   *   <li>パラメータの数は0
   * </ul>
   *
   * @return シングルトンを提供するメソッドの名前
   */
  String method() default "singleton";
}
