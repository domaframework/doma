package org.seasar.doma.jdbc;

import java.lang.reflect.Method;
import org.seasar.doma.DomaNullPointerException;
import org.seasar.doma.jdbc.dialect.Dialect;

/**
 * A repository for {@link SqlTemplate}.
 *
 * <p>A SQL template corresponds to a DAO method.
 *
 * <p>The implementation class must be thread safe.
 */
public interface SqlTemplateRepository {

  /**
   * Returns the SQL template.
   *
   * @param method the DAO method
   * @param dialect the SQL dialect
   * @return the SQL template
   * @throws DomaNullPointerException if any arguments are {@code null}
   * @throws SqlFileNotFoundException if the SQL template is not found from the file
   * @throws JdbcException if an error other than listed above occurs
   */
  SqlTemplate getSqlTemplate(Method method, Dialect dialect);

  /** Clears cache. */
  default void clearCache() {}
}
