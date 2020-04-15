package org.seasar.doma.internal.apt.meta.query;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import org.seasar.doma.internal.apt.Context;

public class DefaultQueryMetaFactory extends AbstractQueryMetaFactory<DefaultQueryMeta> {

  public DefaultQueryMetaFactory(
      Context ctx, TypeElement daoElement, ExecutableElement methodElement) {
    super(ctx, daoElement, methodElement);
  }

  @Override
  public QueryMeta createQueryMeta() {
    boolean isVirtualDefaultMethod =
        ctx.getMoreElements().isVirtualDefaultMethod(daoElement, methodElement);
    if (!isVirtualDefaultMethod && !methodElement.isDefault()) {
      return null;
    }
    DefaultQueryMeta queryMeta =
        new DefaultQueryMeta(daoElement, methodElement, isVirtualDefaultMethod);
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
        queryMeta.addBindableParameterCtType(parameterMeta.getName(), parameterMeta.getCtType());
      }
    }
  }

  @Override
  protected void doReturnType(DefaultQueryMeta queryMeta) {
    QueryReturnMeta resultMeta = createReturnMeta(queryMeta);
    queryMeta.setReturnMeta(resultMeta);
  }
}
