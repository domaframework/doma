package org.seasar.doma;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * データベースのテーブルを示します。
 *
 * <p>このアノテーションは、エンティティクラスに対して有効です。
 *
 * <h3>例:</h3>
 *
 * <pre>
 * &#064;Entity
 * &#064;Table(name = &quot;EMP&quot;)
 * public class Employee {
 *     ...
 * }
 * </pre>
 *
 * @author taedium
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Table {

  /**
   * カタログ名を返します。
   *
   * @return カタログ名
   */
  String catalog() default "";

  /**
   * スキーマ名を返します。
   *
   * @return スキーマ名
   */
  String schema() default "";

  /**
   * テーブル名を返します。
   *
   * <p>指定しない場合、テーブル名は {@link Entity#naming()} に指定した列挙型 によって解決されます。
   *
   * @return テーブル名
   */
  String name() default "";

  /**
   * カタログ、スキーマ、テーブル名を引用符で囲むかどうかを返します。
   *
   * @return 引用符で囲むかどうか
   */
  boolean quote() default false;
}
