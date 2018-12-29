package org.seasar.doma;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 識別子を自動生成する方法を示します。
 *
 * <p>このアノテーションが注釈されるフィールドは、エンティティクラスのメンバでなければいけません。 このアノテーションは {@link Id} と併わせて使用しなければいけません。
 *
 * <p>{@code strategy} 要素に指定する値によっては追加のアノテーションが必要です。
 *
 * <ul>
 *   <li>{@link GenerationType#SEQUENCE} を指定した場合、{@link SequenceGenerator} が必要です。
 *   <li>{@link GenerationType#TABLE} を指定した場合、 {@link TableGenerator} が必要です。
 * </ul>
 *
 * <h3>例:</h3>
 *
 * <pre>
 * &#064;Entity
 * public class Employee {
 *
 *     &#064;Id
 *     &#064;GeneratedValue(strategy = GenerationType.SEQUENCE)
 *     &#064;SequenceGenerator(sequence = &quot;EMPLOYEE_SEQ&quot;)
 *     Integer id;
 *
 *     ...
 * }
 * </pre>
 *
 * @author taedium
 * @see GenerationType
 * @see SequenceGenerator
 * @see TableGenerator
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface GeneratedValue {

  /**
   * 識別子を自動生成する方法を返します。
   *
   * @return 識別子を自動生成する方法
   */
  GenerationType strategy();
}
