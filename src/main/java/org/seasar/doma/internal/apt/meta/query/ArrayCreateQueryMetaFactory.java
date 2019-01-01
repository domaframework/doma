package org.seasar.doma.internal.apt.meta.query;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import java.sql.Array;
import java.util.List;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeKind;
import org.seasar.doma.internal.apt.AptException;
import org.seasar.doma.internal.apt.Context;
import org.seasar.doma.internal.apt.annot.ArrayFactoryAnnot;
import org.seasar.doma.internal.apt.meta.dao.DaoMeta;
import org.seasar.doma.message.Message;

public class ArrayCreateQueryMetaFactory
    extends AbstractCreateQueryMetaFactory<ArrayCreateQueryMeta> {

  public ArrayCreateQueryMetaFactory(Context ctx) {
    super(ctx, Array.class);
  }

  @Override
  public QueryMeta createQueryMeta(ExecutableElement method, DaoMeta daoMeta) {
    assertNotNull(method, daoMeta);
    ArrayFactoryAnnot arrayFactoryAnnot = ctx.getAnnotations().newArrayFactoryAnnot(method);
    if (arrayFactoryAnnot == null) {
      return null;
    }
    ArrayCreateQueryMeta queryMeta = new ArrayCreateQueryMeta(method, daoMeta.getDaoElement());
    queryMeta.setArrayFactoryAnnot(arrayFactoryAnnot);
    queryMeta.setQueryKind(QueryKind.ARRAY_FACTORY);
    doTypeParameters(queryMeta, method, daoMeta);
    doReturnType(queryMeta, method, daoMeta);
    doParameters(queryMeta, method, daoMeta);
    doThrowTypes(queryMeta, method, daoMeta);
    return queryMeta;
  }

  @Override
  protected void doParameters(
      ArrayCreateQueryMeta queryMeta, ExecutableElement method, DaoMeta daoMeta) {
    List<? extends VariableElement> parameters = method.getParameters();
    int size = parameters.size();
    if (size != 1) {
      throw new AptException(
          Message.DOMA4002,
          method,
          new Object[] {daoMeta.getDaoElement().getQualifiedName(), method.getSimpleName()});
    }
    QueryParameterMeta parameterMeta = createParameterMeta(parameters.get(0), queryMeta);
    if (parameterMeta.getType().getKind() != TypeKind.ARRAY) {
      throw new AptException(
          Message.DOMA4076,
          parameterMeta.getElement(),
          new Object[] {daoMeta.getDaoElement().getQualifiedName(), method.getSimpleName()});
    }
    queryMeta.setElementsParameterName(parameterMeta.getName());
    queryMeta.addParameterMeta(parameterMeta);
    if (parameterMeta.isBindable()) {
      queryMeta.addBindableParameterCtType(parameterMeta.getName(), parameterMeta.getCtType());
    }
  }
}
