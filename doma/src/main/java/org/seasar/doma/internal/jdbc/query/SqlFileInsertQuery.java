package org.seasar.doma.internal.jdbc.query;

import static org.seasar.doma.internal.util.Assertions.*;

import java.sql.Statement;

/**
 * @author taedium
 * 
 */
public class SqlFileInsertQuery extends SqlFileModifyQuery implements
        InsertQuery {

    public void compile() {
        assertNotNull(config, sqlFilePath, callerClassName, callerMethodName);
        prepareOptions();
        prepareSql();
        assertNotNull(sql);
    }

    @Override
    public void generateId(Statement statement) {
    }
}
