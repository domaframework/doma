package org.seasar.doma.internal.apt.meta.query;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.VariableElement;
import org.seasar.doma.internal.apt.meta.dao.DaoMeta;

public class DefaultQueryMetaFactory extends AbstractQueryMetaFactory<DefaultQueryMeta> {

  public DefaultQueryMetaFactory(ProcessingEnvironment env) {
    super(env);
  }

  @Override
  public QueryMeta createQueryMeta(ExecutableElement method, DaoMeta daoMeta) {
    assertNotNull(method, daoMeta);
    if (!method.isDefault()) {
      return null;
    }
    DefaultQueryMeta queryMeta = new DefaultQueryMeta(method, daoMeta.getDaoElement());
    queryMeta.setQueryKind(QueryKind.DEFAULT);
    doTypeParameters(queryMeta, method, daoMeta);
    doParameters(queryMeta, method, daoMeta);
    doReturnType(queryMeta, method, daoMeta);
    doThrowTypes(queryMeta, method, daoMeta);
    return queryMeta;
  }

  @Override
  protected void doParameters(
      DefaultQueryMeta queryMeta, ExecutableElement method, DaoMeta daoMeta) {
    for (VariableElement parameter : method.getParameters()) {
      QueryParameterMeta parameterMeta = createParameterMeta(parameter, queryMeta);
      queryMeta.addParameterMeta(parameterMeta);
      if (parameterMeta.isBindable()) {
        queryMeta.addBindableParameterCtType(parameterMeta.getName(), parameterMeta.getCtType());
      }
    }
  }

  @Override
  protected void doReturnType(
      DefaultQueryMeta queryMeta, ExecutableElement method, DaoMeta daoMeta) {
    QueryReturnMeta resultMeta = createReturnMeta(queryMeta);
    queryMeta.setReturnMeta(resultMeta);
  }
}
