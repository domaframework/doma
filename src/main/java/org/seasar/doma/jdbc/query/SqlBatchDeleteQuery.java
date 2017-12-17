package org.seasar.doma.jdbc.query;

import org.seasar.doma.jdbc.SqlKind;

public class SqlBatchDeleteQuery extends SqlBatchModifyQuery implements BatchDeleteQuery {

    public SqlBatchDeleteQuery() {
        super(SqlKind.DELETE);
    }

}
