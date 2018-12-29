package org.seasar.doma;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.sql.Statement;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.JdbcException;
import org.seasar.doma.jdbc.SqlFileNotFoundException;
import org.seasar.doma.jdbc.SqlLogType;
import org.seasar.doma.jdbc.UniqueConstraintException;

/**
 * 挿入処理を示します。
 *
 * <p>このアノテーションが注釈されるメソッドは、Daoインタフェースのメンバでなければいけません。
 *
 * <h3>例:</h3>
 *
 * <pre>
 * &#064;Entity
 * public class Employee {
 *     ...
 * }
 *
 * &#064;Dao(config = AppConfig.class)
 * public interface EmployeeDao {
 *
 *     &#064;Insert
 *     int insert(Employee employee);
 * }
 * </pre>
 *
 * 注釈されるメソッドは、次の例外をスローすることがあります。
 *
 * <ul>
 *   <li>{@link DomaNullPointerException} パラメータに {@code null}を渡した場合
 *   <li>{@link UniqueConstraintException} 一意制約違反が発生した場合
 *   <li>{@link SqlFileNotFoundException} {@code sqlFile} 要素が {@code true} で、SQLファイルが見つからなかった場合
 *   <li>{@link JdbcException} 上記以外でJDBCに関する例外が発生した場合
 * </ul>
 *
 * @author taedium
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@DaoMethod
public @interface Insert {

  /**
   * SQLファイルにマッピングするかどうかを返します。
   *
   * @return SQLファイルにマッピングするかどうか
   */
  boolean sqlFile() default false;

  /**
   * クエリタイムアウト（秒）を返します。
   *
   * <p>指定しない場合、{@link Config#getQueryTimeout()}が使用されます。
   *
   * @return クエリタイムアウト（秒）
   * @see Statement#setQueryTimeout(int)
   */
  int queryTimeout() default -1;

  /**
   * INSERT文で {@code null} のプロパティに対応するカラムを除去するかどうかを返します。
   *
   * <p>この要素に対する指定は、{@link #sqlFile()} が {@code false} の場合にのみ有効です。
   *
   * @return カラムを除去するかどうか
   */
  boolean excludeNull() default false;

  /**
   * INSERT文に含めるプロパティ名の配列を返します。
   *
   * <p>ここに指定できるのは、カラム名ではなく挿入対象エンティティクラスのプロパティ名です。
   *
   * <p>この要素に対する指定は、{@link #sqlFile()} が {@code false} の場合にのみ有効です。
   *
   * @return 含めるプロパティ名の配列
   */
  String[] include() default {};

  /**
   * INSERT文から除去するプロパティ名の配列を返します。
   *
   * <p>ここに指定できるのは、カラム名ではなく挿入対象エンティティクラスのプロパティ名です。
   *
   * <p>この要素に対する指定は、{@link #sqlFile()} が {@code false} の場合にのみ有効です。
   *
   * @return 除去するプロパティ名の配列
   */
  String[] exclude() default {};

  /**
   * SQLのログの出力形式を返します。
   *
   * @return SQLログの出力形式
   * @since 2.0.0
   */
  SqlLogType sqlLog() default SqlLogType.FORMATTED;
}
