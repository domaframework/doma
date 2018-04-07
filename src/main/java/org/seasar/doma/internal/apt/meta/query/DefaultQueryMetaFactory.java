package org.seasar.doma.internal.apt.meta.query;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.VariableElement;
import org.seasar.doma.internal.apt.AptException;
import org.seasar.doma.internal.apt.Context;
import org.seasar.doma.internal.apt.annot.AbstractAnnot;
import org.seasar.doma.message.Message;

public class DefaultQueryMetaFactory extends AbstractQueryMetaFactory<DefaultQueryMeta> {

  public DefaultQueryMetaFactory(Context ctx, ExecutableElement methodElement) {
    super(ctx, methodElement);
  }

  @Override
  public QueryMeta createQueryMeta() {
    if (!methodElement.isDefault()) {
      return null;
    }
    var queryMeta = new DefaultQueryMeta(methodElement);
    queryMeta.setQueryKind(QueryKind.DEFAULT);
    doAnnotation(queryMeta, null);
    doTypeParameters(queryMeta);
    doParameters(queryMeta);
    doReturnType(queryMeta);
    doThrowTypes(queryMeta);
    return queryMeta;
  }

  @Override
  protected void doAnnotation(DefaultQueryMeta queryMeta, AbstractAnnot targetAnnot) {
    if (sqlAnnot == null) {
      return;
    }
    throw new AptException(Message.DOMA4442, methodElement, sqlAnnot.getAnnotationMirror());
  }

  @Override
  protected void doParameters(DefaultQueryMeta queryMeta) {
    for (VariableElement parameter : methodElement.getParameters()) {
      var parameterMeta = createParameterMeta(parameter);
      queryMeta.addParameterMeta(parameterMeta);
      if (parameterMeta.isBindable()) {
        queryMeta.addBindableParameterCtType(parameterMeta.getName(), parameterMeta.getCtType());
      }
    }
  }

  @Override
  protected void doReturnType(DefaultQueryMeta queryMeta) {
    var resultMeta = createReturnMeta();
    queryMeta.setReturnMeta(resultMeta);
  }
}
