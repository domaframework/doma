package org.seasar.doma;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.sql.Statement;
import org.seasar.doma.jdbc.ResultMappingException;

/**
 * ストアドファンクションやストアドプロシージャーから返される結果セットにマッピングされることを示します。
 *
 * <p>{@code ResultSet} をカーソルとしてOUTパラメータで返すRDBMSにおいては、注釈されたパラメータは実質的にOUTパラメータとして扱われます
 * 。そうでないRDBMSにおいては、IN、INOUT、OUTのいずれのパラメータにもみなされません。 {@link
 * Statement#getResultSet()}で取得される結果セットにマッピングされます。
 *
 * <p>このアノテーションが注釈されるパラメータは、 {@link Function} もしくは {@link Procedure} が注釈されたメソッドのパラメータでなければいけません。
 *
 * <h3>例:</h3>
 *
 * <pre>
 * &#064;Dao(config = AppConfig.class)
 * public interface EmployeeDao {
 *
 *     &#064;Procedure
 *     void fetchEmployees(@In Integer departmentId,
 *             &#064;ResultSet List&lt;Employee&gt; employees);
 * }
 * </pre>
 *
 * <pre>
 * EmployeeDao dao = new EmployeeDaoImpl();
 * List&lt;Employee&gt; employees = new ArrayList&lt;Employee&gt;();
 * dao.fetchEmployees(10, employees);
 * for (Employee e : employees) {
 *     ...
 * }
 * </pre>
 *
 * @author taedium
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface ResultSet {

  /**
   * 結果がエンティティやエンティティのリストの場合、 エンティティのすべてのプロパティに結果セットのカラムがマッピングされることを保証するかどうかを返します。
   *
   * <p>{@code true} の場合、マッピングされないプロパティが存在すれば、このアノテーションが注釈されたパラメータを持つメソッドから {@link
   * ResultMappingException} がスローされます。
   *
   * @return 保証するかどうか
   * @since 1.34.0
   */
  boolean ensureResultMapping() default false;
}
