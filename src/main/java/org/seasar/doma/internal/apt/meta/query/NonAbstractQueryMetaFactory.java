package org.seasar.doma.internal.apt.meta.query;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.VariableElement;
import org.seasar.doma.internal.apt.AptException;
import org.seasar.doma.internal.apt.Context;
import org.seasar.doma.internal.apt.annot.AbstractAnnot;
import org.seasar.doma.message.Message;

public class NonAbstractQueryMetaFactory extends AbstractQueryMetaFactory<NonAbstractQueryMeta> {

  public NonAbstractQueryMetaFactory(Context ctx, ExecutableElement methodElement) {
    super(ctx, methodElement);
  }

  @Override
  public QueryMeta createQueryMeta() {
    if (methodElement.getModifiers().contains(Modifier.ABSTRACT)) {
      return null;
    }
    var queryMeta = new NonAbstractQueryMeta(methodElement);
    queryMeta.setQueryKind(QueryKind.NON_ABSTRACT);
    doAnnotation(queryMeta, null);
    doTypeParameters(queryMeta);
    doParameters(queryMeta);
    doReturnType(queryMeta);
    doThrowTypes(queryMeta);
    return queryMeta;
  }

  @Override
  protected void doAnnotation(NonAbstractQueryMeta queryMeta, AbstractAnnot targetAnnot) {
    if (sqlAnnot == null) {
      return;
    }
    throw new AptException(Message.DOMA4444, methodElement, sqlAnnot.getAnnotationMirror());
  }

  @Override
  protected void doParameters(NonAbstractQueryMeta queryMeta) {
    for (VariableElement parameter : methodElement.getParameters()) {
      var parameterMeta = createParameterMeta(parameter);
      queryMeta.addParameterMeta(parameterMeta);
      if (parameterMeta.isBindable()) {
        queryMeta.addBindableParameterCtType(parameterMeta.getName(), parameterMeta.getCtType());
      }
    }
  }

  @Override
  protected void doReturnType(NonAbstractQueryMeta queryMeta) {
    var resultMeta = createReturnMeta();
    queryMeta.setReturnMeta(resultMeta);
  }
}
