package org.seasar.doma.internal.apt.meta;

/**
 * @author taedium
 * 
 */
public class AutoFunctionQueryMeta extends AutoModuleQueryMeta {

    protected String functionName;

    protected ResultParameterMeta resultParameterMeta;

    public String getFunctionName() {
        return functionName;
    }

    public void setFunctionName(String functionName) {
        this.functionName = functionName;
    }

    public ResultParameterMeta getResultParameterMeta() {
        return resultParameterMeta;
    }

    public void setResultParameterMeta(ResultParameterMeta resultParameterMeta) {
        this.resultParameterMeta = resultParameterMeta;
    }

    @Override
    public <R, P> R accept(QueryMetaVisitor<R, P> visitor, P p) {
        return visitor.visitAutoFunctionQueryMeta(this, p);
    }
}
