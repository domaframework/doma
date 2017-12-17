package org.seasar.doma.jdbc.query;

import java.sql.Statement;

import org.seasar.doma.jdbc.SqlKind;

public class SqlBatchInsertQuery extends SqlBatchModifyQuery implements BatchInsertQuery {

    public SqlBatchInsertQuery() {
        super(SqlKind.INSERT);
    }

    @Override
    public void generateId(Statement statement, int index) {
    }

    @Override
    public boolean isBatchSupported() {
        return true;
    }

}
