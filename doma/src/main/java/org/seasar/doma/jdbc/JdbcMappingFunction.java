package org.seasar.doma.jdbc;

import java.sql.SQLException;

import org.seasar.doma.domain.Domain;


/**
 * @author taedium
 * 
 */
public interface JdbcMappingFunction {

    <V> void apply(Domain<V, ?> domain, JdbcType<V> jdbcType)
            throws SQLException;
}
