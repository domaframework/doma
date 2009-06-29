package org.seasar.doma.internal.jdbc.query;

import static org.seasar.doma.internal.util.Assertions.*;

import org.seasar.doma.internal.jdbc.sql.CallableSqlBuilder;
import org.seasar.doma.internal.jdbc.sql.ResultParameter;

/**
 * @author taedium
 * 
 */
public class AutoFunctionQuery<R> extends AutoModuleQuery implements
        FunctionQuery<R> {

    protected String functionName;

    protected ResultParameter<R> resultParameter;

    public void compile() {
        assertNotNull(config, functionName, resultParameter, callerClassName, callerMethodName);
        prepareOptions();
        prepareSql();
        assertNotNull(sql);
    }

    protected void prepareSql() {
        CallableSqlBuilder builder = new CallableSqlBuilder(config.dialect(),
                config.sqlLogFormattingVisitor(), functionName, parameters,
                resultParameter);
        sql = builder.build();
    }

    public void setFunctionName(String functionName) {
        this.functionName = functionName;
    }

    public void setResultParameter(ResultParameter<R> parameter) {
        this.resultParameter = parameter;
    }

    @Override
    public R getResult() {
        return resultParameter.getResult();
    }

}
