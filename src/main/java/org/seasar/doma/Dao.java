package org.seasar.doma;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.seasar.doma.jdbc.Config;

/**
 * Daoインタフェースであることを示します。
 *
 * <p>このアノテーションは、トップレベルのインタフェースに指定できます。
 *
 * <p>Daoインタフェースは、他のDaoインタフェースを1つのみ拡張できます。
 *
 * <p>インタフェースのメンバメソッドには、メタアノテーション {@link DaoMethod} でマークされたアノテーションのいずれかを指定しなければいけません。
 *
 * <h3>例:</h3>
 *
 * <pre>
 * &#064;Dao(config = AppConfig.class)
 * public interface EmployeeDao {
 *
 *     &#064;Insert
 *     int insert(Employee employee);
 * }
 * </pre>
 *
 * @author taedium
 * @see ArrayFactory
 * @see BatchDelete
 * @see BatchInsert
 * @see BatchUpdate
 * @see BlobFactory
 * @see ClobFactory
 * @see Delete
 * @see Function
 * @see Insert
 * @see NClobFactory
 * @see Procedure
 * @see Select
 * @see Script
 * @see Update
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Dao {

  /**
   * Daoを実行する際の設定（ {@literal JDBC} の接続情報や {@literal RDBMS} の方言等）を返します。
   *
   * <p>この要素に値を指定しないでデフォルトの値を使用する場合、 Daoの実装クラスには {@code Config} を受け取る {@code public}
   * なコンストラクタが生成されます。
   *
   * <p>{@code Config} 以外のクラスを指定する場合、そのクラスは、引数なしのpublicなコンストラクタを持つ具象クラスでなければいけません。
   * その場合、Daoの実装クラスには引数なしの {@code public} なコンストラクタが生成されます。 この要素に指定されたクラスは、そのコンストラクタの中でインスタンス化されます。
   *
   * @return Daoを実行する際の設定
   */
  Class<? extends Config> config() default Config.class;

  /**
   * Daoの実装クラスのアクセスレベルを返します。
   *
   * @return Daoの実装クラスのアクセスレベル
   * @since 2.0.0
   */
  AccessLevel accessLevel() default AccessLevel.PUBLIC;
}
