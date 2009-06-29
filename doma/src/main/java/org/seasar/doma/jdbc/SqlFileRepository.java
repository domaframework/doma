package org.seasar.doma.jdbc;

/**
 * @author taedium
 * 
 */
public interface SqlFileRepository {

    SqlFile getSqlFile(String path, Dialect dialect);

}
