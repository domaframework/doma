package org.seasar.doma.jdbc;

import java.lang.reflect.Method;
import org.seasar.doma.DomaIllegalArgumentException;
import org.seasar.doma.DomaNullPointerException;
import org.seasar.doma.jdbc.dialect.Dialect;

/**
 * A repository for {@link SqlFile}.
 *
 * <p>A SQL file corresponds to a DAO method. The path is built from the full qualified name of the
 * DAO and the method name. For example, when {@code org.example.ExampleDao} interface has {@code
 * selectAll} method, the corresponding SQL file path is {@code
 * META-INF/org/example/ExampleDao/selectAll.sql}.
 *
 * <p>The implementation class must be thread safe.
 */
public interface SqlFileRepository {

  /**
   * Returns the SQL file.
   *
   * @param method the DAO method
   * @param path the SQL file path
   * @param dialect the SQL dialect
   * @return the SQL file
   * @throws DomaNullPointerException if any arguments are {@code null}
   * @throws DomaIllegalArgumentException if the {@code method} is not annotated with {@link
   *     org.seasar.doma.experimental.Sql} and the {@code path} does not match the Ant-style glob
   *     pattern "META-INF&#47;**&#47;*.sql"
   * @throws SqlFileNotFoundException if the SQL file is not found
   * @throws JdbcException if an error other than listed above occurs
   */
  SqlFile getSqlFile(Method method, String path, Dialect dialect);

  /** Clears cache. */
  default void clearCache() {}
}
