package org.seasar.doma.internal.apt.meta.query;

import java.util.List;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.VariableElement;
import org.seasar.doma.internal.apt.AptException;
import org.seasar.doma.internal.apt.Context;
import org.seasar.doma.internal.apt.cttype.CtType;
import org.seasar.doma.internal.apt.cttype.EntityCtType;
import org.seasar.doma.internal.apt.cttype.SimpleCtTypeVisitor;
import org.seasar.doma.internal.apt.reflection.ModifyReflection;
import org.seasar.doma.message.Message;

public class AutoModifyQueryMetaFactory extends AbstractQueryMetaFactory<AutoModifyQueryMeta> {

  public AutoModifyQueryMetaFactory(Context ctx, ExecutableElement methodElement) {
    super(ctx, methodElement);
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
    AutoModifyQueryMeta queryMeta = new AutoModifyQueryMeta(methodElement);
    ModifyReflection modifyReflection = ctx.getReflections().newInsertReflection(methodElement);
    if (modifyReflection != null && !modifyReflection.getSqlFileValue()) {
      queryMeta.setModifyReflection(modifyReflection);
      queryMeta.setQueryKind(QueryKind.AUTO_INSERT);
      return queryMeta;
    }
    modifyReflection = ctx.getReflections().newUpdateReflection(methodElement);
    if (modifyReflection != null && !modifyReflection.getSqlFileValue()) {
      queryMeta.setModifyReflection(modifyReflection);
      queryMeta.setQueryKind(QueryKind.AUTO_UPDATE);
      return queryMeta;
    }
    modifyReflection = ctx.getReflections().newDeleteReflection(methodElement);
    if (modifyReflection != null && !modifyReflection.getSqlFileValue()) {
      queryMeta.setModifyReflection(modifyReflection);
      queryMeta.setQueryKind(QueryKind.AUTO_DELETE);
      return queryMeta;
    }
    return null;
  }

  @Override
  protected void doReturnType(AutoModifyQueryMeta queryMeta) {
    QueryReturnMeta returnMeta = createReturnMeta();
    EntityCtType entityCtType = queryMeta.getEntityCtType();
    if (entityCtType != null && entityCtType.isImmutable()) {
      if (!returnMeta.isResult(entityCtType)) {
        throw new AptException(Message.DOMA4222, returnMeta.getMethodElement());
      }
    } else {
      if (!returnMeta.isPrimitiveInt()) {
        throw new AptException(Message.DOMA4001, returnMeta.getMethodElement());
      }
    }
    queryMeta.setReturnMeta(returnMeta);
  }

  @Override
  protected void doParameters(AutoModifyQueryMeta queryMeta) {
    List<? extends VariableElement> parameters = methodElement.getParameters();
    int size = parameters.size();
    if (size != 1) {
      throw new AptException(Message.DOMA4002, methodElement);
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
                    throw new AptException(Message.DOMA4003, parameterMeta.getElement());
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
    ModifyReflection modifyReflection = queryMeta.getModifyReflection();
    validateEntityPropertyNames(
        entityCtType.getType(),
        modifyReflection.getAnnotationMirror(),
        modifyReflection.getInclude(),
        modifyReflection.getExclude());
  }
}
