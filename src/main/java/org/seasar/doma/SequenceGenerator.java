package org.seasar.doma;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.seasar.doma.jdbc.id.BuiltinSequenceIdGenerator;
import org.seasar.doma.jdbc.id.SequenceIdGenerator;

/**
 * シーケンスを利用する識別子ジェネレータを示します。
 *
 * <p>このアノテーションが注釈されるフィールドは、エンティティクラスのメンバでなければいけません。 このアノテーションは {@link Id} 、 {@link GeneratedValue}
 * と併わせて使用しなければいけません。
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
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface SequenceGenerator {

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
   * シーケンス名を返します。
   *
   * @return シーケンス名
   */
  String sequence();

  /**
   * 初期値を返します。
   *
   * @return 初期値
   */
  long initialValue() default 1;

  /**
   * 割り当てサイズを返します。
   *
   * @return 割り当てサイズ
   */
  long allocationSize() default 1;

  /**
   * ジェネレータの実装クラスを返します。
   *
   * @return ジェネレータの実装クラス
   */
  Class<? extends SequenceIdGenerator> implementer() default BuiltinSequenceIdGenerator.class;
}
