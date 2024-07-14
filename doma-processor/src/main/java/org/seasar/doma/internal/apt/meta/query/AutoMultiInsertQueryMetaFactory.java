package org.seasar.doma.internal.apt.meta.query;

import java.util.List;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import org.seasar.doma.internal.apt.AptException;
import org.seasar.doma.internal.apt.Context;
import org.seasar.doma.internal.apt.annot.MultiInsertAnnot;
import org.seasar.doma.internal.apt.cttype.CtType;
import org.seasar.doma.internal.apt.cttype.EntityCtType;
import org.seasar.doma.internal.apt.cttype.IterableCtType;
import org.seasar.doma.internal.apt.cttype.SimpleCtTypeVisitor;
import org.seasar.doma.message.Message;

public class AutoMultiInsertQueryMetaFactory
    extends AbstractQueryMetaFactory<AutoMultiInsertQueryMeta> {

  public AutoMultiInsertQueryMetaFactory(
      Context ctx, TypeElement daoElement, ExecutableElement methodElement) {
    super(ctx, daoElement, methodElement);
  }

  @Override
  public QueryMeta createQueryMeta() {
    AutoMultiInsertQueryMeta queryMeta = createAutoMultiInsertQueryMeta();
    if (queryMeta == null) {
      return null;
    }
    doTypeParameters(queryMeta);
    doParameters(queryMeta);
    doReturnType(queryMeta);
    doThrowTypes(queryMeta);
    return queryMeta;
  }

  private AutoMultiInsertQueryMeta createAutoMultiInsertQueryMeta() {
    AutoMultiInsertQueryMeta queryMeta = new AutoMultiInsertQueryMeta(daoElement, methodElement);
    MultiInsertAnnot insertAnnot = ctx.getAnnotations().newMultiInsertAnnot(methodElement);
    if (insertAnnot != null) {
      queryMeta.setMultiInsertAnnot(insertAnnot);
      queryMeta.setQueryKind(QueryKind.AUTO_MULTI_INSERT);
      return queryMeta;
    }
    return null;
  }

  @Override
  protected void doReturnType(AutoMultiInsertQueryMeta queryMeta) {
    QueryReturnMeta returnMeta = createReturnMeta(queryMeta);
    EntityCtType entityCtType = queryMeta.getEntityCtType();
    if (entityCtType != null && entityCtType.isImmutable()) {
      if (!returnMeta.isMultiResult(entityCtType)) {
        throw new AptException(Message.DOMA4461, methodElement, new Object[] {});
      }
    } else {
      if (!returnMeta.isPrimitiveInt()) {
        throw new AptException(Message.DOMA4001, methodElement, new Object[] {});
      }
    }
    queryMeta.setReturnMeta(returnMeta);
  }

  @Override
  protected void doParameters(AutoMultiInsertQueryMeta queryMeta) {
    List<? extends VariableElement> parameters = methodElement.getParameters();
    int size = parameters.size();
    if (size != 1) {
      throw new AptException(Message.DOMA4002, methodElement, new Object[] {});
    }
    final QueryParameterMeta parameterMeta = createParameterMeta(parameters.get(0));
    IterableCtType iterableCtType =
        parameterMeta
            .getCtType()
            .accept(
                new SimpleCtTypeVisitor<IterableCtType, Void, RuntimeException>() {

                  @Override
                  protected IterableCtType defaultAction(CtType ctType, Void p)
                      throws RuntimeException {
                    throw new AptException(Message.DOMA4042, methodElement, new Object[] {});
                  }

                  @Override
                  public IterableCtType visitIterableCtType(IterableCtType ctType, Void p)
                      throws RuntimeException {
                    return ctType;
                  }
                },
                null);
    EntityCtType entityCtType =
        iterableCtType
            .getElementCtType()
            .accept(
                new SimpleCtTypeVisitor<EntityCtType, Void, RuntimeException>() {

                  @Override
                  protected EntityCtType defaultAction(CtType ctType, Void p)
                      throws RuntimeException {
                    throw new AptException(Message.DOMA4043, methodElement, new Object[] {});
                  }

                  @Override
                  public EntityCtType visitEntityCtType(EntityCtType ctType, Void p)
                      throws RuntimeException {
                    return ctType;
                  }
                },
                null);
    queryMeta.setEntityCtType(entityCtType);
    queryMeta.setEntityParameterName(parameterMeta.getName());
    queryMeta.addParameterMeta(parameterMeta);
    if (parameterMeta.isBindable()) {
      queryMeta.addBindableParameterCtType(parameterMeta.getName(), parameterMeta.getCtType());
    }
    MultiInsertAnnot insertAnnot = queryMeta.getMultiInsertAnnot();
    validateEntityPropertyNames(
        entityCtType.getType(),
        insertAnnot.getAnnotationMirror(),
        insertAnnot.getInclude(),
        insertAnnot.getExclude());
  }
}
