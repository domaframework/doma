package org.seasar.doma.internal.jdbc.query;

import static org.seasar.doma.internal.util.Assertions.*;

/**
 * @author taedium
 * 
 */
public class SqlFileDeleteQuery extends SqlFileModifyQuery implements
        DeleteQuery {

    public void compile() {
        assertNotNull(config, sqlFilePath, callerClassName, callerMethodName);
        prepareOptions();
        prepareSql();
        assertNotNull(sql);
    }

}
