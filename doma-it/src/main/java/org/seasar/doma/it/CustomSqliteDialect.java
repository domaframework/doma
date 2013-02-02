package org.seasar.doma.it;

import org.seasar.doma.jdbc.dialect.SqliteDialect;

public class CustomSqliteDialect extends SqliteDialect {

    public CustomSqliteDialect() {
        super(new CustomSqliteJdbcMappingVisitor());
    }
}
