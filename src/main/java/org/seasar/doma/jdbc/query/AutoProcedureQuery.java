package org.seasar.doma.jdbc.query;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import org.seasar.doma.internal.jdbc.sql.CallableSqlBuilder;
import org.seasar.doma.jdbc.SqlKind;

public class AutoProcedureQuery extends AutoModuleQuery implements ProcedureQuery {

    @Override
    public void prepare() {
        super.prepare();
        assertNotNull(moduleName);
        prepareQualifiedName();
        prepareOptions();
        prepareSql();
        assertNotNull(sql);
    }

    protected void prepareSql() {
        CallableSqlBuilder builder = new CallableSqlBuilder(config, SqlKind.PROCEDURE,
                qualifiedName, parameters, sqlLogType);
        sql = builder.build(this::comment);
    }

    public void setProcedureName(String procedureName) {
        setModuleName(procedureName);
    }

}
