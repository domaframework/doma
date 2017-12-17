package org.seasar.doma.jdbc.query;

import org.seasar.doma.jdbc.CallableSql;
import org.seasar.doma.jdbc.SqlLogType;

/**
 * An object used for building a statement that invoke a database module such as
 * a procedure.
 */
public interface ModuleQuery extends Query {

    @Override
    CallableSql getSql();

    String getQualifiedName();

    SqlLogType getSqlLogType();
}
