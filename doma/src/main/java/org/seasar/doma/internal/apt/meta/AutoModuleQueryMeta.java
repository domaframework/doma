package org.seasar.doma.internal.apt.meta;

import java.util.ArrayList;
import java.util.List;

/**
 * @author taedium
 * 
 */
public abstract class AutoModuleQueryMeta extends AbstractQueryMeta {

    protected final List<CallableStatementParameterMeta> callableStatementParameterMetas = new ArrayList<CallableStatementParameterMeta>();

    public void addCallableStatementParameterMeta(
            CallableStatementParameterMeta parameterMeta) {
        callableStatementParameterMetas.add(parameterMeta);
    }

    public List<CallableStatementParameterMeta> getCallableStatementParameterMetas() {
        return callableStatementParameterMetas;
    }

}
