package org.seasar.doma.jdbc.query;

import org.seasar.doma.jdbc.SqlKind;

public class SqlDeleteQuery extends SqlModifyQuery implements DeleteQuery {

    public SqlDeleteQuery() {
        super(SqlKind.DELETE);
    }

}
