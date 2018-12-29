package org.seasar.doma;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.seasar.doma.jdbc.entity.EntityListener;
import org.seasar.doma.jdbc.entity.NamingType;
import org.seasar.doma.jdbc.entity.NullEntityListener;

/**
 * エンティティクラスを示します。エンティティクラスのインスタンスは、テーブルもしくは結果セットのレコードを表現します。
 *
 * <h3>例: ミュータブルなエンティティ</h3>
 *
 * <pre>
 * &#064;Entity
 * public class Employee {
 *
 *     &#064;Id
 *     &#064;Column(name = &quot;ID&quot;)
 *     Integer id;
 *
 *     &#064;Column(name = &quot;EMPLOYEE_NAME&quot;)
 *     String employeeName;
 *
 *     &#064;Version
 *     &#064;Column(name = &quot;VERSION&quot;)
 *     int version;
 *
 *     ...
 * }
 * </pre>
 *
 * <h3>例: イミュータブルなエンティティ</h3>
 *
 * <pre>
 * &#064;Entity(immutable = true)
 * public class Employee {
 *
 *     &#064;Id
 *     &#064;Column(name = &quot;ID&quot;)
 *     final Integer id;
 *
 *     &#064;Column(name = &quot;EMPLOYEE_NAME&quot;)
 *     final String employeeName;
 *
 *     &#064;Version
 *     &#064;Column(name = &quot;VERSION&quot;)
 *     final int version;
 *
 *     public Employee(Integer id, String employeeName, int version) {
 *         this.id = id;
 *         this.employeeName = employeeName;
 *         this.version = version;
 *     }
 *     ...
 * }
 * </pre>
 *
 * <p>注釈されたインタフェースの実装はスレッドセーフであることを要求されません。
 *
 * <p>
 *
 * @author taedium
 * @see Table
 * @see Column
 * @see Id
 * @see Transient
 * @see Version
 * @see OriginalStates
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Entity {

  /**
   * リスナーを返します。
   *
   * <p>この要素に値を指定しない場合、エンティティクラスが他のエンティティクラスを継承しているかどうかで採用する設定が変わります。
   *
   * <ul>
   *   <li>継承している場合、親エンティティクラスの設定を引き継ぎます
   *   <li>継承していない場合、デフォルトの設定を使用します
   * </ul>
   *
   * <p>リスナーは、エンティティクラスごとに1つだけインスタンス化されます。
   *
   * @return リスナー
   */
  @SuppressWarnings("rawtypes")
  Class<? extends EntityListener> listener() default NullEntityListener.class;

  /**
   * ネーミング規約を返します。
   *
   * <p>この要素に値を指定しない場合、エンティティクラスが他のエンティティクラスを継承しているかどうかで採用する設定が変わります。
   *
   * <ul>
   *   <li>継承している場合、親エンティティクラスの設定を引き継ぎます
   *   <li>継承していない場合、デフォルトの設定を使用します
   * </ul>
   *
   * @return ネーミング規約
   */
  NamingType naming() default NamingType.NONE;

  /**
   * イミュータブルかどうかを返します。
   *
   * <p>この要素に値を指定しない場合、エンティティクラスが他のエンティティクラスを継承しているかどうかで採用する設定が変わります。
   *
   * <ul>
   *   <li>継承している場合、親エンティティクラスの設定を引き継ぎます
   *   <li>継承していない場合、デフォルトの設定を使用します
   * </ul>
   *
   * <p>ただし、エンティティクラスの継承階層で {@code true} と {@code false} の混在はできません。
   *
   * @return イミュータブルの場合 {@code true}
   * @since 1.34.0
   */
  boolean immutable() default false;
}
