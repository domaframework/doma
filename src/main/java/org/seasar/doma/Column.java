package org.seasar.doma;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * データベースのテーブルのカラムを示します。
 *
 * <p>このアノテーションが注釈されるフィールドは、 エンティティクラスのメンバでなければいけません。
 *
 * <h3>例:</h3>
 *
 * <pre>
 * &#064;Entity
 * public class Employee {
 *
 *     &#064;Column(name = &quot;EMPLOYEE_NAME&quot;)
 *     String employeeName;
 *
 *     &#064;Column(name = &quot;SALARY&quot;)
 *     BigDecimal salary;
 *
 *     ...
 * }
 * </pre>
 *
 * @author taedium
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Column {

  /**
   * カラム名を返します。
   *
   * <p>指定しない場合、カラム名は {@link Entity#naming()} に指定した列挙型 によって解決されます。
   *
   * @return カラム名
   */
  String name() default "";

  /**
   * プロパティに対応するカラムをINSERT文に含めるかどうかを返します。
   *
   * @return カラムをINSERT文に含めるかどうか
   */
  boolean insertable() default true;

  /**
   * プロパティに対応するカラムをUPDATE文のSET句に含めるかどうかを返します。
   *
   * @return カラムをUPDATE文のSET句に含めるかどうか
   */
  boolean updatable() default true;

  /**
   * カラム名を引用符で囲むかどうかを返します。
   *
   * @return カラム名を引用符で囲むかどうか
   */
  boolean quote() default false;
}
