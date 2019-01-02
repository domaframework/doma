package org.seasar.doma.internal.apt.meta.query;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import java.util.List;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.VariableElement;
import org.seasar.doma.internal.apt.AptException;
import org.seasar.doma.internal.apt.Context;
import org.seasar.doma.internal.apt.annot.ModifyAnnot;
import org.seasar.doma.internal.apt.cttype.CtType;
import org.seasar.doma.internal.apt.cttype.EntityCtType;
import org.seasar.doma.internal.apt.cttype.SimpleCtTypeVisitor;
import org.seasar.doma.internal.apt.meta.dao.DaoMeta;
import org.seasar.doma.message.Message;

public class AutoModifyQueryMetaFactory extends AbstractQueryMetaFactory<AutoModifyQueryMeta> {

  public AutoModifyQueryMetaFactory(Context ctx) {
    super(ctx);
  }

  @Override
  public QueryMeta createQueryMeta(ExecutableElement method, DaoMeta daoMeta) {
    assertNotNull(method, daoMeta);
    AutoModifyQueryMeta queryMeta = createAutoModifyQueryMeta(method, daoMeta);
    if (queryMeta == null) {
      return null;
    }
    doTypeParameters(queryMeta, method, daoMeta);
    doParameters(queryMeta, method, daoMeta);
    doReturnType(queryMeta, method, daoMeta);
    doThrowTypes(queryMeta, method, daoMeta);
    return queryMeta;
  }

  protected AutoModifyQueryMeta createAutoModifyQueryMeta(
      ExecutableElement method, DaoMeta daoMeta) {
    AutoModifyQueryMeta queryMeta = new AutoModifyQueryMeta(method, daoMeta.getDaoElement());
    ModifyAnnot modifyAnnot = ctx.getAnnotations().newInsertAnnot(method);
    if (modifyAnnot != null && !modifyAnnot.getSqlFileValue()) {
      queryMeta.setModifyAnnot(modifyAnnot);
      queryMeta.setQueryKind(QueryKind.AUTO_INSERT);
      return queryMeta;
    }
    modifyAnnot = ctx.getAnnotations().newUpdateAnnot(method);
    if (modifyAnnot != null && !modifyAnnot.getSqlFileValue()) {
      queryMeta.setModifyAnnot(modifyAnnot);
      queryMeta.setQueryKind(QueryKind.AUTO_UPDATE);
      return queryMeta;
    }
    modifyAnnot = ctx.getAnnotations().newDeleteAnnot(method);
    if (modifyAnnot != null && !modifyAnnot.getSqlFileValue()) {
      queryMeta.setModifyAnnot(modifyAnnot);
      queryMeta.setQueryKind(QueryKind.AUTO_DELETE);
      return queryMeta;
    }
    return null;
  }

  @Override
  protected void doReturnType(
      AutoModifyQueryMeta queryMeta, ExecutableElement method, DaoMeta daoMeta) {
    QueryReturnMeta returnMeta = createReturnMeta(queryMeta);
    EntityCtType entityCtType = queryMeta.getEntityCtType();
    if (entityCtType != null && entityCtType.isImmutable()) {
      if (!returnMeta.isResult(entityCtType)) {
        throw new AptException(
            Message.DOMA4222,
            returnMeta.getMethodElement(),
            new Object[] {daoMeta.getDaoElement().getQualifiedName(), method.getSimpleName()});
      }
    } else {
      if (!returnMeta.isPrimitiveInt()) {
        throw new AptException(
            Message.DOMA4001,
            returnMeta.getMethodElement(),
            new Object[] {daoMeta.getDaoElement().getQualifiedName(), method.getSimpleName()});
      }
    }
    queryMeta.setReturnMeta(returnMeta);
  }

  @Override
  protected void doParameters(
      AutoModifyQueryMeta queryMeta, final ExecutableElement method, DaoMeta daoMeta) {
    List<? extends VariableElement> parameters = method.getParameters();
    int size = parameters.size();
    if (size != 1) {
      throw new AptException(
          Message.DOMA4002,
          method,
          new Object[] {daoMeta.getDaoElement().getQualifiedName(), method.getSimpleName()});
    }
    final QueryParameterMeta parameterMeta = createParameterMeta(parameters.get(0), queryMeta);
    EntityCtType entityCtType =
        parameterMeta
            .getCtType()
            .accept(
                new SimpleCtTypeVisitor<EntityCtType, Void, RuntimeException>() {

                  @Override
                  protected EntityCtType defaultAction(CtType type, Void p)
                      throws RuntimeException {
                    throw new AptException(
                        Message.DOMA4003,
                        parameterMeta.getElement(),
                        new Object[] {
                          daoMeta.getDaoElement().getQualifiedName(), method.getSimpleName()
                        });
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
    ModifyAnnot modifyAnnot = queryMeta.getModifyAnnot();
    validateEntityPropertyNames(
        entityCtType.getType(),
        method,
        modifyAnnot.getAnnotationMirror(),
        modifyAnnot.getInclude(),
        modifyAnnot.getExclude());
  }
}
