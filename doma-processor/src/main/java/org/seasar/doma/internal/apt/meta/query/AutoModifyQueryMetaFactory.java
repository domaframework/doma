package org.seasar.doma.internal.apt.meta.query;

import java.util.List;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import org.seasar.doma.internal.apt.AptException;
import org.seasar.doma.internal.apt.Context;
import org.seasar.doma.internal.apt.annot.ModifyAnnot;
import org.seasar.doma.internal.apt.annot.SqlAnnot;
import org.seasar.doma.internal.apt.cttype.CtType;
import org.seasar.doma.internal.apt.cttype.EntityCtType;
import org.seasar.doma.internal.apt.cttype.SimpleCtTypeVisitor;
import org.seasar.doma.message.Message;

public class AutoModifyQueryMetaFactory extends AbstractQueryMetaFactory<AutoModifyQueryMeta> {

  public AutoModifyQueryMetaFactory(
      Context ctx, TypeElement daoElement, ExecutableElement methodElement) {
    super(ctx, daoElement, methodElement);
  }

  @Override
  public QueryMeta createQueryMeta() {
    AutoModifyQueryMeta queryMeta = createAutoModifyQueryMeta();
    if (queryMeta == null) {
      return null;
    }
    doTypeParameters(queryMeta);
    doParameters(queryMeta);
    doReturnType(queryMeta);
    doThrowTypes(queryMeta);
    return queryMeta;
  }

  private AutoModifyQueryMeta createAutoModifyQueryMeta() {
    SqlAnnot sqlAnnot = ctx.getAnnotations().newSqlAnnot(methodElement);
    if (sqlAnnot != null) {
      return null;
    }
    AutoModifyQueryMeta queryMeta = new AutoModifyQueryMeta(daoElement, methodElement);
    ModifyAnnot modifyAnnot = ctx.getAnnotations().newInsertAnnot(methodElement);
    if (modifyAnnot != null && !modifyAnnot.getSqlFileValue()) {
      queryMeta.setModifyAnnot(modifyAnnot);
      queryMeta.setQueryKind(QueryKind.AUTO_INSERT);
      return queryMeta;
    }
    modifyAnnot = ctx.getAnnotations().newUpdateAnnot(methodElement);
    if (modifyAnnot != null && !modifyAnnot.getSqlFileValue()) {
      queryMeta.setModifyAnnot(modifyAnnot);
      queryMeta.setQueryKind(QueryKind.AUTO_UPDATE);
      return queryMeta;
    }
    modifyAnnot = ctx.getAnnotations().newDeleteAnnot(methodElement);
    if (modifyAnnot != null && !modifyAnnot.getSqlFileValue()) {
      queryMeta.setModifyAnnot(modifyAnnot);
      queryMeta.setQueryKind(QueryKind.AUTO_DELETE);
      return queryMeta;
    }
    return null;
  }

  @Override
  protected void doReturnType(AutoModifyQueryMeta queryMeta) {
    QueryReturnMeta returnMeta = createReturnMeta(queryMeta);
    EntityCtType entityCtType = queryMeta.getEntityCtType();
    if (entityCtType != null && entityCtType.isImmutable()) {
      if (!returnMeta.isResult(entityCtType)) {
        throw new AptException(Message.DOMA4222, methodElement, new Object[] {});
      }
    } else {
      if (!returnMeta.isPrimitiveInt()) {
        throw new AptException(Message.DOMA4001, methodElement, new Object[] {});
      }
    }
    queryMeta.setReturnMeta(returnMeta);
  }

  @Override
  protected void doParameters(AutoModifyQueryMeta queryMeta) {
    List<? extends VariableElement> parameters = methodElement.getParameters();
    int size = parameters.size();
    if (size != 1) {
      throw new AptException(Message.DOMA4002, methodElement, new Object[] {});
    }
    final QueryParameterMeta parameterMeta = createParameterMeta(parameters.get(0));
    EntityCtType entityCtType =
        parameterMeta
            .getCtType()
            .accept(
                new SimpleCtTypeVisitor<EntityCtType, Void, RuntimeException>() {

                  @Override
                  protected EntityCtType defaultAction(CtType type, Void p)
                      throws RuntimeException {
                    throw new AptException(
                        Message.DOMA4003, parameterMeta.getElement(), new Object[] {});
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
        modifyAnnot.getAnnotationMirror(),
        modifyAnnot.getInclude(),
        modifyAnnot.getExclude(),
        modifyAnnot.getDuplicateKeys());
  }
}
