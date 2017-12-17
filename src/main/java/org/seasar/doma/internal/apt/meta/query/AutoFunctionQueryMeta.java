package org.seasar.doma.internal.apt.meta.query;

import javax.lang.model.element.ExecutableElement;

import org.seasar.doma.MapKeyNamingType;
import org.seasar.doma.internal.apt.meta.parameter.ResultParameterMeta;
import org.seasar.doma.internal.apt.reflection.FunctionReflection;
import org.seasar.doma.jdbc.SqlLogType;

/**
 * @author taedium
 * 
 */
public class AutoFunctionQueryMeta extends AutoModuleQueryMeta {

    private ResultParameterMeta resultParameterMeta;

    private FunctionReflection functionReflection;

    public AutoFunctionQueryMeta(ExecutableElement method) {
        super(method);
    }

    public ResultParameterMeta getResultParameterMeta() {
        return resultParameterMeta;
    }

    public void setResultParameterMeta(ResultParameterMeta resultParameterMeta) {
        this.resultParameterMeta = resultParameterMeta;
    }

    public FunctionReflection getFunctionReflection() {
        return functionReflection;
    }

    public void setFunctionReflection(FunctionReflection functionReflection) {
        this.functionReflection = functionReflection;
    }

    public String getCatalogName() {
        return functionReflection.getCatalogValue();
    }

    public String getSchemaName() {
        return functionReflection.getSchemaValue();
    }

    public String getFunctionName() {
        return functionReflection.getNameValue();
    }

    public boolean isQuoteRequired() {
        return functionReflection.getQuoteValue();
    }

    public int getQueryTimeout() {
        return functionReflection.getQueryTimeoutValue();
    }

    public boolean getEnsureResultMapping() {
        return functionReflection.getEnsureResultMappingValue();
    }

    @Override
    public MapKeyNamingType getMapKeyNamingType() {
        return functionReflection.getMapKeyNamingValue();
    }

    public SqlLogType getSqlLogType() {
        return functionReflection.getSqlLogValue();
    }

    @Override
    public <R, P> R accept(QueryMetaVisitor<R, P> visitor, P p) {
        return visitor.visitAutoFunctionQueryMeta(this, p);
    }

}
