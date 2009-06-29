package org.seasar.doma.internal.jdbc.query;

import static org.seasar.doma.internal.util.Assertions.*;

import org.seasar.doma.internal.jdbc.sql.CallableSqlBuilder;

/**
 * @author taedium
 * 
 */
public class AutoProcedureQuery extends AutoModuleQuery implements
        ProcedureQuery {

    protected String procedureName;

    public void compile() {
        assertNotNull(config, procedureName, callerClassName, callerMethodName);
        prepareOptions();
        prepareSql();
        assertNotNull(sql);
    }

    protected void prepareSql() {
        CallableSqlBuilder builder = new CallableSqlBuilder(config.dialect(),
                config.sqlLogFormattingVisitor(), procedureName, parameters);
        sql = builder.build();
    }

    public void setProcedureName(String procedureName) {
        this.procedureName = procedureName;
    }

}
