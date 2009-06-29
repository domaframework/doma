package org.seasar.doma.internal.jdbc.query;

import org.seasar.doma.internal.jdbc.sql.CallableSql;

/**
 * @author taedium
 * 
 */
public interface ModuleQuery extends Query {

    CallableSql getSql();
}
