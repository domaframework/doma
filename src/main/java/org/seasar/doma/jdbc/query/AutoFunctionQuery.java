package org.seasar.doma.jdbc.query;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import org.seasar.doma.internal.jdbc.sql.CallableSqlBuilder;
import org.seasar.doma.jdbc.ResultParameter;
import org.seasar.doma.jdbc.SqlKind;

public class AutoFunctionQuery<RESULT> extends AutoModuleQuery implements FunctionQuery<RESULT> {

    protected ResultParameter<RESULT> resultParameter;

    @Override
    public void prepare() {
        super.prepare();
        assertNotNull(moduleName, resultParameter);
        prepareQualifiedName();
        prepareOptions();
        prepareSql();
        assertNotNull(sql);
    }

    protected void prepareSql() {
        CallableSqlBuilder builder = new CallableSqlBuilder(config, SqlKind.FUNCTION, qualifiedName,
                parameters, sqlLogType, resultParameter);
        sql = builder.build(this::comment);
    }

    public void setFunctionName(String functionName) {
        setModuleName(functionName);
    }

    public void setResultParameter(ResultParameter<RESULT> parameter) {
        this.resultParameter = parameter;
    }

    @Override
    public RESULT getResult() {
        return resultParameter.getResult();
    }

}
