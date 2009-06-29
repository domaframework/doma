package org.seasar.doma.jdbc;

import java.util.List;

import org.seasar.doma.internal.jdbc.sql.SqlParameter;


/**
 * @author taedium
 * 
 */
public interface Sql<P extends SqlParameter> {

    String getRawSql();

    String getFormattedSql();

    List<P> getParameters();
}
