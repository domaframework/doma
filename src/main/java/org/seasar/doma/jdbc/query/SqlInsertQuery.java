package org.seasar.doma.jdbc.query;

import java.sql.Statement;

import org.seasar.doma.jdbc.SqlKind;

public class SqlInsertQuery extends SqlModifyQuery implements InsertQuery {

    public SqlInsertQuery() {
        super(SqlKind.INSERT);
    }

    @Override
    public void generateId(Statement statement) {
    }

}
