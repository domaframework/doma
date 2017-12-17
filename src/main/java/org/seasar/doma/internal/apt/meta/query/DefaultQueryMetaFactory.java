package org.seasar.doma.internal.apt.meta.query;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.VariableElement;

import org.seasar.doma.internal.apt.Context;

/**
 * @author taedium
 * 
 */
public class DefaultQueryMetaFactory extends AbstractQueryMetaFactory<DefaultQueryMeta> {

    public DefaultQueryMetaFactory(Context ctx, ExecutableElement methodElement) {
        super(ctx, methodElement);
    }

    @Override
    public QueryMeta createQueryMeta() {
        if (!methodElement.isDefault()) {
            return null;
        }
        DefaultQueryMeta queryMeta = new DefaultQueryMeta(methodElement);
        queryMeta.setQueryKind(QueryKind.DEFAULT);
        doTypeParameters(queryMeta);
        doParameters(queryMeta);
        doReturnType(queryMeta);
        doThrowTypes(queryMeta);
        return queryMeta;
    }

    @Override
    protected void doParameters(DefaultQueryMeta queryMeta) {
        for (VariableElement parameter : methodElement.getParameters()) {
            QueryParameterMeta parameterMeta = createParameterMeta(parameter);
            queryMeta.addParameterMeta(parameterMeta);
            if (parameterMeta.isBindable()) {
                queryMeta.addBindableParameterCtType(parameterMeta.getName(),
                        parameterMeta.getCtType());
            }
        }
    }

    @Override
    protected void doReturnType(DefaultQueryMeta queryMeta) {
        QueryReturnMeta resultMeta = createReturnMeta();
        queryMeta.setReturnMeta(resultMeta);
    }

}
