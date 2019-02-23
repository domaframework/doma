package org.seasar.doma.internal.apt.meta.query;

import java.sql.Array;
import java.util.List;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeKind;
import org.seasar.doma.internal.apt.AptException;
import org.seasar.doma.internal.apt.Context;
import org.seasar.doma.internal.apt.annot.ArrayFactoryAnnot;
import org.seasar.doma.message.Message;

public class ArrayCreateQueryMetaFactory
    extends AbstractCreateQueryMetaFactory<ArrayCreateQueryMeta> {

  public ArrayCreateQueryMetaFactory(
      Context ctx, TypeElement daoElement, ExecutableElement methodElement) {
    super(ctx, daoElement, methodElement, Array.class);
  }

  @Override
  public QueryMeta createQueryMeta() {
    ArrayFactoryAnnot arrayFactoryAnnot = ctx.getAnnotations().newArrayFactoryAnnot(methodElement);
    if (arrayFactoryAnnot == null) {
      return null;
    }
    ArrayCreateQueryMeta queryMeta = new ArrayCreateQueryMeta(daoElement, methodElement);
    queryMeta.setArrayFactoryAnnot(arrayFactoryAnnot);
    queryMeta.setQueryKind(QueryKind.ARRAY_FACTORY);
    doTypeParameters(queryMeta);
    doReturnType(queryMeta);
    doParameters(queryMeta);
    doThrowTypes(queryMeta);
    return queryMeta;
  }

  @Override
  protected void doParameters(ArrayCreateQueryMeta queryMeta) {
    List<? extends VariableElement> parameters = methodElement.getParameters();
    int size = parameters.size();
    if (size != 1) {
      throw new AptException(Message.DOMA4002, methodElement, new Object[] {});
    }
    QueryParameterMeta parameterMeta = createParameterMeta(parameters.get(0));
    if (parameterMeta.getType().getKind() != TypeKind.ARRAY) {
      throw new AptException(Message.DOMA4076, parameterMeta.getElement(), new Object[] {});
    }
    queryMeta.setElementsParameterName(parameterMeta.getName());
    queryMeta.addParameterMeta(parameterMeta);
    if (parameterMeta.isBindable()) {
      queryMeta.addBindableParameterCtType(parameterMeta.getName(), parameterMeta.getCtType());
    }
  }
}
