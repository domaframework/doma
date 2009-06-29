package org.seasar.doma.internal.jdbc.sql;

import org.seasar.doma.domain.Domain;

/**
 * @author taedium
 * 
 */
public interface PreparedSqlParameter extends SqlParameter {

    Domain<?, ?> getDomain();
}
